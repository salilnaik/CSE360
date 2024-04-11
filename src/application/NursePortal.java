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

public class NursePortal extends Application
{

	private Database db = new Database();
	private Patient patient;
    private String username;
    private TextArea vitalsTextArea;
    private TextArea allergiesTextArea;
    private TextArea concernsTextArea;
    private TextArea historyTextArea;
    String selectedPatient;

    public NursePortal(String username)
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

        Label titleLabel = new Label("Nurse Portal");
        titleLabel.setFont(Font.font(24));
        titleLabel.setAlignment(Pos.CENTER);
        
        Label historyLabel = new Label("Patient Visit History:");
        historyLabel.setPrefWidth(880);
        historyLabel.setAlignment(Pos.BOTTOM_LEFT);

        // add components to display patient information
        ComboBox<String> patientsComboBox = new ComboBox<>();
        patientsComboBox.setItems(getPatientIDs());
        patientsComboBox.setPromptText("Choose Patient");
        patientsComboBox.setOnAction(e ->
        {
            selectedPatient = patientsComboBox.getValue();
            if (selectedPatient != null && !selectedPatient.isEmpty())
            {
                patient = db.getPatientInfo(patientsComboBox.getValue());
                String out = "";
                try {
	                Scanner reader = new Scanner(new File("patient_info/" + patientsComboBox.getValue() + ".txt"));
	    			while(reader.hasNextLine()) {
	    				out += reader.nextLine() + "\n";
	    			}
                }catch(IOException exception) {
                	System.out.println(exception);
                }
                historyTextArea.setText(out);
            }
        });

        vitalsTextArea = new TextArea();
        vitalsTextArea.setText("Weight: \nHeight: \nBody Temp: \nBlood Pressure:");
        vitalsTextArea.setPrefHeight(200); 
        
        allergiesTextArea = new TextArea();
        allergiesTextArea.setPromptText("Enter allergies here...");
        allergiesTextArea.setPrefHeight(200);
        
        concernsTextArea = new TextArea();
        concernsTextArea.setPromptText("Enter health concerns here...");
        concernsTextArea.setPrefHeight(200);
        
        historyTextArea = new TextArea();
        historyTextArea.setPromptText("No patient history yet.");
        historyTextArea.setPrefHeight(300);
        historyTextArea.setEditable(false);
        

        Button updateButton = new Button("Save");
        updateButton.setOnAction(e ->
        {
        	File f = new File("patient_info/" + selectedPatient + ".txt");
        	if(!f.exists()) {
        		try {
        			f.createNewFile();
        		}catch(IOException exception) {
        			System.out.println("Error making patient info file" + exception);
        		}
        	}
        	int visits = 1;
        	patient.setPreviousVisits(vitalsTextArea.getText() + "\nAllergies: " + allergiesTextArea.getText() + "\nConcerns: " + concernsTextArea.getText());
        	try {
				Scanner reader = new Scanner(new File("patient_info/" + selectedPatient + ".txt"));
				while(reader.hasNextLine()) {
					if(reader.nextLine().contains("----------"))
						visits++;
				}
				reader.close();
				PrintWriter writer = new PrintWriter(new FileOutputStream(new File("patient_info/" + patient.getPatientId() + ".txt"), true));
				writer.append("----------Visit " + visits + "----------\n" + "INTAKE INFO:\n" + patient.getPreviousVisits() + "\n");
				writer.close();
			} catch(FileNotFoundException exception) {
				System.out.println(exception);
			}
        });

        VBox updateMedicalRecordsBox = new VBox(10);
        updateMedicalRecordsBox.setAlignment(Pos.CENTER);
        updateMedicalRecordsBox.getChildren().addAll(
                patientsComboBox, historyLabel, historyTextArea, vitalsTextArea,
                allergiesTextArea, concernsTextArea, updateButton
        );

        Button exitButton = new Button("Exit");
        exitButton.setOnAction(e ->
        {
        	Main main = new Main();
            main.start(primaryStage);
        });

        // add components to root layout
        root.getChildren().addAll(titleLabel, updateMedicalRecordsBox, exitButton);

        // Set up scene
        Scene scene = new Scene(root, 900, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Method to get patient IDs from the Database
    private ObservableList<String> getPatientIDs()
    {
        Database database = new Database();
        return FXCollections.observableArrayList(database.getPatientIDs());
    }


    public static void main(String[] args)
    {
        launch(args);
    }
}
