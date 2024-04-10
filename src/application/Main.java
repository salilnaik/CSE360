package application;

import java.io.FileInputStream; 
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button; 
import javafx.geometry.Pos;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			VBox root = new VBox();
			root.setAlignment(Pos.CENTER);
			root.setSpacing(10);
			root.setStyle("-fx-background-color: linear-gradient(to bottom, #cccccc, #f9b9aa);");
			
			Text title = new Text("Welcome to");
			title.setFont(Font.loadFont("file:resources/NovaSquare-Regular.ttf", 60));
			Text signInText = new Text("Sign in as:");
			Image logo = new Image(new FileInputStream("resources/logo.png"));
			ImageView view =  new ImageView(logo);
			view.setFitHeight(181); 
		    view.setFitWidth(420); 
		    HBox buttons = new HBox();
		    Button doctorButton = new Button("Doctor");
		    doctorButton.setMinSize(100, 50);
		    Button nurseButton = new Button("Nurse");
		    nurseButton.setMinSize(100, 50);
		    Button patientButton = new Button("Patient");
		    patientButton.setMinSize(100, 50);
		    buttons.getChildren().addAll(doctorButton, nurseButton, patientButton);
		    buttons.setAlignment(Pos.CENTER);
		    buttons.setSpacing(20);
			root.getChildren().addAll(title, view, signInText, buttons);
			Scene scene = new Scene(root,900,600);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
			
			patientButton.setOnAction(e ->
	            {
	            	// go to patient login page
	            	Stage loginStage = new Stage();
	                PatientLoginPage loginPage = new PatientLoginPage();
	                loginPage.start(loginStage);
	                primaryStage.hide();
	            });
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
