package com.example.todolist.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/preferences")
@Tag(name = "Preferences", description = "Настройки отображения")
public class PreferencesController {

  private static final String VIEW_PREFERENCE_COOKIE = "viewPreference";

  @GetMapping("/view")
  @Operation(summary = "Получить режим отображения")
  public ResponseEntity<String> getViewPreference(
      @CookieValue(name = VIEW_PREFERENCE_COOKIE, required = false) String preference,
      HttpServletResponse response) {
    if (preference == null) {
      preference = "detailed";
      Cookie cookie = new Cookie(VIEW_PREFERENCE_COOKIE, preference);
      cookie.setPath("/");
      cookie.setMaxAge(60 * 60 * 24 * 365);
      response.addCookie(cookie);
    }
    return ResponseEntity.ok(preference);
  }

  @PostMapping("/view")
  @Operation(summary = "Изменить режим отображения")
  public ResponseEntity<Void> setViewPreference(
      @RequestParam String mode,
      HttpServletResponse response) {
    Cookie cookie = new Cookie(VIEW_PREFERENCE_COOKIE, mode);
    cookie.setPath("/");
    cookie.setMaxAge(60 * 60 * 24 * 365);
    response.addCookie(cookie);
    return ResponseEntity.ok().build();
  }
}
