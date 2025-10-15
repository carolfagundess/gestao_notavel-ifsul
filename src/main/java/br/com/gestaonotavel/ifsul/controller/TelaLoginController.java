/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package br.com.gestaonotavel.ifsul.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

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
        String cpf  = cpfTextField.getText();
        String senha = senhaPasswordField.getText();

        System.out.println("CPF: " + cpf);
    }
}
