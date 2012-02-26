package controllers;

import java.util.ArrayList;
import java.util.List;

import models.City;
import models.Country;
import models.ZoneAdmin1;
import models.ZoneAdmin2;
import play.Logger;
import play.data.validation.Required;
import play.mvc.With;
import controllers.securesocial.SecureSocialPublic;

@With(SecureSocialPublic.class)
public class Search extends AbstractController {

    public static void geoZoneForm() {
        String menu = "participate";
        params.put("level", "3");
        Boolean isAdmin = hasAdminRight();
        render(menu, isAdmin);
    }

    public static void geoZoneResult(@Required Integer level, @Required String query) {
        String menu = "participate";
        Boolean isAdmin = hasAdminRight();
        List<Country> countries = new ArrayList<Country>();
        List<ZoneAdmin1> zone1 = new ArrayList<ZoneAdmin1>();
        List<ZoneAdmin2> zone2 = new ArrayList<ZoneAdmin2>();
        List<City> cities = new ArrayList<City>();
        if (validation.hasErrors()) {
            params.flash();
            render("@geoZoneForm", menu);
        }
        validation.clear();
        switch (level) {
            case 0:
                // search country
                countries = Country.find("lower(name) LIKE ?", "%" + query.toLowerCase() + "%").fetch(50);
                Logger.debug("Country size is " + countries.size());
                break;
            case 1:
                // search zone1
                zone1 = ZoneAdmin1.find("lower(name) LIKE ?", "%" + query.toLowerCase() + "%").fetch(50);
                Logger.debug("Zone1 size is " + zone1.size());
                break;
            case 2:
                // search zone2
                zone2 = ZoneAdmin2.find("lower(name) LIKE ?", "%" + query.toLowerCase() + "%").fetch(50);
                Logger.debug("Zone2 size is " + zone2.size());
                break;
            case 3:
                // search city
                cities = City.find("lower(name) LIKE ?", "%" + query.toLowerCase() + "%").fetch(50);
                Logger.debug("City size is " + cities.size());
                break;
        }
        render("@geoZoneForm", countries, zone1, zone2, cities, menu, isAdmin);
    }
}
