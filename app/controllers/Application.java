package controllers;

import java.util.regex.Pattern;

import models.EventInfo;
import models.UserGroup;
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
    	
    	private static String emailRegex =
    			"[a-zA-Z0-9.!#$%&'*+-/=?_`{}~|^]+@[a-zA-Z0-9.-]+\\.[A-Za-z]+";
    	
    	public String validate() {
    		if (!UserInfo.usernameAvailable(username)) {
    			return "Username already taken";
    		}
    		else if (!UserInfo.emailAvailable(email)) {
    			return "Account already created with that email";
    		}
    		else if (email == null || !email.matches(emailRegex)) {
    			return "Invalid email";
    		}
    		else if (password == null || password.length() <= 0) {
    			return "Invalid password";
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
    		UserInfo.create(account.username, account.email, account.password);
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
    	return ok(newgroup.render(form(Group.class)));
    }
    
    public static Result createGroup() {
    	Form<Group> groupForm = form(Group.class).bindFromRequest();
    	if (groupForm.hasErrors()) {
    		return badRequest(newgroup.render(groupForm));
    	}
    	else {
    		Group group = groupForm.get();
    		UserGroup.create(session().get("username"), group.name,
    				group.description);
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
    	
    	public String validate() {
    		//TODO validate this shiz
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
    		EventInfo.create(session().get("username"), event.name,
    				event.description, event.distance);
    		flash("success", "Event created");
    		return redirect(routes.Application.index());
    	}
    }

}