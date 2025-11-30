package br.com.gestaonotavel.ifsul.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ValidationUtil {

    private static final Set<String> CPFS_INVALIDOS_CONHECIDOS = new HashSet<>(Arrays.asList(
            "00000000000", "11111111111", "22222222222", "33333333333", "44444444444",
            "55555555555", "66666666666", "77777777777", "88888888888", "99999999999"
    ));

    public static boolean validarCPF(String cpf){
        if (cpf == null) return false;

        String cpfLimpo = cpf.replaceAll("[^0-9]", "");

        if (cpfLimpo.length() != 11) return false;

        if (CPFS_INVALIDOS_CONHECIDOS.contains(cpfLimpo)) return false;

        try {
            // --- Cálculo do 1º Dígito Verificador (DV1) ---
            int soma1 = 0;
            int peso1 = 10;
            for (int i = 0; i < 9; i++) {
                int digitoInt = Character.getNumericValue(cpfLimpo.charAt(i));
                soma1 += digitoInt * peso1;
                peso1--;
            }

            int resto1 = soma1 % 11;
            int dv1Calculado = (resto1 < 2) ? 0 : (11 - resto1);
            int dv1Informado = Character.getNumericValue(cpfLimpo.charAt(9));

            if (dv1Calculado != dv1Informado) return false;

            // --- Cálculo do 2º Dígito Verificador (DV2) ---
            int soma2 = 0;
            int peso2 = 11;
            for (int i = 0; i < 10; i++) {
                int digitoInt = Character.getNumericValue(cpfLimpo.charAt(i));
                soma2 += digitoInt * peso2;
                peso2--;
            }

            int resto2 = soma2 % 11;
            int dv2Calculado = (resto2 < 2) ? 0 : (11 - resto2);
            int dv2Informado = Character.getNumericValue(cpfLimpo.charAt(10));

            if (dv2Calculado != dv2Informado) return false;

            return true;

        } catch (Exception e) {
            System.err.println("Erro inesperado ao validar CPF: " + e.getMessage());
            return false;
        }
    }

    //Código para validação de CPF. Pode ocorrer erro de login.
}