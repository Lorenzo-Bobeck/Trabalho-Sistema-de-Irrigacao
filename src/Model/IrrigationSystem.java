package Model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Controla o estado de uma irrigação simulada.
 */
public class IrrigationSystem {
    private double volumeAgua;
    private String horarioIrrigacao;
    private boolean emExecucao;
    private LocalDateTime ultimaAtivacao;

    public IrrigationSystem(double volumeAgua, String horarioIrrigacao) {
        setVolumeAgua(volumeAgua);
        setHorarioIrrigacao(horarioIrrigacao);
    }

    public String iniciarIrrigacao() {
        if (volumeAgua <= 0) {
            return "Irrigação não iniciada: o volume recomendado é zero.";
        }
        emExecucao = true;
        ultimaAtivacao = LocalDateTime.now();
        return String.format("Irrigação iniciada com %.2f L, programada para %s.",
                volumeAgua, horarioIrrigacao);
    }

    public String pararIrrigacao() {
        if (!emExecucao) {
            return "A irrigação já está parada.";
        }
        emExecucao = false;
        return "Irrigação encerrada.";
    }

    public double getVolumeAgua() {
        return volumeAgua;
    }

    public void setVolumeAgua(double volumeAgua) {
        if (!Double.isFinite(volumeAgua) || volumeAgua < 0) {
            throw new IllegalArgumentException("O volume de água não pode ser negativo.");
        }
        this.volumeAgua = volumeAgua;
    }

    public String getHorarioIrrigacao() {
        return horarioIrrigacao;
    }

    public void setHorarioIrrigacao(String horarioIrrigacao) {
        if (horarioIrrigacao == null || !horarioIrrigacao.matches("([01]\\d|2[0-3]):[0-5]\\d")) {
            throw new IllegalArgumentException("O horário deve estar no formato HH:mm.");
        }
        this.horarioIrrigacao = horarioIrrigacao;
    }

    public boolean isEmExecucao() {
        return emExecucao;
    }

    public String getUltimaAtivacaoFormatada() {
        if (ultimaAtivacao == null) {
            return "Nunca ativada";
        }
        return ultimaAtivacao.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
    }
}
