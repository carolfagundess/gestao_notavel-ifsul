package br.com.gestaonotavel.ifsul.controller;

import br.com.gestaonotavel.ifsul.model.Atividade;
import br.com.gestaonotavel.ifsul.model.ParticipacaoAtividade;
import br.com.gestaonotavel.ifsul.model.Responsavel;
import br.com.gestaonotavel.ifsul.service.AtividadeService;
import br.com.gestaonotavel.ifsul.service.ParticipacaoAtividadeService;
import br.com.gestaonotavel.ifsul.service.ResponsavelService;
import br.com.gestaonotavel.ifsul.util.AlertUtil;
import br.com.gestaonotavel.ifsul.util.RegraDeNegocioException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class TelaRegistroParticipacaoController implements Initializable {

    @FXML private ComboBox<Atividade> cbxAtividade;
    @FXML private ComboBox<Responsavel> cbxResponsavel;
    @FXML private TextField txtFuncao;
    @FXML private TextField txtHoras;
    @FXML private Label lblCreditosGerados;
    @FXML private Button btnLimpar;
    @FXML private Button btnCancelar;
    @FXML private Button btnSalvar;

    private final ParticipacaoAtividadeService participacaoService;
    private final AtividadeService atividadeService;
    private final ResponsavelService responsavelService;

    public TelaRegistroParticipacaoController(ParticipacaoAtividadeService participacaoService, AtividadeService atividadeService, ResponsavelService responsavelService) {
        this.participacaoService = participacaoService;
        this.atividadeService = atividadeService;
        this.responsavelService = responsavelService;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        carregarAtividades();
        carregarResponsaveis();
        configurarPreviewCreditos();
    }

    private void carregarAtividades() {
        List<Atividade> atividades = atividadeService.listarTodos();
        cbxAtividade.getItems().setAll(atividades);
        cbxAtividade.setConverter(new StringConverter<Atividade>() {
            @Override public String toString(Atividade atividade) { return (atividade == null) ? null : atividade.getNome(); }
            @Override public Atividade fromString(String string) { return null; }
        });
    }

    private void carregarResponsaveis() {
        List<Responsavel> responsaveis = responsavelService.buscarTodos();
        cbxResponsavel.getItems().setAll(responsaveis);
        cbxResponsavel.setConverter(new StringConverter<Responsavel>() {
            @Override public String toString(Responsavel responsavel) { return (responsavel == null) ? null : responsavel.getNome(); }
            @Override public Responsavel fromString(String string) { return null; }
        });
    }

    private void configurarPreviewCreditos() {
        txtHoras.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                double horas = Double.parseDouble(newValue.replace(",", "."));
                double creditos = horas * ParticipacaoAtividade.getValorHoraCredito();
                lblCreditosGerados.setText(String.format("R$ %.2f", creditos));
            } catch (NumberFormatException e) {
                lblCreditosGerados.setText("R$ 0,00");
            }
        });
    }

    @FXML
    void handleSalvarButtonAction(ActionEvent event) {
        try {
            Responsavel responsavel = cbxResponsavel.getValue();
            Atividade atividade = cbxAtividade.getValue();
            String funcao = txtFuncao.getText();
            Double horas = null;

            try {
                horas = Double.parseDouble(txtHoras.getText().replace(",", "."));
            } catch (NumberFormatException e) {
                throw new RegraDeNegocioException("Número de horas inválido. Use apenas números.");
            }

            participacaoService.registrarParticipacao(responsavel, atividade, horas, funcao);
            AlertUtil.showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Participação registrada com sucesso!");
            limparFormulario();
        } catch (RegraDeNegocioException e) {
            AlertUtil.showAlert(Alert.AlertType.WARNING, "Erro de Validação", e.getMessage());
        } catch (Exception e) {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "Erro", "Ocorreu um erro inesperado: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML void handleLimparButtonAction(ActionEvent event) { limparFormulario(); }
    @FXML void handleCancelarButtonAction(ActionEvent event) { ((Stage) btnCancelar.getScene().getWindow()).close(); }

    private void limparFormulario() {
        cbxAtividade.setValue(null);
        cbxResponsavel.setValue(null);
        txtFuncao.clear();
        txtHoras.clear();
        Platform.runLater(() -> cbxAtividade.requestFocus());
    }
}