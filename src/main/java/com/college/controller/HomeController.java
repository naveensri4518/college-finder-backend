package com.college.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
public class HomeController {

    @GetMapping(value = "/", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> home() {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", "✅ Backend Running Successfully");
        response.put("project", "College Discovery Platform");
        response.put("version", "1.0.0");
        response.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        response.put("database", "MySQL Connected ✅");
        response.put("availableEndpoints", List.of(
            "GET  /api/colleges               — List colleges (search, filter, paginate)",
            "GET  /api/colleges/{id}           — College detail",
            "GET  /api/colleges/compare?ids=   — Compare 2-3 colleges",
            "GET  /api/colleges/saved          — Get saved colleges (auth required)",
            "POST /api/colleges/save           — Save a college (auth required)",
            "DELETE /api/colleges/save/{id}    — Unsave a college (auth required)",
            "POST /api/auth/register           — Register new user",
            "POST /api/auth/login              — Login user"
        ));
        response.put("frontend", "http://localhost:5173");
        return response;
    }

    @GetMapping(value = "/health")
    public Map<String, String> health() {
        return Map.of(
            "status", "UP",
            "service", "college-discovery-backend"
        );
    }
}
