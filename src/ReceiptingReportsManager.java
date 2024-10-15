import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.sql.*;
import java.time.LocalDate;

public class ReceiptingReportsManager {
    private Stage primaryStage;

    public ReceiptingReportsManager(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void showReceiptingReportsPage() {
        Stage receiptingReportsStage = new Stage();
        receiptingReportsStage.setTitle("Receipting Reports");

        GridPane grid = new GridPane();

        Button paymentListButton = new Button("Payment List");
        paymentListButton.setOnAction(e -> showPaymentListOptions());

        Button reprintReportButton = new Button("Reprint Report");
        reprintReportButton.setOnAction(e -> showReprintReportOptions());

        grid.add(paymentListButton, 0, 0);
        grid.add(reprintReportButton, 1, 0);

        Scene scene = new Scene(grid, 400, 200);
        receiptingReportsStage.setScene(scene);
        receiptingReportsStage.show();
    }

    private void showPaymentListOptions() {
        Stage optionsStage = new Stage();
        optionsStage.setTitle("Payment List Report Options");

        GridPane grid = new GridPane();

        Label fromDateLabel = new Label("From Date:");
        DatePicker startDatePicker = new DatePicker();
        Label toDateLabel = new Label("To Date:");
        DatePicker endDatePicker = new DatePicker();

        grid.add(fromDateLabel, 0, 0);
        grid.add(startDatePicker, 1, 0);
        grid.add(toDateLabel, 0, 1);
        grid.add(endDatePicker, 1, 1);

        Button generateButton = new Button("Generate");
        generateButton.setOnAction(e -> {
            LocalDate fromDate = startDatePicker.getValue();
            LocalDate toDate = endDatePicker.getValue();
            generatePaymentListReport(fromDate, toDate);
            showAlert(Alert.AlertType.INFORMATION, "Report Generated", "The Payment List report has been generated.");
            optionsStage.close();
        });

        grid.add(generateButton, 1, 2);

        Scene scene = new Scene(grid, 400, 250);
        optionsStage.setScene(scene);
        optionsStage.show();
    }

    private void generatePaymentListReport(LocalDate fromDate, LocalDate toDate) {
        // Implement logic to generate the payment list report based on date range
        try (Connection conn = DriverManager.getConnection("jdbc:your_database_url", "username", "password");
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM receipts WHERE payment_date BETWEEN ? AND ?")) {

            stmt.setDate(1, Date.valueOf(fromDate));
            stmt.setDate(2, Date.valueOf(toDate));

            ResultSet rs = stmt.executeQuery();

            // Process the result set and generate the report

        } catch (SQLException e) {
            System.out.println("Database connection error: " + e.getMessage());
        }
    }

    private void showReprintReportOptions() {
        Stage optionsStage = new Stage();
        optionsStage.setTitle("Reprint Report Options");

        GridPane grid = new GridPane();

        Label fromDateLabel = new Label("From Date:");
        DatePicker startDatePicker = new DatePicker();
        Label toDateLabel = new Label("To Date:");
        DatePicker endDatePicker = new DatePicker();

        grid.add(fromDateLabel, 0, 0);
        grid.add(startDatePicker, 1, 0);
        grid.add(toDateLabel, 0, 1);
        grid.add(endDatePicker, 1, 1);

        Button generateButton = new Button("Generate");
        generateButton.setOnAction(e -> {
            LocalDate fromDate = startDatePicker.getValue();
            LocalDate toDate = endDatePicker.getValue();
            generateReprintReport(fromDate, toDate);
            showAlert(Alert.AlertType.INFORMATION, "Report Generated", "The Reprint report has been generated.");
            optionsStage.close();
        });

        grid.add(generateButton, 1, 2);

        Scene scene = new Scene(grid, 400, 250);
        optionsStage.setScene(scene);
        optionsStage.show();
    }

    private void generateReprintReport(LocalDate fromDate, LocalDate toDate) {
        // Implement logic to generate the reprint report based on date range
        try (Connection conn = DriverManager.getConnection("jdbc:your_database_url", "username", "password");
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM receipts WHERE reprint_date BETWEEN ? AND ?")) {

            stmt.setDate(1, Date.valueOf(fromDate));
            stmt.setDate(2, Date.valueOf(toDate));

            ResultSet rs = stmt.executeQuery();

            // Process the result set and generate the report

        } catch (SQLException e) {
            System.out.println("Database connection error: " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
