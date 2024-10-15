import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

public class LoginManager extends Application {

    private static final Map<String, String> BRANCH_CODES = new HashMap<>();

    static {
        BRANCH_CODES.put("KLE", "Klerksdorp");
        BRANCH_CODES.put("MTH", "Mthatha");
        BRANCH_CODES.put("EMP", "Emphangeni");
        BRANCH_CODES.put("TAU", "Taung");
        BRANCH_CODES.put("JBG", "Johannesburg");
        BRANCH_CODES.put("RBG", "Rustenburg");
        BRANCH_CODES.put("ZEE", "Zeerust");
        BRANCH_CODES.put("NEW", "Newcastle");
        BRANCH_CODES.put("HWA", "Hartswater");
        BRANCH_CODES.put("BTB", "Bothshabelo");
        BRANCH_CODES.put("MFK", "Mafikeng");
    }

    private static String selectedBranchCode;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Login Manager");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(10);
        grid.setHgap(10);

        // Role Selection
        grid.add(new Label("Role:"), 0, 0);
        ComboBox<String> roleComboBox = new ComboBox<>();
        roleComboBox.getItems().addAll("Admin", "Agent", "Programmer");
        grid.add(roleComboBox, 1, 0);

        // Username Field (for Agent)
        Label agentIDLabel = new Label("AgentID:");
        TextField agentIDField = new TextField();
        grid.add(agentIDLabel, 0, 1);
        grid.add(agentIDField, 1, 1);

        // Password Field
        grid.add(new Label("Password:"), 0, 2);
        PasswordField passwordField = new PasswordField();
        grid.add(passwordField, 1, 2);

        // Branch Dropdown (for Agent)
        Label branchLabel = new Label("Branch:");
        ComboBox<String> branchComboBox = new ComboBox<>();
        branchComboBox.getItems().addAll(
                "Klerksdorp", "Mthatha", "Taung", "Rustenburg", "Newcastle",
                "Bothshabelo", "Emphangeni", "Johannesburg", "Zeerust", "Hartswater"
        );
        grid.add(branchLabel, 0, 3);
        grid.add(branchComboBox, 1, 3);

        // Login Button
        Button loginButton = new Button("Login");
        grid.add(loginButton, 1, 4);

        // Initially hide Agent-specific fields
        agentIDLabel.setVisible(false);
        agentIDField.setVisible(false);
        branchLabel.setVisible(false);
        branchComboBox.setVisible(false);

        // Role selection event
        roleComboBox.setOnAction(e -> {
            String selectedRole = roleComboBox.getValue();
            if ("Agent".equals(selectedRole)) {
                agentIDLabel.setVisible(true);
                agentIDField.setVisible(true);
                branchLabel.setVisible(true);
                branchComboBox.setVisible(true);
            } else {
                agentIDLabel.setVisible(false);
                agentIDField.setVisible(false);
                branchLabel.setVisible(false);
                branchComboBox.setVisible(false);
            }
        });

        // Automatically fill branch based on AgentID
        agentIDField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() >= 3) {
                String prefix = newValue.substring(0, 3).toUpperCase();
                String branchName = BRANCH_CODES.get(prefix);
                if (branchName != null) {
                    branchComboBox.setValue(branchName);
                }
            }
        });

        // Login button action
        loginButton.setOnAction(e -> handleLogin(roleComboBox.getValue(), agentIDField.getText(), passwordField.getText(), branchComboBox.getValue(), primaryStage));

        Scene scene = new Scene(grid, 350, 250);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void handleLogin(String role, String agentID, String password, String branch, Stage loginStage) {
        boolean isAuthenticated = false;
        if ("Admin".equals(role) || "Programmer".equals(role)) {
            isAuthenticated = authenticateUser(role, password);
        } else if ("Agent".equals(role)) {
            if (agentID != null && !agentID.isEmpty()) {
                isAuthenticated = authenticateUser(agentID, password);
                if (isAuthenticated) {
                    selectedBranchCode = BRANCH_CODES.get(agentID.substring(0, 3).toUpperCase());
                }
            }
        }

        if (isAuthenticated) {
            System.out.println(role + " logged in.");
            showHomePage(loginStage);
        } else {
            showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid credentials.");
        }
    }

    private boolean authenticateUser(String username, String password) {
        boolean isAuthenticated = false;
        String query = "SELECT * FROM userlogins WHERE username = ? AND password = ?";

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/yourdatabase", "username", "password");
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, username);
            stmt.setString(2, password);

            try (ResultSet rs = stmt.executeQuery()) {
                isAuthenticated = rs.next(); // If result set is not empty, authentication is successful
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return isAuthenticated;
    }

    private void showHomePage(Stage loginStage) {
        try {
            HomePage homePage = new HomePage();
            homePage.start(new Stage());  // Start HomePage in a new stage
            loginStage.close();  // Close the login stage
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load home page.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
