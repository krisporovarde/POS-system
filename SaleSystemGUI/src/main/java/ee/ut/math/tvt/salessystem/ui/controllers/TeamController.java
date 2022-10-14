package ee.ut.math.tvt.salessystem.ui.controllers;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.*;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Set;

public class TeamController implements Initializable {
    private String teamName;
    private String contactPerson;
    private String teamMembers;
    private String teamLogo;
    @FXML
    private ImageView imageView;
    @FXML
    private TextArea textArea;

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
//    public static void main(String args[]) throws FileNotFoundException {
//
//    }
//    public void ReadInto() throws FileNotFoundException {
//        TeamController readingProp = new TeamController();
//        readingProp.ReadProperties();
//        teamName = readingProp.teamName;
//        teamMembers = readingProp.teamMembers;
//        contactPerson = readingProp.contactPerson;
//        System.out.println(teamName);
//    }
//
    public void ReadProperties() throws Exception{
        Properties teaminfo = new Properties();
        try (final InputStream inputStream = this.getClass().getResourceAsStream("src/main/resources/application.properties")) {
            teaminfo.load(inputStream);

        } catch (IOException e) {
            e.printStackTrace();
        }
//        teamName = teaminfo.getProperty("teamName");
//        contactPerson = teaminfo.getProperty("contactPerson");
//        teamMembers = teaminfo.getProperty("teamMembers");
//        teamLogo = teaminfo.getProperty("teamLogo");
        System.out.println(teaminfo);

    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        File file = new File("../../../../../../../../../../resources/LV.png");
//        Image image = new Image(file.toURI().toString());
//        imageView.setImage(image);
//        try {
//            ReadProperties();
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//        textArea.setText(teamName + "\n" + contactPerson + "\n" + teamMembers + "\n" + teamLogo);
    }
}
