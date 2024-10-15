import javax.swing.*;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        try {
            SwingUtilities.invokeLater(() -> {
                Labirinto _labirinto = new Labirinto("labirinto");
            });
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }
}