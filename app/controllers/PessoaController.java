package controllers;

import beans.PessoaFilter;
import beans.PessoaResource;
import controllers.handlers.PessoaResourceHandler;
import models.Instituicao;
import play.data.FormFactory;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Results;
import security.AppSecurity;
import security.AuthSecurity;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

import static play.libs.Json.toJson;

public class PessoaController extends AppController {

    public final PessoaResourceHandler handler;

    @Inject
    public PessoaController(FormFactory formFactory, PessoaResourceHandler handler, HttpExecutionContext ec) {
        super(formFactory, ec);
        this.handler = handler;
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
        PessoaResource data = formFactory.form(PessoaResource.class).bindFromRequest(request).get();
        Instituicao instituicao = AppSecurity.getInstituicaoBy(request);
        return handler.save(data, instituicao)
                .thenApplyAsync(
                optionalResource -> optionalResource.map(resource -> ok(toJson(resource))).orElseGet(Results::notFound), ec.current());
    }

    @AppSecurity.Authenticated(AuthSecurity.class)
    public CompletionStage<Result> filter(final Http.Request request) {
        PessoaFilter filtro = formFactory.form(PessoaFilter.class).bindFromRequest(request).get();
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

}
