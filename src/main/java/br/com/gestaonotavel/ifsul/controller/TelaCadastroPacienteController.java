package br.com.gestaonotavel.ifsul.controller;

import br.com.gestaonotavel.ifsul.model.Paciente;
import br.com.gestaonotavel.ifsul.model.Responsavel;
import br.com.gestaonotavel.ifsul.service.PacienteService;
import br.com.gestaonotavel.ifsul.service.factory.ServiceFactory;
import br.com.gestaonotavel.ifsul.util.AlertUtil;
import br.com.gestaonotavel.ifsul.util.MaskUtil; // Import Novo
import br.com.gestaonotavel.ifsul.util.RegraDeNegocioException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class TelaCadastroPacienteController implements Initializable {

    @FXML private TextField txtNomePaciente;
    @FXML private TextField txtCpf;
    @FXML private DatePicker datePickerDataNascimento;
    @FXML private TextField txtDiagnostico;
    @FXML private ComboBox<String> cbxEscolaridade;
    @FXML private TextArea txtCondicaoClinica;

    // Labels do Responsável (Preview)
    @FXML private Label lblNomeResponsavel;
    @FXML private Label lblCpfResponsavel;
    @FXML private Label lblStatusResponsavel;
    @FXML private Label lblTelefoneResponsavel;
    @FXML private VBox vboxResponsavelPreview;
    @FXML private VBox vboxSemResponsavel;

    @FXML private Button btnSalvar;

    private final PacienteService pacienteService;
    private Responsavel responsavel;
    private Paciente pacienteEmEdicao = null;

    public TelaCadastroPacienteController(PacienteService pacienteService) {
        this.pacienteService = pacienteService;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cbxEscolaridade.getItems().addAll("Sem escolaridade", "Ensino fundamental", "Ensino médio", "Ensino superior");

        // --- APLICAÇÃO DA MÁSCARA (SPRINT 3) ---
        MaskUtil.cpfField(txtCpf);
    }

    @FXML
    public void handleSalvarButtonAction(ActionEvent event) {
        try {
            Paciente paciente = (pacienteEmEdicao == null) ? new Paciente() : pacienteEmEdicao;
            popularPaciente(paciente);

            if (this.responsavel != null) {
                if (!paciente.getResponsaveisLista().contains(this.responsavel)) {
                    paciente.getResponsaveisLista().add(this.responsavel);
                }
            }

            pacienteService.salvarPaciente(paciente);
            AlertUtil.showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Paciente salvo!");
            handleCancelarButtonAction(event);
        } catch (RegraDeNegocioException e) {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "Erro", e.getMessage());
        }
    }

    private void popularPaciente(Paciente p) {
        p.setNome(txtNomePaciente.getText());
        p.setCpf(txtCpf.getText());
        p.setDataNascimento(datePickerDataNascimento.getValue());
        p.setDiagnostico(txtDiagnostico.getText());
        p.setEscolaridade(cbxEscolaridade.getValue());
        p.setCondicaoClinica(txtCondicaoClinica.getText());
    }

    @FXML
    public void handleAssociarButtonAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/TelaCadastroResponsavel.fxml"));
            loader.setControllerFactory(c -> new TelaCadastroResponsavelController(ServiceFactory.getInstance().getResponsavelService()));
            Parent parent = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(parent));
            stage.showAndWait();

            TelaCadastroResponsavelController controller = loader.getController();
            Responsavel r = controller.getResponsavel();
            if(r != null) {
                this.responsavel = r;
                preencherResponsavel(r);
            }
        } catch (IOException e) { e.printStackTrace(); }
    }

    private void preencherResponsavel(Responsavel r) {
        lblNomeResponsavel.setText(r.getNome());
        lblCpfResponsavel.setText(r.getCpf());
        lblTelefoneResponsavel.setText(r.getTelefone());
        vboxResponsavelPreview.setVisible(true); vboxResponsavelPreview.setManaged(true);
        vboxSemResponsavel.setVisible(false); vboxSemResponsavel.setManaged(false);
        lblStatusResponsavel.setText("(Responsável associado)");
    }

    public void setPacienteParaEdicao(Paciente p) {
        this.pacienteEmEdicao = p;
        txtNomePaciente.setText(p.getNome());
        txtCpf.setText(p.getCpf());
        datePickerDataNascimento.setValue(p.getDataNascimento());
        txtDiagnostico.setText(p.getDiagnostico());
        cbxEscolaridade.setValue(p.getEscolaridade());
        txtCondicaoClinica.setText(p.getCondicaoClinica());
        if(!p.getResponsaveisLista().isEmpty()) {
            this.responsavel = p.getResponsaveisLista().get(0);
            preencherResponsavel(this.responsavel);
        }
    }

    @FXML public void handleLimparButtonAction(ActionEvent event) {
        txtNomePaciente.clear(); txtCpf.clear(); txtDiagnostico.clear(); txtCondicaoClinica.clear();
        cbxEscolaridade.setValue(null); datePickerDataNascimento.setValue(null);
        handleRemoverResponsavelButtonAction(null);
    }

    @FXML public void handleCancelarButtonAction(ActionEvent event) { ((Stage) txtNomePaciente.getScene().getWindow()).close(); }

    @FXML public void handleRemoverResponsavelButtonAction(ActionEvent event) {
        this.responsavel = null;
        vboxResponsavelPreview.setVisible(false); vboxResponsavelPreview.setManaged(false);
        vboxSemResponsavel.setVisible(true); vboxSemResponsavel.setManaged(true);
        lblStatusResponsavel.setText("(Nenhum responsável associado)");
    }
}