package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import play.db.jpa.JPA;
import play.db.jpa.Model;

@Entity
@Table(name = "city")
public class City extends Model {

    @Column(name = "name")
    public String       name;

    @Column(name = "adm0_a3")
    public String       countryCode;

    @Column(name = "longitude")
    public float        longitude;

    @Column(name = "latitude")
    public float        latitude;

    @OneToOne
    @JoinColumn(name = "card_id")
    public OpenDataCard card;

    public static OpenDataCard getCardFromLongLat(float scale, float longitude, float latitude) {
        if (scale < 151) {
            List<Object[]> cards = JPA
                    .em()
                    .createNativeQuery(
                            "SELECT opendatacard.name, opendatacard.status, opendatacard.isThereCitizenMvt, opendatacard.url, opendatacard.plateform, opendatacard.numOfData, opendatacard.opening, opendatacard.lastUpdate, opendatacard.bernersLeerate, opendatacard.license, opendatacard.description, opendatacard.thematic, opendatacard.dataOwners, opendatacard.formats, opendatacard.contacts FROM city INNER JOIN opendatacard ON city.card_id=opendatacard.id WHERE distance(PointFromText('POINT("
                                    + longitude
                                    + " "
                                    + latitude
                                    + ")', 900913), the_geom) < 0.1 ORDER BY distance(PointFromText('POINT("
                                    + longitude + " " + latitude + ")', 900913), the_geom) ASC").getResultList();
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
