package br.com.gestaonotavel.ifsul.controller;

import br.com.gestaonotavel.ifsul.model.Atendimento;
import br.com.gestaonotavel.ifsul.model.Atividade;
import br.com.gestaonotavel.ifsul.model.StatusAtendimento;
import br.com.gestaonotavel.ifsul.service.AtendimentoService;
import br.com.gestaonotavel.ifsul.service.AtividadeService;
import br.com.gestaonotavel.ifsul.service.EspecialistaService;
import br.com.gestaonotavel.ifsul.service.PacienteService;
import br.com.gestaonotavel.ifsul.service.factory.ServiceFactory;
import br.com.gestaonotavel.ifsul.util.DataChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class TelaCalendarioMensalController implements Initializable, DataChangeListener {

    @FXML private Label lblMesAno;
    @FXML private GridPane gridCalendario;
    @FXML private Button btnAnterior;
    @FXML private Button btnProximo;
    @FXML private Button btnFechar;
    @FXML private Button btnNovoAgendamento;

    private YearMonth mesAtual;
    private final AtendimentoService atendimentoService;
    private final AtividadeService atividadeService;
    private final PacienteService pacienteService;
    private final EspecialistaService especialistaService;

    public TelaCalendarioMensalController(AtendimentoService atendimentoService, AtividadeService atividadeService, PacienteService pacienteService, EspecialistaService especialistaService) {
        this.atendimentoService = atendimentoService;
        this.atividadeService = atividadeService;
        this.pacienteService = pacienteService;
        this.especialistaService = especialistaService;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        mesAtual = YearMonth.now();
        atualizarCalendario();
    }

    @FXML
    void handleAnterior(ActionEvent event) {
        mesAtual = mesAtual.minusMonths(1);
        atualizarCalendario();
    }

    @FXML
    void handleProximo(ActionEvent event) {
        mesAtual = mesAtual.plusMonths(1);
        atualizarCalendario();
    }

    @FXML
    void handleFechar(ActionEvent event) {
        ((Stage) btnFechar.getScene().getWindow()).close();
    }

    @FXML
    private void handleNovoAgendamento(ActionEvent event) {
        abrirTelaDeAgendamento(null);
    }

    private void abrirTelaDeAgendamento(Atendimento atendimento) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/TelaAgendamento.fxml"));

            loader.setControllerFactory(c -> new TelaAgendamentoController(
                ServiceFactory.getInstance().getPacienteService(),
                ServiceFactory.getInstance().getEspecialistaService(),
                ServiceFactory.getInstance().getAtendimentoService()
            ));

            Parent root = loader.load();
            
            if (atendimento != null) {
                TelaAgendamentoController controller = loader.getController();
                controller.setAtendimentoParaEdicao(atendimento);
            }

            Stage stage = new Stage();
            stage.setTitle(atendimento == null ? "Novo Agendamento" : "Editar Agendamento");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

            atualizarCalendario();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void abrirTelaDeDetalhes(Atendimento atendimento) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/TelaDetalhesAgendamento.fxml"));
            loader.setControllerFactory(c -> new TelaDetalhesAgendamentoController());
            Parent root = loader.load();

            TelaDetalhesAgendamentoController controller = loader.getController();
            controller.setAtendimento(atendimento, this);

            Stage stage = new Stage();
            stage.setTitle("Detalhes do Agendamento");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(gridCalendario.getScene().getWindow());
            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void atualizarCalendario() {
        lblMesAno.setText(mesAtual.getMonth().name() + " / " + mesAtual.getYear());
        gridCalendario.getChildren().clear();

        LocalDate inicioMes = mesAtual.atDay(1);
        LocalDate fimMes = mesAtual.atEndOfMonth();

        List<Atendimento> atendimentos = atendimentoService.listarTodos(null).stream()
                .filter(a -> a.getDataHora() != null && !a.getDataHora().toLocalDate().isBefore(inicioMes) && !a.getDataHora().toLocalDate().isAfter(fimMes))
                .filter(a -> a.getStatusAtendimento() != StatusAtendimento.CANCELADO)
                .collect(Collectors.toList());

        List<Atividade> atividades = atividadeService.listarTodos().stream()
                .filter(a -> a.getDataInicio() != null && !a.getDataInicio().toLocalDate().isBefore(inicioMes) && !a.getDataInicio().toLocalDate().isAfter(fimMes))
                .collect(Collectors.toList());

        int diaSemanaInicio = mesAtual.atDay(1).getDayOfWeek().getValue() % 7;
        int diasNoMes = mesAtual.lengthOfMonth();

        int coluna = diaSemanaInicio;
        int linha = 0;

        for (int dia = 1; dia <= diasNoMes; dia++) {
            LocalDate dataAtual = mesAtual.atDay(dia);
            VBox celulaDia = criarCelulaDia(dia, dataAtual, atendimentos, atividades);
            gridCalendario.add(celulaDia, coluna, linha);

            coluna++;
            if (coluna > 6) {
                coluna = 0;
                linha++;
            }
        }
    }

    private VBox criarCelulaDia(int dia, LocalDate data, List<Atendimento> atendimentos, List<Atividade> atividades) {
        VBox box = new VBox(2);
        box.setStyle("-fx-border-color: #E0E0E0; -fx-border-width: 1; -fx-padding: 5; -fx-background-color: white;");
        box.setPrefHeight(100);
        box.setPrefWidth(100);

        Label lblDia = new Label(String.valueOf(dia));
        lblDia.setStyle("-fx-font-weight: bold; -fx-text-fill: #333;");
        box.getChildren().add(lblDia);

        for (Atendimento a : atendimentos) {
            if (a.getDataHora().toLocalDate().equals(data)) {
                Label lbl = new Label("• " + a.getPaciente().getNome().split(" ")[0]);
                lbl.setStyle("-fx-font-size: 10; -fx-text-fill: #1976D2; -fx-cursor: hand;");
                lbl.setOnMouseClicked(event -> abrirTelaDeDetalhes(a));
                box.getChildren().add(lbl);
            }
        }

        for (Atividade at : atividades) {
            if (at.getDataInicio().toLocalDate().equals(data)) {
                Label lbl = new Label("★ " + at.getNome());
                lbl.setStyle("-fx-font-size: 10; -fx-text-fill: #9C27B0; -fx-font-weight: bold;");
                box.getChildren().add(lbl);
            }
        }

        return box;
    }

    @Override
    public void atualizarDados(String entidade) {
        if ("Atendimento".equals(entidade)) {
            atualizarCalendario();
        }
    }
}
