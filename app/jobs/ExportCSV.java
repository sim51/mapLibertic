package jobs;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.List;

import models.City;
import models.Country;
import models.OpenDataCard;
import models.ZoneAdmin1;
import models.ZoneAdmin2;
import play.i18n.Lang;
import play.jobs.Job;
import play.jobs.Every;

@Every("4h")
public class ExportCSV extends Job {

    @Override
    public void doJob() throws Exception {
        String fileText = "LEVEL;COUNTRY_CODE;NAME;STATUS;CITIZEN_MVT;URL;PLATEFORM;N°_DATA;OPENING_DATE;LAST_UPDATED;BENERSLEE_RATE;LICENSE;THEMATIC;DATA_OWNER;FORMATS;CONTACTS;COMMENT";

        List<Country> countries = Country.findAllOpen();
        for (Country country : countries) {
            OpenDataCard card = country.getOpenDataCard(Lang.get());
            if (card.status > 0) {
                fileText += getLine(card.level, country.countryCode, card.name, card.status, card.isThereCitizenMvt,
                        card.url, card.plateform, card.numOfData, card.opening, card.lastUpdate, card.bernersLeeRate,
                        card.license, card.thematic, card.dataOwners, card.formats, card.contacts, card.description);
            }
        }
        List<ZoneAdmin1> zone1 = ZoneAdmin1.findAllOpen();
        for (ZoneAdmin1 zone : zone1) {
            OpenDataCard card = zone.getOpenDataCard(Lang.get());
            if (card.status > 0) {
                fileText += getLine(card.level, zone.countryCode, card.name, card.status, card.isThereCitizenMvt,
                        card.url, card.plateform, card.numOfData, card.opening, card.lastUpdate, card.bernersLeeRate,
                        card.license, card.thematic, card.dataOwners, card.formats, card.contacts, card.description);
            }
        }

        List<ZoneAdmin2> zone2 = ZoneAdmin2.findAllOpen();
        for (ZoneAdmin2 zone : zone2) {
            OpenDataCard card = zone.getOpenDataCard(Lang.get());
            if (card.status > 0) {
                fileText += getLine(card.level, zone.countryCode, card.name, card.status, card.isThereCitizenMvt,
                        card.url, card.plateform, card.numOfData, card.opening, card.lastUpdate, card.bernersLeeRate,
                        card.license, card.thematic, card.dataOwners, card.formats, card.contacts, card.description);
            }
        }

        List<City> cities = City.findAllOpen();
        for (City city : cities) {
            OpenDataCard card = city.getOpenDataCard(Lang.get());
            if (card.status > 0) {
                fileText += getLine(card.level, city.countryCode, card.name, card.status, card.isThereCitizenMvt,
                        card.url, card.plateform, card.numOfData, card.opening, card.lastUpdate, card.bernersLeeRate,
                        card.license, card.thematic, card.dataOwners, card.formats, card.contacts, card.description);
            }
        }

        // we create the file
        File file = new File("public/opendata-map.csv");
        FileOutputStream fop = new FileOutputStream(file);
        fop.write(fileText.getBytes());
        fop.flush();
        fop.close();
    }

    private String getLine(Integer level, String country, String name, Integer status, Boolean mvtCitizen, String url,
            String plateform, Integer numData, Date opening, Date updated, Float rate, String license, String thematic,
            String owner, String format, String contact, String comment) {
        String fileText = "\n";
        fileText += "\"" + level + "\";";
        fileText += "\"" + country + "\";";
        fileText += "\"" + name + "\";";

        fileText += "\"" + status + "\";";
        if (mvtCitizen == null) {
            mvtCitizen = Boolean.FALSE;
        }

        fileText += "\"" + mvtCitizen + "\";";
        if (url == null) {
            url = "";
        }
        fileText += "\"" + url + "\";";

        if (plateform == null) {
            plateform = "";
        }
        fileText += "\"" + plateform + "\";";

        if (numData == null) {
            fileText += "\"\";";
        }
        else {
            fileText += "\"" + numData + "\";";
        }

        if (opening == null) {
            fileText += "\"\";";
        }
        else {
            fileText += "\"" + opening + "\";";
        }

        if (updated == null) {
            fileText += "\"\";";
        }
        else {
            fileText += "\"" + updated + "\";";
        }

        if (rate == null) {
            fileText += "\"\";";
        }
        else {
            fileText += "\"" + rate + "\";";
        }

        if (license == null) {
            fileText += "\"\";";
        }
        else {
            fileText += "\"" + license + "\";";
        }

        if (thematic == null) {
            fileText += "\"\";";
        }
        else {
            fileText += "\"" + thematic + "\";";
        }

        if (owner == null) {
            fileText += "\"\";";
        }
        else {
            fileText += "\"" + owner + "\";";
        }

        if (format == null) {
            fileText += "\"\";";
        }
        else {
            fileText += "\"" + format + "\";";
        }

        if (contact == null) {
            fileText += "\"\";";
        }
        else {
            fileText += "\"" + contact + "\";";
        }

        if (comment == null) {
            fileText += "\"\";";
        }
        else {
            fileText += "\"" + comment + "\";";
        }

        return fileText;
    }
}
