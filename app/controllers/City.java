package controllers;

import org.geotools.geometry.jts.JTSFactoryFinder;

import play.Logger;
import play.data.validation.Valid;
import play.mvc.Controller;
import play.mvc.With;
import securesocial.provider.SocialUser;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;

import controllers.securesocial.SecureSocial;
import controllers.securesocial.SecureSocialPublic;

@With(SecureSocialPublic.class)
public class City extends Controller {

    private static void isValidUser() {
        SocialUser user = SecureSocial.getCurrentUser();
        Logger.debug("user is " + user.displayName);
        if (!user.displayName.equals("logisima") && !!user.displayName.equalsIgnoreCase("libertic")) {
            forbidden();
        }
    }

    public static void edit(Long id) {
        isValidUser();
        models.City city = null;
        if (id != null) {
            city = models.City.findById(id);
        }
        else {
            if (params._contains("name")) {
                city = new models.City();
                city.name = params.get("name");
            }
        }
        render(city);
    }

    public static void save(@Valid models.City city) {
        isValidUser();
        if (!validation.valid(city).ok) {
            validation.keep();
            params.flash();
            render("@city", city);
        }
        validation.clear();
        GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();
        city.location = geometryFactory.createPoint(new Coordinate(city.longitude, city.latitude));
        city.location.setSRID(900913);
        city.save();
        render("@city", city);
    }

}
