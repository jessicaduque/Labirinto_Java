import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Labirinto {
    public List<List<String>> estruturaLabirinto;
    public Labirinto(String nomeCSV) {
        estruturaLabirinto = GetEstruturaLabirinto(nomeCSV);
        System.out.println(estruturaLabirinto);
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
}
