package br.com.gestaonotavel.ifsul.util;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class EncryptionUtil {

    private static final String SECRET_KEY = "12345678901234567890123456789012";
    private static final String INIT_VECTOR = "1234567890123456";

    public static String encrypt(String value) {
        if (value == null) return null;
        try {
            IvParameterSpec iv = new IvParameterSpec(INIT_VECTOR.getBytes(StandardCharsets.UTF_8));
            SecretKeySpec skeySpec = new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            byte[] encrypted = cipher.doFinal(value.getBytes());
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception ex) {
            throw new RuntimeException("Erro ao criptografar dados", ex);
        }
    }

    public static String decrypt(String encrypted) {
        if (encrypted == null) return null;
        try {
            IvParameterSpec iv = new IvParameterSpec(INIT_VECTOR.getBytes(StandardCharsets.UTF_8));
            SecretKeySpec skeySpec = new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

            byte[] original = cipher.doFinal(Base64.getDecoder().decode(encrypted));
            return new String(original);
        } catch (Exception ex) {
            // Se a descriptografia falhar, retorna o valor original.
            // Isso pode acontecer se o valor não estiver criptografado.
            return encrypted;
        }
    }

    /**
     * Verifica se uma string parece estar criptografada (neste caso, codificada em Base64).
     * Uma abordagem simples é tentar decodificar. Se funcionar e o resultado for um
     * texto que não é igual ao original, podemos assumir que está criptografado.
     * Uma verificação mais robusta tentaria a descriptografia.
     *
     * @param value A string a ser verificada.
     * @return true se a string provavelmente está criptografada, false caso contrário.
     */
    public static boolean isEncrypted(String value) {
        if (value == null || value.isEmpty()) {
            return false;
        }
        try {
            // Tenta decodificar de Base64
            byte[] decoded = Base64.getDecoder().decode(value);
            // Tenta descriptografar
            decrypt(value);
            // Se a descriptografia não lançar exceção e o valor original não for igual ao "descriptografado"
            // (o decrypt retorna o original em caso de falha), então é provável que seja criptografado.
            // A verificação mais simples é que se a descriptografia não falhar, é porque o formato é válido.
            return !value.equals(decrypt(value));
        } catch (IllegalArgumentException e) {
            // Se a decodificação Base64 falhar, não está criptografado no nosso formato.
            return false;
        } catch (Exception e) {
            // Outras exceções de criptografia também indicam que não está no formato esperado.
            return false;
        }
    }
}
