package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import play.data.validation.Required;
import play.data.validation.URL;
import play.db.jpa.Model;

@Entity
@Table(name = "opendatacard")
public class OpenDataCard extends Model {

    @Required
    public String  name;

    public Date    created;

    public String  lang;

    @Required
    public Integer status;

    public Boolean isThereCitizenMvt;

    @URL
    public String  url;

    public String  plateform;

    public Integer numOfData;

    public Date    opening;

    public Date    lastUpdate;

    public Float   bernersLeeRate;

    public String  license;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    public String  description;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    public String  thematic;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    public String  dataOwners;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    public String  formats;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    public String  contacts;

    @OneToOne
    public Commit  commit;

    public Long findZoneId(int level) {
        Long zoneId = null;
        switch (level) {
            case 0:
                // search country
                Country country = Country.findByCardId(this.id);
                zoneId = country.id;
                break;
            case 1:
                // search zone1
                ZoneAdmin1 zone1 = ZoneAdmin1.findByCardId(this.id);
                zoneId = zone1.id;
                break;
            case 2:
                // search zone2
                ZoneAdmin2 zone2 = ZoneAdmin2.findByCardId(this.id);
                zoneId = zone2.id;
                break;
            case 3:
                // search city
                City city = City.findByCardId(this.id);
                zoneId = city.id;
                break;
        }
        return zoneId;
    }

    public static List<OpenDataCard> getLastCards(Boolean onlyNew, int size) {
        if (onlyNew) {
            return OpenDataCard
                    .find("SELECT card FROM OpenDataCard card JOIN card.commit commit WHERE commit.isMajor = TRUE AND commit.isFirst = TRUE ORDER BY card.created DESC")
                    .fetch(size);
        }
        else {
            return OpenDataCard
                    .find("SELECT card FROM OpenDataCard card JOIN card.commit commit WHERE commit.isMajor = TRUE ORDER BY card.created DESC")
                    .fetch(size);
        }
    }
}
