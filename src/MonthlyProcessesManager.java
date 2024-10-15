import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class MonthlyProcessesManager {
    private Stage primaryStage;

    public MonthlyProcessesManager(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void showMonthlyProcessesPage() {
        Stage stage = new Stage();
        stage.setTitle("Monthly Processes - Billing");

        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setPadding(new Insets(10));
        grid.setAlignment(Pos.CENTER);

        // Billing Type Selection
        Label billingTypeLabel = new Label("Select Billing Type:");
        ComboBox<String> billingTypeComboBox = new ComboBox<>();
        billingTypeComboBox.getItems().addAll("General Billing", "Individual Billing");

        // Proceed Button
        Button proceedButton = new Button("Proceed");
        proceedButton.setOnAction(e -> {
            String billingType = billingTypeComboBox.getValue();

            if (billingType != null) {
                stage.close();
                if (billingType.equals("General Billing")) {
                    showGeneralBillingPage();
                } else if (billingType.equals("Individual Billing")) {
                    showIndividualBillingPage();
                }
            } else {
                showAlert("Selection Error", "Please select a billing type.");
            }
        });

        // Add components to grid
        grid.add(billingTypeLabel, 0, 0);
        grid.add(billingTypeComboBox, 1, 0);
        grid.add(proceedButton, 0, 1, 2, 1);

        // Set the scene and show the stage
        Scene scene = new Scene(grid, 400, 200);
        stage.setScene(scene);
        stage.show();
    }

    private void showGeneralBillingPage() {
        Stage stage = new Stage();
        stage.setTitle("General Billing");

        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setPadding(new Insets(10));
        grid.setAlignment(Pos.CENTER);

        // Policy Selection
        Label policyLabel = new Label("Select Policy:");
        ComboBox<String> policyComboBox = new ComboBox<>();
        populatePolicies(policyComboBox);

        // Month and Year Selection
        Label monthLabel = new Label("Select Month:");
        ComboBox<String> monthComboBox = new ComboBox<>();
        monthComboBox.getItems().addAll(
                "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"
        );

        Label yearLabel = new Label("Select Year:");
        ComboBox<Integer> yearComboBox = new ComboBox<>();
        for (int year = LocalDate.now().getYear(); year >= 2000; year--) {
            yearComboBox.getItems().add(year);
        }

        // Generate Bill Button
        Button generateBillButton = new Button("Generate General Bill");
        generateBillButton.setOnAction(e -> {
            String selectedPolicy = policyComboBox.getValue();
            String selectedMonth = monthComboBox.getValue();
            Integer selectedYear = yearComboBox.getValue();

            if (selectedPolicy != null && selectedMonth != null && selectedYear != null) {
                handleGeneralBilling(selectedPolicy, selectedMonth, selectedYear);
                showAlert("Billing Successful", "General bill has been successfully generated.");
                stage.close();
            } else {
                showAlert("Input Error", "Please select a valid policy, month, and year.");
            }
        });

        // Add components to grid
        grid.add(policyLabel, 0, 0);
        grid.add(policyComboBox, 1, 0);
        grid.add(monthLabel, 0, 1);
        grid.add(monthComboBox, 1, 1);
        grid.add(yearLabel, 0, 2);
        grid.add(yearComboBox, 1, 2);
        grid.add(generateBillButton, 0, 3, 2, 1);

        // Set the scene and show the stage
        Scene scene = new Scene(grid, 400, 300);
        stage.setScene(scene);
        stage.show();
    }

    private void showIndividualBillingPage() {
        Stage stage = new Stage();
        stage.setTitle("Individual Billing");

        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setPadding(new Insets(10));
        grid.setAlignment(Pos.CENTER);

        // Member ID Selection
        Label memberIdLabel = new Label("Enter Member ID:");
        ComboBox<String> memberIdComboBox = new ComboBox<>();
        populateMemberIds(memberIdComboBox);

        // Enable user to type and search
        memberIdComboBox.setEditable(true);
        TextField editor = memberIdComboBox.getEditor();
        ObservableList<String> items = memberIdComboBox.getItems();

        editor.textProperty().addListener((obs, oldText, newText) -> {
            if (!newText.equals(memberIdComboBox.getValue())) {
                memberIdComboBox.hide();
                ObservableList<String> filteredItems = FXCollections.observableArrayList();
                for (String item : items) {
                    if (item.toLowerCase().startsWith(newText.toLowerCase())) {
                        filteredItems.add(item);
                    }
                }
                memberIdComboBox.setItems(filteredItems);
                memberIdComboBox.show();
            }
        });

        // Month and Year Selection
        Label monthLabel = new Label("Select Month:");
        ComboBox<String> monthComboBox = new ComboBox<>();
        monthComboBox.getItems().addAll(
                "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"
        );

        Label yearLabel = new Label("Select Year:");
        ComboBox<Integer> yearComboBox = new ComboBox<>();
        for (int year = LocalDate.now().getYear(); year >= 2000; year--) {
            yearComboBox.getItems().add(year);
        }

        // Generate Bill Button
        Button generateBillButton = new Button("Generate Individual Bill");
        generateBillButton.setOnAction(e -> {
            String memberId = memberIdComboBox.getValue();
            String selectedMonth = monthComboBox.getValue();
            Integer selectedYear = yearComboBox.getValue();

            if (memberId != null && selectedMonth != null && selectedYear != null) {
                handleIndividualBilling(memberId, selectedMonth, selectedYear);
                showAlert("Billing Successful", "Individual bill has been successfully generated.");
                stage.close();
            } else {
                showAlert("Input Error", "Please select a valid member ID, month, and year.");
            }
        });

        // Add components to grid
        grid.add(memberIdLabel, 0, 0);
        grid.add(memberIdComboBox, 1, 0);
        grid.add(monthLabel, 0, 1);
        grid.add(monthComboBox, 1, 1);
        grid.add(yearLabel, 0, 2);
        grid.add(yearComboBox, 1, 2);
        grid.add(generateBillButton, 0, 3, 2, 1);

        // Set the scene and show the stage
        Scene scene = new Scene(grid, 400, 300);
        stage.setScene(scene);
        stage.show();
    }

    private void populateMemberIds(ComboBox<String> memberIdComboBox) {
        String url = "jdbc:your_database_url";
        String user = "username";
        String password = "password";

        String query = "SELECT member_id FROM members"; // Adjust query as needed

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                memberIdComboBox.getItems().add(rs.getString("member_id"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void populatePolicies(ComboBox<String> policyComboBox) {
        String url = "jdbc:your_database_url";
        String user = "username";
        String password = "password";

        String query = "SELECT policy_name FROM policies"; // Adjust query as needed

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                policyComboBox.getItems().add(rs.getString("policy_name"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void handleGeneralBilling(String policy, String month, int year) {
        // Logic to generate a general bill for all members under the selected policy and month/year
        // Example: System.out.println("Generating general bill for policy: " + policy + " for " + month + " " + year);
    }

    private void handleIndividualBilling(String memberId, String month, int year) {
        // Logic to generate a bill for the selected member and month/year
        // Example: System.out.println("Generating individual bill for member ID: " + memberId + " for " + month + " " + year);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
