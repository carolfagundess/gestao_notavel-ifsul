package br.com.gestaonotavel.ifsul.controller;

import br.com.gestaonotavel.ifsul.model.Paciente;
import br.com.gestaonotavel.ifsul.service.PacienteService;
import br.com.gestaonotavel.ifsul.util.AlertUtil;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;


/**
 * FXML Controller class
 *
 * @author carol
 */
public class TelaCadastroPacienteController implements Initializable {

    /**
     * Initializes the controller class.
     */

    @FXML
    private TextField txtNome;

    @FXML
    private TextField txtCpf;

    @FXML
    private DatePicker datePickerDataNascimento;

    @FXML
    private TextField txtDiagnostico;

    @FXML
    private TextField txtEscolaridade;


    @FXML
    public void handleSalvarButtonAction(ActionEvent event) {
        PacienteService pacienteService = new PacienteService();

        try {

            String nome = txtNome.getText();
            String cpf = txtCpf.getText();
            LocalDate dataNascimento = datePickerDataNascimento.getValue();
            String diagnostico = txtDiagnostico.getText();
            String escolaridade = txtEscolaridade.getText();

            Paciente paciente =  new Paciente();
            paciente.setNome(nome);
            paciente.setCpf(cpf);
            paciente.setDiagnostico(diagnostico);
            paciente.setEscolaridade(escolaridade);
            if (dataNascimento != null) {
                paciente.setDataNascimento(Date.from(dataNascimento.atStartOfDay(ZoneId.systemDefault()).toInstant()));
            }

            pacienteService.salvarPaciente(paciente);
        }catch (IllegalArgumentException e){
            AlertUtil.showAlert(Alert.AlertType.ERROR, "Erro ao salvar", e.getMessage());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
