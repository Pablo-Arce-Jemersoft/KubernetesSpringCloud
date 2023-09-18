package com.arcepablo.springcloud.msvc.cursos.services;

import com.arcepablo.springcloud.msvc.cursos.models.Usuario;
import com.arcepablo.springcloud.msvc.cursos.models.entity.Curso;

import java.util.List;
import java.util.Optional;

public interface CursoService {
    List<Curso> listar();
    
    Optional<Curso> listarCursoConUsuarios(Long id);
    Optional<Curso> porId(Long id);
    Curso guardar(Curso curso);
    void eliminar(Long id);
    
    Optional<Usuario> asignarUsuario (Usuario usuario, Long cursoId);
    
    Optional<Usuario> crearUsuario (Usuario usuario, Long cursoId);
    
    Optional<Usuario> eliminarUsuarioDeCurso (Usuario usuario, Long cursoId);
    
    void eliminarUsuarioCursoPorId(Long id);
    
}
