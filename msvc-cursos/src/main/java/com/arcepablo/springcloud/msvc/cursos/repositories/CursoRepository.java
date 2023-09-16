package com.arcepablo.springcloud.msvc.cursos.repositories;

import com.arcepablo.springcloud.msvc.cursos.models.entity.Curso;
import org.springframework.data.repository.CrudRepository;

public interface CursoRepository extends CrudRepository<Curso, Long> {
}
