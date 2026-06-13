package service;

/**
 * Resultado imutável de uma simulação de irrigação.
 */
public class IrrigationRecommendation {
    private final double evapotranspiracao;
    private final double umidadeMediaSolo;
    private final double deficitMedioSolo;
    private final double volumeRecomendado;
    private final String justificativa;

    public IrrigationRecommendation(double evapotranspiracao, double umidadeMediaSolo,
            double deficitMedioSolo, double volumeRecomendado, String justificativa) {
        this.evapotranspiracao = evapotranspiracao;
        this.umidadeMediaSolo = umidadeMediaSolo;
        this.deficitMedioSolo = deficitMedioSolo;
        this.volumeRecomendado = volumeRecomendado;
        this.justificativa = justificativa;
    }

    public double getEvapotranspiracao() {
        return evapotranspiracao;
    }

    public double getUmidadeMediaSolo() {
        return umidadeMediaSolo;
    }

    public double getDeficitMedioSolo() {
        return deficitMedioSolo;
    }

    public double getVolumeRecomendado() {
        return volumeRecomendado;
    }

    public String getJustificativa() {
        return justificativa;
    }
}
