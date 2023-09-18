package com.arcepablo.springcloud.msvc.cursos.services;

import com.arcepablo.springcloud.msvc.cursos.clients.UsuarioClientRest;
import com.arcepablo.springcloud.msvc.cursos.models.Usuario;
import com.arcepablo.springcloud.msvc.cursos.models.entity.Curso;
import com.arcepablo.springcloud.msvc.cursos.models.entity.CursoUsuario;
import com.arcepablo.springcloud.msvc.cursos.repositories.CursoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CursoServiceImpl implements CursoService{

    private final CursoRepository cursoRepository;
    
    private final UsuarioClientRest usuarioClientRest;

    public CursoServiceImpl(CursoRepository cursoRepository, UsuarioClientRest usuarioClientRest) {
        this.cursoRepository = cursoRepository;
        this.usuarioClientRest = usuarioClientRest;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Curso> listar() {
        return (List<Curso>) cursoRepository.findAll();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Curso> listarCursoConUsuarios(Long id) {
        Optional<Curso> cursoDb = cursoRepository.findById(id);
        if (cursoDb.isPresent()) {
            Curso curso = cursoDb.get();
            if (!curso.getCursoUsuarios().isEmpty()) {
                List<Long> ids =
                        curso.getCursoUsuarios().stream().map(cursoUsuario -> cursoUsuario.getUsuarioId()).collect(Collectors.toList());
                List<Usuario> usuarios = usuarioClientRest.getAllUsuariosMsvcByIds(ids);
                curso.setUsuarios(usuarios);
            }
            return Optional.of(curso);
        }
        return Optional.empty();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Curso> porId(Long id) {
        return cursoRepository.findById(id);
    }

    @Override
    @Transactional
    public Curso guardar(Curso curso) {
        return cursoRepository.save(curso);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        cursoRepository.deleteById(id);
    }
    
    @Override
    @Transactional
    public Optional<Usuario> asignarUsuario(Usuario usuario, Long cursoId) {
        Optional<Curso> cursoBd = cursoRepository.findById(cursoId);
        
        if(cursoBd.isPresent()){
            Usuario usuarioMsvc = usuarioClientRest.getUserMvsc(usuario.getId());
    
            CursoUsuario cursoUsuario = new CursoUsuario();
            cursoUsuario.setUsuarioId(usuarioMsvc.getId());
    
            Curso cursoToSave = cursoBd.get();
            cursoToSave.addCursoUsuario(cursoUsuario);
            cursoRepository.save(cursoToSave);

            return Optional.of(usuarioMsvc);
        }
        
        return Optional.empty();
    }
    
    @Override
    @Transactional
    public Optional<Usuario> crearUsuario(Usuario usuario, Long cursoId) {
        Optional<Curso> cursoBd = cursoRepository.findById(cursoId);
        
        if (cursoBd.isPresent()){
            Usuario newUserMvsc = usuarioClientRest.createUserMvsc(usuario);
            
            CursoUsuario cursoUsuario = new CursoUsuario();
            cursoUsuario.setUsuarioId(newUserMvsc.getId());
            
            Curso cursoToSave = cursoBd.get();
            cursoToSave.addCursoUsuario(cursoUsuario);
            cursoRepository.save(cursoToSave);
            
            return Optional.of(newUserMvsc);
        }
        
        return Optional.empty();
    }
    
    @Override
    @Transactional
    public Optional<Usuario> eliminarUsuarioDeCurso(Usuario usuario, Long cursoId) {
        Optional<Curso> cursoBd = cursoRepository.findById(cursoId);
        
        if(cursoBd.isPresent()){
            Usuario usuarioMsvc = usuarioClientRest.getUserMvsc(usuario.getId());
            
            CursoUsuario cursoUsuario = new CursoUsuario();
            cursoUsuario.setUsuarioId(usuarioMsvc.getId());
            
            Curso cursoToSave = cursoBd.get();
            cursoToSave.removeCursoUsario(cursoUsuario);
            cursoRepository.save(cursoToSave);
            return Optional.of(usuarioMsvc);
        }
        
        return Optional.empty();
    }
    
    @Override
    @Transactional
    public void eliminarUsuarioCursoPorId(Long id) {
        cursoRepository.eliminarUsuarioCursoPorId(id);
    }
}
