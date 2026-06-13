package Model;

import java.util.ArrayList;
import java.util.List;

public class SoilProfile {

    private List<SoilLayer> listaCamadas;

    public SoilProfile() {
        listaCamadas = new ArrayList<>();
    }

    public void adicionarCamada(SoilLayer camada) {
        listaCamadas.add(camada);
    }

    public double calcularBalancoHidrico() {
        double total = 0;

        for (SoilLayer camada : listaCamadas) {
            total += camada.getUmidadeAtual();
        }

        return total;
    }

    public List<SoilLayer> getListaCamadas() {
        return listaCamadas;
    }
}
