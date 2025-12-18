package br.com.gestaonotavel.ifsul.controller;

import br.com.gestaonotavel.ifsul.model.Atendimento;
import br.com.gestaonotavel.ifsul.model.Especialista;
import br.com.gestaonotavel.ifsul.model.Paciente;
import br.com.gestaonotavel.ifsul.model.StatusAtendimento;
import br.com.gestaonotavel.ifsul.service.AtendimentoService;
import br.com.gestaonotavel.ifsul.service.EspecialistaService;
import br.com.gestaonotavel.ifsul.service.PacienteService;
import br.com.gestaonotavel.ifsul.util.AlertUtil;
import br.com.gestaonotavel.ifsul.util.DataChangeManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TelaAgendamentoController extends BaseController implements Initializable {
    @FXML private ComboBox<Paciente> cbxPaciente;
    @FXML private ComboBox<Especialista> cbxEspecialista;
    @FXML private TextField txtLocal;
    @FXML private DatePicker datePickerData;
    @FXML private ComboBox<Integer> cbxHora;
    @FXML private ComboBox<Integer> cbxMinuto;
    @FXML private ComboBox<StatusAtendimento> cbxStatus;
    @FXML private TextArea txtObservacao;
    @FXML private Label lblValorSessao;
    @FXML private Label lblDuracao;
    @FXML private Label lblEspecialidade;
    @FXML private Button btnSalvar;
    @FXML private Button btnLimpar;
    @FXML private Button btnCancelar;
    @FXML private VBox boxCamposAgendamento;

    private final PacienteService pacienteService;
    private final EspecialistaService especialistaService;
    private final AtendimentoService atendimentoService;

    private Atendimento atendimentoParaEdicao;

    public TelaAgendamentoController(PacienteService ps, EspecialistaService es, AtendimentoService as) {
        this.pacienteService = ps;
        this.especialistaService = es;
        this.atendimentoService = as;
    }

    public void setAtendimentoParaEdicao(Atendimento atendimento) {
        this.atendimentoParaEdicao = atendimento;
        if (cbxPaciente != null) {
            preencherFormularioParaEdicao();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        btnSalvar.setOnAction(this::handleSalvarButtonAction);
        btnLimpar.setOnAction(this::handleLimparButtonAction);
        btnCancelar.setOnAction(this::handleCancelarButtonAction);

        carregarCombos();
        ObservableList<Integer> horas = FXCollections.observableArrayList(IntStream.rangeClosed(8, 18).boxed().collect(Collectors.toList()));
        ObservableList<Integer> minutos = FXCollections.observableArrayList(0, 15, 30, 45);
        cbxHora.setItems(horas);
        cbxMinuto.setItems(minutos);

        // Popula o ComboBox de Status
        cbxStatus.getItems().setAll(StatusAtendimento.values());
        cbxStatus.setDisable(true); // Desabilitado por padrão

        // Listeners
        cbxPaciente.valueProperty().addListener((obs, old, newVal) -> cbxPaciente.getStyleClass().remove("error"));
        cbxEspecialista.valueProperty().addListener((obs, old, newVal) -> {
            cbxEspecialista.getStyleClass().remove("error");
            atualizarInfos(newVal);
            atualizarHorariosDisponiveis();
        });
        datePickerData.valueProperty().addListener((obs, old, newVal) -> {
            datePickerData.getStyleClass().remove("error");
            atualizarHorariosDisponiveis();
        });
        cbxHora.valueProperty().addListener((obs, old, newVal) -> cbxHora.getStyleClass().remove("error"));
        cbxMinuto.valueProperty().addListener((obs, old, newVal) -> cbxMinuto.getStyleClass().remove("error"));
        txtLocal.textProperty().addListener((obs, old, newVal) -> txtLocal.getStyleClass().remove("error"));

        if (atendimentoParaEdicao != null) {
            preencherFormularioParaEdicao();
        }
    }

    private void preencherFormularioParaEdicao() {
        cbxPaciente.setValue(atendimentoParaEdicao.getPaciente());
        cbxEspecialista.setValue(atendimentoParaEdicao.getEspecialista());
        txtLocal.setText(atendimentoParaEdicao.getLocal());
        
        LocalDateTime dataHora = atendimentoParaEdicao.getDataHora();
        if (dataHora != null) {
            datePickerData.setValue(dataHora.toLocalDate());
            cbxHora.setValue(dataHora.getHour());
            cbxMinuto.setValue(dataHora.getMinute());
        }
        
        txtObservacao.setText(atendimentoParaEdicao.getObservacao());
        
        // Habilita e seleciona o status
        cbxStatus.setDisable(false);
        cbxStatus.setValue(atendimentoParaEdicao.getStatusAtendimento());

        atualizarHorariosDisponiveis();
    }

    private void carregarCombos() {
        cbxPaciente.getItems().setAll(pacienteService.listarTodos());
        cbxPaciente.setConverter(new StringConverter<Paciente>() {
            @Override public String toString(Paciente p) { return p == null ? "" : p.getNome(); }
            @Override public Paciente fromString(String s) { return null; }
        });

        cbxEspecialista.getItems().setAll(especialistaService.listarTodos());
        cbxEspecialista.setConverter(new StringConverter<Especialista>() {
            @Override public String toString(Especialista e) { return e == null ? "" : e.getNome(); }
            @Override public Especialista fromString(String s) { return null; }
        });
    }

    private void atualizarInfos(Especialista e) {
        if (e != null) {
            lblValorSessao.setText("R$ " + String.format("%.2f", e.getValorSessao()));
            lblEspecialidade.setText(e.getEspecialidade());
            lblDuracao.setText(e.getDuracao() != null ? e.getDuracao() + " min" : "-- min");
        } else {
            lblValorSessao.setText("R$ 0,00");
            lblEspecialidade.setText("Não selecionado");
            lblDuracao.setText("-- min");
        }
    }

    private void atualizarHorariosDisponiveis() {
        Especialista especialista = cbxEspecialista.getValue();
        LocalDate data = datePickerData.getValue();

        if (especialista == null || data == null) {
            cbxHora.setDisable(true);
            cbxMinuto.setDisable(true);
            return;
        }

        cbxHora.setDisable(false);
        cbxMinuto.setDisable(false);

        List<Atendimento> atendimentosDoDia = atendimentoService.listarTodos(especialista.getIdEspecialista())
                .stream()
                .filter(a -> a.getDataHora().toLocalDate().equals(data))
                .collect(Collectors.toList());

        cbxHora.setCellFactory(lv -> new ListCell<Integer>() {
            @Override
            protected void updateItem(Integer hora, boolean empty) {
                super.updateItem(hora, empty);
                if (empty || hora == null) {
                    setText(null);
                    setDisable(false);
                } else {
                    setText(String.format("%02d", hora));
                    long horariosOcupadosNaHora = atendimentosDoDia.stream()
                            .filter(a -> a.getDataHora().getHour() == hora)
                            .count();
                    if (horariosOcupadosNaHora >= 4) {
                        setDisable(true);
                        setStyle("-fx-background-color: #FFEBEE; -fx-text-fill: #BDBDBD;");
                    } else {
                        setDisable(false);
                        setStyle("");
                    }
                }
            }
        });
    }

    private void handleSalvarButtonAction(ActionEvent event) {
        limparEstilosDeErro();
        if (!validarCampos()) {
            return;
        }

        try {
            Atendimento atendimento = (atendimentoParaEdicao == null) ? new Atendimento() : atendimentoParaEdicao;

            atendimento.setPaciente(cbxPaciente.getValue());
            atendimento.setEspecialista(cbxEspecialista.getValue());
            
            LocalDate data = datePickerData.getValue();
            atendimento.setDataHora(data.atTime(cbxHora.getValue(), cbxMinuto.getValue()));
            
            atendimento.setLocal(txtLocal.getText());
            atendimento.setObservacao(txtObservacao.getText());

            // Salva o status se estiver em modo de edição
            if (atendimentoParaEdicao != null) {
                atendimento.setStatusAtendimento(cbxStatus.getValue());
            }

            atendimentoService.salvar(atendimento);
            AlertUtil.showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Agendamento salvo com sucesso!");
            
            DataChangeManager.getInstance().notificarListeners("Atendimento");
            closeStage();
        } catch (Exception e) {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao salvar o agendamento: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private boolean validarCampos() {
        StringBuilder erros = new StringBuilder();
        if (cbxPaciente.getValue() == null) {
            erros.append("O campo 'Paciente' é obrigatório.\n");
            cbxPaciente.getStyleClass().add("error");
        }
        if (cbxEspecialista.getValue() == null) {
            erros.append("O campo 'Especialista' é obrigatório.\n");
            cbxEspecialista.getStyleClass().add("error");
        }
        if (datePickerData.getValue() == null) {
            erros.append("O campo 'Data do Atendimento' é obrigatório.\n");
            datePickerData.getStyleClass().add("error");
        }
        if (cbxHora.getValue() == null || cbxMinuto.getValue() == null) {
            erros.append("Os campos de 'Horário' são obrigatórios.\n");
            if (cbxHora.getValue() == null) cbxHora.getStyleClass().add("error");
            if (cbxMinuto.getValue() == null) cbxMinuto.getStyleClass().add("error");
        }
        if (txtLocal.getText() == null || txtLocal.getText().trim().isEmpty()) {
            erros.append("O campo 'Local do Atendimento' é obrigatório.\n");
            txtLocal.getStyleClass().add("error");
        }

        if (erros.length() > 0) {
            AlertUtil.showAlert(Alert.AlertType.WARNING, "Campos Obrigatórios", "Por favor, corrija os campos indicados:\n\n" + erros.toString());
            return false;
        }
        return true;
    }

    private void limparEstilosDeErro() {
        cbxPaciente.getStyleClass().remove("error");
        cbxEspecialista.getStyleClass().remove("error");
        datePickerData.getStyleClass().remove("error");
        cbxHora.getStyleClass().remove("error");
        cbxMinuto.getStyleClass().remove("error");
        txtLocal.getStyleClass().remove("error");
    }

    private void handleCancelarButtonAction(ActionEvent event) {
        closeStage();
    }
    
    private void handleLimparButtonAction(ActionEvent event) {
        limparEstilosDeErro();
        atendimentoParaEdicao = null;
        cbxPaciente.getSelectionModel().clearSelection();
        cbxEspecialista.getSelectionModel().clearSelection();
        txtLocal.clear();
        datePickerData.setValue(null);
        cbxHora.getSelectionModel().clearSelection();
        cbxMinuto.getSelectionModel().clearSelection();
        txtObservacao.clear();
        lblValorSessao.setText("R$ 0,00");
        lblDuracao.setText("-- min");
        lblEspecialidade.setText("Não selecionado");
        cbxStatus.setDisable(true);
        cbxStatus.getSelectionModel().clearSelection();
    }

    private void closeStage() {
        Stage stage = (Stage) btnSalvar.getScene().getWindow();
        if (stage != null) {
            stage.close();
        }
    }
}
