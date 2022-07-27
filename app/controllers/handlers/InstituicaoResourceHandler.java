package controllers.handlers;

import beans.InstituicaoFilter;
import beans.InstituicaoResource;
import controllers.routes;
import models.Instituicao;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Http;
import repository.InstituicaoRepository;
import search.SearchResult;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

public class InstituicaoResourceHandler {

    private final InstituicaoRepository repository;
    private final HttpExecutionContext ec;

    @Inject
    public InstituicaoResourceHandler(InstituicaoRepository repository, HttpExecutionContext ec) {
        this.repository = repository;
        this.ec = ec;
    }

    public CompletionStage<Optional<InstituicaoResource>> findById(String id) {
        return repository.get(Long.parseLong(id), Instituicao::toResource);
    }

    public CompletionStage<Optional<InstituicaoResource>> save(InstituicaoResource resource) {
        final Instituicao data = resource.toInstituicao();
        return repository.merge(data, Instituicao::toResource);
    }

    public CompletionStage<Void> remove(String id) {
        return repository.remove(Long.valueOf(id));
    }

    public CompletionStage<List<InstituicaoResource>> findAllByNome(String nome) {
        return repository.findAllByNome(nome);
    }

    public CompletionStage<SearchResult<InstituicaoResource>> filter(Http.Request request, InstituicaoFilter filter) {
        return repository.filter(filter, Instituicao::toResource).thenApplyAsync(result -> {
                    for (InstituicaoResource bean : result.data) {
                        bean.imagem = routes.Assets.at("images/avatar.svg").absoluteURL(request);
                    }
                    return result;
                }, ec.current()
        );
    }
}
