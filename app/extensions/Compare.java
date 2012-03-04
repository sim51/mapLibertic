package extensions;

import java.io.IOException;

import play.Logger;
import play.templates.JavaExtensions;
import service.DiffUtil;

public class Compare extends JavaExtensions {

    public static String compare(String text, String text2) {
        DiffUtil diffUtil;
        try {
            diffUtil = new DiffUtil(text, text2);
            return diffUtil.toString();
        } catch (IOException e) {
            Logger.error("Error with diff : " + e.getMessage());
            return text;
        }

    }

}
