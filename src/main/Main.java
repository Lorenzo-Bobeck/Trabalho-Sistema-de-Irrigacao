package main;

import Model.Plant;
import Model.Weather;
import Model.SoilLayer;
import Model.SoilProfile;
import Model.IrrigationSystem;
import dao.PlantDAO;
import view.TelaPrincipal;

public class Main {

    public static void main(String[] args) {

        Weather clima = new Weather(30, 70, 10);
        Plant planta = new Plant("Milho", "Grão", 50);

        SoilLayer camada = new SoilLayer(20, 60, 90);

        SoilProfile perfil = new SoilProfile();
        perfil.adicionarCamada(camada);

        IrrigationSystem irrigacao = new IrrigationSystem(1000, "08:00");

        PlantDAO dao = new PlantDAO();
        dao.salvar(planta);

        System.out.println("Evapotranspiração: " + clima.calcularEvapotranspiracao());
        System.out.println("Consumo de água: " + planta.calcularConsumoAgua());
        System.out.println("Balanço hídrico: " + perfil.calcularBalancoHidrico());

        irrigacao.iniciarIrrigacao();

        TelaPrincipal tela = new TelaPrincipal();
        tela.setVisible(true);
    }
}
