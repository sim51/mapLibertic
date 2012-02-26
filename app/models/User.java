package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import play.data.validation.Required;
import play.data.validation.URL;
import play.db.jpa.Model;
import securesocial.provider.ProviderType;
import securesocial.provider.SocialUser;
import securesocial.provider.UserId;

@Entity
@Table(name = "member")
public class User extends Model {

    /**
     * The user full name.
     */
    @Required
    public String            displayName;

    /**
     * The user's email
     */
    public String            email;

    /**
     * The user's site
     */
    @URL
    public String            site;

    /**
     * A URL pointing to an avatar
     */
    public String            avatarUrl;

    /**
     * Is an admin ?
     */
    public Boolean           isAdmin;

    /**
     * The OAuth1 token (available when authMethod is OAUTH1 or OPENID_OAUTH_HYBRID)
     */
    public String            token;

    /**
     * The OAuth1 secret (available when authMethod is OAUTH1 or OPENID_OAUTH_HYBRID)
     */
    public String            secret;

    /**
     * The OAuth2 access token (available when authMethod is OAUTH2)
     */
    public String            accessToken;

    /**
     * The user password (available when authMethod is USER_PASSWORD)
     */
    public String            password;

    /**
     * A boolean indicating if the user has validated his email adddress (available when authMethod is USER_PASSWORD)
     */
    public boolean           isEmailVerified;

    @OneToMany
    public List<UserAccount> accounts = new ArrayList<UserAccount>();

    public SocialUser toUserSocial() {
        SocialUser socialUser = new SocialUser();
        socialUser.accessToken = this.accessToken;
        socialUser.avatarUrl = this.avatarUrl;
        socialUser.displayName = this.displayName;
        socialUser.email = this.email;
        socialUser.isEmailVerified = this.isEmailVerified;
        socialUser.password = this.password;
        socialUser.secret = this.secret;
        socialUser.token = this.token;

        UserAccount account = accounts.get(0);
        UserId userId = new UserId();
        userId.id = account.userId;
        userId.provider = ProviderType.valueOf(account.provider);
        socialUser.id = userId;

        return socialUser;
    }

    public static User fromUserSocial(SocialUser socialUser) {
        User user = new User();
        user.accessToken = socialUser.accessToken;
        user.avatarUrl = socialUser.avatarUrl;
        user.displayName = socialUser.displayName;
        user.email = socialUser.email;
        user.isAdmin = Boolean.FALSE;
        user.isEmailVerified = socialUser.isEmailVerified;
        user.password = socialUser.password;
        user.secret = socialUser.secret;
        user.token = socialUser.token;

        UserAccount account = new UserAccount();
        account.userId = socialUser.id.id;
        account.provider = socialUser.id.provider.toString();

        user.accounts.add(account);

        return user;
    }
}
