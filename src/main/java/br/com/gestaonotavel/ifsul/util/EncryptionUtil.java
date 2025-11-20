package br.com.gestaonotavel.ifsul.util;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class EncryptionUtil {

    // Em produção, estas chaves devem vir de variáveis de ambiente ou arquivo de configuração seguro
    // Para este projeto local, definimos constantes fixas (32 chars para AES-256, 16 chars para IV)
    private static final String SECRET_KEY = "12345678901234567890123456789012"; // 32 chars
    private static final String INIT_VECTOR = "1234567890123456"; // 16 chars

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
            // Se falhar (ex: dado não estava criptografado), retorna o original para evitar crash
            return encrypted;
        }
    }
}