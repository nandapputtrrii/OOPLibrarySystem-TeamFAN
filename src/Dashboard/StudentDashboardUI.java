package Dashboard;

import javafx.application.Application;
import javafx.geometry.*;
import javafx.scene.Node;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import Database.DatabaseConnection;
import Model.Member;
import Model.Buku;
import Model.Peminjaman;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class StudentDashboardUI extends Application {
    public Member member;
    private BorderPane rootPane;
    private Connection currentConnection; // Store the current connection

    // Constructor ini akan dipanggil dari metode main setelah data member didapat
    public StudentDashboardUI(Member member) {
        this.member = member;
    }

    // Constructor default diperlukan oleh JavaFX saat memanggil launch()
    public StudentDashboardUI() {
    }

    @Override
    public void start(Stage primaryStage) {
        rootPane = new BorderPane();

        VBox sidebar = createSidebar();
        HBox header = createHeader();
        Node mainContent = createProfileContent(false); // Tampilan awal adalah profil
        HBox footer = createFooter();

        rootPane.setLeft(sidebar);
        rootPane.setTop(header);
        rootPane.setCenter(mainContent);
        rootPane.setBottom(footer);

        StackPane root = new StackPane(rootPane);
        Scene scene = new Scene(root, 1200, 800);
        primaryStage.setTitle("Student Dashboard - myUMM Library");
        
        primaryStage.setOnCloseRequest(event -> {
            DatabaseConnection.closeConnection(currentConnection); // Pass the current connection
        });
        
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true); // Set the stage to full window
        primaryStage.show();
    }

    // --- BAGIAN UTAMA LAYOUT ---

    private VBox createSidebar() {
        VBox sidebar = new VBox(20);
        sidebar.setPadding(new Insets(0, 0, 0, 0));
        sidebar.setPrefWidth(220);
        sidebar.setStyle("-fx-background-color: #7ea6c7;");

        // Load the logo image
        Image logoImage = new Image(getClass().getResourceAsStream("/laser.jpg"));
        ImageView logoImageView = new ImageView(logoImage);
        logoImageView.setFitWidth(200); // Set the desired width
        logoImageView.setPreserveRatio(true); // Maintain aspect ratio
        logoImageView.setSmooth(true); // Enable smoothing for better quality

        VBox logoBox = new VBox();
        logoBox.setAlignment(Pos.CENTER);
        logoBox.setPadding(new Insets(10, 20, 10, 20));
        logoBox.getChildren().add(logoImageView); // Add the ImageView to the logoBox

        VBox menuBox = new VBox(0);
        String[] menus = {"Profile", "Borrow Book", "Return Book", "Search Book"};
        for (String m : menus) {
            Button btn = new Button(m);
            btn.setPrefWidth(220);
            btn.setAlignment(Pos.CENTER_LEFT);
            btn.setPadding(new Insets(18, 30, 18, 30));
            btn.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-family: 'Segoe UI'; -fx-border-width: 0 0 1 0; -fx-border-color: #bcd2e8;");
            btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: #a2c3de; -fx-text-fill: #2c3e50; -fx-font-size: 16px; -fx-border-width: 0 0 1 0; -fx-border-color: #bcd2e8;"));
            btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-size: 16px; -fx-border-width: 0 0 1 0; -fx-border-color: #bcd2e8;"));
            
            // Event handler untuk setiap tombol menu
            switch (m) {
                case "Profile":
                    btn.setOnAction(e -> updateMainContent(createProfileContent(false)));
                    break;
                case "Borrow Book":
                    btn.setOnAction(e -> updateMainContent(createBorrowBookMenu()));
                    break;
                case "Return Book":
                    btn.setOnAction(e -> updateMainContent(createReturnBookContent()));
                    break;
                case "Search Book":
                    btn.setOnAction(e -> updateMainContent(createSearchBookMenu()));
                    break;
            }

            menuBox.getChildren().add(btn);
        }
        sidebar.getChildren().addAll(logoBox, menuBox);
        return sidebar;
    }

    private HBox createHeader() {
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER_RIGHT);
        header.setStyle("-fx-background-color: #e5e5e5;");
        header.setPadding(new Insets(0, 30, 0, 30));
        header.setPrefHeight(60);

        String accountName = "Account";
        if (member != null && member.nama != null && !member.nama.isEmpty()) {
            accountName = member.nama.split(" ")[0];
        }
        Label accountLabel = new Label(accountName);
        accountLabel.setFont(Font.font("Segoe UI", FontWeight.NORMAL, 16));
        accountLabel.setTextFill(Color.web("#888"));
        
        String initialText = "?";
        if (member != null && member.nama != null && !member.nama.isEmpty()) {
            initialText = String.valueOf(member.nama.charAt(0)).toUpperCase();
        }
        Label accountInitial = new Label(initialText);
        accountInitial.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        accountInitial.setTextFill(Color.web("#888"));
        
        Circle accountCircle = new Circle(18, Color.LIGHTGRAY);
        StackPane accountPane = new StackPane(accountCircle, accountInitial);

        header.getChildren().addAll(accountPane, new Label("  "), accountLabel);
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

    private void updateMainContent(Node content) {
        rootPane.setCenter(content);
    }
    
    // --- KONTEN HALAMAN PROFILE ---

    private VBox createProfileContent(boolean isEditing) {
        VBox mainContent = createWhitePanel("Profile");

        VBox dataPanel = new VBox(18);
        dataPanel.setPadding(new Insets(20));
        dataPanel.setStyle("-fx-background-color: #f7f7f7; -fx-background-radius: 7; -fx-border-color: #e3e3e3; -fx-border-width: 1;");

        Label dataTitle = new Label("Data Mahasiswa");
        dataTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        dataTitle.setTextFill(Color.web("#b0b0b0"));

        HBox dataRow = new HBox(30);
        dataRow.setAlignment(Pos.CENTER_LEFT);

        StackPane photoPane = new StackPane();
        Circle photoCircle = new Circle(70, Color.LIGHTGRAY);
        Label initial = new Label(member != null ? String.valueOf(member.nama.charAt(0)).toUpperCase() : "S");
        initial.setFont(Font.font("Segoe UI", FontWeight.BOLD, 48));
        initial.setTextFill(Color.web("#888"));
        photoPane.getChildren().addAll(photoCircle, initial);

        VBox dataText = new VBox(10);
        if (!isEditing) {
            dataText.getChildren().addAll(
                createProfileLabel(member.nama, 18, FontWeight.BOLD, "#222"),
                createProfileLabel(member.fakultas, 14, FontWeight.NORMAL, "#444"),
                createProfileLabel(member.prodi, 14, FontWeight.NORMAL, "#444"),
                createProfileLabel("HP : " + member.no_hp, 14, FontWeight.NORMAL, "#444"),
                createProfileLabel(member.email, 14, FontWeight.NORMAL, "#444")
            );
            
        Button editProfileBtn = new Button("Edit Profile");
        editProfileBtn.setStyle("-fx-background-color: #b8cfe5; -fx-text-fill: #444; -fx-font-size: 13px; -fx-background-radius: 8;");
            editProfileBtn.setOnAction(e -> updateMainContent(createProfileContent(true)));
        dataText.getChildren().add(editProfileBtn);

        } else {
            TextField nameField = new TextField(member.nama);
            TextField fakultasField = new TextField(member.fakultas);
            TextField prodiField = new TextField(member.prodi);
            TextField hpField = new TextField(member.no_hp);
            TextField emailField = new TextField(member.email);

            Button simpanBtn = new Button("Simpan");
            simpanBtn.setStyle("-fx-background-color: #99c47b; -fx-text-fill: white; -fx-background-radius: 8;");
            simpanBtn.setOnAction(e -> {
                String updateQuery = "UPDATE member SET nama=?, fakultas=?, prodi=?, no_hp=?, email=? WHERE nim=?";
                try (Connection conn = DatabaseConnection.getConnection();
                     PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {
                    
                    pstmt.setString(1, nameField.getText());
                    pstmt.setString(2, fakultasField.getText());
                    pstmt.setString(3, prodiField.getText());
                    pstmt.setString(4, hpField.getText());
                    pstmt.setString(5, emailField.getText());
                    pstmt.setString(6, member.nim);
                    
                    int affectedRows = pstmt.executeUpdate();
                    if (affectedRows > 0) {
                        System.out.println("Profil berhasil diperbarui di database.");
                        member.nama = nameField.getText();
                        member.fakultas = fakultasField.getText();
                        member.prodi = prodiField.getText();
                        member.no_hp = hpField.getText();
                        member.email = emailField.getText();
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                
                updateMainContent(createProfileContent(false));
                rootPane.setTop(createHeader());
            });
            dataText.getChildren().addAll(nameField, fakultasField, prodiField, hpField, emailField, simpanBtn);
        }

        VBox indexPanel = createIndexPanel();
        dataRow.getChildren().addAll(photoPane, dataText, indexPanel);
        dataPanel.getChildren().addAll(dataTitle, dataRow);
        
        VBox contentWrapper = (VBox) mainContent.getChildren().get(0);
        contentWrapper.getChildren().add(dataPanel);

        return mainContent;
    }

    // --- KONTEN HALAMAN BORROW BOOK ---

    private VBox createBorrowBookMenu() {
        VBox mainContent = createWhitePanel("Borrow Book Menu");
        
        VBox menuButtonsContainer = new VBox(20);
        menuButtonsContainer.setAlignment(Pos.CENTER);
        menuButtonsContainer.setPadding(new Insets(20, 0, 20, 0));

        VBox borrowFormButton = createMenuButton("Form Peminjaman Buku", () -> updateMainContent(createBorrowFormContent()));
        VBox currentBorrowedButton = createMenuButton("Buku Sedang Dipinjam", () -> updateMainContent(createCurrentBorrowedContent()));
        VBox borrowHistoryButton = createMenuButton("Riwayat Peminjaman", () -> updateMainContent(createBorrowHistoryContent()));

        menuButtonsContainer.getChildren().addAll(borrowFormButton, currentBorrowedButton, borrowHistoryButton);
        
        // Wrap the menuButtonsContainer in a ScrollPane
        ScrollPane scrollPane = new ScrollPane(menuButtonsContainer);
        scrollPane.setFitToWidth(true); // Make the ScrollPane fit the width of the parent
        
        VBox contentWrapper = (VBox) mainContent.getChildren().get(0);
        contentWrapper.getChildren().add(scrollPane);
        
        return mainContent;
    }
    
    private VBox createBorrowFormContent() {
        VBox mainContent = createWhitePanel("Form Pengisian Peminjaman Buku");
        VBox contentWrapper = (VBox) mainContent.getChildren().get(0);
    
        // --- 1. Membuat TableView untuk menampilkan buku yang tersedia ---
        TableView<Buku> bookTable = new TableView<>();
        bookTable.setPlaceholder(new Label("Tidak ada buku yang tersedia untuk dipinjam."));
    
        // Definisikan kolom-kolom tabel
        TableColumn<Buku, Integer> idCol = new TableColumn<>("ID Buku");
        idCol.setCellValueFactory(new PropertyValueFactory<>("idBuku"));
    
        TableColumn<Buku, String> titleCol = new TableColumn<>("Judul");
        titleCol.setCellValueFactory(new PropertyValueFactory<>("judul"));
        titleCol.setPrefWidth(250); // Beri lebar lebih pada kolom judul
    
        TableColumn<Buku, String> authorCol = new TableColumn<>("Penulis");
        authorCol.setCellValueFactory(new PropertyValueFactory<>("penulis"));
        authorCol.setPrefWidth(150);
    
        TableColumn<Buku, Integer> availableCol = new TableColumn<>("Tersedia");
        availableCol.setCellValueFactory(new PropertyValueFactory<>("salinanTersedia"));
    
        bookTable.getColumns().addAll(idCol, titleCol, authorCol, availableCol);
        
        // Panggil method untuk memuat data dari database ke tabel
        loadAvailableBooks(bookTable);
    
        // Wrap the TableView in a ScrollPane
        ScrollPane scrollPane = new ScrollPane(bookTable);
        scrollPane.setFitToWidth(true); // Make the ScrollPane fit the width of the parent
        scrollPane.setPrefHeight(400); // Increase the preferred height for the scrollable area
    
        // --- 2. Membuat Form Peminjaman ---
        VBox formContainer = createFormContainer();
        
        TextField bookIdField = new TextField();
        bookIdField.setPromptText("ID Buku (pilih dari tabel)");
        bookIdField.setEditable(false); // ID diisi otomatis, tidak bisa diubah manual
        
        TextField bookTitleField = new TextField();
        bookTitleField.setPromptText("Judul Buku (pilih dari tabel)");
        bookTitleField.setEditable(false); // Judul diisi otomatis
        
        TextField borrowerNameField = new TextField(member.nama);
        borrowerNameField.setEditable(false);
        
        TextField borrowerIDField = new TextField(member.nim);
        borrowerIDField.setEditable(false);
        
        DatePicker borrowDatePicker = new DatePicker(LocalDate.now());
        DatePicker returnDatePicker = new DatePicker();
        returnDatePicker.setPromptText("Tanggal Pengembalian");
        
        Button submitButton = new Button("Ajukan Peminjaman");
        submitButton.setStyle("-fx-background-color: #7ea6c7; -fx-text-fill: white;");
    
        // --- 3. Fungsionalitas Auto-fill saat baris tabel diklik ---
        bookTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                // Isi field form dengan data dari baris yang dipilih
                bookIdField.setText(String.valueOf(newSelection.getIdBuku()));
                bookTitleField.setText(newSelection.getJudul());
            }
        });
    
        // Event handler untuk tombol submit (logika Anda tetap sama)
        submitButton.setOnAction(e -> {
            String bookId = bookIdField.getText().trim();
            String bookTitle = bookTitleField.getText().trim();
            LocalDate borrowDate = borrowDatePicker.getValue();
            LocalDate returnDate = returnDatePicker.getValue();
            
            // Validasi input (sekarang lebih simpel karena judul dan id otomatis)
            if (bookId.isEmpty() || returnDate == null) {
                showAlert(Alert.AlertType.WARNING, "Input Error", "Silakan pilih buku dari tabel dan tentukan tanggal pengembalian.");
                return;
            }
            
            if (returnDate.isBefore(borrowDate) || returnDate.isEqual(borrowDate)) {
                showAlert(Alert.AlertType.WARNING, "Date Error", "Tanggal pengembalian harus setelah tanggal peminjaman.");
                return;
            }
            
            if (borrowDate.isBefore(LocalDate.now())) {
                showAlert(Alert.AlertType.WARNING, "Date Error", "Tanggal peminjaman tidak boleh di masa lalu.");
                return;
            }
            
            try (Connection conn = DatabaseConnection.getConnection()) {
                // Cek sekali lagi ketersediaan buku sebelum meminjam
                String checkStockQuery = "SELECT salinan_tersedia FROM buku WHERE id_buku = ?";
                try (PreparedStatement checkStmt = conn.prepareStatement(checkStockQuery)) {
                    checkStmt.setInt(1, Integer.parseInt(bookId));
                    ResultSet rs = checkStmt.executeQuery();
                    if (rs.next() && rs.getInt("salinan_tersedia") <= 0) {
                        showAlert(Alert.AlertType.ERROR, "Stok Habis", "Maaf, buku ini sedang tidak tersedia.");
                        loadAvailableBooks(bookTable); // Refresh tabel
                        return;
                    }
                }
                
                // Lanjutkan proses peminjaman...
                String idPeminjaman = generateIdPeminjaman(member.nama, conn);
                String insertQuery = "INSERT INTO peminjaman (id_peminjaman, id_buku, judul_buku, nama_peminjam, nim_peminjam, tanggal_peminjaman, tanggal_pengembalian, status_peminjaman, tanggal_ajukan) VALUES (?, ?, ?, ?, ?, ?, ?, 'Dipinjam', NOW())";
                
                try (PreparedStatement pstmt = conn.prepareStatement(insertQuery)) {
                    pstmt.setString(1, idPeminjaman);
                    pstmt.setString(2, bookId);
                    pstmt.setString(3, bookTitle);
                    pstmt.setString(4, member.nama);
                    pstmt.setString(5, member.nim);
                    pstmt.setDate(6, java.sql.Date.valueOf(borrowDate));
                    pstmt.setDate(7, java.sql.Date.valueOf(returnDate));
                    
                    int affectedRows = pstmt.executeUpdate();
                    
                    if (affectedRows > 0) {
                        // Kurangi jumlah salinan tersedia di tabel buku
                        String updateStockQuery = "UPDATE buku SET salinan_tersedia = salinan_tersedia - 1 WHERE id_buku = ?";
                        try (PreparedStatement updateStmt = conn.prepareStatement(updateStockQuery)) {
                            updateStmt.setInt(1, Integer.parseInt(bookId));
                            updateStmt.executeUpdate();
                        }
    
                        showAlert(Alert.AlertType.INFORMATION, "Success", "Permintaan peminjaman berhasil diajukan!");
                        clearBorrowForm(bookIdField, bookTitleField, returnDatePicker, borrowDatePicker);
                        loadAvailableBooks(bookTable); // Refresh tabel untuk menunjukkan stok terbaru
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Error", "Gagal menyimpan data peminjaman.");
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Database Error", "Terjadi kesalahan database: " + ex.getMessage());
            }
        });
        
        formContainer.getChildren().addAll(
            new Label("ID Buku:"), bookIdField,
            new Label("Judul Buku:"), bookTitleField,
            new Label("Nama Peminjam:"), borrowerNameField,
            new Label("NIM Peminjam:"), borrowerIDField,
            new Label("Tanggal Peminjaman:"), borrowDatePicker,
            new Label("Tanggal Pengembalian:"), returnDatePicker,
            submitButton
        );
        
        Button backButton = new Button("Kembali ke Menu Peminjaman");
        backButton.setOnAction(e -> updateMainContent(createBorrowBookMenu()));
        
        // Create an HBox to hold the ScrollPane and the formContainer side by side
        HBox hbox = new HBox(20); // 20 is the spacing between the two components
        hbox.getChildren().addAll(scrollPane, formContainer);
        
        // Gabungkan tabel dan form dalam satu VBox
        contentWrapper.getChildren().addAll(new Label("Pilih Buku yang Tersedia:"), hbox, backButton);
        
        return mainContent;
    }
    
    /**
     * Method baru untuk memuat data buku yang tersedia dari database ke TableView.
     * @param table TableView yang akan diisi data.
     */
    private void loadAvailableBooks(TableView<Buku> table) {
        ObservableList<Buku> bookList = FXCollections.observableArrayList();
        // Query hanya mengambil buku yang salinannya masih tersedia (> 0)
        String query = "SELECT id_buku, judul, penulis, salinan_tersedia FROM buku WHERE salinan_tersedia > 0";
    
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
    
            while (rs.next()) {
                bookList.add(new Buku(
                    rs.getInt("id_buku"),
                    rs.getString("judul"),
                    rs.getString("penulis"),
                    rs.getInt("salinan_tersedia")
                ));
            }
            table.setItems(bookList);
    
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Load Data Error", "Gagal memuat data buku dari database.");
        }
    }
    
    
    
    // Method alternatif untuk ID yang lebih sederhana berdasarkan counter global
    
    
    private VBox createCurrentBorrowedContent() {
        VBox mainContent = createWhitePanel("Buku yang Sedang Dipinjam");
        VBox contentWrapper = (VBox) mainContent.getChildren().get(0);
        
        // Create a GridPane for displaying borrowed books
        GridPane borrowedBooksGrid = new GridPane();
        borrowedBooksGrid.setPadding(new Insets(10));
        borrowedBooksGrid.setVgap(8);
        borrowedBooksGrid.setHgap(10);
        
        // Add headers to the GridPane
        borrowedBooksGrid.add(new Label("ID Peminjaman"), 0, 0);
        borrowedBooksGrid.add(new Label("ID Buku"), 1, 0);
        borrowedBooksGrid.add(new Label("Judul Buku"), 2, 0);
        borrowedBooksGrid.add(new Label("Tanggal Peminjaman"), 3, 0);
        borrowedBooksGrid.add(new Label("Tanggal Pengembalian"), 4, 0);
        borrowedBooksGrid.add(new Label("Status Peminjaman"), 5, 0);
        
        String query = "SELECT id_peminjaman, id_buku, judul_buku, tanggal_peminjaman, tanggal_pengembalian, status_peminjaman " +
                           "FROM peminjaman WHERE nim_peminjam = ? AND (status_peminjaman = 'Dipinjam' OR status_peminjaman = 'Terlambat')";
        
        TotalFine totalFine = new TotalFine(); // Use the wrapper class
        try {
            currentConnection = DatabaseConnection.getConnection(); // Get a new connection
            PreparedStatement pstmt = currentConnection.prepareStatement(query);
            pstmt.setString(1, member.nim);
            ResultSet rs = pstmt.executeQuery();
            
            int row = 1; // Start from the second row (first row is for headers)
            while (rs.next()) {
                // Calculate fine logic here
                long fine = calculateFine(rs.getDate("tanggal_pengembalian").toLocalDate());
                totalFine.value += fine; // Accumulate total fine
                
                // Add book details to the GridPane
                borrowedBooksGrid.add(new Label(rs.getString("id_peminjaman")), 0, row);
                borrowedBooksGrid.add(new Label(rs.getString("id_buku")), 1, row);
                borrowedBooksGrid.add(new Label(rs.getString("judul_buku")), 2, row);
                borrowedBooksGrid.add(new Label(rs.getDate("tanggal_peminjaman").toString()), 3, row);
                borrowedBooksGrid.add(new Label(rs.getDate("tanggal_pengembalian").toString()), 4, row);
                borrowedBooksGrid.add(new Label(rs.getString("status_peminjaman")), 5, row);
                row++;
            }
            
            if (row == 1) { // No borrowed books found
                borrowedBooksGrid.add(new Label("Saat ini tidak ada buku yang sedang Anda pinjam."), 0, 1, 6, 1);
            }
            
        } catch (SQLException ex) {
            ex.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Terjadi kesalahan saat mengambil data peminjaman: " + ex.getMessage());
        } finally {
            DatabaseConnection.closeConnection(currentConnection); // Close the connection in the finally block
        }
        
        // Create a VBox to hold the info label and the grid with a background
        VBox borrowedBooksContainer = new VBox();
        borrowedBooksContainer.setStyle("-fx-background-color: #f0f8ff; -fx-padding: 10; -fx-background-radius: 10; -fx-border-color: #7ea6c7; -fx-border-width: 1;"); // Set background color and style
        borrowedBooksContainer.getChildren().addAll(new Label("Daftar Buku yang Sedang Dipinjam:"), borrowedBooksGrid);
        
        contentWrapper.getChildren().add(borrowedBooksContainer);
        
        // Create a separate container for fines
        VBox fineContainer = new VBox(10);
        fineContainer.setStyle("-fx-background-color: #e0f7fa; -fx-padding: 10; -fx-background-radius: 10; -fx-border-color: #00796b; -fx-border-width: 1;");
        
        Label totalFineLabel = new Label("Total Denda: Rp. " + totalFine.value);
        Button payButton = new Button("Bayar Denda");
        payButton.setStyle("-fx-background-color: #a4d6b4; -fx-text-fill: black; -fx-font-weight: bold;");
        payButton.setPadding(new Insets(5, 10, 5, 10));
        
        // Add action for the pay button
        payButton.setOnAction(e -> {
            // Check if there are any fines to pay
            if (totalFine.value <= 0) {
                showAlert(Alert.AlertType.INFORMATION, "Informasi", "Tidak ada denda yang perlu dibayar.");
                return;
            }

            try (Connection conn = DatabaseConnection.getConnection()) {
                // The query correctly targets only 'Terlambat' books for the logged-in user
                String deleteQuery = "DELETE FROM peminjaman WHERE nim_peminjam = ? AND status_peminjaman = 'Terlambat'";
                
                try (PreparedStatement deleteStmt = conn.prepareStatement(deleteQuery)) {
                    deleteStmt.setString(1, member.nim);
                    int affectedRows = deleteStmt.executeUpdate();
                    
                    if (affectedRows > 0) {
                        showAlert(Alert.AlertType.INFORMATION, "Sukses", 
                                  "Denda telah dibayar dan data peminjaman yang terlambat telah dihapus.");
                        // Refresh the current view to show the updated list of borrowed books
                        updateMainContent(createCurrentBorrowedContent());
                    } else {
                        // This case might occur if the status changed between loading the page and clicking the button
                        showAlert(Alert.AlertType.INFORMATION, "Informasi", "Tidak ada data 'Terlambat' yang ditemukan untuk dihapus.");
                    }
                } catch (SQLException ex) {
                    showAlert(Alert.AlertType.ERROR, "Database Error", "Gagal menghapus data peminjaman: " + ex.getMessage());
                }
            } catch (SQLException ex) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Gagal terhubung ke database: " + ex.getMessage());
            }
        });
        
        fineContainer.getChildren().addAll(totalFineLabel, payButton);
        contentWrapper.getChildren().add(fineContainer);
        
        Button backButton = new Button("Kembali ke Menu Peminjaman");
        backButton.setOnAction(e -> updateMainContent(createBorrowBookMenu()));
        contentWrapper.getChildren().add(backButton);
        
        return mainContent;
    }
    
    private long calculateFine(LocalDate returnDate) {
        long daysLate = ChronoUnit.DAYS.between(returnDate, LocalDate.now());
        return (daysLate > 0) ? daysLate * 2000 : 0; // Example fine calculation
    }
    
    private VBox createBorrowHistoryContent() {
        VBox mainContent = createWhitePanel("Riwayat Peminjaman Buku");
        VBox contentWrapper = (VBox) mainContent.getChildren().get(0);
        
        Label info = new Label("Belum ada riwayat peminjaman buku.");
        info.setFont(Font.font("Segoe UI", 16));
        
        // Create a GridPane for displaying returned books
        GridPane returnedBooksGrid = new GridPane();
        returnedBooksGrid.setPadding(new Insets(10));
        returnedBooksGrid.setVgap(8);
        returnedBooksGrid.setHgap(10);
        
        // Add headers to the GridPane
        returnedBooksGrid.add(new Label("ID Peminjaman"), 0, 0);
        returnedBooksGrid.add(new Label("ID Buku"), 1, 0);
        returnedBooksGrid.add(new Label("Judul Buku"), 2, 0);
        returnedBooksGrid.add(new Label("Tanggal Peminjaman"), 3, 0);
        returnedBooksGrid.add(new Label("Tanggal Pengembalian"), 4, 0);
        returnedBooksGrid.add(new Label("Status Peminjaman"), 5, 0);
        
        String query = "SELECT id_peminjaman, id_buku, judul_buku, tanggal_peminjaman, tanggal_pengembalian, status_peminjaman " +
                           "FROM peminjaman WHERE nim_peminjam = ? AND status_peminjaman = 'Dikembalikan'";
        
        try {
            currentConnection = DatabaseConnection.getConnection(); // Get a new connection
            PreparedStatement pstmt = currentConnection.prepareStatement(query);
            pstmt.setString(1, member.nim);
            ResultSet rs = pstmt.executeQuery();
            
            int row = 1; // Start from the second row (first row is for headers)
            while (rs.next()) {
                String idPeminjaman = rs.getString("id_peminjaman");
                String bookId = rs.getString("id_buku");
                String bookTitle = rs.getString("judul_buku");
                LocalDate borrowDate = rs.getDate("tanggal_peminjaman").toLocalDate();
                LocalDate returnDate = rs.getDate("tanggal_pengembalian").toLocalDate();
                String status = rs.getString("status_peminjaman");
                
                // Add book details to the GridPane
                returnedBooksGrid.add(new Label(idPeminjaman), 0, row);
                returnedBooksGrid.add(new Label(bookId), 1, row);
                returnedBooksGrid.add(new Label(bookTitle), 2, row);
                returnedBooksGrid.add(new Label(borrowDate.toString()), 3, row);
                returnedBooksGrid.add(new Label(returnDate.toString()), 4, row);
                
                // Create a label for the status
                Label statusLabel = new Label(status);
                returnedBooksGrid.add(statusLabel, 5, row);
                row++;
            }
            
            if (row == 1) { // No returned books found
                returnedBooksGrid.add(new Label("Saat ini tidak ada buku yang telah dikembalikan."), 0, 1, 6, 1);
            }
            
        } catch (SQLException ex) {
            ex.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", 
                      "Terjadi kesalahan saat mengambil data peminjaman: " + ex.getMessage());
        } finally {
            DatabaseConnection.closeConnection(currentConnection); // Close the connection in the finally block
        }
        
        // Create a VBox to hold the info label and the grid with a background
        VBox returnedBooksContainer = new VBox();
        returnedBooksContainer.setStyle("-fx-background-color: #f0f8ff; -fx-padding: 10; -fx-background-radius: 10; -fx-border-color: #7ea6c7; -fx-border-width: 1;");
        returnedBooksContainer.getChildren().addAll( returnedBooksGrid);
        
        contentWrapper.getChildren().add(returnedBooksContainer);
        
        Button backButton = new Button("Kembali ke Menu Peminjaman");
        backButton.setOnAction(e -> updateMainContent(createBorrowBookMenu()));
        contentWrapper.getChildren().add(backButton);
        
        return mainContent;
    }
    
    // --- KONTEN HALAMAN RETURN BOOK ---
    

    private VBox createReturnBookContent() {
        VBox mainContent = createWhitePanel("Pengembalian Buku");
        VBox contentWrapper = (VBox) mainContent.getChildren().get(0);
        
        // Create a GridPane for displaying borrowed books
        GridPane borrowedBooksGrid = new GridPane();
        borrowedBooksGrid.setPadding(new Insets(10));
        borrowedBooksGrid.setVgap(8);
        borrowedBooksGrid.setHgap(10);
        
        // Add headers to the GridPane
        borrowedBooksGrid.add(new Label("ID Peminjaman"), 0, 0);
        borrowedBooksGrid.add(new Label("ID Buku"), 1, 0);
        borrowedBooksGrid.add(new Label("Judul Buku"), 2, 0);
        borrowedBooksGrid.add(new Label("Tanggal Peminjaman"), 3, 0);
        borrowedBooksGrid.add(new Label("Tanggal Pengembalian"), 4, 0);
        borrowedBooksGrid.add(new Label("Status Peminjaman"), 5, 0);
        
        String query = "SELECT id_peminjaman, id_buku, judul_buku, tanggal_peminjaman, tanggal_pengembalian, status_peminjaman " +
                       "FROM peminjaman WHERE nim_peminjam = ? AND status_peminjaman = 'Dipinjam'";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, member.nim);
            ResultSet rs = pstmt.executeQuery();
            
            int row = 1; // Start from the second row (first row is for headers)
            while (rs.next()) {
                borrowedBooksGrid.add(new Label(rs.getString("id_peminjaman")), 0, row);
                borrowedBooksGrid.add(new Label(rs.getString("id_buku")), 1, row);
                borrowedBooksGrid.add(new Label(rs.getString("judul_buku")), 2, row);
                borrowedBooksGrid.add(new Label(rs.getDate("tanggal_peminjaman").toString()), 3, row);
                borrowedBooksGrid.add(new Label(rs.getDate("tanggal_pengembalian").toString()), 4, row);
                borrowedBooksGrid.add(new Label(rs.getString("status_peminjaman")), 5, row);
                row++;
            }
            
            if (row == 1) { // No borrowed books found
                borrowedBooksGrid.add(new Label("Saat ini tidak ada buku yang sedang Anda pinjam."), 0, 1, 6, 1);
            }
            
        } catch (SQLException ex) {
            ex.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Terjadi kesalahan saat mengambil data peminjaman: " + ex.getMessage());
        }

        // Create a VBox to hold the info label and the grid with a background
        VBox borrowedBooksContainer = new VBox();
        borrowedBooksContainer.setStyle("-fx-background-color: #f0f8ff; -fx-padding: 10; -fx-background-radius: 10; -fx-border-color: #7ea6c7; -fx-border-width: 1;"); // Set background color and style
        borrowedBooksContainer.getChildren().addAll(new Label("Daftar Buku yang Sedang Dipinjam:"), borrowedBooksGrid);
        
        contentWrapper.getChildren().add(borrowedBooksContainer);
        
        // Form for returning books
        VBox formContainer = createFormContainer();
        TextField idPeminjamanField = new TextField();
        idPeminjamanField.setPromptText("Masukkan ID Peminjaman atau ID Buku");
        
        Button returnButton = new Button("Proses Pengembalian");
        returnButton.setStyle("-fx-background-color: #7ea6c7; -fx-text-fill: white;");
        returnButton.setOnAction(e -> {
            String idPeminjaman = idPeminjamanField.getText().trim();
            if (idPeminjaman.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Input Error", "Silakan masukkan ID Peminjaman atau ID Buku.");
                return;
            }
            
            // Process the return in the database
            try (Connection conn = DatabaseConnection.getConnection()) {
                String updateQuery = "UPDATE peminjaman SET status_peminjaman = 'Dikembalikan', tanggal_pengembalian = NOW() WHERE id_peminjaman = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {
                    pstmt.setString(1, idPeminjaman);
                    int affectedRows = pstmt.executeUpdate();
                    
                    if (affectedRows > 0) {
                        showAlert(Alert.AlertType.INFORMATION, "Sukses", "Buku telah dikembalikan.");
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Error", "ID Peminjaman tidak ditemukan.");
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Database Error", "Terjadi kesalahan saat memproses pengembalian: " + ex.getMessage());
            }
        });
        
        formContainer.getChildren().addAll(new Label("ID Peminjaman/Buku:"), idPeminjamanField, returnButton);
        
        Button backButton = new Button("Kembali ke Profile");
        backButton.setOnAction(e -> updateMainContent(createProfileContent(false)));
        
        contentWrapper.getChildren().addAll(formContainer, backButton);
        
        return mainContent;
    }

    // --- KONTEN HALAMAN SEARCH BOOK ---

    private VBox createSearchForm(String mode) {
        VBox mainContent = createWhitePanelWithConstraints("Form Pencarian Berdasarkan " + mode);
        VBox contentWrapper = (VBox) mainContent.getChildren().get(0);
        
        // === KUNCI SOLUSI: Set constraint pada whitePanel ===
        contentWrapper.setPrefHeight(600);  // Set tinggi panel putih
        contentWrapper.setMaxHeight(600);   // Batas maksimum panel putih
        
        // Create form elements
        VBox formContainer = createFormContainer();
    
        TextField searchField = new TextField();
        searchField.setPromptText("Masukkan " + mode + " buku...");
        searchField.setPrefWidth(300);
    
        Button searchButton = new Button("Cari Buku");
        searchButton.setStyle("-fx-background-color: #7ea6c7; -fx-text-fill: white;");
    
        TextArea resultsArea = new TextArea("Hasil pencarian akan ditampilkan di sini.");
        resultsArea.setEditable(false);
        resultsArea.setWrapText(true);
    
        // Database search logic
        searchButton.setOnAction(e -> {
            String keyword = searchField.getText();
            if (keyword.isEmpty()) {
                resultsArea.setText("Silakan masukkan kata kunci pencarian.");
                return;
            }
    
            StringBuilder resultText = new StringBuilder();
            
            String dbColumn;
            if (mode.equalsIgnoreCase("Judul")) {
                dbColumn = "judul";
            } else if (mode.equalsIgnoreCase("Penulis")) {
                dbColumn = "penulis";
            } else {
                dbColumn = "kategori";
            }
    
            String sql = "SELECT * FROM buku WHERE " + dbColumn + " LIKE ?";
            String dbUrl = "jdbc:mysql://localhost:3306/perpustakaan";
            String dbUser = "root";
            String dbPassword = "";
    
            try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                
                pstmt.setString(1, "%" + keyword + "%");
                ResultSet rs = pstmt.executeQuery();
                int count = 0;
    
                while (rs.next()) {
                    resultText.append("-------------------------------------------------\n");
                    resultText.append("ID Buku         : ").append(rs.getInt("id_buku")).append("\n");
                    resultText.append("Judul           : ").append(rs.getString("judul")).append("\n");
                    resultText.append("Penulis         : ").append(rs.getString("penulis")).append("\n");
                    resultText.append("Tanggal Terbit  : ").append(rs.getDate("tanggal_terbit")).append("\n");
                    resultText.append("ISBN            : ").append(rs.getString("isbn")).append("\n");
                    resultText.append("Kategori        : ").append(rs.getString("kategori")).append("\n");
                    resultText.append("Salinan Tersedia: ").append(rs.getInt("salinan_tersedia")).append(" / ").append(rs.getInt("total_salinan")).append("\n");
                    resultText.append("-------------------------------------------------\n\n");
                    count++;
                }
    
                if (count == 0) {
                    resultsArea.setText("Tidak ada buku yang ditemukan dengan " + mode + ": '" + keyword + "'");
                } else {
                    resultsArea.setText(resultText.toString());
                }
    
            } catch (SQLException ex) {
                ex.printStackTrace();
                resultsArea.setText("Terjadi kesalahan saat terhubung ke database.\n" +
                                    "Pesan Error: " + ex.getMessage());
            }
        });
    
        formContainer.getChildren().addAll(
            new Label("Cari Berdasarkan " + mode + ":"), 
            searchField, 
            searchButton, 
            resultsArea
        );
        
        Button backButton = new Button("Kembali ke Menu Pencarian");
        backButton.setOnAction(e -> updateMainContent(createSearchBookMenu()));
    
        // === SOLUSI UTAMA: Proper ScrollPane setup ===
        VBox scrollableContent = new VBox(20);
        scrollableContent.setPadding(new Insets(20));
        scrollableContent.getChildren().addAll(formContainer, backButton);
        
        ScrollPane scrollPane = new ScrollPane(scrollableContent);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(false);
        
        // PENTING: Hitung tinggi yang tersedia setelah title
        // Title + padding ≈ 80px, sisa space untuk scroll
        double availableHeight = 600 - 80; // 520px untuk scrollable area
        scrollPane.setPrefHeight(availableHeight);
        scrollPane.setMaxHeight(availableHeight);
        
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        
        // Set VBox grow priority agar ScrollPane mengisi ruang yang tersedia
        VBox.setVgrow(scrollPane, Priority.ALWAYS);
        
        contentWrapper.getChildren().add(scrollPane);
        
        return mainContent;
    }
    
    // === ALTERNATIF: Jika masih bermasalah, ganti method createWhitePanel ===
    private VBox createWhitePanelWithConstraints(String title) {
        VBox mainContent = new VBox(18);
        mainContent.setPadding(new Insets(30));
        mainContent.setStyle("-fx-background-color: #f8f9fa;");
        
        // PENTING: Set constraint pada mainContent
        mainContent.setPrefHeight(700); // Sesuaikan dengan tinggi window
        mainContent.setMaxHeight(700);
    
        VBox whitePanel = new VBox(18);
        whitePanel.setPadding(new Insets(30));
        whitePanel.setAlignment(Pos.TOP_LEFT);
        whitePanel.setStyle("-fx-background-color: #fff; -fx-background-radius: 10;");
        
        // PENTING: Set constraint pada whitePanel juga
        whitePanel.setPrefHeight(640);  // Sedikit lebih kecil dari mainContent
        whitePanel.setMaxHeight(640);
    
        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 22));
        titleLabel.setTextFill(Color.web("#888"));
        titleLabel.setPadding(new Insets(0, 0, 18, 0));
        
        whitePanel.getChildren().add(titleLabel);
        mainContent.getChildren().add(whitePanel);
        return mainContent;
    }

    // --- HELPER METHODS (METODE BANTU) ---
    
    private VBox createWhitePanel(String title) {
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
    
    private VBox createFormContainer() {
        VBox formContainer = new VBox(10);
        formContainer.setStyle("-fx-background-color: #f7f7f7; -fx-background-radius: 7; -fx-border-radius: 7; -fx-border-color: #e3e3e3; -fx-border-width: 1;");
        formContainer.setPadding(new Insets(20));
        return formContainer;
    }

    private Label createProfileLabel(String text, int size, FontWeight weight, String color) {
        Label label = new Label(text);
        label.setFont(Font.font("Segoe UI", weight, size));
        label.setTextFill(Color.web(color));
        return label;
    }
    
    private VBox createIndexPanel() {
        VBox indexPanel = new VBox(10);
        indexPanel.setAlignment(Pos.TOP_CENTER);
        indexPanel.setPrefSize(220, 180);
        indexPanel.setStyle("-fx-background-color: #f2f2f2; -fx-background-radius: 7; -fx-border-color: #cccccc; -fx-border-width: 1;");
        Label indexLabel = new Label("Index Peminjaman Buku");
        indexLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        indexLabel.setTextFill(Color.web("#b0b0b0"));
        indexPanel.getChildren().add(indexLabel);
        return indexPanel;
    }
    
    private VBox createMenuButton(String text, Runnable action) {
        VBox button = new VBox(10);
        button.setAlignment(Pos.CENTER);
        button.setPadding(new Insets(20));
        button.setPrefSize(200, 180);
        button.setStyle("-fx-background-color: #f7f7f7; -fx-background-radius: 10; -fx-border-radius: 10; -fx-border-color: #e3e3e3; -fx-border-width: 1; -fx-cursor: hand;");

        try {
            Image image = new Image("file:src/heli.jpg"); // Ganti dengan path gambar Anda
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(80);
            imageView.setFitHeight(80);
            imageView.setPreserveRatio(true);
            button.getChildren().add(imageView);
        } catch (Exception e) {
            Circle placeholder = new Circle(40, Color.web("#bdc3c7"));
            button.getChildren().add(placeholder);
        }

        Label label = new Label(text);
        label.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        label.setTextFill(Color.web("#444"));
        label.setTextAlignment(TextAlignment.CENTER);
        button.getChildren().add(label);

        button.setOnMouseEntered(e -> button.setStyle("-fx-background-color: #e8f4fd; -fx-background-radius: 10; -fx-border-color: #7ea6c7; -fx-border-width: 2; -fx-cursor: hand;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-background-color: #f7f7f7; -fx-background-radius: 10; -fx-border-color: #e3e3e3; -fx-border-width: 1; -fx-cursor: hand;"));
        button.setOnMouseClicked(e -> action.run());

        return button;
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // --- MAIN METHOD & INITIALIZATION ---

    private static Member loggedInMember;

    public static void main(String[] args) {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM member LIMIT 1")) {
            
            if (conn == null) {
                System.err.println("Tidak bisa mendapatkan koneksi ke database. Aplikasi berhenti.");
                return;
            }
            
                if (rs.next()) {
                loggedInMember = new Member(
                        rs.getString("nama"),
                        rs.getString("fakultas"),
                        rs.getString("prodi"),
                        rs.getString("no_hp"),
                        rs.getString("email"),
                        rs.getString("nim")
                    );
                }
        } catch (SQLException e) {
                e.printStackTrace();
            return;
        }

        if (loggedInMember == null) {
            System.out.println("Tidak ada data member yang ditemukan di database. Aplikasi tidak dapat dimulai.");
            return;
        }

        launch(args);
    }

    @Override
    public void init() throws Exception {
        super.init();
        if (loggedInMember != null) {
            this.member = loggedInMember;
        } else {
            // Ini adalah fallback, seharusnya tidak pernah terjadi jika main() berhasil
            // dan constructor default dipanggil oleh JavaFX
            Application.Parameters params = getParameters();
            if (params.getRaw().isEmpty()) {
                 // Jika dijalankan langsung tanpa parameter, coba ambil dari static member
                 if(loggedInMember != null) {
                     this.member = loggedInMember;
                 } else {
                     throw new IllegalStateException("Aplikasi tidak dapat dimulai tanpa data member.");
                 }
            }
        }
    }

    private void clearBorrowForm(TextField bookIdField, TextField bookTitleField, DatePicker returnDatePicker, DatePicker borrowDatePicker) {
        bookIdField.clear();
        bookTitleField.clear();
        returnDatePicker.setValue(null);
        borrowDatePicker.setValue(LocalDate.now()); // Reset to today's date or any default value
    }

    public VBox createSearchBookMenu() {
        VBox mainContent = createWhitePanel("Search Book Menu");
        
        VBox menuButtonsContainer = new VBox(20);
        menuButtonsContainer.setAlignment(Pos.CENTER);
        menuButtonsContainer.setPadding(new Insets(20, 0, 20, 0));

        VBox searchByTitle = createMenuButton("Cari Berdasarkan Judul", () -> updateMainContent(createSearchForm("Judul")));
        VBox searchByAuthor = createMenuButton("Cari Berdasarkan Penulis", () -> updateMainContent(createSearchForm("Penulis")));
        VBox searchByCategory = createMenuButton("Cari Berdasarkan Genre", () -> updateMainContent(createSearchForm("Genre")));

        menuButtonsContainer.getChildren().addAll(searchByTitle, searchByAuthor, searchByCategory);
        
        VBox contentWrapper = (VBox) mainContent.getChildren().get(0);
        contentWrapper.getChildren().add(menuButtonsContainer);
        
        return mainContent;
    }

    private String generateIdPeminjaman(String namaPeminjam, Connection conn) throws SQLException {
        // Extract initials from the borrower's name
        String[] nameParts = namaPeminjam.split(" ");
        StringBuilder initials = new StringBuilder();
        for (String part : nameParts) {
            if (!part.isEmpty()) {
                initials.append(part.charAt(0)); // Take the first character of each part
            }
        }

        // Generate a unique identifier (e.g., current timestamp)
        String uniqueIdentifier = String.valueOf(System.currentTimeMillis()).substring(8); // Get last 2 digits of timestamp

        // Retrieve the last sequence number from the database
        String query = "SELECT MAX(CAST(SUBSTRING(id_peminjaman, LENGTH(?) + 3) AS UNSIGNED)) AS lastNumber FROM peminjaman WHERE id_peminjaman LIKE ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, initials.toString());
            pstmt.setString(2, initials.toString() + "%");
            ResultSet rs = pstmt.executeQuery();
            
            int sequenceNumber = 1; // Default to 001
            if (rs.next()) {
                sequenceNumber = rs.getInt("lastNumber") + 1; // Increment the last number found
            }

            // Format the ID
            return String.format("%sXX%03d", initials.toString(), sequenceNumber); // Format as Initials + XX + 001
        }
    }
}
class TotalFine {
    public long value = 0;
}
