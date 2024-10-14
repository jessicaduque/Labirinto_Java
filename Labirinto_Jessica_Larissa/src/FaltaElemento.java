public class FaltaElemento extends Exception{

    public FaltaElemento(String message) {
        super("Uma pilha sendo utilizada está vazia."); // Passa a mensagem para a superclasse
    }
}