package controllers;

import java.util.List;

import controllers.Application.Account;
import controllers.Application.Event;
import extra.Unit;
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
	
    public static class CommentSubmit {
    	
    	public String username;
    	public String comment;
    	
    	public String validate() {
    		//TODO validate this shiz
    		return null;
    	}
    	
    }
	
	public static Result viewEvent(String id) {
		EventInfo event = EventInfo.findByID(id);
		Boolean creator = false;
		if(event.creatorUsername.equals(session().get("username"))){
		creator = true;
		}
		Boolean isAttending = false;
		if(Attend.userIsAttending(session().get("username"), id)){
			isAttending = true;
		}
		
		List<UserInfo> attending = UserInfo.findUsers(Attend.findUsersAttending(id));
		CommentSubmit comment =  new CommentSubmit();
		Form<CommentSubmit> commentForm = form(CommentSubmit.class).fill(comment);
		
		List<Comment> comments = Comment.findCommentsForEvent(id);
		
		return ok(eventinfo.render(commentForm,event,creator, isAttending, attending, comments));
	}
	
	  public static Result editEvent(String id){
		    EventInfo event = EventInfo.findByID(id);
	     
	    	// fill out form
    		EventInfoEdit editEvent =  new EventInfoEdit(event);
    		Form<EventInfoEdit> form = form(EventInfoEdit.class).fill(editEvent);
    		return ok(editeventinfo.render(event, form));
	    }
	  
		public static Result submitComment(String id) {
			  EventInfo event = EventInfo.findByID(id);
				if (event == null) {
					//TODO make our own 404 perhaps?
					return notFound();
				}
			
				//now do the edit based on the form content
		    	Form<CommentSubmit> commentForm =
		    			form(CommentSubmit.class).bindFromRequest();
		
		    	CommentSubmit comment = commentForm.get();
		    	
		    	Comment.create(session().get("username"), comment.comment, id);
		    	
		    	
		    	
			return viewEvent(id);
		
		}
	  
	  
		public static Result submitEventEdit(String id) {
			  EventInfo event = EventInfo.findByID(id);
			if (event == null) {
				//TODO make our own 404 perhaps?
				return notFound();
			}
		
			//now do the edit based on the form content
	    	Form<EventInfoEdit> eventEditForm =
	    			form(EventInfoEdit.class).bindFromRequest();
	    	if (eventEditForm.hasErrors()) {
	    		return badRequest(editeventinfo.render(event, eventEditForm));
	    	}
	    	//otherwise we were successful
	    	EventInfoEdit eventEdit = eventEditForm.get();
			event.name = eventEdit.name;
			event.description = eventEdit.description;
			if(eventEdit.unit.equals("Miles")){
				event.unit = Unit.miles;
			} else if(eventEdit.unit.equals("Meters")){
				event.unit = Unit.meters;
			} else if(eventEdit.unit.equals("Kilometers")) {
				event.unit = Unit.kilometers;		
			}	
			event.save();
			flash("success", "Changes saved");
			return redirect(routes.Info.viewEvent(id));
		}
	  
		@Security.Authenticated(Secured.class)
		public static Result attendEvent(String eventID) {
			String username = session().get("username");
			UserInfo user = UserInfo.findByUsername(username);
			if (user == null) {
				//TODO what's the best response to this?
				return redirect(routes.Application.index());
			}
			
			// if already attending then unattend
			if(Attend.userIsAttending(username, eventID))
			{
				Attend.delete(username, eventID);
			}
			else {
				// else attend event
				Attend.create(username, eventID);
			}
			return redirect(routes.Info.viewEvent(eventID));
		}

	 
		
	  public static class EventInfoEdit {
			
		 	public String name;
	    	public String description;
	    	public double distance;
	    	public String unit;
	    	public String pace;
	    	public String routeDescription;
	    	public String miles = "unchecked";
	    	public String kilometers = "unchecked";
	    	public String meters = "unchecked";
		
	    	public EventInfoEdit() {
	    	this.name = " ";
	    	this.description = " ";
	    	this.distance = 0;
	    	
	    	}
			public EventInfoEdit(Event info) {
				this.name = info.name;
				this.description = info.description;
				this.distance = info.distance;
				this.unit = info.unit;
				this.pace = info.pace;
				this.routeDescription = info.routeDescription;
				
			}
			public EventInfoEdit(EventInfo info) {
				this.name = info.name;
				this.description = info.description;
				this.distance = info.distance;
				this.unit = info.unit.toString();
				this.routeDescription = info.routeDescription;
				this.pace = info.pace;
						
				if(this.unit.equals("miles")){
					miles = "checked";
				}
				else if(this.unit.equals("kilometers")){
					kilometers = "checked";
					
				} else if(this.unit.equals("meters")){
					meters = "checked";
				}
				
				
				
				
			}
			public String validate() {
				//TODO validate
	    		return null;
			}
			
		}
	  
	  
	
	//////////////
	// UserInfo //
	//////////////
	
	public static class UserInfoEdit {
		
		public String fullName;
		public String zipCode;
		public String url;
		public String about;
		
		public UserInfoEdit() {}
		
		public UserInfoEdit(UserInfo info) {
			this.fullName = info.fullName;
			this.zipCode = info.zipCode;
			this.url = info.url;
			this.about = info.about;
		}
		
		public String validate() {
			zipCode = ZipCodeInfo.getValidatedZipCode(zipCode);
			if (fullName == null || fullName.length() <= 0) {
    			return "Please enter your full name";
    		}
			else if (zipCode == null) {
				return "Invalid zip code";
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
		//TODO better security
		Boolean editable = user.username.equals(session().get("username"));
		Boolean followable = !user.username.equals(session().get("username"));
		Boolean viewerFollowing = Follow.alreadyFollowing(
				session().get("username"), username);
		int numFollowing = Follow.numUsersFollowedBy(username);
		int numFollowers = Follow.numFollowersOf(username);
		int numAttending = Attend.numEventsAttendedBy(username);
		return ok(userinfo.render(user, editable, followable, viewerFollowing,
				numFollowing, numFollowers, numAttending));
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
		//check the url
		if (userEdit.url != null) {
			user.url = userEdit.url;
			//tack http:// on it if needed, and if it wasn't just cleared
			if (user.url.length() > 0 && !user.url.startsWith("http://") &&
					!user.url.startsWith("https://")) {
				user.url = "http://" + user.url;
			}
		}
		user.zipCode = ZipCodeInfo.getValidatedZipCode(userEdit.zipCode);
		user.about = userEdit.about;
		user.save();
		flash("success", "Changes saved");
		return Info.editUser(username);
	}
	
	public static Result viewUserFollowInfo(String username) {
		UserInfo user = UserInfo.findByUsername(username);
		if (user == null) {
			//TODO make our own 404 perhaps?
			return notFound();
		}
		//TODO better security
		Boolean editable = user.username.equals(session().get("username"));
		List<UserInfo> following = UserInfo.findUsers(
				Follow.findUsersFollowedBy(username));
		List<UserInfo> followers = UserInfo.findUsers(
				Follow.findFollowersOf(username));
		return ok(userfollowinfo.render(user, editable, following, followers));
	}
	
	public static Result viewUserAttendInfo(String username) {
		UserInfo user = UserInfo.findByUsername(username);
		if (user == null) {
			//TODO make our own 404 perhaps?
			return notFound();
		}
		//TODO better security
		Boolean editable = user.username.equals(session().get("username"));
		List<EventInfo> attending = EventInfo.findEvents(
				Attend.findEventsAttendedBy(username));
		return ok(userattendinfo.render(user, editable, attending));
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
