package repository;

import com.google.inject.ImplementedBy;
import models.Usuario;

import javax.persistence.EntityManager;

@ImplementedBy(JPAUsuarioRepository.class)
public interface UsuarioRepository extends BaseRepository<Usuario>{
    Usuario buscarPor(EntityManager em, String email, String senha);
}
