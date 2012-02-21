package controllers;

import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutionException;

import notifier.Mails;
import play.Logger;
import play.Play;
import play.cache.Cache;
import play.cache.CacheFor;
import play.data.validation.Email;
import play.data.validation.Required;
import play.libs.Codec;
import play.libs.Images;
import play.mvc.Controller;
import securesocial.provider.SocialUser;

import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

import controllers.securesocial.SecureSocial;

public class Application extends Controller {

    @CacheFor
    public static void index() {
        String menu = "index";
        SocialUser user = SecureSocial.getCurrentUser();
        List<SyndFeed> myFeeds = null;
        try {
            URL urlRSS = new URL(Play.configuration.getProperty("libertic.rss"));
            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed = input.build(new XmlReader(urlRSS));
            myFeeds = feed.getEntries().subList(0, 5);
        } catch (Exception e) {
            Logger.error("Lecture flus RSS", e);
        }
        render(menu, user, myFeeds);
    }

    @CacheFor
    public static void about() {
        String menu = "about";
        SocialUser user = SecureSocial.getCurrentUser();
        render(menu, user);
    }

    public static void contact() {
        String menu = "contact";
        String randomID = Codec.UUID();
        SocialUser user = SecureSocial.getCurrentUser();
        render(menu, randomID, user);
    }

    public static void sendContact(@Required String author, @Required String message, @Required @Email String email,
            @Required String code, String randomID) throws InterruptedException, ExecutionException {

        if (validation.hasErrors()) {
            params.flash();
            validation.keep();
            randomID = Codec.UUID();
            String menu = "contact";
            render("@contact", menu, randomID);
        }
        Mails.contact(author, message, email);
        flash.success("Merci pour votre intéret %s", author);
        contact();
    }

    public static void captcha(String id) {
        Images.Captcha captcha = Images.captcha();
        // we set the text color of the captcha
        String code = captcha.getText("#000000");
        // we set the life time of the captcha
        Cache.set(id, code, "30mn");
        renderBinary(captcha);
    }
}
