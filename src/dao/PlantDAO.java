package dao;

import Model.Plant;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * DAO responsável pelo CRUD de plantas e pela persistência em arquivo CSV.
 */
public class PlantDAO {
    private static final String CABECALHO = "id;nome;tipo;necessidadeHidrica";
    private final Path arquivo;

    public PlantDAO() {
        this(Paths.get("data", "plants.csv"));
    }

    public PlantDAO(Path arquivo) {
        if (arquivo == null) {
            throw new IllegalArgumentException("O caminho do arquivo é obrigatório.");
        }
        this.arquivo = arquivo;
    }

    public synchronized Plant salvar(Plant plant) throws IOException {
        validarPlant(plant);
        List<Plant> plantas = lerTodos();
        int proximoId = plantas.stream().mapToInt(Plant::getId).max().orElse(0) + 1;
        Plant salva = new Plant(proximoId, plant.getNome(), plant.getTipo(),
                plant.getNecessidadeHidrica());
        plantas.add(salva);
        gravarTodos(plantas);
        return copiar(salva);
    }

    public synchronized List<Plant> listar() throws IOException {
        return lerTodos().stream().map(this::copiar).toList();
    }

    public synchronized Optional<Plant> buscarPorId(int id) throws IOException {
        return lerTodos().stream()
                .filter(plant -> plant.getId() == id)
                .findFirst()
                .map(this::copiar);
    }

    public synchronized Plant atualizar(Plant plant) throws IOException {
        validarPlant(plant);
        if (plant.getId() <= 0) {
            throw new IllegalArgumentException("Selecione uma planta cadastrada para atualizar.");
        }

        List<Plant> plantas = lerTodos();
        boolean encontrada = false;
        for (int i = 0; i < plantas.size(); i++) {
            if (plantas.get(i).getId() == plant.getId()) {
                plantas.set(i, copiar(plant));
                encontrada = true;
                break;
            }
        }

        if (!encontrada) {
            throw new IllegalArgumentException("Planta não encontrada para atualização.");
        }

        gravarTodos(plantas);
        return copiar(plant);
    }

    public synchronized boolean excluir(int id) throws IOException {
        if (id <= 0) {
            throw new IllegalArgumentException("O identificador deve ser maior que zero.");
        }
        List<Plant> plantas = lerTodos();
        boolean removida = plantas.removeIf(plant -> plant.getId() == id);
        if (removida) {
            gravarTodos(plantas);
        }
        return removida;
    }

    private List<Plant> lerTodos() throws IOException {
        inicializarArquivo();
        List<Plant> plantas = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(arquivo, StandardCharsets.UTF_8)) {
            String linha;
            boolean primeiraLinha = true;
            while ((linha = reader.readLine()) != null) {
                if (primeiraLinha) {
                    primeiraLinha = false;
                    continue;
                }
                if (linha.isBlank()) {
                    continue;
                }
                List<String> campos = separarCsv(linha);
                if (campos.size() != 4) {
                    throw new IOException("Linha inválida no arquivo de persistência: " + linha);
                }
                try {
                    int id = Integer.parseInt(campos.get(0));
                    double necessidade = Double.parseDouble(campos.get(3));
                    plantas.add(new Plant(id, campos.get(1), campos.get(2), necessidade));
                } catch (IllegalArgumentException exception) {
                    throw new IOException("Dados inválidos no arquivo de persistência: " + linha,
                            exception);
                }
            }
        }
        return plantas;
    }

    private void gravarTodos(List<Plant> plantas) throws IOException {
        inicializarDiretorio();
        Path temporario = arquivo.resolveSibling(arquivo.getFileName() + ".tmp");

        try (BufferedWriter writer = Files.newBufferedWriter(temporario, StandardCharsets.UTF_8)) {
            writer.write(CABECALHO);
            writer.newLine();
            for (Plant plant : plantas) {
                writer.write(Integer.toString(plant.getId()));
                writer.write(';');
                writer.write(escaparCsv(plant.getNome()));
                writer.write(';');
                writer.write(escaparCsv(plant.getTipo()));
                writer.write(';');
                writer.write(Double.toString(plant.getNecessidadeHidrica()));
                writer.newLine();
            }
        }

        try {
            Files.move(temporario, arquivo, StandardCopyOption.REPLACE_EXISTING,
                    StandardCopyOption.ATOMIC_MOVE);
        } catch (IOException atomicMoveNaoSuportado) {
            Files.move(temporario, arquivo, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    private void inicializarArquivo() throws IOException {
        inicializarDiretorio();
        if (!Files.exists(arquivo)) {
            Files.writeString(arquivo, CABECALHO + System.lineSeparator(), StandardCharsets.UTF_8);
        }
    }

    private void inicializarDiretorio() throws IOException {
        Path parent = arquivo.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
    }

    private String escaparCsv(String valor) {
        return '"' + valor.replace("\"", "\"\"") + '"';
    }

    private List<String> separarCsv(String linha) throws IOException {
        List<String> campos = new ArrayList<>();
        StringBuilder atual = new StringBuilder();
        boolean entreAspas = false;

        for (int i = 0; i < linha.length(); i++) {
            char caractere = linha.charAt(i);
            if (caractere == '"') {
                if (entreAspas && i + 1 < linha.length() && linha.charAt(i + 1) == '"') {
                    atual.append('"');
                    i++;
                } else {
                    entreAspas = !entreAspas;
                }
            } else if (caractere == ';' && !entreAspas) {
                campos.add(atual.toString());
                atual.setLength(0);
            } else {
                atual.append(caractere);
            }
        }

        if (entreAspas) {
            throw new IOException("Aspas não finalizadas no arquivo CSV.");
        }
        campos.add(atual.toString());
        return campos;
    }

    private void validarPlant(Plant plant) {
        if (plant == null) {
            throw new IllegalArgumentException("A planta é obrigatória.");
        }
    }

    private Plant copiar(Plant plant) {
        return new Plant(plant.getId(), plant.getNome(), plant.getTipo(),
                plant.getNecessidadeHidrica());
    }

    public Path getArquivo() {
        return arquivo;
    }
}
