package controllers;

import play.*;
import play.mvc.*;
import play.mvc.Http.*;

import models.*;

public class Secured extends Security.Authenticator {
    
    @Override
    public String getUsername(Context ctx) {
    	UserInfo loggedIn = SessionInfo.getLoggedInUser(ctx.session());
    	if (loggedIn == null) {
    		return null;
    	}
    	return loggedIn.username;
    }
    
    @Override
    public Result onUnauthorized(Context ctx) {
        return redirect(routes.Application.login());
    }
    
}