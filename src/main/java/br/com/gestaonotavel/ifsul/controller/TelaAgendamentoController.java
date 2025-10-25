package br.com.gestaonotavel.ifsul.controller;

import br.com.gestaonotavel.ifsul.model.Atendimento;
import br.com.gestaonotavel.ifsul.model.Especialista;
import br.com.gestaonotavel.ifsul.model.Paciente;
import br.com.gestaonotavel.ifsul.model.StatusAtendimento;
import br.com.gestaonotavel.ifsul.service.AtendimentoService;
import br.com.gestaonotavel.ifsul.service.EspecialistaService;
import br.com.gestaonotavel.ifsul.service.PacienteService;
import br.com.gestaonotavel.ifsul.util.AlertUtil;
import br.com.gestaonotavel.ifsul.util.RegraDeNegocioException;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TelaAgendamentoController implements Initializable {
    // --- Componentes do Formulário ---
    @FXML
    private ComboBox<Paciente> cbxPaciente;

    @FXML
    private ComboBox<Especialista> cbxEspecialista;

    @FXML
    private TextField txtLocal;

    @FXML
    private DatePicker datePickerData;

    @FXML
    private ComboBox<Integer> cbxHora; // ComboBox para horas

    @FXML
    private ComboBox<Integer> cbxMinuto; // ComboBox para minutos

    @FXML
    private ComboBox<StatusAtendimento> cbxStatus;

    @FXML
    private TextArea txtObservacao;

    // --- Labels de Informações Adicionais ---
    @FXML
    private Label lblValorSessao;

    @FXML
    private Label lblDuracao;

    @FXML
    private Label lblEspecialidade;

    // --- Botões de Ação ---
    @FXML
    private Button btnLimpar;

    @FXML
    private Button btnCancelar;

    @FXML
    private Button btnSalvar;

    // --- Services ---
    final PacienteService pacienteService;
    final EspecialistaService especialistaService;
    final AtendimentoService atendimentoService;

    public TelaAgendamentoController(PacienteService pacienteService, EspecialistaService especialistaService, AtendimentoService atendimentoService) {
        this.pacienteService = pacienteService;
        this.especialistaService = especialistaService;
        this.atendimentoService = atendimentoService;
    }

    // --- Métodos handle para os botões
    @FXML
    private void handleSalvarButtonAction(ActionEvent event) {
        try {
        Paciente paciente =  cbxPaciente.getSelectionModel().getSelectedItem();
        Especialista especialista =  cbxEspecialista.getSelectionModel().getSelectedItem();
        Integer horaData =  cbxHora.getSelectionModel().getSelectedItem();
        Integer minutoData =  cbxMinuto.getSelectionModel().getSelectedItem();
        LocalDate dataPaciente = datePickerData.getValue();
        String local = txtLocal.getText();
        String observacao = txtObservacao.getText();

            validarCampos(paciente, especialista, horaData, minutoData, dataPaciente, local);

        LocalDateTime dataHoraCompleta = dataPaciente.atTime(horaData, minutoData);

        Atendimento atendimento = new Atendimento();
        atendimento.setPaciente(paciente);
        atendimento.setEspecialista(especialista);
        atendimento.setDataHora(dataHoraCompleta);
        atendimento.setLocal(local);
        atendimento.setObservacao(observacao);


            atendimentoService.salvar(atendimento);
            AlertUtil.showAlert(Alert.AlertType.INFORMATION, "Sucesso!", "Atendimento criado!");
            limparFormulario();
        } catch (RegraDeNegocioException | IllegalArgumentException e) { // Captura exceções de validação específicas
            AlertUtil.showAlert(Alert.AlertType.ERROR, "Erro de Validação", e.getMessage());
        } catch (Exception e) { // Captura outros erros inesperados
            AlertUtil.showAlert(Alert.AlertType.ERROR, "Erro Inesperado", "Ocorreu um erro ao salvar o agendamento: " + e.getMessage());
            e.printStackTrace(); // Imprime o erro detalhado na consola para depuração
        }
    }

    private void configurarStatus() {
        cbxStatus.getItems().setAll(StatusAtendimento.values()); // Adiciona todos os valores do Enum
        cbxStatus.setConverter(new StringConverter<StatusAtendimento>() {
            @Override
            public String toString(StatusAtendimento status) {
                // Pode personalizar a exibição se necessário (ex: AGENDADO -> "Agendado")
                return (status == null) ? "" : status.name();
            }
            @Override
            public StatusAtendimento fromString(String string) {
                // Não necessário para ComboBox desabilitado/não editável
                return null;
            }
        });
        cbxStatus.setValue(StatusAtendimento.AGENDADO); // Define "AGENDADO" como padrão
        // O disable="true" no FXML já o desabilita visualmente
    }

    private void configurarCamposTempo() {
        // Popular horas (ex: 8 às 18)
        cbxHora.setItems(FXCollections.observableList(
                IntStream.rangeClosed(8, 18).boxed().collect(Collectors.toList())
        ));
        // Popular minutos (ex: 0, 15, 30, 45)
        cbxMinuto.setItems(FXCollections.observableArrayList(0, 15, 30, 45));
    }

    private void limparFormulario() {
        cbxPaciente.setValue(null);
        cbxEspecialista.setValue(null);
        txtLocal.clear();
        datePickerData.setValue(null);
        cbxHora.setValue(null);
        cbxMinuto.setValue(null);
        cbxStatus.setValue(null); // Assumindo que será populado depois
        txtObservacao.clear();
        cbxStatus.setValue(StatusAtendimento.AGENDADO);

        // Resetar labels de info adicional (opcional, mas bom para clareza)
        lblValorSessao.setText("R$ 0,00");
        lblDuracao.setText("-- min");
        lblEspecialidade.setText("Não selecionado");

        // Focar no primeiro campo para o próximo agendamento
        cbxPaciente.requestFocus();
    }

    private void validarCampos(Paciente paciente, Especialista especialista, Integer horaData, Integer minutoData, LocalDate data,
                               String local) {
        if (paciente == null) {
            throw new RegraDeNegocioException("Selecione um Paciente");
        }else if (especialista == null) {
            throw new RegraDeNegocioException("Selecione um Especialista");
        }else if (horaData == null) {
            throw new RegraDeNegocioException("Selecione um Hora");
        }else if (minutoData == null) {
            throw new RegraDeNegocioException("Selecione um Minuto");
        }else if (data == null) {
            throw new RegraDeNegocioException("Selecione um Data");
        }else if (local.isEmpty()) {
            throw new RegraDeNegocioException("Informe um Local");
        }
    }

    @FXML
    private void handleLimparButtonAction(ActionEvent event) { limparFormulario();  }

    @FXML
    private void handleCancelarButtonAction(ActionEvent event) {
        Stage stage = (Stage) txtLocal.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        carregarEspecialistas();
        carregarPacientes();
        configurarStatus();
        configurarCamposTempo();
        cbxEspecialista.valueProperty().addListener((observable, oldValue, newValue) -> {
            atualizarInfosAdicionais(newValue);
        });

        atualizarInfosAdicionais(null);
    }

    private void atualizarInfosAdicionais(Especialista especialista) {
        if (especialista != null) {
            // Se um especialista foi selecionado:
            // Pegue o valor da sessão, formate como moeda e coloque no lblValorSessao
            lblValorSessao.setText(String.format("R$ %.2f", especialista.getValorSessao()));

            // Pegue a duração, adicione " min" (verificando se não é null) e coloque no lblDuracao
            lblDuracao.setText(especialista.getDuracao() != null ? especialista.getDuracao() + " min" : "-- min");

            // Pegue a especialidade e coloque no lblEspecialidade
            lblEspecialidade.setText(especialista.getEspecialidade());

        } else {
            // Se nenhum especialista foi selecionado (ou a seleção foi limpa):
            // Resetar os labels para os valores padrão
            lblValorSessao.setText("R$ 0,00");
            lblDuracao.setText("-- min");
            lblEspecialidade.setText("Não selecionado");
        }
    }

    private void carregarPacientes(){
        List<Paciente> pacientesCadastrados = pacienteService.listarTodos();
        cbxPaciente.getItems().addAll(pacientesCadastrados);
        cbxPaciente.setConverter(new StringConverter<Paciente>() {
            public String toString(Paciente paciente) {
                if (paciente != null) {
                    return paciente.getNome();
                }else {
                    return "";
                }
            }

            @Override
            public Paciente fromString(String s) {
                return null;
            }
        });
    }

    private void carregarEspecialistas(){
        List<Especialista> especialistasCadastrados = especialistaService.listarTodos();
        cbxEspecialista.getItems().addAll(especialistasCadastrados);
        cbxEspecialista.setConverter(new StringConverter<Especialista>() {
            @Override
            public String toString(Especialista especialista) {
                if (especialista != null) {
                    return especialista.getNome();
                }else  {
                    return "";
                }
            }
            @Override
            public Especialista fromString(String s) {
                return null;
            }
        });
    }
}
