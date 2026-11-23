package com.beautysalon;

import com.beautysalon.controller.HealthController;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class HealthControllerTest {

    @Test
    void healthRenvoieUP() {
        assertThat(new HealthController().health()).containsEntry("status", "UP");
    }
}
