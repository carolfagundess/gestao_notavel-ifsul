package br.com.gestaonotavel.ifsul.controller;

import br.com.gestaonotavel.ifsul.model.TipoAtividade;
import br.com.gestaonotavel.ifsul.service.TipoAtividadeService;
import br.com.gestaonotavel.ifsul.util.AlertUtil;
import br.com.gestaonotavel.ifsul.util.RegraDeNegocioException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class TelaCadastroTipoAtividadeController implements Initializable {

    @FXML private TextField txtNomeTipo;
    @FXML private Button btnSalvarTipo;
    @FXML private TableView<TipoAtividade> tabelaTiposAtividade;
    @FXML private TableColumn<TipoAtividade, String> colunaNome;
    @FXML private TableColumn<TipoAtividade, Void> colunaAcoes;
    @FXML private Button btnFechar;

    private final TipoAtividadeService tipoAtividadeService;
    private ObservableList<TipoAtividade> obsListaTipos;

    public TelaCadastroTipoAtividadeController(TipoAtividadeService tipoAtividadeService) {
        this.tipoAtividadeService = tipoAtividadeService;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        obsListaTipos = FXCollections.observableArrayList();
        configurarTabela();
        carregarDados();
    }

    private void configurarTabela() {
        colunaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));

        colunaAcoes.setCellFactory(param -> new TableCell<TipoAtividade, Void>() {
            private final Button btnExcluir = new Button("Excluir");
            private final HBox container = new HBox(btnExcluir);
            {
                btnExcluir.setStyle("-fx-background-color: #F44336; -fx-text-fill: white; -fx-font-size: 10; -fx-padding: 5;");
                btnExcluir.setOnAction(event -> {
                    TipoAtividade tipo = getTableView().getItems().get(getIndex());
                    handleExcluirTipo(tipo);
                });
                container.setAlignment(javafx.geometry.Pos.CENTER);
            }
            @Override protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : container);
            }
        });
    }

    private void carregarDados() {
        try {
            List<TipoAtividade> tipos = tipoAtividadeService.listarTodos();
            obsListaTipos.clear();
            obsListaTipos.addAll(tipos);
            tabelaTiposAtividade.setItems(obsListaTipos);
        } catch (Exception e) {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao carregar tipos de atividade: " + e.getMessage());
        }
    }

    @FXML
    void handleSalvarTipoButtonAction(ActionEvent event) {
        try {
            String nome = txtNomeTipo.getText();
            tipoAtividadeService.salvar(nome);
            AlertUtil.showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Novo tipo de atividade salvo!");
            txtNomeTipo.clear();
            carregarDados();
        } catch (RegraDeNegocioException e) {
            AlertUtil.showAlert(Alert.AlertType.WARNING, "Erro de Validação", e.getMessage());
        } catch (Exception e) {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "Erro", "Ocorreu um erro inesperado: " + e.getMessage());
        }
    }

    private void handleExcluirTipo(TipoAtividade tipo) {
        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacao.setTitle("Confirmar Exclusão");
        confirmacao.setHeaderText("Tem certeza que deseja excluir o tipo: " + tipo.getNome() + "?");
        confirmacao.setContentText("Esta ação não pode ser desfeita. Se este tipo já estiver em uso, a exclusão falhará.");

        Optional<ButtonType> resultado = confirmacao.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            try {
                tipoAtividadeService.excluir(tipo);
                AlertUtil.showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Tipo de atividade excluído.");
                carregarDados();
            } catch (RegraDeNegocioException e) {
                AlertUtil.showAlert(Alert.AlertType.ERROR, "Erro de Integridade", e.getMessage());
            } catch (Exception e) {
                AlertUtil.showAlert(Alert.AlertType.ERROR, "Erro", "Ocorreu um erro inesperado: " + e.getMessage());
            }
        }
    }

    @FXML
    void handleFecharButtonAction(ActionEvent event) {
        Stage stage = (Stage) btnFechar.getScene().getWindow();
        stage.close();
    }
}