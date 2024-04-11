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

public class NurseLoginPage extends Application
{

    private TextField usernameField;
    private PasswordField passwordField;
    private Label errorLabel;

    @Override
    public void start(Stage primaryStage)
    {
        primaryStage.setTitle("Nurse Login");

        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);
        root.setSpacing(10);
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #cccccc, #f9b9aa);");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Label usernameLabel = new Label("Username:"); 		// label and text field for username
        usernameLabel.setFont(Font.font(20));
        usernameField = new TextField();
        grid.addRow(0, usernameLabel, usernameField);

        Label passwordLabel = new Label("Password:");		// label and text field for password
        passwordLabel.setFont(Font.font(20));
        passwordField = new PasswordField();
        grid.addRow(1, passwordLabel, passwordField);

        Button loginButton = new Button("Login");
        loginButton.setFont(Font.font(20));

     

        Button backButton = new Button("Back");
        backButton.setFont(Font.font(20));
        grid.add(backButton, 0, 2);

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        buttonBox.getChildren().addAll(loginButton);
        grid.add(buttonBox, 1, 2);

        errorLabel = new Label();			// error when information is incorrect
        errorLabel.setTextFill(Color.RED);
        grid.add(errorLabel, 1, 3);

        root.getChildren().addAll(grid);


        loginButton.setOnAction(e -> handleLogin(primaryStage));

        backButton.setOnAction(e ->
        {
            // click to go back to main menu
            Main main = new Main();
            main.start(primaryStage);
        });

        Scene scene = new Scene(root, 900, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void handleLogin(Stage primaryStage)
    {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // authenticate user by using the Database
        Database database = new Database();
        if (database.validateNurse(username, password))
        {
            // switch to the NursePortal scene
            NursePortal nursePortal = new NursePortal(username);
            nursePortal.start(primaryStage);
        }
        
        else
        {
            errorLabel.setText("Invalid username or password.");
        }
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}