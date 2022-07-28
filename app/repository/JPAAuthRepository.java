package repository;

import beans.LoginBean;
import exceptions.AcessoNaoAutorizadoException;
import exceptions.DecryptException;
import exceptions.EncryptException;
import exceptions.UsuarioNaoEncontradoException;
import models.Instituicao;
import models.Usuario;
import org.h2.util.StringUtils;
import play.db.jpa.JPAApi;
import utils.Utils;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.CompletionException;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.supplyAsync;

public class JPAAuthRepository implements AuthRepository {

    public final UsuarioRepository usuarioRepository;
    public final InstituicaoRepository instituicaoRepository;
    public final JPAApi jpaApi;
    public final DatabaseExecutionContext executionContext;

    @Inject
    public JPAAuthRepository(JPAApi jpaApi,
                             DatabaseExecutionContext executionContext,
                             UsuarioRepository usuarioRepository,
                             InstituicaoRepository instituicaoRepository) {
        this.usuarioRepository = usuarioRepository;
        this.instituicaoRepository = instituicaoRepository;
        this.executionContext = executionContext;
        this.jpaApi = jpaApi;
    }

    public CompletionStage<LoginBean> login(LoginBean bean){
        return supplyAsync(()-> this.jpaApi.withTransaction(em -> {
            try {
                return doLogin(em, bean);
            }catch (Exception e){
                throw new CompletionException(e);
            }
        }), executionContext);
    }
    private LoginBean doLogin(EntityManager em, LoginBean bean) throws NoSuchAlgorithmException, UsuarioNaoEncontradoException, EncryptException, DecryptException, AcessoNaoAutorizadoException {
        Usuario usuario;
        Instituicao instituicao;

        String random = String.valueOf(System.currentTimeMillis());

        if (StringUtils.isNullOrEmpty(bean.tokenUsuario)){
            String senha = Utils.toMD5(bean.password);
            usuario = this.usuarioRepository.buscarPor(em, bean.email, senha);
            if (usuario == null){
                throw new UsuarioNaoEncontradoException();
            }
            bean.administrador = Boolean.TRUE.equals(usuario.administrador);
            bean.tokenUsuario = Utils.encrypt(usuario.id.toString());
            bean.authToken = bean.tokenUsuario;
            instituicao = usuario.instituicao;
            if (instituicao == null && !Boolean.TRUE.equals(usuario.administrador)){
                throw new UsuarioNaoEncontradoException();
            }
            if (instituicao != null){
                bean.tokenInstituicao = Utils.encrypt(instituicao.getId().toString());
            }
        }else{
            bean.administrador = Boolean.TRUE;
            String decrypted = Utils.decrypt(bean.tokenUsuario);
            usuario = usuarioRepository.findById(em, Long.valueOf(decrypted));
            if (usuario == null){
                throw new UsuarioNaoEncontradoException();
            }
            if (!Boolean.TRUE.equals(usuario.administrador)){
                throw new AcessoNaoAutorizadoException();
            }
            if (bean.instituicao != null){
                instituicao = instituicaoRepository.findById(em, bean.instituicao.id);
                if (instituicao != null){
                    bean.tokenInstituicao = Utils.encrypt(instituicao.getId().toString());
                }
            }
        }
        return bean;
    }
}
