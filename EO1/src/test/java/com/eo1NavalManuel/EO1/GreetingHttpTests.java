package com.eo1NavalManuel.EO1;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class GreetingHttpTests {
  @Autowired MockMvc mvc;
  @Autowired ObjectMapper json;

  @Test
  void defaultGreetingKeepsSpanishContract() throws Exception {
    mvc.perform(get("/saludo"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").value("Hola, mundo!"))
        .andExpect(jsonPath("$.id").isNumber());
  }

  @Test
  void unicodeNameIsReturnedInJson() throws Exception {
    mvc.perform(get("/saludo").param("name", "Álex <demo>"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").value("Hola, Álex <demo>!"));
  }

  @Test
  void successiveHttpRequestsIncrementIdentity() throws Exception {
    long first =
        json.readTree(mvc.perform(get("/saludo")).andReturn().getResponse().getContentAsString())
            .path("id")
            .asLong();
    long next =
        json.readTree(mvc.perform(get("/saludo")).andReturn().getResponse().getContentAsString())
            .path("id")
            .asLong();
    assertThat(next).isEqualTo(first + 1);
  }

  @Test
  void newControllerStartsAtZeroAndConcurrentCallsHaveDistinctIds() throws Exception {
    ControladorSaludo controller = new ControladorSaludo();
    assertThat(controller.saludo("demo").getId()).isZero();
    ExecutorService executor = Executors.newFixedThreadPool(4);
    try {
      List<Callable<Long>> calls = new ArrayList<>();
      for (int i = 0; i < 100; i++) calls.add(() -> controller.saludo("demo").getId());
      HashSet<Long> ids = new HashSet<>();
      for (Future<Long> result : executor.invokeAll(calls, 5, TimeUnit.SECONDS))
        ids.add(result.get());
      assertThat(ids).hasSize(100).contains(1L, 100L);
    } finally {
      executor.shutdownNow();
      assertThat(executor.awaitTermination(5, TimeUnit.SECONDS)).isTrue();
    }
  }
}
