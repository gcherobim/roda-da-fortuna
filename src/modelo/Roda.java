package modelo;

import java.io.File;
import java.util.Arrays;
import java.util.Random;
import javafx.scene.image.Image;

/**
 *
 * @author braulio
 */
public class Roda {

    private EspacoRoda[] espacos;
    private EspacoRoda espacoAtual;

    private static final String EXTENSAO_IMAGEM = "jpg";
    private static final int NUMERO_ESPACOS = 24;
    private static final String CAMINHO_IMAGENS = "figuras";

    /**
     * Inicializa a roda e faz o um giro.
     */
    public Roda() {
        inicializarRoda();
        girarRoda();
    }

    /**
     * Retorna o valor atual da roda
     * @return int
     */
    public int getValorAtual() {
        return espacoAtual.getValor();
    }

    /**
     * Retorna a imagem atual da roda
     * @return Image
     */
    public Image getImagemAtual() {
        return espacoAtual.getImagem();
    }

    /**
     * Gira a roda aleatoriamente, [1,24]
     */
    public void girarRoda() {
        int giro = new Random().nextInt(NUMERO_ESPACOS) + 1;
        espacoAtual = espacos[giro];
    }

    /**
     * Inicializa os valores e as imagens da roda.
     */
    private void inicializarRoda() {
        
        try {
            // carrega as imagens
            Image[] imagens = new Image[24];
            File fonte = new File(CAMINHO_IMAGENS);
            File[] files = fonte.listFiles();
            Arrays.sort(files);
            String[] arquivos = new String[24];
            for (int i = 0; i < files.length; i++) 
                arquivos[i] = files[i].toString();
            for (int i = 0; i < files.length; i++) {
                imagens[i] = new Image("file:" + arquivos[i]);
            }

            // preenche o array de espaços
            espacos = new EspacoRoda[25];
            for (int i = 0; i < imagens.length; i++) {
                String[] separado = arquivos[i].split("_|\\.");
                switch (separado[1]) {
                    case "bankrupt":
                        espacos[i+1] = new EspacoRoda(-1, imagens[i]);
                        break;
                    case "loseATurn":
                        espacos[i+1] = new EspacoRoda(0, imagens[i]);
                        break;
                    default:
                        espacos[i+1] = new EspacoRoda(Integer.valueOf(separado[1]), imagens[i]);
                        break;
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Erro: " + e.getMessage());
        }
        
    }

    /**
     * Classe interna para armazenar cada valor e imagem.
     */
    private class EspacoRoda {

        int valor;
        Image imagen;

        EspacoRoda(final int valor, final Image imagem) {
            this.valor = valor;
            this.imagen = imagem;
        }

        int getValor() {
            return valor;
        }

        Image getImagem() {
            return imagen;
        }
    }

}