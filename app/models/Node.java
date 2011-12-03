package models;

import java.util.Date;
import java.util.List;

import play.db.jpa.Model;

public class Node extends Model {

    public Date         date;
    public String       url;
    public List<String> categories;
    public Integer      TBL;
    public String       state;
    public String       licence;
    public Integer      numberOfData;
    public String       typology;

}
