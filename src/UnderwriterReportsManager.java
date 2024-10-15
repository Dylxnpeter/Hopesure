import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Button;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;

import java.time.LocalDate;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UnderwriterReportsManager {
    private Stage primaryStage;

    public UnderwriterReportsManager(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void showMonthlyPaymentList() {
        Stage stage = new Stage();
        stage.setTitle("Monthly Payment List");

        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setPadding(new Insets(10));
        grid.setAlignment(Pos.CENTER);

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

        Button generateButton = new Button("Generate Monthly Payment Report");

        generateButton.setOnAction(e -> {
            String selectedMonth = monthComboBox.getValue();
            Integer selectedYear = yearComboBox.getValue();
            if (selectedMonth != null && selectedYear != null) {
                LocalDate month = LocalDate.of(selectedYear, monthComboBox.getItems().indexOf(selectedMonth) + 1, 1);
                generateMonthlyPaymentReport(month);
                showAlert("Report Generated", "The Monthly Payment report has been generated.");
                stage.close();
            } else {
                showAlert("Selection Error", "Please select both month and year.");
            }
        });

        grid.add(monthLabel, 0, 0);
        grid.add(monthComboBox, 1, 0);
        grid.add(yearLabel, 0, 1);
        grid.add(yearComboBox, 1, 1);
        grid.add(generateButton, 0, 2, 2, 1);

        Scene scene = new Scene(grid, 400, 200);
        stage.setScene(scene);
        stage.show();
    }

    private void generateMonthlyPaymentReport(LocalDate month) {
        String url = "jdbc:your_database_url";
        String user = "username";
        String password = "password";

        LocalDate startDate = month.withDayOfMonth(1);
        LocalDate endDate = month.withDayOfMonth(month.lengthOfMonth());

        String query = "SELECT * FROM payments WHERE payment_date BETWEEN ? AND ?";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setDate(1, java.sql.Date.valueOf(startDate));
            stmt.setDate(2, java.sql.Date.valueOf(endDate));

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                // Process result set and generate report
                // Example: System.out.println(rs.getString("column_name"));
            }

        } catch (SQLException e) {
            System.out.println("Database connection error: " + e.getMessage());
        }
    }

    private void showAlert(String title, String message) {
        // Implement an alert to show messages
    }
}
