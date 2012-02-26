package controllers;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import models.City;
import models.Commit;
import models.Country;
import models.OpenDataCard;
import models.ZoneAdmin1;
import models.ZoneAdmin2;
import play.data.validation.Required;
import play.data.validation.Valid;
import play.i18n.Lang;
import play.mvc.With;
import service.UserService;
import controllers.securesocial.SecureSocial;
import controllers.securesocial.SecureSocialPublic;

@With(SecureSocialPublic.class)
public class Card extends AbstractController {

    public static void save(@Required int level, @Required Long zoneId, String name, @Valid OpenDataCard card,
            String comment, @Required Boolean isMajor) {
        isValidUser();

        if (card.lang == null) {
            card.lang = Lang.get();
        }
        if (!validation.valid(card).ok) {
            validation.keep();
            params.flash();
            render("@edit", level, zoneId, name, card);
        }
        validation.clear();

        Commit commit = new Commit();
        commit.description = comment;
        commit.isMajor = isMajor;
        commit.user = UserService.findUser(SecureSocial.getCurrentUser().id);
        commit.save();
        card.commit = commit;
        card.created = Calendar.getInstance().getTime();
        card.level = level;
        card.zoneId = zoneId;
        switch (level) {
            case 0:
                // search country
                Country country = Country.findById(zoneId);
                if (country.cards.size() == 0) {
                    commit.isFirst = Boolean.TRUE;
                    commit.isMajor = Boolean.TRUE;
                }
                commit.save();
                card.save();
                country.cards.add(card);
                country.save();
                break;
            case 1:
                // search zone1
                ZoneAdmin1 zone1 = ZoneAdmin1.findById(zoneId);
                if (zone1.cards.size() == 0) {
                    commit.isFirst = Boolean.TRUE;
                    commit.isMajor = Boolean.TRUE;
                }
                commit.save();
                card.save();
                zone1.cards.add(card);
                zone1.save();
                break;
            case 2:
                // search zone2
                ZoneAdmin2 zone2 = ZoneAdmin2.findById(zoneId);
                if (zone2.cards.size() == 0) {
                    commit.isFirst = Boolean.TRUE;
                    commit.isMajor = Boolean.TRUE;
                }
                commit.save();
                card.save();
                zone2.cards.add(card);
                zone2.save();
                break;
            case 3:
                // search city
                City city = City.findById(zoneId);
                if (city.cards.size() == 0) {
                    commit.isFirst = Boolean.TRUE;
                    commit.isMajor = Boolean.TRUE;
                }
                commit.save();
                card.save();
                city.cards.add(card);
                city.save();
                break;
        }
        flash.success("Enregistrement réussi");
        render("@edit", level, zoneId, name, card);
    }

    public static void edit(@Required Long zoneId, @Required int level) {
        isValidUser();
        String name = "";
        OpenDataCard card = null;
        switch (level) {
            case 0:
                // search country
                Country country = Country.findById(zoneId);
                name = country.name;
                card = country.getOpenDataCard(Lang.get());
                render(level, zoneId, name, card);
                break;
            case 1:
                // search zone1
                ZoneAdmin1 zone1 = ZoneAdmin1.findById(zoneId);
                name = zone1.name;
                card = zone1.getOpenDataCard(Lang.get());
                render(level, zoneId, name, card);
                break;
            case 2:
                // search zone2
                ZoneAdmin2 zone2 = ZoneAdmin2.findById(zoneId);
                name = zone2.name;
                card = zone2.getOpenDataCard(Lang.get());
                render(level, zoneId, name, card);
                break;
            case 3:
                // search city
                City city = City.findById(zoneId);
                name = city.name;
                card = city.getOpenDataCard(Lang.get());
                render(level, zoneId, name, card);
                break;
        }
    }

    public static void view(Long cardId, int level) {
        OpenDataCard card = OpenDataCard.findById(cardId);
        if (card != null) {
            Long zoneId = card.findZoneId(level);
            render(card, level, zoneId);
        }
        else {
            notFound();
        }
    }

    public static void history(Long zoneId, int level) {
        List<OpenDataCard> cards = null;
        String name = null;
        switch (level) {
            case 0:
                // search country
                Country country = Country.findById(zoneId);
                cards = country.getOpenDataCardHistory(Lang.get());
                name = country.name;
                render(level, zoneId, cards, name);
                break;
            case 1:
                // search zone1
                ZoneAdmin1 zone1 = ZoneAdmin1.findById(zoneId);
                cards = zone1.getOpenDataCardHistory(Lang.get());
                name = zone1.name;
                render(level, zoneId, cards, name);
                break;
            case 2:
                // search zone2
                ZoneAdmin2 zone2 = ZoneAdmin2.findById(zoneId);
                cards = zone2.getOpenDataCardHistory(Lang.get());
                name = zone2.name;
                render(level, zoneId, cards, name);
                break;
            case 3:
                // search city
                City city = City.findById(zoneId);
                cards = city.getOpenDataCardHistory(Lang.get());
                name = city.name;
                render(level, zoneId, cards, name);
                break;
        }
    }

    public static void version(Long cardId, Long zoneId, int level) {
        OpenDataCard card = OpenDataCard.findById(cardId);
        render(card, level, zoneId);
    }

    public static void compare(Long fromId, Long toId, Long zoneId, int level) {
        OpenDataCard cardFrom = OpenDataCard.findById(fromId);
        OpenDataCard cardTo = OpenDataCard.findById(toId);
        render(cardFrom, cardTo, level, zoneId);
    }

    public static void rss() {
        List<OpenDataCard> cards = OpenDataCard.getLastCards(Boolean.FALSE, 20);
        response.contentType = "application/rss+xml";
        Date today = new Date();
        render(today, cards);
    }

    public static void csv() {
        render();
    }

}
