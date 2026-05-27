package Model;

public class SoilLayer {

    private double profundidade;
    private double umidadeAtual;
    private double capacidadeRetencao;

    public SoilLayer(double profundidade, double umidadeAtual, double capacidadeRetencao) {
        this.profundidade = profundidade;
        this.umidadeAtual = umidadeAtual;
        this.capacidadeRetencao = capacidadeRetencao;
    }

    public double calcularInfiltracao() {
        return capacidadeRetencao - umidadeAtual;
    }

    public double getProfundidade() {
        return profundidade;
    }

    public void setProfundidade(double profundidade) {
        this.profundidade = profundidade;
    }

    public double getUmidadeAtual() {
        return umidadeAtual;
    }

    public void setUmidadeAtual(double umidadeAtual) {
        this.umidadeAtual = umidadeAtual;
    }

    public double getCapacidadeRetencao() {
        return capacidadeRetencao;
    }

    public void setCapacidadeRetencao(double capacidadeRetencao) {
        this.capacidadeRetencao = capacidadeRetencao;
    }
}