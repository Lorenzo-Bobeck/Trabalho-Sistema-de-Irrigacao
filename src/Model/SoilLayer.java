package Model;

import exception.UmidadeInvalidaException;

/**
 * Representa uma camada do solo e sua capacidade de armazenamento de água.
 */
public class SoilLayer {
    private double profundidade;
    private double umidadeAtual;
    private double capacidadeRetencao;

    public SoilLayer(double profundidade, double umidadeAtual, double capacidadeRetencao)
            throws UmidadeInvalidaException {
        setProfundidade(profundidade);
        setCapacidadeRetencao(capacidadeRetencao);
        setUmidadeAtual(umidadeAtual);
    }

    public double calcularInfiltracao() {
        return Math.max(0, capacidadeRetencao - umidadeAtual);
    }

    public double calcularPercentualDeficit() {
        if (capacidadeRetencao == 0) {
            return 0;
        }
        return calcularInfiltracao() / capacidadeRetencao * 100.0;
    }

    public double getProfundidade() {
        return profundidade;
    }

    public void setProfundidade(double profundidade) {
        if (!Double.isFinite(profundidade) || profundidade <= 0) {
            throw new IllegalArgumentException("A profundidade deve ser maior que zero.");
        }
        this.profundidade = profundidade;
    }

    public double getUmidadeAtual() {
        return umidadeAtual;
    }

    public void setUmidadeAtual(double umidadeAtual) throws UmidadeInvalidaException {
        validarPercentual(umidadeAtual, "A umidade atual");
        if (umidadeAtual > capacidadeRetencao) {
            throw new UmidadeInvalidaException(
                    "A umidade atual não pode ser maior que a capacidade de retenção.");
        }
        this.umidadeAtual = umidadeAtual;
    }

    public double getCapacidadeRetencao() {
        return capacidadeRetencao;
    }

    public void setCapacidadeRetencao(double capacidadeRetencao)
            throws UmidadeInvalidaException {
        validarPercentual(capacidadeRetencao, "A capacidade de retenção");
        if (capacidadeRetencao == 0) {
            throw new UmidadeInvalidaException("A capacidade de retenção deve ser maior que zero.");
        }
        if (umidadeAtual > capacidadeRetencao) {
            throw new UmidadeInvalidaException(
                    "A capacidade de retenção não pode ser menor que a umidade atual.");
        }
        this.capacidadeRetencao = capacidadeRetencao;
    }

    private void validarPercentual(double valor, String campo) throws UmidadeInvalidaException {
        if (!Double.isFinite(valor) || valor < 0 || valor > 100) {
            throw new UmidadeInvalidaException(campo + " deve estar entre 0% e 100%.");
        }
    }
}
