package Model;

import java.util.Objects;

/**
 * Representa uma cultura cadastrada no sistema.
 */
public class Plant implements Monitoravel {
    private int id;
    private String nome;
    private String tipo;
    private double necessidadeHidrica;

    public Plant(String nome, String tipo, double necessidadeHidrica) {
        this(0, nome, tipo, necessidadeHidrica);
    }

    public Plant(int id, String nome, String tipo, double necessidadeHidrica) {
        setId(id);
        setNome(nome);
        setTipo(tipo);
        setNecessidadeHidrica(necessidadeHidrica);
    }

    /**
     * Consumo-base em litros por ciclo de irrigação.
     */
    public double calcularConsumoAgua() {
        return necessidadeHidrica;
    }

    /**
     * Ajusta o consumo-base conforme a evapotranspiração estimada.
     */
    public double calcularConsumoAgua(double evapotranspiracao) {
        if (evapotranspiracao < 0) {
            throw new IllegalArgumentException("A evapotranspiração não pode ser negativa.");
        }
        return necessidadeHidrica * (1.0 + evapotranspiracao / 10.0);
    }

    @Override
    public String monitorar() {
        return String.format("Planta monitorada: %s (%s), necessidade hídrica %.2f L.",
                nome, tipo, necessidadeHidrica);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        if (id < 0) {
            throw new IllegalArgumentException("O identificador não pode ser negativo.");
        }
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O nome da planta é obrigatório.");
        }
        this.nome = nome.trim();
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        if (tipo == null || tipo.isBlank()) {
            throw new IllegalArgumentException("O tipo da planta é obrigatório.");
        }
        this.tipo = tipo.trim();
    }

    public double getNecessidadeHidrica() {
        return necessidadeHidrica;
    }

    public void setNecessidadeHidrica(double necessidadeHidrica) {
        if (!Double.isFinite(necessidadeHidrica) || necessidadeHidrica <= 0) {
            throw new IllegalArgumentException("A necessidade hídrica deve ser maior que zero.");
        }
        this.necessidadeHidrica = necessidadeHidrica;
    }

    @Override
    public String toString() {
        return nome + " - " + tipo;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof Plant other)) {
            return false;
        }
        return id > 0 && id == other.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
