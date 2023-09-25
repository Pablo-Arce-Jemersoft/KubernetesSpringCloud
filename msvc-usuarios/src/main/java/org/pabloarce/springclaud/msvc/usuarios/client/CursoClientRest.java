package org.pabloarce.springclaud.msvc.usuarios.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "msvc-cursos", url = "msvc-cursos:8002")
public interface CursoClientRest {
    @DeleteMapping("/eliminar-usuario-curso/{id}")
    void eliminarUsuarioCursoPorId(@PathVariable Long id);
}
