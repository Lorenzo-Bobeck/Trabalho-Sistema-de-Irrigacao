package main;

import Model.IrrigationSystem;
import Model.Plant;
import Model.SoilLayer;
import Model.SoilProfile;
import Model.Weather;
import dao.PlantDAO;
import exception.UmidadeInvalidaException;
import service.IrrigationRecommendation;
import service.IrrigationService;
import view.TelaPrincipal;

import java.awt.EventQueue;
import java.io.IOException;
import java.util.List;
import javax.swing.UIManager;

public class Main {

    public static void main(String[] args) {
        if (args.length > 0 && "--demo".equalsIgnoreCase(args[0])) {
            executarDemonstracaoConsole();
            return;
        }

        configurarAparencia();
        EventQueue.invokeLater(() -> {
            try {
                new TelaPrincipal(new PlantDAO()).setVisible(true);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });
    }

    private static void configurarAparencia() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ignored) {
            // Mantém o tema padrão quando Nimbus não estiver disponível.
        }
    }

    private static void executarDemonstracaoConsole() {
        try {
            PlantDAO dao = new PlantDAO();
            List<Plant> plantas = dao.listar();
            Plant planta;
            if (plantas.isEmpty()) {
                planta = dao.salvar(new Plant("Milho", "Grão", 50));
                System.out.println("Planta de demonstração cadastrada e persistida.");
            } else {
                planta = plantas.get(0);
            }

            Weather clima = new Weather(30, 70, 10);
            SoilProfile perfil = new SoilProfile();
            perfil.adicionarCamada(new SoilLayer(20, 45, 90));

            IrrigationRecommendation resultado = new IrrigationService()
                    .calcularRecomendacao(planta, clima, perfil);
            IrrigationSystem irrigacao = new IrrigationSystem(
                    resultado.getVolumeRecomendado(), "08:00");

            System.out.println(planta.monitorar());
            System.out.println(clima.monitorar());
            System.out.printf("Evapotranspiração estimada: %.2f mm/dia%n",
                    resultado.getEvapotranspiracao());
            System.out.printf("Umidade média do solo: %.2f%%%n",
                    resultado.getUmidadeMediaSolo());
            System.out.printf("Déficit médio do solo: %.2f%%%n",
                    resultado.getDeficitMedioSolo());
            System.out.printf("Volume recomendado: %.2f L%n",
                    resultado.getVolumeRecomendado());
            System.out.println(resultado.getJustificativa());
            System.out.println(irrigacao.iniciarIrrigacao());
        } catch (IOException | UmidadeInvalidaException | IllegalArgumentException exception) {
            System.err.println("Não foi possível executar a demonstração: " + exception.getMessage());
        }
    }
}
