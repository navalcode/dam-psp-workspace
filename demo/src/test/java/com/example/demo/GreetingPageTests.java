package com.example.demo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class GreetingPageTests {
  @Autowired MockMvc mvc;

  @Test
  void defaultIsSpanishWorldAndNativeGetForm() throws Exception {
    mvc.perform(get("/"))
        .andExpect(status().isOk())
        .andExpect(xpath("//html/@lang").string("es"))
        .andExpect(xpath("//h2[@id='greeting']").string("Hola, mundo."))
        .andExpect(xpath("//form/@method").string("get"))
        .andExpect(xpath("//script").nodeCount(0));
  }

  @Test
  void englishNameAndLanguageLinksKeepDraft() throws Exception {
    mvc.perform(get("/").param("lang", "en").param("name", "Ada Demo"))
        .andExpect(status().isOk())
        .andExpect(xpath("//html/@lang").string("en"))
        .andExpect(xpath("//h2[@id='greeting']").string("Hello, Ada Demo."))
        .andExpect(xpath("//a[@hreflang='es']/@href").string("/?lang=es&name=Ada%20Demo"));
  }

  @Test
  void blankUsesWorldAndUnicodeSurvives() throws Exception {
    mvc.perform(get("/").param("name", "  "))
        .andExpect(status().isOk())
        .andExpect(xpath("//h2[@id='greeting']").string("Hola, mundo."));
    mvc.perform(get("/").param("name", "  Álex  "))
        .andExpect(status().isOk())
        .andExpect(xpath("//h2[@id='greeting']").string("Hola, Álex."));
  }

  @Test
  void markupIsTextAndCannotBecomeElements() throws Exception {
    String value = "<script>alert('demo')</script>";
    mvc.perform(get("/").param("name", value))
        .andExpect(status().isOk())
        .andExpect(xpath("//script").nodeCount(0))
        .andExpect(xpath("//h2[@id='greeting']").string("Hola, " + value + "."))
        .andExpect(xpath("//input[@id='name']/@value").string(value));
  }

  @Test
  void longNameHasAccessibleInline400AndRetainsDraft() throws Exception {
    mvc.perform(get("/").param("name", "x".repeat(81)))
        .andExpect(status().isBadRequest())
        .andExpect(xpath("//input[@id='name']/@value").string("x".repeat(81)))
        .andExpect(xpath("//input[@id='name']/@aria-invalid").string("true"))
        .andExpect(
            xpath("//p[@id='name-error']")
                .string("Usa hasta 80 caracteres, sin saltos de línea ni caracteres de control."));
  }

  @Test
  void controlCharactersAreRejectedInEnglish() throws Exception {
    mvc.perform(get("/").param("lang", "en").param("name", "Ada\nDemo"))
        .andExpect(status().isBadRequest())
        .andExpect(
            xpath("//p[@id='name-error']")
                .string("Use up to 80 characters, without line breaks or control characters."));
  }

  @Test
  void boundaryAndResetRecover() throws Exception {
    mvc.perform(get("/").param("name", "x".repeat(80))).andExpect(status().isOk());
    mvc.perform(get("/").param("lang", "en"))
        .andExpect(status().isOk())
        .andExpect(xpath("//input[@id='name']/@value").string(""))
        .andExpect(xpath("//h2[@id='greeting']").string("Hello, world."));
  }

  @Test
  void unknownLanguageFallsBackToSpanish() throws Exception {
    mvc.perform(get("/").param("lang", "fr"))
        .andExpect(status().isOk())
        .andExpect(xpath("//html/@lang").string("es"));
  }

  @Test
  void localCssAndFontAreServed() throws Exception {
    mvc.perform(get("/styles.css")).andExpect(status().isOk());
    assertThat(
            mvc.perform(get("/fonts/Outfit.woff2"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsByteArray()
                .length)
        .isGreaterThan(1000);
  }
}
