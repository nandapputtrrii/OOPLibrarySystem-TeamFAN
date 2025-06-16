package Dashboard;
import javafx.application.Application;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.*;
import javafx.stage.Stage;
import java.sql.*;
import Database.DatabaseConnection;
import Model.Member;

public class StudentDashboardUI extends Application {
    public Member member;

    @Override
    public void start(Stage primaryStage) {
        // Sidebar
        VBox sidebar = new VBox(20);
        sidebar.setPadding(new Insets(0, 0, 0, 0));
        sidebar.setPrefWidth(220);
        sidebar.setStyle("-fx-background-color: #7ea6c7;");

        // Logo
        VBox logoBox = new VBox();
        logoBox.setAlignment(Pos.CENTER);
        logoBox.setPadding(new Insets(30, 0, 30, 0));
        Label logo1 = new Label("L@ser");
        logo1.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        logo1.setTextFill(Color.WHITE);
        Label logo2 = new Label("myUMM Library");
        logo2.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        logo2.setTextFill(Color.WHITE);
        logoBox.getChildren().addAll(logo1, logo2);

        // Menu
        VBox menuBox = new VBox(0);
        menuBox.setPadding(new Insets(0, 0, 0, 0));
        String[] menus = {"Profile", "Borrow Book", "Return Book", "Search Book"};
        for (String m : menus) {
            Button btn = new Button(m);
            btn.setPrefWidth(220);
            btn.setAlignment(Pos.CENTER_LEFT);
            btn.setPadding(new Insets(18, 30, 18, 30));
            btn.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 16px; -fx-border-width: 0 0 1 0; -fx-border-color: #bcd2e8;");
            btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: #a2c3de; -fx-text-fill: #2c3e50; -fx-font-size: 16px; -fx-border-width: 0 0 1 0; -fx-border-color: #bcd2e8;"));
            btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 16px; -fx-border-width: 0 0 1 0; -fx-border-color: #bcd2e8;"));
            menuBox.getChildren().add(btn);
        }
        sidebar.getChildren().addAll(logoBox, menuBox);

        // Header
        HBox header = new HBox();
        header.setStyle("-fx-background-color: #e5e5e5;");
        header.setPadding(new Insets(0, 30, 0, 0));
        header.setPrefHeight(60);
        Region headerSpacer = new Region();
        HBox.setHgrow(headerSpacer, Priority.ALWAYS);
        Label accountLabel = new Label("Account");
        accountLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
        accountLabel.setTextFill(Color.web("#888"));
        // Icon account (bulat, hanya lingkaran abu-abu atau inisial)
        Circle accountCircle = new Circle(18, Color.LIGHTGRAY);
        Label accountInitial = new Label("A"); // atau ambil inisial dari nama user jika mau
        accountInitial.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        accountInitial.setTextFill(Color.web("#888"));
        StackPane accountPane = new StackPane(accountCircle, accountInitial);
        header.getChildren().addAll(headerSpacer, accountPane, new Label("  "), accountLabel);

        // Main Content
        VBox mainContent = new VBox(18);
        mainContent.setPadding(new Insets(30, 0, 0, 0));
        mainContent.setStyle("-fx-background-color: transparent;");

        // Profile Panel
        VBox profilePanel = new VBox(0);
        profilePanel.setPadding(new Insets(0));
        profilePanel.setAlignment(Pos.TOP_CENTER);

        // Panel putih utama
        VBox whitePanel = new VBox(18);
        whitePanel.setPadding(new Insets(30, 30, 30, 30));
        whitePanel.setAlignment(Pos.TOP_LEFT);
        whitePanel.setStyle("-fx-background-color: #fff; -fx-background-radius: 10;");

        // Judul Profile
        Label profileTitle = new Label("Profile");
        profileTitle.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        profileTitle.setTextFill(Color.web("#888"));
        profileTitle.setPadding(new Insets(0, 0, 18, 0));

        // Sub-panel Data Mahasiswa
        VBox dataPanel = new VBox(0);
        dataPanel.setStyle("-fx-background-color: #f7f7f7; -fx-background-radius: 7; -fx-border-radius: 7; -fx-border-color: #e3e3e3; -fx-border-width: 1;");
        dataPanel.setPadding(new Insets(18, 18, 18, 18));

        Label dataTitle = new Label("Data Mahasiswa");
        dataTitle.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        dataTitle.setTextFill(Color.web("#b0b0b0"));
        dataTitle.setPadding(new Insets(0, 0, 18, 0));

        HBox dataRow = new HBox(30);
        dataRow.setAlignment(Pos.CENTER_LEFT);

        // Foto Profil (bulat, hanya inisial, tanpa gambar)
        StackPane photoPane = new StackPane();
        Circle photoCircle = new Circle(70, Color.LIGHTGRAY);
        String initialText = member != null && member.nama != null && !member.nama.isEmpty()
            ? ("" + member.nama.charAt(0)).toUpperCase()
            : "";
        Label initial = new Label(initialText);
        initial.setFont(Font.font("Arial", FontWeight.BOLD, 48));
        initial.setTextFill(Color.web("#888"));
        photoPane.getChildren().addAll(photoCircle, initial);

        // Data Mahasiswa Text
        VBox dataText = new VBox(2);
        if (member != null) {
            Label name = new Label(member.nama);
            name.setFont(Font.font("Arial", FontWeight.BOLD, 18));
            name.setTextFill(Color.web("#222"));
            Label fakultas = new Label(member.fakultas);
            fakultas.setFont(Font.font("Arial", 14));
            fakultas.setTextFill(Color.web("#444"));
            Label prodi = new Label(member.prodi);
            prodi.setFont(Font.font("Arial", 14));
            prodi.setTextFill(Color.web("#444"));
            Label hp = new Label("HP : " + member.no_hp);
            hp.setFont(Font.font("Arial", 14));
            hp.setTextFill(Color.web("#444"));
            Label email = new Label(member.email);
            email.setFont(Font.font("Arial", 14));
            email.setTextFill(Color.web("#444"));
            dataText.getChildren().addAll(name, fakultas, prodi, hp, email);
        }
        Button editProfileBtn = new Button("Edit Profile");
        editProfileBtn.setStyle("-fx-background-color: #b8cfe5; -fx-text-fill: #444; -fx-font-size: 13px; -fx-background-radius: 8;");
        editProfileBtn.setPadding(new Insets(4, 12, 4, 12));
        dataText.getChildren().add(editProfileBtn);

        // Index Peminjaman Buku (panel kosong)
        VBox indexPanel = new VBox();
        indexPanel.setAlignment(Pos.TOP_CENTER);
        indexPanel.setPrefSize(220, 180);
        indexPanel.setStyle("-fx-background-color: #e3e3e3; -fx-background-radius: 7; -fx-border-radius: 7; -fx-border-color: #cccccc; -fx-border-width: 1;");
        Label indexLabel = new Label("Index Peminjaman Buku");
        indexLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        indexLabel.setTextFill(Color.web("#b0b0b0"));
        indexPanel.getChildren().add(indexLabel);

        dataRow.getChildren().addAll(photoPane, dataText, indexPanel);
        dataPanel.getChildren().addAll(dataTitle, dataRow);
        whitePanel.getChildren().addAll(profileTitle, dataPanel);
        profilePanel.getChildren().add(whitePanel);

        mainContent.getChildren().add(profilePanel);

        // Footer
        HBox footer = new HBox();
        footer.setAlignment(Pos.CENTER);
        footer.setPrefHeight(30);
        footer.setStyle("-fx-background-color: #e5e5e5;");
        Label footerLabel = new Label("Jalan Raya Tlogomas No. 246, Jatimulyo, Lowokwaru, Malang, Jawa Timur, 65144");
        footerLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 12));
        footerLabel.setTextFill(Color.web("#b0b0b0"));
        footer.getChildren().add(footerLabel);

        // Layout utama
        BorderPane rootPane = new BorderPane();
        rootPane.setLeft(sidebar);
        rootPane.setTop(header);
        rootPane.setCenter(mainContent);
        rootPane.setBottom(footer);

        // StackPane root sebagai background
        StackPane root = new StackPane();
        root.getChildren().add(rootPane);

        Scene scene = new Scene(root, 1200, 800);
        primaryStage.setTitle("Student Dashboard - myUMM Library");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private VBox createProfileCard() {
        VBox card = new VBox(20);
        card.setPadding(new Insets(25));
        card.setStyle("-fx-background-color: white; -fx-background-radius: 15; "
            + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 20, 0, 0, 4);");
        card.setPrefWidth(400);

        // Header Card
        Label title = new Label("Student Profile");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        title.setTextFill(Color.web("#2c3e50"));

        // Profile Content
        HBox profileContent = new HBox(25);
        profileContent.setAlignment(Pos.CENTER_LEFT);

        // Fetch member data from database if member is null
        Member dbMember = member;
        if (dbMember == null) {
            try {
                Connection conn = DatabaseConnection.getConnection();
                String query = "SELECT * FROM member LIMIT 1";
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                if (rs.next()) {
                    dbMember = new Member(
                        rs.getString("nama"),
                        rs.getString("fakultas"),
                        rs.getString("prodi"),
                        rs.getString("no_hp"),
                        rs.getString("email"),
                        rs.getString("nim")
                    );
                }
                rs.close();
                stmt.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Foto Profil
        StackPane avatar = new StackPane();
        Circle circle = new Circle(60);
        circle.setFill(Color.web("#bdc3c7"));
        String initialText = dbMember != null && dbMember.nama != null && !dbMember.nama.isEmpty() ? ("" + dbMember.nama.charAt(0)).toUpperCase() : "JD";
        Label initial = new Label(initialText);
        initial.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        initial.setTextFill(Color.web("#7f8c8d"));
        avatar.getChildren().addAll(circle, initial);

        // Data Mahasiswa
        VBox studentData = new VBox(8);
        studentData.setAlignment(Pos.CENTER_LEFT);
        if (dbMember != null) {
            String[] data = {
                dbMember.nama,
                dbMember.fakultas,
                dbMember.prodi,
                "NIM: " + dbMember.nim,
                "Email: " + dbMember.email,
                "HP: " + dbMember.no_hp
            };
            for (String text : data) {
                Label label = new Label(text);
                label.setFont(Font.font("Arial", 14));
                label.setTextFill(Color.web("#34495e"));
                studentData.getChildren().add(label);
            }
        }

        profileContent.getChildren().addAll(avatar, studentData);
        card.getChildren().addAll(title, profileContent);
        return card;
    }

    private VBox createStatsCard() {
        VBox card = new VBox(20);
        card.setPadding(new Insets(25));
        card.setStyle("-fx-background-color: white; -fx-background-radius: 15; "
            + "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 20, 0, 0, 4);");
        card.setPrefWidth(400);

        Label title = new Label("Borrowing Statistics");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        title.setTextFill(Color.web("#2c3e50"));

        // Statistik
        String[] stats = {
            "Active Borrowings: 3 books",
            "Overdue Books: 0",
            "Total Borrowed: 12 books",
            "Favorite Category: Computer Science"
        };

        VBox statsList = new VBox(15);
        for (String stat : stats) {
            HBox item = new HBox(15);
            item.setAlignment(Pos.CENTER_LEFT);
            
            Circle bullet = new Circle(5, Color.web("#3498db"));
            Label text = new Label(stat);
            text.setFont(Font.font("Arial", 14));
            text.setTextFill(Color.web("#34495e"));
            
            item.getChildren().addAll(bullet, text);
            statsList.getChildren().add(item);
        }

        card.getChildren().addAll(title, statsList);
        return card;
    }

    public static void main(String[] args) {
        launch(args);
    }
}