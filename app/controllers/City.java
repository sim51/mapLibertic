package controllers;

import org.geotools.geometry.jts.JTSFactoryFinder;

import play.data.validation.Valid;
import play.mvc.With;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;

import controllers.securesocial.SecureSocialPublic;

@With(SecureSocialPublic.class)
public class City extends AbstractController {

    public static void edit(Long id) {
        String menu = "participate";
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
        render(city, menu);
    }

    public static void save(@Valid models.City city) {
        String menu = "participate";
        isValidUser();
        if (!validation.valid(city).ok) {
            validation.keep();
            params.flash();
            render("@edit", city, menu);
        }
        validation.clear();
        GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();
        city.location = geometryFactory.createPoint(new Coordinate(city.longitude, city.latitude));
        city.location.setSRID(900913);
        city.save();
        flash.success("Enregistrement réussi");
        edit(city.id);
    }

}
