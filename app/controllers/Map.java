package controllers;

import models.City;
import models.Country;
import models.ZoneAdmin1;
import models.ZoneAdmin2;
import play.Play;
import play.cache.CacheFor;
import play.i18n.Messages;
import play.mvc.Controller;
import play.mvc.With;
import securesocial.provider.SocialUser;
import controllers.securesocial.SecureSocial;
import controllers.securesocial.SecureSocialPublic;

@With(SecureSocialPublic.class)
public class Map extends AbstractController {

    public static void index() {
        String wmsurl = Play.configuration.getProperty("map.wms.url");
        String menu = "map";
        SocialUser user = SecureSocial.getCurrentUser();
        render(menu, wmsurl, user);
    }

    @CacheFor
    public static void onClick(Float scale, Float latitude, Float longitude) {

        if (latitude == null | longitude == null | scale == null) {
            renderText(Messages.get("page.map.nodata"));
        }

        Long cardId = null;
        int level = 0;
        cardId = City.getCardIdFromLongLat(scale, longitude, latitude);
        if (cardId != null) {
            level = 3;
            Card.view(cardId, level);
        }
        cardId = ZoneAdmin2.getCardIdFromLongLat(scale, longitude, latitude);
        if (cardId != null) {
            level = 2;
            Card.view(cardId, level);
        }
        cardId = ZoneAdmin1.getCardIdFromLongLat(scale, longitude, latitude);
        if (cardId != null) {
            level = 1;
            Card.view(cardId, level);
        }
        cardId = Country.getCardIdFromLongLat(scale, longitude, latitude);
        if (cardId != null) {
            level = 0;
            Card.view(cardId, level);
        }
        renderText(Messages.get("page.map.nodata"));
    }

}
