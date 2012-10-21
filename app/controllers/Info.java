package controllers;

import play.*;
import play.mvc.*;
import play.data.*;

import models.*;
import play.mvc.Controller;

import views.html.*;

public class Info extends Controller {
	
	public static Result viewEvent(String id) {
		EventInfo event = EventInfo.findByID(id);
		Boolean creator = false;
		if(event.creatorUsername.equals(session().get("username"))){
		creator = true;
		}
		return ok(eventinfo.render(EventInfo.findByID(id),creator));
	}
	
	public static Result viewUser(String username) {
		return ok(userinfo.render(UserInfo.findByUsername(username)));
	}
	
	public static Result viewGroup(String id) {
		return ok(usergroup.render(UserGroup.findByID(id)));
	}

}
