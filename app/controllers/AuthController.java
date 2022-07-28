package controllers;

import beans.LoginBean;
import beans.SessaoBean;
import beans.UsuarioResource;
import controllers.handlers.AuthResourceHandler;
import controllers.handlers.UsuarioResourceHandler;
import exceptions.UsuarioNaoEncontradoException;
import models.Usuario;
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

    public final AuthResourceHandler handler;

    @Inject
    public AuthController(FormFactory formFactory, HttpExecutionContext ec, AuthResourceHandler handler) {
        super(formFactory, ec);
        this.handler = handler;
    }

    public CompletionStage<Result> autenticar(final Http.Request request) {
        LoginBean bean = formFactory.form(LoginBean.class).bindFromRequest(request).get();
        return handler
                .login(bean, request)
                .thenApplyAsync(loginBean -> ok(Json.toJson(loginBean)).withNewSession().addingToSession(request, bean.sessionValues), ec.current())
                .exceptionally((e) -> {
                    if (e.getCause() instanceof UsuarioNaoEncontradoException) {
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
