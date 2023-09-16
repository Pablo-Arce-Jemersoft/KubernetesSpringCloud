package org.pabloarce.springclaud.msvc.usuarios.repositories;

import org.pabloarce.springclaud.msvc.usuarios.models.entity.Usuario;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UsuarioRepository extends CrudRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
    
    @Query("select u from Usuario u where u.email = ?1")
    Optional<Usuario> byEmailQuery(String email);
    
    boolean existsByEmail(String email);
}
