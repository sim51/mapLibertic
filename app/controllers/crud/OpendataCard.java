package controllers.crud;

import java.util.ArrayList;
import java.util.List;

import models.City;
import models.Country;
import models.OpenDataCard;
import models.ZoneAdmin1;
import models.ZoneAdmin2;
import play.Logger;
import play.data.validation.Required;
import play.data.validation.Valid;
import play.i18n.Lang;
import play.mvc.Controller;
import play.mvc.With;
import securesocial.provider.SocialUser;
import controllers.securesocial.SecureSocial;

@With(SecureSocial.class)
public class OpendataCard extends Controller {

    private static void isValidUser() {
        SocialUser user = SecureSocial.getCurrentUser();
        Logger.debug("user is " + user.displayName);
        if (!user.displayName.equals("logisima") && !!user.displayName.equalsIgnoreCase("libertic")) {
            forbidden();
        }
    }

    public static void getSearchGeoZone() {
        isValidUser();
        render();
    }

    public static void searchGeoZone(@Required Integer level, @Required String query) {
        isValidUser();
        List<Country> countries = new ArrayList<Country>();
        List<ZoneAdmin1> zone1 = new ArrayList<ZoneAdmin1>();
        List<ZoneAdmin2> zone2 = new ArrayList<ZoneAdmin2>();
        List<City> cities = new ArrayList<City>();
        if (validation.hasErrors()) {
            params.flash();
            render("@getSearchGeoZone");
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
        render("@getSearchGeoZone", countries, zone1, zone2, cities);
    }

    public static void saveCard(int level, Long id, String name, @Valid OpenDataCard card) {
        isValidUser();
        if (!validation.valid(card).ok) {
            validation.keep();
            params.flash();
            render("@card", level, id, name, card);
        }
        validation.clear();
        if (card.id != null) {
            card.save();
        }
        else {
            card.save();
            switch (level) {
                case 0:
                    // search country
                    Country country = Country.findById(id);
                    country.card.add(card);
                    country.save();
                    break;
                case 1:
                    // search zone1
                    ZoneAdmin1 zone1 = ZoneAdmin1.findById(id);
                    zone1.card.add(card);
                    zone1.save();
                    break;
                case 2:
                    // search zone2
                    ZoneAdmin2 zone2 = ZoneAdmin2.findById(id);
                    zone2.card.add(card);
                    zone2.save();
                    break;
                case 3:
                    // search city
                    City city = City.findById(id);
                    city.card.add(card);
                    city.save();
                    break;
            }
        }
        flash.success("Enregistrement réussi");
        render("@card", level, id, name, card);
    }

    public static void card(@Required int level, @Required Long id) {
        String name = "";
        OpenDataCard card = null;
        switch (level) {
            case 0:
                // search country
                Country country = Country.findById(id);
                name = country.name;
                card = country.getOpenDataCard(Lang.get());
                render(level, id, name, card);
                break;
            case 1:
                // search zone1
                ZoneAdmin1 zone1 = ZoneAdmin1.findById(id);
                name = zone1.name;
                card = zone1.getOpenDataCard(Lang.get());
                render(level, id, name, card);
                break;
            case 2:
                // search zone2
                ZoneAdmin2 zone2 = ZoneAdmin2.findById(id);
                name = zone2.name;
                card = zone2.getOpenDataCard(Lang.get());
                render(level, id, name, card);
                break;
            case 3:
                // search city
                City city = City.findById(id);
                name = city.name;
                card = city.getOpenDataCard(Lang.get());
                render(level, id, name, card);
                break;
        }
    }
}
