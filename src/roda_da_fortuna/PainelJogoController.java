package roda_da_fortuna;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import static javafx.scene.control.Alert.AlertType.INFORMATION;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.effect.SepiaTone;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import modelo.Jogador;
import modelo.Roda;
import modelo.Tabuleiro;

/**
 *
 * @author braulio
 */
public class PainelJogoController {

    @FXML
    private GridPane paneJogadores;

    @FXML
    private Button comprarVogal, girarRoda, resolverPuzzle;

    @FXML
    private ImageView imagemRoda;

    @FXML
    private Label labelPuzzle;

    @FXML
    private GridPane paneVogais, paneConsoantes;

    private final int quantidadeJogadores = 3;
    private final int custoVogal = 250;
    private int posicaoJogadorAtual = 0;

    private Tabuleiro tabuleiro;
    private Roda roda;
    private final Jogador[] jogadores = new Jogador[quantidadeJogadores];
    private Jogador jogadorAtual;

    private final SepiaTone selected = new SepiaTone(1.5);
    
    private int consoantesUsadas = 0;
    private int vogaisUsadas = 0;

    /**
     * Inicializa a cena da primeira jogada.
     */
    @FXML
    private void initialize() {
        // bloquear comprarVogal
        this.comprarVogal.disableProperty().set(true);

        // instanciar a Roda
        this.roda = new Roda();
        // setar a imagem atual da Roda
        imagemRoda.setImage(this.roda.getImagemAtual()); // remover comentario, 
                                                           // apos implementar a classe Roda

        // bloquear o painel das vogais
        this.paneVogais.disableProperty().set(true);
        // setar o evento clicarVogal em cada botao das vogais
        this.paneVogais.getChildren().forEach(n -> ((Button) n).setOnAction(e -> clicarVogal(e)));

        // bloquear o painel das consoantes
        this.paneConsoantes.disableProperty().set(true);
        // setar o evento clicarConsoante em cada botao das consoantes
        this.paneConsoantes.getChildren().forEach(n -> ((Button) n).setOnAction(e -> clicarConsoante(e)));
        
    }

    /**
     * Implementa a logica de comprar uma vogal.
     * @param event 
     */
    @FXML
    private void comprarVogalAction(ActionEvent event) {
        jogadorAtual.aumentaJogada();
        jogadorAtual.reduzirPontos(custoVogal);
        TitledPane jogadorNome = ((TitledPane) paneJogadores.getChildren().get(posicaoJogadorAtual));
        Label pontuacaoJogador = ((Label) jogadorNome.getContent());
        pontuacaoJogador.setText(jogadorAtual.getPontos());
        paneConsoantes.disableProperty().set(true);
        paneVogais.disableProperty().set(false);
        comprarVogal.disableProperty().set(true);
        girarRoda.disableProperty().set(true);
        resolverPuzzle.disableProperty().set(true);
    }

    /**
     * Implementa a logica de girar a roda.
     * @param event 
     */
    @FXML
    private void girarRodaAction(ActionEvent event) {
        jogadorAtual.aumentaJogada();
        jogadorAtual.girarRoda();
        imagemRoda.setImage(roda.getImagemAtual());
        switch (roda.getValorAtual()) {
            case -1:
                alerta(INFORMATION, "Bankrupt", "Perde tudo!");
                jogadorAtual.zerarPontos();
                TitledPane jogadorNome = ((TitledPane) paneJogadores.getChildren().get(posicaoJogadorAtual));
                Label pontuacaoJogador = ((Label) jogadorNome.getContent());
                pontuacaoJogador.setText(jogadorAtual.getPontos());
                avancarProximoJogador();
                break;
            case 0:
                alerta(INFORMATION, "Lose A Turn", "Passa a vez!");
                avancarProximoJogador();
                break;
            default:
                girarRoda.disableProperty().set(true);
                paneConsoantes.disableProperty().set(false);
                comprarVogal.disableProperty().set(true);
                resolverPuzzle.disableProperty().set(true);
                break;
        }
    }

    /**
     * Implementa a logica de tentar adivinhar o puzzle.
     * @param event 
     */
    @FXML
    private void resolverPuzzleAction(ActionEvent event) {
        jogadorAtual.aumentaJogada();
        
        Stage popupwindow = new Stage();
      
        popupwindow.initModality(Modality.APPLICATION_MODAL);
        popupwindow.setTitle("Resolver o Puzzle");
        popupwindow.initStyle(StageStyle.UNDECORATED);

        Label label1 = new Label("Qual a expressão?");
        TextField tffrase = new TextField();
        Button bEnviar = new Button("Enviar");
        bEnviar.setOnAction(e -> compararFrases(tffrase.getText(), popupwindow));

        VBox layout= new VBox(10);
        layout.getChildren().addAll(label1,tffrase, bEnviar);
        layout.setAlignment(Pos.CENTER);

        Scene scene1= new Scene(layout, 300, 100);
        scene1.getStylesheets().add("css/estilo.css");

        popupwindow.setScene(scene1);

        popupwindow.showAndWait();
    }

    /**
     * Implementa a logica de clicar em uma CONSOANTE.
     * @param event 
     */
    private void clicarConsoante(ActionEvent event) {
        Button button = (Button) event.getSource();
        consoantesUsadas = consoantesUsadas + 1;
        if (tabuleiro.receberPalpite(button.getText().charAt(0)) == true) {
            String palpite = labelPuzzle.getText();
            labelPuzzle.setText(tabuleiro.atualizaPalpite(button.getText(), palpite));
            jogadorAtual.aumentarPontos(roda.getValorAtual() * tabuleiro.correspondencias(button.getText()));
            TitledPane jogadorNome = ((TitledPane) paneJogadores.getChildren().get(posicaoJogadorAtual));
            Label pontuacaoJogador = ((Label) jogadorNome.getContent());
            pontuacaoJogador.setText(jogadorAtual.getPontos());
            habilitaBotoes();
        }
        else {
            avancarProximoJogador();
        }
        isFimDoJogo();
        button.disableProperty().set(true);
        paneConsoantes.disableProperty().set(true);
        resolverPuzzle.disableProperty().set(false);
    }

    /**
     * Implementa a logica de clicar em uma VOGAL.
     * @param event 
     */
    private void clicarVogal(ActionEvent event) {
        Button button = (Button) event.getSource();
        vogaisUsadas = vogaisUsadas + 1;
        if (tabuleiro.receberPalpite(button.getText().charAt(0)) == true) {
            String palpite = labelPuzzle.getText();
            labelPuzzle.setText(tabuleiro.atualizaPalpite(button.getText(), palpite));
            habilitaBotoes();
        } else {
            avancarProximoJogador();
        }
        isFimDoJogo();
        button.disableProperty().set(true);
        paneVogais.disableProperty().set(true);
        resolverPuzzle.disableProperty().set(false);
    }

    /**
     * Verifica se o jogo terminou, ou seja, se todas as letras do puzzle foram preenchidas,
     * e, em caso positivo, exibe uma mensagem informando quem ganhou e termina a aplicacao.
     */
    private void isFimDoJogo() {
        if (tabuleiro.getPalpitesRestantes(labelPuzzle.getText()) == 0) {
            alerta(INFORMATION, "Fim de jogo", "O jogador " + jogadorAtual.getNome() + 
                    " venceu com " + jogadorAtual.getPontos() + " pontos em " + 
                    jogadorAtual.getNumeroJogada() + " jogadas.");
            System.exit(0);
        }   
    }

    /**
     * Valida se todos os botoes das VOGAIS foram clicados, i.e., ficaram desativados.
     * @return boolean
     */
    private boolean isVogaisEsgotadas() {
        if (vogaisUsadas == paneVogais.getChildren().size())
            return true;
        else
            return false;
    }

    /**
     * Valida se todos os botoes das CONSOANTES foram clicados, i.e., ficaram desativados.
     * @return boolean
     */
    private boolean isConsoantesEsgotadas() {
        if (consoantesUsadas == paneConsoantes.getChildren().size())
            return true;
        else
            return false;
    }
    
    // checa quais botões serão habilitados
    public void habilitaBotoes() {
        if (isConsoantesEsgotadas() == true)
            girarRoda.disableProperty().set(true);
        else 
            girarRoda.disableProperty().set(false);
        if (isVogaisEsgotadas() == true)
            comprarVogal.disableProperty().set(true);
        else if (Integer.valueOf(jogadorAtual.getPontos()).compareTo(custoVogal) >= 0)
            comprarVogal.disableProperty().set(false);
        else
            comprarVogal.disableProperty().set(true);
    }
    
    // compara o palpite do jogador com o puzzle, é chamado em resolverPuzzleAction()
    public void compararFrases(String frase, Stage popupwindow) {
        frase = removeAcentos(frase);
        if (frase.toUpperCase().equals(tabuleiro.getPuzzle())) {
            alerta(INFORMATION, "Fim de jogo", "O jogador " + jogadorAtual.getNome() + 
                    " venceu com " + jogadorAtual.getPontos() + " pontos em " + 
                    jogadorAtual.getNumeroJogada() + " jogadas.");
            System.exit(0);
        } else if (frase.length() == 0) {
            alerta(INFORMATION, "Dê um palpite.", "Você precisa dar um palpite!");
        } else {
            alerta(INFORMATION, "Errou!", "Próximo jogador.");
            popupwindow.close();
            avancarProximoJogador();
        }
            
    }

    // remove acentos da frase
    public String removeAcentos(String frase) {
        frase = frase.replaceAll("[áàãâ]", "a");
        frase = frase.replaceAll("[éê]", "e");
        frase = frase.replaceAll("í", "i");
        frase = frase.replaceAll("[óôõ]", "o");
        frase = frase.replaceAll("ú", "u");
        return frase;
    }

    /**
     * Cria e exibe uma janela de alerta.
     * @param type
     * @param titulo
     * @param conteudo 
     */
    private void alerta(AlertType type, String titulo, String conteudo) {
        Alert alert = new Alert(type);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(conteudo);
        
        alert.showAndWait();
    }

    /**
     * Avanca para o proximo jogador.
     * Adiciona o efeito de selecionado, i.e., <code>selected</code>, 
     * no <code>TitledPane</code> do jogador atual.
     */
    private void avancarProximoJogador() {
        ((TitledPane) paneJogadores.getChildren().get(posicaoJogadorAtual)).setEffect(null);
        posicaoJogadorAtual = (posicaoJogadorAtual + 1) % quantidadeJogadores;
        jogadorAtual = jogadores[posicaoJogadorAtual];
        ((TitledPane) this.paneJogadores.getChildren().get(posicaoJogadorAtual)).setEffect(selected);
        paneConsoantes.disableProperty().set(true);
        habilitaBotoes();
    }

    /**
     * Recebe o puzzle e os nomes dos jogadores.
     * @param puzzle
     * @param nomeJogadores 
     */
    public void setPuzzleENomeJogadores(final String puzzle, final String... nomeJogadores) {
        this.tabuleiro = new Tabuleiro(puzzle);
        this.labelPuzzle.setText(this.tabuleiro.getPalpitePuzzle());

        for (int i = 0; i < quantidadeJogadores; i++) {
            ((TitledPane) this.paneJogadores.getChildren().get(i)).setText(nomeJogadores[i]);

            this.jogadores[i] = new Jogador(nomeJogadores[i]);
            this.jogadores[i].setRoda(this.roda);
        }

        this.jogadorAtual = this.jogadores[posicaoJogadorAtual];
        ((TitledPane) this.paneJogadores.getChildren().get(posicaoJogadorAtual)).setEffect(selected);
    }

}