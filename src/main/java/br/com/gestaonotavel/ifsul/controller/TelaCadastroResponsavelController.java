package br.com.gestaonotavel.ifsul.controller;

import br.com.gestaonotavel.ifsul.model.Paciente;
import br.com.gestaonotavel.ifsul.model.Responsavel;
import br.com.gestaonotavel.ifsul.service.PacienteService;
import br.com.gestaonotavel.ifsul.service.ResponsavelService;
import br.com.gestaonotavel.ifsul.util.AlertUtil;
import br.com.gestaonotavel.ifsul.util.RegraDeNegocioException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class TelaCadastroResponsavelController implements Initializable {

    @FXML
    private TextField txtNome;
    @FXML
    private TextField txtCpf;
    @FXML
    private TextField txtTelefone;
    @FXML
    private DatePicker datePickerDataNascimento;

    private ResponsavelService responsavelService;

    private Responsavel responsavel;

    public TelaCadastroResponsavelController() {}

    public TelaCadastroResponsavelController(ResponsavelService responsavelService) {
        this.responsavelService = responsavelService;
    }

    @FXML
    public void handleSalvarButtonAction(ActionEvent event) {
        try {
            validarCamposPreenchidos();

            Responsavel responsavel = construirResponsavel();
            this.responsavel = responsavelService.salvar(responsavel);

            AlertUtil.showAlert(Alert.AlertType.INFORMATION,
                    "Sucesso",
                    "Paciente salvo com sucesso!");

            limparFormulario();
            Stage stage = (Stage) txtNome.getScene().getWindow();
            stage.close();

        } catch (RegraDeNegocioException e) {
            AlertUtil.showAlert(Alert.AlertType.ERROR,
                    "Erro ao salvar",
                    e.getMessage());
        } catch (Exception e) {
            AlertUtil.showAlert(Alert.AlertType.ERROR,
                    "Erro",
                    "Erro inesperado: " + e.getMessage());
        }
    }

    private Responsavel construirResponsavel() {
        Responsavel responsavel = new Responsavel();

        responsavel.setNome(txtNome.getText().trim());
        responsavel.setCpf(txtCpf.getText().trim().isEmpty() ? null : txtCpf.getText().trim());
        responsavel.setDataNascimento(datePickerDataNascimento.getValue());
        responsavel.setTelefone(txtTelefone.getText().trim());

        return responsavel;
    }

    @FXML
    public void handleLimparButtonAction(ActionEvent event) {
        limparFormulario();
    }

    @FXML
    public void handleCancelarButtonAction(ActionEvent event) {
        Stage stage = (Stage) txtNome.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (this.responsavelService == null) {
            this.responsavelService = new ResponsavelService();
        }
    }

    public Responsavel getResponsavel() {
        return responsavel;
    }

    private void validarCamposPreenchidos() throws RegraDeNegocioException {
        if (txtNome.getText() == null || txtNome.getText().trim().isEmpty()) {
            throw new RegraDeNegocioException("Preencha o nome do paciente");
        }
        if (datePickerDataNascimento.getValue() == null) {
            throw new RegraDeNegocioException("Preencha a data de nascimento");
        }
        if (txtTelefone.getText() == null || txtTelefone.getText().trim().isEmpty()) {
            throw new RegraDeNegocioException("Preencha o diagnóstico");
        }
        if (txtCpf.getText() == null || txtCpf.getText().isEmpty()) {
            throw new RegraDeNegocioException("Selecione a escolaridade");
        }
    }

    // * Limpa todos os campos do formulário
    private void limparFormulario() {
        txtNome.clear();
        txtCpf.clear();
        txtTelefone.clear();
        datePickerDataNascimento.setValue(null);
        txtNome.requestFocus();
    }

}
