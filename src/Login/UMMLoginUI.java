package Login;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Stage;
import javafx.concurrent.Task;
import Database.DatabaseConnection;
import java.sql.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;

public class UMMLoginUI extends Application {
    private TextField nimField;
    private PasswordField passField;
    private Button masukBtn;
    private ProgressIndicator loadingIndicator;

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
        Label logo = new Label("L@ser\nmyUMM Library");
        logo.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        logo.setTextFill(Color.WHITE);
        logo.setAlignment(Pos.CENTER);
        logo.setTextAlignment(TextAlignment.CENTER);

        Label title = new Label("Login Anggota\nPerpustakaan UMM");
        title.setFont(Font.font("Arial", FontWeight.NORMAL, 22));
        title.setTextFill(Color.WHITE);
        title.setTextAlignment(TextAlignment.CENTER);

        // Toggle Login/Register
        HBox toggleBox = new HBox(10);
        toggleBox.setAlignment(Pos.CENTER);

        Button loginBtn = new Button("Login");
        loginBtn.setPrefWidth(80);
        loginBtn.setStyle("-fx-background-color: #d4d8e0; -fx-text-fill: #5a5a5a; -fx-background-radius: 10; -fx-font-size: 15px;");
        loginBtn.setOnMouseEntered(e -> loginBtn.setStyle("-fx-background-color: #e0e7ef; -fx-text-fill: #2a3a5a; -fx-background-radius: 10; -fx-font-size: 15px;"));
        loginBtn.setOnMouseExited(e -> loginBtn.setStyle("-fx-background-color: #d4d8e0; -fx-text-fill: #5a5a5a; -fx-background-radius: 10; -fx-font-size: 15px;"));

        Button registerBtn = new Button("Register");
        registerBtn.setPrefWidth(80);
        registerBtn.setStyle("-fx-background-color: transparent; -fx-border-color: #d4d8e0; -fx-text-fill: #5a5a5a; -fx-background-radius: 10; -fx-border-radius: 10; -fx-font-size: 15px;");
        registerBtn.setOnMouseEntered(e -> registerBtn.setStyle("-fx-background-color: #e0e7ef; -fx-border-color: #d4d8e0; -fx-text-fill: #2a3a5a; -fx-background-radius: 10; -fx-border-radius: 10; -fx-font-size: 15px;"));
        registerBtn.setOnMouseExited(e -> registerBtn.setStyle("-fx-background-color: transparent; -fx-border-color: #d4d8e0; -fx-text-fill: #5a5a5a; -fx-background-radius: 10; -fx-border-radius: 10; -fx-font-size: 15px;"));
        
        // Add event handler for register button
        registerBtn.setOnAction(e -> {
            UMMRegisterPanelUI registerPanel = new UMMRegisterPanelUI();
            try {
                registerPanel.start(new Stage());
                primaryStage.close(); // Close the login window
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        toggleBox.getChildren().addAll(loginBtn, registerBtn);

        // Field NIM/Email
        nimField = new TextField();
        nimField.setPromptText("NIM/Email UMM");
        nimField.setMaxWidth(260);
        nimField.setStyle("-fx-background-radius: 10; -fx-font-size: 15px;");

        // Field Password
        passField = new PasswordField();
        passField.setPromptText("Password");
        passField.setMaxWidth(260);
        passField.setStyle("-fx-background-radius: 10; -fx-font-size: 15px;");

        // Loading indicator
        loadingIndicator = new ProgressIndicator();
        loadingIndicator.setVisible(false);
        loadingIndicator.setMaxSize(30, 30);
        loadingIndicator.setStyle("-fx-progress-color: white;");

        // Tombol Masuk
        masukBtn = new Button("Masuk");
        masukBtn.setPrefWidth(110);
        masukBtn.setStyle("-fx-background-color: #d4d8e0; -fx-text-fill: #5a5a5a; -fx-background-radius: 10; -fx-font-size: 15px;");
        masukBtn.setOnAction(e -> handleLogin());

        // Create HBox for button and loading indicator
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(loadingIndicator, masukBtn);

        // Use a GridPane for form fields and button for better alignment
        GridPane formGrid = new GridPane();
        formGrid.setHgap(0);
        formGrid.setVgap(18);
        formGrid.setAlignment(Pos.CENTER);
        formGrid.add(nimField, 0, 0);
        formGrid.add(passField, 0, 1);
        formGrid.add(buttonBox, 0, 2);
        GridPane.setHalignment(buttonBox, HPos.RIGHT);

        vbox.getChildren().clear();
        vbox.getChildren().addAll(logo, title, toggleBox, formGrid);

        borderPane.setCenter(vbox);

        // Footer alamat
        Label alamat = new Label("Jalan Raya Tlogomas No. 246, Jatimulyo, Lowokwaru, Malang, Jawa Timur, 65144");
        alamat.setFont(Font.font("Arial", FontWeight.NORMAL, 11));
        alamat.setTextFill(Color.WHITE);
        alamat.setAlignment(Pos.CENTER);

        VBox mainLayout = new VBox(10);
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.getChildren().addAll(borderPane, alamat);

        root.getChildren().add(mainLayout);

        // Scene dan stage
        Scene scene = new Scene(root, 600, 480);
        primaryStage.setTitle("Login Anggota Perpustakaan UMM");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void handleLogin() {
        if (nimField.getText().isEmpty() || passField.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Login Error");
            alert.setContentText("Please fill in all fields!");
            alert.showAndWait();
            return;
        }

        // Show loading indicator and disable button
        loadingIndicator.setVisible(true);
        masukBtn.setDisable(true);

        // Create a background task for login
        Task<Boolean> loginTask = new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {
                try {
                    Connection conn = DatabaseConnection.getConnection();
                    String query = "SELECT * FROM member WHERE (nim = ? OR email = ?) AND password = ?";
                    
                    PreparedStatement pstmt = conn.prepareStatement(query);
                    pstmt.setString(1, nimField.getText());
                    pstmt.setString(2, nimField.getText());
                    pstmt.setString(3, passField.getText());

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
            loadingIndicator.setVisible(false);
            masukBtn.setDisable(false);
            
            if (loginTask.getValue()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText("Login Successful");
                alert.setContentText("Welcome to UMM Library!");
                alert.showAndWait();
                
                // TODO: Open main application window
                // For now, just clear the fields
                nimField.clear();
                passField.clear();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Login Failed");
                alert.setContentText("Invalid NIM/Email or password!");
                alert.showAndWait();
            }
        });

        loginTask.setOnFailed(e -> {
            loadingIndicator.setVisible(false);
            masukBtn.setDisable(false);
            
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Login Failed");
            alert.setContentText("An error occurred during login. Please try again.");
            alert.showAndWait();
        });

        // Start the task
        new Thread(loginTask).start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
