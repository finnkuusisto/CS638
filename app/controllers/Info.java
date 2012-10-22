package controllers;

import controllers.Application.Account;
import play.*;
import play.mvc.*;
import play.data.*;

import models.*;
import play.mvc.Controller;

import views.html.*;

public class Info extends Controller {
	
	///////////////
	// EventInfo //
	///////////////
	
	public static Result viewEvent(String id) {
		EventInfo event = EventInfo.findByID(id);
		Boolean creator = false;
		if(event.creatorUsername.equals(session().get("username"))){
		creator = true;
		}
		return ok(eventinfo.render(EventInfo.findByID(id),creator));
	}
	
	//////////////
	// UserInfo //
	//////////////
	
	public static class UserInfoEdit {
		
		public String fullName;
		public String about;
		
		public UserInfoEdit() {}
		
		public UserInfoEdit(UserInfo info) {
			this.fullName = info.fullName;
			this.about = info.about;
		}
		
		public String validate() {
			if (fullName == null || fullName.length() <= 0) {
    			return "Please enter your full name";
    		}
    		return null;
		}
		
	}
	
	public static Result viewUser(String username) {
		UserInfo user = UserInfo.findByUsername(username);
		if (user == null) {
			//TODO make our own 404 perhaps?
			return notFound();
		}
		Boolean editable = user.username.equals(session().get("username"));
		return ok(userinfo.render(user, editable));
	}
	
	public static Result editUser(String username) {
		//security check
		UserInfo user = UserInfo.findByUsername(username);
		if (user == null) {
			//TODO make our own 404 perhaps?
			return notFound();
		}
		//TODO better security
		else if (!username.equals(session().get("username"))) {
			return Info.viewUser(username);
		}
		//fill out a form to start with
		UserInfoEdit edit = new UserInfoEdit(user);
		Form<UserInfoEdit> form = form(UserInfoEdit.class).fill(edit);
		return ok(edituserinfo.render(user, form));
	}
	
	public static Result submitUserEdit(String username) {
		//security check
		UserInfo user = UserInfo.findByUsername(username);
		if (user == null) {
			//TODO make our own 404 perhaps?
			return notFound();
		}
		//TODO better security
		else if (!username.equals(session().get("username"))) {
			return Info.viewUser(username);
		}
		//now do the edit based on the form content
    	Form<UserInfoEdit> userEditForm =
    			form(UserInfoEdit.class).bindFromRequest();
    	if (userEditForm.hasErrors()) {
    		return badRequest(edituserinfo.render(user, userEditForm));
    	}
    	//otherwise we were successful
		UserInfoEdit userEdit = userEditForm.get();
		user.fullName = userEdit.fullName;
		user.about = userEdit.about;
		user.save();
		flash("success", "Changes saved");
		return ok(edituserinfo.render(user, userEditForm));
	}
	
	///////////////
	// UserGroup //
	///////////////
	
	public static Result viewGroup(String id) {
		return ok(usergroup.render(UserGroup.findByID(id)));
	}

}
