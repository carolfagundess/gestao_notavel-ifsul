package br.com.gestaonotavel.ifsul.controller;

import br.com.gestaonotavel.ifsul.service.ResponsavelService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

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

    public TelaCadastroResponsavelController() {}

    public TelaCadastroResponsavelController(ResponsavelService responsavelService) {
        this.responsavelService = responsavelService;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
