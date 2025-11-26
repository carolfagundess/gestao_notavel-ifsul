package br.com.gestaonotavel.ifsul.controller;

import br.com.gestaonotavel.ifsul.model.MovimentacaoFinanceira;
import br.com.gestaonotavel.ifsul.service.MovimentacaoFinanceiraService;
import br.com.gestaonotavel.ifsul.util.DataChangeListener;
import br.com.gestaonotavel.ifsul.util.DataChangeManager;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
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

    private final MovimentacaoFinanceiraService financeiroService;
    private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public TelaListagemMovimentacoesController(MovimentacaoFinanceiraService fs) { this.financeiroService = fs; }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        configurarTabela();
        carregarDados();
        DataChangeManager.getInstance().addDataChangeListener(this);
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
        lblSaldoAtual.setStyle(saldo >= 0 ? "-fx-text-fill: green;" : "-fx-text-fill: red;");
    }

    @Override
    public void atualizarDados(String entidade) {
        if ("MovimentacaoFinanceira".equals(entidade)) carregarDados();
    }
}