package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import play.db.jpa.JPA;
import play.db.jpa.Model;

@Entity
@Table(name = "country")
public class Country extends Model {

    @Column(name = "name")
    public String             name;

    @Column(name = "iso_a3")
    public String             countryCode;

    @OneToMany
    public List<OpenDataCard> card;

    public OpenDataCard getOpenDataCard(String lang) {
        return OpenDataCard.find(
                "SELECT card FROM OpenDataCard card JOIN Country c WHERE c.id = :id ORDER BY card.date DESC", this.id)
                .first();
    }

    public static Long getCardIdFromLongLat(float scale, float longitude, float latitude) {
        Long id = null;
        if (scale > 20000000) {
            List<Object[]> cards = JPA
                    .em()
                    .createNativeQuery(
                            "SELECT opendatacard.id, opendatacard.name FROM country INNER JOIN opendatacard ON country.card_id=opendatacard.id WHERE opendatacard.status>0 AND contains(the_geom, PointFromText('POINT("
                                    + longitude + " " + latitude + ")', 900913)) LIMIT 1").getResultList();
            if (cards.size() > 0) {
                Object[] result = cards.get(0);
                id = Long.valueOf(result[0].toString());
            }
        }
        return id;
    }

    public static OpenDataCard getCardFromLongLat(float scale, float longitude, float latitude) {
        if (scale > 150) {
            List<Object[]> cards = JPA
                    .em()
                    .createNativeQuery(
                            "SELECT opendatacard.name, opendatacard.status, opendatacard.isThereCitizenMvt, opendatacard.url, opendatacard.plateform, opendatacard.numOfData, opendatacard.opening, opendatacard.lastUpdate, opendatacard.bernersLeerate, opendatacard.license, opendatacard.description, opendatacard.thematic, opendatacard.dataOwners, opendatacard.formats, opendatacard.contacts FROM country INNER JOIN opendatacard ON country.card_id=opendatacard.id WHERE opendatacard.status>0 AND contains(the_geom, PointFromText('POINT("
                                    + longitude + " " + latitude + ")', 900913)) LIMIT 1").getResultList();
            if (cards.size() > 0) {
                Object[] result = cards.get(0);
                OpenDataCard card = new OpenDataCard();
                card.name = (String) result[0];
                card.status = (Integer) result[1];
                card.isThereCitizenMvt = (Boolean) result[2];
                card.url = (String) result[3];
                card.plateform = (String) result[4];
                card.numOfData = (Integer) result[5];
                card.opening = (Date) result[6];
                card.lastUpdate = (Date) result[7];
                card.bernersLeeRate = (Float) result[8];
                card.license = (String) result[9];
                card.description = (String) result[10];
                card.thematic = (String) result[11];
                card.dataOwners = (String) result[12];
                card.formats = (String) result[13];
                card.contacts = (String) result[14];
                return card;
            }
            else {
                return null;
            }
        }
        else {
            return null;
        }
    }

}
