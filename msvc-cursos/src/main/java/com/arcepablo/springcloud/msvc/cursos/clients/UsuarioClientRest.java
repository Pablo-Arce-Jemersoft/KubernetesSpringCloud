package com.arcepablo.springcloud.msvc.cursos.clients;

import com.arcepablo.springcloud.msvc.cursos.models.Usuario;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "msvc-usuarios", url = "localhost:8001")
public interface UsuarioClientRest {

    @GetMapping("/{id}")
    Usuario getUserMvsc(@PathVariable Long id);
    
    @PostMapping
    Usuario createUserMvsc(@RequestBody Usuario usuario);
}
