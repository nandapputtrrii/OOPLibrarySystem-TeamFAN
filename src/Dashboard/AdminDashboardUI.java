package Dashboard;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Callback; // Import Callback

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.time.LocalDate;

import Database.DatabaseConnection;
import Model.Mahasiswa; // Ensure this import is present
import Model.Peminjaman;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class AdminDashboardUI extends Application {

    private Stage primaryStage;
    private BorderPane mainLayout;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        VBox sidebar = createSidebar();
        VBox homeContent = createHomeContent();

        mainLayout = new BorderPane();
        mainLayout.setLeft(sidebar);
        mainLayout.setCenter(homeContent);

        Scene scene = new Scene(mainLayout, 1200, 800);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Admin Dashboard - myUMM Library");
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    private VBox createSidebar() {
        VBox sidebar = new VBox(20);
        sidebar.setPrefWidth(220);
        sidebar.setStyle("-fx-background-color: #7ea6c7;");
        sidebar.setPadding(new Insets(20, 0, 20, 0));

        Image logoImage = new Image(getClass().getResourceAsStream("/laser.jpg"));
        ImageView logoImageView = new ImageView(logoImage);
        logoImageView.setFitWidth(200);
        logoImageView.setPreserveRatio(true);
        logoImageView.setSmooth(true);

        VBox logoBox = new VBox();
        logoBox.setAlignment(Pos.CENTER);
        logoBox.setPadding(new Insets(10, 20, 10, 20));
        logoBox.getChildren().add(logoImageView);

        Button homeBtn = createMenuButton("Home");
        Button manageBookBtn = createMenuButton("Manage Book");
        Button manageUserBtn = createMenuButton("Manage User");

        homeBtn.setOnAction(e -> updateMainContent(createHomeContent()));
        manageUserBtn.setOnAction(e -> showManageUserMenu());
        manageBookBtn.setOnAction(e -> showManageBookMenu());

        VBox menuBox = new VBox(0, homeBtn, manageBookBtn, manageUserBtn);
        sidebar.getChildren().addAll(logoBox, menuBox);
        return sidebar;
    }

    private Button createMenuButton(String text) {
        Button btn = new Button(text);
        btn.setPrefWidth(220);
        btn.setAlignment(Pos.CENTER_LEFT);
        btn.setPadding(new Insets(18, 30, 18, 30));
        btn.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-family: 'Segoe UI'; -fx-border-width: 0 0 1 0; -fx-border-color: #bcd2e8;");
        btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: #a2c3de; -fx-text-fill: #2c3e50; -fx-font-size: 16px; -fx-border-width: 0 0 1 0; -fx-border-color: #bcd2e8;"));
        btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 16px; -fx-border-width: 0 0 1 0; -fx-border-color: #bcd2e8;"));
        return btn;
    }

    private VBox createHomeContent() {
        VBox homeContent = new VBox();
        homeContent.setStyle("-fx-background-color: #f8f9fa;");

        HBox header = createHeader("Admin");
        
        VBox contentWrapper = new VBox(20);
        contentWrapper.setPadding(new Insets(30));
        contentWrapper.setAlignment(Pos.TOP_CENTER);

        Label welcomeLabel = new Label("Welcome to Admin Dashboard");
        welcomeLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 28));

        GridPane menuGrid = new GridPane();
        menuGrid.setHgap(30);
        menuGrid.setVgap(30);
        menuGrid.setAlignment(Pos.CENTER);

        VBox mahasiswaItem = createMenuItem("Data Mahasiswa", "Lihat & Kelola", () -> showStudentTable(false));
        VBox bukuItem = createMenuItem("Data Buku", "Lihat & Kelola", () -> showManageBookMenu());
        VBox availableItem = createMenuItem("Available Book", "Buku Tersedia", this::showAvailableBooksTable);
        VBox lateItem = createMenuItem("Late Return", "Buku Terlambat", this::showLateReturnsTable);
        
        menuGrid.add(mahasiswaItem, 0, 0);
        menuGrid.add(bukuItem, 1, 0);
        menuGrid.add(availableItem, 0, 1);
        menuGrid.add(lateItem, 1, 1);
        
        contentWrapper.getChildren().addAll(welcomeLabel, menuGrid);
        
        HBox footer = createFooter();
        
        VBox.setVgrow(contentWrapper, Priority.ALWAYS);
        homeContent.getChildren().addAll(header, contentWrapper, footer);
        
        return homeContent;
    }
    
    // --- BAGIAN UTAMA LAYOUT (HEADER, FOOTER, DLL) ---

    private HBox createHeader(String accountName) {
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_RIGHT);
        header.setStyle("-fx-background-color: #e5e5e5;");
        header.setPadding(new Insets(0, 30, 0, 30));
        header.setPrefHeight(60);
        Label accountLabel = new Label(accountName);
        accountLabel.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 16));
        header.getChildren().add(accountLabel);
        return header;
    }

    private HBox createFooter() {
        HBox footer = new HBox();
        footer.setAlignment(Pos.CENTER);
        footer.setPrefHeight(30);
        footer.setStyle("-fx-background-color: #e5e5e5;");
        Label footerLabel = new Label("Jalan Raya Tlogomas No. 246, Jatimulyo, Lowokwaru, Malang, Jawa Timur, 65144");
        footerLabel.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 12));
        footerLabel.setTextFill(Color.web("#b0b0b0"));
        footer.getChildren().add(footerLabel);
        return footer;
    }

    private void updateMainContent(VBox content) {
        mainLayout.setCenter(content);
    }
    
    public static VBox createWhitePanel(String title) {
        VBox mainContent = new VBox(18);
        mainContent.setPadding(new Insets(30));
        mainContent.setStyle("-fx-background-color: #f8f9fa;");

        VBox whitePanel = new VBox(18);
        whitePanel.setPadding(new Insets(30));
        whitePanel.setAlignment(Pos.TOP_LEFT);
        whitePanel.setStyle("-fx-background-color: #fff; -fx-background-radius: 10;");

        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 22));
        titleLabel.setTextFill(Color.web("#888"));
        titleLabel.setPadding(new Insets(0, 0, 18, 0));
        
        whitePanel.getChildren().add(titleLabel);
        mainContent.getChildren().add(whitePanel);
        return mainContent;
    }
    
    private VBox createMenuItem(String title, String subtitle, Runnable action) {
        VBox box = new VBox(5);
        box.setPadding(new Insets(20));
        box.setAlignment(Pos.CENTER_LEFT);
        box.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 8; -fx-border-color: #e0e0e0; -fx-border-width: 1; -fx-cursor: hand;");
        box.setPrefSize(250, 120);

        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        
        Label subtitleLabel = new Label(subtitle);
        subtitleLabel.setFont(Font.font("Segoe UI", 12));
        subtitleLabel.setTextFill(Color.GRAY);

        box.getChildren().addAll(titleLabel, subtitleLabel);
        
        box.setOnMouseEntered(e -> box.setStyle("-fx-background-color: #f0f8ff; -fx-background-radius: 8; -fx-border-color: #7ea6c7; -fx-border-width: 1.5; -fx-cursor: hand;"));
        box.setOnMouseExited(e -> box.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 8; -fx-border-color: #e0e0e0; -fx-border-width: 1; -fx-cursor: hand;"));
        box.setOnMouseClicked(e -> action.run());
        
        return box;
    }

    // --- MANAGE USER ---

    private void showManageUserMenu() {
        VBox content = createWhitePanel("Manage User");
        VBox whitePanel = (VBox) content.getChildren().get(0);

        GridPane menuGrid = new GridPane();
        menuGrid.setHgap(25);
        menuGrid.setVgap(25);
        menuGrid.setAlignment(Pos.CENTER_LEFT);

        VBox item1 = createMenuItem("Tampilkan Semua Mahasiswa", "Lihat semua data", () -> showStudentTable(true));
        VBox item2 = createMenuItem("Cari Mahasiswa", "Temukan berdasarkan NIM/Nama", () -> showStudentSearch());
        VBox item3 = createMenuItem("Tambah Mahasiswa", "Daftarkan mahasiswa baru", () -> showAddStudentForm());
        
        menuGrid.add(item1, 0, 0);
        menuGrid.add(item2, 1, 0);
        menuGrid.add(item3, 0, 1);

        whitePanel.getChildren().add(menuGrid);
        updateMainContent(content);
    }
    
    private void showStudentTable(boolean fromManageUser) {
        VBox content = createWhitePanel("Data Mahasiswa");
        VBox whitePanel = (VBox) content.getChildren().get(0);

        TableView<Mahasiswa> table = new TableView<>();
        table.setPrefHeight(500);

        TableColumn<Mahasiswa, String> namaCol = new TableColumn<>("Nama");
        namaCol.setCellValueFactory(new PropertyValueFactory<>("nama"));
        namaCol.setPrefWidth(200);

        TableColumn<Mahasiswa, String> nimCol = new TableColumn<>("NIM");
        nimCol.setCellValueFactory(new PropertyValueFactory<>("nim"));
        nimCol.setPrefWidth(150);

        TableColumn<Mahasiswa, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        emailCol.setPrefWidth(250);

        TableColumn<Mahasiswa, Void> actionCol = new TableColumn<>("Aksi");

        // Create a button for each row in the action column
        Callback<TableColumn<Mahasiswa, Void>, TableCell<Mahasiswa, Void>> cellFactory = new Callback<TableColumn<Mahasiswa, Void>, TableCell<Mahasiswa, Void>>() {
            @Override
            public TableCell<Mahasiswa, Void> call(final TableColumn<Mahasiswa, Void> param) {
                final TableCell<Mahasiswa, Void> cell = new TableCell<Mahasiswa, Void>() {
                    private final Button deleteButton = new Button("Delete");

                    {
                        deleteButton.setOnAction(event -> {
                            Mahasiswa mahasiswa = getTableView().getItems().get(getIndex());
                            deleteMahasiswa(mahasiswa.getNim()); // Call the delete method
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null); // Don't show anything if the row is empty
                        } else {
                            setGraphic(deleteButton); // Show button if the row contains data
                        }
                    }
                };
                return cell;
            }
        };

        actionCol.setCellFactory(cellFactory);
        table.getColumns().addAll(namaCol, nimCol, emailCol, actionCol); // Add action column to the table

        ObservableList<Mahasiswa> mahasiswaList = FXCollections.observableArrayList();
        String sql = "SELECT nim, nama, email, fakultas, prodi, no_hp FROM member";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                mahasiswaList.add(new Mahasiswa(
                    rs.getString("nim"),
                    rs.getString("nama"),
                    rs.getString("email"),
                    rs.getString("fakultas"),
                    rs.getString("prodi"),
                    rs.getString("no_hp")
                ));
            }
            table.setItems(mahasiswaList);

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Gagal memuat data mahasiswa: " + e.getMessage());
        }

        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        Button backBtn = new Button("Kembali");
        backBtn.setOnAction(e -> {
            if (fromManageUser) {
                showManageUserMenu();
            } else {
                updateMainContent(createHomeContent());
            }
        });

        whitePanel.getChildren().addAll(table, backBtn);
        updateMainContent(content);
    }

    private void deleteMahasiswa(String nim) {
        String sql = "DELETE FROM member WHERE nim = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nim);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Berhasil", "Mahasiswa berhasil dihapus!");
                showStudentTable(false); // Refresh the table
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Gagal menghapus mahasiswa.");
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Gagal menghapus mahasiswa: " + e.getMessage());
        }
    }

    private void showStudentSearch() {
        VBox content = createWhitePanel("Cari Mahasiswa");
        VBox whitePanel = (VBox) content.getChildren().get(0);

        HBox searchBox = new HBox(10);
        searchBox.setAlignment(Pos.CENTER_LEFT);
        TextField inputField = new TextField();
        inputField.setPromptText("Masukkan NIM atau Nama...");
        inputField.setPrefWidth(300);
        Button searchButton = new Button("Cari");
        searchBox.getChildren().addAll(new Label("Cari:"), inputField, searchButton);

        TableView<Mahasiswa> table = new TableView<>();
        table.setPrefHeight(400);
        table.setPlaceholder(new Label("Hasil pencarian akan ditampilkan di sini."));

        TableColumn<Mahasiswa, String> namaCol = new TableColumn<>("Nama");
        namaCol.setCellValueFactory(new PropertyValueFactory<>("nama"));
        TableColumn<Mahasiswa, String> nimCol = new TableColumn<>("NIM");
        nimCol.setCellValueFactory(new PropertyValueFactory<>("nim"));
        TableColumn<Mahasiswa, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        table.getColumns().addAll(namaCol, nimCol, emailCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        searchButton.setOnAction(e -> {
            String keyword = inputField.getText().trim();
            if (keyword.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Input Kosong", "Silakan masukkan kata kunci pencarian.");
                return;
            }

            ObservableList<Mahasiswa> mahasiswaList = FXCollections.observableArrayList();
            String sql = "SELECT nim, nama, email, fakultas, prodi, no_hp FROM member WHERE nim LIKE ? OR nama LIKE ?";
            
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                
                pstmt.setString(1, "%" + keyword + "%");
                pstmt.setString(2, "%" + keyword + "%");
                ResultSet rs = pstmt.executeQuery();

                while (rs.next()) {
                    mahasiswaList.add(new Mahasiswa(
                        rs.getString("nim"),
                        rs.getString("nama"),
                        rs.getString("email"),
                        rs.getString("fakultas"),
                        rs.getString("prodi"),
                        rs.getString("no_hp")
                    ));
                }
                table.setItems(mahasiswaList);
                if (mahasiswaList.isEmpty()) {
                    table.setPlaceholder(new Label("Data dengan kata kunci '" + keyword + "' tidak ditemukan."));
                }

            } catch (SQLException ex) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Gagal mencari data: " + ex.getMessage());
            }
        });

        Button backBtn = new Button("Kembali");
        backBtn.setOnAction(e -> showManageUserMenu());

        whitePanel.getChildren().addAll(searchBox, table, backBtn);
        updateMainContent(content);
    }
    
    private void showAddStudentForm() {
        VBox content = createWhitePanel("Tambah Mahasiswa Baru");
        VBox whitePanel = (VBox) content.getChildren().get(0);

        GridPane grid = new GridPane();
        grid.setVgap(15);
        grid.setHgap(10);

        TextField nimField = new TextField();
        TextField namaField = new TextField();
        TextField emailField = new TextField();
        TextField fakultasField = new TextField();
        TextField prodiField = new TextField();
        TextField hpField = new TextField();
        PasswordField passwordField = new PasswordField();

        grid.addRow(0, new Label("NIM:"), nimField);
        grid.addRow(1, new Label("Nama Lengkap:"), namaField);
        grid.addRow(2, new Label("Email:"), emailField);
        grid.addRow(3, new Label("Fakultas:"), fakultasField);
        grid.addRow(4, new Label("Program Studi:"), prodiField);
        grid.addRow(5, new Label("No. HP:"), hpField);
        grid.addRow(6, new Label("Password:"), passwordField);

        Button addBtn = new Button("Tambah Mahasiswa");
        addBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        Button backBtn = new Button("Kembali");
        backBtn.setOnAction(e -> showManageUserMenu());
        
        addBtn.setOnAction(e -> {
            String nim = nimField.getText().trim();
            String nama = namaField.getText().trim();
            String email = emailField.getText().trim();
            String password = passwordField.getText().trim();

            if (nim.isEmpty() || nama.isEmpty() || email.isEmpty() || password.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Input Error", "NIM, Nama, Email, dan Password tidak boleh kosong.");
                return;
            }

            String sql = "INSERT INTO member (nim, nama, email, fakultas, prodi, no_hp, password) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                
                pstmt.setString(1, nim);
                pstmt.setString(2, nama);
                pstmt.setString(3, email);
                pstmt.setString(4, fakultasField.getText().trim());
                pstmt.setString(5, prodiField.getText().trim());
                pstmt.setString(6, hpField.getText().trim());
                pstmt.setString(7, password); 

                int affectedRows = pstmt.executeUpdate();
                if (affectedRows > 0) {
                    showAlert(Alert.AlertType.INFORMATION, "Berhasil", "Mahasiswa baru berhasil ditambahkan!");
                    showManageUserMenu();
                }

            } catch (SQLException ex) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Gagal menambahkan mahasiswa: " + ex.getMessage());
            }
        });

        HBox buttons = new HBox(10, addBtn, backBtn);
        grid.add(buttons, 1, 7);

        whitePanel.getChildren().add(grid);
        updateMainContent(content);
    }

    // --- MANAGE BOOK ---

    private void showManageBookMenu() {
        VBox content = createWhitePanel("Manage Book");
        VBox whitePanel = (VBox) content.getChildren().get(0);

        GridPane menuGrid = new GridPane();
        menuGrid.setHgap(25);
        menuGrid.setVgap(25);
        menuGrid.setAlignment(Pos.CENTER_LEFT);

        VBox item1 = createMenuItem("Tampilkan Semua Buku", "Lihat semua koleksi", this::showAllBooksTable);
        VBox item2 = createMenuItem("Cari Buku", "Temukan berdasarkan Judul/Penulis", this::showSearchBookPage);
        VBox item3 = createMenuItem("Tambah Buku", "Tambahkan koleksi baru", this::showAddBookForm);

        menuGrid.add(item1, 0, 0);
        menuGrid.add(item2, 1, 0);
        menuGrid.add(item3, 0, 1);

        whitePanel.getChildren().add(menuGrid);
        updateMainContent(content);
    }
    
    private void showAllBooksTable() {
        VBox content = createWhitePanel("Data Semua Buku");
        VBox whitePanel = (VBox) content.getChildren().get(0);

        TableView<Book> table = new TableView<>();
        table.setPrefHeight(500);

        TableColumn<Book, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("idBuku"));

        TableColumn<Book, String> titleCol = new TableColumn<>("Judul");
        titleCol.setCellValueFactory(new PropertyValueFactory<>("judul"));
        titleCol.setPrefWidth(250);

        TableColumn<Book, String> authorCol = new TableColumn<>("Penulis");
        authorCol.setCellValueFactory(new PropertyValueFactory<>("penulis"));
        authorCol.setPrefWidth(150);

        TableColumn<Book, String> isbnCol = new TableColumn<>("ISBN");
        isbnCol.setCellValueFactory(new PropertyValueFactory<>("isbn"));

        TableColumn<Book, String> stockCol = new TableColumn<>("Stok");
        stockCol.setCellValueFactory(new PropertyValueFactory<>("stockStatus"));

        TableColumn<Book, Void> actionCol = new TableColumn<>("Aksi");

        // --- PERBAIKAN TableCell DIMULAI DI SINI ---
        Callback<TableColumn<Book, Void>, TableCell<Book, Void>> cellFactory = new Callback<TableColumn<Book, Void>, TableCell<Book, Void>>() {
            @Override
            public TableCell<Book, Void> call(final TableColumn<Book, Void> param) {
                final TableCell<Book, Void> cell = new TableCell<Book, Void>() {
                    private final Button btn = new Button("Detail");
                    {
                        btn.setOnAction(event -> {
                            Book book = getTableView().getItems().get(getIndex());
                            showBookDetailForm(book);
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null); // Don't show anything if the row is empty
                        } else {
                            setGraphic(btn); // Show button if the row contains data
                        }
                    }
                };
                return cell;
            }
        };
        actionCol.setCellFactory(cellFactory);
        // --- PERBAIKAN TableCell SELESAI ---

        table.getColumns().addAll(idCol, titleCol, authorCol, isbnCol, stockCol, actionCol);
        loadAllBooksData(table);

        Button backBtn = new Button("Kembali");
        backBtn.setOnAction(e -> showManageBookMenu());
        
        Button refreshBtn = new Button("Refresh");
        refreshBtn.setOnAction(e -> loadAllBooksData(table));

        HBox buttonBox = new HBox(10, refreshBtn, backBtn);
        whitePanel.getChildren().addAll(table, buttonBox);
        updateMainContent(content);
    }
    
    private void loadAllBooksData(TableView<Book> table) {
        ObservableList<Book> bookList = FXCollections.observableArrayList();
        String sql = "SELECT id_buku, judul, penulis, tanggal_terbit, isbn, kategori, total_salinan, salinan_tersedia FROM buku";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                bookList.add(new Book(
                    rs.getInt("id_buku"),
                    rs.getString("judul"),
                    rs.getString("penulis"),
                    rs.getDate("tanggal_terbit"),
                    rs.getString("isbn"),
                    rs.getString("kategori"),
                    rs.getInt("total_salinan"),
                    rs.getInt("salinan_tersedia")
                ));
            }
            table.setItems(bookList);

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Gagal memuat data buku: " + e.getMessage());
        }
    }

    private void showSearchBookPage() {
        VBox content = createWhitePanel("Cari Buku");
        VBox whitePanel = (VBox) content.getChildren().get(0);

        HBox searchBox = new HBox(10);
        searchBox.setAlignment(Pos.CENTER_LEFT);
        TextField inputField = new TextField();
        inputField.setPromptText("Masukkan Judul atau Penulis...");
        inputField.setPrefWidth(300);
        Button searchButton = new Button("Cari");
        searchBox.getChildren().addAll(new Label("Cari:"), inputField, searchButton);

        TableView<Book> table = new TableView<>();
        table.setPrefHeight(400);
        table.setPlaceholder(new Label("Hasil pencarian akan ditampilkan di sini."));

        TableColumn<Book, String> titleCol = new TableColumn<>("Judul");
        titleCol.setCellValueFactory(new PropertyValueFactory<>("judul"));
        TableColumn<Book, String> authorCol = new TableColumn<>("Penulis");
        authorCol.setCellValueFactory(new PropertyValueFactory<>("penulis"));
        TableColumn<Book, String> stockCol = new TableColumn<>("Stok");
        stockCol.setCellValueFactory(new PropertyValueFactory<>("stockStatus"));
        table.getColumns().addAll(titleCol, authorCol, stockCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        searchButton.setOnAction(e -> {
            String keyword = inputField.getText().trim();
            if (keyword.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Input Kosong", "Silakan masukkan kata kunci pencarian.");
                return;
            }

            ObservableList<Book> bookList = FXCollections.observableArrayList();
            String sql = "SELECT id_buku, judul, penulis, tanggal_terbit, isbn, kategori, total_salinan, salinan_tersedia FROM buku WHERE judul LIKE ? OR penulis LIKE ?";
            
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                
                pstmt.setString(1, "%" + keyword + "%");
                pstmt.setString(2, "%" + keyword + "%");
                ResultSet rs = pstmt.executeQuery();

                while (rs.next()) {
                    bookList.add(new Book(
                        rs.getInt("id_buku"),
                        rs.getString("judul"),
                        rs.getString("penulis"),
                        rs.getDate("tanggal_terbit"),
                        rs.getString("isbn"),
                        rs.getString("kategori"),
                        rs.getInt("total_salinan"),
                        rs.getInt("salinan_tersedia")
                    ));
                }
                table.setItems(bookList);
                if (bookList.isEmpty()) {
                    table.setPlaceholder(new Label("Buku dengan kata kunci '" + keyword + "' tidak ditemukan."));
                }

            } catch (SQLException ex) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Gagal mencari buku: " + ex.getMessage());
            }
        });

        Button backBtn = new Button("Kembali");
        backBtn.setOnAction(e -> showManageBookMenu());

        whitePanel.getChildren().addAll(searchBox, table, backBtn);
        updateMainContent(content);
    }

    private void showAddBookForm() {
        VBox content = createWhitePanel("Tambah Buku Baru");
        VBox whitePanel = (VBox) content.getChildren().get(0);

        GridPane grid = new GridPane();
        grid.setVgap(15);
        grid.setHgap(10);

        TextField titleField = new TextField();
        TextField authorField = new TextField();
        TextField isbnField = new TextField();
        TextField categoryField = new TextField();
        DatePicker publishDatePicker = new DatePicker(LocalDate.now());
        TextField totalStockField = new TextField();

        grid.addRow(0, new Label("Judul:"), titleField);
        grid.addRow(1, new Label("Penulis:"), authorField);
        grid.addRow(2, new Label("ISBN:"), isbnField);
        grid.addRow(3, new Label("Kategori:"), categoryField);
        grid.addRow(4, new Label("Tanggal Terbit:"), publishDatePicker);
        grid.addRow(5, new Label("Jumlah Stok:"), totalStockField);

        Button addBtn = new Button("Tambah Buku");
        addBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        Button backBtn = new Button("Kembali");
        backBtn.setOnAction(e -> showManageBookMenu());

        addBtn.setOnAction(e -> {
            String sql = "INSERT INTO buku (judul, penulis, tanggal_terbit, isbn, kategori, total_salinan, salinan_tersedia) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                
                int totalStock = Integer.parseInt(totalStockField.getText().trim());

                pstmt.setString(1, titleField.getText().trim());
                pstmt.setString(2, authorField.getText().trim());
                pstmt.setDate(3, Date.valueOf(publishDatePicker.getValue()));
                pstmt.setString(4, isbnField.getText().trim());
                pstmt.setString(5, categoryField.getText().trim());
                pstmt.setInt(6, totalStock);
                pstmt.setInt(7, totalStock);

                int affectedRows = pstmt.executeUpdate();
                if (affectedRows > 0) {
                    showAlert(Alert.AlertType.INFORMATION, "Berhasil", "Buku baru berhasil ditambahkan!");
                    showManageBookMenu();
                }

            } catch (NumberFormatException ex) {
                showAlert(Alert.AlertType.ERROR, "Input Error", "Jumlah stok harus berupa angka.");
            } catch (SQLException ex) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Gagal menambahkan buku: " + ex.getMessage());
            }
        });

        HBox buttons = new HBox(10, addBtn, backBtn);
        grid.add(buttons, 1, 6);

        whitePanel.getChildren().add(grid);
        updateMainContent(content);
    }

    private void showBookDetailForm(Book book) {
        VBox content = createWhitePanel("Detail Buku");
        VBox whitePanel = (VBox) content.getChildren().get(0);

        GridPane grid = new GridPane();
        grid.setVgap(15);
        grid.setHgap(10);

        TextField titleField = new TextField(book.getJudul());
        TextField authorField = new TextField(book.getPenulis());
        TextField isbnField = new TextField(book.getIsbn());
        TextField categoryField = new TextField(book.getKategori());
        DatePicker publishDatePicker = new DatePicker(book.getTanggalTerbit().toLocalDate());
        TextField totalStockField = new TextField(String.valueOf(book.getTotalSalinan()));
        TextField availableStockField = new TextField(String.valueOf(book.getSalinanTersedia()));
        availableStockField.setEditable(true);

        grid.addRow(0, new Label("Judul:"), titleField);
        grid.addRow(1, new Label("Penulis:"), authorField);
        grid.addRow(2, new Label("ISBN:"), isbnField);
        grid.addRow(3, new Label("Kategori:"), categoryField);
        grid.addRow(4, new Label("Tanggal Terbit:"), publishDatePicker);
        grid.addRow(5, new Label("Total Stok:"), totalStockField);
        grid.addRow(6, new Label("Stok Tersedia:"), availableStockField);

        Button updateBtn = new Button("Update Buku");
        updateBtn.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
        Button deleteBtn = new Button("Hapus Buku");
        deleteBtn.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
        Button backBtn = new Button("Kembali");
        backBtn.setOnAction(e -> showAllBooksTable());

        Button updateStockBtn = new Button("Update Stok");
        updateStockBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");

        updateStockBtn.setOnAction(e -> {
            int newStock;
            try {
                newStock = Integer.parseInt(availableStockField.getText().trim());
                if (newStock < 0) {
                    showAlert(Alert.AlertType.WARNING, "Input Error", "Stok tidak boleh negatif.");
                    return;
                }

                String sql = "UPDATE buku SET salinan_tersedia = ? WHERE id_buku = ?";
                try (Connection conn = DatabaseConnection.getConnection();
                     PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setInt(1, newStock);
                    pstmt.setInt(2, book.getIdBuku());
                    int affectedRows = pstmt.executeUpdate();
                    if (affectedRows > 0) {
                        showAlert(Alert.AlertType.INFORMATION, "Berhasil", "Stok berhasil diperbarui!");
                        showAllBooksTable();
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Error", "Gagal memperbarui stok.");
                    }
                } catch (SQLException ex) {
                    showAlert(Alert.AlertType.ERROR, "Database Error", "Gagal memperbarui data: " + ex.getMessage());
                }
            } catch (NumberFormatException ex) {
                showAlert(Alert.AlertType.WARNING, "Input Error", "Silakan masukkan angka yang valid untuk stok.");
            }
        });

        updateBtn.setOnAction(e -> {
            String sql = "UPDATE buku SET judul=?, penulis=?, tanggal_terbit=?, isbn=?, kategori=?, total_salinan=? WHERE id_buku=?";
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setString(1, titleField.getText().trim());
                pstmt.setString(2, authorField.getText().trim());
                pstmt.setDate(3, Date.valueOf(publishDatePicker.getValue()));
                pstmt.setString(4, isbnField.getText().trim());
                pstmt.setString(5, categoryField.getText().trim());
                pstmt.setInt(6, Integer.parseInt(totalStockField.getText().trim()));
                pstmt.setInt(7, book.getIdBuku());

                pstmt.executeUpdate();
                showAlert(Alert.AlertType.INFORMATION, "Berhasil", "Data buku telah diperbarui.");
                showAllBooksTable();

            } catch (NumberFormatException ex) {
                showAlert(Alert.AlertType.ERROR, "Input Error", "Jumlah stok harus berupa angka.");
            } catch (SQLException ex) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Gagal memperbarui buku: " + ex.getMessage());
            }
        });

        deleteBtn.setOnAction(e -> {
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION, "Apakah Anda yakin ingin menghapus buku ini?", ButtonType.YES, ButtonType.NO);
            confirmAlert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.YES) {
                    String sql = "DELETE FROM buku WHERE id_buku = ?";
                    try (Connection conn = DatabaseConnection.getConnection();
                         PreparedStatement pstmt = conn.prepareStatement(sql)) {
                        
                        pstmt.setInt(1, book.getIdBuku());
                        int affectedRows = pstmt.executeUpdate();

                        if (affectedRows > 0) {
                            showAlert(Alert.AlertType.INFORMATION, "Berhasil", "Buku berhasil dihapus.");
                            showAllBooksTable();
                        }
                    } catch (SQLException ex) {
                        showAlert(Alert.AlertType.ERROR, "Database Error", "Gagal menghapus buku: " + ex.getMessage());
                    }
                }
            });
        });

        HBox buttons = new HBox(10, updateBtn, deleteBtn, backBtn, updateStockBtn);
        grid.add(buttons, 1, 7);

        whitePanel.getChildren().add(grid);
        updateMainContent(content);
    }
    
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    // --- INNER CLASS UNTUK DATA MODEL ---
    
    public static class Book {
        private int idBuku, totalSalinan, salinanTersedia;
        private String judul, penulis, isbn, kategori;
        private Date tanggalTerbit;

        public Book(int idBuku, String judul, String penulis, Date tanggalTerbit, String isbn, String kategori, int totalSalinan, int salinanTersedia) {
            this.idBuku = idBuku;
            this.judul = judul;
            this.penulis = penulis;
            this.tanggalTerbit = tanggalTerbit;
            this.isbn = isbn;
            this.kategori = kategori;
            this.totalSalinan = totalSalinan;
            this.salinanTersedia = salinanTersedia;
        }
        
        public int getIdBuku() { return idBuku; }
        public String getJudul() { return judul; }
        public String getPenulis() { return penulis; }
        public Date getTanggalTerbit() { return tanggalTerbit; }
        public String getIsbn() { return isbn; }
        public String getKategori() { return kategori; }
        public int getTotalSalinan() { return totalSalinan; }
        public int getSalinanTersedia() { return salinanTersedia; }
        public String getStockStatus() { return salinanTersedia + "/" + totalSalinan; }
    }

    public static class Mahasiswa {
        private String nim, nama, email, fakultas, prodi, no_hp;

        public Mahasiswa(String nim, String nama, String email, String fakultas, String prodi, String no_hp) {
            this.nim = nim;
            this.nama = nama;
            this.email = email;
            this.fakultas = fakultas;
            this.prodi = prodi;
            this.no_hp = no_hp;
        }

        public String getNama() { return nama; }
        public String getNim() { return nim; }
        public String getEmail() { return email; }
        public String getFakultas() { return fakultas; }
        public String getProdi() { return prodi; }
        public String getNo_hp() { return no_hp; }
    }

    private void showAvailableBooksTable() {
        VBox content = createWhitePanel("Available Books");
        VBox whitePanel = (VBox) content.getChildren().get(0);

        TableView<Book> table = new TableView<>();
        table.setPrefHeight(500);

        TableColumn<Book, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("idBuku"));

        TableColumn<Book, String> titleCol = new TableColumn<>("Judul");
        titleCol.setCellValueFactory(new PropertyValueFactory<>("judul"));
        titleCol.setPrefWidth(250);

        TableColumn<Book, String> authorCol = new TableColumn<>("Penulis");
        authorCol.setCellValueFactory(new PropertyValueFactory<>("penulis"));
        authorCol.setPrefWidth(150);

        TableColumn<Book, String> isbnCol = new TableColumn<>("ISBN");
        isbnCol.setCellValueFactory(new PropertyValueFactory<>("isbn"));

        TableColumn<Book, String> stockCol = new TableColumn<>("Stok");
        stockCol.setCellValueFactory(new PropertyValueFactory<>("stockStatus"));

        table.getColumns().addAll(idCol, titleCol, authorCol, isbnCol, stockCol);
        loadAvailableBooksData(table);

        Button backBtn = new Button("Kembali");
        backBtn.setOnAction(e -> showManageBookMenu());

        whitePanel.getChildren().addAll(table, backBtn);
        updateMainContent(content);
    }

    private void loadAvailableBooksData(TableView<Book> table) {
        ObservableList<Book> bookList = FXCollections.observableArrayList();
        String sql = "SELECT id_buku, judul, penulis, tanggal_terbit, isbn, kategori, total_salinan, salinan_tersedia FROM buku WHERE salinan_tersedia > 0";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                bookList.add(new Book(
                    rs.getInt("id_buku"),
                    rs.getString("judul"),
                    rs.getString("penulis"),
                    rs.getDate("tanggal_terbit"),
                    rs.getString("isbn"),
                    rs.getString("kategori"),
                    rs.getInt("total_salinan"),
                    rs.getInt("salinan_tersedia")
                ));
            }
            table.setItems(bookList);

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Gagal memuat data buku: " + e.getMessage());
        }
    }

    private void showLateReturnsTable() {
        VBox content = createWhitePanel("Late Returns");
        VBox whitePanel = (VBox) content.getChildren().get(0);

        TableView<Peminjaman> table = new TableView<>();
        table.setPrefHeight(500);

        TableColumn<Peminjaman, String> idPeminjamanCol = new TableColumn<>("ID Peminjaman");
        idPeminjamanCol.setCellValueFactory(new PropertyValueFactory<>("idPeminjaman"));

        TableColumn<Peminjaman, String> idBukuCol = new TableColumn<>("ID Buku");
        idBukuCol.setCellValueFactory(new PropertyValueFactory<>("idBuku"));

        TableColumn<Peminjaman, String> judulBukuCol = new TableColumn<>("Judul Buku");
        judulBukuCol.setCellValueFactory(new PropertyValueFactory<>("judulBuku"));

        TableColumn<Peminjaman, LocalDate> tanggalPeminjamanCol = new TableColumn<>("Tanggal Peminjaman");
        tanggalPeminjamanCol.setCellValueFactory(new PropertyValueFactory<>("tanggalPeminjaman"));

        TableColumn<Peminjaman, LocalDate> tanggalPengembalianCol = new TableColumn<>("Tanggal Pengembalian");
        tanggalPengembalianCol.setCellValueFactory(new PropertyValueFactory<>("tanggalPengembalian"));

        TableColumn<Peminjaman, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("statusPeminjaman"));

        table.getColumns().addAll(idPeminjamanCol, idBukuCol, judulBukuCol, tanggalPeminjamanCol, tanggalPengembalianCol, statusCol);
        loadLateReturnsData(table);

        Button backBtn = new Button("Kembali");
        backBtn.setOnAction(e -> showManageUserMenu());

        whitePanel.getChildren().addAll(table, backBtn);
        updateMainContent(content);
    }

    private void loadLateReturnsData(TableView<Peminjaman> table) {
        ObservableList<Peminjaman> peminjamanList = FXCollections.observableArrayList();
        String sql = "SELECT id_peminjaman, id_buku, judul_buku, tanggal_peminjaman, tanggal_pengembalian, status_peminjaman " +
                     "FROM peminjaman WHERE status_peminjaman = 'Terlambat'";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                peminjamanList.add(new Peminjaman(
                    rs.getString("id_peminjaman"),
                    rs.getString("id_buku"),
                    rs.getString("judul_buku"),
                    rs.getDate("tanggal_peminjaman").toLocalDate(),
                    rs.getDate("tanggal_pengembalian").toLocalDate(),
                    rs.getString("status_peminjaman")
                ));
            }
            table.setItems(peminjamanList);

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Gagal memuat data peminjaman: " + e.getMessage());
        }
    }

    public void displayOverdueBooks(ObservableList<Peminjaman> overdueBooks) {
        VBox content = createWhitePanel("Overdue Books");
        VBox whitePanel = (VBox) content.getChildren().get(0);

        TableView<Peminjaman> table = new TableView<>();
        table.setPrefHeight(500);

        TableColumn<Peminjaman, String> idPeminjamanCol = new TableColumn<>("ID Peminjaman");
        idPeminjamanCol.setCellValueFactory(new PropertyValueFactory<>("idPeminjaman"));

        TableColumn<Peminjaman, String> idBukuCol = new TableColumn<>("ID Buku");
        idBukuCol.setCellValueFactory(new PropertyValueFactory<>("idBuku"));

        TableColumn<Peminjaman, String> judulBukuCol = new TableColumn<>("Judul Buku");
        judulBukuCol.setCellValueFactory(new PropertyValueFactory<>("judulBuku"));

        TableColumn<Peminjaman, LocalDate> tanggalPeminjamanCol = new TableColumn<>("Tanggal Peminjaman");
        tanggalPeminjamanCol.setCellValueFactory(new PropertyValueFactory<>("tanggalPeminjaman"));

        TableColumn<Peminjaman, LocalDate> tanggalPengembalianCol = new TableColumn<>("Tanggal Pengembalian");
        tanggalPengembalianCol.setCellValueFactory(new PropertyValueFactory<>("tanggalPengembalian"));

        TableColumn<Peminjaman, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("statusPeminjaman"));

        table.getColumns().addAll(idPeminjamanCol, idBukuCol, judulBukuCol, tanggalPeminjamanCol, tanggalPengembalianCol, statusCol);
        
        // Set the items to the table only if overdueBooks is not empty
        if (overdueBooks != null && !overdueBooks.isEmpty()) {
            table.setItems(overdueBooks);
        } else {
            table.setPlaceholder(new Label("Tidak ada buku yang terlambat untuk ditampilkan."));
        }

        Button backBtn = new Button("Kembali");
        backBtn.setOnAction(e -> updateMainContent(createHomeContent()));

        whitePanel.getChildren().addAll(table, backBtn);
        updateMainContent(content);
    }

    private void showOverdueBooks() {
        VBox content = createWhitePanel("Overdue Books");
        VBox whitePanel = (VBox) content.getChildren().get(0);

        TableView<Peminjaman> table = new TableView<>();
        table.setPrefHeight(500);

        TableColumn<Peminjaman, String> idPeminjamanCol = new TableColumn<>("ID Peminjaman");
        idPeminjamanCol.setCellValueFactory(new PropertyValueFactory<>("idPeminjaman"));

        TableColumn<Peminjaman, String> idBukuCol = new TableColumn<>("ID Buku");
        idBukuCol.setCellValueFactory(new PropertyValueFactory<>("idBuku"));

        TableColumn<Peminjaman, String> judulBukuCol = new TableColumn<>("Judul Buku");
        judulBukuCol.setCellValueFactory(new PropertyValueFactory<>("judulBuku"));

        TableColumn<Peminjaman, LocalDate> tanggalPeminjamanCol = new TableColumn<>("Tanggal Peminjaman");
        tanggalPeminjamanCol.setCellValueFactory(new PropertyValueFactory<>("tanggalPeminjaman"));

        TableColumn<Peminjaman, LocalDate> tanggalPengembalianCol = new TableColumn<>("Tanggal Pengembalian");
        tanggalPengembalianCol.setCellValueFactory(new PropertyValueFactory<>("tanggalPengembalian"));

        TableColumn<Peminjaman, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("statusPeminjaman"));

        table.getColumns().addAll(idPeminjamanCol, idBukuCol, judulBukuCol, tanggalPeminjamanCol, tanggalPengembalianCol, statusCol);
        loadOverdueBooksData(table);

        Button backBtn = new Button("Kembali");
        backBtn.setOnAction(e -> updateMainContent(createHomeContent()));

        whitePanel.getChildren().addAll(table, backBtn);
        updateMainContent(content);
    }

    private void loadOverdueBooksData(TableView<Peminjaman> table) {
        ObservableList<Peminjaman> overdueList = FXCollections.observableArrayList();
        String sql = "SELECT id_peminjaman, id_buku, judul_buku, tanggal_peminjaman, tanggal_pengembalian, status_peminjaman " +
                     "FROM peminjaman WHERE status_peminjaman = 'Terlambat'";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                overdueList.add(new Peminjaman(
                    rs.getString("id_peminjaman"),
                    rs.getString("id_buku"),
                    rs.getString("judul_buku"),
                    rs.getDate("tanggal_peminjaman").toLocalDate(),
                    rs.getDate("tanggal_pengembalian").toLocalDate(),
                    rs.getString("status_peminjaman")
                ));
            }
            table.setItems(overdueList);

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Gagal memuat data peminjaman: " + e.getMessage());
        }
    }
}