package com.example.demo;

import javax.servlet.http.HttpServletResponse;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@org.springframework.stereotype.Controller
public class Controller {
  @GetMapping("/")
  public String greeting(
      @RequestParam(defaultValue = "") String name, Model model, HttpServletResponse response) {
    boolean invalid = name.length() > 80 || name.codePoints().anyMatch(Character::isISOControl);
    model.addAttribute("draftName", name);
    model.addAttribute("displayName", name.strip());
    model.addAttribute("invalid", invalid);
    if (invalid) response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    return "index";
  }
}
