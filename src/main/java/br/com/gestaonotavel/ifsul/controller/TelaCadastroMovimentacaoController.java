package br.com.gestaonotavel.ifsul.controller;

import br.com.gestaonotavel.ifsul.model.*;
import br.com.gestaonotavel.ifsul.service.*;
import br.com.gestaonotavel.ifsul.util.AlertUtil;
import br.com.gestaonotavel.ifsul.util.RegraDeNegocioException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class TelaCadastroMovimentacaoController implements Initializable {
    @FXML private ComboBox<TipoMovimento> cbxTipoMovimento;
    @FXML private TextField txtValor;
    @FXML private DatePicker datePickerData;
    @FXML private ComboBox<FormaPagamento> cbxFormaPagamento;
    @FXML private TextField txtObservacao;
    @FXML private ComboBox<Especialista> cbxEspecialista;
    @FXML private ComboBox<Atividade> cbxAtividade;
    @FXML private Button btnSalvar;
    @FXML private Button btnCancelar;

    private final MovimentacaoFinanceiraService financeiroService;
    private final EspecialistaService especialistaService;
    private final AtividadeService atividadeService;

    public TelaCadastroMovimentacaoController(MovimentacaoFinanceiraService fs, EspecialistaService es, AtividadeService as) {
        this.financeiroService = fs; this.especialistaService = es; this.atividadeService = as;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cbxTipoMovimento.getItems().setAll(TipoMovimento.values());
        cbxFormaPagamento.getItems().setAll(FormaPagamento.values());
        cbxEspecialista.getItems().setAll(especialistaService.listarTodos());
        cbxEspecialista.setConverter(new StringConverter<Especialista>() {
            @Override public String toString(Especialista e) { return e==null?null:e.getNome(); }
            @Override public Especialista fromString(String s) { return null; }
        });
        cbxAtividade.getItems().setAll(atividadeService.listarTodos());
        cbxAtividade.setConverter(new StringConverter<Atividade>() {
            @Override public String toString(Atividade a) { return a==null?null:a.getNome(); }
            @Override public Atividade fromString(String s) { return null; }
        });
    }

    @FXML void handleSalvarButtonAction(ActionEvent event) {
        try {
            MovimentacaoFinanceira m = new MovimentacaoFinanceira();
            m.setTipoMovimento(cbxTipoMovimento.getValue());
            m.setValorMovimentacao(Double.parseDouble(txtValor.getText().replace(",",".")));
            if(datePickerData.getValue()!=null) m.setDataMovimentacao(datePickerData.getValue().atStartOfDay());
            m.setFormaPagamento(cbxFormaPagamento.getValue());
            m.setObservacao(txtObservacao.getText());
            m.setEspecialista(cbxEspecialista.getValue());
            m.setAtividade(cbxAtividade.getValue());

            financeiroService.salvar(m);
            AlertUtil.showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Salvo!");
            ((Stage)btnSalvar.getScene().getWindow()).close();
        } catch (Exception e) {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "Erro", e.getMessage());
        }
    }

    @FXML void handleCancelarButtonAction(ActionEvent event) { ((Stage)btnCancelar.getScene().getWindow()).close(); }
}