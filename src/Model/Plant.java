package Model;

public class Plant implements Monitoravel {

   private String nome;
   private String tipo;
   private int necessidadeHidrica;

   public Plant(String nome, String tipo, int necessidadeHidrica) {
       this.nome = nome;
       this.tipo = tipo;
       this.necessidadeHidrica = necessidadeHidrica;
   }

   public int calcularConsumoAgua() {
       return necessidadeHidrica * 2;
   }

   @Override
   public void monitorar() {
       System.out.println("Monitorando planta: " + nome);
   }

   public String getNome() {
       return nome;
   }

   public void setNome(String nome) {
       this.nome = nome;
   }

   public String getTipo() {
       return tipo;
   }

   public void setTipo(String tipo) {
       this.tipo = tipo;
   }

   public int getNecessidadeHidrica() {
       return necessidadeHidrica;
   }

   public void setNecessidadeHidrica(int necessidadeHidrica) {
       this.necessidadeHidrica = necessidadeHidrica;
   }
}