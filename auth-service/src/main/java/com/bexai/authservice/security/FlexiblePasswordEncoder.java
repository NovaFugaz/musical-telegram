package com.bexai.authservice.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * PasswordEncoder que soporta tanto hashes BCrypt como texto plano para los PINs
 * de desarrollo precargados en data.sql.
 */
@Component
public class FlexiblePasswordEncoder implements PasswordEncoder {

    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @Override
    public String encode(CharSequence rawPassword) {
        return bCryptPasswordEncoder.encode(rawPassword);
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        if (rawPassword == null || encodedPassword == null) {
            return false;
        }

        // Si la contraseña almacenada parece un hash BCrypt ($2a$, $2b$, $2y$)
        if (encodedPassword.startsWith("$2a$") || encodedPassword.startsWith("$2b$") || encodedPassword.startsWith("$2y$")) {
            return bCryptPasswordEncoder.matches(rawPassword, encodedPassword);
        }

        // De lo contrario, comparar como texto plano (útil para seeds de data.sql)
        return rawPassword.toString().equals(encodedPassword);
    }
}
