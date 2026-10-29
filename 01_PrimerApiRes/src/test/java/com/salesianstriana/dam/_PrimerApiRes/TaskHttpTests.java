package com.salesianstriana.dam._PrimerApiRes;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class TaskHttpTests {
  @Autowired MockMvc mvc;
  @Autowired TaskRepository repository;
  @Autowired ObjectMapper json;

  @BeforeEach
  void reset() {
    repository.deleteAll();
  }

  @Test
  void completeLifecycleWritesAndReloads() throws Exception {
    String response =
        mvc.perform(
                post("/task/")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json.writeValueAsBytes(new Task("Review", "Fictional exercise"))))
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse()
            .getContentAsString();
    long id = json.readTree(response).path("id").asLong();
    assertThat(repository.findById(id).orElseThrow().getTitle()).isEqualTo("Review");
    mvc.perform(get("/task/")).andExpect(status().isOk()).andExpect(jsonPath("$[0].id").value(id));
    mvc.perform(get("/task/" + id))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.title").value("Review"));
    mvc.perform(
            put("/task/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"Revised\",\"text\":null}"))
        .andExpect(status().isOk());
    assertThat(repository.findById(id).orElseThrow().getTitle()).isEqualTo("Revised");
    mvc.perform(delete("/task/" + id))
        .andExpect(status().isNoContent())
        .andExpect(content().string(""));
    assertThat(repository.findById(id)).isEmpty();
  }

  @Test
  void absentReadIs404() throws Exception {
    mvc.perform(get("/task/999999"))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.code").value("not_found"));
  }

  @Test
  void absentEditIs404WithoutInsert() throws Exception {
    mvc.perform(
            put("/task/999999")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"Missing\"}"))
        .andExpect(status().isNotFound());
    assertThat(repository.count()).isZero();
  }

  @Test
  void absentDeleteIs404() throws Exception {
    mvc.perform(delete("/task/999999")).andExpect(status().isNotFound());
  }

  @Test
  void createCannotOverwriteSuppliedIdentity() throws Exception {
    Task existing = repository.saveAndFlush(new Task("Keep", "Original"));
    mvc.perform(
            post("/task/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    json.writeValueAsBytes(new Task(existing.getId(), "Overwrite", "Changed"))))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value("invalid_request"));
    assertThat(repository.count()).isEqualTo(1);
    assertThat(repository.findById(existing.getId()).orElseThrow().getTitle()).isEqualTo("Keep");
  }

  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {" ", "\t\n"})
  void titleIsRequiredWithoutWrite(String title) throws Exception {
    mvc.perform(
            post("/task/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.writeValueAsBytes(new Task(title, null))))
        .andExpect(status().isBadRequest());
    assertThat(repository.count()).isZero();
  }

  @Test
  void maximumMappedLengthsPersist() throws Exception {
    mvc.perform(
            post("/task/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.writeValueAsBytes(new Task("t".repeat(255), "x".repeat(255)))))
        .andExpect(status().isCreated());
    assertThat(repository.findAll().get(0).getText()).hasSize(255);
  }

  @Test
  void oversizedEditAndCreateDoNotWrite() throws Exception {
    Task existing = repository.saveAndFlush(new Task("Keep", "Original"));
    mvc.perform(
            put("/task/" + existing.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.writeValueAsBytes(new Task("x".repeat(256), "Changed"))))
        .andExpect(status().isBadRequest());
    mvc.perform(
            post("/task/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.writeValueAsBytes(new Task("New", "x".repeat(256)))))
        .andExpect(status().isBadRequest());
    Task reloaded = repository.findById(existing.getId()).orElseThrow();
    assertThat(reloaded.getTitle()).isEqualTo("Keep");
    assertThat(reloaded.getText()).isEqualTo("Original");
    assertThat(repository.count()).isEqualTo(1);
  }

  @Test
  void editUsesPathIdentity() throws Exception {
    Task first = repository.saveAndFlush(new Task("First", null));
    Task second = repository.saveAndFlush(new Task("Second", null));
    mvc.perform(
            put("/task/" + first.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(json.writeValueAsBytes(new Task(second.getId(), "Updated first", null))))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(first.getId()));
    assertThat(repository.findById(second.getId()).orElseThrow().getTitle()).isEqualTo("Second");
  }

  @Test
  void malformedJsonAndPathAre400() throws Exception {
    mvc.perform(post("/task/").contentType(MediaType.APPLICATION_JSON).content("{"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value("invalid_request"));
    mvc.perform(get("/task/not-a-number")).andExpect(status().isBadRequest());
    assertThat(repository.count()).isZero();
  }
}
