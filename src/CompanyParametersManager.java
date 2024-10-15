import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.control.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CompanyParametersManager {
    private Stage primaryStage;

    public CompanyParametersManager(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void showCompanyParametersPage() {
        Stage stage = new Stage();
        stage.setTitle("Company Parameters");

        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setPadding(new Insets(10));
        grid.setAlignment(Pos.CENTER);

        Button productSetupButton = new Button("Product Setup");
        productSetupButton.setOnAction(e -> showProductSetupPage());

        Button additionalProductsSetupButton = new Button("Additional Products Setup");
        additionalProductsSetupButton.setOnAction(e -> showAdditionalProductsSetupPage());

        Button salesRepSetupButton = new Button("Sales Rep/Agent Setup");
        salesRepSetupButton.setOnAction(e -> showSalesRepSetupPage());

        Button userMaintenanceButton = new Button("User Maintenance");
        userMaintenanceButton.setOnAction(e -> showUserMaintenancePage());

        // Removed Employee Setup button
        grid.add(productSetupButton, 0, 0);
        grid.add(additionalProductsSetupButton, 1, 0);
        grid.add(salesRepSetupButton, 0, 1);
        grid.add(userMaintenanceButton, 1, 1);

        Scene scene = new Scene(grid, 400, 250);
        stage.setScene(scene);
        stage.show();
    }

    private void showProductSetupPage() {
        Stage stage = new Stage();
        stage.setTitle("Product Setup");

        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setPadding(new Insets(10));
        grid.setAlignment(Pos.CENTER);

        Label productLabel = new Label("Select Product:");
        ComboBox<String> productComboBox = new ComboBox<>();
        loadProductsIntoComboBox(productComboBox);

        Label nameLabel = new Label("Product Name:");
        TextField nameField = new TextField();

        Label benefitsLabel = new Label("Product Benefits:");
        TextArea benefitsArea = new TextArea();
        benefitsArea.setPrefRowCount(5);

        Button saveButton = new Button("Save");
        saveButton.setOnAction(e -> {
            String selectedProduct = productComboBox.getValue();
            String productName = nameField.getText();
            String productBenefits = benefitsArea.getText();

            if (productName.isEmpty() || productBenefits.isEmpty()) {
                showAlert("Input Error", "Please fill in all fields.");
            } else {
                if (selectedProduct.equals("Add New Product...")) {
                    addNewProduct(productName, productBenefits);
                } else {
                    updateProduct(selectedProduct, productName, productBenefits);
                }
                showAlert("Success", "Product details have been saved successfully.");
                stage.close();
            }
        });

        grid.add(productLabel, 0, 0);
        grid.add(productComboBox, 1, 0);
        grid.add(nameLabel, 0, 1);
        grid.add(nameField, 1, 1);
        grid.add(benefitsLabel, 0, 2);
        grid.add(benefitsArea, 1, 2);
        grid.add(saveButton, 0, 3, 2, 1);

        Scene scene = new Scene(grid, 600, 400);
        stage.setScene(scene);
        stage.show();
    }

    private void loadProductsIntoComboBox(ComboBox<String> productComboBox) {
        try (Connection conn = DriverManager.getConnection("jdbc:your_database_url", "username", "password");
             PreparedStatement stmt = conn.prepareStatement("SELECT product_name FROM products");
             ResultSet rs = stmt.executeQuery()) {

            productComboBox.getItems().clear();
            while (rs.next()) {
                productComboBox.getItems().add(rs.getString("product_name"));
            }
            productComboBox.getItems().add("Add New Product...");

        } catch (SQLException e) {
            System.out.println("Database connection error: " + e.getMessage());
        }
    }

    private void addNewProduct(String productName, String productBenefits) {
        try (Connection conn = DriverManager.getConnection("jdbc:your_database_url", "username", "password");
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO products (product_name, benefits) VALUES (?, ?)")) {

            stmt.setString(1, productName);
            stmt.setString(2, productBenefits);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Database connection error: " + e.getMessage());
        }
    }

    private void updateProduct(String selectedProduct, String productName, String productBenefits) {
        try (Connection conn = DriverManager.getConnection("jdbc:your_database_url", "username", "password");
             PreparedStatement stmt = conn.prepareStatement("UPDATE products SET product_name = ?, benefits = ? WHERE product_name = ?")) {

            stmt.setString(1, productName);
            stmt.setString(2, productBenefits);
            stmt.setString(3, selectedProduct);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Database connection error: " + e.getMessage());
        }
    }

    private void showAdditionalProductsSetupPage() {
        Stage stage = new Stage();
        stage.setTitle("Additional Products Setup");

        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setPadding(new Insets(10));
        grid.setAlignment(Pos.CENTER);

        Label additionalProductLabel = new Label("Select Additional Product:");
        ComboBox<String> additionalProductComboBox = new ComboBox<>();
        loadAdditionalProductsIntoComboBox(additionalProductComboBox);

        Label additionalNameLabel = new Label("Additional Product Name:");
        TextField additionalNameField = new TextField();

        Label additionalDescriptionLabel = new Label("Additional Product Description:");
        TextArea additionalDescriptionArea = new TextArea();
        additionalDescriptionArea.setPrefRowCount(5);

        Button saveButton = new Button("Save");
        saveButton.setOnAction(e -> {
            String selectedProduct = additionalProductComboBox.getValue();
            String additionalProductName = additionalNameField.getText();
            String additionalProductDescription = additionalDescriptionArea.getText();

            if (additionalProductName.isEmpty() || additionalProductDescription.isEmpty()) {
                showAlert("Input Error", "Please fill in all fields.");
            } else {
                if (selectedProduct.equals("Add New Additional Product...")) {
                    addNewAdditionalProduct(additionalProductName, additionalProductDescription);
                } else {
                    updateAdditionalProduct(selectedProduct, additionalProductName, additionalProductDescription);
                }
                showAlert("Success", "Additional product details have been saved successfully.");
                stage.close();
            }
        });

        grid.add(additionalProductLabel, 0, 0);
        grid.add(additionalProductComboBox, 1, 0);
        grid.add(additionalNameLabel, 0, 1);
        grid.add(additionalNameField, 1, 1);
        grid.add(additionalDescriptionLabel, 0, 2);
        grid.add(additionalDescriptionArea, 1, 2);
        grid.add(saveButton, 0, 3, 2, 1);

        Scene scene = new Scene(grid, 600, 400);
        stage.setScene(scene);
        stage.show();
    }

    private void loadAdditionalProductsIntoComboBox(ComboBox<String> additionalProductComboBox) {
        try (Connection conn = DriverManager.getConnection("jdbc:your_database_url", "username", "password");
             PreparedStatement stmt = conn.prepareStatement("SELECT additional_product_name FROM additional_products");
             ResultSet rs = stmt.executeQuery()) {

            additionalProductComboBox.getItems().clear();
            while (rs.next()) {
                additionalProductComboBox.getItems().add(rs.getString("additional_product_name"));
            }
            additionalProductComboBox.getItems().add("Add New Additional Product...");

        } catch (SQLException e) {
            System.out.println("Database connection error: " + e.getMessage());
        }
    }

    private void addNewAdditionalProduct(String additionalProductName, String additionalProductDescription) {
        try (Connection conn = DriverManager.getConnection("jdbc:your_database_url", "username", "password");
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO additional_products (additional_product_name, description) VALUES (?, ?)")) {

            stmt.setString(1, additionalProductName);
            stmt.setString(2, additionalProductDescription);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Database connection error: " + e.getMessage());
        }
    }

    private void updateAdditionalProduct(String selectedProduct, String additionalProductName, String additionalProductDescription) {
        try (Connection conn = DriverManager.getConnection("jdbc:your_database_url", "username", "password");
             PreparedStatement stmt = conn.prepareStatement("UPDATE additional_products SET additional_product_name = ?, description = ? WHERE additional_product_name = ?")) {

            stmt.setString(1, additionalProductName);
            stmt.setString(2, additionalProductDescription);
            stmt.setString(3, selectedProduct);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Database connection error: " + e.getMessage());
        }
    }

    private void showSalesRepSetupPage() {
        Stage stage = new Stage();
        stage.setTitle("Sales Rep/Agent Setup");

        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setPadding(new Insets(10));
        grid.setAlignment(Pos.CENTER);

        // Sales Rep/Agent Biodata Fields
        Label nameLabel = new Label("Name:");
        TextField nameField = new TextField();

        Label idLabel = new Label("ID Number:");
        TextField idField = new TextField();

        Label phoneLabel = new Label("Phone Number:");
        TextField phoneField = new TextField();

        Label emailLabel = new Label("Email:");
        TextField emailField = new TextField();

        // Access Level ComboBox
        Label accessLevelLabel = new Label("Access Level:");
        ComboBox<String> accessLevelComboBox = new ComboBox<>();
        accessLevelComboBox.getItems().addAll("Admin", "User", "Guest"); // Add more roles as needed

        // Privileges Checklist
        Label privilegesLabel = new Label("Privileges:");
        CheckBox viewReportsCheckBox = new CheckBox("View Reports");
        CheckBox editDataCheckBox = new CheckBox("Edit Data");
        CheckBox manageUsersCheckBox = new CheckBox("Manage Users");

        // Photo Upload Button
        Button uploadPhotoButton = new Button("Upload Photo");
        Label photoPathLabel = new Label();
        uploadPhotoButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            File selectedFile = fileChooser.showOpenDialog(stage);
            if (selectedFile != null) photoPathLabel.setText(selectedFile.getAbsolutePath());
        });

        // Save Button
        Button saveButton = new Button("Save");
        saveButton.setOnAction(e -> {
            // Save the agent's information and photo to the database

            // Show confirmation
            showAlert("Save Successful", "Agent information has been saved successfully.");
        });

        // Add components to the grid
        grid.add(nameLabel, 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(idLabel, 0, 1);
        grid.add(idField, 1, 1);
        grid.add(phoneLabel, 0, 2);
        grid.add(phoneField, 1, 2);
        grid.add(emailLabel, 0, 3);
        grid.add(emailField, 1, 3);
        grid.add(accessLevelLabel, 0, 4);
        grid.add(accessLevelComboBox, 1, 4);
        grid.add(privilegesLabel, 0, 5);
        grid.add(viewReportsCheckBox, 1, 5);
        grid.add(editDataCheckBox, 1, 6);
        grid.add(manageUsersCheckBox, 1, 7);
        grid.add(uploadPhotoButton, 0, 8);
        grid.add(photoPathLabel, 1, 8);
        grid.add(saveButton, 1, 9);

        // Set the scene and show the stage
        Scene scene = new Scene(grid, 600, 500); // Adjust size as needed
        stage.setScene(scene);
        stage.show();
    }

    private void loadAgentsIntoComboBox(ComboBox<String> agentComboBox) {
        try (Connection conn = DriverManager.getConnection("jdbc:your_database_url", "username", "password");
             PreparedStatement stmt = conn.prepareStatement("SELECT agent_name FROM agents");
             ResultSet rs = stmt.executeQuery()) {

            agentComboBox.getItems().clear();
            while (rs.next()) {
                agentComboBox.getItems().add(rs.getString("agent_name"));
            }
            agentComboBox.getItems().add("Add New Agent...");

        } catch (SQLException e) {
            System.out.println("Database connection error: " + e.getMessage());
        }
    }

    private void addNewAgent(String agentName, String agentEmail, String agentPhone) {
        try (Connection conn = DriverManager.getConnection("jdbc:your_database_url", "username", "password");
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO agents (agent_name, email, phone) VALUES (?, ?, ?)")) {

            stmt.setString(1, agentName);
            stmt.setString(2, agentEmail);
            stmt.setString(3, agentPhone);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Database connection error: " + e.getMessage());
        }
    }

    private void updateAgent(String selectedAgent, String agentName, String agentEmail, String agentPhone) {
        try (Connection conn = DriverManager.getConnection("jdbc:your_database_url", "username", "password");
             PreparedStatement stmt = conn.prepareStatement("UPDATE agents SET agent_name = ?, email = ?, phone = ? WHERE agent_name = ?")) {

            stmt.setString(1, agentName);
            stmt.setString(2, agentEmail);
            stmt.setString(3, agentPhone);
            stmt.setString(4, selectedAgent);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Database connection error: " + e.getMessage());
        }
    }

    private void showUserMaintenancePage() {
        Stage stage = new Stage();
        stage.setTitle("User Maintenance");

        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setPadding(new Insets(10));
        grid.setAlignment(Pos.CENTER);

        Label userLabel = new Label("Select Agent:");
        ComboBox<String> userComboBox = new ComboBox<>();
        loadUsersIntoComboBox(userComboBox);

        Label userNameLabel = new Label("User Name:");
        TextField userNameField = new TextField();

        Label userEmailLabel = new Label("User Email:");
        TextField userEmailField = new TextField();

        Label userPhoneLabel = new Label("User Phone:");
        TextField userPhoneField = new TextField();

        Label userPhotoLabel = new Label("User Photo:");
        Button uploadPhotoButton = new Button("Upload Photo");
        File[] photoFile = new File[1]; // Array to hold the photo file

        uploadPhotoButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
            File selectedFile = fileChooser.showOpenDialog(stage);
            if (selectedFile != null) {
                photoFile[0] = selectedFile; // Store the selected photo file
                // Optionally display the selected photo in the UI
            }
        });

        Label userAccessLabel = new Label("Access Level:");
        ComboBox<String> accessLevelComboBox = new ComboBox<>();
        accessLevelComboBox.getItems().addAll("Admin", "User", "Guest"); // Example access levels

        Button saveButton = new Button("Save");
        saveButton.setOnAction(e -> {
            String selectedUser = userComboBox.getValue();
            String userName = userNameField.getText();
            String userEmail = userEmailField.getText();
            String userPhone = userPhoneField.getText();
            String accessLevel = accessLevelComboBox.getValue();
            File photo = photoFile[0]; // Get the selected photo

            if (userName.isEmpty() || userEmail.isEmpty() || userPhone.isEmpty() || accessLevel == null) {
                showAlert("Input Error", "Please fill in all fields.");
            } else {
                if (selectedUser.equals("Add New User...")) {
                    addNewUser(userName, userEmail, userPhone, photo, accessLevel);
                } else {
                    updateUser(selectedUser, userName, userEmail, userPhone, photo, accessLevel);
                }
                showAlert("Success", "User details have been saved successfully.");
                stage.close();
            }
        });

        userComboBox.setOnAction(e -> {
            String selectedUser = userComboBox.getValue();
            if (selectedUser != null && !selectedUser.equals("Add New User...")) {
                loadUserDetails(selectedUser, userNameField, userEmailField, userPhoneField, photoFile, accessLevelComboBox);
            }
        });

        grid.add(userLabel, 0, 0);
        grid.add(userComboBox, 1, 0);
        grid.add(userNameLabel, 0, 1);
        grid.add(userNameField, 1, 1);
        grid.add(userEmailLabel, 0, 2);
        grid.add(userEmailField, 1, 2);
        grid.add(userPhoneLabel, 0, 3);
        grid.add(userPhoneField, 1, 3);
        grid.add(userPhotoLabel, 0, 4);
        grid.add(uploadPhotoButton, 1, 4);
        grid.add(userAccessLabel, 0, 5);
        grid.add(accessLevelComboBox, 1, 5);
        grid.add(saveButton, 0, 6, 2, 1);

        Scene scene = new Scene(grid, 600, 400);
        stage.setScene(scene);
        stage.show();
    }
    private void loadUserDetails(String username, TextField nameField, TextField emailField, TextField phoneField, File[] photoFile, ComboBox<String> accessCombo) {
        // Logic to load user details from the database and populate the fields
    }

    private void addNewUser(String userName, String userEmail, String userPhone, File photo, String accessLevel) {
        // Logic to add a new user
    }

    private void updateUser(String selectedUser, String userName, String userEmail, String userPhone, File photo, String accessLevel) {
        // Logic to update user details
    }


    private void loadUsersIntoComboBox(ComboBox<String> userComboBox) {
        try (Connection conn = DriverManager.getConnection("jdbc:your_database_url", "username", "password");
             PreparedStatement stmt = conn.prepareStatement("SELECT user_name FROM users");
             ResultSet rs = stmt.executeQuery()) {

            userComboBox.getItems().clear();
            while (rs.next()) {
                userComboBox.getItems().add(rs.getString("user_name"));
            }
            userComboBox.getItems().add("Add New User...");

        } catch (SQLException e) {
            System.out.println("Database connection error: " + e.getMessage());
        }
    }

    private void addNewUser(String userName, String userEmail, String userPhone) {
        try (Connection conn = DriverManager.getConnection("jdbc:your_database_url", "username", "password");
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO users (user_name, email, phone) VALUES (?, ?, ?)")) {

            stmt.setString(1, userName);
            stmt.setString(2, userEmail);
            stmt.setString(3, userPhone);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Database connection error: " + e.getMessage());
        }
    }

    private void updateUser(String selectedUser, String userName, String userEmail, String userPhone) {
        try (Connection conn = DriverManager.getConnection("jdbc:your_database_url", "username", "password");
             PreparedStatement stmt = conn.prepareStatement("UPDATE users SET user_name = ?, email = ?, phone = ? WHERE user_name = ?")) {

            stmt.setString(1, userName);
            stmt.setString(2, userEmail);
            stmt.setString(3, userPhone);
            stmt.setString(4, selectedUser);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Database connection error: " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
