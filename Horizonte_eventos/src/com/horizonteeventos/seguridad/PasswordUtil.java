package com.horizonteeventos.seguridad;

import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/** Crea/verifica hashes PBKDF2. No guardar contrasenas en texto plano. */
public final class PasswordUtil {
    private static final int ITERACIONES = 120000;
    private static final int TAMANO_SALT = 16;
    private static final int TAMANO_HASH = 256;
    private static final SecureRandom RANDOM = new SecureRandom();

    private PasswordUtil() { }

    public static String crearHash(char[] contrasena) throws GeneralSecurityException {
        byte[] salt = new byte[TAMANO_SALT];
        RANDOM.nextBytes(salt);
        byte[] hash = derivar(contrasena, salt, ITERACIONES);
        return "pbkdf2$" + ITERACIONES + "$" + Base64.getEncoder().encodeToString(salt)
                + "$" + Base64.getEncoder().encodeToString(hash);
    }

    public static boolean verificar(char[] contrasena, String valorGuardado)
            throws GeneralSecurityException {
        if (valorGuardado == null || !valorGuardado.startsWith("pbkdf2$")) {
            return false;
        }
        String[] partes = valorGuardado.split("\\$");
        if (partes.length != 4) return false;
        int iteraciones = Integer.parseInt(partes[1]);
        byte[] salt = Base64.getDecoder().decode(partes[2]);
        byte[] esperado = Base64.getDecoder().decode(partes[3]);
        byte[] obtenido = derivar(contrasena, salt, iteraciones);
        if (obtenido.length != esperado.length) return false;
        int diferencia = 0;
        for (int i = 0; i < obtenido.length; i++) diferencia |= obtenido[i] ^ esperado[i];
        return diferencia == 0;
    }

    private static byte[] derivar(char[] contrasena, byte[] salt, int iteraciones)
            throws GeneralSecurityException {
        PBEKeySpec spec = new PBEKeySpec(contrasena, salt, iteraciones, TAMANO_HASH);
        try {
            return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
                    .generateSecret(spec).getEncoded();
        } finally {
            spec.clearPassword();
        }
    }
}
