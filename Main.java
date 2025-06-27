package Login;

import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.scene.control.ButtonBar;
import Admin.AdminLoginUI;
import Login.UMMLoginUI;

public class Main {
    public static void main(String[] args) {
        Application.launch(LoginChoiceUI.class, args);
    }

    // Make LoginChoiceUI a static inner class
    public static class LoginChoiceUI extends Application {
        @Override
        public void start(Stage primaryStage) {
            // Create an alert dialog for login choice
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Login Choice");
            alert.setHeaderText("Choose Login Type");
            alert.setContentText("Please select your login type:");

            // Add buttons for Admin and User login
            ButtonType adminButton = new ButtonType("Admin Login");
            ButtonType userButton = new ButtonType("User Login");
            ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

            alert.getButtonTypes().setAll(adminButton, userButton, cancelButton);

            // Show the dialog and wait for a response
            alert.showAndWait().ifPresent(response -> {
                if (response == adminButton) {
                    // Launch AdminLoginUI without calling main()
                    try {
                        AdminLoginUI adminLoginUI = new AdminLoginUI();
                        adminLoginUI.start(new Stage());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (response == userButton) {
                    // Launch UMMLoginUI without calling main()
                    try {
                        UMMLoginUI userLoginUI = new UMMLoginUI();
                        userLoginUI.start(new Stage());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    // Handle cancel action if needed
                    System.out.println("Login canceled.");
                }
            });
        }
    }
} 