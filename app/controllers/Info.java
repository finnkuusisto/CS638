package controllers;

import play.*;
import play.mvc.*;
import play.data.*;

import models.*;
import play.mvc.Controller;

import views.html.*;

public class Info extends Controller {
	
	public static Result viewEvent(String id) {
		return ok(eventinfo.render(EventInfo.findByID(id)));
	}
	
	public static Result viewUser(String username) {
		return ok(userinfo.render(UserInfo.findByUsername(username)));
	}
	
	public static Result viewGroup(String id) {
		return ok(usergroup.render(UserGroup.findByID(id)));
	}

}
