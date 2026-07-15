package com.alkewallet.controller;

//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class TransaccionController {

    private final JdbcTemplate jdbcTemplate;

    TransaccionController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Realizar depósito
     */
    @PostMapping("/deposito")
    @Transactional
    public ResponseEntity<Map<String, Object>> realizarDeposito(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();

        try {
            Long usuarioId = Long.valueOf(request.get("usuarioId").toString());
            Double monto = Double.valueOf(request.get("monto").toString());

            if (monto <= 0) {
                response.put("success", false);
                response.put("message", "El monto debe ser mayor a 0");
                return ResponseEntity.badRequest().body(response);
            }

            String updateSql = "UPDATE Usuario SET saldo = saldo + ? WHERE id = ?";
            jdbcTemplate.update(updateSql, monto, usuarioId);

            String insertTx = "INSERT INTO Transaccion (emisor_id, receptor_id, moneda_id, monto) VALUES (?, ?, 1, ?)";
            jdbcTemplate.update(insertTx, usuarioId, usuarioId, monto);

            Double nuevoSaldo = jdbcTemplate.queryForObject("SELECT saldo FROM Usuario WHERE id = ?", Double.class, usuarioId);

            response.put("success", true);
            response.put("message", "Depósito realizado exitosamente");
            response.put("nuevoSaldo", nuevoSaldo);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al realizar el depósito");
            return ResponseEntity.status(500).body(response);
        }
    }

    /**
     * Realizar transferencia
     */
    @PostMapping("/transferencia")
    @Transactional
    public ResponseEntity<Map<String, Object>> realizarTransferencia(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = new HashMap<>();

        try {
            Long emisorId = Long.valueOf(request.get("emisorId").toString());
            Long receptorId = Long.valueOf(request.get("receptorId").toString());
            Double monto = Double.valueOf(request.get("monto").toString());

            if (monto <= 0) {
                response.put("success", false);
                response.put("message", "El monto debe ser mayor a 0");
                return ResponseEntity.badRequest().body(response);
            }

            Double saldoEmisor = jdbcTemplate.queryForObject(
                "SELECT saldo FROM Usuario WHERE id = ?", Double.class, emisorId);

            if (saldoEmisor == null || saldoEmisor < monto) {
                response.put("success", false);
                response.put("message", "Saldo insuficiente");
                return ResponseEntity.badRequest().body(response);
            }

            jdbcTemplate.update("UPDATE Usuario SET saldo = saldo - ? WHERE id = ?", monto, emisorId);
            jdbcTemplate.update("UPDATE Usuario SET saldo = saldo + ? WHERE id = ?", monto, receptorId);

            String insertTx = "INSERT INTO Transaccion (emisor_id, receptor_id, moneda_id, monto) VALUES (?, ?, 1, ?)";
            jdbcTemplate.update(insertTx, emisorId, receptorId, monto);

            Double nuevoSaldo = jdbcTemplate.queryForObject("SELECT saldo FROM Usuario WHERE id = ?", Double.class, emisorId);

            response.put("success", true);
            response.put("message", "Transferencia realizada exitosamente");
            response.put("nuevoSaldo", nuevoSaldo);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error en la transferencia");
            return ResponseEntity.status(500).body(response);
        }
    }

    /**
     * Obtener historial de transacciones del usuario
     */
@GetMapping("/transacciones")
public ResponseEntity<Map<String, Object>> getTransacciones(@RequestParam Long usuarioId) {
    Map<String, Object> response = new HashMap<>();

    try {
        String sql = """
            SELECT 
                t.fecha,
                t.monto,
                CASE 
                    WHEN t.emisor_id = t.receptor_id THEN 'Depósito'
                    WHEN t.emisor_id = ? THEN 'Enviado'
                    WHEN t.receptor_id = ? THEN 'Recibido'
                    ELSE 'Otro'
                END as tipo,
                COALESCE(u1.nombre, 'Sistema') as emisor,
                COALESCE(u2.nombre, 'Sistema') as receptor
            FROM Transaccion t
            LEFT JOIN Usuario u1 ON t.emisor_id = u1.id
            LEFT JOIN Usuario u2 ON t.receptor_id = u2.id
            WHERE t.emisor_id = ? OR t.receptor_id = ?
            ORDER BY t.fecha DESC
            LIMIT 50
            """;

        List<Map<String, Object>> transacciones = jdbcTemplate.queryForList(
            sql, usuarioId, usuarioId, usuarioId, usuarioId);

        response.put("success", true);
        response.put("transacciones", transacciones);

        return ResponseEntity.ok(response);

    } catch (Exception e) {
        response.put("success", false);
        response.put("message", "Error al obtener transacciones");
        return ResponseEntity.status(500).body(response);
    }
  }
}