package controllers;

import play.data.FormFactory;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Controller;

public class AppController extends Controller {

    public final FormFactory formFactory;
    public final HttpExecutionContext ec;

    public AppController(FormFactory formFactory, HttpExecutionContext ec) {
        this.formFactory = formFactory;
        this.ec = ec;
    }
}
