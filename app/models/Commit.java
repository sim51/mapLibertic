package models;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Type;

import play.data.validation.Required;
import play.db.jpa.Model;

@Entity
public class Commit extends Model {

    @Required
    @ManyToOne
    public User    user;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    public String  description;

    @Required
    public Boolean isMajor;

}
