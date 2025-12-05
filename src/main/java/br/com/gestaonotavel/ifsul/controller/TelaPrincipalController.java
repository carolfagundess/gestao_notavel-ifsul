package br.com.gestaonotavel.ifsul.controller;

import br.com.gestaonotavel.ifsul.model.Paciente;
import br.com.gestaonotavel.ifsul.model.Permission;
import br.com.gestaonotavel.ifsul.model.Responsavel;
import br.com.gestaonotavel.ifsul.service.AuditoriaLogService;
import br.com.gestaonotavel.ifsul.service.PacienteService;
import br.com.gestaonotavel.ifsul.service.factory.ServiceFactory;
import br.com.gestaonotavel.ifsul.util.AlertUtil;
import br.com.gestaonotavel.ifsul.util.DataChangeListener;
import br.com.gestaonotavel.ifsul.util.DataChangeManager;
import br.com.gestaonotavel.ifsul.util.SessionManager;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class TelaPrincipalController implements Initializable, DataChangeListener {

    // ==================== ELEMENTOS DA INTERFACE (FXML) ====================

    @FXML private TableView<Paciente> pacientesTableView;
    @FXML private TableColumn<Paciente, String> colunaNome;
    @FXML private TableColumn<Paciente, Integer> colunaIdade;
    @FXML private TableColumn<Paciente, String> colunaDiagnostico;
    @FXML private TableColumn<Paciente, String> colunaResponsavel;
    @FXML private TableColumn<Paciente, String> colunaSessao;
    @FXML private TableColumn<Paciente, String> colunaStatus;
    @FXML private TableColumn<Paciente, Void> colunaAcoes;

    @FXML private TextField txtBuscar;
    @FXML private ComboBox<String> cbxFiltroStatus;
    @FXML private ComboBox<String> cbxFiltroResponsavel;
    @FXML private Button btnLimparFiltros;

    // Botões do Menu Lateral
    @FXML private Button btnMenu;
    @FXML private Button btnPacientes;
    @FXML private Button btnAgendamentos;
    @FXML private Button btnAtividades;
    @FXML private Button btnRegistrarVoluntariado;
    @FXML private Button btnRelatorioVoluntariado;
    @FXML private Button btnFinanceiro;
    @FXML private Button btnRelatorios;

    // Botões do Cabeçalho
    @FXML private Button btnSair;
    @FXML private Button btnNovoPaciente;
    @FXML private Button btnNovoEspecialista; // NOVO BOTÃO

    // Labels de Estatística
    @FXML private Label lblTotalPacientes;
    @FXML private Label lblAtendimentosHoje;
    @FXML private Label lblPendentes;
    @FXML private Label lblSemResponsavel;
    @FXML private Label lblTotalRegistros;

    // ==================== SERVIÇOS E DADOS ====================

    private final PacienteService pacienteService;
    private final AuditoriaLogService auditoriaLogService;

    private ObservableList<Paciente> listaPacientes;
    private ObservableList<Paciente> listaFiltrada;

    private ChangeListener<String> filtroTextoChangeListener;
    private ChangeListener<String> filtroStatusChangeListener;
    private ChangeListener<String> filtroResponsavelChangeListener;

    // ==================== CONSTRUTOR ====================

    public TelaPrincipalController(PacienteService pacienteService, AuditoriaLogService auditoriaLogService) {
        this.pacienteService = pacienteService;
        this.auditoriaLogService = auditoriaLogService;
    }

    // ==================== INICIALIZAÇÃO ====================

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.listaPacientes = FXCollections.observableArrayList();
        this.listaFiltrada = FXCollections.observableArrayList();

        configurarTabela();
        configurarFiltros();
        carregarDados();

        // --- CONEXÃO MANUAL DOS BOTÕES ---
        if (btnFinanceiro != null) btnFinanceiro.setOnAction(this::handleAbrirFinanceiro);
        if (btnRelatorios != null) btnRelatorios.setOnAction(this::handleAbrirRelatorioVoluntarios);
        if (btnAtividades != null) btnAtividades.setOnAction(this::handleAbrirListagemAtividades);
        if (btnRegistrarVoluntariado != null) btnRegistrarVoluntariado.setOnAction(this::handleAbrirRegistroVoluntariado);
        if (btnRelatorioVoluntariado != null) btnRelatorioVoluntariado.setOnAction(this::handleAbrirRelatorioVoluntarios);
        if (btnNovoEspecialista != null) btnNovoEspecialista.setOnAction(this::handleNovoEspecialista);
        // -----------------------------------------------------

        DataChangeManager.getInstance().addDataChangeListener(this);
    }

    // ==================== AÇÕES DOS BOTÕES ====================

    @FXML
    private void handleAbrirFinanceiro(ActionEvent event) {
        abrirModal(
                "/view/TelaListagemMovimentacoes.fxml",
                "Fluxo de Caixa",
                (Callback<Class<?>, Object>) controller -> new TelaListagemMovimentacoesController(
                        ServiceFactory.getInstance().getMovimentacaoFinanceiraService()
                )
        );
    }

    @FXML
    private void handleAbrirListagemAtividades(ActionEvent event) {
        abrirModal(
                "/view/TelaListagemAtividades.fxml",
                "Gerenciamento de Atividades",
                (Callback<Class<?>, Object>) controller -> new TelaListagemAtividadesController(
                        ServiceFactory.getInstance().getAtividadeService(),
                        ServiceFactory.getInstance().getTipoAtividadeService()
                )
        );
    }

    @FXML
    private void handleAbrirRegistroVoluntariado(ActionEvent event) {
        abrirModal(
                "/view/TelaRegistroParticipacao.fxml",
                "Registrar Voluntariado",
                (Callback<Class<?>, Object>) controller -> new TelaRegistroParticipacaoController(
                        ServiceFactory.getInstance().getParticipacaoAtividadeService(),
                        ServiceFactory.getInstance().getAtividadeService(),
                        ServiceFactory.getInstance().getResponsavelService()
                )
        );
    }

    @FXML
    private void handleAbrirRelatorioVoluntarios(ActionEvent event) {
        // Cria um diálogo de escolha
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Central de Relatórios");
        alert.setHeaderText("Selecione o relatório desejado:");
        alert.setContentText("Escolha uma opção:");

        ButtonType btnVoluntarios = new ButtonType("Voluntariado");
        ButtonType btnFinanceiro = new ButtonType("Financeiro");
        ButtonType btnAuditoria = new ButtonType("Auditoria (Logs)");
        ButtonType btnCancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(btnVoluntarios, btnFinanceiro, btnAuditoria, btnCancelar);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent()) {
            if (result.get() == btnVoluntarios) {
                abrirModal("/view/TelaRelatorioVoluntarios.fxml", "Relatório de Voluntariado",
                        c -> new TelaRelatorioVoluntariosController(ServiceFactory.getInstance().getResponsavelService()));
            } else if (result.get() == btnFinanceiro) {
                abrirModal("/view/TelaRelatorioFinanceiro.fxml", "Relatório Financeiro",
                        c -> new TelaRelatorioFinanceiroController(ServiceFactory.getInstance().getMovimentacaoFinanceiraService()));
            } else if (result.get() == btnAuditoria) {
                abrirModal("/view/TelaVisualizarLogs.fxml", "Logs de Auditoria",
                        c -> new TelaVisualizarLogsController(ServiceFactory.getInstance().getAuditoriaLogService()));
            }
        }
    }

    @FXML
    private void handleNovoEspecialista(ActionEvent event) {
        abrirModal(
                "/view/TelaCadastroEspecialista.fxml",
                "Cadastro de Especialista",
                (Callback<Class<?>, Object>) controller -> new TelaCadastroEspecialistaController(
                        ServiceFactory.getInstance().getEspecialistaService()
                )
        );
    }

    @FXML
    public void handleNovoCadastroButtonAction(ActionEvent event) {
        abrirTelaCadastro(null);
    }

    @FXML
    private void handleAbrirAgendamento(ActionEvent actionEvent){
        abrirModal(
                "/view/TelaAgendamento.fxml",
                "Agendamento de Atendimentos",
                (Callback<Class<?>, Object>) controller -> new TelaAgendamentoController(
                        pacienteService,
                        ServiceFactory.getInstance().getEspecialistaService(),
                        ServiceFactory.getInstance().getAtendimentoService()
                )
        );
    }

    @FXML
    private void handleLimparFiltros(ActionEvent event) {
        txtBuscar.textProperty().removeListener(filtroTextoChangeListener);
        cbxFiltroStatus.valueProperty().removeListener(filtroStatusChangeListener);
        cbxFiltroResponsavel.valueProperty().removeListener(filtroResponsavelChangeListener);

        txtBuscar.clear();
        cbxFiltroStatus.setValue("Todos");
        cbxFiltroResponsavel.setValue("Todos");

        txtBuscar.textProperty().addListener(filtroTextoChangeListener);
        cbxFiltroStatus.valueProperty().addListener(filtroStatusChangeListener);
        cbxFiltroResponsavel.valueProperty().addListener(filtroResponsavelChangeListener);

        aplicarFiltros();
    }

    @FXML
    private void handleSairButtonAction(ActionEvent event) {
        SessionManager.getInstance().encerrarSessao();
        Stage stagePrincipal = (Stage) pacientesTableView.getScene().getWindow();
        stagePrincipal.close();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/TelaLogin.fxml"));
            loader.setControllerFactory(controller -> new TelaLoginController(ServiceFactory.getInstance().getUsuarioService()));
            Parent root = loader.load();
            Stage loginStage = new Stage();
            loginStage.setTitle("Gestão Notável - Login");
            loginStage.setScene(new Scene(root));
            loginStage.setResizable(false);
            loginStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            AlertUtil.showAlert(Alert.AlertType.ERROR, "Erro Crítico", "Não foi possível reabrir a tela de login.");
        }
    }

    // ==================== MÉTODOS AUXILIARES E LÓGICA ====================

    private void configurarTabela() {
        colunaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colunaIdade.setCellValueFactory(cellData -> {
            LocalDate dn = cellData.getValue().getDataNascimento();
            int idade = (dn != null) ? Period.between(dn, LocalDate.now()).getYears() : 0;
            return new SimpleIntegerProperty(idade).asObject();
        });
        colunaDiagnostico.setCellValueFactory(new PropertyValueFactory<>("diagnostico"));
        colunaResponsavel.setCellValueFactory(cellData -> {
            List<Responsavel> resp = cellData.getValue().getResponsaveisLista();
            return new SimpleStringProperty((resp != null && !resp.isEmpty()) ? resp.get(0).getNome() : "Sem responsável");
        });
        colunaSessao.setCellValueFactory(cellData -> new SimpleStringProperty("--/--/----"));

        colunaStatus.setCellValueFactory(cellData -> new SimpleStringProperty("Ativo"));
        colunaStatus.setCellFactory(col -> new TableCell<Paciente, String>() {
            @Override protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null); setStyle("");
                } else {
                    setText(item);
                    setStyle("-fx-background-color: #E8F5E9; -fx-text-fill: #2E7D32; -fx-font-weight: bold; -fx-background-radius: 12; -fx-padding: 5 12; -fx-alignment: CENTER;");
                }
            }
        });

        configurarColunaAcoes();
        pacientesTableView.setPlaceholder(new Label("Nenhum paciente cadastrado"));
    }

    private void configurarColunaAcoes() {
        SessionManager session = SessionManager.getInstance();
        colunaAcoes.setCellFactory(param -> new TableCell<Paciente, Void>() {
            private final Button btnVisualizar = criarBotaoAcao("👁", "#2196F3", "Visualizar");
            private final Button btnEditar = criarBotaoAcao("✏", "#FF9800", "Editar");
            private final Button btnExcluir = criarBotaoAcao("🗑", "#F44336", "Excluir");
            private final HBox container = new HBox(8, btnVisualizar, btnEditar, btnExcluir);
            {
                container.setAlignment(Pos.CENTER);
                btnEditar.setDisable(!session.hasPermission(Permission.EDITAR_PACIENTE));
                btnExcluir.setDisable(!session.hasPermission(Permission.EXCLUIR_PACIENTE));

                btnVisualizar.setOnAction(event -> visualizarPaciente(getTableView().getItems().get(getIndex())));
                btnEditar.setOnAction(event -> editarPaciente(getTableView().getItems().get(getIndex())));
                btnExcluir.setOnAction(event -> excluirPaciente(getTableView().getItems().get(getIndex())));
            }
            @Override protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : container);
            }
        });
    }

    private Button criarBotaoAcao(String text, String color, String tooltip) {
        Button btn = new Button(text);
        btn.setStyle("-fx-background-color: " + color + "; -fx-text-fill: white; -fx-font-size: 14; -fx-padding: 6 12; -fx-background-radius: 6; -fx-cursor: hand; -fx-min-width: 38; -fx-pref-width: 38;");
        btn.setTooltip(new Tooltip(tooltip));
        return btn;
    }

    private void configurarFiltros() {
        cbxFiltroStatus.getItems().addAll("Todos", "Ativo", "Inativo", "Pendente");
        cbxFiltroStatus.setValue("Todos");
        cbxFiltroResponsavel.getItems().add("Todos");
        cbxFiltroResponsavel.setValue("Todos");

        filtroTextoChangeListener = (obs, oldVal, newVal) -> { if (newVal != null) aplicarFiltros(); };
        filtroStatusChangeListener = (obs, oldVal, newVal) -> { if (newVal != null) aplicarFiltros(); };
        filtroResponsavelChangeListener = (obs, oldVal, newVal) -> { if (newVal != null) aplicarFiltros(); };

        txtBuscar.textProperty().addListener(filtroTextoChangeListener);
        cbxFiltroStatus.valueProperty().addListener(filtroStatusChangeListener);
        cbxFiltroResponsavel.valueProperty().addListener(filtroResponsavelChangeListener);
    }

    private void aplicarFiltros() {
        String busca = txtBuscar.getText() != null ? txtBuscar.getText().toLowerCase().trim() : "";
        String responsavel = cbxFiltroResponsavel.getValue() != null ? cbxFiltroResponsavel.getValue() : "Todos";

        listaFiltrada.clear();
        listaFiltrada.addAll(listaPacientes.stream()
                .filter(p -> busca.isEmpty() ||
                        (p.getNome() != null && p.getNome().toLowerCase().contains(busca)) ||
                        (p.getCpf() != null && p.getCpf().contains(busca)) ||
                        (p.getDiagnostico() != null && p.getDiagnostico().toLowerCase().contains(busca)))
                .filter(p -> {
                    if (responsavel.equals("Todos")) return true;
                    List<Responsavel> resp = p.getResponsaveisLista();
                    return resp != null && !resp.isEmpty() && resp.get(0).getNome().equals(responsavel);
                })
                .collect(Collectors.toList()));

        pacientesTableView.setItems(listaFiltrada);
        atualizarContadores();
    }

    private void carregarDados() {
        try {
            listaPacientes.clear();
            listaPacientes.addAll(pacienteService.listarTodos());
            atualizarComboResponsaveis();
            aplicarFiltros();
        } catch (Exception e) {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao carregar dados: " + e.getMessage());
        }
    }

    private void atualizarComboResponsaveis() {
        if (filtroResponsavelChangeListener != null) cbxFiltroResponsavel.valueProperty().removeListener(filtroResponsavelChangeListener);

        String selecionado = cbxFiltroResponsavel.getValue();
        cbxFiltroResponsavel.getItems().clear();
        cbxFiltroResponsavel.getItems().add("Todos");

        listaPacientes.stream()
                .flatMap(p -> p.getResponsaveisLista().stream())
                .filter(r -> r != null)
                .map(Responsavel::getNome)
                .distinct()
                .sorted()
                .forEach(cbxFiltroResponsavel.getItems()::add);

        cbxFiltroResponsavel.setValue(cbxFiltroResponsavel.getItems().contains(selecionado) ? selecionado : "Todos");

        if (filtroResponsavelChangeListener != null) cbxFiltroResponsavel.valueProperty().addListener(filtroResponsavelChangeListener);
    }

    private void atualizarContadores() {
        lblTotalPacientes.setText(String.valueOf(listaPacientes.size()));
        lblTotalRegistros.setText(listaFiltrada.size() + " registro(s)");
        long semResp = listaPacientes.stream().filter(p -> p.getResponsaveisLista() == null || p.getResponsaveisLista().isEmpty()).count();
        lblSemResponsavel.setText(String.valueOf(semResp));
        lblAtendimentosHoje.setText("0");
        lblPendentes.setText("0");
    }

    private void visualizarPaciente(Paciente paciente) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Visualizar Paciente");
        dialog.setHeaderText("📋 Informações de " + paciente.getNome());

        GridPane grid = new GridPane();
        grid.setHgap(15); grid.setVgap(12); grid.setPadding(new Insets(20));
        grid.setStyle("-fx-background-color: white;");

        String labelStyle = "-fx-font-weight: bold; -fx-text-fill: #495057;";
        String valueStyle = "-fx-text-fill: #212529;";

        int row = 0;
        grid.add(new Label("DADOS PESSOAIS"), 0, row++, 2, 1);
        grid.add(new Separator(), 0, row++, 2, 1);

        addLabelValue(grid, row++, "Nome:", paciente.getNome(), labelStyle, valueStyle);
        addLabelValue(grid, row++, "Idade:", Period.between(paciente.getDataNascimento(), LocalDate.now()).getYears() + " anos", labelStyle, valueStyle);
        addLabelValue(grid, row++, "CPF:", paciente.getCpf() != null ? paciente.getCpf() : "Não informado", labelStyle, valueStyle);

        row++;
        grid.add(new Label("INFORMAÇÕES CLÍNICAS"), 0, row++, 2, 1);
        grid.add(new Separator(), 0, row++, 2, 1);
        addLabelValue(grid, row++, "Diagnóstico:", paciente.getDiagnostico(), labelStyle, valueStyle);
        addLabelValue(grid, row++, "Condição:", paciente.getCondicaoClinica(), labelStyle, valueStyle);

        ScrollPane scroll = new ScrollPane(grid);
        scroll.setFitToWidth(true); scroll.setPrefSize(500, 450);
        dialog.getDialogPane().setContent(scroll);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        dialog.showAndWait();
    }

    private void addLabelValue(GridPane grid, int row, String labelText, String valueText, String labelStyle, String valueStyle) {
        Label label = new Label(labelText); label.setStyle(labelStyle); label.setMinWidth(120);
        Label value = new Label(valueText); value.setStyle(valueStyle); value.setWrapText(true); value.setMaxWidth(300);
        grid.add(label, 0, row); grid.add(value, 1, row);
    }

    private void editarPaciente(Paciente paciente) {
        abrirTelaCadastro(paciente);
    }

    private void excluirPaciente(Paciente paciente) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Tem certeza que deseja excluir " + paciente.getNome() + "?", ButtonType.YES, ButtonType.NO);
        alert.setHeaderText("Esta ação é irreversível.");

        if (alert.showAndWait().orElse(ButtonType.NO) == ButtonType.YES) {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Segurança");
            dialog.setHeaderText("Digite EXCLUIR para confirmar:");

            Optional<String> result = dialog.showAndWait();
            if (result.isPresent() && result.get().equals("EXCLUIR")) {
                try {
                    pacienteService.deletarPaciente(paciente);
                    auditoriaLogService.registrarAcao("Excluiu paciente ID: " + paciente.getId() + ", Nome: " + paciente.getNome());
                    AlertUtil.showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Paciente excluído.");
                    carregarDados();
                } catch (Exception e) {
                    AlertUtil.showAlert(Alert.AlertType.ERROR, "Erro", "Falha ao excluir: " + e.getMessage());
                }
            } else {
                AlertUtil.showAlert(Alert.AlertType.WARNING, "Cancelado", "Texto de confirmação incorreto.");
            }
        }
    }

    private void abrirTelaCadastro(Paciente paciente) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/TelaCadastroPaciente.fxml"));
            fxmlLoader.setControllerFactory(c -> new TelaCadastroPacienteController(ServiceFactory.getInstance().getPacienteService()));
            Parent root = fxmlLoader.load();

            if (paciente != null) {
                TelaCadastroPacienteController controller = fxmlLoader.getController();
                controller.setPacienteParaEdicao(paciente);
            }

            Stage stage = new Stage();
            stage.setTitle(paciente == null ? "Cadastro de Pacientes" : "Editar Paciente");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            if(pacientesTableView.getScene() != null) stage.initOwner(pacientesTableView.getScene().getWindow());

            if (paciente != null) {
                Button btnSalvar = (Button) root.lookup("#btnSalvar");
                if (btnSalvar != null) btnSalvar.setText("Atualizar");
            }
            stage.showAndWait();
        } catch (IOException ex) {
            ex.printStackTrace();
            AlertUtil.showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao abrir tela de cadastro.");
        }
    }

    private void abrirModal(String fxmlPath, String title, Callback<Class<?>, Object> controllerFactory) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlPath));
            fxmlLoader.setControllerFactory(controllerFactory);
            Parent parent = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(parent));
            stage.setResizable(false);
            stage.initModality(Modality.APPLICATION_MODAL);
            if (pacientesTableView.getScene() != null) {
                stage.initOwner(pacientesTableView.getScene().getWindow());
            }
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            AlertUtil.showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao abrir tela: " + e.getMessage());
        }
    }

    @Override
    public void atualizarDados(String entidade) {
        if ("Paciente".equals(entidade)) {
            carregarDados();
        }
    }
}