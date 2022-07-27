package security;
/*
 * Copyright (C) Lightbend Inc. <https://www.lightbend.com>
 */

import beans.SessaoBean;
import models.Instituicao;
import models.Usuario;
import play.db.jpa.JPAApi;
import play.libs.typedmap.TypedMap;
import play.mvc.*;
import play.inject.Injector;
import play.libs.typedmap.TypedKey;
import play.mvc.Http.Request;
import utils.Constantes;
import utils.Utils;

import javax.inject.Inject;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

/**
 * Defines several security helpers.
 */

public class AppSecurity {

    public static final TypedKey<String> USERNAME = TypedKey.create("username");
    public static final TypedKey<Usuario> USUARIO = TypedKey.create("usuario");
    public static final TypedKey<Instituicao> INSTITUICAO = TypedKey.create("instituicao");
    public static final TypedKey<SessaoBean> SESSAO = TypedKey.create("sessao");

    public static String getUsernameBy(Request request){
        return request.attrs().get(USERNAME);
    }
    public static SessaoBean getSessionInfoBy(Request request){
        return request.attrs().get(AppSecurity.SESSAO);
    }
    public static Usuario getUsuarioBy(Request request){
        return request.attrs().get(AppSecurity.USUARIO);
    }
    public static Instituicao getInstituicaoBy(Request request){
        return request.attrs().get(AppSecurity.INSTITUICAO);
    }
    /**
     * Wraps the annotated action in an {@link AuthenticatedAction}.
     */
    @With(AuthenticatedAction.class)
    @Target({ElementType.TYPE, ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Authenticated {
        Class<? extends Authenticator> value() default Authenticator.class;
    }

    /**
     * Wraps another action, allowing only authenticated HTTP requests.
     *
     * <p>The user name is retrieved from the session cookie, and added to the HTTP request's <code>
     * username</code> attribute.
     */
    public static class AuthenticatedAction extends Action<Authenticated> {

        private final Function<Authenticated, Authenticator> configurator;

        @Inject
        private JPAApi jpaApi;

        @Inject
        public AuthenticatedAction(Injector injector) {
            this(authenticated -> injector.instanceOf(authenticated.value()));
        }

        public AuthenticatedAction(Authenticator authenticator) {
            this(authenticated -> authenticator);
        }

        public AuthenticatedAction(Function<Authenticated, Authenticator> configurator) {
            this.configurator = configurator;
        }

        public CompletionStage<Result> call(final Request req) {
            Authenticator authenticator = configurator.apply(configuration);
            Usuario usuario;
            Instituicao instituicao;

            Optional<String> usuarioToken = req.session().get(Constantes.USUARIO_TOKEN);
            Optional<String> instituicaoToken = req.session().get(Constantes.INSTITUICAO_TOKEN);

            TypedMap typedMap = req.attrs();
            SessaoBean sessaoBean = new SessaoBean();

            if (usuarioToken.isPresent()){
                try {
                    String token = usuarioToken.get();
                    if (Utils.isNotNullOrEmpty(token)){
                        Long id = Long.valueOf(Utils.decrypt(token));
                        usuario = this.jpaApi.withTransaction(em -> {
                            Usuario usuarioLogado = em.find(Usuario.class, id);
                            sessaoBean.usuario = usuarioLogado.toResource();
                            return usuarioLogado;
                        });
                        typedMap = typedMap.put(USUARIO, usuario);
                    }
                }catch (Exception exception){
                    exception.printStackTrace();
                }
            }
            if (instituicaoToken.isPresent()){
                try {
                    String token = instituicaoToken.get();
                    if (Utils.isNotNullOrEmpty(token)){
                        Long id = Long.valueOf(Utils.decrypt(token));
                        instituicao = this.jpaApi.withTransaction(em -> {
                            Instituicao instituicaoLogada = em.find(Instituicao.class, id);
                            sessaoBean.instituicao = instituicaoLogada.toResource();
                            return instituicaoLogada;
                        });
                        typedMap = typedMap.put(INSTITUICAO, instituicao);
                    }
                }catch (Exception exception){
                    exception.printStackTrace();
                }
            }

            typedMap = typedMap.put(SESSAO, sessaoBean);

            TypedMap finalTypedMap = typedMap;
            return authenticator
                    .getUsername(req)
                    .map(username -> {
                        TypedMap params = finalTypedMap.put(USERNAME, username);
                        return delegate.call(req.withAttrs(params));
                    })
                    .orElseGet(() -> CompletableFuture.completedFuture(authenticator.onUnauthorized(req)));
        }
    }

    /**
     * Handles authentication.
     */
    public static class Authenticator extends Results {

        /**
         * Retrieves the username from the HTTP request; the default is to read from the session cookie.
         *
         * @param req the current request
         * @return the username if the user is authenticated.
         */
        public Optional<String> getUsername(Request req) {
            return req.session().get("username");
        }

        /**
         * Generates an alternative result if the user is not authenticated; the default a simple '401
         * Not Authorized' page.
         *
         * @param req the current request
         * @return a <code>401 Not Authorized</code> result
         */
        public Result onUnauthorized(Request req) {
            return unauthorized(views.html.defaultpages.unauthorized.render(req.asScala()));
        }
    }
}
