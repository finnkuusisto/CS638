package controllers;

import play.*;
import play.mvc.*;
import play.data.*;

import models.*;
import play.mvc.Controller;

import views.html.*;

@Security.Authenticated(Secured.class)
public class Lists extends Controller {
	
	public static Result index() {
		return redirect(routes.Lists.users());
	}
	
	public  static Result users() {
		return ok(userlist.render(UserInfo.findAll()));
	}
	
	public  static Result groups() {
		return ok(grouplist.render(UserGroup.findAll()));
	}
	
	public  static Result events() {
		return ok(eventlist.render(EventInfo.findAll()));
	}

}
