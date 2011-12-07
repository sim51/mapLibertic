package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

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
}
