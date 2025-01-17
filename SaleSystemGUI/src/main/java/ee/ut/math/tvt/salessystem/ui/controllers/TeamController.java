package ee.ut.math.tvt.salessystem.ui.controllers;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.properties.PropertiesConfiguration;

import java.io.*;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Set;

public class TeamController implements Initializable {

    private static final Logger log = LogManager.getLogger(TeamController.class);
    private String teamName;
    private String contactPerson;
    private String teamMembers;
    private String teamLogo;
    @FXML
    private ImageView imageView;
    @FXML
    private TextArea textArea;


    public static Properties ReadProperties(String filename) throws Exception{
        Properties teaminfo = new Properties();
        try  {
            FileInputStream file = new FileInputStream(filename);
            teaminfo.load(file);
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
            log.error(e);
        }
        log.debug(teaminfo);
        return teaminfo;

    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            Properties teaminfo = ReadProperties("../application.properties");
            this.teamName = teaminfo.getProperty("teamName");
            this.contactPerson = teaminfo.getProperty("contactPerson");
            this.teamMembers = teaminfo.getProperty("teamMembers");
            log.debug("teamName: " + teamName +  " contactPerson: " +  contactPerson + " teamMembers: " + teamMembers);
            //textArea.setText(teamName + "\n" + contactPerson + "\n" + teamMembers);
        } catch (Exception e) {
            log.error(e);
            throw new RuntimeException(e);
        }

    }
}
