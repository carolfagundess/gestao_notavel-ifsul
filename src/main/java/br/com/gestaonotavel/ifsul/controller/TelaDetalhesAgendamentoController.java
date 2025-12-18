package br.com.gestaonotavel.ifsul.controller;

import br.com.gestaonotavel.ifsul.model.Atendimento;
import br.com.gestaonotavel.ifsul.model.StatusAtendimento;
import br.com.gestaonotavel.ifsul.service.AtendimentoService;
import br.com.gestaonotavel.ifsul.service.factory.ServiceFactory;
import br.com.gestaonotavel.ifsul.util.AlertUtil;
import br.com.gestaonotavel.ifsul.util.DataChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class TelaDetalhesAgendamentoController {

    @FXML private Label lblPaciente;
    @FXML private Label lblEspecialista;
    @FXML private Label lblData;
    @FXML private Label lblHorario;
    @FXML private Label lblLocal;
    @FXML private Label lblStatus;
    @FXML private Label lblObservacao;
    @FXML private Button btnConcluir;
    @FXML private Button btnCancelarAtendimento;
    @FXML private Button btnEditar;
    @FXML private Button btnFechar;

    private Atendimento atendimento;
    private AtendimentoService atendimentoService;
    private DataChangeListener dataChangeListener;

    public void setAtendimento(Atendimento atendimento, DataChangeListener listener) {
        this.atendimento = atendimento;
        this.dataChangeListener = listener;
        this.atendimentoService = ServiceFactory.getInstance().getAtendimentoService();
        preencherDetalhes();
    }

    private void preencherDetalhes() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        lblPaciente.setText(atendimento.getPaciente().getNome());
        lblEspecialista.setText(atendimento.getEspecialista().getNome());
        lblData.setText(atendimento.getDataHora().format(dateFormatter));
        lblHorario.setText(atendimento.getDataHora().format(timeFormatter));
        lblLocal.setText(atendimento.getLocal());
        lblStatus.setText(atendimento.getStatusAtendimento().toString());
        lblObservacao.setText(atendimento.getObservacao().isEmpty() ? "Nenhuma observação registrada" : atendimento.getObservacao());

        configurarEstiloStatus();
        configurarBotoes();
    }

    private void configurarEstiloStatus() {
        String style = "-fx-font-weight: bold; -fx-padding: 5 10; -fx-background-radius: 12; -fx-text-fill: white;";
        switch (atendimento.getStatusAtendimento()) {
            case AGENDADO:
                lblStatus.setStyle(style + "-fx-background-color: #2196F3;"); // Azul
                break;
            case REALIZADO:
                lblStatus.setStyle(style + "-fx-background-color: #4CAF50;"); // Verde
                break;
            case CANCELADO:
                lblStatus.setStyle(style + "-fx-background-color: #f44336;"); // Vermelho
                break;
            case FALTOU:
                lblStatus.setStyle(style + "-fx-background-color: #FF9800;"); // Laranja
                break;
        }
    }

    private void configurarBotoes() {
        boolean isFinalizado = atendimento.getStatusAtendimento() == StatusAtendimento.REALIZADO || atendimento.getStatusAtendimento() == StatusAtendimento.CANCELADO;
        btnConcluir.setDisable(isFinalizado);
        btnCancelarAtendimento.setDisable(isFinalizado);
        btnEditar.setDisable(isFinalizado);
    }

    @FXML
    private void handleConcluir() {
        atualizarStatus(StatusAtendimento.REALIZADO, "concluído");
    }

    @FXML
    private void handleCancelarAtendimento() {
        atualizarStatus(StatusAtendimento.CANCELADO, "cancelado");
    }

    private void atualizarStatus(StatusAtendimento novoStatus, String acao) {
        try {
            atendimentoService.atualizarStatus(atendimento.getIdAtendimento(), novoStatus);
            AlertUtil.showAlert(Alert.AlertType.INFORMATION, "Sucesso", "Atendimento marcado como " + acao + ".");
            if (dataChangeListener != null) {
                dataChangeListener.atualizarDados("Atendimento");
            }
            closeStage();
        } catch (Exception e) {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "Erro", "Não foi possível atualizar o status do atendimento.");
        }
    }

    @FXML
    private void handleEditar() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/TelaAgendamento.fxml"));
            
            ServiceFactory factory = ServiceFactory.getInstance();
            loader.setControllerFactory(c -> new TelaAgendamentoController(
                factory.getPacienteService(),
                factory.getEspecialistaService(),
                factory.getAtendimentoService()
            ));

            Parent root = loader.load();
            
            TelaAgendamentoController controller = loader.getController();
            controller.setAtendimentoParaEdicao(this.atendimento);

            Stage stage = new Stage();
            stage.setTitle("Editar Agendamento");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(btnEditar.getScene().getWindow());
            stage.showAndWait();

            // Notifica para atualizar a lista e fecha a tela de detalhes
            if (dataChangeListener != null) {
                dataChangeListener.atualizarDados("Atendimento");
            }
            closeStage();

        } catch (IOException e) {
            e.printStackTrace();
            AlertUtil.showAlert(Alert.AlertType.ERROR, "Erro", "Falha ao abrir a tela de edição.");
        }
    }

    @FXML
    private void handleFechar() {
        closeStage();
    }

    private void closeStage() {
        ((Stage) btnFechar.getScene().getWindow()).close();
    }
}
