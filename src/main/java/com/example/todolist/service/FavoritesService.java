package com.example.todolist.service;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class FavoritesService {

  private static final String FAVORITES_SESSION_KEY = "favoriteTaskIds";

  @SuppressWarnings("unchecked")
  public Set<Long> getFavorites(HttpSession session) {
    Set<Long> favorites = (Set<Long>) session.getAttribute(FAVORITES_SESSION_KEY);
    if (favorites == null) {
      favorites = new HashSet<>();
      session.setAttribute(FAVORITES_SESSION_KEY, favorites);
    }
    return favorites;
  }

  public void addFavorite(Long taskId, HttpSession session) {
    Set<Long> favorites = getFavorites(session);
    favorites.add(taskId);
    session.setAttribute(FAVORITES_SESSION_KEY, favorites);
  }

  public void removeFavorite(Long taskId, HttpSession session) {
    Set<Long> favorites = getFavorites(session);
    favorites.remove(taskId);
    session.setAttribute(FAVORITES_SESSION_KEY, favorites);
  }

  public boolean isFavorite(Long taskId, HttpSession session) {
    return getFavorites(session).contains(taskId);
  }
}