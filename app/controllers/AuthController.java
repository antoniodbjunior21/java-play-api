package controllers;

import beans.LoginBean;
import beans.SessaoBean;
import exceptions.UsuarioNaoEncontradoException;
import play.data.FormFactory;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Http;
import play.mvc.Result;
import repository.AuthRepository;
import repository.UsuarioRepository;
import security.AppSecurity;
import security.AuthSecurity;
import utils.Constantes;
import utils.Utils;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletionStage;

public class AuthController extends AppController {

    public final UsuarioRepository usuarioRepository;
    public final AuthRepository authRepository;

    @Inject
    public AuthController(FormFactory formFactory, UsuarioRepository usuarioRepository, HttpExecutionContext ec, AuthRepository authRepository) {
        super(formFactory, ec);
        this.usuarioRepository = usuarioRepository;
        this.authRepository = authRepository;
    }

    public CompletionStage<Result> autenticar(final Http.Request request) {
        LoginBean bean = formFactory.form(LoginBean.class).bindFromRequest(request).get();

        request.session().get(Constantes.USUARIO_TOKEN).ifPresent(s -> bean.tokenUsuario = s);
        request.session().get(Constantes.INSTITUICAO_TOKEN).ifPresent(s -> bean.tokenInstituicao = s);

        return authRepository
                .login(bean)
                .thenApplyAsync(loginBean -> {
                            Map<String, String> newValues = new HashMap<>();
                            newValues.put(Constantes.USUARIO_TOKEN, loginBean.tokenUsuario);
                            if (Utils.isNotNullOrEmpty(loginBean.tokenInstituicao)){
                                newValues.put(Constantes.INSTITUICAO_TOKEN, loginBean.tokenInstituicao);
                            }
                            return ok(Json.toJson(loginBean)).withNewSession().addingToSession(request, newValues);
                        }
                        , ec.current()).exceptionally((e) -> {
                    if (e instanceof UsuarioNaoEncontradoException) {
                        return badRequest(UsuarioNaoEncontradoException.class.getSimpleName().toUpperCase());
                    } else {
                        e.printStackTrace();
                        return badRequest();
                    }
                });
    }

    @AppSecurity.Authenticated(AuthSecurity.class)
    public Result buscarSessaoAtual(final Http.Request request) {
        try {
            SessaoBean bean = AppSecurity.getSessionInfoBy(request);
            return ok(Json.toJson(bean));
        }catch (Exception e){
            return badRequest();
        }
    }

    @AppSecurity.Authenticated(AuthSecurity.class)
    public Result logout(final Http.Request request) {
        return ok().withNewSession();
    }


}
