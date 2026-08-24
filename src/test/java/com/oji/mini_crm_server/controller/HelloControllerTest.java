package com.oji.mini_crm_server.controller;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class HelloControllerTest {

    @Test
    void greet_shouldReturnPlainText() {
        HelloController controller = new HelloController();
        String result = controller.greet();
        assertThat(result).isEqualTo("Hello from Mini CRM Server");
    }
}


