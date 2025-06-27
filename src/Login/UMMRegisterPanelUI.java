package Login;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.*;
import javafx.stage.Stage;
import java.util.*;
import java.sql.*;
import javafx.concurrent.Task;
import Database.DatabaseConnection;

public class UMMRegisterPanelUI extends Application {
    private Map<String, List<String>> fakultasProdiMap;
    private ComboBox<String> fakultasComboBox;
    private ComboBox<String> prodiComboBox;
    private TextField namaField;
    private TextField nimField;
    private TextField emailField;
    private PasswordField passwordField;
    private PasswordField confirmField;
    private TextField alamatField;
    private TextField nohpField;
    private ProgressIndicator loadingIndicator;
    private Button masukBtn;

    public UMMRegisterPanelUI() {
        initializeFakultasProdiMap();
    }

    private void initializeFakultasProdiMap() {
        fakultasProdiMap = new HashMap<>();
        
        // FEB
        fakultasProdiMap.put("FEB", Arrays.asList(
            "Manajemen",
            "Akuntansi",
            "Ekonomi Pembangunan"
        ));
        
        // FISIP
        fakultasProdiMap.put("FISIP", Arrays.asList(
            "Ilmu Komunikasi",
            "Ilmu Pemerintahan",
            "Hubungan Internasional",
            "Kesejahteraan Sosial",
            "Sosiologi"
        ));
        
        // FKIP
        fakultasProdiMap.put("FKIP", Arrays.asList(
            "Pendidikan Matematika",
            "Pendidikan Biologi",
            "Pendidikan Bahasa Indonesia",
            "Pendidikan Pancasila dan Kewarganegaraan",
            "Pendidikan Bahasa Inggris",
            "Pendidikan Guru Sekolah Dasar"
        ));
        
        // PSIKOLOGI
        fakultasProdiMap.put("FAKULTAS PSIKOLOGI", Arrays.asList("Psikologi"));
        
        // FH
        fakultasProdiMap.put("FH", Arrays.asList("Ilmu Hukum"));
        
        // FPP
        fakultasProdiMap.put("FPP", Arrays.asList(
            "Agroteknologi/Agronomi",
            "Agribisnis",
            "Teknologi Pangan",
            "Kehutanan",
            "Peternakan",
            "Akuakultur"
        ));
        
        // FT
        fakultasProdiMap.put("FT", Arrays.asList(
            "Teknik Mesin",
            "Teknik Sipil",
            "Teknik Industri",
            "Informatika",
            "Teknik Elektro"
        ));
        
        // FAI
        fakultasProdiMap.put("FAI", Arrays.asList(
            "Pendidikan Agama Islam",
            "Hukum Keluarga Islam Ahwal Syakhshiyyah",
            "Ekonomi Syariah",
            "Pendidikan Bahasa Arab"
        ));
        
        // FIKES
        fakultasProdiMap.put("FIKES", Arrays.asList(
            "Ilmu Keperawatan",
            "Farmasi",
            "Fisioterapi"
        ));
        
        // FK
        fakultasProdiMap.put("FK", Arrays.asList(
            "Kedokteran"
        ));
        
        // VOKASI
        fakultasProdiMap.put("VOKASI", Arrays.asList(
            "D3 Ilmu Keperawatan",
            "D3 Teknologi Elektronika",
            "D3 Keuangan dan Perbankan",
            "D3 Bisnis Properti",
            "D3 Agribisnis Unggas"
        ));
    }

    private void registerMember() {
        // Validate input fields
        if (namaField.getText().isEmpty() || nimField.getText().isEmpty() || 
            emailField.getText().isEmpty() || passwordField.getText().isEmpty() || 
            confirmField.getText().isEmpty() || alamatField.getText().isEmpty() || 
            nohpField.getText().isEmpty() || fakultasComboBox.getValue() == null || 
            prodiComboBox.getValue() == null) {
            
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Registration Error");
            alert.setContentText("Please fill in all fields!");
            alert.showAndWait();
            return;
        }

        // Validate password match
        if (!passwordField.getText().equals(confirmField.getText())) {
            passwordField.setStyle("-fx-background-radius: 10; -fx-font-size: 15px; -fx-effect: dropshadow(three-pass-box, red, 8, 0, 0, 0);");
            confirmField.setStyle("-fx-background-radius: 10; -fx-font-size: 15px; -fx-effect: dropshadow(three-pass-box, red, 8, 0, 0, 0);");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Password tidak sama");
            alert.setContentText("Password dan Confirm harus sama!");
            alert.showAndWait();
            return;
        }

        // Show loading indicator and disable button
        loadingIndicator.setVisible(true);
        masukBtn.setDisable(true);

        // Create a background task for registration
        Task<Void> registrationTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                try {
                    Connection conn = DatabaseConnection.getConnection();
                    String query = "INSERT INTO member (nama, alamat, no_hp, email, password, fakultas, prodi, nim) " +
                                  "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                    
                    PreparedStatement pstmt = conn.prepareStatement(query);
                    pstmt.setString(1, namaField.getText());
                    pstmt.setString(2, alamatField.getText());
                    pstmt.setString(3, nohpField.getText());
                    pstmt.setString(4, emailField.getText());
                    pstmt.setString(5, passwordField.getText());
                    pstmt.setString(6, fakultasComboBox.getValue());
                    pstmt.setString(7, prodiComboBox.getValue());
                    pstmt.setString(8, nimField.getText());

                    int result = pstmt.executeUpdate();
                    
                    Platform.runLater(() -> {
                        if (result > 0) {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Success");
                            alert.setHeaderText("Registration Successful");
                            alert.setContentText("Your account has been created successfully!");
                            alert.showAndWait();
                            
                            // Clear all fields
                            clearFields();
                            
                            // Switch to login screen
                            UMMLoginUI loginUI = new UMMLoginUI();
                            try {
                                loginUI.start(new Stage());
                                ((Stage) namaField.getScene().getWindow()).close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    
                    pstmt.close();
                    
                } catch (SQLException e) {
                    Platform.runLater(() -> {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Database Error");
                        alert.setHeaderText("Registration Failed");
                        alert.setContentText("Error: " + e.getMessage());
                        alert.showAndWait();
                    });
                    e.printStackTrace();
                }
                return null;
            }
        };

        // Handle task completion
        registrationTask.setOnSucceeded(e -> {
            loadingIndicator.setVisible(false);
            masukBtn.setDisable(false);
        });

        registrationTask.setOnFailed(e -> {
            loadingIndicator.setVisible(false);
            masukBtn.setDisable(false);
        });

        // Start the task
        new Thread(registrationTask).start();
    }

    private void clearFields() {
        namaField.clear();
        nimField.clear();
        emailField.clear();
        passwordField.clear();
        confirmField.clear();
        alamatField.clear();
        nohpField.clear();
        fakultasComboBox.setValue(null);
        prodiComboBox.setValue(null);
    }

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
        overlay.setFill(Color.rgb(158, 180, 199, 0.8));
        // Make overlay and image resize with window
        overlay.widthProperty().bind(primaryStage.widthProperty());
        overlay.heightProperty().bind(primaryStage.heightProperty());
        backgroundImageView.fitWidthProperty().bind(primaryStage.widthProperty());
        backgroundImageView.fitHeightProperty().bind(primaryStage.heightProperty());
        root.getChildren().addAll(backgroundImageView, overlay);

        // Panel utama dengan border putih, rounded, drop shadow, and glow
        VBox panel = new VBox(18);
        panel.setPadding(new Insets(36, 36, 36, 36));
        panel.setAlignment(Pos.TOP_CENTER);
        panel.setBackground(new Background(new BackgroundFill(Color.web("#b0c4d6", 0.92), new CornerRadii(22), Insets.EMPTY)));
        panel.setBorder(new Border(new BorderStroke(Color.web("#e0e7ef"), BorderStrokeStyle.SOLID, new CornerRadii(22), new BorderWidths(2))));
        DropShadow dropShadow = new DropShadow(18, Color.rgb(80, 120, 180, 0.18));
        panel.setEffect(dropShadow);

        // Logo dan judul
        VBox header = new VBox(2);
        header.setAlignment(Pos.CENTER);
        Label logo = new Label("L@ser");
        logo.setFont(Font.font("Arial", FontWeight.BOLD, 15));
        logo.setTextFill(Color.web("#3a4a6b"));

        Label myumm = new Label("myUMM Library");
        myumm.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        myumm.setTextFill(Color.web("#3a4a6b"));

        Label title = new Label("Registrasi Anggota\nPerpustakaan UMM");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 26));
        title.setTextFill(Color.web("#2a3a5a"));
        title.setTextAlignment(TextAlignment.CENTER);

        header.getChildren().addAll(logo, myumm, title);

        // Toggle Login/Register
        HBox toggleBox = new HBox(10);
        toggleBox.setAlignment(Pos.CENTER);

        Button loginBtn = new Button("Login");
        loginBtn.setPrefWidth(100);
        loginBtn.setStyle("-fx-background-color: transparent; -fx-border-color: #d4d8e0; -fx-text-fill: #5a5a5a; -fx-background-radius: 10; -fx-border-radius: 10; -fx-font-size: 15px;");
        loginBtn.setOnMouseEntered(e -> loginBtn.setStyle("-fx-background-color: #e0e7ef; -fx-border-color: #d4d8e0; -fx-text-fill: #2a3a5a; -fx-background-radius: 10; -fx-border-radius: 10; -fx-font-size: 15px;"));
        loginBtn.setOnMouseExited(e -> loginBtn.setStyle("-fx-background-color: transparent; -fx-border-color: #d4d8e0; -fx-text-fill: #5a5a5a; -fx-background-radius: 10; -fx-border-radius: 10; -fx-font-size: 15px;"));

        Button registerBtn = new Button("Register");
        registerBtn.setPrefWidth(100);
        registerBtn.setStyle("-fx-background-color: #ede6e6; -fx-text-fill: #5a5a5a; -fx-background-radius: 10; -fx-font-size: 15px;");
        registerBtn.setOnMouseEntered(e -> registerBtn.setStyle("-fx-background-color: #e0e7ef; -fx-text-fill: #2a3a5a; -fx-background-radius: 10; -fx-font-size: 15px;"));
        registerBtn.setOnMouseExited(e -> registerBtn.setStyle("-fx-background-color: #ede6e6; -fx-text-fill: #5a5a5a; -fx-background-radius: 10; -fx-font-size: 15px;"));

        // Add event handler for login button
        loginBtn.setOnAction(e -> {
            UMMLoginUI loginUI = new UMMLoginUI();
            try {
                loginUI.start(new Stage());
                primaryStage.close(); // Close the register window
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        toggleBox.getChildren().addAll(loginBtn, registerBtn);

        // GridPane untuk form
        GridPane grid = new GridPane();
        grid.setHgap(18);
        grid.setVgap(14);
        grid.setPadding(new Insets(18, 0, 0, 0));
        grid.setAlignment(Pos.CENTER);

        // Kolom kiri
        Label namaLbl = new Label("Nama :");
        namaLbl.setTextFill(Color.web("#2a3a5a"));
        namaLbl.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 14));
        namaLbl.setEffect(new DropShadow(1, Color.rgb(255,255,255,0.15)));
        namaField = new TextField();
        namaField.setPrefWidth(260);
        namaField.setStyle("-fx-background-radius: 10; -fx-font-size: 15px;");

        Label nimLbl = new Label("NIM :");
        nimLbl.setTextFill(Color.web("#2a3a5a"));
        nimLbl.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 14));
        nimLbl.setEffect(new DropShadow(1, Color.rgb(255,255,255,0.15)));
        nimField = new TextField();
        nimField.setPrefWidth(260);
        nimField.setStyle("-fx-background-radius: 10; -fx-font-size: 15px;");

        Label fakultasLbl = new Label("Fakultas :");
        fakultasLbl.setTextFill(Color.web("#2a3a5a"));
        fakultasLbl.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 14));
        fakultasLbl.setEffect(new DropShadow(1, Color.rgb(255,255,255,0.15)));
        fakultasComboBox = new ComboBox<>();
        fakultasComboBox.getItems().addAll(fakultasProdiMap.keySet());
        fakultasComboBox.setPromptText("Pilih Fakultas");
        fakultasComboBox.setPrefWidth(260);
        fakultasComboBox.setStyle("-fx-background-radius: 10; -fx-font-size: 15px;");

        Label emailLbl = new Label("Email :");
        emailLbl.setTextFill(Color.web("#2a3a5a"));
        emailLbl.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 14));
        emailLbl.setEffect(new DropShadow(1, Color.rgb(255,255,255,0.15)));
        emailField = new TextField();
        emailField.setPrefWidth(260);
        emailField.setStyle("-fx-background-radius: 10; -fx-font-size: 15px;");

        Label passwordLbl = new Label("Password :");
        passwordLbl.setTextFill(Color.web("#2a3a5a"));
        passwordLbl.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 14));
        passwordLbl.setEffect(new DropShadow(1, Color.rgb(255,255,255,0.15)));
        passwordField = new PasswordField();
        passwordField.setPrefWidth(260);
        passwordField.setStyle("-fx-background-radius: 10; -fx-font-size: 15px;");

        // Real-time validation for password match
        passwordField.textProperty().addListener((obs, oldVal, newVal) -> validatePasswordMatch());

        Label alamatLbl = new Label("Alamat :");
        alamatLbl.setTextFill(Color.web("#2a3a5a"));
        alamatLbl.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 14));
        alamatLbl.setEffect(new DropShadow(1, Color.rgb(255,255,255,0.15)));
        alamatField = new TextField();
        alamatField.setPrefWidth(260);
        alamatField.setStyle("-fx-background-radius: 10; -fx-font-size: 15px;");

        // Kolom kanan
        Label prodiLbl = new Label("Prodi :");
        prodiLbl.setTextFill(Color.web("#2a3a5a"));
        prodiLbl.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 14));
        prodiLbl.setEffect(new DropShadow(1, Color.rgb(255,255,255,0.15)));
        prodiComboBox = new ComboBox<>();
        prodiComboBox.setPromptText("Pilih Prodi");
        prodiComboBox.setPrefWidth(260);
        prodiComboBox.setStyle("-fx-background-radius: 10; -fx-font-size: 15px;");

        Label nohpLbl = new Label("No. HP :");
        nohpLbl.setTextFill(Color.web("#2a3a5a"));
        nohpLbl.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 14));
        nohpLbl.setEffect(new DropShadow(1, Color.rgb(255,255,255,0.15)));
        nohpField = new TextField();
        nohpField.setPrefWidth(260);
        nohpField.setStyle("-fx-background-radius: 10; -fx-font-size: 15px;");

        Label confirmLbl = new Label("Confirm :");
        confirmLbl.setTextFill(Color.web("#2a3a5a"));
        confirmLbl.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 14));
        confirmLbl.setEffect(new DropShadow(1, Color.rgb(255,255,255,0.15)));
        confirmField = new PasswordField();
        confirmField.setPrefWidth(260);
        confirmField.setStyle("-fx-background-radius: 10; -fx-font-size: 15px;");
        confirmField.textProperty().addListener((obs, oldVal, newVal) -> validatePasswordMatch());

        // Add listener to fakultasComboBox
        fakultasComboBox.setOnAction(e -> {
            String selectedFakultas = fakultasComboBox.getValue();
            if (selectedFakultas != null) {
                prodiComboBox.getItems().clear();
                prodiComboBox.getItems().addAll(fakultasProdiMap.get(selectedFakultas));
            }
        });

        // Penempatan grid
        grid.add(namaLbl, 0, 0);      grid.add(namaField, 1, 0, 2, 1);
        grid.add(nimLbl, 0, 1);       grid.add(nimField, 1, 1, 2, 1);
        grid.add(fakultasLbl, 0, 2);  grid.add(fakultasComboBox, 1, 2);
        grid.add(prodiLbl, 2, 2);     grid.add(prodiComboBox, 3, 2);
        grid.add(emailLbl, 0, 3);     grid.add(emailField, 1, 3);
        grid.add(nohpLbl, 2, 3);      grid.add(nohpField, 3, 3);
        grid.add(passwordLbl, 0, 4);  grid.add(passwordField, 1, 4);
        grid.add(confirmLbl, 2, 4);   grid.add(confirmField, 3, 4);
        grid.add(alamatLbl, 0, 5);    grid.add(alamatField, 1, 5, 2, 1);

        // Create loading indicator
        loadingIndicator = new ProgressIndicator();
        loadingIndicator.setVisible(false);
        loadingIndicator.setMaxSize(50, 50);
        loadingIndicator.setStyle("-fx-progress-color: white;");

        // Tombol Masuk rata kanan bawah
        masukBtn = new Button("Daftar");
        masukBtn.setPrefWidth(100);
        masukBtn.setStyle("-fx-background-color: #ede6e6; -fx-text-fill: #5a5a5a; -fx-background-radius: 10;");
        masukBtn.setOnAction(e -> registerMember());

        // Create HBox for button and loading indicator
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        buttonBox.getChildren().addAll(loadingIndicator, masukBtn);
        grid.add(buttonBox, 3, 6);
        GridPane.setHalignment(buttonBox, HPos.RIGHT);

        // Panel isi
        panel.getChildren().addAll(header, toggleBox, grid);

        // Footer alamat
        Label alamat = new Label("Jalan Raya Tlogomas No. 246, Jatimulyo, Lowokwaru, Malang, Jawa Timur, 65144");
        alamat.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        alamat.setTextFill(Color.WHITE);
        alamat.setAlignment(Pos.CENTER);

        VBox mainLayout = new VBox(18);
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.getChildren().addAll(panel, alamat);

        root.getChildren().add(mainLayout);

        // Scene dan stage
        Scene scene = new Scene(root, 700, 520);
        primaryStage.setTitle("Registrasi Anggota Perpustakaan UMM");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void validatePasswordMatch() {
        String pass = passwordField.getText();
        String confirm = confirmField.getText();
        if (!confirm.isEmpty() && !pass.equals(confirm)) {
            passwordField.setStyle("-fx-background-radius: 10; -fx-font-size: 15px; -fx-effect: dropshadow(three-pass-box, red, 8, 0, 0, 0);");
            confirmField.setStyle("-fx-background-radius: 10; -fx-font-size: 15px; -fx-effect: dropshadow(three-pass-box, red, 8, 0, 0, 0);");
        } else {
            passwordField.setStyle("-fx-background-radius: 10; -fx-font-size: 15px;");
            confirmField.setStyle("-fx-background-radius: 10; -fx-font-size: 15px;");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}