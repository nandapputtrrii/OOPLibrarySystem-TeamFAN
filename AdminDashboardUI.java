import javafx.application.Application;
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

public class AdminDashboardUI extends Application {

    private Stage primaryStage;
    private VBox homeContent;
    private VBox requestsContainer;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        VBox sidebar = new VBox();
        sidebar.setPrefWidth(200);
        sidebar.setStyle("-fx-background-color: #7FB3D5;");
        sidebar.setPadding(new Insets(20));
        sidebar.setSpacing(15);

        Label logo = new Label("L@ser\nmyUMM Library");
        logo.setTextFill(Color.WHITE);
        logo.setFont(Font.font("Arial", 18));
        logo.setAlignment(Pos.CENTER_LEFT);

        Label home = new Label("Home");
        Label manageBook = new Label("Manage Book");
        Label manageUser = new Label("Manage User");

        for (Label label : new Label[]{home, manageBook, manageUser}) {
            label.setTextFill(Color.WHITE);
            label.setFont(Font.font(14));
        }

        home.setOnMouseClicked(e -> updateMainContent(getHomeContent()));
        manageUser.setOnMouseClicked(e -> showManageUserMenu());
        manageBook.setOnMouseClicked(e -> showManageBookMenu());

        sidebar.getChildren().addAll(logo, home, manageBook, manageUser);

        BorderPane mainLayout = new BorderPane();
        mainLayout.setLeft(sidebar);
        mainLayout.setCenter(getHomeContent());

        Scene scene = new Scene(mainLayout, 1000, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Admin Dashboard - myUMM Library");
        primaryStage.show();
    }

    private VBox getHomeContent() {
        if (homeContent == null) {
            HBox header = new HBox();
            header.setPrefHeight(60);
            header.setStyle("-fx-background-color: #E5E5E5;");
            header.setPadding(new Insets(0, 30, 0, 0));
            header.setAlignment(Pos.CENTER_RIGHT);
            Label accountLabel = new Label("Account");
            header.getChildren().add(accountLabel);

            VBox homeMenu = new VBox(20);
            homeMenu.setPadding(new Insets(40));
            homeMenu.setAlignment(Pos.TOP_CENTER);

            HBox menuGrid = new HBox(30);
            menuGrid.setAlignment(Pos.CENTER);

            // Create menu items
            VBox mahasiswaItem = createMenuItem("Data Mahasiswa", "👨‍🎓");
            VBox bukuItem = createMenuItem("Data Buku", "📚");
            VBox availableItem = createMenuItem("Available Book", "📖");
            VBox lateItem = createMenuItem("Late Return", "⏰");
            VBox actionItem = createMenuItem("Action", "⚙️");

            // Click events
            mahasiswaItem.setOnMouseClicked(e -> showStudentTable(false));
            bukuItem.setOnMouseClicked(e -> showManageBookMenu());
            availableItem.setOnMouseClicked(e -> showAvailableBookPage());
            lateItem.setOnMouseClicked(e -> showLateReturnPage());
            actionItem.setOnMouseClicked(e -> showActionPage());

            menuGrid.getChildren().addAll(mahasiswaItem, bukuItem, availableItem, lateItem, actionItem);
            homeMenu.getChildren().add(menuGrid);

            HBox footer = new HBox();
            footer.setPrefHeight(30);
            footer.setStyle("-fx-background-color: #F2F2F2;");
            footer.setAlignment(Pos.CENTER);
            Label address = new Label("Jalan Raya Tlogomas No. 246, Jatimulyo, Lowokwaru, Malang, Jawa Timur, 65144");
            address.setFont(Font.font(11));
            footer.getChildren().add(address);

            homeContent = new VBox();
            homeContent.setSpacing(10);
            homeContent.getChildren().addAll(header, homeMenu, footer);
            homeContent.setStyle("-fx-background-color: #FAFAFA;");
        }
        return homeContent;
    }

    private VBox getHomeContent2() {
        VBox homeContent = new VBox();
        homeContent.setPadding(new Insets(30));
        homeContent.setSpacing(20);
        homeContent.setStyle("-fx-background-color: #f5f5f5;");

        // Welcome section
        Label welcomeLabel = new Label("Welcome to Admin Dashboard");
        welcomeLabel.setFont(Font.font("System", FontWeight.BOLD, 28));
        welcomeLabel.setTextFill(Color.web("#1f2937"));

        Label subLabel = new Label("myUMM Library Management System");
        subLabel.setFont(Font.font("System", FontWeight.NORMAL, 16));
        subLabel.setTextFill(Color.web("#6b7280"));

        // Quick stats or overview
        VBox statsCard = new VBox();
        statsCard.setPadding(new Insets(25));
        statsCard.setSpacing(15);
        statsCard.setStyle("-fx-background-color: white; -fx-background-radius: 10; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);");

        Label statsTitle = new Label("Quick Overview");
        statsTitle.setFont(Font.font("System", FontWeight.BOLD, 18));
        statsTitle.setTextFill(Color.web("#1f2937"));

        Label overview = new Label("• Manage users and their library access\n" +
                "• Handle book management and inventory\n" +
                "• Process user requests and actions");
        overview.setFont(Font.font("System", FontWeight.NORMAL, 14));
        overview.setTextFill(Color.web("#4b5563"));

        statsCard.getChildren().addAll(statsTitle, overview);
        homeContent.getChildren().addAll(welcomeLabel, subLabel, statsCard);

        return homeContent;
    }

    private void updateMainContent(VBox content) {
        BorderPane mainLayout = (BorderPane) primaryStage.getScene().getRoot();
        mainLayout.setCenter(content);
    }

    private void showManageUserMenu() {
        VBox content = new VBox(18);
        content.setPadding(new Insets(30));
        content.setStyle("-fx-background-color: #f8f9fa;");

        VBox whitePanel = new VBox(25);
        whitePanel.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
        whitePanel.setPadding(new Insets(30));
        whitePanel.setAlignment(Pos.TOP_LEFT);

        Label title = new Label("Manage User");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        title.setTextFill(Color.web("#888"));

        HBox menuGrid = new HBox(25);
        menuGrid.setAlignment(Pos.CENTER_LEFT);

        VBox item1 = createMenuItem("Tampilkan Semua Mahasiswa", "👥");
        item1.setOnMouseClicked(e -> showStudentTable(true));

        VBox item2 = createMenuItem("Cari Mahasiswa", "🔍");
        item2.setOnMouseClicked(e -> showStudentSearch());

        VBox item3 = createMenuItem("Actions Request", "📦");
        item3.setOnMouseClicked(e -> showUserManageContent());

        VBox item4 = createMenuItem("Add Student", "➕");
        item4.setOnMouseClicked(e -> showAddStudentForm());

        menuGrid.getChildren().addAll(item1, item2, item3, item4);
        whitePanel.getChildren().addAll(title, menuGrid);
        content.getChildren().add(whitePanel);

        updateMainContent(content);
    }

    private void showUserManageContent() {
        VBox userContent = createUserManageContent();
        updateMainContent(userContent);
    }

    // Ganti method showManageBookMenu() yang ada dengan implementasi ini:

    private void showManageBookMenu() {
        VBox content = new VBox(18);
        content.setPadding(new Insets(30));
        content.setStyle("-fx-background-color: #f8f9fa;");

        // Header dengan Account
        HBox header = new HBox();
        header.setPrefHeight(60);
        header.setStyle("-fx-background-color: #E5E5E5;");
        header.setPadding(new Insets(0, 30, 0, 0));
        header.setAlignment(Pos.CENTER_RIGHT);

        // Profile circle
        Region profileCircle = new Region();
        profileCircle.setPrefSize(25, 25);
        profileCircle.setStyle("-fx-background-color: #888888; -fx-background-radius: 12.5;");

        Label accountLabel = new Label("Account");
        accountLabel.setPadding(new Insets(0, 0, 0, 8));

        HBox accountBox = new HBox(5);
        accountBox.setAlignment(Pos.CENTER);
        accountBox.getChildren().addAll(profileCircle, accountLabel);

        header.getChildren().add(accountBox);

        VBox whitePanel = new VBox(25);
        whitePanel.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
        whitePanel.setPadding(new Insets(30));
        whitePanel.setAlignment(Pos.TOP_LEFT);

        Label title = new Label("Manage Book");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        title.setTextFill(Color.web("#888"));

        // Grid untuk menu items
        GridPane menuGrid = new GridPane();
        menuGrid.setHgap(25);
        menuGrid.setVgap(25);
        menuGrid.setAlignment(Pos.CENTER_LEFT);
        menuGrid.setPadding(new Insets(20, 0, 0, 0));

        // Buat menu items sesuai gambar
        VBox item1 = createBookMenuItem("Tampilkan Semua\nBuku", "📖", "#B5B5B5");
        item1.setOnMouseClicked(e -> showAllBooksTable());

        VBox item2 = createBookMenuItem("Cari Buku", "🔍", "#B5B5B5");
        item2.setOnMouseClicked(e -> showSearchBookPage());

        VBox item3 = createBookMenuItem("Hapus Buku", "🗑️", "#B5B5B5");
        item3.setOnMouseClicked(e -> showDeleteBookPage());

        VBox item4 = createBookMenuItem("Tambah Buku", "➕", "#B5B5B5");
        item4.setOnMouseClicked(e -> showAddBookForm());

        // Susun dalam grid 2x2
        menuGrid.add(item1, 0, 0);
        menuGrid.add(item2, 1, 0);
        menuGrid.add(item3, 0, 1);
        menuGrid.add(item4, 1, 1);

        whitePanel.getChildren().addAll(title, menuGrid);

        // Footer
        HBox footer = new HBox();
        footer.setPrefHeight(30);
        footer.setStyle("-fx-background-color: #F2F2F2;");
        footer.setAlignment(Pos.CENTER);
        Label address = new Label("Jalan Raya Tlogomas No. 246, Jatimulyo, Lowokwaru, Malang, Jawa Timur, 65144");
        address.setFont(Font.font(11));
        footer.getChildren().add(address);

        VBox mainContent = new VBox();
        mainContent.getChildren().addAll(header, whitePanel);
        VBox.setVgrow(whitePanel, Priority.ALWAYS);

        content.getChildren().addAll(mainContent, footer);
        updateMainContent(content);
    }

    // Method untuk membuat menu item khusus untuk book management
    private VBox createBookMenuItem(String labelText, String iconText, String bgColor) {
        VBox box = new VBox(10);
        box.setPadding(new Insets(25));
        box.setAlignment(Pos.CENTER);
        box.setStyle("-fx-background-color: " + bgColor + "; -fx-background-radius: 8;");
        box.setPrefSize(160, 140);

        Label icon = new Label(iconText);
        icon.setFont(Font.font(35));

        Label text = new Label(labelText);
        text.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        text.setWrapText(true);
        text.setTextAlignment(TextAlignment.CENTER);
        text.setTextFill(Color.WHITE);

        box.getChildren().addAll(icon, text);

        // Hover effect
        box.setOnMouseEntered(e -> box.setStyle("-fx-background-color: derive(" + bgColor + ", -10%); -fx-background-radius: 8; -fx-cursor: hand;"));
        box.setOnMouseExited(e -> box.setStyle("-fx-background-color: " + bgColor + "; -fx-background-radius: 8;"));

        return box;
    }

    // Method untuk menampilkan semua buku
    private void showAllBooksTable() {
        VBox content = new VBox(18);
        content.setPadding(new Insets(30));
        content.setStyle("-fx-background-color: #f8f9fa;");

        VBox whitePanel = new VBox(15);
        whitePanel.setPadding(new Insets(30));
        whitePanel.setStyle("-fx-background-color: white; -fx-background-radius: 10;");

        Label title = new Label("Data Buku");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        title.setTextFill(Color.web("#888"));

        TableView<Book> table = new TableView<>();
        table.setPrefHeight(300);

        TableColumn<Book, String> titleCol = new TableColumn<>("Judul");
        TableColumn<Book, String> authorCol = new TableColumn<>("Pengarang");
        TableColumn<Book, String> isbnCol = new TableColumn<>("ISBN");
        TableColumn<Book, String> statusCol = new TableColumn<>("Status");
        TableColumn<Book, Void> actionCol = new TableColumn<>("Aksi");

        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorCol.setCellValueFactory(new PropertyValueFactory<>("author"));
        isbnCol.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        actionCol.setCellFactory(col -> new TableCell<>() {
            private final Button btn = new Button("Detail");

            {
                btn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
                btn.setOnAction(e -> {
                    Book book = getTableView().getItems().get(getIndex());
                    showBookDetailForm(book);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });

        titleCol.setPrefWidth(200);
        authorCol.setPrefWidth(150);
        isbnCol.setPrefWidth(120);
        statusCol.setPrefWidth(100);
        actionCol.setPrefWidth(80);

        table.getColumns().addAll(titleCol, authorCol, isbnCol, statusCol, actionCol);

        // Sample data
        table.getItems().addAll(
                new Book("Pemrograman Java", "Budi Raharjo", "978-123-456-789", "Tersedia"),
                new Book("Database Management", "Siti Nurhaliza", "978-987-654-321", "Dipinjam"),
                new Book("Web Development", "Ahmad Dahlan", "978-555-666-777", "Tersedia")
        );

        Button backBtn = new Button("← Kembali");
        backBtn.setOnAction(e -> showManageBookMenu());

        whitePanel.getChildren().addAll(title, table, backBtn);
        content.getChildren().add(whitePanel);
        updateMainContent(content);
    }

    // Method untuk pencarian buku
    private void showSearchBookPage() {
        VBox content = new VBox(18);
        content.setPadding(new Insets(30));
        content.setStyle("-fx-background-color: #f8f9fa;");

        VBox whitePanel = new VBox(25);
        whitePanel.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
        whitePanel.setPadding(new Insets(30));
        whitePanel.setAlignment(Pos.TOP_CENTER);

        Label title = new Label("Cari Buku");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        title.setTextFill(Color.web("#888"));

        VBox searchBox = new VBox(10);
        searchBox.setAlignment(Pos.CENTER);
        searchBox.setPadding(new Insets(20));

        Label label = new Label("Masukkan judul/pengarang/ISBN");
        TextField inputField = new TextField();
        inputField.setMaxWidth(300);
        Button searchButton = new Button("Cari");
        searchButton.setStyle("-fx-background-color: #82c4c3; -fx-text-fill: white;");

        Button backBtn = new Button("← Kembali");
        backBtn.setOnAction(e -> showManageBookMenu());

        searchBox.getChildren().addAll(label, inputField, searchButton);
        whitePanel.getChildren().addAll(title, searchBox, backBtn);
        content.getChildren().add(whitePanel);

        updateMainContent(content);
    }

    // Method untuk hapus buku
    private void showDeleteBookPage() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(30));
        content.setAlignment(Pos.CENTER);
        content.setStyle("-fx-background-color: #f8f9fa;");

        Label label = new Label("Hapus Buku - Fitur dalam pengembangan");
        label.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        Button backButton = new Button("← Kembali");
        backButton.setOnAction(e -> showManageBookMenu());

        content.getChildren().addAll(label, backButton);
        updateMainContent(content);
    }

    // Method untuk tambah buku
    private void showAddBookForm() {
        VBox content = new VBox(18);
        content.setPadding(new Insets(30));
        content.setStyle("-fx-background-color: #f8f9fa;");

        VBox whitePanel = new VBox(25);
        whitePanel.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
        whitePanel.setPadding(new Insets(30));

        Label title = new Label("Tambah Buku Baru");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        title.setTextFill(Color.web("#888"));

        GridPane grid = new GridPane();
        grid.setVgap(15);
        grid.setHgap(10);
        grid.setPadding(new Insets(20));

        TextField titleField = new TextField();
        TextField authorField = new TextField();
        TextField isbnField = new TextField();
        TextField publisherField = new TextField();

        titleField.setMaxWidth(300);
        authorField.setMaxWidth(300);
        isbnField.setMaxWidth(300);
        publisherField.setMaxWidth(300);

        titleField.setPromptText("Masukkan judul buku");
        authorField.setPromptText("Masukkan nama pengarang");
        isbnField.setPromptText("Masukkan ISBN");
        publisherField.setPromptText("Masukkan penerbit");

        grid.addRow(0, new Label("Judul:"), titleField);
        grid.addRow(1, new Label("Pengarang:"), authorField);
        grid.addRow(2, new Label("ISBN:"), isbnField);
        grid.addRow(3, new Label("Penerbit:"), publisherField);

        Button addBtn = new Button("Tambah Buku");
        addBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        Button backBtn = new Button("← Kembali");
        backBtn.setOnAction(e -> showManageBookMenu());

        addBtn.setOnAction(e -> {
            String judul = titleField.getText().trim();
            String pengarang = authorField.getText().trim();
            String isbn = isbnField.getText().trim();
            String penerbit = publisherField.getText().trim();

            if (judul.isEmpty() || pengarang.isEmpty() || isbn.isEmpty() || penerbit.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Input Error");
                alert.setHeaderText("Mohon isi semua field");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Berhasil");
                alert.setHeaderText("Buku berhasil ditambahkan!");
                alert.showAndWait();
                showManageBookMenu();
            }
        });

        HBox buttons = new HBox(10, addBtn, backBtn);
        grid.add(buttons, 0, 4, 2, 1);

        whitePanel.getChildren().addAll(title, grid);
        content.getChildren().add(whitePanel);

        updateMainContent(content);
    }

    // Method untuk detail buku
    private void showBookDetailForm(Book book) {
        VBox content = new VBox(18);
        content.setPadding(new Insets(30));
        content.setStyle("-fx-background-color: #f8f9fa;");

        VBox whitePanel = new VBox(25);
        whitePanel.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
        whitePanel.setPadding(new Insets(30));

        Label title = new Label("Detail Buku");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        title.setTextFill(Color.web("#888"));

        GridPane grid = new GridPane();
        grid.setVgap(15);
        grid.setHgap(10);
        grid.setPadding(new Insets(20));

        TextField titleField = new TextField(book.getTitle());
        TextField authorField = new TextField(book.getAuthor());
        TextField isbnField = new TextField(book.getIsbn());
        TextField statusField = new TextField(book.getStatus());

        titleField.setMaxWidth(300);
        authorField.setMaxWidth(300);
        isbnField.setMaxWidth(300);
        statusField.setMaxWidth(300);

        grid.addRow(0, new Label("Judul:"), titleField);
        grid.addRow(1, new Label("Pengarang:"), authorField);
        grid.addRow(2, new Label("ISBN:"), isbnField);
        grid.addRow(3, new Label("Status:"), statusField);

        Button updateBtn = new Button("Update Buku");
        updateBtn.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
        Button deleteBtn = new Button("Hapus Buku");
        deleteBtn.setStyle("-fx-background-color: red; -fx-text-fill: white;");
        Button backBtn = new Button("← Kembali");
        backBtn.setOnAction(e -> showAllBooksTable());

        HBox buttons = new HBox(10, updateBtn, deleteBtn, backBtn);
        grid.add(buttons, 0, 4, 2, 1);

        whitePanel.getChildren().addAll(title, grid);
        content.getChildren().add(whitePanel);

        updateMainContent(content);
    }

    // Class Book untuk data buku
    class Book {
        private String title, author, isbn, status;

        public Book(String title, String author, String isbn, String status) {
            this.title = title;
            this.author = author;
            this.isbn = isbn;
            this.status = status;
        }

        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }

        public String getAuthor() { return author; }
        public void setAuthor(String author) { this.author = author; }

        public String getIsbn() { return isbn; }
        public void setIsbn(String isbn) { this.isbn = isbn; }

        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }

    private VBox createUserManageContent() {
        VBox content = new VBox();
        content.setPadding(new Insets(30));
        content.setSpacing(20);
        content.setStyle("-fx-background-color: #f5f5f5;");

        // Title
        Label pageTitle = new Label("Manage User");
        pageTitle.setFont(Font.font("System", FontWeight.BOLD, 24));
        pageTitle.setTextFill(Color.web("#1f2937"));

        // Action Requests Card
        VBox card = new VBox();
        card.setPadding(new Insets(25));
        card.setSpacing(15);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 10; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);");

        Label cardTitle = new Label("Action Requests");
        cardTitle.setFont(Font.font("System", FontWeight.BOLD, 18));
        cardTitle.setTextFill(Color.web("#1f2937"));

        requestsContainer = new VBox();
        requestsContainer.setSpacing(12);

        updateRequestsDisplay();

        card.getChildren().addAll(cardTitle, requestsContainer);

        // Statistics
        HBox statsContainer = createStatsContainer();

        // Back button
        Button backButton = new Button("← Kembali");
        backButton.setOnAction(e -> showManageUserMenu());

        content.getChildren().addAll(pageTitle, card, statsContainer, backButton);
        return content;
    }

    private void updateRequestsDisplay() {
        if (requestsContainer != null) {
            requestsContainer.getChildren().clear();

            Label noRequests = new Label("No pending requests at this time.");
            noRequests.setFont(Font.font("System", FontWeight.NORMAL, 14));
            noRequests.setTextFill(Color.web("#6b7280"));

            requestsContainer.getChildren().add(noRequests);
        }
    }

    private HBox createStatsContainer() {
        HBox statsContainer = new HBox();
        statsContainer.setSpacing(20);
        statsContainer.setAlignment(Pos.CENTER);

        VBox statsBox = new VBox();
        statsBox.setPadding(new Insets(20));
        statsBox.setSpacing(10);
        statsBox.setStyle("-fx-background-color: white; -fx-background-radius: 10; " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);");

        Label statsTitle = new Label("User Statistics");
        statsTitle.setFont(Font.font("System", FontWeight.BOLD, 16));
        statsTitle.setTextFill(Color.web("#1f2937"));

        Label statsInfo = new Label("Total Users: 1\nActive Users: 1");
        statsInfo.setFont(Font.font("System", FontWeight.NORMAL, 14));
        statsInfo.setTextFill(Color.web("#4b5563"));

        statsBox.getChildren().addAll(statsTitle, statsInfo);
        statsContainer.getChildren().add(statsBox);

        return statsContainer;
    }

    private VBox createMenuItem(String labelText, String iconText) {
        VBox box = new VBox(10);
        box.setPadding(new Insets(20));
        box.setAlignment(Pos.CENTER);
        box.setStyle("-fx-background-color: #f1f1f1; -fx-background-radius: 8;");
        box.setPrefSize(140, 140);

        Label icon = new Label(iconText);
        icon.setFont(Font.font(30));

        Label text = new Label(labelText);
        text.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        text.setWrapText(true);
        text.setTextAlignment(TextAlignment.CENTER);

        box.getChildren().addAll(icon, text);
        return box;
    }

    private VBox showNoDataFound(String keyword) {
        VBox box = new VBox(15);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(30));

        Label icon = new Label("🔍");
        icon.setFont(Font.font(64));

        Label msg = new Label("Data dengan kata kunci '" + keyword + "' tidak ditemukan.");
        msg.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        msg.setTextFill(Color.GRAY);

        Button backBtn = new Button("← Kembali");
        backBtn.setOnAction(e -> showStudentSearch());

        box.getChildren().addAll(icon, msg, backBtn);
        return box;
    }

    private void showStudentTable(boolean fromManageUser) {
        VBox content = new VBox(18);
        content.setPadding(new Insets(30));
        content.setStyle("-fx-background-color: #f8f9fa;");

        VBox whitePanel = new VBox(15);
        whitePanel.setPadding(new Insets(30));
        whitePanel.setStyle("-fx-background-color: white; -fx-background-radius: 10;");

        Label title = new Label("Data Mahasiswa");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        title.setTextFill(Color.web("#888"));

        TableView<Mahasiswa> table = new TableView<>();
        table.setPrefHeight(250);

        TableColumn<Mahasiswa, String> namaCol = new TableColumn<>("Nama");
        TableColumn<Mahasiswa, String> nimCol = new TableColumn<>("NIM");
        TableColumn<Mahasiswa, String> emailCol = new TableColumn<>("Email");
        TableColumn<Mahasiswa, Void> aksiCol = new TableColumn<>("Aksi");

        namaCol.setCellValueFactory(new PropertyValueFactory<>("nama"));
        nimCol.setCellValueFactory(new PropertyValueFactory<>("nim"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));

        aksiCol.setCellFactory(col -> new TableCell<>() {
            private final Button btn = new Button("Detail");

            {
                btn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
                btn.setOnAction(e -> {
                    Mahasiswa mhs = getTableView().getItems().get(getIndex());
                    showStudentDetailForm(mhs, fromManageUser);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });

        // Set column widths for better display
        namaCol.setPrefWidth(150);
        nimCol.setPrefWidth(120);
        emailCol.setPrefWidth(180);
        aksiCol.setPrefWidth(80);

        table.getColumns().addAll(namaCol, nimCol, emailCol, aksiCol);
        table.getItems().add(new Mahasiswa("Amalia Sanyoto", "202410370110038", "amailia@gmail.com"));

        Button backBtn = new Button("← Kembali");
        backBtn.setOnAction(e -> {
            if (fromManageUser) {
                showManageUserMenu();
            } else {
                updateMainContent(getHomeContent());
            }
        });

        whitePanel.getChildren().addAll(title, table, backBtn);
        content.getChildren().add(whitePanel);
        updateMainContent(content);
    }

    private void showStudentSearch() {
        VBox content = new VBox(18);
        content.setPadding(new Insets(30));
        content.setStyle("-fx-background-color: #f8f9fa;");

        VBox whitePanel = new VBox(25);
        whitePanel.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
        whitePanel.setPadding(new Insets(30));
        whitePanel.setAlignment(Pos.TOP_CENTER);

        Label title = new Label("Cari Mahasiswa");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        title.setTextFill(Color.web("#888"));

        VBox searchBox = new VBox(10);
        searchBox.setAlignment(Pos.CENTER);
        searchBox.setPadding(new Insets(20));

        Label label = new Label("Masukkan NIM/nama/email");
        TextField inputField = new TextField();
        inputField.setMaxWidth(300);
        Button searchButton = new Button("Cari");
        searchButton.setStyle("-fx-background-color: #82c4c3; -fx-text-fill: white;");

        VBox resultBox = new VBox(10);
        resultBox.setAlignment(Pos.CENTER);

        searchButton.setOnAction(e -> {
            String keyword = inputField.getText().trim();
            resultBox.getChildren().clear();

            if (keyword.isEmpty()) {
                VBox noData = showNoDataFound("Input pencarian kosong");
                resultBox.getChildren().add(noData);
            } else if (keyword.equalsIgnoreCase("Amalia") ||
                    keyword.equalsIgnoreCase("202410370110038") ||
                    keyword.toLowerCase().contains("amailia")) {
                // Show search results table
                TableView<Mahasiswa> table = new TableView<>();
                table.setPrefHeight(200);

                TableColumn<Mahasiswa, String> namaCol = new TableColumn<>("Nama");
                TableColumn<Mahasiswa, String> nimCol = new TableColumn<>("NIM");
                TableColumn<Mahasiswa, String> emailCol = new TableColumn<>("Email");
                TableColumn<Mahasiswa, Void> aksiCol = new TableColumn<>("Aksi");

                namaCol.setCellValueFactory(new PropertyValueFactory<>("nama"));
                nimCol.setCellValueFactory(new PropertyValueFactory<>("nim"));
                emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));

                // Set column widths for better display
                namaCol.setPrefWidth(150);
                nimCol.setPrefWidth(120);
                emailCol.setPrefWidth(180);
                aksiCol.setPrefWidth(80);

                aksiCol.setCellFactory(col -> new TableCell<>() {
                    private final Button btn = new Button("Detail");

                    {
                        btn.setStyle("-fx-background-color: #7fd1ae; -fx-text-fill: white;");
                        btn.setOnAction(ev -> {
                            Mahasiswa mhs = getTableView().getItems().get(getIndex());
                            showStudentDetailForm(mhs, true);
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        setGraphic(empty ? null : btn);
                    }
                });

                table.getColumns().addAll(namaCol, nimCol, emailCol, aksiCol);
                table.getItems().add(new Mahasiswa("Amalia Sanyoto", "202410370110038", "amailia@gmail.com"));
                table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

                resultBox.getChildren().add(table);
            } else {
                VBox noData = showNoDataFound(keyword);
                resultBox.getChildren().add(noData);
            }
        });

        Button backBtn = new Button("← Kembali");
        backBtn.setOnAction(e -> showManageUserMenu());

        searchBox.getChildren().addAll(label, inputField, searchButton);
        whitePanel.getChildren().addAll(title, searchBox, resultBox, backBtn);
        content.getChildren().add(whitePanel);

        updateMainContent(content);
    }

    private void showStudentDetailForm(Mahasiswa mhs, boolean fromManageUser) {
        VBox content = new VBox(18);
        content.setPadding(new Insets(30));
        content.setStyle("-fx-background-color: #f8f9fa;");

        VBox whitePanel = new VBox(25);
        whitePanel.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
        whitePanel.setPadding(new Insets(30));

        Label title = new Label("Detail Mahasiswa");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        title.setTextFill(Color.web("#888"));

        GridPane grid = new GridPane();
        grid.setVgap(15);
        grid.setHgap(10);
        grid.setPadding(new Insets(20));

        TextField namaField = new TextField(mhs.getNama());
        TextField nimField = new TextField(mhs.getNim());
        TextField emailField = new TextField(mhs.getEmail());

        namaField.setMaxWidth(300);
        nimField.setMaxWidth(300);
        emailField.setMaxWidth(300);

        grid.addRow(0, new Label("Nama:"), namaField);
        grid.addRow(1, new Label("NIM:"), nimField);
        grid.addRow(2, new Label("Email:"), emailField);

        Button deleteBtn = new Button("Delete Student");
        deleteBtn.setStyle("-fx-background-color: red; -fx-text-fill: white;");
        Button backBtn = new Button("← Kembali");
        backBtn.setOnAction(e -> {
            if (fromManageUser) {
                showStudentTable(true);
            } else {
                showStudentTable(false);
            }
        });

        HBox buttons = new HBox(10, deleteBtn, backBtn);
        grid.add(buttons, 0, 3, 2, 1);

        whitePanel.getChildren().addAll(title, grid);
        content.getChildren().add(whitePanel);

        updateMainContent(content);
    }

    private void showAddStudentForm() {
        VBox content = new VBox(18);
        content.setPadding(new Insets(30));
        content.setStyle("-fx-background-color: #f8f9fa;");

        VBox whitePanel = new VBox(25);
        whitePanel.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
        whitePanel.setPadding(new Insets(30));

        Label title = new Label("Add New Student");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        title.setTextFill(Color.web("#888"));

        GridPane grid = new GridPane();
        grid.setVgap(15);
        grid.setHgap(10);
        grid.setPadding(new Insets(20));

        TextField namaField = new TextField();
        TextField nimField = new TextField();
        TextField emailField = new TextField();

        namaField.setMaxWidth(300);
        nimField.setMaxWidth(300);
        emailField.setMaxWidth(300);

        namaField.setPromptText("Enter student name");
        nimField.setPromptText("Enter NIM");
        emailField.setPromptText("Enter email");

        grid.addRow(0, new Label("Nama:"), namaField);
        grid.addRow(1, new Label("NIM:"), nimField);
        grid.addRow(2, new Label("Email:"), emailField);

        Button addBtn = new Button("Add Student");
        addBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        Button backBtn = new Button("← Kembali");
        backBtn.setOnAction(e -> showManageUserMenu());

        addBtn.setOnAction(e -> {
            String nama = namaField.getText().trim();
            String nim = nimField.getText().trim();
            String email = emailField.getText().trim();

            if (nama.isEmpty() || nim.isEmpty() || email.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Input Error");
                alert.setHeaderText("Please fill all fields");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText("Student added successfully!");
                alert.showAndWait();
                showManageUserMenu();
            }
        });

        HBox buttons = new HBox(10, addBtn, backBtn);
        grid.add(buttons, 0, 3, 2, 1);

        whitePanel.getChildren().addAll(title, grid);
        content.getChildren().add(whitePanel);

        updateMainContent(content);
    }

    private void showAvailableBookPage() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(30));
        content.setAlignment(Pos.CENTER);
        content.setStyle("-fx-background-color: #f8f9fa;");

        Label label = new Label("Available Book - Konten masih dikembangkan");
        Button backButton = new Button("← Kembali ke Home");
        backButton.setOnAction(e -> updateMainContent(getHomeContent()));

        content.getChildren().addAll(label, backButton);
        updateMainContent(content);
    }

    private void showLateReturnPage() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(30));
        content.setAlignment(Pos.CENTER);
        content.setStyle("-fx-background-color: #f8f9fa;");

        Label label = new Label("Late Return - Konten masih dikembangkan");
        Button backButton = new Button("← Kembali ke Home");
        backButton.setOnAction(e -> updateMainContent(getHomeContent()));

        content.getChildren().addAll(label, backButton);
        updateMainContent(content);
    }

    private void showActionPage() {
        VBox content = new VBox(20);
        content.setPadding(new Insets(30));
        content.setAlignment(Pos.CENTER);
        content.setStyle("-fx-background-color: #f8f9fa;");

        Label label = new Label("Action - Konten masih dikembangkan");
        Button backButton = new Button("← Kembali ke Home");
        backButton.setOnAction(e -> updateMainContent(getHomeContent()));

        content.getChildren().addAll(label, backButton);
        updateMainContent(content);
    }
}

class Mahasiswa {
    private String nama, nim, email;

    public Mahasiswa(String nama, String nim, String email) {
        this.nama = nama;
        this.nim = nim;
        this.email = email;
    }

    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }

    public String getNim() { return nim; }
    public void setNim(String nim) { this.nim = nim; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}