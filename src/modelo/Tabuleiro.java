package modelo;

import java.util.regex.Pattern;

/**
 *
 * @author braulio
 */
public class Tabuleiro {

    private final String puzzle;
    private final char[] palpitePuzzle;
    private int palpitesRestantes;
    
    private int correspondencia;

    /**
     * Seta o puzzle do jogo e inicializa o tabuleiro.
     * @param puzzle 
     */
    public Tabuleiro(String puzzle) {
        puzzle = removeAcentos(puzzle);
        this.puzzle = puzzle.toUpperCase();
        this.palpitePuzzle = new char[puzzle.length()];
        inicializarTabuleiro();
    }

    /**
     * Inicializa <code>palpitesRestantes</code>, so considera as letras, e
     * <code>palpitePuzzle</code>.
     */
    private void inicializarTabuleiro() {
        for (int i = 0; i < puzzle.length(); i++)
            palpitePuzzle[i] = puzzle.charAt(i);
        palpitesRestantes = 0;
        String palpiteStr = getPalpitePuzzle();
        for (int i = 0; i < puzzle.length(); i++) {
            if (String.valueOf(palpiteStr.charAt(i)).equals("_"))
                palpitesRestantes = palpitesRestantes + 1;
        }
    }

    /**
     * Recebe o palpite do jogador
     * @param palpite
     * @return true se bateu o palpite
     */
    public boolean receberPalpite(char palpite) {
        if (puzzle.contains(String.valueOf(palpite))) {
            return true;
        }
        else 
            return false;
    }

    /**
     * Retorna os palpites do jogo a serem mostrados na tela.
     * @return String
     */
    public String getPalpitePuzzle() {
        String palpiteStr = "";
        for (int i = 0; i < puzzle.length(); i++) {
            if (String.valueOf(palpitePuzzle[i]).equals(" "))
                palpiteStr = palpiteStr + " ";
            else if (Pattern.matches("\\p{Punct}", String.valueOf(palpitePuzzle[i])))
                palpiteStr = palpiteStr + String.valueOf(palpitePuzzle[i]);
            else 
                palpiteStr = palpiteStr + "_";
        }
        return palpiteStr;
    }
    
    // atualiza o palpite exibido
    public String atualizaPalpite(String palpite, String palpiteStr) {
        String palpiteAtual = new String("");
        for (int i = 0; i < palpiteStr.length(); i++) {
            if (String.valueOf(palpitePuzzle[i]).equals(palpite))
                palpiteAtual = palpiteAtual + palpite;
            else
                palpiteAtual = palpiteAtual + String.valueOf(palpiteStr.charAt(i));
        }
        return palpiteAtual;
    }
    
    // retorna as correspondencias do palpite no puzzle
    public int correspondencias(String palpite) {
        correspondencia = 0;
        for (int i = 0; i < puzzle.length(); i++)
            if (String.valueOf(palpitePuzzle[i]).equals(palpite))
                correspondencia = correspondencia + 1;
        return correspondencia;
    }

    public int getPalpitesRestantes(String palpiteStr) {
        int palpitesRes = 0;
        for (int i = 0; i < puzzle.length(); i++) {
            if (String.valueOf(palpiteStr.charAt(i)).equals("_"))
                palpitesRes = palpitesRes + 1;
        }
        return palpitesRes;
    }

    // remove acentos do puzzle
    public String removeAcentos(String frase) {
        frase = frase.replaceAll("[áàãâ]", "a");
        frase = frase.replaceAll("[éê]", "e");
        frase = frase.replaceAll("í", "i");
        frase = frase.replaceAll("[óôõ]", "o");
        frase = frase.replaceAll("ú", "u");
        return frase;
    }

    public String getPuzzle() {
        return puzzle;
    }

}