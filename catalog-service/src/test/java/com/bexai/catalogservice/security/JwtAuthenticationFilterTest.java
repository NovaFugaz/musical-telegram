package com.bexai.catalogservice.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;

@ExtendWith(MockitoExtension.class)
public class JwtAuthenticationFilterTest {
    @Mock
    private JwtService jwtService;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;
	
    @AfterEach
    void limpiarContexto() {
        SecurityContextHolder.clearContext();
    }

    // usuario queda autenticado
    @Test
    void doFilter_TokenValido_AutenticaConRolYContinuaLaCadena() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.addHeader("Authorization", "Bearer token-valido");

        Claims claims = Jwts.claims()
                .subject("OP-1003")
                .add("rol", "GERENCIA")
                .build();

        when(jwtService.extractAllClaims("token-valido")).thenReturn(claims);

        jwtAuthenticationFilter.doFilter(request, response, filterChain);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(auth);
        assertEquals("OP-1003", auth.getName());
        assertEquals(List.of("ROLE_GERENCIA"),
                auth.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList());
        verify(jwtService).extractAllClaims("token-valido");
        verify(filterChain).doFilter(request, response);
    }

    // sin el Authorization el usuario no queda autenticado
    @Test
    void doFilter_SinHeaderAuthorization_NoAutenticaYContinuaLaCadena() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        jwtAuthenticationFilter.doFilter(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());

        verifyNoInteractions(jwtService);
        verify(filterChain).doFilter(request, response);
    }

    // sin prefijo bearer
    @Test
    void doFilter_HeaderSinPrefijoBearer_NoAutenticaYContinuaLaCadena() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.addHeader("Authorization", "token-sin-prefijo");

        jwtAuthenticationFilter.doFilter(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());

        verifyNoInteractions(jwtService);
        verify(filterChain).doFilter(request, response);
    }

    // comprobar que jwtService rechaza el token
    @Test
    void doFilter_TokenInvalido_NoAutenticaYContinuaLaCadena() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.addHeader("Authorization", "Bearer token-invalido");

        when(jwtService.extractAllClaims("token-invalido"))
                .thenThrow(new JwtException("Token inválido"));

        jwtAuthenticationFilter.doFilter(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());

        verify(jwtService).extractAllClaims("token-invalido");
        verify(filterChain).doFilter(request, response);
    }

    // token valido sin rol no deja al usuario autenticado
    @Test
    void doFilter_TokenSinRol_NoAutenticaYContinuaLaCadena() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.addHeader("Authorization", "Bearer token-sin-rol");

        Claims claimsSinRol = Jwts.claims()
                .subject("OP-1003")
                .build();

        when(jwtService.extractAllClaims("token-sin-rol")).thenReturn(claimsSinRol);

        jwtAuthenticationFilter.doFilter(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());

        verify(jwtService).extractAllClaims("token-sin-rol");
        verify(filterChain).doFilter(request, response);
    }
}
