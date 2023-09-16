package com.arcepablo.springcloud.msvc.cursos.controllers;

import com.arcepablo.springcloud.msvc.cursos.models.Usuario;
import com.arcepablo.springcloud.msvc.cursos.models.entity.Curso;
import com.arcepablo.springcloud.msvc.cursos.services.CursoService;
import feign.FeignException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class CursoController {

    private final CursoService cursoService;

    public CursoController(CursoService cursoService) {
        this.cursoService = cursoService;
    }

    @GetMapping
    public ResponseEntity<List<Curso>> listar() {
        return ResponseEntity.ok(cursoService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> detalle(@PathVariable Long id) {
        Optional<Curso> curso = cursoService.porId(id);

        if(curso.isPresent()){
            return ResponseEntity.ok(curso.get());
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody Curso curso, BindingResult result) {
        if (result.hasErrors()) {
            return validate(result);
        }
        Curso cursodb = cursoService.guardar(curso);
        return ResponseEntity.status(HttpStatus.CREATED).body(cursodb);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editar(@Valid @PathVariable Long id, BindingResult result, @RequestBody Curso curso){
        if (result.hasErrors()) {
            return validate(result);
        }
        Optional<Curso> optionalCurso = cursoService.porId(id);
        if(optionalCurso.isPresent()){
            Curso cursoDb = optionalCurso.get();
            cursoDb.setNombre(curso.getNombre());
            return ResponseEntity.status(HttpStatus.CREATED).body(cursoService.guardar(cursoDb));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id){
        Optional<Curso> optionalCurso = cursoService.porId(id);
        if(optionalCurso.isPresent()){
            cursoService.eliminar(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    
    @PutMapping("/asignar-usuario/{cursoId}")
    public ResponseEntity<?> asignarUsuario(@Valid @RequestBody Usuario usuario, @PathVariable Long cursoId) {
        Optional<Usuario> usuarioAsignado;
        try {
            usuarioAsignado = cursoService.asignarUsuario(usuario, cursoId);
        } catch (FeignException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("errorMessage",
                    e.getMessage()));
        }
        if (usuarioAsignado.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(usuarioAsignado.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    
    @PostMapping("/crear-usuario/{cursoId}}")
    public ResponseEntity<?> crearUsuario (@Valid @RequestBody Usuario usuario, @PathVariable Long cursoId) {
        Optional<Usuario> usuarioMsvc;
        try{
           usuarioMsvc = cursoService.crearUsuario(usuario, cursoId);
        }catch (FeignException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("errorMessage",
                    e.getMessage()));
        }
        if(usuarioMsvc.isPresent()){
            return ResponseEntity.status(HttpStatus.CREATED).body(usuarioMsvc.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    
    @DeleteMapping("/desasignar-usuario/{cursoId}")
    public ResponseEntity<?> desasignarUsuario (@Valid @RequestBody Usuario usuario, Long cursoId) {
        Optional<Usuario> usuarioMsvc;
        try{
            usuarioMsvc = cursoService.eliminarUsuarioDeCurso(usuario, cursoId);
        }catch (FeignException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("errorMessage", e.getMessage()));
        }
        return usuarioMsvc.map(ResponseEntity::ok).orElseGet(() ->ResponseEntity.notFound().build());
    }
    
    private static ResponseEntity<Map<String, String>> validate(BindingResult result) {
        Map<String, String> errores = new HashMap<>();
        result.getFieldErrors().forEach(error -> errores.put(error.getField(), "El campo " + error.getField() +
                " " + error.getDefaultMessage()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errores);
    }
}
