package br.com.gestaonotavel.ifsul.controller;

import br.com.gestaonotavel.ifsul.model.MovimentacaoFinanceira;
import br.com.gestaonotavel.ifsul.service.MovimentacaoFinanceiraService;
import br.com.gestaonotavel.ifsul.service.factory.ServiceFactory;
import br.com.gestaonotavel.ifsul.util.AlertUtil;
import br.com.gestaonotavel.ifsul.util.DataChangeListener;
import br.com.gestaonotavel.ifsul.util.DataChangeManager;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class TelaListagemMovimentacoesController implements Initializable, DataChangeListener {
    @FXML private TableView<MovimentacaoFinanceira> tabelaFinanceira;
    @FXML private TableColumn<MovimentacaoFinanceira, String> colunaData;
    @FXML private TableColumn<MovimentacaoFinanceira, String> colunaTipo;
    @FXML private TableColumn<MovimentacaoFinanceira, String> colunaValor;
    @FXML private TableColumn<MovimentacaoFinanceira, String> colunaDescricao;
    @FXML private Label lblSaldoAtual;
    @FXML private Button btnNovaMovimentacao; // Botão Novo

    private final MovimentacaoFinanceiraService financeiroService;
    private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public TelaListagemMovimentacoesController(MovimentacaoFinanceiraService fs) { this.financeiroService = fs; }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        configurarTabela();
        carregarDados();
        DataChangeManager.getInstance().addDataChangeListener(this);
    }

    // --- AÇÃO NOVA ---
    @FXML
    void handleNovaMovimentacao(ActionEvent event) {
        abrirModal("/view/TelaCadastroMovimentacao.fxml", "Nova Movimentação",
                (Callback<Class<?>, Object>) controller -> new TelaCadastroMovimentacaoController(
                        financeiroService,
                        ServiceFactory.getInstance().getEspecialistaService(),
                        ServiceFactory.getInstance().getAtividadeService()
                ));
    }

    private void configurarTabela() {
        colunaData.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getDataMovimentacao().format(fmt)));
        colunaTipo.setCellValueFactory(d -> new SimpleStringProperty(d.getValue().getTipoMovimento().toString()));
        colunaValor.setCellValueFactory(d -> new SimpleStringProperty(String.format("R$ %.2f", d.getValue().getValorMovimentacao())));
        colunaDescricao.setCellValueFactory(new PropertyValueFactory<>("observacao"));
    }

    private void carregarDados() {
        tabelaFinanceira.setItems(FXCollections.observableArrayList(financeiroService.listarTodas()));
        double saldo = financeiroService.calcularSaldoAtual();
        lblSaldoAtual.setText(String.format("R$ %.2f", saldo));
        lblSaldoAtual.setStyle(saldo >= 0 ? "-fx-text-fill: white;" : "-fx-text-fill: #FFCDD2;"); // Ajustei cor para fundo escuro
    }

    @Override
    public void atualizarDados(String entidade) {
        if ("MovimentacaoFinanceira".equals(entidade)) carregarDados();
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
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            AlertUtil.showAlert(javafx.scene.control.Alert.AlertType.ERROR, "Erro", "Erro ao abrir tela: " + e.getMessage());
        }
    }
}