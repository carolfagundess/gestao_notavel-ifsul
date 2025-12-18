package br.com.gestaonotavel.ifsul.controller;

import br.com.gestaonotavel.ifsul.model.Responsavel;
import br.com.gestaonotavel.ifsul.service.ResponsavelService;
import br.com.gestaonotavel.ifsul.util.AlertUtil;
import br.com.gestaonotavel.ifsul.util.MaskUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert; // Import Importante
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.net.URL;
import java.util.ResourceBundle;

public class TelaCadastroResponsavelController extends BaseController implements Initializable {

    @FXML private TextField txtNome;
    @FXML private TextField txtCpf;
    @FXML private TextField txtTelefone;
    @FXML private DatePicker datePickerDataNascimento;

    private final ResponsavelService responsavelService;
    private Responsavel responsavel;

    public TelaCadastroResponsavelController(ResponsavelService responsavelService) {
        this.responsavelService = responsavelService;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        MaskUtil.cpfField(txtCpf);
        MaskUtil.foneField(txtTelefone);
    }

    @FXML
    public void handleSalvarButtonAction(ActionEvent event) {
        try {
            Responsavel r = new Responsavel();
            r.setNome(txtNome.getText());
            r.setCpf(txtCpf.getText());
            r.setTelefone(txtTelefone.getText());
            r.setDataNascimento(datePickerDataNascimento.getValue());

            this.responsavel = responsavelService.salvar(r);
            AlertUtil.showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Responsável salvo!");
            ((Stage) txtNome.getScene().getWindow()).close();
        } catch (Exception e) {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "Erro", e.getMessage());
        }
    }

    public Responsavel getResponsavel() { return responsavel; }
    @FXML public void handleCancelarButtonAction(ActionEvent event) { ((Stage) txtNome.getScene().getWindow()).close(); }
    @FXML public void handleLimparButtonAction(ActionEvent event) {
        txtNome.clear(); txtCpf.clear(); txtTelefone.clear(); datePickerDataNascimento.setValue(null);
    }
}
