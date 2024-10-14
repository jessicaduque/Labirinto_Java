import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Labirinto {
    private List<List<String>> estruturaLabirinto; // Matriz de strings para guardar a estrutura original do labirinto
    private List<List<String>> estruturaLabirintoComRota;
    private int largura, altura; // Largura e altura original do labirinto

    // Construtor que lê um arquivo csv para determinar a estrutura, altura e largura do labirinto
    public Labirinto(String nomeCSV) {
        this.estruturaLabirinto = GetEstruturaLabirinto(nomeCSV);
        this.estruturaLabirintoComRota = this.estruturaLabirinto;
        this.largura = this.estruturaLabirinto.getFirst().size();
        this.altura = this.estruturaLabirinto.size();
    }

    // Função para conseguir ler a estrutura do labirinto a partir do csv
    private List<List<String>> GetEstruturaLabirinto(String nomeCSV) {
        List<List<String>> records = new ArrayList<>();

        try (Scanner scanner = new Scanner(new File(nomeCSV + ".csv"))) {
            while (scanner.hasNextLine()) {
                List<String> linha = GetLinhaLabirinto(scanner.nextLine());
                records.add(linha);
            }
        } catch (FileNotFoundException e) {
            System.out.println("CSV especificado não pôde ser encontrado.");
        }
        return records;
    }

    // Função que retorna cada linha lida no csv do labirinto para formar sua estrutura
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

    // Função que descobre a rota para a saída do labirinto
    public void DescobrirRotaLabirinto() throws FaltaEspaco, FaltaElemento, SemSaida {
        // Variáveis
        PilhaS<int[]> pilhaPassosAvancados = new PilhaS<>(largura * altura);
        int[] entrada = {0, 0}; // Ponto de entrada
        int[] posicaoAtual = entrada;

        pilhaPassosAvancados.empilhe(entrada);
        estruturaLabirintoComRota.get(posicaoAtual[1]).set(posicaoAtual[0], "X");

        while (true) {
            int posX = posicaoAtual[0];
            int posY = posicaoAtual[1];

            int[] proximoPasso = GetProximoPasso(posX, posY);
            if(proximoPasso != null){ // Checar próximo passo se existe, e se existe, mover para o próximo passo
                posicaoAtual = proximoPasso;
                pilhaPassosAvancados.empilhe(posicaoAtual);
                estruturaLabirintoComRota.get(posicaoAtual[1]).set(posicaoAtual[0], "X");
                //ImprimirLabirinto();
                if(EstaNaSaida(posicaoAtual[0],posicaoAtual[1])){ // Checar se está na saída, e se sim, retornar para terminar a função
                    return;
                }
            }
            else{ //Se não existe, voltar um passo e tentar achar outra rota
                estruturaLabirintoComRota.get(posicaoAtual[1]).set(posicaoAtual[0], "0");
                pilhaPassosAvancados.desempilhe();
                if(!pilhaPassosAvancados.vazia()){
                    posicaoAtual = pilhaPassosAvancados.getValorTopo();
                    //ImprimirLabirinto();
                }
                else{
                    throw new SemSaida("Sem saída");
                }
            }
        }
    }

    // Função que retorna qual é o próximo passo a ser tomado no labirinto. Se nenhum é reconhecido, um valor nulo é retornado
    private int[] GetProximoPasso(int posX, int posY) {
        // Com prioridade para as laterais em vez das diagonais, procura o próximo passo no labirinto
        int xTemp = posX;
        int yTemp = posY;

        // Checa a esquerda e direita
        for (int adicionalX = -1; adicionalX < 2; adicionalX += 2) {
            xTemp = posX + adicionalX;
            if (xTemp >= 0 && xTemp < this.largura) {
                if (ExisteProximoPasso(xTemp, yTemp)) {
                    return new int[]{xTemp, yTemp};
                }
            }
        }
        xTemp = posX;
        // Checa em baixo e cima
        for (int adicionalY = -1; adicionalY < 2; adicionalY += 2) {
            yTemp = posY + adicionalY;
            if (yTemp >= 0 && yTemp < this.altura) {
                if (ExisteProximoPasso(xTemp, yTemp)) {
                    return new int[]{xTemp, yTemp};
                }
            }
        }
        yTemp = posY;
        // Checa nas diagonais da cima direita e da baixa esquerda
        for (int adicional = -1; adicional < 2; adicional += 2) {
            xTemp = posX + adicional;
            yTemp = posY + adicional;
            if (xTemp >= 0 && xTemp < this.largura && yTemp >= 0 && yTemp < this.altura) {
                if (ExisteProximoPasso(xTemp, yTemp)) {
                    return new int[]{xTemp, yTemp};
                }
            }
        }
        // Checa nas diagonais da cima esquerda e da baixa direita
        for (int adicional = -1; adicional < 2; adicional += 2) {
            xTemp = posX - adicional;
            yTemp = posY + adicional;
            if (xTemp >= 0 && xTemp < this.largura && yTemp >= 0 && yTemp < this.altura) {
                if (ExisteProximoPasso(xTemp, yTemp)) {
                    return new int[]{xTemp, yTemp};
                }
            }
        }

        // Se nada foi retornado, é porque nenhum passo foi encontrado
        return null;
    }

    // Função para checar que o próximo passo sendo checado está marcado como um caminho no labirinto
    private boolean ExisteProximoPasso(int x, int y){
        return estruturaLabirinto.get(y).get(x).equals("1");
    }

    // Função para verificar se a posição atual é a saída do labirinto
    private boolean EstaNaSaida(int x, int y) {
        return x == largura - 1 || y == altura - 1 || x == 0 || y == 0;
    }

    public void ImprimirLabirinto() {
        for (List<String> linha : estruturaLabirintoComRota) {
            System.out.println(String.join("\t", linha));
        }
        System.out.println();
    }
}
