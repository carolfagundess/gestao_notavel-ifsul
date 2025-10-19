/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package br.com.gestaonotavel.ifsul.controller;

import br.com.gestaonotavel.ifsul.model.Usuario;
import br.com.gestaonotavel.ifsul.service.UsuarioService;
import br.com.gestaonotavel.ifsul.util.AlertUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * FXML Controller class
 *
 * @author carol
 */
public class TelaLoginController implements Initializable {

    /**
     * Initializes the controller class.
     */

    @FXML
    private TextField cpfTextField;
    @FXML
    private TextField senhaPasswordField;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void handleEntrarButtonAction(ActionEvent event) {

        UsuarioService usuarioService = new UsuarioService();

        try {
            String cpf  = cpfTextField.getText();
            String senha = senhaPasswordField.getText();

            Usuario usuario = usuarioService.autenticarUsuario(cpf, senha);

            System.out.println("Login bem-sucedido! Bem-vindo, " + usuario.getNome());
            abrirTelaPrincipal();
        }catch (IllegalArgumentException erro){
            AlertUtil.showAlert(Alert.AlertType.ERROR, "Erro de autenticação", erro.getMessage());
        }
    }

    public void abrirTelaPrincipal(){

        try {
            // Carrega o FXML da nova tela
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/TelaPrincipal.fxml"));
            Parent root = loader.load();

            // Cria um novo palco (Stage), que é uma nova janela
            Stage stage = new Stage();
            stage.setTitle("Gestão Notável - Painel Principal");
            stage.setScene(new Scene(root));

            // Mostra a nova janela
            stage.show();

            // Pega a referência da janela de login atual e a fecha
            Stage loginStage = (Stage) cpfTextField.getScene().getWindow();
            loginStage.close();

        } catch (IOException e) {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "Erro", "Falha ao abrir a tela principal.");
        }
    }


}
