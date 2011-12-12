package extensions;

import play.templates.JavaExtensions;

public class RateExtensions extends JavaExtensions {

    public static int rate(Float note) {
        return Math.round(note / 5 * 100);
    }

}
