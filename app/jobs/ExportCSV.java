package jobs;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import models.City;
import models.Country;
import models.OpenDataCard;
import models.ZoneAdmin1;
import models.ZoneAdmin2;
import play.i18n.Lang;
import play.jobs.Job;
import play.jobs.On;

@On("0 0 12 * * ?")
public class ExportCSV extends Job {

    @Override
    public void doJob() throws Exception {
        String fileText = "LEVEL;COUNTRY_CODE;NAME;STATUS;CITIZEN_MVT;URL;PLATEFORM;N°_DATA;OPENING_DATE;LAST_UPDATED;BENERSLEE_RATE;LICENSE;THEMATIC;DATA_OWNER;FORMATS;CONTACTS;COMMENT";

        List<Country> countries = Country.findAllOpen();
        for (Country country : countries) {
            OpenDataCard card = country.getOpenDataCard(Lang.get());
            //@formatter:off
            fileText += "\n";
            fileText += "1;";                           // LEVEL
            fileText += country.countryCode + ";";      // COUNTRY
            fileText += country.name + ";";             // NAME
            fileText += card.status + ";";              // STATUS
            fileText += card.isThereCitizenMvt + ";";   // CITIZEN_MVT
            fileText += card.url + ";";                 // URL
            fileText += card.plateform + ";";           // PLATEFORME
            fileText += card.numOfData + ";";           // N° DATA
            fileText += card.opening + ";";             // OPENING
            fileText += card.lastUpdate + ";";          // UPDATED
            fileText += card.bernersLeeRate + ";";      // RATE
            fileText += card.license + ";";             // LICENSE
            fileText += card.thematic + ";";            // THEMATIC
            fileText += card.dataOwners + ";";          // DATA_OWNER
            fileText += card.formats + ";";             // FORMAT
            fileText += card.contacts + ";";            // CONTACT
            fileText += card.description;               // COMMENT
            //@formatter:on
        }
        List<ZoneAdmin1> zone1 = ZoneAdmin1.findAllOpen();
        for (ZoneAdmin1 zone : zone1) {
            OpenDataCard card = zone.getOpenDataCard(Lang.get());
            //@formatter:off
            fileText += "\n";
            fileText += "1;";                           // LEVEL
            fileText += zone.countryCode + ";";         // COUNTRY
            fileText += zone.name + ";";                // NAME
            fileText += card.status + ";";              // STATUS
            fileText += card.isThereCitizenMvt + ";";   // CITIZEN_MVT
            fileText += card.url + ";";                 // URL
            fileText += card.plateform + ";";           // PLATEFORME
            fileText += card.numOfData + ";";           // N° DATA
            fileText += card.opening + ";";             // OPENING
            fileText += card.lastUpdate + ";";          // UPDATED
            fileText += card.bernersLeeRate + ";";      // RATE
            fileText += card.license + ";";             // LICENSE
            fileText += card.thematic + ";";            // THEMATIC
            fileText += card.dataOwners + ";";          // DATA_OWNER
            fileText += card.formats + ";";             // FORMAT
            fileText += card.contacts + ";";            // CONTACT
            fileText += card.description;               // COMMENT
            //@formatter:on
        }

        List<ZoneAdmin2> zone2 = ZoneAdmin2.findAllOpen();
        for (ZoneAdmin2 zone : zone2) {
            OpenDataCard card = zone.getOpenDataCard(Lang.get());
            //@formatter:off
            fileText += "\n";
            fileText += "1;";                           // LEVEL
            fileText += zone.countryCode + ";";         // COUNTRY
            fileText += zone.name + ";";                // NAME
            fileText += card.status + ";";              // STATUS
            fileText += card.isThereCitizenMvt + ";";   // CITIZEN_MVT
            fileText += card.url + ";";                 // URL
            fileText += card.plateform + ";";           // PLATEFORME
            fileText += card.numOfData + ";";           // N° DATA
            fileText += card.opening + ";";             // OPENING
            fileText += card.lastUpdate + ";";          // UPDATED
            fileText += card.bernersLeeRate + ";";      // RATE
            fileText += card.license + ";";             // LICENSE
            fileText += card.thematic + ";";            // THEMATIC
            fileText += card.dataOwners + ";";          // DATA_OWNER
            fileText += card.formats + ";";             // FORMAT
            fileText += card.contacts + ";";            // CONTACT
            fileText += card.description;               // COMMENT
            //@formatter:on
        }

        List<City> cities = City.findAllOpen();
        for (City city : cities) {
            OpenDataCard card = city.getOpenDataCard(Lang.get());
            //@formatter:off
            fileText += "\n";
            fileText += "1;";                           // LEVEL
            fileText += city.countryCode + ";";         // COUNTRY
            fileText += city.name + ";";                // NAME
            fileText += card.status + ";";              // STATUS
            fileText += card.isThereCitizenMvt + ";";   // CITIZEN_MVT
            fileText += card.url + ";";                 // URL
            fileText += card.plateform + ";";           // PLATEFORME
            fileText += card.numOfData + ";";           // N° DATA
            fileText += card.opening + ";";             // OPENING
            fileText += card.lastUpdate + ";";          // UPDATED
            fileText += card.bernersLeeRate + ";";      // RATE
            fileText += card.license + ";";             // LICENSE
            fileText += card.thematic + ";";            // THEMATIC
            fileText += card.dataOwners + ";";          // DATA_OWNER
            fileText += card.formats + ";";             // FORMAT
            fileText += card.contacts + ";";            // CONTACT
            fileText += card.description;               // COMMENT
            //@formatter:on
        }

        // we create the file
        File file = new File("public/opendata-map.csv");
        FileOutputStream fop = new FileOutputStream(file);
        fop.write(fileText.getBytes());
        fop.flush();
        fop.close();
    }
}
