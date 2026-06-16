package exception;

/**
 * Exceção de domínio usada quando um percentual de umidade é inválido.
 */
public class UmidadeInvalidaException extends Exception {
    private static final long serialVersionUID = 1L;

    public UmidadeInvalidaException(String mensagem) {
        super(mensagem);
    }
}
