package models;

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
    public List<OpenDataCard> cards;

    public OpenDataCard getOpenDataCard(String lang) {
        return OpenDataCard.find("SELECT c FROM Country z JOIN z.cards c WHERE c.id = ? ORDER BY c.created DESC",
                this.id).first();
    }

    public List<OpenDataCard> getOpenDataCardHistory(String lang) {
        return OpenDataCard.find("SELECT c FROM Country z JOIN z.cards c WHERE c.id = ? ORDER BY c.created DESC",
                this.id).fetch();
    }

    public static Long getCardIdFromLongLat(float scale, float longitude, float latitude) {
        Long id = null;
        if (scale > 20000000) {
            //@formatter:off
            List<Object[]> cards = JPA.em().createNativeQuery(
                        "SELECT " +
                                "opendatacard.id, " +
                                "opendatacard.name " +
                        "FROM " +
                            "country_opendatacard INNER JOIN country ON country_opendatacard.country_id=country.id INNER JOIN opendatacard ON opendatacard.id=country_opendatacard.cards_id " +
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

    public static Country findByCardId(Long cardId) {
        return Country.find("SELECT z FROM Country z JOIN z.cards c WHERE c.id=?", cardId).first();
    }

    public static List<Country> findAllOpen() {
        return Country.find("SELECT z FROM Country z JOIN z.cards c WHERE c.status>0").fetch();
    }

}
