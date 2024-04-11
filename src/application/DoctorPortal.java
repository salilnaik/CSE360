package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class DoctorPortal extends Application
{

    private String username;
    private TextArea findingsTextArea;
    private TextArea prescriptionTextArea;
    private TextArea immunizationsTextArea;
    private TextArea medicalRecordsTextArea;
    private TextArea messagesTextArea;
    private TextArea sendMessageTextArea;
    private Patient patient;
    private Database db = new Database();

    public DoctorPortal(String username)
    {
        this.username = username;
    }

    @Override
    public void start(Stage primaryStage)
    {
        primaryStage.setTitle("Doctor Portal");

        // create UI components for doctor portal
        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);
        root.setSpacing(10);
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #cccccc, #f9b9aa);");

        Label titleLabel = new Label("Doctor Portal");
        titleLabel.setFont(Font.font(24));
        titleLabel.setAlignment(Pos.CENTER);

        // add components to display patient information
        ComboBox<String> patientsComboBox = new ComboBox<>();
        patientsComboBox.setItems(getPatientIDs());
        patientsComboBox.setPromptText("Choose Patient");
        
        patientsComboBox.setOnAction(e ->
        {
            String selectedPatient = patientsComboBox.getValue();
            if (selectedPatient != null && !selectedPatient.isEmpty())
            {
            	String out = "";
                try {
	                Scanner reader = new Scanner(new File("patient_info/" + patientsComboBox.getValue() + ".txt"));
	    			while(reader.hasNextLine()) {
	    				out += reader.nextLine() + "\n";
	    			}
                }catch(IOException exception) {
                	System.out.println(exception);
                }
                medicalRecordsTextArea.setText(out);
            	patient = db.getPatientInfo(selectedPatient);
                loadMessages(selectedPatient);
            }
        });

        medicalRecordsTextArea = new TextArea();
        medicalRecordsTextArea.setPromptText("No patient history yet.");
        medicalRecordsTextArea.setPrefHeight(200); 
        medicalRecordsTextArea.setEditable(false);

        // add fields for updating medical records
        findingsTextArea = new TextArea();
        findingsTextArea.setPromptText("Enter findings here...");
        findingsTextArea.setPrefHeight(100); 

        prescriptionTextArea = new TextArea();
        prescriptionTextArea.setPromptText("Enter prescription here...");
        prescriptionTextArea.setPrefHeight(100); 

        immunizationsTextArea = new TextArea();
        immunizationsTextArea.setPromptText("Enter immunizations here...");
        immunizationsTextArea.setPrefHeight(100); 

        // textArea for displaying messages
        messagesTextArea = new TextArea();
        messagesTextArea.setEditable(false); 
        messagesTextArea.setPrefHeight(100);
        
        sendMessageTextArea = new TextArea();
        sendMessageTextArea.setPromptText("Type your message here...");
        sendMessageTextArea.setPrefHeight(100);

        Button updateButton = new Button("Update");
        updateButton.setOnAction(e ->
        {
            // get selected patient and update medical records
            String selectedPatient = patientsComboBox.getValue();
            
            try {
				PrintWriter writer = new PrintWriter(new FileOutputStream(new File("patient_info/" + patient.getPatientId() + ".txt"), true));
				writer.append("\nFINDINGS:\n" + findingsTextArea.getText() + "\nIMMUNIZATIONS/PRESCRIPTIONS:\n" + "Immunizations:\n" + immunizationsTextArea.getText() + "\nPrescriptions:\n" + prescriptionTextArea.getText());
				writer.close();
			} catch(FileNotFoundException exception) {
				System.out.println(exception);
			}
            
//            upload(selectedPatient);
        });
        
        // button for sending message
        Button sendButton = new Button("Send");
        sendButton.setOnAction(e ->
        {
            String selectedPatient = patientsComboBox.getValue();
            String message = sendMessageTextArea.getText();
            sendMessage(selectedPatient, message);
            sendMessageTextArea.clear(); // clear the TextArea after sending the message
        });
        
        VBox sendMessageBox = new VBox(10);
        sendMessageBox.setAlignment(Pos.CENTER);
        sendMessageBox.getChildren().addAll(sendMessageTextArea, sendButton);

        VBox updateMedicalRecordsBox = new VBox(10);
        updateMedicalRecordsBox.setAlignment(Pos.CENTER);
        updateMedicalRecordsBox.getChildren().addAll(
                patientsComboBox, findingsTextArea, immunizationsTextArea, prescriptionTextArea, updateButton
        );

        Button exitButton = new Button("Exit");
        exitButton.setOnAction(e ->
        {
            primaryStage.close();
            DoctorLoginPage doctorLoginPage = new DoctorLoginPage();
            doctorLoginPage.start(new Stage());
        });

        // add components to root layout
        root.getChildren().addAll(titleLabel, medicalRecordsTextArea, updateMedicalRecordsBox, messagesTextArea, sendMessageBox, exitButton);

        // Set up scene
        Scene scene = new Scene(root, 900, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // method to get patient IDs from the Database
    private ObservableList<String> getPatientIDs()
    {
        return FXCollections.observableArrayList(db.getPatientIDs());
    }

    // method to send a message to the patient
    private void sendMessage(String patientUsername, String message)
    {
        Database database = new Database();
        // pass the doctor's ID as the senderId and the patient's ID as the recipientId
        database.saveMessage(patientUsername, message, false);
    }


    private void upload(String patientUsername)
    {
        String newFindings = findingsTextArea.getText();
        String newPrescription = prescriptionTextArea.getText();
        String newAllergies = allergiesTextArea.getText();
        String newImmunizations = immunizationsTextArea.getText();

        // update patient information in the file
        String patientFilePath = "patient_login/" + patientUsername + ".txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(patientFilePath)))
        {
            String line;
            StringBuilder updatedInfo = new StringBuilder();
            boolean foundFindings = false;
            boolean foundPrescription = false;
            boolean foundAllergies = false;
            boolean foundImmunizations = false;
            while ((line = reader.readLine()) != null)
            {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    String key = parts[0].trim();
                    String value = parts[1].trim();
                    switch (key) {
                        case "Findings":
                            updatedInfo.append("Findings: ").append(newFindings).append("\n");
                            foundFindings = true;
                            break;
                        case "Prescription":
                            updatedInfo.append("Prescription: ").append(newPrescription).append("\n");
                            foundPrescription = true;
                            break;
                        case "Allergies":
                            updatedInfo.append("Allergies: ").append(newAllergies).append("\n");
                            foundAllergies = true;
                            break;
                        case "Immunizations":
                            updatedInfo.append("Immunizations: ").append(newImmunizations).append("\n");
                            foundImmunizations = true;
                            break;
                        default:
                            updatedInfo.append(line).append("\n");
                    }
                }
            }
            // append immunizations and allergies if not found
            if (!foundImmunizations)
            {
                updatedInfo.append("Immunizations: ").append(newImmunizations).append("\n");
            }
            
            if (!foundAllergies)
            {
                updatedInfo.append("Allergies: ").append(newAllergies).append("\n");
            }
            // write the updated information back to the file
            try (PrintWriter writer = new PrintWriter(new FileWriter(patientFilePath)))
            {
                writer.print(updatedInfo.toString());
                System.out.println("Patient information updated successfully");
                // append findings, immunizations, allergies, and prescriptions to the medical records box
                medicalRecordsTextArea.appendText("\n\nFindings: " + newFindings);
                medicalRecordsTextArea.appendText("\nImmunizations: " + newImmunizations);
                medicalRecordsTextArea.appendText("\nAllergies: " + newAllergies);
                medicalRecordsTextArea.appendText("\nPrescription: " + newPrescription);
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
    
    private void loadMessages(String patientId)
    {
        String messages = db.getMessages(patientId);
        messagesTextArea.setText(messages);
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}
