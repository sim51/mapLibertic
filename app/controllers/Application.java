package controllers;

import java.util.ArrayList;
import java.util.Date;

import models.Node;
import play.Play;
import play.mvc.Controller;

public class Application extends Controller {

    public static void index() {
        String url = Play.configuration.getProperty("map.url");
        render(url);
    }

    public static void bubble() {
        Node node = new Node();
        node.date = new Date();
        node.numberOfData = 51;
        node.licence = "ODBL";
        node.TBL = 5;
        node.state = "In reflection";
        node.url = "http://data.nantes.fr/";
        node.categories = new ArrayList<String>();
        node.categories.add("Culture");
        node.categories.add("Tourisme");
        render(node);
    }

}
