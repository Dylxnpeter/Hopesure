import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;

import java.time.LocalDate;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TransactionalReportsManager {
    private Stage primaryStage;

    public TransactionalReportsManager(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void showTransactionalReportsPage() {
        Stage stage = new Stage();
        stage.setTitle("Generate Transactional Report");

        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setPadding(new Insets(10));
        grid.setAlignment(Pos.CENTER);

        Label fromDateLabel = new Label("From Date:");
        DatePicker startDatePicker = new DatePicker();
        Label toDateLabel = new Label("To Date:");
        DatePicker endDatePicker = new DatePicker();
        ComboBox<String> agentComboBox = new ComboBox<>();
        agentComboBox.setPromptText("Select Agent(s)");
        populateAgents(agentComboBox);

        Button generateButton = new Button("Generate Transactional Report");
        generateButton.setOnAction(e -> {
            LocalDate startDate = startDatePicker.getValue();
            LocalDate endDate = endDatePicker.getValue();
            String agent = agentComboBox.getValue();
            if (startDate != null && endDate != null) {
                generateTransactionalReport(startDate, endDate, agent);
                showAlert("Report Generated", "The Transactional Report has been generated.");
            } else {
                showAlert("Input Error", "Please select both dates.");
            }
        });

        grid.add(fromDateLabel, 0, 0);
        grid.add(startDatePicker, 1, 0);
        grid.add(toDateLabel, 0, 1);
        grid.add(endDatePicker, 1, 1);
        grid.add(agentComboBox, 0, 2, 2, 1);
        grid.add(generateButton, 0, 3, 2, 1);

        Scene scene = new Scene(grid, 600, 200);
        stage.setScene(scene);
        stage.show();
    }

    private void showAlert(String title, String message) {
        // Implement an alert to show messages
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

    private void generateTransactionalReport(LocalDate startDate, LocalDate endDate, String agent) {
        String url = "jdbc:your_database_url";
        String user = "username";
        String password = "password";

        String query = "SELECT * FROM transactions WHERE transaction_date BETWEEN ? AND ?" +
                (agent != null && !agent.equals("All Agents") ? " AND agent = ?" : "");

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setDate(1, java.sql.Date.valueOf(startDate));
            stmt.setDate(2, java.sql.Date.valueOf(endDate));
            if (agent != null && !agent.equals("All Agents")) {
                stmt.setString(3, agent);
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    // Process results
                    // Example: System.out.println(rs.getString("transaction_id"));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
