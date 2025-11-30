package br.com.gestaonotavel.ifsul.controller;

import br.com.gestaonotavel.ifsul.model.Atividade;
import br.com.gestaonotavel.ifsul.model.TipoAtividade;
import br.com.gestaonotavel.ifsul.service.AtividadeService;
import br.com.gestaonotavel.ifsul.service.TipoAtividadeService;
import br.com.gestaonotavel.ifsul.util.AlertUtil;
import br.com.gestaonotavel.ifsul.util.DataChangeListener;
import br.com.gestaonotavel.ifsul.util.DataChangeManager;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class TelaListagemAtividadesController implements Initializable, DataChangeListener {

    @FXML private TextField txtBuscar;
    @FXML private ComboBox<TipoAtividade> cbxFiltroTipo;
    @FXML private DatePicker datePickerFiltro;
    @FXML private Button btnLimparFiltros;
    @FXML private Button btnGerenciarTipos;
    @FXML private Button btnNovaAtividade;
    @FXML private Label lblTotalRegistros;
    @FXML private TableView<Atividade> tabelaAtividades;
    @FXML private TableColumn<Atividade, String> colunaNome;
    @FXML private TableColumn<Atividade, String> colunaTipo;
    @FXML private TableColumn<Atividade, String> colunaDataInicio;
    @FXML private TableColumn<Atividade, String> colunaDataFim;
    @FXML private TableColumn<Atividade, String> colunaLocal;
    @FXML private TableColumn<Atividade, String> colunaArrecadado;
    @FXML private TableColumn<Atividade, Void> colunaAcoes;
    @FXML private Button btnFechar;

    private final AtividadeService atividadeService;
    private final TipoAtividadeService tipoAtividadeService;
    private ObservableList<Atividade> obsListaAtividades;
    private FilteredList<Atividade> filteredData;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public TelaListagemAtividadesController(AtividadeService atividadeService, TipoAtividadeService tipoAtividadeService) {
        this.atividadeService = atividadeService;
        this.tipoAtividadeService = tipoAtividadeService;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        obsListaAtividades = FXCollections.observableArrayList();
        filteredData = new FilteredList<>(obsListaAtividades, p -> true);

        configurarTabela();
        configurarFiltros();
        carregarDados();

        DataChangeManager.getInstance().addDataChangeListener(this);
    }

    private void configurarTabela() {
        tabelaAtividades.setItems(filteredData);
        tabelaAtividades.setPlaceholder(new Label("Nenhuma atividade cadastrada."));

        colunaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colunaLocal.setCellValueFactory(new PropertyValueFactory<>("local"));

        colunaTipo.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTipo().getNome()));
        colunaDataInicio.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDataInicio().format(formatter)));
        colunaDataFim.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDataFim().format(formatter)));
        colunaArrecadado.setCellValueFactory(cellData -> new SimpleStringProperty(String.format("R$ %.2f", cellData.getValue().getValorArrecadado())));
        colunaArrecadado.getStyleClass().add("table-cell-arrecadado");
    }

    private void configurarFiltros() {
        cbxFiltroTipo.getItems().add(null);
        cbxFiltroTipo.getItems().addAll(tipoAtividadeService.listarTodos());

        cbxFiltroTipo.setCellFactory(param -> new ListCell<TipoAtividade>() {
            @Override protected void updateItem(TipoAtividade item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "Todos os Tipos" : item.getNome());
            }
        });
        cbxFiltroTipo.setButtonCell(new ListCell<TipoAtividade>() {
            @Override protected void updateItem(TipoAtividade item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "Todos os Tipos" : item.getNome());
            }
        });

        txtBuscar.textProperty().addListener((obs, old, val) -> aplicarFiltros());
        cbxFiltroTipo.valueProperty().addListener((obs, old, val) -> aplicarFiltros());
        datePickerFiltro.valueProperty().addListener((obs, old, val) -> aplicarFiltros());
    }

    private void aplicarFiltros() {
        String busca = txtBuscar.getText();
        TipoAtividade tipo = cbxFiltroTipo.getValue();
        LocalDate data = datePickerFiltro.getValue();

        filteredData.setPredicate(atividade -> {
            boolean matchBusca = (busca == null || busca.isEmpty()) ||
                    (atividade.getNome().toLowerCase().contains(busca.toLowerCase())) ||
                    (atividade.getLocal().toLowerCase().contains(busca.toLowerCase()));

            boolean matchTipo = (tipo == null) || (atividade.getTipo().equals(tipo));

            boolean matchData = (data == null) ||
                    (atividade.getDataInicio().toLocalDate().equals(data) ||
                            atividade.getDataFim().toLocalDate().equals(data) ||
                            (data.isAfter(atividade.getDataInicio().toLocalDate()) && data.isBefore(atividade.getDataFim().toLocalDate())));

            return matchBusca && matchTipo && matchData;
        });

        lblTotalRegistros.setText(filteredData.size() + " registro(s) encontrado(s)");
    }

    private void carregarDados() {
        try {
            List<Atividade> atividades = atividadeService.listarTodos();
            obsListaAtividades.clear();
            obsListaAtividades.addAll(atividades);
            aplicarFiltros();
        } catch (Exception e) {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao carregar atividades: " + e.getMessage());
        }
    }

    @FXML
    void handleGerenciarTiposAction(ActionEvent event) {
        abrirModal("/view/TelaCadastroTipoAtividade.fxml", "Gerenciar Tipos de Atividade",
                (Callback<Class<?>, Object>) controller -> new TelaCadastroTipoAtividadeController(tipoAtividadeService));

        TipoAtividade selecionado = cbxFiltroTipo.getValue();
        cbxFiltroTipo.getItems().clear();
        configurarFiltros();
        cbxFiltroTipo.setValue(selecionado);
    }

    @FXML
    void handleNovaAtividadeAction(ActionEvent event) {
        abrirModal("/view/TelaCadastroAtividade.fxml", "Cadastrar Nova Atividade",
                (Callback<Class<?>, Object>) controller -> new TelaCadastroAtividadeController(atividadeService, tipoAtividadeService));
    }

    @FXML void handleLimparFiltros(ActionEvent event) {
        txtBuscar.clear();
        cbxFiltroTipo.setValue(null);
        datePickerFiltro.setValue(null);
        aplicarFiltros();
    }

    @FXML void handleFecharButtonAction(ActionEvent event) { ((Stage) btnFechar.getScene().getWindow()).close(); }

    @Override
    public void atualizarDados(String entidade) {
        if ("Atividade".equals(entidade)) carregarDados();
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
            if (tabelaAtividades.getScene() != null) stage.initOwner(tabelaAtividades.getScene().getWindow());
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            AlertUtil.showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao abrir a tela: " + e.getMessage());
        }
    }
}