import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Labirinto {
    private List<List<String>> estruturaLabirinto;
    private int largura, altura;

    public Labirinto(String nomeCSV) {
        this.estruturaLabirinto = GetEstruturaLabirinto(nomeCSV);

        if (this.estruturaLabirinto.isEmpty()) {
            System.out.println("O arquivo CSV está vazio ou não pôde ser lido.");
            return;
        }

        this.largura = this.estruturaLabirinto.get(0).size();
        this.altura = this.estruturaLabirinto.size();

        try {
            boolean caminhoEncontrado = DescobrirRotaLabirinto();
            if (!caminhoEncontrado) {
                System.out.println("Nenhum caminho para a saída encontrado.");
            }
        } catch (FaltaEspaco e) {
            throw new RuntimeException(e);
        } catch (FaltaElemento e) {
            throw new RuntimeException(e);
        }
        ImprimirLabirinto();
    }

    private List<List<String>> GetEstruturaLabirinto(String nomeCSV) {
        List<List<String>> records = new ArrayList<>();
        URL csvPath = getClass().getResource("/" + nomeCSV + ".csv");
        if (csvPath == null) {
            System.out.println("O arquivo CSV especificado não pôde ser encontrado.");
            return records;
        }

        File csvFile = new File(csvPath.getFile());
        try (Scanner scanner = new Scanner(csvFile)) {
            while (scanner.hasNextLine()) {
                List<String> linha = GetLinhaLabirinto(scanner.nextLine());
                records.add(linha);
            }
        } catch (FileNotFoundException e) {
            System.out.println("CSV especificado não pôde ser encontrado.");
        }
        return records;
    }

    private List<String> GetLinhaLabirinto(String line) {
        List<String> values = new ArrayList<>();
        try (Scanner rowScanner = new Scanner(line)) {
            rowScanner.useDelimiter(",");  // Alterar para vírgula como delimitador
            while (rowScanner.hasNext()) {
                values.add(rowScanner.next());
            }
        }
        return values;
    }

    // Função principal para descobrir a rota
    private boolean DescobrirRotaLabirinto() throws FaltaEspaco, FaltaElemento {
        PilhaS<int[]> pilha = new PilhaS<>(largura * altura);
        int[] entrada = {0, 0}; // Ponto de entrada
        pilha.empilhe(entrada);

        while (!pilha.vazia()) {
            // Para testes

//            ImprimirLabirinto();
//            System.out.println("___");

            int[] posicaoAtual = pilha.getValorTopo();
            int x = posicaoAtual[0];
            int y = posicaoAtual[1];

            // Verifica se a posição atual é um caminho (1)
            if (estruturaLabirinto.get(y).get(x).equals("1")) {
                // Verifica se é a saída (última célula)
                if (x == largura - 1 && y == altura - 1) {
                    estruturaLabirinto.get(y).set(x, "X"); // Marca a saída
                    return true; // Caminho encontrado
                }

                // Marca a célula como parte do caminho
                estruturaLabirinto.get(y).set(x, "X");

                // Empilha os próximos passos, com prioridade para "baixo" e "direita"
                if (EmpilharProximoPasso(pilha, x, y)) {
                    continue; // Se empilhou algum passo, continua para a próxima iteração
                } else {
                    // Se não há mais passos, desmarcar se não estamos perto da saída
                    if (!EhCaminhoFinal(x, y)) {
                        estruturaLabirinto.get(y).set(x, "0");
                    }
                }
            }
            else{
                System.out.println("a");
            }
        }
        return false; // Nenhum caminho para a saída encontrado
    }

    private boolean EhCaminhoFinal(int x, int y) {
        // Verifica se estamos no caminho final ou na saída
        return x == largura - 1 || y == altura - 1 || x == 0 || y == 0;
    }

    private boolean EmpilharProximoPasso(PilhaS<int[]> pilha, int x, int y) throws FaltaEspaco {
        boolean passoAdicionado = false;

        // Empilha com prioridade em Baixo e Direita
        if (ExisteProximoPasso(x, y + 1)) { // Baixo
            pilha.empilhe(new int[]{x, y + 1});
            passoAdicionado = true;
        }
        if (ExisteProximoPasso(x + 1, y)) { // Direita
            pilha.empilhe(new int[]{x + 1, y});
            passoAdicionado = true;
        }
        if (ExisteProximoPasso(x, y - 1)) { // Cima
            pilha.empilhe(new int[]{x, y - 1});
            passoAdicionado = true;
        }
        if (ExisteProximoPasso(x - 1, y)) { // Esquerda
            pilha.empilhe(new int[]{x - 1, y});
            passoAdicionado = true;
        }
        if (ExisteProximoPasso(x + 1, y + 1)) { // Cima direita
            pilha.empilhe(new int[]{x + 1, y + 1});
            passoAdicionado = true;
        }
        if (ExisteProximoPasso(x + 1, y - 1)) { // Baixo direita
            pilha.empilhe(new int[]{x + 1, y - 1});
            passoAdicionado = true;
        }
        if (ExisteProximoPasso(x - 1, y + 1)) { // Cima esquerda
            pilha.empilhe(new int[]{x - 1, y + 1});
            passoAdicionado = true;
        }
        if (ExisteProximoPasso(x - 1, y - 1)) { // Baixo esquerda
            pilha.empilhe(new int[]{x - 1, y - 1});
            passoAdicionado = true;
        }

        return passoAdicionado; // Retorna se algum passo foi empilhado
    }

    private boolean ExisteProximoPasso(int x, int y) {
        // Verifica se a posição está dentro dos limites e é um caminho (1)
        return (x >= 0 && x < largura && y >= 0 && y < altura && estruturaLabirinto.get(y).get(x).equals("1"));
    }

    private void ImprimirLabirinto() {
        for (List<String> linha : estruturaLabirinto) {
            System.out.println(String.join("\t", linha));
        }
    }
}
