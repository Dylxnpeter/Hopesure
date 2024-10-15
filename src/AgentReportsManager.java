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

public class AgentReportsManager {
    private Stage primaryStage;

    public AgentReportsManager(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void showAgentReportsPage() {
        Stage stage = new Stage();
        stage.setTitle("Agent Reports");

        VBox vbox = new VBox(15);
        vbox.setPadding(new Insets(10));
        vbox.setAlignment(Pos.CENTER);

        Button ntuReportButton = new Button("NTU Reports");
        Button productExtractButton = new Button("Product Extract");
        Button additionalProductExtractButton = new Button("Additional Product Extract");

        ntuReportButton.setOnAction(e -> showNTUReports());
        productExtractButton.setOnAction(e -> showProductExtract());
        additionalProductExtractButton.setOnAction(e -> showAdditionalProductExtract());

        vbox.getChildren().addAll(ntuReportButton, productExtractButton, additionalProductExtractButton);

        Scene scene = new Scene(vbox, 400, 300);
        stage.setScene(scene);
        stage.show();
    }

    private void showNTUReports() {
        Stage ntuStage = new Stage();
        ntuStage.setTitle("NTU Reports");

        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setPadding(new Insets(10));
        grid.setAlignment(Pos.CENTER);

        Button generateButton = new Button("Generate NTU Report");
        generateButton.setOnAction(e -> {
            generateNTUReport();
            showAlert("Report Generated", "The NTU report has been generated.");
            ntuStage.close();
        });

        grid.add(generateButton, 0, 0);

        Scene scene = new Scene(grid, 400, 200);
        ntuStage.setScene(scene);
        ntuStage.show();
    }

    private void showProductExtract() {
        Stage productExtractStage = new Stage();
        productExtractStage.setTitle("Product Extract");

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

        Button generateButton = new Button("Generate Product Extract Report");
        generateButton.setOnAction(e -> {
            generateProductExtractReport(startDatePicker.getValue(), endDatePicker.getValue(), agentComboBox.getValue());
            showAlert("Report Generated", "The Product Extract report has been generated.");
            productExtractStage.close();
        });

        grid.add(fromDateLabel, 0, 0);
        grid.add(startDatePicker, 1, 0);
        grid.add(toDateLabel, 0, 1);
        grid.add(endDatePicker, 1, 1);
        grid.add(agentComboBox, 0, 2, 2, 1);
        grid.add(generateButton, 0, 3, 2, 1);

        Scene scene = new Scene(grid, 600, 200);
        productExtractStage.setScene(scene);
        productExtractStage.show();
    }

    private void showAdditionalProductExtract() {
        Stage additionalProductExtractStage = new Stage();
        additionalProductExtractStage.setTitle("Additional Product Extract");

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

        Button generateButton = new Button("Generate Additional Product Extract Report");
        generateButton.setOnAction(e -> {
            generateAdditionalProductExtractReport(startDatePicker.getValue(), endDatePicker.getValue(), agentComboBox.getValue());
            showAlert("Report Generated", "The Additional Product Extract report has been generated.");
            additionalProductExtractStage.close();
        });

        grid.add(fromDateLabel, 0, 0);
        grid.add(startDatePicker, 1, 0);
        grid.add(toDateLabel, 0, 1);
        grid.add(endDatePicker, 1, 1);
        grid.add(agentComboBox, 0, 2, 2, 1);
        grid.add(generateButton, 0, 3, 2, 1);

        Scene scene = new Scene(grid, 600, 200);
        additionalProductExtractStage.setScene(scene);
        additionalProductExtractStage.show();
    }

    private void showAlert(String title, String message) {
        // Implement an alert to show messages
        // Example: Alert alert = new Alert(Alert.AlertType.INFORMATION);
        // alert.setTitle(title);
        // alert.setHeaderText(null);
        // alert.setContentText(message);
        // alert.showAndWait();
    }

    private void populateAgents(ComboBox<String> agentComboBox) {
        String url = "jdbc:your_database_url";
        String user = "username";
        String password = "password";

        String query = "SELECT agent_name FROM agents"; // Adjust query as needed

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                agentComboBox.getItems().add(rs.getString("agent_name"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void generateNTUReport() {
        String url = "jdbc:your_database_url";
        String user = "username";
        String password = "password";

        String query = "SELECT * FROM members WHERE policy_status = 'Not Taken Up'";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                // Process results
                // Example: System.out.println(rs.getString("member_name"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void generateProductExtractReport(LocalDate startDate, LocalDate endDate, String agent) {
        String url = "jdbc:your_database_url";
        String user = "username";
        String password = "password";

        String query = "SELECT * FROM products WHERE sale_date BETWEEN ? AND ? AND agent = ?";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setDate(1, java.sql.Date.valueOf(startDate));
            stmt.setDate(2, java.sql.Date.valueOf(endDate));
            stmt.setString(3, agent);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    // Process results
                    // Example: System.out.println(rs.getString("product_name"));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void generateAdditionalProductExtractReport(LocalDate startDate, LocalDate endDate, String agent) {
        String url = "jdbc:your_database_url";
        String user = "username";
        String password = "password";

        String query = "SELECT * FROM additional_products WHERE sale_date BETWEEN ? AND ? AND agent = ?";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setDate(1, java.sql.Date.valueOf(startDate));
            stmt.setDate(2, java.sql.Date.valueOf(endDate));
            stmt.setString(3, agent);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    // Process results
                    // Example: System.out.println(rs.getString("additional_product_name"));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
