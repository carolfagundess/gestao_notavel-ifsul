package br.com.gestaonotavel.ifsul.controller;

import br.com.gestaonotavel.ifsul.model.Paciente;
import br.com.gestaonotavel.ifsul.model.Responsavel;
import br.com.gestaonotavel.ifsul.service.PacienteService;
import br.com.gestaonotavel.ifsul.util.AlertUtil;
import br.com.gestaonotavel.ifsul.util.DataChangeListener;
import br.com.gestaonotavel.ifsul.util.DataChangeManager;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener; // Importação necessária
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * Controller da Tela Principal
 * Gerencia a listagem e operações dos pacientes
 */
public class TelaPrincipalController implements Initializable, DataChangeListener {

    // ==================== TABELA ====================
    @FXML
    private TableView<Paciente> pacientesTableView;
    @FXML
    private TableColumn<Paciente, String> colunaNome;
    @FXML
    private TableColumn<Paciente, Integer> colunaIdade;
    @FXML
    private TableColumn<Paciente, String> colunaDiagnostico;
    @FXML
    private TableColumn<Paciente, String> colunaResponsavel;
    @FXML
    private TableColumn<Paciente, String> colunaSessao;
    @FXML
    private TableColumn<Paciente, String> colunaStatus;
    @FXML
    private TableColumn<Paciente, Void> colunaAcoes;

    // ==================== FILTROS ====================
    @FXML
    private TextField txtBuscar;
    @FXML
    private ComboBox<String> cbxFiltroStatus;
    @FXML
    private ComboBox<String> cbxFiltroResponsavel;
    @FXML
    private Button btnLimparFiltros;

    // ==================== ESTATÍSTICAS ====================
    @FXML
    private Label lblTotalPacientes;
    @FXML
    private Label lblAtendimentosHoje;
    @FXML
    private Label lblPendentes;
    @FXML
    private Label lblSemResponsavel;
    @FXML
    private Label lblTotalRegistros;

    // ==================== SERVIÇOS E LISTAS ====================
    private PacienteService pacienteService;
    private ObservableList<Paciente> listaPacientes;
    private ObservableList<Paciente> listaFiltrada;

    // ==================== LISTENERS (NOVO) ====================
    private ChangeListener<String> filtroTextoChangeListener;
    private ChangeListener<String> filtroStatusChangeListener;
    private ChangeListener<String> filtroResponsavelChangeListener;


    // ==================== INITIALIZE ====================
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.pacienteService = new PacienteService();
        this.listaPacientes = FXCollections.observableArrayList();
        this.listaFiltrada = FXCollections.observableArrayList();

        configurarTabela();
        configurarFiltros(); // Chama o método que agora cria e armazena os listeners
        carregarDados();

        DataChangeManager.getInstance().addDataChangeListener(this);
    }

    // ==================== CONFIGURAÇÃO DA TABELA ====================
    private void configurarTabela() {
        // Configurar colunas básicas
        colunaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));

        // Coluna Idade (calculada)
        colunaIdade.setCellValueFactory(cellData -> {
            LocalDate dataNascimento = cellData.getValue().getDataNascimento();
            int idade = Period.between(dataNascimento, LocalDate.now()).getYears();
            return new SimpleIntegerProperty(idade).asObject();
        });

        colunaDiagnostico.setCellValueFactory(new PropertyValueFactory<>("diagnostico"));

        // Coluna Responsável
        colunaResponsavel.setCellValueFactory(cellData -> {
            List<Responsavel> responsaveis = cellData.getValue().getResponsaveisLista();
            if (responsaveis != null && !responsaveis.isEmpty()) {
                return new SimpleStringProperty(responsaveis.get(0).getNome());
            }
            return new SimpleStringProperty("Sem responsável");
        });

        // Coluna Próxima Sessão (placeholder)
        colunaSessao.setCellValueFactory(cellData ->
                new SimpleStringProperty("--/--/----")
        );

        // Coluna Status com estilização
        colunaStatus.setCellValueFactory(cellData ->
                new SimpleStringProperty("Ativo")
        );
        colunaStatus.setCellFactory(col -> new TableCell<Paciente, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    setStyle("-fx-background-color: #E8F5E9; -fx-text-fill: #2E7D32; " +
                            "-fx-font-weight: bold; -fx-background-radius: 12; " +
                            "-fx-padding: 5 12; -fx-alignment: CENTER;");
                }
            }
        });

        // Configurar coluna de ações
        configurarColunaAcoes();

        // Estilo da tabela
        pacientesTableView.setPlaceholder(new Label("Nenhum paciente cadastrado"));

        // Estilo das linhas alternadas (opcional, pode ser melhorado via CSS)
        pacientesTableView.setRowFactory(tv -> {
            TableRow<Paciente> row = new TableRow<>();
            row.setStyle("-fx-background-color: transparent;"); // Reset base style
            row.indexProperty().addListener((obs, oldIndex, newIndex) -> {
                if (newIndex.intValue() % 2 == 0) {
                    row.setStyle("-fx-background-color: #f9f9f9;");
                } else {
                    row.setStyle("-fx-background-color: white;");
                }
            });
            return row;
        });
    }

    private void configurarColunaAcoes() {
        colunaAcoes.setCellFactory(param -> new TableCell<Paciente, Void>() {
            private final Button btnVisualizar = criarBotaoAcao("👁", "#2196F3", "Visualizar");
            private final Button btnEditar = criarBotaoAcao("✏", "#FF9800", "Editar");
            private final Button btnExcluir = criarBotaoAcao("🗑", "#F44336", "Excluir");
            private final HBox container = new HBox(8, btnVisualizar, btnEditar, btnExcluir);

            {
                container.setAlignment(Pos.CENTER);

                btnVisualizar.setOnAction(event -> {
                    Paciente paciente = getTableView().getItems().get(getIndex());
                    visualizarPaciente(paciente);
                });

                btnEditar.setOnAction(event -> {
                    Paciente paciente = getTableView().getItems().get(getIndex());
                    editarPaciente(paciente);
                });

                btnExcluir.setOnAction(event -> {
                    Paciente paciente = getTableView().getItems().get(getIndex());
                    excluirPaciente(paciente);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : container);
            }
        });
    }

    private Button criarBotaoAcao(String texto, String cor, String tooltip) {
        Button btn = new Button(texto);
        String baseStyle = String.format(
                "-fx-background-color: %s; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 14; " +
                        "-fx-padding: 6 12; " +
                        "-fx-background-radius: 6; " +
                        "-fx-cursor: hand; " +
                        "-fx-min-width: 38; " +
                        "-fx-pref-width: 38;", cor
        );
        String hoverStyle = baseStyle + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 8, 0, 0, 2);";

        btn.setStyle(baseStyle);
        btn.setOnMouseEntered(e -> btn.setStyle(hoverStyle));
        btn.setOnMouseExited(e -> btn.setStyle(baseStyle));
        btn.setTooltip(new Tooltip(tooltip));
        return btn;
    }

    // ==================== CONFIGURAÇÃO DE FILTROS (ATUALIZADO) ====================
    private void configurarFiltros() {
        // ComboBox Status
        cbxFiltroStatus.getItems().addAll("Todos", "Ativo", "Inativo", "Pendente");
        cbxFiltroStatus.setValue("Todos");

        // ComboBox Responsável
        cbxFiltroResponsavel.getItems().add("Todos");
        cbxFiltroResponsavel.setValue("Todos");

        // --- Criar e guardar os listeners ---
        filtroTextoChangeListener = (obs, oldVal, newVal) -> {
            if (newVal != null) {
                aplicarFiltros();
            }
        };

        filtroStatusChangeListener = (obs, oldVal, newVal) -> {
            if (newVal != null) {
                aplicarFiltros();
            }
        };

        filtroResponsavelChangeListener = (obs, oldVal, newVal) -> {
            if (newVal != null) {
                aplicarFiltros();
            }
        };

        // --- Adicionar os listeners ---
        txtBuscar.textProperty().addListener(filtroTextoChangeListener);
        cbxFiltroStatus.valueProperty().addListener(filtroStatusChangeListener);
        cbxFiltroResponsavel.valueProperty().addListener(filtroResponsavelChangeListener);
    }

    // ==================== APLICAR FILTROS ====================
    private void aplicarFiltros() {
        String busca = txtBuscar.getText() != null ? txtBuscar.getText().toLowerCase().trim() : "";
        String status = cbxFiltroStatus.getValue() != null ? cbxFiltroStatus.getValue() : "Todos";
        String responsavel = cbxFiltroResponsavel.getValue() != null ? cbxFiltroResponsavel.getValue() : "Todos";

        listaFiltrada.clear();
        listaFiltrada.addAll(
                listaPacientes.stream()
                        .filter(p -> busca.isEmpty() ||
                                (p.getNome() != null && p.getNome().toLowerCase().contains(busca)) ||
                                (p.getDiagnostico() != null && p.getDiagnostico().toLowerCase().contains(busca)) ||
                                (p.getCpf() != null && p.getCpf().contains(busca)))
                        .filter(p -> status.equals("Todos") || status.equals("Ativo")) // TODO: Expandir lógica para outros status
                        .filter(p -> {
                            if (responsavel == null || responsavel.equals("Todos")) return true;
                            List<Responsavel> resp = p.getResponsaveisLista();
                            return resp != null && !resp.isEmpty() &&
                                    resp.get(0).getNome().equals(responsavel);
                        })
                        .collect(Collectors.toList())
        );

        pacientesTableView.setItems(listaFiltrada);
        atualizarContadores();
    }

    @FXML
    private void handleLimparFiltros(ActionEvent event) {
        // Remover listeners temporariamente para evitar trigger múltiplo
        txtBuscar.textProperty().removeListener(filtroTextoChangeListener);
        cbxFiltroStatus.valueProperty().removeListener(filtroStatusChangeListener);
        cbxFiltroResponsavel.valueProperty().removeListener(filtroResponsavelChangeListener);

        // Limpar campos
        txtBuscar.clear();
        cbxFiltroStatus.setValue("Todos");
        cbxFiltroResponsavel.setValue("Todos"); // aplicarFiltros será chamado aqui

        // Readicionar listeners
        txtBuscar.textProperty().addListener(filtroTextoChangeListener);
        cbxFiltroStatus.valueProperty().addListener(filtroStatusChangeListener);
        cbxFiltroResponsavel.valueProperty().addListener(filtroResponsavelChangeListener);

        // Garantir que a tabela atualize com os filtros limpos
        aplicarFiltros();
    }

    // ==================== CARREGAMENTO DE DADOS (ATUALIZADO) ====================
    private void carregarDados() {
        try {
            List<Paciente> pacientes = pacienteService.listarTodos();
            listaPacientes.clear();
            listaPacientes.addAll(pacientes);
            atualizarComboResponsaveis(); // Atualiza o combo antes de aplicar filtros
            aplicarFiltros(); // Aplica filtros e atualiza a tabela/contadores
        } catch (Exception e) {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "Erro de Carregamento",
                    "Não foi possível carregar os dados dos pacientes: " + e.getMessage());
            listaPacientes.clear();
            listaFiltrada.clear();
            pacientesTableView.setItems(listaFiltrada); // Mostra tabela vazia
            atualizarContadores(); // Zera contadores
        }
    }

    private void atualizarComboResponsaveis() {
        String valorAtual = cbxFiltroResponsavel.getValue();

        // Remover listener temporariamente
        if (filtroResponsavelChangeListener != null) {
            cbxFiltroResponsavel.valueProperty().removeListener(filtroResponsavelChangeListener);
        }

        cbxFiltroResponsavel.getItems().clear();
        cbxFiltroResponsavel.getItems().add("Todos");

        listaPacientes.stream()
                .flatMap(p -> p.getResponsaveisLista() != null ?
                        p.getResponsaveisLista().stream() :
                        java.util.stream.Stream.empty())
                .filter(r -> r != null && r.getNome() != null) // Garantir que não haja nulls
                .map(Responsavel::getNome)
                .distinct()
                .sorted()
                .forEach(nome -> cbxFiltroResponsavel.getItems().add(nome));

        // Restaurar valor ou definir "Todos"
        if (valorAtual != null && cbxFiltroResponsavel.getItems().contains(valorAtual)) {
            cbxFiltroResponsavel.setValue(valorAtual);
        } else {
            cbxFiltroResponsavel.setValue("Todos");
        }

        // Readicionar listener se ele existir
        if (filtroResponsavelChangeListener != null) {
            cbxFiltroResponsavel.valueProperty().addListener(filtroResponsavelChangeListener);
        }
    }


    private void atualizarContadores() {
        // Total de pacientes na lista original
        lblTotalPacientes.setText(String.valueOf(listaPacientes.size()));

        // Total de registros na lista filtrada (exibida na tabela)
        lblTotalRegistros.setText(listaFiltrada.size() + " registro(s) encontrado(s)");

        // Atendimentos hoje (placeholder - futura implementação)
        lblAtendimentosHoje.setText("0");

        // Pendentes (placeholder - futura implementação)
        lblPendentes.setText("0");

        // Sem responsável (contado na lista original)
        long semResponsavel = listaPacientes.stream()
                .filter(p -> p.getResponsaveisLista() == null || p.getResponsaveisLista().isEmpty())
                .count();
        lblSemResponsavel.setText(String.valueOf(semResponsavel));
    }

    // ==================== AÇÕES DE PACIENTE ====================
    @FXML
    public void handleNovoCadastroButtonAction(ActionEvent event) {
        abrirTelaCadastro(null);
    }

    private void visualizarPaciente(Paciente paciente) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Visualizar Paciente");
        alert.setHeaderText("Informações de " + paciente.getNome());

        StringBuilder info = new StringBuilder();
        info.append("📋 DADOS PESSOAIS\n");
        info.append("━━━━━━━━━━━━━━━━━━━━━━━━\n");
        info.append("Nome: ").append(paciente.getNome()).append("\n");
        info.append("Idade: ").append(Period.between(paciente.getDataNascimento(), LocalDate.now()).getYears()).append(" anos\n");
        info.append("CPF: ").append(paciente.getCpf() != null ? paciente.getCpf() : "Não informado").append("\n\n");

        info.append("🏥 INFORMAÇÕES CLÍNICAS\n");
        info.append("━━━━━━━━━━━━━━━━━━━━━━━━\n");
        info.append("Diagnóstico: ").append(paciente.getDiagnostico()).append("\n");
        info.append("Escolaridade: ").append(paciente.getEscolaridade()).append("\n");
        info.append("Condição Clínica: ").append(paciente.getCondicaoClinica()).append("\n");

        if (paciente.getResponsaveisLista() != null && !paciente.getResponsaveisLista().isEmpty()) {
            Responsavel resp = paciente.getResponsaveisLista().get(0);
            info.append("\n👤 RESPONSÁVEL\n");
            info.append("━━━━━━━━━━━━━━━━━━━━━━━━\n");
            info.append("Nome: ").append(resp.getNome()).append("\n");
            info.append("Telefone: ").append(resp.getTelefone()).append("\n");
            info.append("CPF: ").append(resp.getCpf());
        } else {
            info.append("\n⚠️ ATENÇÃO\n");
            info.append("━━━━━━━━━━━━━━━━━━━━━━━━\n");
            info.append("Nenhum responsável associado");
        }

        alert.setContentText(info.toString());
        // Aumentar tamanho da caixa de diálogo
        alert.getDialogPane().setPrefSize(480, 400);
        alert.showAndWait();
    }


    private void editarPaciente(Paciente paciente) {
        // TODO: Implementar edição completa
        abrirTelaCadastro(paciente); // Reutiliza a tela de cadastro para edição
    }

    private void excluirPaciente(Paciente paciente) {
        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacao.setTitle("Confirmar Exclusão");
        confirmacao.setHeaderText("⚠️ Atenção!");
        confirmacao.setContentText(
                "Deseja realmente excluir o paciente?\n\n" +
                        "Nome: " + paciente.getNome() + "\n" +
                        "Diagnóstico: " + paciente.getDiagnostico() + "\n\n" +
                        "Esta ação não pode ser desfeita."
        );

        ButtonType btnConfirmar = new ButtonType("Excluir", ButtonBar.ButtonData.OK_DONE);
        ButtonType btnCancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
        confirmacao.getButtonTypes().setAll(btnConfirmar, btnCancelar);

        Optional<ButtonType> resultado = confirmacao.showAndWait();
        if (resultado.isPresent() && resultado.get() == btnConfirmar) {
            try {
                // TODO: Chamar pacienteService.excluir(paciente.getId())
                // pacienteService.excluir(paciente.getId()); // Descomentar quando o método existir no service/DAO

                AlertUtil.showAlert(Alert.AlertType.INFORMATION,
                        "Sucesso",
                        "Paciente excluído com sucesso! (Simulação)"); // Remover "(Simulação)" depois
                carregarDados(); // Recarrega a lista
            } catch (Exception e) {
                AlertUtil.showAlert(Alert.AlertType.ERROR,
                        "Erro",
                        "Erro ao excluir paciente: " + e.getMessage());
            }
        }
    }

    private void abrirTelaCadastro(Paciente paciente) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/TelaCadastroPaciente.fxml"));
            Parent root = fxmlLoader.load();

            // Se for edição, passar o paciente para o controller da tela de cadastro
            if (paciente != null) {
                TelaCadastroPacienteController controller = fxmlLoader.getController();
                // TODO: Criar um método no TelaCadastroPacienteController para receber e preencher o formulário
                // controller.setPacienteParaEdicao(paciente);
            }

            Stage stage = new Stage();
            stage.setTitle(paciente == null ? "Cadastro de Pacientes" : "Editar Paciente: " + paciente.getNome());
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.showAndWait(); // Espera a tela de cadastro fechar

            // Após fechar, recarrega os dados caso algo tenha mudado (novo cadastro ou edição)
            // carregarDados(); // Removido, pois o DataChangeListener já faz isso

        } catch (IOException ex) {
            System.err.println("Erro ao abrir tela de cadastro: " + ex.getMessage());
            ex.printStackTrace(); // Imprime o stack trace para depuração
            AlertUtil.showAlert(Alert.AlertType.ERROR, "Erro Crítico",
                    "Não foi possível abrir a tela de cadastro de paciente.\nErro: " + ex.getMessage());
        } catch (Exception e) {
            System.err.println("Erro inesperado ao abrir tela de cadastro: " + e.getMessage());
            e.printStackTrace();
            AlertUtil.showAlert(Alert.AlertType.ERROR, "Erro Inesperado",
                    "Ocorreu um erro inesperado.\nErro: " + e.getMessage());
        }
    }


    // ==================== DATA CHANGE LISTENER ====================
    @Override
    public void atualizarDados(String entidade) {
        if (entidade.equals("Paciente")) {
            System.out.println("Recebido evento de atualização para Paciente. Recarregando dados...");
            carregarDados();
        }
    }
}