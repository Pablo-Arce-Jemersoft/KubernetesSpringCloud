package org.pabloarce.springclaud.msvc.usuarios.controllers;

import jakarta.validation.Valid;
import org.pabloarce.springclaud.msvc.usuarios.models.entity.Usuario;
import org.pabloarce.springclaud.msvc.usuarios.services.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class UsuarioController {
    
    private final UsuarioService service;
    
    public UsuarioController(UsuarioService service) {
        this.service = service;
    }
    
    @GetMapping
    public List<Usuario> listar() {
        return service.listar();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> detalle(@PathVariable Long id) {
        Optional<Usuario> usuarioOptional = service.porId(id);
        return usuarioOptional.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody Usuario usuario, BindingResult result) {
    
        if (result.hasErrors()) {
            return validate(result);
        }
        
        if(service.porEmail(usuario.getEmail()).isPresent()){
            return ResponseEntity.badRequest()
                    .body(Collections.singletonMap("mensaje", "Ya existe un usuario con este email"));
        }
        
        return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(usuario));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editar(@Valid @RequestBody Usuario usuario, BindingResult result, @PathVariable Long id) {
        if (result.hasErrors()) {
            return validate(result);
        }
        Optional<Usuario> usuarioOptional = service.porId(id);
        if (usuarioOptional.isPresent()) {
            Usuario usuarioDb = usuarioOptional.get();
    
            if(!usuario.getEmail().equalsIgnoreCase(usuarioDb.getEmail()) && service.porEmail(usuario.getEmail()).isPresent()){
                return ResponseEntity.badRequest()
                        .body(Collections.singletonMap("mensaje", "Ya existe un usuario con este email"));
            }
            
            usuarioDb.setNombre(usuario.getNombre());
            usuarioDb.setEmail(usuario.getEmail());
            usuarioDb.setPassword(usuario.getPassword());
            return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(usuarioDb));
        }
        return ResponseEntity.notFound().build();
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        Optional<Usuario> usuarioOptional = service.porId(id);
        if (usuarioOptional.isPresent()) {
            service.eliminar(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
    
    @GetMapping("/usuarios-por-curso")
    public ResponseEntity<?> obtenerAlumnosPorCurso(@RequestParam List<Long> ids) {
        return ResponseEntity.ok(service.findAllById(ids));
    }

    private static ResponseEntity<Map<String, String>> validate(BindingResult result) {
        Map<String, String> errores = new HashMap<>();
        result.getFieldErrors().forEach(error -> errores.put(error.getField(), "El campo " + error.getField() +
                " " + error.getDefaultMessage()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errores);
    }
    
}
