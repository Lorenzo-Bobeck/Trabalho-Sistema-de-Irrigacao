package Model;

import exception.UmidadeInvalidaException;

/**
 * Dados climáticos usados na simulação de irrigação.
 */
public class Weather implements Monitoravel {
    private double temperatura;
    private double umidade;
    private double precipitacao;

    public Weather(double temperatura, double umidade, double precipitacao)
            throws UmidadeInvalidaException {
        setTemperatura(temperatura);
        setUmidade(umidade);
        setPrecipitacao(precipitacao);
    }

    /**
     * Estimativa didática de evapotranspiração em mm/dia.
     * Não substitui um cálculo agronômico de campo.
     */
    public double calcularEvapotranspiracao() {
        double fatorTemperatura = Math.max(0, temperatura) * 0.15;
        double fatorSecuraDoAr = (100.0 - umidade) * 0.03;
        double efeitoPrecipitacao = precipitacao * 0.05;
        return Math.max(0, fatorTemperatura + fatorSecuraDoAr - efeitoPrecipitacao);
    }

    @Override
    public String monitorar() {
        return String.format("Clima: %.1f °C, umidade do ar %.1f%% e precipitação %.1f mm.",
                temperatura, umidade, precipitacao);
    }

    public double getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(double temperatura) {
        if (!Double.isFinite(temperatura) || temperatura < -50 || temperatura > 70) {
            throw new IllegalArgumentException("A temperatura deve estar entre -50 °C e 70 °C.");
        }
        this.temperatura = temperatura;
    }

    public double getUmidade() {
        return umidade;
    }

    public void setUmidade(double umidade) throws UmidadeInvalidaException {
        if (!Double.isFinite(umidade) || umidade < 0 || umidade > 100) {
            throw new UmidadeInvalidaException("A umidade do ar deve estar entre 0% e 100%.");
        }
        this.umidade = umidade;
    }

    public double getPrecipitacao() {
        return precipitacao;
    }

    public void setPrecipitacao(double precipitacao) {
        if (!Double.isFinite(precipitacao) || precipitacao < 0) {
            throw new IllegalArgumentException("A precipitação não pode ser negativa.");
        }
        this.precipitacao = precipitacao;
    }
}
