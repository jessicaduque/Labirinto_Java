public class FaltaEspaco extends Exception{
    public FaltaEspaco(String message) {
        super("Uma pilha sendo utilizada está cheia, então não é possível empilhar mais."); // Passa a mensagem para a superclasse
    }
}