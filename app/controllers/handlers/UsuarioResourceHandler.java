package controllers.handlers;

import beans.UsuarioFilter;
import beans.UsuarioResource;
import controllers.routes;
import models.Instituicao;
import models.Usuario;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Http;
import repository.UsuarioRepository;
import search.SearchResult;

import javax.inject.Inject;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

public class UsuarioResourceHandler {

    private final UsuarioRepository repository;
    private final HttpExecutionContext ec;

    @Inject
    public UsuarioResourceHandler(UsuarioRepository repository, HttpExecutionContext ec) {
        this.repository = repository;
        this.ec = ec;
    }

    public CompletionStage<Optional<UsuarioResource>> findById(String id) {
        return repository.get(Long.parseLong(id), Usuario::toResource);
    }

    public CompletionStage<Optional<UsuarioResource>> save(UsuarioResource resource, Instituicao instituicao) {
        final Usuario usuario = new Usuario().fromResource(resource, instituicao);
        return repository.merge(usuario, Usuario::toResource);
    }

    public CompletionStage<Void> remove(String id) {
        return repository.remove(Long.valueOf(id));
    }

    public CompletionStage<SearchResult<UsuarioResource>> filter(Http.Request request, UsuarioFilter filter) {
        return repository.filter(filter, Usuario::toResource).thenApplyAsync(result -> {
                    for (UsuarioResource bean : result.data) {
                        bean.imagem = routes.Assets.at("images/avatar.svg").absoluteURL(request);
                    }
                    return result;
                }, ec.current()
        );
    }
}
