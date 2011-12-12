package controllers;

import java.util.concurrent.ExecutionException;

import models.City;
import models.Country;
import models.OpenDataCard;
import models.ZoneAdmin1;
import models.ZoneAdmin2;
import notifier.Mails;
import play.Play;
import play.cache.Cache;
import play.cache.CacheFor;
import play.data.validation.Email;
import play.data.validation.Required;
import play.libs.Codec;
import play.libs.Images;
import play.mvc.Controller;

public class Application extends Controller {

    public static void index() {
        String wmsurl = Play.configuration.getProperty("map.wms.url");
        String menu = "index";
        render(menu, wmsurl);
    }

    public static void card(float scale, float latitude, float longitude) {
        OpenDataCard card = null;
        int level = 0;
        card = City.getCardFromLongLat(scale, longitude, latitude);
        if (card != null) {
            level = 3;
            render(card, level);
        }
        card = ZoneAdmin2.getCardFromLongLat(scale, longitude, latitude);
        if (card != null) {
            level = 2;
            render(card, level);
        }
        card = ZoneAdmin1.getCardFromLongLat(scale, longitude, latitude);
        if (card != null) {
            level = 1;
            render(card, level);
        }
        card = Country.getCardFromLongLat(scale, longitude, latitude);
        if (card != null) {
            level = 0;
            render(card, level);
        }
        renderText("");
    }

    @CacheFor
    public static void about() {
        String menu = "about";
        render(menu);
    }

    @CacheFor
    public static void contact() {
        String menu = "contact";
        String randomID = Codec.UUID();
        render(menu, randomID);
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
