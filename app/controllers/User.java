package controllers;

import java.util.Collection;

import play.data.validation.Valid;
import play.mvc.With;
import securesocial.provider.ProviderRegistry;
import securesocial.provider.SocialUser;
import service.UserService;
import controllers.securesocial.SecureSocial;
import controllers.securesocial.SecureSocialPublic;

@With(SecureSocialPublic.class)
public class User extends AbstractController {

    public static void myProfile() {
        isValidUser();
        SocialUser user = SecureSocial.getCurrentUser();
        models.User member = UserService.findUser(user.id);
        Boolean mine = Boolean.TRUE;
        render("@view", member, mine);
    }

    public static void view(Long userId) {
        models.User member = models.User.findById(userId);
        if (member != null) {
            Boolean mine = Boolean.FALSE;
            render(member, mine);
        }
        else {
            notFound();
        }
    }

    public static void edit() {
        isValidUser();
        Collection providers = ProviderRegistry.all();
        SocialUser user = SecureSocial.getCurrentUser();
        models.User member = UserService.findUser(user.id);
        session.put("member", member.id);
        render(providers, member);
    }

    public static void save(@Valid models.User member) {
        isValidUser();
        if (!validation.valid(member).ok) {
            validation.keep();
            params.flash();
            Collection providers = ProviderRegistry.all();
            render("@edit", providers, member);
        }
        validation.clear();
        member.save();
        edit();
    }
}
