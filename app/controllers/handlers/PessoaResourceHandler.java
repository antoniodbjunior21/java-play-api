package controllers.handlers;

import beans.PessoaFilter;
import beans.PessoaResource;
import controllers.routes;
import models.Instituicao;
import models.Pessoa;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Http;
import repository.PessoaRepository;
import search.SearchResult;

import javax.inject.Inject;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

public class PessoaResourceHandler {

    private final PessoaRepository repository;
    private final HttpExecutionContext ec;

    @Inject
    public PessoaResourceHandler(PessoaRepository repository, HttpExecutionContext ec) {
        this.repository = repository;
        this.ec = ec;
    }

    public CompletionStage<Optional<PessoaResource>> findById(String id) {
        return repository.get(Long.parseLong(id), Pessoa::toResource);
    }

    public CompletionStage<Optional<PessoaResource>> save(PessoaResource resource, Instituicao instituicao) {
        final Pessoa pessoa = new Pessoa().fromResource(resource, instituicao);
        return repository.merge(pessoa, Pessoa::toResource);
    }

    public CompletionStage<Void> remove(String id) {
        return repository.remove(Long.valueOf(id));
    }

    public CompletionStage<SearchResult<PessoaResource>> filter(Http.Request request, PessoaFilter filter) {
        return repository.filter(filter, Pessoa::toResource).thenApplyAsync(result -> {
                    for (PessoaResource bean : result.data) {
                        bean.imagem = routes.Assets.at("images/avatar.svg").absoluteURL(request);
                    }
                    return result;
                }, ec.current()
        );
    }
}
