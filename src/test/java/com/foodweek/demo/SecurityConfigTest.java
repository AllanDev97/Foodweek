package com.foodweek.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.MockMvc;

import com.foodweek.demo.security.UserDetailsServiceImpl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityConfigTest {

    @Autowired
    private SecurityFilterChain securityFilterChain;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void contextLoadsSecurityBeans() {
        assertThat(securityFilterChain).isNotNull();
        assertThat(authenticationManager).isNotNull();
        assertThat(passwordEncoder).isNotNull();
        assertThat(userDetailsService).isNotNull();
    }

    @Test
    void shouldRejectUnauthorizedAccessToProtectedRoute() throws Exception {
        mockMvc.perform(get("/api/users")) // 🔁 Remplace par une vraie route protégée
            .andExpect(status().isForbidden());
    }
}
