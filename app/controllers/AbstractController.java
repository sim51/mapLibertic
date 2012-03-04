package controllers;

import play.Logger;
import play.mvc.Before;
import play.mvc.Controller;
import securesocial.provider.SocialUser;
import service.UserService;
import controllers.securesocial.SecureSocial;

public class AbstractController extends Controller {

    private static final String ROOT         = "/";
    private static final String ORIGINAL_URL = "originalUrl";
    private static final String GET          = "GET";

    protected static void isValidUser() {
        SocialUser user = SecureSocial.getCurrentUser();
        if (user == null) {
            final String originalUrl = request.method.equals(GET) ? request.url : ROOT;
            flash.put(ORIGINAL_URL, originalUrl);
            Logger.debug("Redirect user to login page");
            SecureSocial.login();
        }
        else {
            Logger.debug("User is " + user.displayName);
        }
    }

    protected static void isAdminUser() {
        isValidUser();
        SocialUser user = SecureSocial.getCurrentUser();
        models.User member = UserService.findUser(user.id);
        if (member == null || member.isAdmin == null || !member.isAdmin) {
            forbidden();
        }
    }

    @Before
    public static void setAdminRight() {
        Boolean isAdmin = Boolean.FALSE;
        SocialUser user = SecureSocial.getCurrentUser();
        if (user != null && user.id != null) {
            models.User member = UserService.findUser(user.id);
            response.setCookie("nocache", "nocache");
            if (member != null) {
                isAdmin = member.isAdmin;
            }
        }
        renderArgs.put("isAdmin", isAdmin);
    }

    public static Boolean hasAdminRight() {
        Boolean isAdmin = Boolean.FALSE;
        SocialUser user = SecureSocial.getCurrentUser();
        if (user != null && user.id != null) {
            models.User member = UserService.findUser(user.id);
            if (member != null) {
                isAdmin = member.isAdmin;
            }
        }
        return isAdmin;
    }
}
