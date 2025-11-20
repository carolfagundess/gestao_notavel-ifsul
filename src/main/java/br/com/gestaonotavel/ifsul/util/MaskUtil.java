package br.com.gestaonotavel.ifsul.util;

import javafx.application.Platform;
import javafx.scene.control.TextField;

public class MaskUtil {

    /**
     * Adiciona máscara de CPF (000.000.000-00) ao TextField
     */
    public static void cpfField(final TextField textField) {
        maxField(textField, 14);
        textField.lengthProperty().addListener((observableValue, number, t1) -> {
            String value = textField.getText();
            if (t1.intValue() <= 14) {
                value = value.replaceAll("[^0-9]", "");
                value = value.replaceFirst("(\\d{3})(\\d)", "$1.$2");
                value = value.replaceFirst("(\\d{3})(\\d)", "$1.$2");
                value = value.replaceFirst("(\\d{3})(\\d)", "$1-$2");
            }
            final String text = value;
            Platform.runLater(() -> {
                textField.setText(text);
                textField.positionCaret(text.length());
            });
        });
    }

    /**
     * Adiciona máscara de Telefone ((00) 00000-0000)
     */
    public static void foneField(final TextField textField) {
        maxField(textField, 15);
        textField.lengthProperty().addListener((observableValue, number, t1) -> {
            String value = textField.getText();
            if (t1.intValue() <= 15) {
                value = value.replaceAll("[^0-9]", "");
                value = value.replaceFirst("(\\d{2})(\\d)", "($1) $2");
                value = value.replaceFirst("(\\d{5})(\\d)", "$1-$2");
            }
            final String text = value;
            Platform.runLater(() -> {
                textField.setText(text);
                textField.positionCaret(text.length());
            });
        });
    }

    private static void maxField(final TextField textField, final Integer length) {
        textField.textProperty().addListener((observableValue, oldValue, newValue) -> {
            if (newValue == null || newValue.length() > length) {
                textField.setText(oldValue);
            }
        });
    }
}
