package models;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import play.db.jpa.Model;

@Entity
@Table(name = "zoneadmin2")
public class ZoneAdmin2 extends Model {

    @Column(name = "name_2")
    public String       name;

    @Column(name = "iso")
    public String       countryCode;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "card_id")
    public OpenDataCard card;

}
