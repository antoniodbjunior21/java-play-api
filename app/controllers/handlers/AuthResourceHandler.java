package controllers.handlers;

import beans.LoginBean;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Http;
import repository.AuthRepository;
import utils.Constantes;
import utils.Utils;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.concurrent.CompletionStage;

public class AuthResourceHandler {

    private final AuthRepository repository;
    private final HttpExecutionContext context;
    @Inject
    public AuthResourceHandler(AuthRepository repository, HttpExecutionContext context) {
        this.repository = repository;
        this.context = context;
    }

    public CompletionStage<LoginBean> login(LoginBean bean, Http.Request request) {
        request.session().get(Constantes.USUARIO_TOKEN).ifPresent(userToken -> bean.tokenUsuario = userToken);
        request.session().get(Constantes.USUARIO_TOKEN).ifPresent(userToken -> bean.authToken = userToken);
        request.session().get(Constantes.INSTITUICAO_TOKEN).ifPresent(instituicaoToken -> bean.tokenInstituicao = instituicaoToken);

        return this.repository.login(bean).thenApplyAsync(result -> {
            result.sessionValues = new HashMap<>();
            result.sessionValues.put(Constantes.USUARIO_TOKEN, result.tokenUsuario);
            if (Utils.isNotNullOrEmpty(result.tokenInstituicao)){
                result.sessionValues.put(Constantes.INSTITUICAO_TOKEN, result.tokenInstituicao);
            }
            return result;
        }, context.current());
    }
}
