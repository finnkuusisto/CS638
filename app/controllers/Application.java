package controllers;

import models.EventInfo;
import models.UserInfo;
import play.*;
import play.data.*;
import play.data.validation.*;
import play.mvc.*;

import views.html.*;

public class Application extends Controller {
	
	public static Result index() {
		return redirect(routes.Lists.index());
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
            session("username", loginForm.get().username);
            return redirect(routes.Lists.index());
        }
    }
    
    //////////
    //Logout//
    //////////
    
    /**
     * Logout and clean the session.
     */
    public static Result logout() {
        session().clear();
        flash("success", "You've been logged out");
        return redirect(routes.Application.login());
    }
    
    ////////////////////
    //Account Creation//
    ////////////////////
    
    public static class Account {
    	
    	public String username;
    	public String email;
    	public String password;
    	
    	public String validate() {
    		if (!UserInfo.usernameAvailable(username)) {
    			return "Username already taken";
    		}
    		//TODO validate email
    		else if (!UserInfo.emailAvailable(email)) {
    			return "Account already created with that email";
    		}
    		//TODO validate password
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
    		UserInfo.create(account.username, account.email, account.password);
    		flash("success", "Account created");
    		return redirect(routes.Application.login());
    	}
    }
    
    
    ////////////////////
    //Event Creation//
    ////////////////////
    
    public static class Event {
    	
    	public String name;
    	public String description;
    	public double distance;
    	
    	public String validate() {
    		return null;
    	}
    	
    }
    
    public static Result newEvent() {
    	return ok(newevent.render(form(Event.class)));
    }
    
    public static Result createEvent() {
    	Form<Event> eventForm = form(Event.class).bindFromRequest();
    	if (eventForm.hasErrors()) {
    		return badRequest(newevent.render(eventForm));
    	}
    	else {
    		Event event = eventForm.get();
    		EventInfo.create(session().get("username"),event.name, event.description, event.distance);
    		flash("success", "Event created");
    		return redirect(routes.Application.index());
    	}
    }

}