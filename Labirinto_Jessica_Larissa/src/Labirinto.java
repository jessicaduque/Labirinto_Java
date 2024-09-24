import java.util.List;
import java.util.Scanner;

public class Labirinto {
    public Labirinto(String nomeCSV) {
    }
    public List<String> estruturaLabirinto;
    private List<String> GetEstruturaLabirinto(String nomeCSV) {
        List<List<String>> records = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File("{nomeCSV}.csv"))) {
            while (scanner.hasNextLine()) {
                records.add(getRecordFromLine(scanner.nextLine()));
            }
        }

        private List<String> getRecordFromLine(String line) {
            List<String> values = new ArrayList<String>();
            try (Scanner rowScanner = new Scanner(line)) {
                rowScanner.useDelimiter(COMMA_DELIMITER);
                while (rowScanner.hasNext()) {
                    values.add(rowScanner.next());
                }
            }
            return values;
        }
    }
}
