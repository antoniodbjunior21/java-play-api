package repository;

import models.Usuario;
import play.db.jpa.JPAApi;
import utils.Utils;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;
import java.util.concurrent.CompletionException;

public class JPAUsuarioRepository extends JPABaseRepository<Usuario> implements UsuarioRepository {

    @Inject
    public JPAUsuarioRepository(JPAApi jpaApi, DatabaseExecutionContext executionContext) {
        super(jpaApi, executionContext, Usuario.class);
    }

    @Override
    public Optional<Usuario> merge(EntityManager em, Usuario usuario) {
        try {
            boolean ehNovo = usuario.id == null;
            if (ehNovo) {
                usuario.senha = Utils.toMD5(usuario.senha);
            }else{
                Usuario usuarioSalvo = findById(em, usuario.id);
                usuario.senha = usuarioSalvo.senha;
                usuario.administrador = Boolean.TRUE.equals(usuarioSalvo.administrador);
            }
            return super.merge(em, usuario);
        } catch (NoSuchAlgorithmException e) {
            throw new CompletionException(e);
        }
    }

    public Usuario buscarPor(EntityManager em, String email, String senha){
        try {
            Query query = em.createQuery("SELECT u FROM Usuario u WHERE u.email = :email AND u.senha = :senha ");
            query.setParameter("email", email.trim());
            query.setParameter("senha", senha);
            return (Usuario) query.getSingleResult();
        }catch (NoResultException e){
            return null;
        }
    }

}
