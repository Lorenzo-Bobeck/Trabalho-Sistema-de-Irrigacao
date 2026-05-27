package Model;

public class IrrigationSystem {

    private double volumeAgua;
    private String horarioIrrigacao;

    public IrrigationSystem(double volumeAgua, String horarioIrrigacao) {
        this.volumeAgua = volumeAgua;
        this.horarioIrrigacao = horarioIrrigacao;
    }

    public void iniciarIrrigacao() {
        System.out.println("Irrigação iniciada.");
    }

    public void pararIrrigacao() {
        System.out.println("Irrigação encerrada.");
    }

    public double getVolumeAgua() {
        return volumeAgua;
    }

    public void setVolumeAgua(double volumeAgua) {
        this.volumeAgua = volumeAgua;
    }

    public String getHorarioIrrigacao() {
        return horarioIrrigacao;
    }

    public void setHorarioIrrigacao(String horarioIrrigacao) {
        this.horarioIrrigacao = horarioIrrigacao;
    }
}