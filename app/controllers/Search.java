package controllers;

import java.util.ArrayList;
import java.util.List;

import models.City;
import models.Country;
import models.ZoneAdmin1;
import models.ZoneAdmin2;
import play.Logger;
import play.data.validation.Required;
import play.mvc.Controller;
import securesocial.provider.SocialUser;
import controllers.securesocial.SecureSocial;

public class Search extends Controller {

    private static void isValidUser() {
        SocialUser user = SecureSocial.getCurrentUser();
        Logger.debug("user is " + user.displayName);
        if (!user.displayName.equals("logisima") && !!user.displayName.equalsIgnoreCase("libertic")) {
            forbidden();
        }
    }

    public static void geoZoneForm() {
        isValidUser();
        render();
    }

    public static void geoZoneResult(@Required Integer level, @Required String query) {
        isValidUser();
        List<Country> countries = new ArrayList<Country>();
        List<ZoneAdmin1> zone1 = new ArrayList<ZoneAdmin1>();
        List<ZoneAdmin2> zone2 = new ArrayList<ZoneAdmin2>();
        List<City> cities = new ArrayList<City>();
        if (validation.hasErrors()) {
            params.flash();
            render("@geoZoneForm");
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
        render("@geoZoneForm", countries, zone1, zone2, cities);
    }
}
