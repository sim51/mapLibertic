package controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import models.Node;
import notifier.Mails;
import play.Play;
import play.cache.Cache;
import play.data.validation.Email;
import play.data.validation.Required;
import play.libs.Codec;
import play.libs.Images;
import play.mvc.Controller;

public class Application extends Controller {

    public static void index() {
        String url = Play.configuration.getProperty("map.url");
        String menu = "index";
        render(menu, url);
    }

    public static void about() {
        String menu = "about";
        render(menu);
    }

    public static void contact() {
        String menu = "contact";
        render(menu);
    }

    public static void sendContact(@Required(message = "L'auteur est obligatoire") String author,
            @Required(message = "Le message est obligatoire") String message,
            @Required(message = "L'email est obligatoire") @Email String email,
            @Required(message = "Le code de sécurité est obligatoire") String code, String randomID)
            throws InterruptedException, ExecutionException {

        if (validation.hasErrors()) {
            randomID = Codec.UUID();
            String menu = "contact";
            render("Application/contact.html", menu, randomID);
        }
        Mails.contact(author, message, email);
        flash.success("Merci pour votre intéret %s", author);
        contact();
    }

    public static void bubble() {
        Node node = new Node();
        node.date = new Date();
        node.numberOfData = 51;
        node.licence = "ODBL";
        node.TBL = 5;
        node.state = "In reflection";
        node.url = "http://data.nantes.fr/";
        node.categories = new ArrayList<String>();
        node.categories.add("Culture");
        node.categories.add("Tourisme");
        render(node);
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
