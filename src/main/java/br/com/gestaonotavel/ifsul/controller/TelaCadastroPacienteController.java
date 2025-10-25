package br.com.gestaonotavel.ifsul.controller;

import br.com.gestaonotavel.ifsul.model.Paciente;
import br.com.gestaonotavel.ifsul.model.Responsavel;
import br.com.gestaonotavel.ifsul.service.PacienteService;
import br.com.gestaonotavel.ifsul.service.ResponsavelService;
import br.com.gestaonotavel.ifsul.service.factory.ServiceFactory;
import br.com.gestaonotavel.ifsul.util.AlertUtil;
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
    private TextField txtNomePaciente;

    @FXML
    private TextField txtCpf;

    @FXML
    private DatePicker datePickerDataNascimento;

    @FXML
    private TextField txtDiagnostico;

    @FXML
    private ComboBox<String> cbxEscolaridade;

    @FXML
    private TextArea txtCondicaoClinica;

    @FXML
    private Label lblNomeResponsavel;

    @FXML
    private Label lblCpfResponsavel;

    @FXML
    private Label lblStatusResponsavel;

    @FXML
    private Button btnSalvar;

    @FXML
    private Button btnLimpar;

    @FXML
    private Button btnCancelar;

    @FXML
    private Button btnAssociar;

    @FXML
    private VBox vboxResponsavelPreview;

    @FXML
    private VBox vboxSemResponsavel;

    @FXML
    private Button btnRemoverResponsavel;

    @FXML
    private Label lblTelefoneResponsavel;

    private final PacienteService pacienteService;
    private Responsavel responsavel;
    private Paciente pacienteEmEdicao = null;

    public TelaCadastroPacienteController(PacienteService pacienteService) {
        this.pacienteService = pacienteService;
    }


    /**
     * Método para o botão de salvar o cliente
     *
     */
    @FXML
    public void handleSalvarButtonAction(ActionEvent event) {
        try {
            validarCamposPreenchidos();
            Paciente pacienteSalvado;
            String mensagemAlerta;

            if(pacienteEmEdicao == null){
                Paciente paciente = new Paciente();
                pacienteSalvado = construirPaciente(paciente);
                mensagemAlerta = "Paciente salvo com sucesso!";
                if (this.responsavel != null) {
                    pacienteService.criarEAssociarResponsavel(this.responsavel, pacienteSalvado);
                }else{
                    pacienteService.salvarPaciente(pacienteSalvado);
                }
            }else{
                pacienteSalvado = pacienteEmEdicao;
                popularPaciente(pacienteEmEdicao);
                mensagemAlerta = "Paciente atualizado com sucesso!";

                pacienteSalvado.getResponsaveisLista().clear();
                if (this.responsavel != null) {
                    // ...adiciona ele à lista.
                    pacienteSalvado.getResponsaveisLista().add(this.responsavel);
                }

                pacienteService.salvarPaciente(pacienteSalvado);
            }
            AlertUtil.showAlert(Alert.AlertType.INFORMATION,
                    "Sucesso",
                    mensagemAlerta);

            if (pacienteEmEdicao == null) {
                limparFormulario();
            }else{
                handleCancelarButtonAction(event);
            }

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

    /**
     * Método para limpar o formulário
     */
    @FXML
    public void handleLimparButtonAction(ActionEvent event) {
        limparFormulario();
    }

    /**
     * Método para cancelar e fechar a janela
     */
    @FXML
    public void handleCancelarButtonAction(ActionEvent event) {
        Stage stage = (Stage) txtNomePaciente.getScene().getWindow();
        stage.close();
    }

    /**
     * Ira associar um responsavel
     */
    @FXML
    public void handleAssociarButtonAction(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/TelaCadastroResponsavel.fxml"));
            fxmlLoader.setControllerFactory(TelaCadastroResponsavelController -> {
                ServiceFactory serviceFactory =  ServiceFactory.getInstance();
                return new TelaCadastroResponsavelController(serviceFactory.getResponsavelService());
            });
            Parent parent= fxmlLoader.load();

            Scene scene = new Scene(parent);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Cadastro Paciente");
            stage.setResizable(false);
            stage.showAndWait();
            TelaCadastroResponsavelController telaCadastroResponsavelController = fxmlLoader.getController();
            this.responsavel = telaCadastroResponsavelController.getResponsavel();
            if(this.responsavel != null){
                preencherFormularioResponsavel(this.responsavel);
            }
        }catch (IOException ex){
            System.out.println(ex.getMessage());
            AlertUtil.showAlert(Alert.AlertType.ERROR, "Erro", "Ocorreu um erro ao abrir a tela de cadastro");
        }
    }

    @FXML
    public void handleRemoverResponsavelButtonAction(ActionEvent event) {
        this.responsavel = null;

        // Limpar labels
        lblNomeResponsavel.setText("-");
        lblCpfResponsavel.setText("-");
        lblTelefoneResponsavel.setText("-");

        // Ocultar preview e mostrar aviso
        vboxResponsavelPreview.setVisible(false);
        vboxResponsavelPreview.setManaged(false);
        vboxSemResponsavel.setVisible(true);
        vboxSemResponsavel.setManaged(true);
        lblStatusResponsavel.setText("(Nenhum responsável associado)");
    }

    /**
     * Valida se todos os campos obrigatórios foram preenchidos
     */
    private void validarCamposPreenchidos() throws RegraDeNegocioException {
        if (txtNomePaciente.getText() == null || txtNomePaciente.getText().trim().isEmpty()) {
            throw new RegraDeNegocioException("Preencha o nome do paciente");
        }
        if (datePickerDataNascimento.getValue() == null) {
            throw new RegraDeNegocioException("Preencha a data de nascimento");
        }
        if (txtDiagnostico.getText() == null || txtDiagnostico.getText().trim().isEmpty()) {
            throw new RegraDeNegocioException("Preencha o diagnóstico");
        }
        if (cbxEscolaridade.getValue() == null || cbxEscolaridade.getValue().isEmpty()) {
            throw new RegraDeNegocioException("Selecione a escolaridade");
        }
        if (txtCondicaoClinica.getText() == null || txtCondicaoClinica.getText().trim().isEmpty()) {
            throw new RegraDeNegocioException("Preencha a condição clínica");
        }
    }

    private Paciente construirPaciente(Paciente paciente) {
        popularPaciente(paciente);
        return paciente;
    }

    private void popularPaciente(Paciente paciente) {
        paciente.setNome(txtNomePaciente.getText().trim());
        paciente.setCpf(txtCpf.getText().trim().isEmpty() ? null : txtCpf.getText().trim());
        paciente.setDataNascimento(datePickerDataNascimento.getValue());
        paciente.setDiagnostico(txtDiagnostico.getText().trim());
        paciente.setEscolaridade(cbxEscolaridade.getValue());
        paciente.setCondicaoClinica(txtCondicaoClinica.getText().trim());
    }

    private void preencherFormularioResponsavel(Responsavel responsavel) {
        lblNomeResponsavel.setText(responsavel.getNome());
        lblCpfResponsavel.setText(responsavel.getCpf());
        lblTelefoneResponsavel.setText(responsavel.getTelefone());

        vboxResponsavelPreview.setVisible(true);
        vboxResponsavelPreview.setManaged(true);
        vboxSemResponsavel.setVisible(false);
        vboxSemResponsavel.setManaged(false);
        lblStatusResponsavel.setText("(Responsável associado)");
    }

    // * Limpa todos os campos do formulário
    private void limparFormulario() {
        txtNomePaciente.clear();
        txtCpf.clear();
        txtDiagnostico.clear();
        txtCondicaoClinica.clear();
        cbxEscolaridade.setValue(null);
        datePickerDataNascimento.setValue(null);
        txtNomePaciente.requestFocus();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Preencher ComboBox de Escolaridade
        cbxEscolaridade.getItems().addAll(
                "Sem escolaridade",
                "Ensino fundamental incompleto",
                "Ensino fundamental completo",
                "Ensino médio incompleto",
                "Ensino médio completo",
                "Ensino superior incompleto",
                "Ensino superior completo"
        );

    }

    public void setPacienteParaEdicao(Paciente paciente) {
        this.pacienteEmEdicao = paciente;
        // Preenche os campos do formulário com os dados do paciente
        txtNomePaciente.setText(paciente.getNome());
        // Verifica se o CPF não é nulo antes de tentar preencher
        if (paciente.getCpf() != null) {
            txtCpf.setText(paciente.getCpf());
        } else {
            txtCpf.clear(); // Limpa o campo se o paciente não tiver CPF
        }
        datePickerDataNascimento.setValue(paciente.getDataNascimento());
        txtDiagnostico.setText(paciente.getDiagnostico());
        cbxEscolaridade.setValue(paciente.getEscolaridade()); // O ComboBox deve ter os mesmos valores que foram usados para salvar
        txtCondicaoClinica.setText(paciente.getCondicaoClinica());

        // Preenche os dados do responsável, se houver
        if (paciente.getResponsaveisLista() != null && !paciente.getResponsaveisLista().isEmpty()) {
            // Assume que estamos interessados no primeiro responsável da lista
            Responsavel responsavelDoPaciente = paciente.getResponsaveisLista().get(0);
            this.responsavel = responsavelDoPaciente; // Guarda a referência do responsável atual
            preencherFormularioResponsavel(responsavelDoPaciente);
        } else {
            // Garante que a área do responsável fique vazia se não houver um associado
            handleRemoverResponsavelButtonAction(null); // Reutiliza a lógica para limpar a área do responsável
        }

    }
}
