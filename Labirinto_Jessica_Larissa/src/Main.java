//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        try {
            Labirinto _labirinto = new Labirinto("labirinto");
            _labirinto.DescobrirRotaLabirinto();
            _labirinto.ImprimirLabirintoTexto();
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }
}