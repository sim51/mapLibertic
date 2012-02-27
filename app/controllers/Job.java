package controllers;

import jobs.ExportCSV;
import play.libs.F.Promise;
import play.mvc.With;
import controllers.securesocial.SecureSocialPublic;

@With(SecureSocialPublic.class)
public class Job extends AbstractController {

    public static void csv() {
        hasAdminRight();
        Promise csv = new ExportCSV().now();
        await(csv);
        renderText("OK");
    }
}
