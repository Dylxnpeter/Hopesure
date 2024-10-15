import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.image.ImageView;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static javafx.application.Application.launch;

public class SystemSettingsManager {

    public void showSystemSettingsSelection() {
        Stage stage = new Stage();
        stage.setTitle("System Settings");

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(20));
        vbox.setAlignment(Pos.CENTER);

        Label selectLabel = new Label("Select System Settings Option:");

        Button systemSetupButton = new Button("System Setup");
        systemSetupButton.setOnAction(e -> {
            stage.close();
            showSystemSetup();
        });

        Button userProfileButton = new Button("User Profile");
        userProfileButton.setOnAction(e -> {
            stage.close();
            showUserProfile();
        });

        Button userManualButton = new Button("User Manual and Documentation");
        userManualButton.setOnAction(e -> {
            stage.close();
            showUserManuals();
        });

        Button feedbackButton = new Button("Feedback");
        feedbackButton.setOnAction(e -> {
            stage.close();
            showFeedbackForm();
        });

        vbox.getChildren().addAll(selectLabel, systemSetupButton, userProfileButton, userManualButton, feedbackButton);

        Scene scene = new Scene(vbox, 400, 300);
        stage.setScene(scene);
        stage.show();
    }

    // 1. System Setup
    public void showSystemSetup() {
        Stage stage = new Stage();
        stage.setTitle("System Setup");

        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setPadding(new Insets(10));
        grid.setAlignment(Pos.CENTER);

        Label modeLabel = new Label("Choose Theme:");
        ToggleGroup themeGroup = new ToggleGroup();

        RadioButton darkMode = new RadioButton("Dark Mode");
        darkMode.setToggleGroup(themeGroup);
        RadioButton lightMode = new RadioButton("Light Mode");
        lightMode.setToggleGroup(themeGroup);
        RadioButton matchDevice = new RadioButton("Match Device Settings");
        matchDevice.setToggleGroup(themeGroup);

        Button saveButton = new Button("Save");
        saveButton.setOnAction(e -> {
            RadioButton selectedRadioButton = (RadioButton) themeGroup.getSelectedToggle();
            String selectedTheme = selectedRadioButton.getText();
            saveSystemTheme(selectedTheme);
            showAlert("Success", "System theme has been updated to " + selectedTheme);
            stage.close();
        });

        VBox vbox = new VBox(10, modeLabel, darkMode, lightMode, matchDevice, saveButton);
        vbox.setAlignment(Pos.CENTER);

        Scene scene = new Scene(vbox, 300, 200);
        stage.setScene(scene);
        stage.show();
    }

    private void saveSystemTheme(String theme) {
        // Implement logic to save the selected theme to system settings
    }

    // 2. User Profile
    public void showUserProfile() {
        System.out.println("Opening User Profile..."); // Debug statement

        Stage stage = new Stage();
        stage.setTitle("User Profile");

        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setPadding(new Insets(10));
        grid.setAlignment(Pos.CENTER);

        // User Photo
        ImageView photoView = new ImageView(); // Add a default photo or user's photo
        photoView.setFitWidth(100);
        photoView.setFitHeight(100);
        // Replace with the actual user's photo if available
        // photoView.setImage(new Image("file:path_to_user_photo"));

        Label photoLabel = new Label("Photo:");
        GridPane.setConstraints(photoView, 1, 0, 2, 1);

        // User Information
        Label nameLabel = new Label("User Name:");
        TextField nameField = new TextField();
        nameField.setEditable(false);

        Label titleLabel = new Label("Title:");
        TextField titleField = new TextField();
        titleField.setEditable(false);

        Label emailLabel = new Label("Email:");
        TextField emailField = new TextField();
        emailField.setEditable(false);

        Label phoneLabel = new Label("Phone:");
        TextField phoneField = new TextField();
        phoneField.setEditable(false);

        Button changePasswordButton = new Button("Change Password");
        changePasswordButton.setOnAction(e -> showChangePasswordForm());

        grid.add(photoLabel, 0, 0);
        grid.add(photoView, 1, 0, 2, 1);
        grid.add(nameLabel, 0, 1);
        grid.add(nameField, 1, 1);
        grid.add(titleLabel, 0, 2);
        grid.add(titleField, 1, 2);
        grid.add(emailLabel, 0, 3);
        grid.add(emailField, 1, 3);
        grid.add(phoneLabel, 0, 4);
        grid.add(phoneField, 1, 4);
        grid.add(changePasswordButton, 1, 5);

        // Fetch and display user profile data
        fetchAndDisplayUserProfile(nameField, titleField, emailField, phoneField);

        Scene scene = new Scene(grid, 400, 300);
        stage.setScene(scene);
        stage.show();
    }

    private void showChangePasswordForm() {
    }

    private void fetchAndDisplayUserProfile(TextField nameField, TextField titleField, TextField emailField, TextField phoneField) {
        String url = "jdbc:your_database_url";
        String user = "username";
        String password = "password";

        String query = "SELECT user_name, title, email, phone FROM user_profile WHERE user_id = ?";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            // Assuming you have a way to get the current user's ID
            String currentUserId = getCurrentUserId();
            stmt.setString(1, currentUserId);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                nameField.setText(rs.getString("user_name"));
                titleField.setText(rs.getString("title"));
                emailField.setText(rs.getString("email"));
                phoneField.setText(rs.getString("phone"));

                // Optionally, set the photo if available
                // String photoPath = rs.getString("photo_path");
                // photoView.setImage(new Image("file:" + photoPath));
            } else {
                showAlert("User Profile Error", "User profile not found.");
            }

        } catch (SQLException e) {
            System.out.println("Database connection error: " + e.getMessage());
            showAlert("Database Error", "Unable to load user profile.");
        }
    }

    private String getCurrentUserId() {
        // Implement a method to return the current user's ID
        return "current_user_id"; // Replace with actual logic to get the current user ID
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // 3. User Manual and Documentation
    public void showUserManuals() {
        Stage stage = new Stage();
        stage.setTitle("User Manual and Documentation");

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(20));
        vbox.setAlignment(Pos.CENTER);

        Label selectLabel = new Label("User Manual and Documentation");

        Button addManualsButton = new Button("Add Manuals");
        addManualsButton.setOnAction(e -> showAddManualsForm());

        Button viewManualsButton = new Button("View Existing Manuals");
        viewManualsButton.setOnAction(e -> showViewManuals());

        vbox.getChildren().addAll(selectLabel, addManualsButton, viewManualsButton);

        Scene scene = new Scene(vbox, 400, 300);
        stage.setScene(scene);
        stage.show();
    }

    private void showAddManualsForm() {
        Stage stage = new Stage();
        stage.setTitle("Add Manuals");

        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setPadding(new Insets(10));
        grid.setAlignment(Pos.CENTER);

        Label fileLabel = new Label("Select Manual File:");
        Button chooseFileButton = new Button("Choose File");
        Label filePathLabel = new Label("No file chosen");

        chooseFileButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select Manual File");
            File file = fileChooser.showOpenDialog(stage);
            if (file != null) {
                filePathLabel.setText(file.getAbsolutePath());
                // Save the file to the database or file system
                saveManualFile(file);
            }
        });

        Button saveButton = new Button("Save");
        saveButton.setOnAction(e -> {
            if (!filePathLabel.getText().equals("No file chosen")) {
                showAlert("Success", "Manual has been added successfully.");
                stage.close();
            } else {
                showAlert("Input Error", "Please choose a file to upload.");
            }
        });

        grid.add(fileLabel, 0, 0);
        grid.add(chooseFileButton, 1, 0);
        grid.add(filePathLabel, 1, 1);
        grid.add(saveButton, 1, 2);

        Scene scene = new Scene(grid, 400, 200);
        stage.setScene(scene);
        stage.show();
    }

    private void saveManualFile(File file) {
        // Implement the logic to save the file to your database or file system
    }

    private void showViewManuals() {
        Stage stage = new Stage();
        stage.setTitle("View Existing Manuals");

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(20));
        vbox.setAlignment(Pos.CENTER);

        Label manualLabel = new Label("Available Manuals:");

        ListView<String> manualListView = new ListView<>();
        loadManuals(manualListView);

        if (manualListView.getItems().isEmpty()) {
            manualListView.getItems().add("No existing user manual/documentation.");
        }

        vbox.getChildren().addAll(manualLabel, manualListView);

        Scene scene = new Scene(vbox, 400, 300);
        stage.setScene(scene);
        stage.show();
    }

    private void loadManuals(ListView<String> listView) {
        // Implement the logic to load existing manuals from your database or file system
        String[] manuals = getExistingManuals(); // Replace with actual retrieval logic
        listView.getItems().addAll(manuals);
    }

    private String[] getExistingManuals() {
        // Implement the logic to fetch existing manuals from your database or file system
        return new String[0]; // Replace with actual manual retrieval
    }

    public void showFeedbackForm() {
        Stage stage = new Stage();
        stage.setTitle("Feedback Form");

        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setPadding(new Insets(10));
        grid.setAlignment(Pos.CENTER);

        Label feedbackLabel = new Label("Feedback:");
        TextArea feedbackArea = new TextArea();
        feedbackArea.setPrefRowCount(5);

        Button submitButton = new Button("Submit");
        submitButton.setOnAction(e -> {
            String feedback = feedbackArea.getText();
            if (!feedback.isEmpty()) {
                submitFeedback(feedback);
                showAlert("Success", "Feedback submitted successfully.");
                stage.close();
            } else {
                showAlert("Input Error", "Feedback cannot be empty.");
            }
        });

        grid.add(feedbackLabel, 0, 0);
        grid.add(feedbackArea, 1, 0);
        grid.add(submitButton, 1, 1);

        Scene scene = new Scene(grid, 400, 300);
        stage.setScene(scene);
        stage.show();
    }

    private void submitFeedback(String feedback) {
        // Implement logic to submit feedback
    }

    public static void main(String[] args) {
        launch(args);
    }
}
