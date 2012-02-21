package controllers;

import models.City;
import models.Country;
import models.OpenDataCard;
import models.ZoneAdmin1;
import models.ZoneAdmin2;
import play.Play;
import play.cache.CacheFor;
import play.mvc.Controller;
import securesocial.provider.SocialUser;
import controllers.securesocial.SecureSocial;

public class Map extends Controller {

    public static void index() {
        String wmsurl = Play.configuration.getProperty("map.wms.url");
        String menu = "map";
        SocialUser user = SecureSocial.getCurrentUser();
        render(menu, wmsurl, user);
    }

    @CacheFor
    public static void card(Float scale, Float latitude, Float longitude) {
        if (latitude == null | longitude == null | scale == null) {
            renderText("");
        }

        Long cardId = null;
        int level = 0;
        cardId = City.getCardIdFromLongLat(scale, longitude, latitude);
        if (cardId != null) {
            level = 3;
            viewCard(cardId, level);
        }
        cardId = ZoneAdmin2.getCardIdFromLongLat(scale, longitude, latitude);
        if (cardId != null) {
            level = 2;
            viewCard(cardId, level);
        }
        cardId = ZoneAdmin1.getCardIdFromLongLat(scale, longitude, latitude);
        if (cardId != null) {
            level = 1;
            viewCard(cardId, level);
        }
        cardId = Country.getCardIdFromLongLat(scale, longitude, latitude);
        if (cardId != null) {
            level = 0;
            viewCard(cardId, level);
        }
        renderText("");
    }

    @CacheFor
    public static void viewCard(Long id, int level) {
        OpenDataCard card = OpenDataCard.findById(id);
        if (card != null) {
            render(card, level);
        }
        else {
            notFound();
        }
    }

}
