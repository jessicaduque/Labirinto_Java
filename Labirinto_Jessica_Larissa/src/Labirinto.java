import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Labirinto {
    private List<List<String>> estruturaLabirinto;
    private int largura, altura;
    public Labirinto(String nomeCSV) {
        this.estruturaLabirinto = GetEstruturaLabirinto(nomeCSV);
        this.largura = this.estruturaLabirinto.getFirst().size();
        this.altura = this.estruturaLabirinto.size();
    }
    private List<List<String>> GetEstruturaLabirinto(String nomeCSV) {
        List<List<String>> records = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File(nomeCSV + ".csv"))) {
            while (scanner.hasNextLine()) {
                records.add(GetLinhaLabirinto(scanner.nextLine()));
            }
        }
        catch (FileNotFoundException e) {
            System.out.println("CSV especificado não pôde ser encontrado.");
        }
        return records;
    }
    private List<String> GetLinhaLabirinto(String line) {
        List<String> values = new ArrayList<String>();
        try (Scanner rowScanner = new Scanner(line)) {
            rowScanner.useDelimiter(",");
            while (rowScanner.hasNext()) {
                values.add(rowScanner.next());
            }
        }
        return values;
    }
    private void DescobrirRotaLabirinto(){
        List<List<String>> estruturaComRota = this.estruturaLabirinto;
        int[] entradaLabirinto = new int[2];
        // Pegar entrada a partir do usuário
        int[] passoAtual = entradaLabirinto;
        boolean encontrouSaida = false;
        while(!encontrouSaida){

        }
        // Função para botar bonito a estrutura e imprim-lo
    }

    private boolean ExisteProximoPasso(){
        return false;
    }

    private int[] GetProximoPasso(){
        return null;
    }
}
