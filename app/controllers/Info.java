package controllers;

import java.util.List;

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
		Boolean followable = !user.username.equals(session().get("username"));
		Boolean viewerFollowing = Follow.alreadyFollowing(
				session().get("username"), username);
		List<UserInfo> following = UserInfo.findUsers(
				Follow.findUsersFollowedBy(username));
		List<UserInfo> followers = UserInfo.findUsers(
				Follow.findFollowersOf(username));
		return ok(userinfo.render(user, editable, followable, viewerFollowing,
				following, followers));
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
	
	@Security.Authenticated(Secured.class)
	public static Result followUser(String username) {
		UserInfo user = UserInfo.findByUsername(username);
		if (user == null) {
			//TODO what's the best response to this?
			return redirect(routes.Application.index());
		}
		//if they try to follow themself
		else if (username.equals(session().get("username"))) {
			return redirect(routes.Info.viewUser(username));
		}
		//otherwise, we're good
		//delete or create, depending on current situation
		if (Follow.alreadyFollowing(session().get("username"), username)) {
			//then unfollow
			Follow.delete(session().get("username"), username);
		}
		else {
			//otherwise follow
			Follow.create(session().get("username"), username);
		}
		return redirect(routes.Info.viewUser(username));
	}
	
	///////////////
	// UserGroup //
	///////////////
	
	public static Result viewGroup(String id) {
		return ok(usergroup.render(UserGroup.findByID(id)));
	}

}
