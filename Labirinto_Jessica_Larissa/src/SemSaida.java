public class SemSaida extends RuntimeException {
    public SemSaida(String message) {
        super("Labirinto não possui uma rota de saída.");
    }
}
