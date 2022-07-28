package controllers;

import beans.SessaoBean;
import beans.UsuarioFilter;
import beans.UsuarioResource;
import controllers.handlers.UsuarioResourceHandler;
import models.Instituicao;
import play.data.FormFactory;
import play.libs.Json;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Results;
import repository.AuthRepository;
import security.AppSecurity;
import security.AuthSecurity;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

import static play.libs.Json.toJson;

public class UsuarioController extends AppController {

    public final AuthRepository authRepository;
    public final UsuarioResourceHandler handler;
    @Inject
    public UsuarioController(FormFactory formFactory, UsuarioResourceHandler handler, HttpExecutionContext ec, AuthRepository authRepository) {
        super(formFactory, ec);
        this.handler = handler;
        this.authRepository = authRepository;
    }

    @AppSecurity.Authenticated(AuthSecurity.class)
    public CompletionStage<Result> findById(String id) {
        return handler
                .findById(id)
                .thenApplyAsync(optionalResource -> optionalResource.map(resource -> ok(toJson(resource)))
                        .orElseGet(Results::notFound), ec.current());
    }

    @AppSecurity.Authenticated(AuthSecurity.class)
    public CompletionStage<Result> save(final Http.Request request) {
        UsuarioResource data = formFactory.form(UsuarioResource.class).bindFromRequest(request).get();
        Instituicao instituicao = AppSecurity.getInstituicaoBy(request);
        return handler.save(data, instituicao)
                .thenApplyAsync(
                        optionalResource -> optionalResource.map(resource -> ok(toJson(resource))).orElseGet(Results::notFound), ec.current());
    }

    @AppSecurity.Authenticated(AuthSecurity.class)
    public CompletionStage<Result> filter(final Http.Request request) {
        UsuarioFilter filtro = formFactory.form(UsuarioFilter.class).bindFromRequest(request).get();
        filtro.instituicao = AppSecurity.getInstituicaoBy(request);
        return handler
                .filter(request, filtro)
                .thenApplyAsync(result -> ok(toJson(result)), ec.current());
    }

    @AppSecurity.Authenticated(AuthSecurity.class)
    public CompletionStage<Result> remove(String id) {
        return handler
                .remove(id)
                .thenApplyAsync((val) -> ok(), ec.current());
    }

    @AppSecurity.Authenticated(AuthSecurity.class)
    public Result getLoggedUserInfo(final Http.Request request) {
        try {
            SessaoBean bean = AppSecurity.getSessionInfoBy(request);
            UsuarioResource usuarioResource = bean.usuario;
            usuarioResource.pic = handler.getAvatarPicture(usuarioResource.id, request);
            return ok(Json.toJson(bean.usuario));
        }catch (Exception e){
            return badRequest();
        }
    }
}
