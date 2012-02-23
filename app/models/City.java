package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import play.data.validation.Required;
import play.db.jpa.GenericModel;
import play.db.jpa.JPA;

import com.vividsolutions.jts.geom.Point;

@Entity
@Table(name = "city")
@SequenceGenerator(name = "SequenceIdGenerator", sequenceName = "city_id_seq")
public class City extends GenericModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long               id;

    @Required
    @Column(name = "name")
    public String             name;

    @Required
    @Column(name = "adm0_a3")
    public String             countryCode;

    @Required
    @Column(name = "longitude")
    public float              longitude;

    @Required
    @Column(name = "latitude")
    public float              latitude;

    @Column(name = "the_geom")
    @Type(type = "org.hibernatespatial.GeometryUserType")
    public Point              location;

    @OneToMany
    public List<OpenDataCard> card;

    public OpenDataCard getOpenDataCard(String lang) {
        return OpenDataCard.find(
                "SELECT card FROM OpenDataCard card JOIN City city WHERE city.id = :id ORDER BY card.date DESC",
                this.id).first();
    }

    public static Long getCardIdFromLongLat(float scale, float longitude, float latitude) {
        Long id = null;
        if (scale < 200000000) {
            //@formatter:off
            List<Object[]> cards = JPA.em().createNativeQuery(
                            "SELECT " +
                                    "opendatacard.id, " +
                                    "opendatacard.name " +
                            "FROM " +
                                "city INNER JOIN opendatacard ON city.card_id=opendatacard.id " +
                            "WHERE " +
                                "opendatacard.status>0 AND " +
                                "distance(PointFromText('POINT("+ longitude + " " + latitude + ")', 900913), the_geom) < 0.1 " +
                            "ORDER BY " +
                                "distance(PointFromText('POINT(" + longitude + " " + latitude + ")', 900913), the_geom) ASC LIMIT 1"
                    ).getResultList();
            //@formatter:on
            if (cards.size() > 0) {
                Object[] result = cards.get(0);
                id = Long.valueOf(result[0].toString());
            }
        }
        return id;
    }

    public static OpenDataCard getCardFromLongLat(float scale, float longitude, float latitude) {
        if (scale < 20000000) {
            List<Object[]> cards = JPA
                    .em()
                    .createNativeQuery(
                            "SELECT opendatacard.name, opendatacard.status, opendatacard.isThereCitizenMvt, opendatacard.url, opendatacard.plateform, opendatacard.numOfData, opendatacard.opening, opendatacard.lastUpdate, opendatacard.bernersLeerate, opendatacard.license, opendatacard.description, opendatacard.thematic, opendatacard.dataOwners, opendatacard.formats, opendatacard.contacts FROM city INNER JOIN opendatacard ON city.card_id=opendatacard.id WHERE opendatacard.status>0 AND distance(PointFromText('POINT("
                                    + longitude
                                    + " "
                                    + latitude
                                    + ")', 900913), the_geom) < 0.1 ORDER BY distance(PointFromText('POINT("
                                    + longitude + " " + latitude + ")', 900913), the_geom) ASC LIMIT 1")
                    .getResultList();
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
