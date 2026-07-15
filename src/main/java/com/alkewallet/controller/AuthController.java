package com.alkewallet.controller;

//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*") // Permite llamadas desde el frontend
public class AuthController {

    private final JdbcTemplate jdbcTemplate;

    AuthController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Endpoint de Login
     * El frontend enviará email y clave mediante fetch()
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> credentials) {
        
        String email = credentials.get("email");
        String clave = credentials.get("clave");

        Map<String, Object> response = new HashMap<>();

        try {
            String sql = "SELECT id, nombre, email, saldo FROM Usuario WHERE email = ? AND clave = ?";
            
            Map<String, Object> user = jdbcTemplate.queryForMap(sql, email, clave);

            // Login exitoso
            response.put("success", true);
            response.put("message", "Login exitoso");
            response.put("user", user);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            // Login fallido
            response.put("success", false);
            response.put("message", "Email o contraseña incorrectos");
            return ResponseEntity.status(401).body(response);
        }
    }
}