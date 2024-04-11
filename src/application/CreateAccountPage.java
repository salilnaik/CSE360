package application;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

public class CreateAccountPage extends Application
{

    private TextField firstNameField;
    private TextField lastNameField;
    private DatePicker dobPicker;
    private TextField emailField;
    private TextField phoneNumberField;
    private TextField pharmacyField; 
    private TextField insuranceField; 
    private PasswordField passwordField;
    private PasswordField confirmPasswordField;
    private Label errorLabel;
    
    private String constructUsername(String firstName, String lastName, String dob)
    {
        // construct the username using the specified format
        String firstNameInitial = firstName.substring(0, 1).toLowerCase();
        String lastNameWithoutSpaces = lastName.replace(" ", "").toLowerCase();
        String yearOfBirth = dob.substring(0, 4);
        return firstNameInitial + lastNameWithoutSpaces + yearOfBirth;
    } 
    
    @Override
    public void start(Stage primaryStage)
    {
    	primaryStage.setTitle("Create Account");

        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);
        root.setSpacing(10);
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #cccccc, #f9b9aa);"); // set background and gradient

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Label firstNameLabel = new Label("First Name:");		// label for first name
        firstNameLabel.setFont(Font.font(20));
        grid.add(firstNameLabel, 0, 0);

        firstNameField = new TextField();		// text field for first name
        grid.add(firstNameField, 1, 0);

        Label lastNameLabel = new Label("Last Name:");		// label for last name
        lastNameLabel.setFont(Font.font(20));
        grid.add(lastNameLabel, 0, 1);

        lastNameField = new TextField();		// text field for last name
        grid.add(lastNameField, 1, 1);

        Label dobLabel = new Label("Date of Birth:");		// label for date of birth
        dobLabel.setFont(Font.font(20));	
        grid.add(dobLabel, 0, 2);

        dobPicker = new DatePicker();		// date picker interaction
        grid.add(dobPicker, 1, 2);

        Label emailLabel = new Label("Email:");		// label for email
        emailLabel.setFont(Font.font(20));
        grid.add(emailLabel, 0, 3);

        emailField = new TextField();		// text field for email
        grid.add(emailField, 1, 3);

        Label phoneNumberLabel = new Label("Phone Number:");	// label for phone number
        phoneNumberLabel.setFont(Font.font(20));
        grid.add(phoneNumberLabel, 0, 4);

        phoneNumberField = new TextField();		// text field for phone number
        grid.add(phoneNumberField, 1, 4);

        Label pharmacyLabel = new Label("Pharmacy Information:"); // label for pharmacy information
        pharmacyLabel.setFont(Font.font(20));
        grid.add(pharmacyLabel, 0, 5);

        pharmacyField = new TextField(); // text field for pharmacy information
        grid.add(pharmacyField, 1, 5);

        Label insuranceLabel = new Label("Insurance ID:"); // label for insurance ID
        insuranceLabel.setFont(Font.font(20));
        grid.add(insuranceLabel, 0, 6);

        insuranceField = new TextField(); // text field for insurance ID
        grid.add(insuranceField, 1, 6);

        Label passwordLabel = new Label("Password:");	// label for password
        passwordLabel.setFont(Font.font(20));
        grid.add(passwordLabel, 0, 7);

        passwordField = new PasswordField();	// text field for password
        grid.add(passwordField, 1, 7);

        Label confirmPasswordLabel = new Label("Confirm Password:");	// label for confirm password
        confirmPasswordLabel.setFont(Font.font(20));
        grid.add(confirmPasswordLabel, 0, 8);

        confirmPasswordField = new PasswordField();		// text field for confirm password
        grid.add(confirmPasswordField, 1, 8);

        Button backButton = new Button("Back");		// back button to go to previous page
        backButton.setFont(Font.font(20));
        grid.add(backButton, 0, 9);

        Button createAccountButton = new Button("Create Account");		// create account button
        createAccountButton.setFont(Font.font(20));
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(createAccountButton);
        grid.add(hbBtn, 1, 9);

        errorLabel = new Label();				// display error with color
        errorLabel.setTextFill(Color.RED);
        grid.add(errorLabel, 1, 10);

        backButton.setOnAction(e ->
        {
            // go back to patient login page
            Stage loginStage = new Stage();
            PatientLoginPage loginPage = new PatientLoginPage();
            loginPage.start(loginStage);
            primaryStage.hide();
        });

        createAccountButton.setOnAction(e ->
        {
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();
            String dob = dobPicker.getValue().toString();
            String email = emailField.getText();
            String phoneNumber = phoneNumberField.getText();
            String pharmacyInfo = pharmacyField.getText(); 
            String insuranceID = insuranceField.getText(); 
            String password = passwordField.getText();
            String confirmPassword = confirmPasswordField.getText();

            // checking if passwords match
            if (!password.equals(confirmPassword))
            {
                errorLabel.setText("Passwords do not match.");
                return;
            }

            // validate insurance ID to contain only integers
            if (!insuranceID.matches("\\d+"))
            {
                errorLabel.setText("Insurance ID must contain only digits.");
                return;
            }

            // if correct, create account
            String username = constructUsername(firstName, lastName, dob);
            String directoryPath = "patient_login/";
            File directory = new File(directoryPath);
            if (!directory.exists())
            {
                if (!directory.mkdirs())
                {
                    System.out.println("Failed to create directory: " + directoryPath);
                    return;
                }
            }

            // populate file with patient info
            String filePath = directoryPath + username + ".txt";
            try (PrintWriter writer = new PrintWriter(new FileOutputStream(new File(filePath), true)))
            {
                writer.write("First Name: " + firstName + "\n");
                writer.write("Last Name: " + lastName + "\n");
                writer.write("Date of Birth: " + dob + "\n");
                writer.write("Email: " + email + "\n");
                writer.write("Phone Number: " + phoneNumber + "\n");
                writer.write("Pharmacy Information: " + pharmacyInfo + "\n"); 
                writer.write("Insurance ID: " + insuranceID + "\n"); 
                writer.write("Password: " + password + "\n");
                System.out.println("Account created successfully!");
                System.out.println("File path: " + filePath);
            }
            
            catch (IOException ex)
            {
                System.out.println("Error occurred while creating account: " + ex.getMessage());
            }

        });

        root.getChildren().addAll(grid);
        Scene scene = new Scene(root, 900, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}
