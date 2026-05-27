package Model;

public class Weather implements Monitoravel {

    private double temperatura;
    private double umidade;
    private double precipitacao;

    public Weather(double temperatura, double umidade, double precipitacao) {
        this.temperatura = temperatura;
        this.umidade = umidade;
        this.precipitacao = precipitacao;
    }

    public double calcularEvapotranspiracao() {
        return temperatura * 0.5;
    }

    @Override
    public void monitorar() {
        System.out.println("Monitorando condições climáticas...");
    }

    public double getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(double temperatura) {
        this.temperatura = temperatura;
    }

    public double getUmidade() {
        return umidade;
    }

    public void setUmidade(double umidade) {
        this.umidade = umidade;
    }

    public double getPrecipitacao() {
        return precipitacao;
    }

    public void setPrecipitacao(double precipitacao) {
        this.precipitacao = precipitacao;
    }
}