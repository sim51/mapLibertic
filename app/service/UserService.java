package service;

import java.util.List;

import models.User;
import models.UserAccount;
import play.cache.Cache;
import play.libs.Codec;
import play.mvc.Scope.Session;
import securesocial.provider.SocialUser;
import securesocial.provider.UserId;

public class UserService implements securesocial.provider.UserService.Service {

    public static User findUser(UserId id) {
        List<User> users = User.find("SELECT u FROM User u JOIN u.accounts a WHERE a.userId = ? AND a.provider = ?",
                id.id, id.provider.toString()).fetch(1);
        if (users.size() > 0) {
            return users.get(0);
        }
        else {
            return null;
        }
    }

    @Override
    public SocialUser find(UserId id) {
        User user = this.findUser(id);
        if (user != null) {
            return user.toUserSocial();
        }
        else {
            return null;
        }
    }

    @Override
    public void save(SocialUser user) {
        User userDb = findUser(user.id);
        if (userDb == null) {
            if (Session.current().get("member") != null) {
                Long id = Long.valueOf(Session.current().get("member"));
                userDb = User.findById(id);
                UserAccount account = new UserAccount();
                account.userId = user.id.id;
                account.provider = user.id.provider.toString();
                account.save();
                userDb.accounts.add(account);
            }
            else {
                userDb = User.fromUserSocial(user);
                for (UserAccount account : userDb.accounts) {
                    account.save();
                }
            }
            userDb.save();
        }
    }

    @Override
    public String createActivation(SocialUser user) {
        final String uuid = Codec.UUID();
        Cache.add(uuid, user, "24h");
        return uuid;
    }

    @Override
    public boolean activate(String uuid) {
        SocialUser socialUser = (SocialUser) Cache.get(uuid);
        User user = this.findUser(socialUser.id);
        user.isEmailVerified = true;
        user.save();
        Cache.delete(uuid);
        return true;
    }

    @Override
    public void deletePendingActivations() {
        List<User> users = User.find("SELECT user FROM User u WHERE u.isEmailVerified = true").fetch();
        for (User user : users) {
            user.delete();
        }
    }

}
