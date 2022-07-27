package security;

import play.mvc.Http;
import play.mvc.Result;
import utils.Constantes;

import java.util.Optional;


public class AuthSecurity extends AppSecurity.Authenticator {

    @Override
    public Optional<String> getUsername(Http.Request req) {
        return req.session().get(Constantes.USUARIO_TOKEN);
    }

    @Override
    public Result onUnauthorized(Http.Request req) {
        System.out.println("unauthorized");
        return badRequest();
    }
}
