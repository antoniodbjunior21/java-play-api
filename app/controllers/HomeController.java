package controllers;

import play.data.FormFactory;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Http;
import play.mvc.Result;

import javax.inject.Inject;

import static play.mvc.Results.ok;
import static play.mvc.Results.unauthorized;

public class HomeController {

    public final FormFactory formFactory;
    public final HttpExecutionContext ec;

    @Inject
    public HomeController(FormFactory formFactory, HttpExecutionContext ec) {
        this.formFactory = formFactory;
        this.ec = ec;
    }

    public Result index(final Http.Request request) {
        return ok(views.html.index.render(request))
                .addingToSession(request, "connected", "user@gmail.com");

    }

    public Result sessionTest(final Http.Request request) {
        return request
                .session()
                .get("connected")
                .map(user -> ok("Hello " + user))
                .orElseGet(() -> unauthorized("Oops, you are not connected"));
    }
}
