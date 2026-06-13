package test;

import Model.Plant;
import Model.SoilLayer;
import Model.SoilProfile;
import Model.Weather;
import dao.PlantDAO;
import service.IrrigationRecommendation;
import service.IrrigationService;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Teste de integração simples, sem dependências externas.
 */
public class TesteSistema {

    public static void main(String[] args) throws Exception {
        Path directory = Files.createTempDirectory("irrigacao-test-");
        Path file = directory.resolve("plants.csv");
        PlantDAO dao = new PlantDAO(file);

        Plant milho = dao.salvar(new Plant("Milho", "Grão", 50));
        Plant tomate = dao.salvar(new Plant("Tomate", "Hortaliça", 35));
        assertTrue(milho.getId() == 1, "O primeiro ID deve ser 1.");
        assertTrue(tomate.getId() == 2, "O segundo ID deve ser 2.");
        assertTrue(dao.listar().size() == 2, "Duas plantas devem estar persistidas.");

        PlantDAO novoDao = new PlantDAO(file);
        assertTrue(novoDao.listar().size() == 2,
                "Uma nova instância do DAO deve recuperar os dados do arquivo.");

        milho.setNecessidadeHidrica(55);
        dao.atualizar(milho);
        assertTrue(dao.buscarPorId(milho.getId()).orElseThrow()
                .getNecessidadeHidrica() == 55, "A atualização deve ser persistida.");

        assertTrue(dao.excluir(tomate.getId()), "A exclusão deve retornar verdadeiro.");
        assertTrue(dao.listar().size() == 1, "Deve restar uma planta após a exclusão.");

        Weather weather = new Weather(30, 65, 0);
        SoilProfile profile = new SoilProfile();
        profile.adicionarCamada(new SoilLayer(20, 40, 90));
        IrrigationRecommendation recommendation = new IrrigationService()
                .calcularRecomendacao(milho, weather, profile);
        assertTrue(recommendation.getVolumeRecomendado() > 0,
                "A simulação deve recomendar um volume positivo neste cenário.");

        boolean umidadeInvalidaDetectada = false;
        try {
            new Weather(25, 120, 0);
        } catch (exception.UmidadeInvalidaException expected) {
            umidadeInvalidaDetectada = true;
        }
        assertTrue(umidadeInvalidaDetectada,
                "A exceção personalizada deve rejeitar umidade acima de 100%.");

        System.out.println("TODOS OS TESTES PASSARAM");
        System.out.println("Arquivo temporário testado: " + file);
    }

    private static void assertTrue(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
