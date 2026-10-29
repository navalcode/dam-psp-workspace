package com.example.demo;

import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;

@Configuration
public class PageLanguages {
  @Bean
  public LocaleResolver localeResolver() {
    return new LocaleResolver() {
      @Override
      public Locale resolveLocale(HttpServletRequest request) {
        return "en".equals(request.getParameter("lang")) ? Locale.ENGLISH : new Locale("es");
      }

      @Override
      public void setLocale(
          HttpServletRequest request, HttpServletResponse response, Locale locale) {
        throw new UnsupportedOperationException(
            "Select the language through the lang query parameter.");
      }
    };
  }
}
