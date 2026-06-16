package Model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Perfil de solo composto por uma ou mais camadas.
 */
public class SoilProfile {
    private final List<SoilLayer> listaCamadas = new ArrayList<>();

    public void adicionarCamada(SoilLayer camada) {
        if (camada == null) {
            throw new IllegalArgumentException("A camada do solo não pode ser nula.");
        }
        listaCamadas.add(camada);
    }

    public void removerCamada(SoilLayer camada) {
        listaCamadas.remove(camada);
    }

    /**
     * Retorna a umidade média das camadas do perfil.
     */
    public double calcularBalancoHidrico() {
        if (listaCamadas.isEmpty()) {
            return 0;
        }
        return listaCamadas.stream()
                .mapToDouble(SoilLayer::getUmidadeAtual)
                .average()
                .orElse(0);
    }

    public double calcularDeficitMedio() {
        if (listaCamadas.isEmpty()) {
            return 0;
        }
        return listaCamadas.stream()
                .mapToDouble(SoilLayer::calcularPercentualDeficit)
                .average()
                .orElse(0);
    }

    public List<SoilLayer> getListaCamadas() {
        return Collections.unmodifiableList(listaCamadas);
    }
}
