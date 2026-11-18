package pl.fitrent;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import pl.fitrent.security.JwtService;
import pl.fitrent.user.User;
import pl.fitrent.user.UserRole;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {
        "app.jwt.secret=integration-test-jwt-secret-key-very-long-2026"
})
@AutoConfigureMockMvc
class ApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtService jwtService;

    @Test
    void shouldReturnUnauthorizedWithoutToken() throws Exception {
        mockMvc.perform(
                        get("/api/equipment")
                )
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldAllowUserToReadEquipment() throws Exception {
        User user = new User(
                "integration-user",
                "password",
                UserRole.USER
        );

        String token = jwtService.generateToken(user);

        mockMvc.perform(
                        get("/api/equipment?page=0&size=10")
                                .header(
                                        HttpHeaders.AUTHORIZATION,
                                        "Bearer " + token
                                )
                )
                .andExpect(status().isOk());
    }
    
    @Test
    void shouldForbidUserFromCreatingEquipment() throws Exception {
        User user = new User(
                "integration-user",
                "password",
                UserRole.USER
        );

        String token = jwtService.generateToken(user);

        mockMvc.perform(
                        post("/api/equipment")
                                .header(
                                        HttpHeaders.AUTHORIZATION,
                                        "Bearer " + token
                                )
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                    {
                                      "name": "Test equipment",
                                      "description": "Integration test",
                                      "categoryId": 1,
                                      "active": true
                                    }
                                    """)
                )
                .andExpect(status().isForbidden());
    }
}