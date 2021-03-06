package controllers;

import java.util.List;

import models.AchievementInfo;
import models.EventInfo;
import models.SessionInfo;
import models.UserGroup;
import models.UserInfo;
import models.ZipCodeInfo;
import play.*;
import play.data.*;
import play.data.validation.*;
import play.mvc.*;
import play.libs.*;

import views.html.*;

public class Application extends Controller {

	public static final String EMAIL_REGEX =
			"[a-zA-Z0-9.!#$%&'*+-/=?_`{}~|^]+@[a-zA-Z0-9.-]+\\.[A-Za-z]+";

	public static Result index() {
		return redirect(routes.Application.viewFeed());
	}

	public static Result about() {
		return ok(about.render());
	}

	public static Result contact(){
		return ok(contact.render());
	}

	public static class Message {

		public String message;
		public String name;
		public String email;

		public String validate() {
			//TODO validate this shiz
			return null;
		}

	}

	public static Result submitMessage(){

		Form<Message> messageForm =
				form(Message.class).bindFromRequest();
		if (messageForm.hasErrors()) {
			return badRequest(contact.render());
		}
		//otherwise we were successful
		Message message = messageForm.get();
		flash("success", "Message Sent");

		return ok(contact.render());
	}

	////////
	//Feed//
	////////

	public static Result viewFeed() {
		//first check if someone is logged in
		UserInfo viewer = SessionInfo.getLoggedInUser(session());
		String username = viewer == null ? null : viewer.username;
		if (username != null) {
			return Info.viewUserFeed(username);
		}
		//otherwise, tell them to login and give them a general feed
		flash("info","Sign in, or sign up, to get a personalized feed");
		List<UserInfo> newestUsers = UserInfo.getNewestUsers();
		List<EventInfo> newestEvents = EventInfo.getNewestEvents();
		return ok(feed.render(false,null,null,null,newestUsers,newestEvents));
	}

	/////////
	//Login//
	/////////

	public static class Login {

		public String username;
		public String password;

		public String validate() {
			if (!UserInfo.authenticate(username, password)) {
				return "Invalid username or password";
			}
			return null;
		}

	}

	public static Result login() {
		return ok(login.render(form(Login.class)));
	}

	/**
	 * Handle login form submission.
	 */
	public static Result authenticate() {
		Form<Login> loginForm = form(Login.class).bindFromRequest();
		if(loginForm.hasErrors()) {
			return badRequest(login.render(loginForm));
		} else {
			String username = loginForm.get().username;
			//set up their session
			session("username", username);
			String sessionKey = SessionInfo.create(username);
			session("key", sessionKey);
			//update their last login and login count
			UserInfo.updateInfoForLoginSuccess(username);
			return redirect(routes.Application.index());
		}
	}

	//////////
	//Logout//
	//////////

	/**
	 * Logout and clean the session.
	 */
	public static Result logout() {
		//clear their session info
		UserInfo viewer = SessionInfo.getLoggedInUser(session());
		String username = viewer == null ? "" : viewer.username;
		SessionInfo.deleteSessionInfo(username);
		session().clear();
		flash("success", "You've been logged out");
		return redirect(routes.Application.login());
	}

	////////////////////
	//Account Creation//
	////////////////////

	public static class Account {

		public String fullname;
		public String username;
		public String email;
		public String zipCode;
		public String password;

		private static String usernameRegex = "\\w+";

		public String validate() {
			if (!UserInfo.usernameAvailable(username)) {
				return "Username already taken";
			}
			else if (!UserInfo.emailAvailable(email)) {
				return "Account already created with that email";
			}
			else if (username == null || username.length() <=0 ||
					!username.matches(usernameRegex)) {
				return "Invalid username";
			}
			else if (email == null || !email.matches(EMAIL_REGEX)) {
				return "Invalid email";
			}
			else if (ZipCodeInfo.getValidatedZipCode(zipCode) == null) {
				return "Invalid zip code";
			}
			else if (password == null || password.length() <= 0) {
				return "Invalid password";
			}
			else if (fullname == null || fullname.length() <= 0) {
				return "Please enter your name";
			}
			return null;
		}

	}

	public static Result newAccount() {
		return ok(newaccount.render(form(Account.class)));
	}

	public static Result createAccount() {
		Form<Account> accountForm = form(Account.class).bindFromRequest();
		if (accountForm.hasErrors()) {
			return badRequest(newaccount.render(accountForm));
		}
		else {
			Account account = accountForm.get();
			UserInfo.create(account.fullname, account.username, account.email,
					account.zipCode, account.password);
			//also create their AchievementInfo
			AchievementInfo.create(account.username);
			flash("success", "Account created");
			return redirect(routes.Application.login());
		}
	}

	////////////////////
	//Group Creation//
	////////////////////

	public static class Group {

		public String name;
		public String description;

		public String validate() {
			//TODO validate this shiz
			return null;
		}

	}

	public static Result newGroup() {
		UserInfo user = SessionInfo.getLoggedInUser(session());
		if (user == null) {
			return redirect(routes.Application.index());
		}
		return ok(newgroup.render(form(Group.class)));
	}

	public static Result createGroup() {
		UserInfo user = SessionInfo.getLoggedInUser(session());
		if (user == null) {
			return redirect(routes.Application.index());
		}
		Form<Group> groupForm = form(Group.class).bindFromRequest();
		if (groupForm.hasErrors()) {
			return badRequest(newgroup.render(groupForm));
		}
		else {
			Group group = groupForm.get();
			UserGroup.create(user.username, group.name, group.description);
			flash("success", "Group created");
			return redirect(routes.Application.index());
		}
	}


	////////////////////
	//Event Creation//
	////////////////////

	public static class Event {

		public String name;
		public String description;
		public double distance;
		public String unit;
		public String pace;
		public String routeDescription;

		public String validate() {
			//TODO validate this shiz
			return null;
		}

	}

	public static Result newEvent() {
		UserInfo user = SessionInfo.getLoggedInUser(session());
		if (user == null) {
			return redirect(routes.Application.index());
		}
		return ok(newevent.render(form(Event.class)));
	}

	public static Result createEvent() {
		//the logged in user is creating the event
		UserInfo user = SessionInfo.getLoggedInUser(session());
		if (user == null) {
			return redirect(routes.Application.index());
		}
		Form<Event> eventForm = form(Event.class).bindFromRequest();
		if (eventForm.hasErrors()) {
			return badRequest(newevent.render(eventForm));
		}
		else {
			Event event = eventForm.get();

			EventInfo eventinfo = EventInfo.create(user.username, event.name,
					event.description, event.distance, event.unit,event.routeDescription, event.pace);
			flash("success", "Event created");

			return  Info.attendEvent(eventinfo.id);
		}
	}
	/*  
    public static Result editEvent(String id){
      	Form<Event> eventForm = form(Event.class).bindFromRequest();
    	if (eventForm.hasErrors()) {
    		return badRequest(newevent.render(eventForm));
    	}
    	else {
    		Event event = eventForm.get();	
    		EventInfo eventInfo = EventInfo.findByID(id);
    		if(eventInfo != null) {
    			eventInfo.description = event.description;
    			eventInfo.distance = event.distance;
    			eventInfo.name = event.name;
    			eventInfo.save();
    			flash("success", "Event updated");
    		}


    		return Info.viewEvent(eventInfo.id);

    	}

    }

	 */

	public static class Comment {

		public String username;
		public String comment;
		public long time;
		//id of the event being attended
		public String eventID;

		public String validate() {
			//TODO validate this shiz
			return null;
		}

	}

}
