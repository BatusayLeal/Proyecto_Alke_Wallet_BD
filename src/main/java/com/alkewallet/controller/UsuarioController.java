package com.alkewallet.controller;

//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/usuario")
@CrossOrigin(origins = "*")
public class UsuarioController {

    private final JdbcTemplate jdbcTemplate;

    UsuarioController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Obtener saldo actual del usuario
     */
    @GetMapping("/saldo")
    public ResponseEntity<Map<String, Object>> getSaldo(@RequestParam Long usuarioId) {
        Map<String, Object> response = new HashMap<>();

        try {
            String sql = "SELECT saldo FROM Usuario WHERE id = ?";
            Double saldo = jdbcTemplate.queryForObject(sql, Double.class, usuarioId);

            response.put("success", true);
            response.put("saldo", saldo != null ? saldo : 0.0);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Usuario no encontrado");
            return ResponseEntity.status(404).body(response);
        }
    }
}