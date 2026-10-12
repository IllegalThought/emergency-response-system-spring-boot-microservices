package com.campusnaksha.incident;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.campusnaksha.incident.config.SecurityConfig;
import com.campusnaksha.incident.config.SecurityErrorHandler;
import com.campusnaksha.incident.controller.IncidentController;
import com.campusnaksha.incident.dto.IncidentResponse;
import com.campusnaksha.incident.exception.GlobalExceptionHandler;
import com.campusnaksha.incident.service.IncidentService;

@WebMvcTest(controllers = IncidentController.class)
@Import({
        SecurityConfig.class,
        SecurityErrorHandler.class,
        GlobalExceptionHandler.class
})
class IncidentSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private IncidentService incidentService;

    @MockitoBean
    private JwtDecoder jwtDecoder;


    @Test
    void unauthenticatedRequestReturns401() throws Exception {

        mockMvc.perform(
                get("/api/incidents")
        )
        .andExpect(
                status().isUnauthorized()
        );
    }


    @Test
    void studentCanViewIncidents() throws Exception {

        when(
                incidentService.findAll(null, null)
        ).thenReturn(
                List.of()
        );

        UUID studentId = UUID.randomUUID();

        mockMvc.perform(
                get("/api/incidents")
                        .with(
                                jwt()
                                        .jwt(jwt ->
                                                jwt.subject(
                                                        studentId.toString()
                                                )
                                        )
                                        .authorities(
                                                new SimpleGrantedAuthority(
                                                        "ROLE_STUDENT"
                                                )
                                        )
                        )
        )
        .andExpect(
                status().isOk()
        );
    }


    @Test
    void guardCanViewIncidents() throws Exception {

        when(
                incidentService.findAll(null, null)
        ).thenReturn(
                List.of()
        );

        UUID guardId = UUID.randomUUID();

        mockMvc.perform(
                get("/api/incidents")
                        .with(
                                jwt()
                                        .jwt(jwt ->
                                                jwt.subject(
                                                        guardId.toString()
                                                )
                                        )
                                        .authorities(
                                                new SimpleGrantedAuthority(
                                                        "ROLE_GUARD"
                                                )
                                        )
                        )
        )
        .andExpect(
                status().isOk()
        );
    }


    @Test
    void adminCanViewIncidents() throws Exception {

        when(
                incidentService.findAll(null, null)
        ).thenReturn(
                List.of()
        );

        UUID adminId = UUID.randomUUID();

        mockMvc.perform(
                get("/api/incidents")
                        .with(
                                jwt()
                                        .jwt(jwt ->
                                                jwt.subject(
                                                        adminId.toString()
                                                )
                                        )
                                        .authorities(
                                                new SimpleGrantedAuthority(
                                                        "ROLE_ADMIN"
                                                )
                                        )
                        )
        )
        .andExpect(
                status().isOk()
        );
    }


    @Test
    void studentCannotUpdateStatus() throws Exception {

        UUID incidentId = UUID.randomUUID();
        UUID studentId = UUID.randomUUID();

        mockMvc.perform(
                patch(
                        "/api/incidents/"
                                + incidentId
                                + "/status"
                )
                        .with(
                                jwt()
                                        .jwt(jwt ->
                                                jwt.subject(
                                                        studentId.toString()
                                                )
                                        )
                                        .authorities(
                                                new SimpleGrantedAuthority(
                                                        "ROLE_STUDENT"
                                                )
                                        )
                        )
                        .contentType(
                                "application/json"
                        )
                        .content("""
                                {
                                    "status": "ACKNOWLEDGED"
                                }
                                """)
        )
        .andExpect(
                status().isForbidden()
        );
    }


    @Test
    void guardCannotUpdateStatus() throws Exception {

        UUID incidentId = UUID.randomUUID();
        UUID guardId = UUID.randomUUID();

        mockMvc.perform(
                patch(
                        "/api/incidents/"
                                + incidentId
                                + "/status"
                )
                        .with(
                                jwt()
                                        .jwt(jwt ->
                                                jwt.subject(
                                                        guardId.toString()
                                                )
                                        )
                                        .authorities(
                                                new SimpleGrantedAuthority(
                                                        "ROLE_GUARD"
                                                )
                                        )
                        )
                        .contentType(
                                "application/json"
                        )
                        .content("""
                                {
                                    "status": "ACKNOWLEDGED"
                                }
                                """)
        )
        .andExpect(
                status().isForbidden()
        );
    }


    @Test
    void adminCanUpdateStatus() throws Exception {

        UUID incidentId = UUID.randomUUID();
        UUID adminId = UUID.randomUUID();

        when(
                incidentService.updateStatus(
                        eq(incidentId),
                        any(),
                        eq(adminId)
                )
        ).thenReturn(
                (IncidentResponse) null
        );

        mockMvc.perform(
                patch(
                        "/api/incidents/"
                                + incidentId
                                + "/status"
                )
                        .with(
                                jwt()
                                        .jwt(jwt ->
                                                jwt.subject(
                                                        adminId.toString()
                                                )
                                        )
                                        .authorities(
                                                new SimpleGrantedAuthority(
                                                        "ROLE_ADMIN"
                                                )
                                        )
                        )
                        .contentType(
                                "application/json"
                        )
                        .content("""
                                {
                                    "status": "ACKNOWLEDGED",
                                    "note": "Issue verified by administrator."
                                }
                                """)
        )
        .andExpect(
                status().isOk()
        );

        verify(
                incidentService
        ).updateStatus(
                eq(incidentId),
                any(),
                eq(adminId)
        );
    }
}