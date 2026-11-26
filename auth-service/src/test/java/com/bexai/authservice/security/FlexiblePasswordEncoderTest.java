package com.bexai.authservice.security;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class FlexiblePasswordEncoderTest {

    private final FlexiblePasswordEncoder encoder = new FlexiblePasswordEncoder();

    @Test
    void matches_ConTextoPlano_RetornaTrue() {
        assertTrue(encoder.matches("123456", "123456"));
        assertFalse(encoder.matches("123456", "654321"));
    }

    @Test
    void matches_ConBCryptHash_RetornaTrue() {
        String hash = encoder.encode("123456");
        assertNotNull(hash);
        assertTrue(hash.startsWith("$2a$"));
        assertTrue(encoder.matches("123456", hash));
        assertFalse(encoder.matches("wrongpin", hash));
    }
}
