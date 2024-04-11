package application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.Scanner;

public class PatientPortal extends Application
{

    private String loggedInUsername;
    private VBox patientInfoBox;
    private TabPane tabPane;
    private TextArea messageTextArea;
    private Patient patient;
    private Database db = new Database();

    public PatientPortal(String username)
    {
        this.loggedInUsername = username;
        this.patient = db.getPatientInfo(username);
    }

    @Override
    public void start(Stage primaryStage)
    {
        primaryStage.setTitle("Patient Portal");

        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);
        root.setSpacing(10);
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #cccccc, #f9b9aa);");

        Label titleLabel = new Label("Patient Portal");
        titleLabel.setFont(Font.font(24));
        titleLabel.setAlignment(Pos.CENTER);

        patientInfoBox = createPatientInfoBox(loggedInUsername);

        Button refreshButton = new Button("Refresh");
        refreshButton.setOnAction(e -> refreshPatientInfo(loggedInUsername));

        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(e ->
        {
            primaryStage.close();
            PatientLoginPage patientLoginPage = new PatientLoginPage();
            patientLoginPage.start(new Stage());
        });

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        buttonBox.getChildren().addAll(refreshButton, logoutButton);
        buttonBox.setPadding(new Insets(10, 0, 0, 0));
        
        TabPane tabPane = createTabPane();
        Button sendButton = new Button("Send Message");
        sendButton.setOnAction(e -> sendMessageToDoctor(tabPane));

        root.getChildren().addAll(titleLabel, patientInfoBox, buttonBox, tabPane);

        Scene scene = new Scene(root, 900, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private VBox createPatientInfoBox(String username)
    {
        VBox patientInfoBox = new VBox();
        patientInfoBox.setAlignment(Pos.CENTER);
        patientInfoBox.setSpacing(10);

        String filePath = "patient_login/" + username + ".txt";

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath)))
        {
            String line;
            while ((line = reader.readLine()) != null)
            {
                String[] parts = line.split(":");
                if (parts.length == 2)
                {
                    String key = parts[0].trim();
                    String value = parts[1].trim();
                    if (key.equals("First Name") || key.equals("Last Name") || key.equals("Date of Birth"))
                    {
                        Label infoLabel = new Label(key + ": " + value);
                        patientInfoBox.getChildren().add(infoLabel);
                    }
                    
                    else if (!key.equals("Password") &&
                            !key.equals("Findings") && !key.equals("Prescription") &&
                            !key.equals("Allergies") && !key.equals("Immunizations"))
                    {
                        HBox infoBox = new HBox(10);
                        infoBox.setAlignment(Pos.CENTER); 
                        Label keyLabel = new Label(key + ": ");
                        TextField valueTextField = new TextField(value);
                        Button updateButton = new Button("Update");
                        updateButton.setOnAction(e ->
                        {
                            // update patient's information
                            String newValue = valueTextField.getText();
                            updatePatientInfo(username, key, newValue);
                            // refresh patient info box
                            patientInfoBox.getChildren().clear();
                            patientInfoBox.getChildren().addAll(createPatientInfoBox(username).getChildren());
                        });
                        infoBox.getChildren().addAll(keyLabel, valueTextField, updateButton);
                        patientInfoBox.getChildren().add(infoBox);
                    }
                }
            }
        }
        
        catch (IOException e)
        {
            System.err.println("Error reading patient information: " + e.getMessage());
        }

        return patientInfoBox;
    }


    private void refreshPatientInfo(String username)
    {
        patientInfoBox.getChildren().clear();
        VBox updatedInfoBox = createPatientInfoBox(username);
        patientInfoBox.getChildren().add(updatedInfoBox);
    }
    
    private void updatePatientInfo(String username, String key, String newValue)
    {
        String filePath = "patient_login/" + username + ".txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath)))
        {
            String line;
            StringBuilder updatedInfo = new StringBuilder();
            while ((line = reader.readLine()) != null)
            {
                if (line.startsWith(key))
                {
                    updatedInfo.append(key).append(": ").append(newValue).append("\n");
                }
                
                else
                {
                    updatedInfo.append(line).append("\n");
                }
            }
            // write the updated information back to the file
            try (PrintWriter writer = new PrintWriter(new FileWriter(filePath)))
            {
                writer.print(updatedInfo.toString());
                System.out.println("Patient information updated successfully");
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

    private TabPane createTabPane()
    {
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        Tab medicalRecordsTab = new Tab("Medical Records");
        medicalRecordsTab.setContent(createMedicalRecordsContent(loggedInUsername));

        Tab allergiesTab = new Tab("Allergies");
        allergiesTab.setContent(createAllergiesContent(loggedInUsername));

        Tab visitEntriesTab = new Tab("Visit Entries");
        visitEntriesTab.setContent(createVisitEntriesContent(loggedInUsername));

        Tab immunizationsTab = new Tab("Immunizations");
        immunizationsTab.setContent(createImmunizationsContent(loggedInUsername));

        Tab prescriptionsTab = new Tab("Prescriptions");
        prescriptionsTab.setContent(createPrescriptionsContent(loggedInUsername));

        Tab findingsTab = new Tab("Findings");
        findingsTab.setContent(createFindingsContent(loggedInUsername));

        Tab messagesTab = new Tab("Messages");
        VBox messagesContent = new VBox(10);
        messagesContent.setAlignment(Pos.CENTER);
        messageTextArea = new TextArea();
        messageTextArea.setPromptText("Enter your message here...");
        messageTextArea.setPrefHeight(100);
        Button sendButton = new Button("Send Message");
        sendButton.setOnAction(e -> sendMessageToDoctor(tabPane));
        messagesContent.getChildren().addAll(messageTextArea, sendButton);
        messagesTab.setContent(messagesContent);

        loadDoctorMessages(messagesContent);
        
        tabPane.getTabs().addAll(medicalRecordsTab, allergiesTab, visitEntriesTab, immunizationsTab, prescriptionsTab, findingsTab, messagesTab);

        return tabPane;
    }


    private void sendMessageToDoctor(TabPane tabPane)
    {
        String message = messageTextArea.getText();
        if (!message.isEmpty())
        {
            String filePath = "messages/" + loggedInUsername + ".txt";
            try (FileWriter writer = new FileWriter(filePath, true))
            {
                writer.write("Patient: " + LocalDateTime.now() + "\n" + message + "\n");
                writer.flush();
                // update messages tab content
                TextArea messagesTextArea = (TextArea) ((VBox) ((Tab) tabPane.getTabs().get(6)).getContent()).getChildren().get(0);
                messagesTextArea.appendText("\nYou: " + LocalDateTime.now() + "\n" + message + "\n");
                messageTextArea.clear();
            }
            
            catch (IOException ex)
            {
                System.err.println("Error writing message to file: " + ex.getMessage());
            }
        }
    }
    
    private void loadDoctorMessages(VBox messagesContent) {
        String filePath = "messages/" + loggedInUsername + ".txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            TextArea messagesTextArea = new TextArea();
            messagesTextArea.setEditable(false);
            while ((line = reader.readLine()) != null) {
                messagesTextArea.appendText(line + "\n");
            }
            messagesContent.getChildren().add(messagesTextArea);
        } catch (IOException e) {
            System.err.println("Error reading doctor messages: " + e.getMessage());
        }
    }

    private TextArea createMedicalRecordsContent(String username)
    {
        TextArea content = new TextArea();
        String out = "";
        try {
            Scanner reader = new Scanner(new File("patient_info/" + username + ".txt"));
			while(reader.hasNextLine()) {
				out += reader.nextLine() + "\n";
			}
        }catch(IOException exception) {
        	System.out.println(exception);
        }
        content.setText(out);
        return content;
    }
    
    private TextArea createFindingsContent(String username)
    {
        TextArea content = new TextArea(patient.getMedicalHistory());
        content.setEditable(false);
        return content;
    }

    
    private TextArea createPrescriptionsContent(String username)
    {
        TextArea content = new TextArea();
        String v = patient.getImmunizationsPrescriptions();
        content.setText(v.substring(v.lastIndexOf("Prescriptions:")));
        content.setEditable(false);
        return content;
    }

    
    private TextArea createImmunizationsContent(String username)
    {
        TextArea content = new TextArea();
        
        String v = patient.getImmunizationsPrescriptions();
        if(v.lastIndexOf("Immunizations:") < v.lastIndexOf("Prescriptions:"))
        	content.setText(v.substring(v.lastIndexOf("Immunizations:"), v.lastIndexOf("Prescriptions:")));
        else
        	content.setText(v);
        content.setEditable(false);
        return content;
    }


    private TextArea createAllergiesContent(String username)
    {
        TextArea content = new TextArea();

        String v = patient.getPreviousVisits();
        if(v.lastIndexOf("Allergies:") < v.lastIndexOf("Concerns:"))
        	content.setText(v.substring(v.lastIndexOf("Allergies:"), v.lastIndexOf("Concerns:")));
        else
        	content.setText(v);
        content.setEditable(false);

        return content;
    }

    private TextArea createVisitEntriesContent(String username)
    {
    	TextArea content = new TextArea();
        String out = "";
        try {
            Scanner reader = new Scanner(new File("patient_info/" + username + ".txt"));
			while(reader.hasNextLine()) {
				out += reader.nextLine() + "\n";
			}
        }catch(IOException exception) {
        	System.out.println(exception);
        }
        if(out.lastIndexOf("INTAKE INFO:")+13 < out.lastIndexOf("FINDINGS:"))
        	content.setText(out.substring(out.lastIndexOf("INTAKE INFO:")+13, out.lastIndexOf("FINDINGS:")));
        else
        	content.setText(out);
        return content;
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}
