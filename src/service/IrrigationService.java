package service;

import Model.Plant;
import Model.SoilProfile;
import Model.Weather;

/**
 * Centraliza as regras de negócio usadas na recomendação de irrigação.
 */
public class IrrigationService {

    public IrrigationRecommendation calcularRecomendacao(Plant plant, Weather weather,
            SoilProfile soilProfile) {
        if (plant == null || weather == null || soilProfile == null) {
            throw new IllegalArgumentException("Planta, clima e perfil do solo são obrigatórios.");
        }
        if (soilProfile.getListaCamadas().isEmpty()) {
            throw new IllegalArgumentException("Adicione ao menos uma camada ao perfil do solo.");
        }

        double evapotranspiracao = weather.calcularEvapotranspiracao();
        double umidadeMedia = soilProfile.calcularBalancoHidrico();
        double deficitMedio = soilProfile.calcularDeficitMedio();
        double consumoAjustado = plant.calcularConsumoAgua(evapotranspiracao);

        double fatorDeficit = deficitMedio / 100.0;
        double compensacaoChuva = weather.getPrecipitacao() * 0.8;
        double volume = Math.max(0, consumoAjustado * fatorDeficit - compensacaoChuva);

        String justificativa;
        if (volume == 0) {
            justificativa = "A umidade do solo e/ou a precipitação informada não indicam necessidade de irrigação.";
        } else if (deficitMedio >= 50) {
            justificativa = "O solo apresenta déficit elevado; a irrigação é recomendada.";
        } else {
            justificativa = "O solo apresenta déficit moderado; recomenda-se irrigação controlada.";
        }

        return new IrrigationRecommendation(evapotranspiracao, umidadeMedia, deficitMedio,
                volume, justificativa);
    }
}
