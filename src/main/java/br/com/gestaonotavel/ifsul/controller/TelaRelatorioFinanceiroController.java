package br.com.gestaonotavel.ifsul.controller;

import br.com.gestaonotavel.ifsul.model.Especialista;
import br.com.gestaonotavel.ifsul.model.MovimentacaoFinanceira;
import br.com.gestaonotavel.ifsul.model.TipoMovimento;
import br.com.gestaonotavel.ifsul.service.MovimentacaoFinanceiraService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class TelaRelatorioFinanceiroController implements Initializable {

    @FXML private TableView<Map.Entry<String, Double>> tabelaResumo;
    @FXML private TableColumn<Map.Entry<String, Double>, String> colunaCategoria;
    @FXML private TableColumn<Map.Entry<String, Double>, String> colunaValor;

    private final MovimentacaoFinanceiraService financeiroService;

    public TelaRelatorioFinanceiroController(MovimentacaoFinanceiraService financeiroService) {
        this.financeiroService = financeiroService;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colunaCategoria.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getKey()));
        colunaValor.setCellValueFactory(p -> new SimpleStringProperty(String.format("R$ %.2f", p.getValue().getValue())));
        carregarDados();
    }

    private void carregarDados() {
        Map<String, Double> resumo = new HashMap<>();

        for (MovimentacaoFinanceira m : financeiroService.listarTodas()) {
            String chave = "Outros";
            if (m.getEspecialista() != null) {
                chave = "Profissional: " + m.getEspecialista().getNome();
            } else if (m.getAtividade() != null) {
                chave = "Atividade: " + m.getAtividade().getNome();
            }

            double valor = m.getValorMovimentacao();
            if (m.getTipoMovimento() == TipoMovimento.SAIDA) valor *= -1;

            resumo.put(chave, resumo.getOrDefault(chave, 0.0) + valor);
        }

        tabelaResumo.setItems(FXCollections.observableArrayList(resumo.entrySet()));
    }
}