package Admin;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.*;
import javafx.stage.Stage;
import javafx.concurrent.Task;
import Database.DatabaseConnection;
import java.sql.*;
import Dashboard.AdminDashboardUI;
import javafx.scene.text.FontPosture;

public class AdminLoginUI extends Application {
    private TextField usernameField;
    private PasswordField passField;
    private Button loginBtn;

    @Override
    public void start(Stage primaryStage) {
        // Root pane with image background
        StackPane root = new StackPane();
        
        // Create background image
        Image backgroundImage = new Image(getClass().getResourceAsStream("/heli.jpeg"));
        ImageView backgroundImageView = new ImageView(backgroundImage);
        backgroundImageView.setFitWidth(primaryStage.getWidth());
        backgroundImageView.setFitHeight(primaryStage.getHeight());
        backgroundImageView.setPreserveRatio(false);
        
        // Add semi-transparent overlay
        Rectangle overlay = new Rectangle(primaryStage.getWidth(), primaryStage.getHeight());
        overlay.setFill(Color.rgb(158, 180, 199, 0.8)); // Using the original color with transparency
        
        // Make overlay and image resize with window
        overlay.widthProperty().bind(primaryStage.widthProperty());
        overlay.heightProperty().bind(primaryStage.heightProperty());
        backgroundImageView.fitWidthProperty().bind(primaryStage.widthProperty());
        backgroundImageView.fitHeightProperty().bind(primaryStage.heightProperty());
        
        root.getChildren().addAll(backgroundImageView, overlay);

        // BorderPane untuk membungkus konten utama
        BorderPane borderPane = new BorderPane();
        borderPane.setPadding(new Insets(30, 40, 30, 40));
        borderPane.setMaxWidth(440);
        borderPane.setMaxHeight(380);
        borderPane.setBackground(new Background(new BackgroundFill(Color.web("#b0c4d6"), new CornerRadii(10), Insets.EMPTY)));
        borderPane.setBorder(new Border(new BorderStroke(Color.WHITE, BorderStrokeStyle.SOLID, new CornerRadii(10), new BorderWidths(2))));

        // VBox untuk konten login
        VBox vbox = new VBox(18);
        vbox.setAlignment(Pos.CENTER);

        // Logo dan judul
        Image logoImage = new Image(getClass().getResourceAsStream("/laser.jpg"));
        ImageView logoImageView = new ImageView(logoImage);
        logoImageView.setFitWidth(200);
        logoImageView.setPreserveRatio(true);

        Label title = new Label("Login Admin\nPerpustakaan UMM");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 24));
        title.setTextFill(Color.WHITE);
        title.setTextAlignment(TextAlignment.CENTER);

        // Field Username
        usernameField = new TextField();
        usernameField.setPromptText("Username");
        usernameField.setMaxWidth(260);
        usernameField.setStyle("-fx-background-radius: 10; -fx-font-size: 15px; -fx-font-family: 'Segoe UI';");

        // Field Password
        passField = new PasswordField();
        passField.setPromptText("Password");
        passField.setMaxWidth(260);
        passField.setStyle("-fx-background-radius: 10; -fx-font-size: 15px; -fx-font-family: 'Segoe UI';");

        // Login button
        loginBtn = new Button("Masuk");
        loginBtn.setPrefWidth(110);
        loginBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-background-radius: 10; -fx-font-size: 15px; -fx-font-family: 'Segoe UI';");
        loginBtn.setOnAction(e -> handleLogin());

        // Create HBox for button
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(loginBtn);

        // Use a GridPane for form fields and button for better alignment
        GridPane formGrid = new GridPane();
        formGrid.setHgap(0);
        formGrid.setVgap(18);
        formGrid.setAlignment(Pos.CENTER);
        formGrid.add(usernameField, 0, 0);
        formGrid.add(passField, 0, 1);
        formGrid.add(buttonBox, 0, 2);
        GridPane.setHalignment(buttonBox, HPos.RIGHT);

        vbox.getChildren().addAll(logoImageView, title, formGrid);

        borderPane.setCenter(vbox);

        // Footer alamat
        Label alamat = new Label("Jalan Raya Tlogomas No. 246, Jatimulyo, Lowokwaru, Malang, Jawa Timur, 65144");
        alamat.setFont(Font.font("Segoe UI", FontPosture.ITALIC, 11));
        alamat.setTextFill(Color.WHITE);
        alamat.setAlignment(Pos.CENTER);

        VBox mainLayout = new VBox(10);
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.getChildren().addAll(borderPane, alamat);

        root.getChildren().add(mainLayout);

        // Scene dan stage
        Scene scene = new Scene(root, 600, 480);
        primaryStage.setTitle("Login Admin Perpustakaan UMM");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void handleLogin() {
        if (usernameField.getText().isEmpty() || passField.getText().isEmpty()) {
            showAlert("Error", "Please fill in all fields!");
            return;
        }

        // Create a background task for login
        Task<Boolean> loginTask = new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {
                try {
                    Connection conn = DatabaseConnection.getConnection();
                    String query = "SELECT * FROM admin WHERE username = ? AND password = ?";
                    
                    PreparedStatement pstmt = conn.prepareStatement(query);
                    pstmt.setString(1, usernameField.getText());
                    pstmt.setString(2, passField.getText());

                    ResultSet rs = pstmt.executeQuery();
                    boolean success = rs.next();
                    
                    rs.close();
                    pstmt.close();
                    
                    return success;
                } catch (SQLException e) {
                    e.printStackTrace();
                    return false;
                }
            }
        };

        // Handle task completion
        loginTask.setOnSucceeded(e -> {
            if (loginTask.getValue()) {
                showAlert("Success", "Login successful!");
                // Launch AdminDashboardUI
                AdminDashboardUI dashboard = new AdminDashboardUI();
                dashboard.start(new Stage()); // Start the dashboard in a new stage
                ((Stage) usernameField.getScene().getWindow()).close(); // Close the login window
            } else {
                showAlert("Error", "Invalid username or password!");
            }
        });

        loginTask.setOnFailed(e -> {
            showAlert("Error", "An error occurred during login. Please try again.");
        });

        // Start the task
        new Thread(loginTask).start();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}