module main_app
{
    requires javafx.controls;
    requires javafx.fxml;

    // Open the main_app.models package to javafx.base for reflective access
    opens main_app.models to javafx.base;

    // Keep this if main_app itself needs to be open to javafx.fxml
    opens main_app to javafx.fxml;

    // Export the main_app package
    exports main_app;
}
