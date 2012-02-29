package models;

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
    public Float              longitude;

    @Required
    @Column(name = "latitude")
    public Float              latitude;

    @Required
    @Column(name = "pop_max")
    public Integer            population;

    @Column(name = "the_geom")
    @Type(type = "org.hibernatespatial.GeometryUserType")
    public Point              location;

    @OneToMany
    public List<OpenDataCard> cards;

    public OpenDataCard getOpenDataCard(String lang) {
        return OpenDataCard.find("SELECT c FROM City z JOIN z.cards c WHERE z.id = ? ORDER BY c.created DESC", this.id)
                .first();
    }

    public List<OpenDataCard> getOpenDataCardHistory(String lang) {
        return OpenDataCard.find("SELECT c FROM City z JOIN z.cards c WHERE z.id = ? ORDER BY c.created DESC", this.id)
                .fetch();
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
                                "city_opendatacard INNER JOIN city ON city_opendatacard.city_id=city.id INNER JOIN opendatacard ON opendatacard.id=city_opendatacard.cards_id " +
                            "WHERE " +
                                "(opendatacard.status>0 OR opendatacard.istherecitizenmvt = '1') AND " +
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

    public static City findByCardId(Long cardId) {
        return City.find("SELECT z FROM City z JOIN z.cards c WHERE c.id=?", cardId).first();
    }

    public static List<City> findAllOpen() {
        return City.find("SELECT DISTINCT z FROM City z JOIN z.cards c WHERE c.status>0").fetch();
    }
}
