
package br.com.gestaonotavel.ifsul.controller;

import br.com.gestaonotavel.ifsul.model.Paciente;
import br.com.gestaonotavel.ifsul.model.Responsavel;
import br.com.gestaonotavel.ifsul.service.PacienteService;
import br.com.gestaonotavel.ifsul.util.AlertUtil;
import br.com.gestaonotavel.ifsul.util.DataChangeListener;
import br.com.gestaonotavel.ifsul.util.DataChangeManager;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author carol
 */
public class TelaPrincipalController implements Initializable, DataChangeListener {

    /**
     * Initializes the controller class.
     */

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

    private Paciente pacienteSelecionado;

    @Override
    public void atualizarDados(String entidade) {
        if(entidade.equals("Paciente")){
            populaTabelaPaciente();
        }
    }

    @FXML
    public void handleNovoCadastroButtonAction(ActionEvent event) throws IOException {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/TelaCadastroPaciente.fxml"));
            Parent root = fxmlLoader.load();

            Stage stage = new Stage();
            stage.setTitle("Cadastro de Pacientes");
            stage.setScene(new Scene(root));
            stage.showAndWait();
        }catch (IOException ex){
            AlertUtil.showAlert(Alert.AlertType.ERROR, "Erro", "Erro ao abrir a tela de cadastro");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        populaTabelaPaciente();
        DataChangeManager.getInstance().addDataChangeListener(this);

        pacientesTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                System.out.println("selecionado Paciente");
                this.pacienteSelecionado = (Paciente) newValue;
            }
        });

        TableColumn<Paciente, Void> colunaAcoes = this.colunaAcoes;

        colunaAcoes.setCellFactory(param -> new TableCell<Paciente, Void>() {
            private final Button btnEditar = new Button("Editar");
            private final Button btnExcluir = new Button("Excluir");
            private final HBox pane = new HBox(btnEditar, btnExcluir);

            {
                pane.setSpacing(10); // Espaçamento entre os botões

                btnEditar.setOnAction(event -> {
                    Paciente paciente = getTableView().getItems().get(getIndex());
                    System.out.println("Editar clicado para: " + paciente.getNome());
                    iniciarEdicao(paciente);
                });

                btnExcluir.setOnAction(event -> {
                    Paciente paciente = getTableView().getItems().get(getIndex());
                    System.out.println("Excluir clicado para: " + paciente.getNome());
                    solicitarExclusao(paciente);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(pane);
                }
            }
        });

    }

    private void solicitarExclusao(Paciente paciente) {
    }

    private void iniciarEdicao(Paciente paciente) {
    }

    public void populaTabelaPaciente() {
        PacienteService pacienteService = new PacienteService();

        colunaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colunaIdade.setCellValueFactory(cellData -> {
            LocalDate dataNascimento = cellData.getValue().getDataNascimento();
            int idade = Period.between(dataNascimento, LocalDate.now()).getYears();
            return new SimpleIntegerProperty(idade).asObject();
        });
        colunaDiagnostico.setCellValueFactory(new PropertyValueFactory<>("diagnostico"));
        colunaResponsavel.setCellValueFactory(cellData -> {
            List<Responsavel> responsavelLista = cellData.getValue().getResponsaveisLista();
            if (responsavelLista != null && !responsavelLista.isEmpty()) {
                return new SimpleStringProperty(responsavelLista.get(0).getNome());
            }
            return new SimpleStringProperty("Nenhum");
        });
        List<Paciente> pacientes = pacienteService.listarTodos();

        pacientesTableView.setItems(FXCollections.observableArrayList(pacientes));
    }
    
}
