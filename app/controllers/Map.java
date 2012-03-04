package controllers;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

import models.City;
import models.Country;
import models.ZoneAdmin1;
import models.ZoneAdmin2;

import org.apache.commons.io.IOUtils;
import org.im4java.core.CompositeCmd;
import org.im4java.core.IMOperation;

import play.Play;
import play.cache.CacheFor;
import play.i18n.Messages;
import play.libs.Codec;
import play.libs.WS;
import play.libs.WS.HttpResponse;
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

    public static void admin() {
        isAdminUser();
        String wmsurl = Play.configuration.getProperty("map.wms.url");
        String wfsurl = Play.configuration.getProperty("map.wfs.url");
        String menu = "mapAdmin";
        SocialUser user = SecureSocial.getCurrentUser();
        render(menu, wmsurl, wfsurl, user);
    }

    public static void image(String bbox) {
        try {
            String wmsurl = Play.configuration.getProperty("map.wmsimage.url");
            wmsurl = wmsurl.replace("@@BBOX@@", bbox);
            HttpResponse res = WS.url(wmsurl).get();
            if (res.getStatus() == 200) {
                InputStream is = res.getStream();
                String uuid = Codec.UUID();
                FileOutputStream geoServerOut;
                String uuid2 = Codec.UUID();
                geoServerOut = new FileOutputStream(Play.tmpDir + "/" + uuid + ".png");
                IOUtils.copy(is, geoServerOut);

                CompositeCmd cmd = new CompositeCmd();
                IMOperation op = new IMOperation();
                op.gravity("SouthWest");
                op.addImage();
                op.addImage();
                op.addImage();

                // op.addImage(Play.tmpDir + "/" + uuid2);
                String scriptName = Play.tmpDir + "/" + Codec.UUID() + ".sh";
                cmd.createScript(scriptName, op);
                cmd.run(op, Play.applicationPath + "/public/images/map-overlay.png", Play.tmpDir + "/" + uuid + ".png",
                        Play.tmpDir + "/" + uuid2 + ".png");

                InputStream is2 = new BufferedInputStream(new FileInputStream(Play.tmpDir + "/" + uuid2 + ".png"));
                renderBinary(is2, "opendata-map.png");
            }
        } catch (Exception e) {
            error();
        }
        error();
    }
}
