package ee.ut.math.tvt.salessystem.ui.controllers;


import javafx.fxml.Initializable;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Set;

public class TeamController implements Initializable {
    private String teamName;
    private String contactPerson;
    private String[] teamMembers;
    private String teamLogo;

    public TeamController() throws FileNotFoundException {
        this.teamName = teamName;
        this.contactPerson = contactPerson;
        this.teamMembers = teamMembers;
        this.teamLogo = teamLogo;
    }

    //private static final Properties configProp = new Properties();

    /*private static void PropertiesCache() {
        //Private constructor to restrict new instances
        InputStream in = this.getClass().getClassLoader().getResourceAsStream("application.properties");
        System.out.println("Reading all properties from the file");
        try {
            configProp.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/
    public static void main(String args[]) throws FileNotFoundException {

    }
    public void ReadInto() throws FileNotFoundException {
        TeamController readingProp = new TeamController();
        readingProp.ReadProperties();
        teamName = readingProp.teamName;
        teamMembers = readingProp.teamMembers;
        contactPerson = readingProp.contactPerson;
        System.out.println(teamName);
    }

    public void ReadProperties() {
        Properties properties = new Properties();
        try (final InputStream inputStream = this.getClass().getResourceAsStream("../application.properties")) {
            properties.load(inputStream);
            teamName = properties.getProperty(teamName);
            System.out.println(teamName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initialize(URL location, ResourceBundle resources) {
        // TODO refresh view after adding new items
    }
}
