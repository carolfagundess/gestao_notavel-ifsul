package br.com.gestaonotavel.ifsul.controller;

import br.com.gestaonotavel.ifsul.model.Atendimento;
import br.com.gestaonotavel.ifsul.model.Especialista;
import br.com.gestaonotavel.ifsul.model.Paciente;
import br.com.gestaonotavel.ifsul.service.AtendimentoService;
import br.com.gestaonotavel.ifsul.service.EspecialistaService;
import br.com.gestaonotavel.ifsul.service.PacienteService;
import br.com.gestaonotavel.ifsul.util.AlertUtil;
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
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TelaAgendamentoController implements Initializable {
    @FXML private ComboBox<Paciente> cbxPaciente;
    @FXML private ComboBox<Especialista> cbxEspecialista;
    @FXML private TextField txtLocal;
    @FXML private DatePicker datePickerData;
    @FXML private ComboBox<Integer> cbxHora;
    @FXML private ComboBox<Integer> cbxMinuto;
    @FXML private ComboBox<String> cbxStatus; // Simplificado
    @FXML private TextArea txtObservacao;
    @FXML private Label lblValorSessao;
    @FXML private Label lblDuracao;
    @FXML private Label lblEspecialidade;
    @FXML private Button btnSalvar;
    @FXML private VBox boxCamposAgendamento;
    @FXML private Button btnNovoAgendamento;

    final PacienteService pacienteService;
    final EspecialistaService especialistaService;
    final AtendimentoService atendimentoService;

    public TelaAgendamentoController(PacienteService ps, EspecialistaService es, AtendimentoService as) {
        this.pacienteService = ps; this.especialistaService = es; this.atendimentoService = as;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        carregarCombos();
        cbxHora.getItems().addAll(IntStream.rangeClosed(8, 18).boxed().collect(Collectors.toList()));
        cbxMinuto.getItems().addAll(0, 15, 30, 45);

        cbxEspecialista.valueProperty().addListener((obs, old, newVal) -> atualizarInfos(newVal));
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
        if(e!=null) {
            lblValorSessao.setText("R$ " + e.getValorSessao());
            lblEspecialidade.setText(e.getEspecialidade());
            lblDuracao.setText(e.getDuracao() != null ? e.getDuracao() + " min" : "-- min");
        } else {
            lblValorSessao.setText("R$ 0,00");
            lblEspecialidade.setText("Não selecionado");
            lblDuracao.setText("-- min");
        }
    }

    @FXML
    void handleSalvarButtonAction(ActionEvent event) {
        try {
            Atendimento a = new Atendimento();
            a.setPaciente(cbxPaciente.getValue());
            a.setEspecialista(cbxEspecialista.getValue());
            LocalDate data = datePickerData.getValue();
            if(data != null && cbxHora.getValue() != null && cbxMinuto.getValue() != null) {
                a.setDataHora(data.atTime(cbxHora.getValue(), cbxMinuto.getValue()));
            }
            a.setLocal(txtLocal.getText());
            a.setObservacao(txtObservacao.getText());

            atendimentoService.salvar(a);
            AlertUtil.showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Agendado!");
            ((Stage)btnSalvar.getScene().getWindow()).close();
        } catch (Exception e) {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "Erro", e.getMessage());
        }
    }

    @FXML void handleCancelarButtonAction(ActionEvent event) { ((Stage)btnSalvar.getScene().getWindow()).close(); }
    @FXML void handleLimparButtonAction(ActionEvent event) { txtLocal.clear(); }

    @FXML
    public void handleNovoAgendamento(ActionEvent event) {
        boxCamposAgendamento.setVisible(true);
        boxCamposAgendamento.setManaged(true);
        btnNovoAgendamento.setDisable(true);
        // Limpa os campos
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
    }
}