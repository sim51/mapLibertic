package models;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import play.data.validation.Required;
import play.data.validation.URL;
import play.db.jpa.Model;

@Entity
@Table(name = "opendatacard")
public class OpenDataCard extends Model {

    @Required
    public int     status;

    public Boolean isThereCitizenMvt;

    @URL
    public String  url;

    public String  plateform;

    public int     numOfData;

    public Date    opening;

    public Date    lastUpdate;

    public float   bernersLeeRate;

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

}
