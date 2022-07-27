package controllers;

import beans.InstituicaoFilter;
import beans.InstituicaoResource;
import controllers.handlers.InstituicaoResourceHandler;
import play.data.DynamicForm;
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
import static play.mvc.Results.ok;

public class InstituicaoController {

    public final FormFactory formFactory;
    public final HttpExecutionContext ec;
    private final InstituicaoResourceHandler handler;

    @Inject
    public InstituicaoController(FormFactory formFactory, HttpExecutionContext ec, InstituicaoResourceHandler handler) {
        this.formFactory = formFactory;
        this.handler = handler;
        this.ec = ec;
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
        InstituicaoResource data = formFactory.form(InstituicaoResource.class).bindFromRequest(request).get();
        return handler.save(data)
                .thenApplyAsync(
                        optionalResource -> optionalResource.map(resource -> ok(toJson(resource))).orElseGet(Results::notFound), ec.current());
    }

    @AppSecurity.Authenticated(AuthSecurity.class)
    public CompletionStage<Result> remove(String id) {
        return handler
                .remove(id)
                .thenApplyAsync((val) -> ok(), ec.current());
    }

    @AppSecurity.Authenticated(AuthSecurity.class)
    public CompletionStage<Result> pesquisar(final Http.Request request) {
        DynamicForm dynamicForm = formFactory.form().bindFromRequest(request);
        String query = dynamicForm.get("query");
        return handler
                .findAllByNome(query)
                .thenApplyAsync(data -> ok(toJson(data)), ec.current());
    }

    @AppSecurity.Authenticated(AuthSecurity.class)
    public CompletionStage<Result> filter(final Http.Request request) {
        InstituicaoFilter filtro = formFactory.form(InstituicaoFilter.class).bindFromRequest(request).get();
        return handler
                .filter(request, filtro)
                .thenApplyAsync(result -> ok(toJson(result)), ec.current());
    }
}
