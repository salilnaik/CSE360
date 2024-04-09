package application;

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
import java.io.FileWriter;
import java.io.IOException;

public class CreateAccountPage extends Application
{

    private TextField firstNameField;
    private TextField lastNameField;
    private DatePicker dobPicker;
    private PasswordField passwordField;
    private PasswordField confirmPasswordField;
    private TextField phoneNumberField;
    private Label errorLabel;

    @Override
    public void start(Stage primaryStage)
    {
        primaryStage.setTitle("Create Account");

        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);
        root.setSpacing(10);
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #cccccc, #f9b9aa);"); // set background

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Label firstNameLabel = new Label("First Name:");
        firstNameLabel.setFont(Font.font(20));
        grid.add(firstNameLabel, 0, 0);

        firstNameField = new TextField();
        grid.add(firstNameField, 1, 0);

        Label lastNameLabel = new Label("Last Name:");
        lastNameLabel.setFont(Font.font(20));
        grid.add(lastNameLabel, 0, 1);

        lastNameField = new TextField();
        grid.add(lastNameField, 1, 1);

        Label dobLabel = new Label("Date of Birth:");
        dobLabel.setFont(Font.font(20));
        grid.add(dobLabel, 0, 2);

        dobPicker = new DatePicker();
        grid.add(dobPicker, 1, 2);

        Label passwordLabel = new Label("Password:");
        passwordLabel.setFont(Font.font(20));
        grid.add(passwordLabel, 0, 3);

        passwordField = new PasswordField();
        grid.add(passwordField, 1, 3);

        Label confirmPasswordLabel = new Label("Confirm Password:");
        confirmPasswordLabel.setFont(Font.font(20));
        grid.add(confirmPasswordLabel, 0, 4);

        confirmPasswordField = new PasswordField();
        grid.add(confirmPasswordField, 1, 4);

        Label phoneNumberLabel = new Label("Phone Number:");
        phoneNumberLabel.setFont(Font.font(20));
        grid.add(phoneNumberLabel, 0, 5);

        phoneNumberField = new TextField();
        grid.add(phoneNumberField, 1, 5);

        Button backButton = new Button("Back");
        backButton.setFont(Font.font(20));
        grid.add(backButton, 0, 6);

        Button createAccountButton = new Button("Create Account");
        createAccountButton.setFont(Font.font(20));
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().add(createAccountButton);
        grid.add(hbBtn, 1, 6);

        errorLabel = new Label();
        errorLabel.setTextFill(Color.RED);
        grid.add(errorLabel, 1, 7);

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
            String password = passwordField.getText();
            String confirmPassword = confirmPasswordField.getText();
            String phoneNumber = phoneNumberField.getText();
            
            // checking if passwords match
            if (!password.equals(confirmPassword))
            {
                errorLabel.setText("Passwords do not match.");
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
            
            String filePath = directoryPath + username + ".txt";
            try (FileWriter writer = new FileWriter(filePath, true))
            {
                writer.write(password + "\n");
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

    private String constructUsername(String firstName, String lastName, String dob)
    {
        // construct the username using the specified format
        String firstNameInitial = firstName.substring(0, 1).toLowerCase();
        String lastNameWithoutSpaces = lastName.replace(" ", "").toLowerCase();
        String yearOfBirth = dob.substring(0, 4);
        return firstNameInitial + lastNameWithoutSpaces + yearOfBirth;
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}
