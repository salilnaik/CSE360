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

public class PatientLoginPage extends Application {

    private TextField usernameField;
    private PasswordField passwordField;
    private Label errorLabel;

    @Override
    public void start(Stage primaryStage) {
        try {
            primaryStage.setTitle("Patient Login");

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

            Button createAccountButton = new Button("Create Account");
            createAccountButton.setFont(Font.font(20));
            grid.add(createAccountButton, 1, 3);

            Button backButton = new Button("Back");
            backButton.setFont(Font.font(20));
            grid.add(backButton, 0, 3);

            errorLabel = new Label();
            errorLabel.setTextFill(Color.RED);
            grid.add(errorLabel, 1, 4);

            HBox buttonBox = new HBox(10);
            buttonBox.setAlignment(Pos.CENTER_RIGHT);
            buttonBox.getChildren().addAll(createAccountButton, loginButton);
            grid.add(buttonBox, 1, 3);

            createAccountButton.setOnAction(e -> {
                // Switch to the CreateAccountPage
                Stage createAccountStage = new Stage();
                CreateAccountPage createAccountPage = new CreateAccountPage();
                createAccountPage.start(createAccountStage);
                primaryStage.hide();
            });

            loginButton.setOnAction(e -> {
                String username = usernameField.getText();
                String password = passwordField.getText();

                // Authenticate user using Database class
                Database database = new Database();
                if (database.validatePatient(username, password)) {
                    // Redirect to patient dashboard or next page
                    System.out.println("Login successful");
                } else {
                    errorLabel.setText("Invalid username or password.");
                }
            });

            backButton.setOnAction(e -> {
                // Go back to the main menu
                Main main = new Main();
                main.start(primaryStage);
            });

            root.getChildren().addAll(grid);
            Scene scene = new Scene(root, 900, 600);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
