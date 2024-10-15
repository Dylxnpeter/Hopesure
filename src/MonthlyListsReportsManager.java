import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Button;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class MonthlyListsReportsManager {
    private Stage primaryStage;

    public MonthlyListsReportsManager(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void showMonthlyListsReportsPage() {
        Stage stage = new Stage();
        stage.setTitle("Generate Monthly Lists Report");

        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setPadding(new Insets(10));
        grid.setAlignment(Pos.CENTER);

        // Month Selection
        Label monthLabel = new Label("Select Month:");
        ComboBox<String> monthComboBox = new ComboBox<>();
        monthComboBox.getItems().addAll(
                "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"
        );

        // Year Selection
        Label yearLabel = new Label("Select Year:");
        ComboBox<Integer> yearComboBox = new ComboBox<>();
        for (int year = LocalDate.now().getYear(); year >= 2000; year--) {
            yearComboBox.getItems().add(year);
        }

        // Report Type Selection
        Label reportTypeLabel = new Label("Select Report Type:");
        ComboBox<String> reportTypeComboBox = new ComboBox<>();
        reportTypeComboBox.getItems().addAll(
                "Transactions", "Payments", "SMS Reminders"
        );

        // Agent Selection
        Label agentLabel = new Label("Select Agent(s):");
        ComboBox<String> agentComboBox = new ComboBox<>();
        agentComboBox.setPromptText("Select Agent(s)");
        populateAgents(agentComboBox);

        // Generate Button
        Button generateButton = new Button("Generate Monthly Report");
        generateButton.setOnAction(e -> {
            String selectedMonth = monthComboBox.getValue();
            Integer selectedYear = yearComboBox.getValue();
            String reportType = reportTypeComboBox.getValue();
            String agent = agentComboBox.getValue();

            if (selectedMonth != null && selectedYear != null && reportType != null) {
                boolean success = generateMonthlyListsReport(selectedMonth, selectedYear, reportType, agent);
                if (success) {
                    showAlert("Report Generated", "The Monthly " + reportType + " Report has been generated.");
                } else {
                    showAlert("Error Occurred", "There was an error generating the report. Please try again.");
                }
                stage.close(); // Close the window regardless of success
            } else {
                showAlert("Input Error", "Please select a valid month, year, and report type.");
            }
        });

        // Add components to grid
        grid.add(monthLabel, 0, 0);
        grid.add(monthComboBox, 1, 0);
        grid.add(yearLabel, 0, 1);
        grid.add(yearComboBox, 1, 1);
        grid.add(reportTypeLabel, 0, 2);
        grid.add(reportTypeComboBox, 1, 2);
        grid.add(agentLabel, 0, 3);
        grid.add(agentComboBox, 1, 3);
        grid.add(generateButton, 0, 4, 2, 1);

        // Set the scene and show the stage
        Scene scene = new Scene(grid, 600, 300);
        stage.setScene(scene);
        stage.show();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void populateAgents(ComboBox<String> agentComboBox) {
        String url = "jdbc:your_database_url";
        String user = "username";
        String password = "password";

        String query = "SELECT agent_name FROM agents"; // Adjust query as needed

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            agentComboBox.getItems().add("All Agents"); // Option to select all agents
            while (rs.next()) {
                agentComboBox.getItems().add(rs.getString("agent_name"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean generateMonthlyListsReport(String month, int year, String reportType, String agent) {
        String url = "jdbc:your_database_url";
        String user = "username";
        String password = "password";

        String query = buildQuery(month, year, reportType, agent);

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            if (agent != null && !agent.equals("All Agents")) {
                stmt.setString(1, agent);
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    // Process results and summarize data for the monthly report
                    // Example: System.out.println(rs.getString("transaction_id"));
                }
                return true; // If no exception occurs, return true indicating success
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Return false indicating failure
        }
    }

    private String buildQuery(String month, int year, String reportType, String agent) {
        String table = "";
        switch (reportType) {
            case "Transactions":
                table = "transactions";
                break;
            case "Payments":
                table = "payments";
                break;
            case "SMS Reminders":
                table = "sms_reminders";
                break;
        }

        String query = "SELECT * FROM " + table + " WHERE YEAR(transaction_date) = " + year +
                " AND MONTHNAME(transaction_date) = '" + month + "'";

        if (agent != null && !agent.equals("All Agents")) {
            query += " AND agent = ?";
        }

        return query;
    }
}
