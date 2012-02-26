package models;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import play.db.jpa.JPA;
import play.db.jpa.Model;

@Entity
@Table(name = "zoneadmin2")
public class ZoneAdmin2 extends Model {

    @Column(name = "name_2")
    public String             name;

    @Column(name = "iso")
    public String             countryCode;

    @OneToMany
    public List<OpenDataCard> cards;

    public OpenDataCard getOpenDataCard(String lang) {
        return OpenDataCard.find("SELECT c FROM ZoneAdmin2 z JOIN z.cards c WHERE z.id = ? ORDER BY c.created DESC",
                this.id).first();
    }

    public List<OpenDataCard> getOpenDataCardHistory(String lang) {
        return OpenDataCard.find("SELECT c FROM ZoneAdmin2 z JOIN z.cards c WHERE z.id = ? ORDER BY c.created DESC",
                this.id).fetch();
    }

    public static Long getCardIdFromLongLat(float scale, float longitude, float latitude) {
        Long id = null;
        if (scale > 5000000 && scale < 20000000) {
            //@formatter:off
            List<Object[]> cards = JPA.em().createNativeQuery(
                        "SELECT " +
                                "opendatacard.id, " +
                                "opendatacard.name " +
                        "FROM " +
                            "zoneadmin2_opendatacard INNER JOIN zoneadmin2 ON zoneadmin2_opendatacard.zoneadmin2_id=zoneadmin2.id INNER JOIN opendatacard ON opendatacard.id=zoneadmin2_opendatacard.cards_id " +
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

    public static ZoneAdmin2 findByCardId(Long cardId) {
        return ZoneAdmin2.find("SELECT z FROM ZoneAdmin2 z JOIN z.cards c WHERE c.id=?", cardId).first();
    }

    public static List<ZoneAdmin2> findAllOpen() {
        return ZoneAdmin2.find("SELECT z FROM ZoneAdmin2 z JOIN z.cards c WHERE c.status>0").fetch();
    }

}
