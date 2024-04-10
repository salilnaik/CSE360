package application;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class PatientPortal extends Application
{

    private TextField phoneNumberField;
    private TextField emailField;
    private TextField pharmacyField;
    private TextField insuranceField;
    private Label messageLabel;
    private String loggedInUsername; 
    
    // keep track of current user
    public PatientPortal(String username)
    {
        this.loggedInUsername = username;
    }

    @Override
    public void start(Stage primaryStage)
    {
        primaryStage.setTitle("Patient Portal");

        // create UI components for patient portal
        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);
        root.setSpacing(10);
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #cccccc, #f9b9aa);");

        Label titleLabel = new Label("Patient Portal");
        titleLabel.setFont(Font.font(24));
        titleLabel.setAlignment(Pos.CENTER);

        // add components to display patient information
        VBox patientInfoBox = createPatientInfoBox(loggedInUsername);

        // add fields for updating contact information, pharmacy information, and insurance ID
        phoneNumberField = new TextField();
        phoneNumberField.setPromptText("New Phone Number");

        emailField = new TextField();
        emailField.setPromptText("New Email Address");

        pharmacyField = new TextField();
        pharmacyField.setPromptText("New Pharmacy Information");

        insuranceField = new TextField();
        insuranceField.setPromptText("New Insurance ID");

        Button updateButton = new Button("Update");			// update button
        updateButton.setOnAction(e -> updatePatientInfo());

        messageLabel = new Label();
        messageLabel.setTextFill(Color.GREEN);

        VBox updateInfoBox = new VBox(10);					// update infobox
        updateInfoBox.setAlignment(Pos.CENTER);
        updateInfoBox.getChildren().addAll(phoneNumberField, emailField, pharmacyField, insuranceField, updateButton, messageLabel);

        // add components to root layout
        root.getChildren().addAll(titleLabel, patientInfoBox, updateInfoBox, createTabPane());

        // set up scene
        Scene scene = new Scene(root, 900, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private VBox createPatientInfoBox(String username)
    {
        // create a VBox to display patient information
        VBox patientInfoBox = new VBox();
        patientInfoBox.setAlignment(Pos.CENTER);
        patientInfoBox.setSpacing(10);

        String filePath = "patient_login/" + username + ".txt";

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath)))
        {
            // read patient information from the file
            String line;
            while ((line = reader.readLine()) != null)
            {
                // parse the line to extract relevant information
                String[] parts = line.split(":");
                if (parts.length == 2 && !parts[0].trim().equals("Password"))
                { 
                	// exclude password field
                    String key = parts[0].trim();
                    String value = parts[1].trim();
                    // display the patient information
                    Label infoLabel = new Label(key + ": " + value);
                    patientInfoBox.getChildren().add(infoLabel);
                }
            }
        }
        
        catch (IOException e)
        {
            System.err.println("Error reading patient information: " + e.getMessage());
        }

        return patientInfoBox;
    }


    private TabPane createTabPane()
    {
        // create a TabPane for navigating between different sections of patient information
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        // create tabs for different sections such as Medical Records, Allergies, Visit Entries
        Tab medicalRecordsTab = new Tab("Medical Records");
        Tab allergiesTab = new Tab("Allergies");
        Tab visitEntriesTab = new Tab("Visit Entries");

        // add placeholder content to each tab
        medicalRecordsTab.setContent(createMedicalRecordsContent());
        allergiesTab.setContent(createAllergiesContent());
        visitEntriesTab.setContent(createVisitEntriesContent());

        // add tabs to the TabPane
        tabPane.getTabs().addAll(medicalRecordsTab, allergiesTab, visitEntriesTab);

        return tabPane;
    }

    // placeholder methods for creating content of each tab
    private VBox createMedicalRecordsContent()
    {
        VBox content = new VBox();
        content.getChildren().add(new Label("Medical Records Content"));
        return content;
    }

    private VBox createAllergiesContent()
    {
        VBox content = new VBox();
        content.getChildren().add(new Label("Allergies Content"));
        return content;
    }

    private VBox createVisitEntriesContent()
    {
        VBox content = new VBox();
        content.getChildren().add(new Label("Visit Entries Content"));
        return content;
    }

    // method to update patient information
    private void updatePatientInfo()
    {
        // get updated values from input fields
        String newPhoneNumber = phoneNumberField.getText();
        String newEmail = emailField.getText();
        String newPharmacy = pharmacyField.getText();
        String newInsuranceID = insuranceField.getText();

        // update patient information in the file
        String username = "username";
        String filePath = "patient_login/" + username + ".txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath)))
        {
            String line;
            StringBuilder updatedInfo = new StringBuilder();
            while ((line = reader.readLine()) != null)
            {
                String[] parts = line.split(":");
                if (parts.length == 2)
                {
                    String key = parts[0].trim();
                    String value = parts[1].trim();
                    switch (key)
                    {
                        case "Phone Number":
                            updatedInfo.append("Phone Number: ").append(newPhoneNumber).append("\n");
                            break;
                        case "Email":
                            updatedInfo.append("Email: ").append(newEmail).append("\n");
                            break;
                        case "Pharmacy Information":
                            updatedInfo.append("Pharmacy Information: ").append(newPharmacy).append("\n");
                            break;
                        case "Insurance ID":
                            updatedInfo.append("Insurance ID: ").append(newInsuranceID).append("\n");
                            break;
                        default:
                            updatedInfo.append(line).append("\n");
                    }
                }
            }
            // write the updated information back to the file
            try (PrintWriter writer = new PrintWriter(new FileWriter(filePath)))
            {
                writer.print(updatedInfo.toString());
                messageLabel.setText("Patient information updated successfully");
            }
            
            catch (IOException ex)
            {
                System.err.println("Error updating patient information: " + ex.getMessage());
            }
        }
        
        catch (IOException e)
        {
            System.err.println("Error reading patient information: " + e.getMessage());
        }
    }

    public static void main(String[] args)
    {
        // pass the logged-in username as an argument when launching the application
        launch(args);
    }

    // method to set the logged-in username
    public void setLoggedInUsername(String username)
    {
        this.loggedInUsername = username;
    }
}
