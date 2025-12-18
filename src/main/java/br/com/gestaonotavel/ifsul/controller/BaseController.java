package br.com.gestaonotavel.ifsul.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public abstract class BaseController {

    @FXML
    protected void handleMenu(ActionEvent event) {
        // Lógica para o botão de menu
    }

    @FXML
    protected void handlePacientes(ActionEvent event) {
        // Lógica para o botão de pacientes
    }

    @FXML
    protected void handleAgendamentos(ActionEvent event) {
        // Lógica para o botão de agendamentos
    }

    @FXML
    protected void handleFinanceiro(ActionEvent event) {
        // Lógica para o botão de financeiro
    }

    @FXML
    protected void handleRelatorios(ActionEvent event) {
        // Lógica para o botão de relatórios
    }
}
