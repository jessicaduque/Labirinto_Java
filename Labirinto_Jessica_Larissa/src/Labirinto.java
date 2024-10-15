import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import javax.swing.*;
import java.awt.*;
public class Labirinto {
    private final List<List<Integer>> estruturaLabirinto; // Matriz de strings para guardar a estrutura original do labirinto
    private List<List<Integer>> estruturaLabirintoComRota;
    private final int largura, altura; // Largura e altura original do labirinto
    private JPanel[][] celulas;
    private final JFrame framePrincipal = new JFrame();

    // Construtor que lê um arquivo csv para determinar a estrutura, altura e largura do labirinto
    public Labirinto(String nomeCSV) {
        this.estruturaLabirinto = this.GetEstruturaLabirinto(nomeCSV);
        this.estruturaLabirintoComRota = this.estruturaLabirinto;
        this.largura = this.estruturaLabirinto.getFirst().size();
        this.altura = this.estruturaLabirinto.size();
        this.celulas = new JPanel[this.altura][this.largura];
        initUI();
    }

    // Função para conseguir ler a estrutura do labirinto a partir do csv
    private List<List<Integer>> GetEstruturaLabirinto(String nomeCSV) {
        List<List<Integer>> records = new ArrayList<>();

        try (Scanner scanner = new Scanner(new File(nomeCSV + ".csv"))) {
            while (scanner.hasNextLine()) {
                List<Integer> linha = this.GetLinhaLabirinto(scanner.nextLine());
                records.add(linha);
            }
        } catch (FileNotFoundException e) {
            System.out.println("CSV especificado não pôde ser encontrado.");
        }
        return records;
    }

    // Função que retorna cada linha lida no csv do labirinto para formar sua estrutura
    private List<Integer> GetLinhaLabirinto(String line) {
        List<Integer> values = new ArrayList<>();
        try (Scanner rowScanner = new Scanner(line)) {
            rowScanner.useDelimiter(",");  // Alterar para vírgula como delimitador
            while (rowScanner.hasNextInt()) {
                values.add(rowScanner.nextInt());
            }
        }
        return values;
    }

    // Função que inicia o UI do labirinto
    private void initUI() {
        // Configuração da janela e layout
        framePrincipal.setTitle("Labirinto com Animação");
        framePrincipal.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        framePrincipal.setLayout(new GridLayout(this.altura, this.largura));

        // Inicializa o labirinto gráfico
        for (int i = 0; i < this.altura; i++) {
            for (int j = 0; j < this.largura; j++) {
                celulas[i][j] = new JPanel();
                if (estruturaLabirinto.get(i).get(j) == 0) {
                    celulas[i][j].setBackground(Color.BLACK); // Paredes são pretas
                } else {
                    celulas[i][j].setBackground(Color.WHITE); // Passagens são brancas
                }
                framePrincipal.add(celulas[i][j]);
            }
        }

        framePrincipal.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        framePrincipal.setSize(400, 400);
        framePrincipal.setLocationRelativeTo(null);
        framePrincipal.setVisible(true);
    }

    // Função que descobre a rota para a saída do labirinto
    public void DescobrirRotaLabirinto() throws FaltaEspaco, FaltaElemento, SemSaida, InputMismatchException, NumberFormatException {
        // Variáveis
        PilhaS<int[]> pilhaPassosAvancados = new PilhaS<>(this.largura * this.altura);
        Scanner scanner = new Scanner(System.in);
        System.out.println("Qual a posição de entrada? (Ex: 0,0)");
        String[] entradaString = scanner.nextLine().split(",");;
        int[] posEntrada = new int[2];
        int[] posicaoAtual;

        try { // Checar se a entrada informada pelo usuário está no padrão esperado correto
            for (int i = 0; i < entradaString.length; i++) {
                posEntrada[i] = Integer.valueOf(entradaString[i]);
            }
            posicaoAtual = posEntrada;
        }
        catch(Exception e) {
            throw new InputMismatchException("Formato de posição da entrada informada diferente do esperado.");
        }
        try{ // Checar se a entrada informada pelo usuário é de fato marcada como um caminho na estrutura do labirinto
            if(this.estruturaLabirintoComRota.get(posicaoAtual[1]).get(posicaoAtual[0]) != 1){
                throw new Exception();
            }
        }
        catch(Exception e){
            throw new RuntimeException("Entrada informada não corresponde à um caminho na estrutura do labirinto.");
        }

        celulas[posEntrada[1]][posEntrada[0]].setBackground(Color.RED);
        pilhaPassosAvancados.empilhe(posEntrada);

        while (true) {
            int posX = posicaoAtual[0];
            int posY = posicaoAtual[1];

            int[] proximoPasso = this.GetProximoPasso(posX, posY);
            if(proximoPasso != null){ // Checar próximo passo se existe, e se existe, mover para o próximo passo
                celulas[posY][posX].setBackground(Color.WHITE);
                posicaoAtual = proximoPasso;
                pilhaPassosAvancados.empilhe(posicaoAtual);
                celulas[posicaoAtual[1]][posicaoAtual[0]].setBackground(Color.RED);
                if(this.EstaNaSaida(posicaoAtual[0],posicaoAtual[1])){ // Checar se está na saída, e se sim, retornar para terminar a função
                    return;
                }
            }
            else{ //Se não existe, voltar um passo e tentar achar outra rota
                celulas[posicaoAtual[1]][posicaoAtual[0]].setBackground(Color.BLACK);
                pilhaPassosAvancados.desempilhe();
                if(!pilhaPassosAvancados.vazia()){
                    posicaoAtual = pilhaPassosAvancados.getValorTopo();
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
                if (this.ExisteProximoPasso(xTemp, yTemp)) {
                    return new int[]{xTemp, yTemp};
                }
            }
        }
        xTemp = posX;
        // Checa em baixo e cima
        for (int adicionalY = -1; adicionalY < 2; adicionalY += 2) {
            yTemp = posY + adicionalY;
            if (yTemp >= 0 && yTemp < this.altura) {
                if (this.ExisteProximoPasso(xTemp, yTemp)) {
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
                if (this.ExisteProximoPasso(xTemp, yTemp)) {
                    return new int[]{xTemp, yTemp};
                }
            }
        }
        // Checa nas diagonais da cima esquerda e da baixa direita
        for (int adicional = -1; adicional < 2; adicional += 2) {
            xTemp = posX - adicional;
            yTemp = posY + adicional;
            if (xTemp >= 0 && xTemp < this.largura && yTemp >= 0 && yTemp < this.altura) {
                if (this.ExisteProximoPasso(xTemp, yTemp)) {
                    return new int[]{xTemp, yTemp};
                }
            }
        }

        // Se nada foi retornado, é porque nenhum passo foi encontrado
        return null;
    }

    // Função para checar que o próximo passo sendo checado está marcado como um caminho no labirinto
    private boolean ExisteProximoPasso(int x, int y){
        return this.estruturaLabirinto.get(y).get(x) == 1;
    }

    // Função para verificar se a posição atual é a saída do labirinto
    private boolean EstaNaSaida(int x, int y) {
        return x == this.largura - 1 || y == this.altura - 1 || x == 0 || y == 0;
    }

    // Função para imprimir a estrutura do labirinto de forma bonita em texto
//    public void ImprimirLabirintoTexto() {
//        for (List<String> linha : this.estruturaLabirintoComRota) {
//            System.out.println(String.join("\t", linha));
//        }
//        System.out.println();
//    }
}
