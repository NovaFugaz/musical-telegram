package com.bexai.authservice.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.bexai.authservice.dto.AuthResponse;
import com.bexai.authservice.dto.LoginRequest;
import com.bexai.authservice.dto.OperadorResponse;
import com.bexai.authservice.enums.Rol;
import com.bexai.authservice.enums.Turno;
import com.bexai.authservice.exception.GlobalExceptionHandler;
import com.bexai.authservice.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void login_ConCredencialesValidas_Retorna200YToken() throws Exception {
        LoginRequest request = new LoginRequest("OP-1001", "123456");
        AuthResponse response = AuthResponse.builder()
                .token("jwt.token.mock")
                .tipo("Bearer")
                .codigo("OP-1001")
                .nombre("Rin Tohsaka")
                .rol(Rol.TERRENO)
                .sucursalId(1L)
                .turno(Turno.TARDE)
                .build();

        when(authService.login(any(LoginRequest.class))).thenReturn(response);

        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt.token.mock"))
                .andExpect(jsonPath("$.codigo").value("OP-1001"))
                .andExpect(jsonPath("$.rol").value("TERRENO"));
    }

    @Test
    void login_ConPinInvalido_Retorna401() throws Exception {
        LoginRequest request = new LoginRequest("OP-1001", "000000");

        when(authService.login(any(LoginRequest.class)))
                .thenThrow(new BadCredentialsException("Código de operador o PIN incorrecto"));

        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("Unauthorized"))
                .andExpect(jsonPath("$.message").value("Código de operador o PIN incorrecto"));
    }

    @Test
    void login_ConFormatoInvalido_Retorna400BadRequest() throws Exception {
        // Código no coincide con el regex OP-XXXX y PIN no tiene 6 dígitos
        LoginRequest request = new LoginRequest("invalido", "12");

        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.details").isArray());
    }

    @Test
    void getPerfil_ConUsuarioAutenticado_Retorna200() throws Exception {
        OperadorResponse response = OperadorResponse.builder()
                .codigo("OP-1001")
                .nombre("Rin Tohsaka")
                .rol(Rol.TERRENO)
                .sucursalId(1L)
                .build();

        when(authService.getPerfil("OP-1001")).thenReturn(response);

        mockMvc.perform(get("/me").principal(() -> "OP-1001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.codigo").value("OP-1001"))
                .andExpect(jsonPath("$.nombre").value("Rin Tohsaka"));
    }
}
