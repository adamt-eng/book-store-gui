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
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.*;
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

import main_app.models.*;
import main_app.utils.*;

public class App extends Application
{
    private BorderPane root;
    private VBox mainMenu;
    private Scene scene;
    private boolean isAdmin;
    private User user;

    @Override
    public void start(@SuppressWarnings("exports") Stage primaryStage)
    {
        root = new BorderPane();
        root.getStyleClass().add("borderpane-style");

        var label = createLabel("Welcome To Our Book Store!", "style");

        VBox vbox = new VBox(label);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPrefHeight(200);
        vbox.getStyleClass().add("top-background");
        VBox.setVgrow(label, Priority.ALWAYS);

        root.setTop(vbox);

        HBox buttonContainer = new HBox(150);
        buttonContainer.setAlignment(Pos.CENTER);

        Button adminButton = createButton("Admin", "admin");
        Button readerButton = createButton("Reader", "reader");

        applyHoverEffect(adminButton);
        applyHoverEffect(readerButton);

        adminButton.setOnAction(event ->
        {
            isAdmin = true;
            showLoginForm();
        });

        readerButton.setOnAction(event ->
        {
            isAdmin = false;
            showLoginForm();
        });

        buttonContainer.getChildren().addAll(adminButton, readerButton);

        mainMenu = new VBox(50);
        mainMenu.setAlignment(Pos.CENTER);
        mainMenu.getChildren().add(buttonContainer);

        root.setCenter(mainMenu);

        scene = new Scene(root, 800, 600);
        scene.getStylesheets().add(getClass().getResource("/main_app/resources/css/welcome.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.setTitle("Welcome");
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    private void showMainMenu()
    {
        resetScene("/main_app/resources/css/welcome.css");
        root.setCenter(mainMenu);
    }

    private void showLoginForm()
    {
        resetScene("/main_app/resources/css/login.css");

        VBox loginForm = new VBox(20);
        loginForm.setPadding(new Insets(20));
        loginForm.setAlignment(Pos.CENTER);
        loginForm.setMaxSize(400, 400);
        loginForm.getStyleClass().add("form-container");

        Label loginLabel = createLabel("Login", "login");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter your username");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");

        Button loginButton = createButton("Login", null);
        applyHoverEffect(loginButton);
        loginButton.setOnAction(event ->
        {
            user = User.login(
                    usernameField.getText(),
                    SecurityService.hash(passwordField.getText()),
                    isAdmin ? "admin" : "reader");
            if (user != null)
            {
                showUserOptions();
            }
            else
            {
                showAlert("Error", "Incorrect username or password.", AlertType.ERROR);
            }
        });

        Button backButton = createButton("Back", null);
        applyHoverEffect(backButton);
        backButton.setOnAction(event -> showMainMenu());

        loginForm.getChildren().addAll(loginLabel, usernameField, passwordField, loginButton, backButton);

        if (!isAdmin)
        {
            Label registerLabel = new Label("Don't have an account? Register here");
            registerLabel.setTextFill(Color.WHITE);
            registerLabel.setFont(Font.font("System", FontWeight.BOLD, 12));
            registerLabel.setTextAlignment(TextAlignment.CENTER);
            registerLabel.setStyle("-fx-cursor: hand;");
            VBox.setMargin(registerLabel, new Insets(30, 0, 0, 0));

            registerLabel.setOnMouseClicked(event ->
            {
                resetScene("/main_app/resources/css/register.css");

                VBox registerForm = new VBox(20);
                registerForm.setPadding(new Insets(20));
                registerForm.setAlignment(Pos.CENTER);
                registerForm.setMaxSize(600, 600);
                registerForm.getStyleClass().add("form-container");

                Label registerLabel_Form = createLabel("Registration Form", "login");

                TextField emailField = new TextField();
                emailField.setPromptText("Enter your email");

                TextField usernameRegisterField = new TextField();
                usernameRegisterField.setPromptText("Enter your username");

                TextField paymentMethodField = new TextField();
                paymentMethodField.setPromptText("Enter your payment method");

                PasswordField passwordRegisterField = new PasswordField();
                passwordRegisterField.setPromptText("Enter your password");

                TextField addressField = new TextField();
                addressField.setPromptText("Enter your address");

                TextField phoneNumberField = new TextField();
                phoneNumberField.setPromptText("Enter your phone number");

                Button saveButton = createButton("Register", null);
                applyHoverEffect(saveButton);
                saveButton.setOnAction(
                        event_Register ->
                        {
                            String email = emailField.getText();
                            String username = usernameField.getText();
                            String paymentMethod = paymentMethodField.getText();
                            String password = passwordField.getText();
                            String address = addressField.getText();
                            String phoneNumber = phoneNumberField.getText();

                            if (email.isEmpty() || username.isEmpty() || paymentMethod.isEmpty() || password.isEmpty()
                                    || address.isEmpty()
                                    || phoneNumber.isEmpty())
                            {
                                showAlert("Incomplete Form", "Please fill in all the fields.", AlertType.ERROR);
                                return;
                            }

                            if (Reader.register(email, username, password, phoneNumber, address, paymentMethod))
                            {
                                showAlert("Registration Successful", "You have successfully registered.",
                                        AlertType.INFORMATION);
                                showLoginForm();
                            }
                            else
                            {
                                showAlert("Registration Failed", "A user already exists with that email!",
                                        AlertType.ERROR);
                            }
                        });

                Button backButton_Register = createButton("Back", null);
                applyHoverEffect(backButton_Register);
                backButton_Register.setOnAction(event_Back -> showMainMenu());

                HBox buttonContainer = new HBox(15, saveButton, backButton_Register);
                buttonContainer.setAlignment(Pos.CENTER);

                registerForm.getChildren().addAll(
                        registerLabel_Form, emailField, usernameRegisterField, paymentMethodField,
                        passwordRegisterField, addressField, phoneNumberField, buttonContainer);

                root.setCenter(registerForm);
            });
            loginForm.getChildren().add(registerLabel);
        }

        root.setCenter(loginForm);
    }

    @SuppressWarnings("unchecked")
    private void showUserOptions()
    {
        root.getChildren().clear();
        root.getStyleClass().add("borderpane-style");
        resetScene("/main_app/resources/css/reader_menu.css");

        HBox topContainer = new HBox(20);
        topContainer.setAlignment(Pos.TOP_RIGHT);
        topContainer.setPadding(new Insets(10));

        Button logoutButton = createButton("Logout", "logout");
        logoutButton.setMaxSize(150, 80);
        applyHoverEffect(logoutButton);
        logoutButton.setOnAction(event ->
        {
            user = null;
            root.getChildren().clear();
            showMainMenu();
        });

        topContainer.getChildren().add(logoutButton);

        VBox options = new VBox(50);
        options.setAlignment(Pos.CENTER);
        root.setCenter(options);

        if (!isAdmin)
        {
            HBox line1 = new HBox(50);
            line1.setAlignment(Pos.CENTER);

            Button editAccountButton = createButton("Edit Account Information", null);
            applyHoverEffect(editAccountButton);
            editAccountButton.setOnAction(event ->
            {
                root.getChildren().clear();
                root.getStyleClass().add("borderpane-style");
                resetScene("/main_app/resources/css/common.css");

                Label editInfoLabel = createLabel("Edit Information", "style");

                VBox formContainer = new VBox(15);
                formContainer.setPadding(new Insets(20));
                formContainer.setAlignment(Pos.CENTER);
                formContainer.setMaxSize(600, 600);
                formContainer.getStyleClass().add("form-container");

                Reader reader = (Reader) user;

                TextField usernameField = new TextField(reader.getUsername());
                usernameField.setPromptText("Enter your new username");

                TextField paymentMethodField = new TextField(reader.getPaymentMethod());
                paymentMethodField.setPromptText("Enter your new payment method");

                PasswordField passwordField = new PasswordField();
                passwordField.setPromptText("Enter your new password");
                passwordField.setText(reader.getPassword());

                TextField addressField = new TextField(reader.getAddress());
                addressField.setPromptText("Enter your new address");

                TextField phoneNumberField = new TextField(reader.getPhoneNumber());
                phoneNumberField.setPromptText("Enter your new phone number");

                Button saveButton = createButton("Save", null);
                applyHoverEffect(saveButton);
                saveButton
                        .setOnAction(event_update ->
                        {
                            reader.setUsername(usernameField.getText());
                            reader.setPaymentMethod(paymentMethodField.getText());

                            if (!passwordField.getText().equals(reader.getPassword()))
                            {
                                reader.setPassword(SecurityService.hash(passwordField.getText()));
                            }

                            reader.setAddress(addressField.getText());
                            reader.setPhoneNumber(phoneNumberField.getText());

                            reader.updateReaderInformation();

                            showAlert("Information Updated", "Your account information has been updated successfully.",
                                    AlertType.INFORMATION);
                            showUserOptions();
                        });

                Button backButton = createButton("Back", null);
                applyHoverEffect(backButton);
                backButton.setOnAction(event_options -> showUserOptions());

                formContainer.getChildren().addAll(editInfoLabel, usernameField, paymentMethodField, passwordField,
                        addressField, phoneNumberField, saveButton, backButton);

                root.setCenter(formContainer);
            });

            Button showOrdersButton = createButton("Show Previous Orders", null);
            applyHoverEffect(showOrdersButton);
            showOrdersButton.setOnAction(event ->
            {
                root.getChildren().clear();
                root.getStyleClass().add("borderpane-style");
                resetScene("/main_app/resources/css/common.css");

                VBox orderContainer = new VBox(10);
                orderContainer.setAlignment(Pos.CENTER);

                TableView<Order> orderTable = new TableView<>();
                orderTable.getStyleClass().add("table-view");

                TableColumn<Order, Integer> idColumn = new TableColumn<>("Order ID");
                idColumn.setCellValueFactory(new PropertyValueFactory<>("orderID"));
                idColumn.setPrefWidth(250);

                TableColumn<Order, Date> dateColumn = new TableColumn<>("Date");
                dateColumn.setCellValueFactory(new PropertyValueFactory<>("dateCreated"));
                dateColumn.setPrefWidth(250);

                TableColumn<Order, Double> totalColumn = new TableColumn<>("Total Pay");
                totalColumn.setCellValueFactory(new PropertyValueFactory<>("total"));
                totalColumn.setPrefWidth(150);

                orderTable.getColumns().addAll(idColumn, dateColumn, totalColumn);
                orderTable.getItems().addAll(((Reader) user).getPreviousOrders());

                HBox orderButtonsContainer = new HBox(300);
                orderButtonsContainer.setAlignment(Pos.CENTER);

                Button backButton = createButton("Back", null);
                applyHoverEffect(backButton);
                backButton.setPrefSize(120, 50);
                backButton.setOnAction(event_options -> showUserOptions());

                orderButtonsContainer.getChildren().addAll(backButton);

                orderContainer.getChildren().addAll(orderTable, orderButtonsContainer);

                root.setCenter(orderContainer);

                orderTable.setPrefHeight(600);
                orderTable.setMaxWidth(650);
            });

            line1.getChildren().addAll(editAccountButton, showOrdersButton);

            HBox line2 = new HBox(50);
            line2.setAlignment(Pos.CENTER);

            Button displayBooksButton = createButton("Display Books", null);
            applyHoverEffect(displayBooksButton);
            displayBooksButton.setOnAction(event -> showAllBooks());

            Button showCartButton = createButton("Display Cart", null);
            applyHoverEffect(showCartButton);
            showCartButton.setOnAction(event -> showCart());

            line2.getChildren().addAll(displayBooksButton, showCartButton);

            options.getChildren().addAll(line1, line2);
        }
        else
        {
            showAllBooks();
        }

        root.setTop(topContainer);
    }

    @SuppressWarnings("unchecked")
    private void showAllBooks()
    {
        root.getChildren().clear();
        root.getStyleClass().add("borderpane-style");
        resetScene("/main_app/resources/css/searchbook.css");

        HBox topContainer = new HBox(20);
        topContainer.setAlignment(Pos.TOP_RIGHT);
        topContainer.setPadding(new Insets(10));

        if (isAdmin)
        {
            Button logoutButton = createButton("Logout", null);
            logoutButton.setPrefSize(120, 50);
            applyHoverEffect(logoutButton);
            logoutButton.setOnAction(event ->
            {
                user = null;
                root.getChildren().clear();
                showMainMenu();
            });

            topContainer.getChildren().add(logoutButton);
        }
        else
        {
            Button backButton = createButton("Back", null);
            applyHoverEffect(backButton);
            backButton.setPrefSize(120, 50);
            backButton.setOnAction(event -> showUserOptions());
            topContainer.getChildren().add(backButton);
        }

        VBox searchBookContainer = new VBox(10);
        searchBookContainer.setAlignment(Pos.CENTER);
        searchBookContainer.setPadding(new Insets(20));

        TextField searchBookField = new TextField();
        searchBookField.setPromptText("Enter book name");
        searchBookField.setMaxWidth(1100);
        searchBookField.setPrefHeight(60);
        searchBookField.getStyleClass().add("text-field");

        TableView<Book> bookTable = new TableView<>();
        bookTable.getStyleClass().add("table-view");

        TableColumn<Book, String> nameColumn = new TableColumn<>("Book Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setPrefWidth(350);

        TableColumn<Book, String> authorColumn = new TableColumn<>("Author");
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        authorColumn.setPrefWidth(350);

        TableColumn<Book, Integer> stockColumn = new TableColumn<>("Stock");
        stockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        stockColumn.setPrefWidth(100);

        TableColumn<Book, String> categoryColumn = new TableColumn<>("Book Category");
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        categoryColumn.setPrefWidth(200);

        TableColumn<Book, Double> priceColumn = new TableColumn<>("Price");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        priceColumn.setPrefWidth(100);

        bookTable.getColumns().addAll(nameColumn, authorColumn, stockColumn, categoryColumn, priceColumn);

        ObservableList<Book> bookList = FXCollections.observableArrayList();
        ArrayList<String> books = FileManager.readFile(Constants.BOOKS_FILE_PATH);

        for (String line : books)
        {
            if (isAdmin || !line.contains(",0,"))
            {
                String[] bookDetails = line.split(",");
                Book book = new Book(
                        bookDetails[0],
                        bookDetails[1],
                        Double.parseDouble(bookDetails[2]),
                        Integer.parseInt(bookDetails[3]),
                        bookDetails[4]);
                bookList.add(book);
            }
        }

        bookTable.setItems(bookList);

        searchBookField.textProperty()
                .addListener((observable, oldValue, newValue) ->
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

                    bookTable.setItems(filteredList);
                });

        HBox searchBookButtonsContainer = new HBox(20);
        searchBookButtonsContainer.setAlignment(Pos.CENTER);

        if (isAdmin)
        {
            Button deleteBookButton = createButton("Delete Book", null);
            deleteBookButton.setPrefSize(150, 50);
            applyHoverEffect(deleteBookButton);
            deleteBookButton.setOnAction(event ->
            {
                Book selectedBook = bookTable.getSelectionModel().getSelectedItem();
                if (selectedBook != null)
                {
                    bookList.remove(selectedBook);
                    ((Admin) user).deleteBook(selectedBook);
                }
                else
                {
                    showAlert("No Selection", "Please select a book to delete.", AlertType.WARNING);
                }
            });

            Button editBookButton = createButton("Edit Book", null);
            editBookButton.setPrefSize(150, 50);
            applyHoverEffect(editBookButton);
            editBookButton.setOnAction(event ->
            {
                Book selectedBook = bookTable.getSelectionModel().getSelectedItem();
                if (selectedBook != null)
                {
                    showBookInputDialog(selectedBook, bookList, bookTable);
                }
                else
                {
                    showAlert("No Selection", "Please select a book to edit.", AlertType.WARNING);
                }
            });

            Button addBookButton = createButton("Add New Book", null);
            addBookButton.setPrefSize(150, 50);
            applyHoverEffect(addBookButton);
            addBookButton.setOnAction(event -> showBookInputDialog(null, bookList, bookTable));

            searchBookButtonsContainer.getChildren().addAll(addBookButton, editBookButton, deleteBookButton);
        }
        else
        {
            Button addToCartButton = createButton("Add to Cart", null);
            addToCartButton.setPrefSize(150, 50);
            applyHoverEffect(addToCartButton);
            addToCartButton.setOnAction(event ->
            {
                Book selectedBook = bookTable.getSelectionModel().getSelectedItem();
                if (selectedBook != null)
                {
                    var shoppingCart = ((Reader) user).getShoppingCart();
                    if (!shoppingCart.getAddedBooks().contains(selectedBook))
                    {
                        shoppingCart.addToCart(selectedBook);
                        showConfirmation("The selected book has been added to your cart.", searchBookContainer);
                    }
                    else
                    {
                        showAlert("Error", "Book already in cart.", AlertType.ERROR);
                    }

                }
                else
                {
                    showAlert("No Selection", "Please select a book to add to cart.", AlertType.WARNING);
                }
            });

            searchBookButtonsContainer.getChildren().add(addToCartButton);
        }

        searchBookContainer.getChildren().addAll(searchBookField, bookTable, searchBookButtonsContainer);

        root.setTop(topContainer);
        root.setCenter(searchBookContainer);

        bookTable.setPrefHeight(600);
        bookTable.setMaxWidth(1100);
    }

    private void showBookInputDialog(Book book, ObservableList<Book> bookList, TableView<Book> bookTable)
    {
        Dialog<Book> dialog = new Dialog<>();
        dialog.setTitle(book == null ? "Add New Book" : "Edit Book");

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

        if (book != null)
        {
            nameField.setText(book.getName());
            authorField.setText(book.getAuthor());
            priceField.setText(String.valueOf(book.getPrice()));
            stockField.setText(String.valueOf(book.getStock()));
            categoryField.setText(book.getCategory());
        }

        VBox content = new VBox(10);
        content.getChildren().addAll(
                new Label("Book Name:"), nameField,
                new Label("Author:"), authorField,
                new Label("Price:"), priceField,
                new Label("Stock:"), stockField,
                new Label("Category:"), categoryField);

        dialog.getDialogPane().setContent(content);

        ButtonType saveButtonType = new ButtonType("Save", ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton ->
        {
            if (dialogButton == saveButtonType)
            {
                Book newBook = new Book(
                        nameField.getText(),
                        authorField.getText(),
                        Double.parseDouble(priceField.getText()),
                        Integer.parseInt(stockField.getText()),
                        categoryField.getText());

                if (book == null)
                {
                    bookList.add(newBook);
                    ((Admin) user).addBook(newBook);
                }
                else
                {
                    bookList.remove(book);
                    bookList.add(newBook);
                    ((Admin) user).editBook(book, newBook);
                }
            }
            return null;
        });

        dialog.showAndWait();
    }

    @SuppressWarnings("unchecked")
    private void showCart()
    {
        root.getChildren().clear();
        root.getStyleClass().add("borderpane-style");
        resetScene("/main_app/resources/css/common.css");

        VBox cartContainer = new VBox(10);
        cartContainer.setAlignment(Pos.CENTER);

        Label cartLabel = createLabel("Your cart", "style");

        TableView<Book> cartTable = new TableView<>();
        cartTable.getStyleClass().add("table-view");

        TableColumn<Book, String> bookNameColumn = new TableColumn<>("Book Name");
        bookNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        bookNameColumn.setPrefWidth(350);

        TableColumn<Book, String> authorColumn = new TableColumn<>("Author");
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        authorColumn.setPrefWidth(350);

        TableColumn<Book, String> categoryColumn = new TableColumn<>("Book category");
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        categoryColumn.setPrefWidth(200);

        TableColumn<Book, Double> priceColumn = new TableColumn<>("Price");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        priceColumn.setPrefWidth(100);

        cartTable.getColumns().addAll(bookNameColumn, authorColumn, categoryColumn, priceColumn);

        ArrayList<Book> addedBooks = ((Reader) user).getShoppingCart().getAddedBooks();
        cartTable.getItems().addAll(addedBooks);

        HBox cartButtonsContainer = new HBox(300);
        cartButtonsContainer.setAlignment(Pos.CENTER);

        Button backButton = createButton("Back", null);
        applyHoverEffect(backButton);
        backButton.setOnAction(event -> showUserOptions());

        Button removeButton = createButton("Remove", null);
        applyHoverEffect(removeButton);
        removeButton.setOnAction(event ->
        {
            Book selectedBook = cartTable.getSelectionModel().getSelectedItem();
            if (selectedBook != null)
            {
                cartTable.getItems().remove(selectedBook);
                ((Reader) user).getShoppingCart().removeFromCart(selectedBook);
            }
            else
            {
                showAlert("No Selection", "Please select a book to remove.", AlertType.WARNING);
            }
        });

        Button receiptButton = createButton("Go To Receipt", null);
        applyHoverEffect(receiptButton);
        receiptButton.setOnAction(event ->
        {
            if (!addedBooks.isEmpty())
            {
                root.getChildren().clear();
                root.getStyleClass().add("borderpane-style");
                resetScene("/main_app/resources/css/receipt.css");

                VBox receiptContainer = new VBox(10);
                receiptContainer.setAlignment(Pos.CENTER);

                Label receiptLabel = createLabel("Receipt", "style");

                TableView<Book> receiptTable = new TableView<>();
                receiptTable.getStyleClass().add("table-view");

                receiptTable.getColumns().addAll(bookNameColumn, authorColumn, categoryColumn, priceColumn);

                double total = addedBooks.stream()
                        .peek(receiptTable.getItems()::add).mapToDouble(Book::getPrice).sum();

                Label totalLabel = new Label();
                totalLabel.setText("Total: $" + total);
                totalLabel.getStyleClass().add("total-label");

                HBox receiptButtonsContainer = new HBox(300);
                receiptButtonsContainer.setAlignment(Pos.CENTER);

                Button payButton = createButton("Pay", null);
                applyHoverEffect(payButton);
                payButton.setPrefSize(120, 50);
                payButton.setOnAction(event_Payment ->
                {
                    Order order = new Order(Math.abs(new java.util.Random().nextInt()), new Date(), (Reader) user,
                            total);
                    order.generateOrder(addedBooks);
                    ((Reader) user).getPreviousOrders().add(order);

                    showAlert("Success", "The books have been purchased successfully.", AlertType.INFORMATION);
                    showUserOptions();
                });

                Button backButton_Receipt = createButton("Back", null);
                applyHoverEffect(backButton_Receipt);
                backButton_Receipt.setPrefSize(120, 50);
                backButton_Receipt.setOnAction(event_showCart -> showCart());

                receiptButtonsContainer.getChildren().addAll(backButton_Receipt, payButton);

                receiptContainer.getChildren().addAll(receiptTable, totalLabel, receiptButtonsContainer);

                root.setTop(receiptLabel);
                root.setCenter(receiptContainer);

                receiptTable.setPrefHeight(600);
                receiptTable.setMaxWidth(1000);
            }
            else
            {
                showAlert("No books in cart", "You don't have any books added to your cart.", AlertType.ERROR);
            }
        });

        cartButtonsContainer.getChildren().addAll(backButton, removeButton, receiptButton);

        cartContainer.getChildren().addAll(cartTable, cartButtonsContainer);

        root.setTop(cartLabel);
        root.setCenter(cartContainer);

        cartTable.setPrefHeight(600);
        cartTable.setMaxWidth(1000);
    }

    private void showAlert(String title, String content, AlertType alertType)
    {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showConfirmation(String message, VBox vbox)
    {
        Label confirmationLabel = new Label(message);
        confirmationLabel.setStyle("-fx-background-color: lightgreen; -fx-padding: 10px;");
        vbox.getChildren().add(confirmationLabel);

        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(event_ -> vbox.getChildren().remove(confirmationLabel));
        pause.play();
    }

    private void resetScene(String cssFile)
    {
        scene.getStylesheets().clear();
        scene.getStylesheets().add(getClass().getResource(cssFile).toExternalForm());
    }

    private Label createLabel(String text, String styleClass)
    {
        Label label = new Label(text);
        label.setAlignment(Pos.CENTER);
        label.setMaxWidth(Double.MAX_VALUE);
        label.setId(styleClass);
        return label;
    }

    private Button createButton(String text, String id)
    {
        Button button = new Button(text);
        if (id != null)
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

    public static void main(String[] args)
    {
        launch(args);
    }
}
