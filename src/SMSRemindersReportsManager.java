import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Button;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.time.LocalDate;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SMSRemindersReportsManager {
    private Stage primaryStage;

    public SMSRemindersReportsManager(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void showSMSRemindersReportsPage() {
        Stage smsStage = new Stage();
        smsStage.setTitle("SMS Reminders Reports");

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

        Button generateButton = new Button("Generate SMS Reminders Report");
        generateButton.setOnAction(e -> {
            generateSMSRemindersReport(startDatePicker.getValue(), endDatePicker.getValue(), agentComboBox.getValue());
            showAlert("Report Generated", "The SMS Reminders report has been generated.");
            smsStage.close();
        });

        grid.add(fromDateLabel, 0, 0);
        grid.add(startDatePicker, 1, 0);
        grid.add(toDateLabel, 0, 1);
        grid.add(endDatePicker, 1, 1);
        grid.add(agentComboBox, 0, 2, 2, 1);
        grid.add(generateButton, 0, 3, 2, 1);

        Scene scene = new Scene(grid, 600, 300);
        smsStage.setScene(scene);
        smsStage.show();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void populateAgents(ComboBox<String> agentComboBox) {
        // Example database connection and fetching agents
        try (Connection conn = DriverManager.getConnection("jdbc:your_database_url", "username", "password");
             PreparedStatement stmt = conn.prepareStatement("SELECT agent_name FROM agents");
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                agentComboBox.getItems().add(rs.getString("agent_name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load agents.");
        }
    }

    private void generateSMSRemindersReport(LocalDate startDate, LocalDate endDate, String agent) {
        String query = "SELECT * FROM sms_reminders WHERE reminder_date BETWEEN ? AND ?" +
                (agent != null && !agent.isEmpty() ? " AND agent = ?" : "");

        try (Connection conn = DriverManager.getConnection("jdbc:your_database_url", "username", "password");
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setDate(1, java.sql.Date.valueOf(startDate));
            stmt.setDate(2, java.sql.Date.valueOf(endDate));
            if (agent != null && !agent.isEmpty()) {
                stmt.setString(3, agent);
            }

            ResultSet rs = stmt.executeQuery();
            // Process the results to generate the report
            while (rs.next()) {
                // Example: Printing data to console (replace with actual report generation logic)
                System.out.println("Reminder ID: " + rs.getInt("reminder_id") +
                        ", Agent: " + rs.getString("agent") +
                        ", Date: " + rs.getDate("reminder_date"));
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "An error occurred while generating the report.");
        }
    }
}
