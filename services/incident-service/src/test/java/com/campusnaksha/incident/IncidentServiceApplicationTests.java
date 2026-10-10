package com.campusnaksha.incident;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(
        properties = {
                "security.jwt.secret=Q2FtcHVzTmFrc2hhVGVzdE9ubHlTZWNyZXRLZXkzMiE=",
                "eureka.client.enabled=false"
        }
)
class IncidentServiceApplicationTests {

    @Test
    void contextLoads() {
    }
}