package main_app;

import java.util.ArrayList;
import java.util.Date;
import javafx.animation.PauseTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.util.Duration;
import main_app.models.Admin;
import main_app.models.Book;
import main_app.models.Order;
import main_app.models.Reader;
import main_app.models.User;
import main_app.utils.Constants;
import main_app.utils.FileManager;
import main_app.utils.SecurityService;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

public class App extends Application
{
    private BorderPane root;
    private VBox mainMenu;
    private VBox loginForm;
    private VBox registerForm;
    private Scene scene;
    public boolean admin;

    private TextField usernameField;
    private PasswordField passwordField;
    private User user;

    private TextField emailField;
    private TextField usernameField_register;
    private TextField paymentMethodField;
    private TextField passwordField_register;
    private TextField addressField;
    private TextField phoneNumberField;

    @Override
    public void start(@SuppressWarnings("exports") Stage primaryStage)
    {
        root = new BorderPane();
        root.getStyleClass().add("borderpane-style");

        createMainMenu();
        createLoginForm(primaryStage);
        createRegisterForm(primaryStage);

        root.setCenter(mainMenu); // Show the main menu first

        scene = new Scene(root, 800, 600); // Set a reasonable default size
        scene.getStylesheets().add(getClass().getResource("/main_app/resources/css/welcome.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.setTitle("Welcome");
        primaryStage.setMaximized(true); // Maximize window on startup
        primaryStage.show();
    }

    private void createMainMenu()
    {
        Label welcomeLabel = createLabel("Welcome to our book store", "style");

        VBox topContainer = createVBox(Pos.CENTER, welcomeLabel, "top-background", 200);
        root.setTop(topContainer);

        HBox buttonContainer = createButtonContainer();

        mainMenu = new VBox(50);
        mainMenu.setAlignment(Pos.CENTER);
        mainMenu.getChildren().add(buttonContainer);
    }

    private void createLoginForm(Stage primaryStage)
    {
        loginForm = new VBox(20);
        loginForm.setPadding(new Insets(20));
        loginForm.setAlignment(Pos.CENTER);
        loginForm.setMaxSize(400, 400);
        loginForm.getStyleClass().add("form-container");

        Label loginLabel = createLabel("Login", "login");

        usernameField = new TextField(); // Initialize the username field
        usernameField.setPromptText("Enter your username");

        passwordField = new PasswordField(); // Initialize the password field
        passwordField.setPromptText("Enter your password");

        Button loginButton = createButton("Login", null);
        applyHoverEffect(loginButton);
        loginButton.setOnAction(event -> handleLoginButtonClick(primaryStage));

        Button backButton = createButton("Back", null);
        applyHoverEffect(backButton);
        backButton.setOnAction(event -> showMainMenu()); // Go back to main menu

        loginForm.getChildren().addAll(loginLabel, usernameField, passwordField, loginButton, backButton);

        if (!admin)
        {
            Label registerLabel = createRegisterLabel(primaryStage);
            loginForm.getChildren().add(registerLabel);
        }
    }

    private void createRegisterForm(Stage primaryStage)
    {
        registerForm = new VBox(20);
        registerForm.setPadding(new Insets(20));
        registerForm.setAlignment(Pos.CENTER);
        registerForm.setMaxSize(600, 600);
        registerForm.getStyleClass().add("form-container");

        Label registerLabel = createLabel("Registration Form", "login");

        emailField = new TextField();
        emailField.setPromptText("Enter your email");

        usernameField_register = new TextField();
        usernameField_register.setPromptText("Enter your username");

        paymentMethodField = new TextField();
        paymentMethodField.setPromptText("Enter your payment method");

        passwordField_register = new PasswordField();
        passwordField_register.setPromptText("Enter your password");

        addressField = new TextField();
        addressField.setPromptText("Enter your address");

        phoneNumberField = new TextField();
        phoneNumberField.setPromptText("Enter your phone number");

        Button saveButton = createButton("Register", null);
        applyHoverEffect(saveButton);
        saveButton.setOnAction(event -> handleRegisterButtonClick());

        Button backButton = createButton("Back", null);
        applyHoverEffect(backButton);
        backButton.setOnAction(event -> showMainMenu()); // Go back to main menu

        HBox buttonContainer = new HBox(15, saveButton, backButton);
        buttonContainer.setAlignment(Pos.CENTER);

        registerForm.getChildren().addAll(registerLabel, emailField, usernameField_register, paymentMethodField,
                passwordField_register, addressField, phoneNumberField, buttonContainer);
    }

    private void showMainMenu()
    {
        scene.getStylesheets().clear();
        scene.getStylesheets().add(getClass().getResource("/main_app/resources/css/welcome.css").toExternalForm());
        root.setCenter(mainMenu);
    }

    private void showLoginForm()
    {
        scene.getStylesheets().clear();
        scene.getStylesheets().add(getClass().getResource("/main_app/resources/css/login.css").toExternalForm());
        root.setCenter(loginForm);
    }

    private void showRegisterForm()
    {
        scene.getStylesheets().clear();
        scene.getStylesheets().add(getClass().getResource("/main_app/resources/css/register.css").toExternalForm());
        root.setCenter(registerForm);
    }

    private Label createLabel(String text, String styleClass)
    {
        Label label = new Label(text);
        label.setMaxWidth(Double.MAX_VALUE);
        label.setId(styleClass);
        return label;
    }

    private VBox createVBox(Pos alignment, Label label, String styleClass, double prefHeight)
    {
        VBox vbox = new VBox(label);
        vbox.setAlignment(alignment);
        vbox.setPrefHeight(prefHeight);
        vbox.getStyleClass().add(styleClass);
        VBox.setVgrow(label, Priority.ALWAYS);
        return vbox;
    }

    private HBox createButtonContainer()
    {
        HBox buttonContainer = new HBox(150);
        buttonContainer.setAlignment(Pos.CENTER);

        Button adminButton = createButton("Admin", "admin");
        Button readerButton = createButton("Reader", "reader");

        applyHoverEffect(adminButton);
        applyHoverEffect(readerButton);

        // Set admin flag and show login form
        adminButton.setOnAction(event ->
        {
            this.admin = true;
            showLoginForm();
        });

        // Set reader flag and show login form
        readerButton.setOnAction(event ->
        {
            this.admin = false;
            showLoginForm();
        });

        buttonContainer.getChildren().addAll(adminButton, readerButton);
        return buttonContainer;
    }

    private Button createButton(String text, String id)
    {
        Button button = new Button(text);
        button.setId(id);
        button.getStyleClass().add("center-button");
        return button;
    }

    private void applyHoverEffect(Button button)
    {
        ScaleTransition enlargeTransition = new ScaleTransition(Duration.seconds(0.5), button);
        enlargeTransition.setToX(1.15);
        enlargeTransition.setToY(1.15);

        ScaleTransition resetTransition = new ScaleTransition(Duration.seconds(0.5), button);
        resetTransition.setToX(1.0);
        resetTransition.setToY(1.0);

        button.addEventFilter(MouseEvent.MOUSE_ENTERED, event -> enlargeTransition.playFromStart());
        button.addEventFilter(MouseEvent.MOUSE_EXITED, event -> resetTransition.playFromStart());
    }

    private Label createRegisterLabel(Stage primaryStage)
    {
        Label registerLabel = new Label("Don't have an account? Register here");
        registerLabel.setTextFill(Color.WHITE);
        registerLabel.setFont(Font.font("System", FontWeight.BOLD, 12));
        registerLabel.setTextAlignment(TextAlignment.CENTER);
        registerLabel.setStyle("-fx-cursor: hand;");
        VBox.setMargin(registerLabel, new Insets(30, 0, 0, 0));

        registerLabel.setOnMouseClicked(event -> showRegisterForm()); // Show register form

        return registerLabel;
    }

    private void handleLoginButtonClick(Stage primaryStage)
    {
        user = User.login(usernameField.getText(), SecurityService.hash(passwordField.getText()),
                admin ? "admin" : "reader");
        if (user != null)
        {
            // Assuming a successful login logic here
            showOptions(primaryStage);
        } else
        {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Incorrect username or password.");
            ButtonType okButton = new ButtonType("OK", ButtonData.OK_DONE);
            alert.getButtonTypes().setAll(okButton);
            alert.showAndWait();
        }
    }

    private void showOptions(Stage primaryStage)
    {
        root.getChildren().clear();
        root.getStyleClass().add("borderpane-style");
        scene.getStylesheets().clear();
        scene.getStylesheets().add(getClass().getResource("/main_app/resources/css/reader_menu.css").toExternalForm());

        HBox topContainer = new HBox();
        topContainer.setSpacing(20);
        topContainer.setAlignment(Pos.TOP_RIGHT); // Align to top right
        topContainer.setPadding(new Insets(10)); // Add some padding

        // Create the logout button and place it on the top right
        Button logoutButton = createButton("Logout", "logout");
        // logoutButton.setPrefSize(120, 50);
        logoutButton.setMaxWidth(150);
        logoutButton.setMaxHeight(80);
        applyHoverEffect(logoutButton);
        logoutButton.setOnAction(arg0 ->
        {
            user = null;
            root.getChildren().clear(); // Clear the UI elements
            showMainMenu(); // Show the main menu
        });

        topContainer.getChildren().addAll(logoutButton); // Add the logout button to the top container

        VBox options = new VBox();
        options.setSpacing(50);
        root.setCenter(options);
        options.setAlignment(Pos.CENTER);

        if (!admin)
        {
            HBox line1 = new HBox();
            line1.setSpacing(50);

            Button editAccountInformation = createButton("Edit Account Information", null);
            applyHoverEffect(editAccountInformation);
            editAccountInformation.setOnAction(e -> showEditAccountInformation(primaryStage));

            Button showPreviousOrders = createButton("Show Previous Orders", null);
            applyHoverEffect(showPreviousOrders);
            showPreviousOrders.setOnAction(e -> showPreviousOrders(primaryStage));

            editAccountInformation.getStyleClass().add("center-button");
            showPreviousOrders.getStyleClass().add("center-button");
            line1.getChildren().addAll(editAccountInformation, showPreviousOrders);
            line1.setAlignment(Pos.CENTER);

            HBox line2 = new HBox();
            line2.setSpacing(50);

            Button displayAllBooks = createButton("Display Books", null);
            applyHoverEffect(displayAllBooks);
            displayAllBooks.setOnAction(e -> showAllBooks(primaryStage)); // Show all books

            Button showReceipt = createButton("Display Cart", null);
            applyHoverEffect(showReceipt);
            showReceipt.setOnAction(e -> showCart(primaryStage));

            showReceipt.getStyleClass().add("center-button");
            line2.getChildren().add(showReceipt);

            displayAllBooks.getStyleClass().add("center-button");
            line2.setAlignment(Pos.CENTER);
            line2.getChildren().add(displayAllBooks);

            options.getChildren().addAll(line1, line2);
        } else
        {
            showAllBooks(primaryStage);
        }

        root.setTop(topContainer); // Set the top container (with logout) to the top of the root
    }

    private void showEditAccountInformation(Stage primaryStage)
    {
        root.getChildren().clear();
        root.getStyleClass().add("borderpane-style");
        scene.getStylesheets().clear();
        scene.getStylesheets().add(getClass().getResource("/main_app/resources/css/editinfo.css").toExternalForm());

        Label editInfoLabel = new Label("Edit Account Information");
        editInfoLabel.setMaxWidth(Double.MAX_VALUE);
        editInfoLabel.setId("style");

        VBox editInfoFormContainer = new VBox();
        editInfoFormContainer.setSpacing(10);
        editInfoFormContainer.setPadding(new Insets(20));
        editInfoFormContainer.setAlignment(Pos.CENTER);
        editInfoFormContainer.setPrefSize(600, 600);
        editInfoFormContainer.setMaxWidth(600);
        editInfoFormContainer.setMaxHeight(600);
        editInfoFormContainer.getStyleClass().add("form-container");
        editInfoFormContainer.setSpacing(15);

        Reader reader = (Reader) user;
        // Create form elements with unique variable names
        TextField editUsernameField = new TextField();
        editUsernameField.setPromptText("Enter your new username");
        editUsernameField.setText(reader.getUsername());

        TextField editPaymentMethodField = new TextField();
        editPaymentMethodField.setPromptText("Enter your new payment method");
        editPaymentMethodField.setText(reader.getPaymentMethod());

        PasswordField editPasswordField = new PasswordField();
        editPasswordField.setPromptText("Enter your new password");
        editPasswordField.setText(reader.getPassword());

        TextField editAddressField = new TextField();
        editAddressField.setPromptText("Enter your new address");
        editAddressField.setText(reader.getAddress());

        TextField editPhoneNumberField = new TextField();
        editPhoneNumberField.setPromptText("Enter your new phone number");
        editPhoneNumberField.setText(reader.getPhoneNumber());

        Button saveEditButton = new Button("Save");
        applyHoverEffect(saveEditButton);
        saveEditButton.setOnAction(event ->
        {
            reader.setUsername(editUsernameField.getText());
            reader.setPaymentMethod(editPaymentMethodField.getText());

            if (!editPasswordField.getText().equals(reader.getPassword()))
            {
                reader.setPassword(SecurityService.hash(editPasswordField.getText()));
            }

            reader.setAddress(editAddressField.getText());
            reader.setPhoneNumber(editPhoneNumberField.getText());

            reader.updateReaderInformation();

            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Information Updated");
            alert.setHeaderText(null);
            alert.setContentText("Your account information has been updated successfully.");
            alert.showAndWait();

            // Redirect the user back to the options menu or main menu
            showOptions(primaryStage);
        });

        Button backEditButton = new Button("Back");
        applyHoverEffect(backEditButton);
        backEditButton.setOnAction(event -> showOptions(primaryStage)); // Go back to the options menu

        editInfoFormContainer.getChildren().addAll(editInfoLabel, editUsernameField, editPaymentMethodField,
                editPasswordField, editAddressField, editPhoneNumberField, saveEditButton, backEditButton);

        root.setCenter(editInfoFormContainer);
    }

    @SuppressWarnings("unchecked")
    private void showAllBooks(Stage primaryStage)
    {
        root.getChildren().clear();
        root.getStyleClass().add("borderpane-style");
        scene.getStylesheets().clear();
        scene.getStylesheets().add(getClass().getResource("/main_app/resources/css/searchbook.css").toExternalForm());

        HBox topContainer = new HBox();
        topContainer.setSpacing(20);
        topContainer.setAlignment(Pos.TOP_RIGHT);
        topContainer.setPadding(new Insets(10));

        if (admin)
        {
            Button logoutButton = new Button("Logout");
            logoutButton.setPrefSize(120, 50);
            applyHoverEffect(logoutButton);
            logoutButton.setOnAction(arg0 ->
            {
                user = null;
                root.getChildren().clear();
                showMainMenu();
            });

            topContainer.getChildren().add(logoutButton);
        } else
        {
            Button back = createButton("Back", null);
            applyHoverEffect(back);
            back.setPrefSize(120, 50);
            back.setOnAction(e -> showOptions(primaryStage));
            topContainer.getChildren().add(back);
        }

        VBox searchBookContainer = new VBox();
        searchBookContainer.setSpacing(10);
        searchBookContainer.setAlignment(Pos.CENTER);
        searchBookContainer.setPadding(new Insets(20));

        TextField searchBookField = new TextField();
        searchBookField.setPromptText("Enter book name");
        searchBookField.setMaxWidth(1100);
        searchBookField.setPrefHeight(60);
        searchBookField.getStyleClass().add("text-field");

        TableView<Book> searchBookTable = new TableView<>();
        searchBookTable.getStyleClass().add("table-view");

        TableColumn<Book, String> searchBookNameColumn = new TableColumn<>("Book Name");
        searchBookNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        searchBookNameColumn.setPrefWidth(350);

        TableColumn<Book, String> searchAuthorNameColumn = new TableColumn<>("Author");
        searchAuthorNameColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        searchAuthorNameColumn.setPrefWidth(350);

        TableColumn<Book, Integer> searchStockColumn = new TableColumn<>("Stock");
        searchStockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        searchStockColumn.setPrefWidth(100);

        TableColumn<Book, String> searchCategoryColumn = new TableColumn<>("Book Category");
        searchCategoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        searchCategoryColumn.setPrefWidth(200);

        TableColumn<Book, Double> searchPriceColumn = new TableColumn<>("Price");
        searchPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        searchPriceColumn.setPrefWidth(100);

        searchBookTable.getColumns().addAll(searchBookNameColumn, searchAuthorNameColumn, searchCategoryColumn,
                searchPriceColumn, searchStockColumn);

        ObservableList<Book> bookList = FXCollections.observableArrayList();
        ArrayList<String> books = FileManager.readFile(Constants.BOOKS_FILE_PATH);
        for (String line : books)
        {
            if (admin || !line.contains(",0,"))
            {
                String[] bookDetails = line.split(",");
                Book book = new Book(bookDetails[0], bookDetails[1], Double.parseDouble(bookDetails[2]),
                        Integer.parseInt(bookDetails[3]), bookDetails[4]);
                bookList.add(book);
            }
        }

        searchBookTable.setItems(bookList);

        searchBookField.textProperty().addListener((observable, oldValue, newValue) ->
        {
            ObservableList<Book> filteredList = FXCollections.observableArrayList();
            for (Book book : bookList)
            {
                if (book.getName().toLowerCase().contains(newValue.toLowerCase()) ||
                        book.getAuthor().toLowerCase().contains(newValue.toLowerCase()))
                {
                    filteredList.add(book);
                }
            }
            searchBookTable.setItems(filteredList);
        });

        HBox searchBookButtonsContainer = new HBox();
        searchBookButtonsContainer.setSpacing(20);
        searchBookButtonsContainer.setAlignment(Pos.CENTER);

        if (admin)
        {
            Button addBookButton = new Button("Add New Book");
            addBookButton.setPrefSize(150, 50);
            applyHoverEffect(addBookButton);
            addBookButton.setOnAction(e -> showBookInputDialog(null, bookList, searchBookTable));

            Button editBookButton = new Button("Edit Book");
            editBookButton.setPrefSize(150, 50);
            applyHoverEffect(editBookButton);
            editBookButton.setOnAction(e ->
            {
                Book selectedBook = searchBookTable.getSelectionModel().getSelectedItem();
                if (selectedBook != null)
                {
                    showBookInputDialog(selectedBook, bookList, searchBookTable);
                } else
                {
                    showAlert("No Selection", "Please select a book to edit.");
                }
            });

            Button deleteBookButton = new Button("Delete Book");
            deleteBookButton.setPrefSize(150, 50);
            applyHoverEffect(deleteBookButton);
            deleteBookButton.setOnAction(e ->
            {
                Book selectedBook = searchBookTable.getSelectionModel().getSelectedItem();
                if (selectedBook != null)
                {
                    bookList.remove(selectedBook);
                    ((Admin) user).deleteBook(selectedBook);
                } else
                {
                    showAlert("No Selection", "Please select a book to delete.");
                }
            });

            searchBookButtonsContainer.getChildren().addAll(addBookButton, editBookButton, deleteBookButton);
        } else
        {
            Button addBookToCartButton = new Button("Add to Cart");
            addBookToCartButton.setPrefSize(150, 50);
            applyHoverEffect(addBookToCartButton);
            addBookToCartButton.setOnAction(arg0 ->
            {
                Book book = searchBookTable.getSelectionModel().getSelectedItem();
                if (book != null)
                {
                    ((Reader) user).getShoppingCart().addToCart(book);

                    Label confirmationLabel = new Label("The selected book has been added to your cart.");
                    confirmationLabel.setStyle("-fx-background-color: lightgreen; -fx-padding: 10px;");
                    searchBookContainer.getChildren().add(confirmationLabel);

                    PauseTransition pause = new PauseTransition(Duration.seconds(2));
                    pause.setOnFinished(event -> searchBookContainer.getChildren().remove(confirmationLabel));
                    pause.play();
                } else
                {
                    showAlert("No Selection", "Please select a book to add to cart.");
                }
            });

            searchBookButtonsContainer.getChildren().add(addBookToCartButton);
        }

        searchBookContainer.getChildren().addAll(searchBookField, searchBookTable, searchBookButtonsContainer);

        root.setTop(topContainer);
        root.setCenter(searchBookContainer);

        searchBookTable.setPrefHeight(600);
        searchBookTable.setMaxWidth(1100);
    }

    private void showBookInputDialog(Book book, ObservableList<Book> bookList, TableView<Book> tableView)
    {
        Dialog<Book> dialog = new Dialog<>();
        dialog.setTitle(book == null ? "Add New Book" : "Edit Book");

        // Create fields
        TextField nameField = new TextField();
        nameField.setPromptText("Book Name");
        TextField authorField = new TextField();
        authorField.setPromptText("Author");
        TextField priceField = new TextField();
        priceField.setPromptText("Price");
        TextField stockField = new TextField();
        stockField.setPromptText("Stock");
        TextField categoryField = new TextField();
        categoryField.setPromptText("Category");

        // Pre-fill fields if editing
        if (book != null)
        {
            nameField.setText(book.getName());
            authorField.setText(book.getAuthor());
            priceField.setText(String.valueOf(book.getPrice()));
            stockField.setText(String.valueOf(book.getStock()));
            categoryField.setText(book.getCategory());
        }

        VBox content = new VBox(10);
        content.getChildren().addAll(new Label("Book Name:"), nameField,
                new Label("Author:"), authorField,
                new Label("Price:"), priceField,
                new Label("Stock:"), stockField,
                new Label("Category:"), categoryField);
        dialog.getDialogPane().setContent(content);

        ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton ->
        {
            if (dialogButton == saveButtonType)
            {
                if (book == null)
                {
                    Book newBook = new Book(nameField.getText(), authorField.getText(),
                            Double.parseDouble(priceField.getText()), Integer.parseInt(stockField.getText()),
                            categoryField.getText());

                    bookList.add(newBook);

                    ((Admin) user).addBook(newBook);
                } else
                {
                    Book updatedBook = new Book(nameField.getText(), authorField.getText(),
                            Double.parseDouble(priceField.getText()), Integer.parseInt(stockField.getText()),
                            categoryField.getText());

                    bookList.remove(book);
                    bookList.add(updatedBook);

                    ((Admin) user).editBook(book, updatedBook);
                }
            }
            return null;
        });

        dialog.showAndWait();
    }

    private void showAlert(String title, String content)
    {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @SuppressWarnings("unchecked")
    private void showCart(Stage primaryStage)
    {
        root.getChildren().clear();
        root.getStyleClass().add("borderpane-style");
        scene.getStylesheets().clear();
        scene.getStylesheets().add(getClass().getResource("/main_app/resources/css/displaycart.css").toExternalForm());

        VBox cart = new VBox();

        Label welcome = new Label("Your cart");
        welcome.setMaxWidth(Double.MAX_VALUE);
        welcome.setId("style");

        TableView<Book> table = new TableView<>();
        table.getStyleClass().add("table-view");

        TableColumn<Book, String> booknameColumn = new TableColumn<>("Book Name");
        booknameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        booknameColumn.setPrefWidth(350);

        TableColumn<Book, String> authorNameColumn = new TableColumn<>("Author");
        authorNameColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        authorNameColumn.setPrefWidth(350);

        TableColumn<Book, String> bookcategoryColumn = new TableColumn<>("Book category");
        bookcategoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        bookcategoryColumn.setPrefWidth(200);

        TableColumn<Book, Double> priceColumn = new TableColumn<>("Price");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        priceColumn.setPrefWidth(100);

        table.getColumns().addAll(booknameColumn, authorNameColumn, bookcategoryColumn, priceColumn);

        ArrayList<Book> addedBooks = ((Reader) user).getShoppingCart().getAddedBooks();
        for (Book book : addedBooks)
        {
            table.getItems().add(book);
        }

        HBox buttonsContainer = new HBox();
        buttonsContainer.setSpacing(300);

        Button remove = new Button("Remove");
        applyHoverEffect(remove);
        remove.setOnAction(arg0 ->
        {
            Book book = table.getSelectionModel().getSelectedItem();
            if (book != null)
            {
                table.getItems().remove(book);
                ((Reader) user).getShoppingCart().removeFromCart(book);
            } else
            {
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("No Selection");
                alert.setHeaderText(null);
                alert.setContentText("Please select a book to remove.");
                ButtonType okButton = new ButtonType("OK", ButtonData.OK_DONE);
                alert.getButtonTypes().setAll(okButton);
                alert.showAndWait();
            }
        });

        Button back = new Button("Back");
        applyHoverEffect(back);
        back.setOnAction(e -> showOptions(primaryStage));

        Button toReceipt = new Button("Go To Receipt");
        applyHoverEffect(toReceipt);
        toReceipt.setOnAction(e ->
        {
            if (!addedBooks.isEmpty())
            {
                showReceipt(primaryStage);
            } else
            {
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("No books in cart");
                alert.setHeaderText(null);
                alert.setContentText("You don't have any books added to your cart.");
                ButtonType okButton = new ButtonType("OK", ButtonData.OK_DONE);
                alert.getButtonTypes().setAll(okButton);
                alert.showAndWait();
            }
        });

        buttonsContainer.getChildren().addAll(back, remove, toReceipt);
        buttonsContainer.setAlignment(Pos.CENTER);

        cart.getChildren().addAll(table, buttonsContainer);
        cart.setAlignment(Pos.CENTER);
        cart.setSpacing(10);

        root.setTop(welcome);
        root.setCenter(cart);

        table.setPrefHeight(600);
        table.setMaxWidth(1000);
    }

    private void showReceipt(Stage primaryStage)
    {
        root.getChildren().clear();
        root.getStyleClass().add("borderpane-style");
        scene.getStylesheets().clear();
        scene.getStylesheets().add(getClass().getResource("/main_app/resources/css/receipt.css").toExternalForm());

        VBox cart = new VBox();

        Label welcom = new Label("Receipt");
        welcom.setMaxWidth(Double.MAX_VALUE);
        welcom.setId("style");

        TableView<Book> table = new TableView<Book>();
        table.getStyleClass().add("table-view");

        TableColumn<Book, String> booknameColumn = new TableColumn<Book, String>("Book Name");
        booknameColumn.setCellValueFactory(new PropertyValueFactory<Book, String>("name"));
        booknameColumn.setPrefWidth(350);

        TableColumn<Book, String> authorNameColumn = new TableColumn<Book, String>("Author");
        authorNameColumn.setCellValueFactory(new PropertyValueFactory<Book, String>("author"));
        authorNameColumn.setPrefWidth(350);

        TableColumn<Book, String> bookcategoryColumn = new TableColumn<Book, String>("Book category");
        bookcategoryColumn.setCellValueFactory(new PropertyValueFactory<Book, String>("category"));
        bookcategoryColumn.setPrefWidth(200);

        TableColumn<Book, Double> priceColumn = new TableColumn<Book, Double>("Price");
        priceColumn.setCellValueFactory(new PropertyValueFactory<Book, Double>("price"));
        priceColumn.setPrefWidth(100);

        table.getColumns().add(booknameColumn);
        table.getColumns().add(authorNameColumn);
        table.getColumns().add(bookcategoryColumn);
        table.getColumns().add(priceColumn);

        table.getItems().addAll(((Reader) user).getShoppingCart().getAddedBooks());
        // Label to display total payment
        Label totalLabel = new Label();

        double total = table.getItems().stream()
                .mapToDouble(Book::getPrice)
                .sum();
        totalLabel.setText("Total: $" + total);
        totalLabel.getStyleClass().add("total-label");

        HBox buttonsContainer = new HBox();
        buttonsContainer.setSpacing(300);

        Button pay = new Button("Pay");
        applyHoverEffect(pay);
        pay.setPrefSize(120, 50);
        pay.setOnAction(e ->
        {
            Order order = new Order(Math.abs(new java.util.Random().nextInt()), new Date(), (Reader) user, total);
            order.generateOrder(((Reader) user).getShoppingCart().getAddedBooks());

            Label confirmationLabel = new Label("The books have been purchased successfully.");
            confirmationLabel.setStyle("-fx-background-color: lightgreen; -fx-padding: 10px;");
            cart.getChildren().add(confirmationLabel);

            PauseTransition pause = new PauseTransition(Duration.seconds(2));
            pause.setOnFinished(event -> cart.getChildren().remove(confirmationLabel));
            pause.play();
        });

        Button back = new Button("Back");
        applyHoverEffect(back);
        back.setPrefSize(120, 50);
        back.setOnAction(e -> showCart(primaryStage)); // Go back to the cart view

        buttonsContainer.getChildren().addAll(back, pay);
        buttonsContainer.setAlignment(Pos.CENTER);

        cart.getChildren().addAll(table, totalLabel, buttonsContainer);
        cart.setAlignment(Pos.CENTER);
        cart.setSpacing(10);

        root.setTop(welcom);
        root.setCenter(cart);

        table.setPrefHeight(600);
        table.setMaxWidth(1000);
    }

    private void showPreviousOrders(Stage primaryStage)
    {
        root.getChildren().clear();
        root.getStyleClass().add("borderpane-style");
        scene.getStylesheets().clear();
        scene.getStylesheets()
                .add(getClass().getResource("/main_app/resources/css/previousorders.css").toExternalForm());

        VBox cart = new VBox();

        TableView<Order> table = new TableView<>();
        table.getStyleClass().add("table-view");

        TableColumn<Order, Integer> idColumn = new TableColumn<>("Order ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("orderID"));
        idColumn.setPrefWidth(250);

        TableColumn<Order, Date> dateColumn = new TableColumn<>("Date");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("dateCreated"));
        dateColumn.setPrefWidth(250);

        TableColumn<Order, Double> totalColumn = new TableColumn<>("Total Pay");
        totalColumn.setCellValueFactory(new PropertyValueFactory<>("total"));
        totalColumn.setPrefWidth(150);

        table.getColumns().add(idColumn);
        table.getColumns().add(dateColumn);
        table.getColumns().add(totalColumn);

        table.getItems().addAll(((Reader) user).getPreviousOrders());

        HBox buttonsContainer = new HBox();
        buttonsContainer.setSpacing(300);

        Button back = createButton("Back", null);
        applyHoverEffect(back);
        back.setPrefSize(120, 50);
        back.setOnAction(e -> showOptions(primaryStage));

        buttonsContainer.getChildren().addAll(back);
        buttonsContainer.setAlignment(Pos.CENTER);

        cart.getChildren().addAll(table, buttonsContainer);
        cart.setAlignment(Pos.CENTER);
        cart.setSpacing(10);

        root.setCenter(cart);

        table.setPrefHeight(600);
        table.setMaxWidth(650);
    }

    private void handleRegisterButtonClick()
    {
        // Retrieve input values from the registration form
        String email = emailField.getText();
        String username = usernameField_register.getText();
        String paymentMethod = paymentMethodField.getText();
        String password = passwordField_register.getText();
        String address = addressField.getText();
        String phoneNumber = phoneNumberField.getText();

        if (email.isEmpty() || username.isEmpty() || paymentMethod.isEmpty() || password.isEmpty() || address.isEmpty()
                || phoneNumber.isEmpty())
        {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Incomplete Form");
            alert.setHeaderText(null);
            alert.setContentText("Please fill in all the fields.");
            alert.showAndWait();
            return;
        }

        if (Reader.register(email, username, password, phoneNumber, address, paymentMethod))
        {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Registration Successful");
            alert.setHeaderText(null);
            alert.setContentText("You have successfully registered.");
            alert.showAndWait();

            // Redirect the user to the login form or main menu
            showLoginForm();
        } else
        {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Registration Failed");
            alert.setHeaderText(null);
            alert.setContentText("A user already exists with that email!");
            alert.showAndWait();
        }
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}
