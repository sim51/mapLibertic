package models;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import play.db.jpa.Model;

@Entity
@Table(name = "country")
public class Country extends Model {

    @Column(name = "name")
    public String       name;

    @Column(name = "iso_a3")
    public String       countryCode;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "card_id")
    public OpenDataCard card;

}
