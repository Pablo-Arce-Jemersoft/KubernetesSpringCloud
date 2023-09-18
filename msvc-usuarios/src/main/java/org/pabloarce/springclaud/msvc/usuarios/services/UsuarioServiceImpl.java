package org.pabloarce.springclaud.msvc.usuarios.services;

import org.pabloarce.springclaud.msvc.usuarios.client.CursoClientRest;
import org.pabloarce.springclaud.msvc.usuarios.models.entity.Usuario;
import org.pabloarce.springclaud.msvc.usuarios.repositories.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService{

    private final UsuarioRepository repository;
    
    private final CursoClientRest cursoClientRest;

    public UsuarioServiceImpl(UsuarioRepository repository, CursoClientRest cursoClientRest) {
        this.repository = repository;
        this.cursoClientRest = cursoClientRest;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> listar() {
        return (List<Usuario>) repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> porId(Long id) {
        return repository.findById(id);
    }

    @Override
    @Transactional
    public Usuario guardar(Usuario usuario) {
        return repository.save(usuario);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        repository.deleteById(id);
        cursoClientRest.eliminarUsuarioCursoPorId(id);
    }
    
    @Override
    public Optional<Usuario> porEmail(String email) {
        return repository.findByEmail(email);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Usuario> findAllById(List<Long> ids) {
        return (List<Usuario>) repository.findAllById(ids);
    }
}
