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

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class DoctorLoginPage extends Application
{

    private TextField usernameField;
    private PasswordField passwordField;
    private Label errorLabel;

    @Override
    public void start(Stage primaryStage)
    {
        try {
            primaryStage.setTitle("Doctor Login");

            VBox root = new VBox();
            root.setAlignment(Pos.CENTER);
            root.setSpacing(10);
            root.setStyle("-fx-background-color: linear-gradient(to bottom, #cccccc, #f9b9aa);");	

            GridPane grid = new GridPane();
            grid.setAlignment(Pos.CENTER);
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(25, 25, 25, 25));

            Label usernameLabel = new Label("Username:");
            usernameLabel.setFont(Font.font(20));
            grid.add(usernameLabel, 0, 0);

            usernameField = new TextField();
            grid.add(usernameField, 1, 0);

            Label passwordLabel = new Label("Password:");
            passwordLabel.setFont(Font.font(20));
            grid.add(passwordLabel, 0, 1);

            passwordField = new PasswordField();
            grid.add(passwordField, 1, 1);

            Button loginButton = new Button("Login");
            loginButton.setFont(Font.font(20));
            grid.add(loginButton, 1, 2);

            Button backButton = new Button("Back");
            backButton.setFont(Font.font(20));
            grid.add(backButton, 0, 2);

            errorLabel = new Label();
            errorLabel.setTextFill(Color.RED);
            grid.add(errorLabel, 1, 3);

            backButton.setOnAction(e ->
            {
                // go back to main menu
                Main main = new Main();
                main.start(primaryStage);
            });

            loginButton.setOnAction(e ->
            {
                String username = usernameField.getText();
                String password = passwordField.getText();

                // authenticate user here
                boolean isValid = submit(username, password);
                if (isValid)
                {
                    // redirect to DoctorPortal or any other page
                    DoctorPortal doctorPortal = new DoctorPortal(username);
                    doctorPortal.start(primaryStage);
                }
                
                else
                {
                    errorLabel.setText("Invalid username or password.");
                }
            });

            root.getChildren().addAll(grid);
            Scene scene = new Scene(root, 900, 600);
            primaryStage.setScene(scene);
            primaryStage.show();
        }
        
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    
    private boolean submit(String username, String password)
    {
        String filePath = "doctor_login/" + username + ".txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath)))
        {
            String line = reader.readLine();
            return line != null && line.equals(password);
        }
        
        catch (IOException e)
        {
            e.printStackTrace();
            return false;
        }
    }


    public static void main(String[] args)
    {
        launch(args);
    }
}
