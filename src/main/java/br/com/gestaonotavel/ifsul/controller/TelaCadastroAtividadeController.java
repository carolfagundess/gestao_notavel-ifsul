package br.com.gestaonotavel.ifsul.controller;

import br.com.gestaonotavel.ifsul.model.Atividade;
import br.com.gestaonotavel.ifsul.model.TipoAtividade;
import br.com.gestaonotavel.ifsul.service.AtividadeService;
import br.com.gestaonotavel.ifsul.service.TipoAtividadeService;
import br.com.gestaonotavel.ifsul.util.AlertUtil;
import br.com.gestaonotavel.ifsul.util.RegraDeNegocioException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class TelaCadastroAtividadeController implements Initializable {

    @FXML private TextField txtNome;
    @FXML private ComboBox<TipoAtividade> cbxTipoAtividade;
    @FXML private DatePicker datePickerInicio;
    @FXML private DatePicker datePickerFim;
    @FXML private TextField txtLocal;
    @FXML private TextField txtValorArrecadado;
    @FXML private Button btnLimpar;
    @FXML private Button btnCancelar;
    @FXML private Button btnSalvar;

    private final AtividadeService atividadeService;
    private final TipoAtividadeService tipoAtividadeService;

    public TelaCadastroAtividadeController(AtividadeService atividadeService, TipoAtividadeService tipoAtividadeService) {
        this.atividadeService = atividadeService;
        this.tipoAtividadeService = tipoAtividadeService;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        carregarTiposAtividade();
    }

    private void carregarTiposAtividade() {
        List<TipoAtividade> tipos = tipoAtividadeService.listarTodos();
        cbxTipoAtividade.getItems().setAll(tipos);

        cbxTipoAtividade.setConverter(new StringConverter<TipoAtividade>() {
            @Override public String toString(TipoAtividade tipo) { return (tipo == null) ? null : tipo.getNome(); }
            @Override public TipoAtividade fromString(String string) { return null; }
        });
    }

    @FXML
    void handleSalvarButtonAction(ActionEvent event) {
        try {
            Atividade atividade = new Atividade();
            atividade.setNome(txtNome.getText());
            atividade.setTipo(cbxTipoAtividade.getValue());

            LocalDate dataInicio = datePickerInicio.getValue();
            if (dataInicio != null) atividade.setDataInicio(dataInicio.atStartOfDay());

            LocalDate dataFim = datePickerFim.getValue();
            if (dataFim != null) atividade.setDataFim(dataFim.atTime(23, 59, 59));

            atividade.setLocal(txtLocal.getText());

            String valorStr = txtValorArrecadado.getText();
            if (valorStr == null || valorStr.trim().isEmpty()) {
                atividade.setValorArrecadado(0.0);
            } else {
                try {
                    double valor = Double.parseDouble(valorStr.replace(",", "."));
                    atividade.setValorArrecadado(valor);
                } catch (NumberFormatException e) {
                    throw new RegraDeNegocioException("Valor arrecadado inválido.");
                }
            }

            atividadeService.salvar(atividade);
            AlertUtil.showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Atividade salva com sucesso!");
            limparFormulario();

        } catch (RegraDeNegocioException e) {
            AlertUtil.showAlert(Alert.AlertType.WARNING, "Erro de Validação", e.getMessage());
        } catch (Exception e) {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "Erro", "Ocorreu um erro inesperado: " + e.getMessage());
        }
    }

    @FXML void handleLimparButtonAction(ActionEvent event) { limparFormulario(); }
    @FXML void handleCancelarButtonAction(ActionEvent event) { ((Stage) btnCancelar.getScene().getWindow()).close(); }

    private void limparFormulario() {
        txtNome.clear();
        cbxTipoAtividade.setValue(null);
        datePickerInicio.setValue(null);
        datePickerFim.setValue(null);
        txtLocal.clear();
        txtValorArrecadado.clear();
        txtNome.requestFocus();
    }
}