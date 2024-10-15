import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.*;
import java.time.LocalDate;
import java.util.Date;
import java.util.Scanner;

public class OtherFeaturesManager {

    public void showOtherFeatures() {
        Stage stage = new Stage();
        stage.setTitle("Other Features");

        VBox vbox = new VBox(15);
        vbox.setPadding(new Insets(20));
        vbox.setAlignment(Pos.CENTER);

        Label titleLabel = new Label("Select a Feature:");
        titleLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        ComboBox<String> featureComboBox = new ComboBox<>();
        featureComboBox.getItems().addAll(
                "Specific Search Queries",
                "User Access Level",
                "Work Performance",
                "Receipting",
                "POS Device Management",
                "Chatbot"
        );

        Button selectButton = new Button("Select");
        selectButton.setOnAction(e -> {
            String selectedFeature = featureComboBox.getValue();
            if (selectedFeature != null) {
                switch (selectedFeature) {
                    case "Specific Search Queries":
                        showSpecificSearchQueries();
                        break;
                    case "User Access Level":
                        showUserAccessLevel();
                        break;
                    case "Work Performance":
                        showWorkPerformance();
                        break;
                    case "Receipting":
                        showReceipting();
                        break;
                    case "POS Device Management":
                        showPOSDeviceManagement();
                        break;
                    case "Chatbot":
                        showChatbot();
                        break;
                    default:
                        showAlert("Selection Error", "Please select a valid feature.");
                }
                stage.close();
            } else {
                showAlert("Selection Error", "Please select a feature.");
            }
        });

        vbox.getChildren().addAll(titleLabel, featureComboBox, selectButton);

        Scene scene = new Scene(vbox, 300, 250);
        stage.setScene(scene);
        stage.show();
    }

    private void showSpecificSearchQueries() {
        Stage stage = new Stage();
        stage.setTitle("Specific Search Queries");

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));
        vbox.setAlignment(Pos.CENTER);

        Label selectQueryLabel = new Label("Select the type of search:");

        ComboBox<String> searchTypeComboBox = new ComboBox<>();
        searchTypeComboBox.getItems().addAll(
                "Beneficiaries Search",
                "Coverage Area",
                "Payment Status",
                "Policy Expiry Date"
        );

        Button continueButton = new Button("Continue");
        continueButton.setOnAction(e -> {
            String selectedSearch = searchTypeComboBox.getValue();
            if (selectedSearch != null) {
                switch (selectedSearch) {
                    case "Beneficiaries Search":
                        showBeneficiariesSearchForm();
                        break;
                    case "Coverage Area":
                        showCoverageAreaSearchForm();
                        break;
                    case "Payment Status":
                        showPaymentStatusSearchForm();
                        break;
                    case "Policy Expiry Date":
                        showPolicyExpiryDateSearchForm();
                        break;
                    default:
                        showAlert("Error", "Please select a valid search type.");
                }
                stage.close();
            } else {
                showAlert("Selection Error", "Please select a search type.");
            }
        });

        vbox.getChildren().addAll(selectQueryLabel, searchTypeComboBox, continueButton);

        Scene scene = new Scene(vbox, 300, 200);
        stage.setScene(scene);
        stage.show();
    }
    private void showBeneficiariesSearchForm() {
        Stage stage = new Stage();
        stage.setTitle("Beneficiaries Search");

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));
        vbox.setAlignment(Pos.CENTER);

        Label searchOptionLabel = new Label("Search by:");
        ComboBox<String> searchOptionComboBox = new ComboBox<>();
        searchOptionComboBox.getItems().addAll("Member ID", "Beneficiary Name");

        TextField searchField = new TextField();
        searchField.setPromptText("Please select an option first");
        searchField.setDisable(true); // Initially disable the search field

        searchOptionComboBox.setOnAction(e -> {
            String selectedOption = searchOptionComboBox.getValue();
            if (selectedOption != null) {
                searchField.setDisable(false);
                searchField.setPromptText("Enter " + selectedOption); // Update the prompt based on the selection
            }
        });

        Button searchButton = new Button("Search");
        searchButton.setDisable(true); // Initially disable the search button

        // Enable the search button only if the search field is not empty
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            searchButton.setDisable(newValue.trim().isEmpty());
        });

        searchButton.setOnAction(e -> {
            String selectedOption = searchOptionComboBox.getValue();
            String searchValue = searchField.getText().trim();

            if (selectedOption != null && !searchValue.isEmpty()) {
                searchBeneficiaries(selectedOption, searchValue);
                stage.close();
            } else {
                showAlert("Input Error", "Please select a search option and enter a value.");
            }
        });

        vbox.getChildren().addAll(searchOptionLabel, searchOptionComboBox, searchField, searchButton);

        Scene scene = new Scene(vbox, 300, 250);
        stage.setScene(scene);
        stage.show();
    }

    private void searchBeneficiaries(String searchOption, String searchValue) {
        String url = "jdbc:your_database_url";
        String user = "username";
        String password = "password";

        String query;

        if (searchOption.equals("Member ID")) {
            query = "SELECT * FROM beneficiaries WHERE member_id = ?";
        } else { // Beneficiary Name
            query = "SELECT * FROM beneficiaries WHERE beneficiary_name LIKE ?";
            searchValue = "%" + searchValue + "%"; // Use LIKE for partial matching
        }

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, searchValue);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                // Process result set and display or use the beneficiaries data
                String beneficiaryName = rs.getString("beneficiary_name");
                String memberId = rs.getString("member_id");
                // Display the result or process as required
                System.out.println("Beneficiary: " + beneficiaryName + ", Member ID: " + memberId);
            }

        } catch (SQLException e) {
            System.out.println("Database connection error: " + e.getMessage());
        }
    }


    private void showCoverageAreaSearchForm() {
        Stage stage = new Stage();
        stage.setTitle("Coverage Area Search");

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));
        vbox.setAlignment(Pos.CENTER);

        Label searchOptionLabel = new Label("Search by:");
        ComboBox<String> searchOptionComboBox = new ComboBox<>();
        searchOptionComboBox.getItems().addAll("Province", "City/Town");

        TextField searchField = new TextField();
        searchField.setPromptText("Please select an option first");
        searchField.setDisable(true); // Initially disable the search field

        searchOptionComboBox.setOnAction(e -> {
            String selectedOption = searchOptionComboBox.getValue();
            if (selectedOption != null) {
                searchField.setDisable(false);
                searchField.setPromptText("Enter " + selectedOption); // Update the prompt based on the selection
            }
        });

        Button searchButton = new Button("Search");
        searchButton.setDisable(true); // Initially disable the search button

        // Enable the search button only if the search field is not empty
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            searchButton.setDisable(newValue.trim().isEmpty());
        });

        searchButton.setOnAction(e -> {
            String selectedOption = searchOptionComboBox.getValue();
            String searchValue = searchField.getText().trim();

            if (selectedOption != null && !searchValue.isEmpty()) {
                searchCoverageArea(selectedOption, searchValue);
                stage.close();
            } else {
                showAlert("Input Error", "Please select a search option and enter a value.");
            }
        });

        vbox.getChildren().addAll(searchOptionLabel, searchOptionComboBox, searchField, searchButton);

        Scene scene = new Scene(vbox, 300, 250);
        stage.setScene(scene);
        stage.show();
    }

    private void searchCoverageArea(String searchOption, String searchValue) {
        String url = "jdbc:your_database_url";
        String user = "username";
        String password = "password";

        String query;

        if (searchOption.equals("Province")) {
            query = "SELECT * FROM coverage_areas WHERE province = ?";
        } else { // City/Town
            query = "SELECT * FROM coverage_areas WHERE city_town = ?";
        }

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, searchValue);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                // Process result set and display or use the coverage area data
                String province = rs.getString("province");
                String cityTown = rs.getString("city_town");
                // Display the result or process as required
                System.out.println("Province: " + province + ", City/Town: " + cityTown);
            }

        } catch (SQLException e) {
            System.out.println("Database connection error: " + e.getMessage());
        }
    }

    private void showPaymentStatusSearchForm() {
        Stage stage = new Stage();
        stage.setTitle("Payment Status Search");

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));
        vbox.setAlignment(Pos.CENTER);

        Label statusLabel = new Label("Status:");
        ComboBox<String> statusComboBox = new ComboBox<>();
        statusComboBox.getItems().addAll("Active", "Lapsed", "Reinstated", "Not Taken Up");

        Label fromDateLabel = new Label("From Date:");
        DatePicker fromDatePicker = new DatePicker();

        Label toDateLabel = new Label("To Date:");
        DatePicker toDatePicker = new DatePicker();

        Button searchButton = new Button("Search");
        searchButton.setOnAction(e -> {
            String status = statusComboBox.getValue();
            LocalDate fromDate = fromDatePicker.getValue();
            LocalDate toDate = toDatePicker.getValue();
            if (status != null && fromDate != null && toDate != null) {
                searchPaymentStatus(status, java.sql.Date.valueOf(fromDate), java.sql.Date.valueOf(toDate));
                stage.close();
            } else {
                showAlert("Input Error", "Please select a status and both dates.");
            }
        });

        vbox.getChildren().addAll(statusLabel, statusComboBox, fromDateLabel, fromDatePicker, toDateLabel, toDatePicker, searchButton);

        Scene scene = new Scene(vbox, 300, 300);
        stage.setScene(scene);
        stage.show();
    }

    private void searchPaymentStatus(String status, Date fromDate, Date toDate) {
        String url = "jdbc:your_database_url";
        String user = "username";
        String password = "password";

        String query = "SELECT * FROM payments WHERE status = ? AND payment_date BETWEEN ? AND ?";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, status);
            stmt.setDate(2, (java.sql.Date) fromDate);
            stmt.setDate(3, (java.sql.Date) toDate);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                // Process result set and display or use the payment status data
                String paymentId = rs.getString("payment_id");
                String memberId = rs.getString("member_id");
                Date paymentDate = rs.getDate("payment_date");
                // Display the result or process as required
                System.out.println("Payment ID: " + paymentId + ", Member ID: " + memberId + ", Payment Date: " + paymentDate);
            }

        } catch (SQLException e) {
            System.out.println("Database connection error: " + e.getMessage());
        }
    }

    private void showPolicyExpiryDateSearchForm() {
        Stage stage = new Stage();
        stage.setTitle("Policy Expiry Date Search");

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));
        vbox.setAlignment(Pos.CENTER);

        Label expiryStartDateLabel = new Label("Expiry Start Date:");
        DatePicker expiryStartDatePicker = new DatePicker();

        Label expiryEndDateLabel = new Label("Expiry End Date:");
        DatePicker expiryEndDatePicker = new DatePicker();

        Button searchButton = new Button("Search");
        searchButton.setOnAction(e -> {
            LocalDate startDate = expiryStartDatePicker.getValue();
            LocalDate endDate = expiryEndDatePicker.getValue();
            if (startDate != null && endDate != null) {
                searchPolicyExpiry(java.sql.Date.valueOf(startDate), java.sql.Date.valueOf(endDate));
                stage.close();
            } else {
                showAlert("Input Error", "Please select both start and end dates.");
            }
        });

        vbox.getChildren().addAll(expiryStartDateLabel, expiryStartDatePicker, expiryEndDateLabel, expiryEndDatePicker, searchButton);

        Scene scene = new Scene(vbox, 300, 250);
        stage.setScene(scene);
        stage.show();
    }

    private void searchPolicyExpiry(Date expiryWindowStart, Date expiryWindowEnd) {
        String url = "jdbc:your_database_url";
        String user = "username";
        String password = "password";

        String query = "SELECT * FROM policies WHERE expiry_date BETWEEN ? AND ?";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setDate(1, (java.sql.Date) expiryWindowStart);
            stmt.setDate(2, (java.sql.Date) expiryWindowEnd);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                // Process the result set to retrieve policy details
                String policyId = rs.getString("policy_id");
                String memberId = rs.getString("member_id");
                Date expiryDate = rs.getDate("expiry_date");
                // Display or process the result as needed
                System.out.println("Policy ID: " + policyId + ", Member ID: " + memberId + ", Expiry Date: " + expiryDate);
            }

        } catch (SQLException e) {
            System.out.println("Database connection error: " + e.getMessage());
        }
    }


    private void showUserAccessLevel() {
        Stage stage = new Stage();
        stage.setTitle("User Access Level Management");

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));
        vbox.setAlignment(Pos.CENTER);

        // Agent Selection
        Label agentLabel = new Label("Select Agent:");
        ComboBox<String> agentComboBox = new ComboBox<>();
        loadAgentsIntoComboBox(agentComboBox); // Load agents from the database

        // Job Title Selection
        Label jobTitleLabel = new Label("Job Title:");
        ComboBox<String> jobTitleComboBox = new ComboBox<>();
        jobTitleComboBox.getItems().addAll("Manager", "Supervisor", "Agent", "Administrator"); // Example titles

        // Access Level Input
        Label accessLevelLabel = new Label("Access Level (0-99):");
        TextField accessLevelField = new TextField();
        accessLevelField.setPromptText("Enter access level");

        // Buttons for actions
        Button assignButton = new Button("Assign Access Level");
        Button modifyButton = new Button("Modify Access Level");

        assignButton.setOnAction(e -> {
            String selectedAgent = agentComboBox.getValue();
            String jobTitle = jobTitleComboBox.getValue();
            String accessLevelStr = accessLevelField.getText().trim();

            if (selectedAgent != null && jobTitle != null && isNumeric(accessLevelStr)) {
                int accessLevel = Integer.parseInt(accessLevelStr);
                if (accessLevel >= 0 && accessLevel <= 99) {
                    assignAccessLevelToAgent(selectedAgent, accessLevel, jobTitle);
                } else {
                    showAlert("Input Error", "Access level must be between 0 and 99.");
                }
            } else {
                showAlert("Input Error", "Please select an agent, job title, and enter a valid access level.");
            }
        });

        modifyButton.setOnAction(e -> {
            String selectedAgent = agentComboBox.getValue();
            String jobTitle = jobTitleComboBox.getValue();
            String accessLevelStr = accessLevelField.getText().trim();

            if (selectedAgent != null && jobTitle != null && isNumeric(accessLevelStr)) {
                int accessLevel = Integer.parseInt(accessLevelStr);
                if (accessLevel >= 0 && accessLevel <= 99) {
                    modifyAccessLevelForAgent(selectedAgent, accessLevel, jobTitle);
                } else {
                    showAlert("Input Error", "Access level must be between 0 and 99.");
                }
            } else {
                showAlert("Input Error", "Please select an agent, job title, and enter a valid access level.");
            }
        });

        vbox.getChildren().addAll(agentLabel, agentComboBox, jobTitleLabel, jobTitleComboBox, accessLevelLabel, accessLevelField, assignButton, modifyButton);

        Scene scene = new Scene(vbox, 350, 400);
        stage.setScene(scene);
        stage.show();
    }

    private boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void loadAgentsIntoComboBox(ComboBox<String> agentComboBox) {
        String url = "jdbc:your_database_url";
        String user = "username";
        String password = "password";
        String query = "SELECT agent_id, agent_name FROM agents"; // Example query

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String agentName = rs.getString("agent_name");
                agentComboBox.getItems().add(agentName);
            }

        } catch (SQLException e) {
            System.out.println("Database connection error: " + e.getMessage());
        }
    }

    private void assignAccessLevelToAgent(String agentName, int accessLevel, String jobTitle) {
        String url = "jdbc:your_database_url";
        String user = "username";
        String password = "password";
        String query = "UPDATE agents SET access_level = ?, job_title = ? WHERE agent_name = ?";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, accessLevel);
            stmt.setString(2, jobTitle);
            stmt.setString(3, agentName);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                showAlert("Success", "Access level assigned successfully.");
            } else {
                showAlert("Error", "Failed to assign access level.");
            }

        } catch (SQLException e) {
            System.out.println("Database connection error: " + e.getMessage());
        }
    }

    private void modifyAccessLevelForAgent(String agentName, int accessLevel, String jobTitle) {
        assignAccessLevelToAgent(agentName, accessLevel, jobTitle); // Modify is the same as assign in this case
    }

    private void showWorkPerformance() {
        Stage stage = new Stage();
        stage.setTitle("Agent Work Performance");

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));
        vbox.setAlignment(Pos.CENTER);

        // Agent Selection
        Label agentLabel = new Label("Select Agent:");
        ComboBox<String> agentComboBox = new ComboBox<>();
        loadAgentsIntoComboBox(agentComboBox); // Load agents from the database

        // Date Range Selection
        Label fromDateLabel = new Label("From Date:");
        DatePicker fromDatePicker = new DatePicker();

        Label toDateLabel = new Label("To Date:");
        DatePicker toDatePicker = new DatePicker();

        // Button to generate performance report
        Button generateReportButton = new Button("Generate Performance Report");
        generateReportButton.setOnAction(e -> {
            String selectedAgent = agentComboBox.getValue();
            Date fromDate = java.sql.Date.valueOf(fromDatePicker.getValue());
            Date toDate = java.sql.Date.valueOf(toDatePicker.getValue());

            if (selectedAgent != null && fromDate != null && toDate != null) {
                generatePerformanceReport(selectedAgent, fromDate, toDate);
            } else {
                showAlert("Input Error", "Please select an agent and date range.");
            }
        });

        vbox.getChildren().addAll(agentLabel, agentComboBox, fromDateLabel, fromDatePicker, toDateLabel, toDatePicker, generateReportButton);

        Scene scene = new Scene(vbox, 400, 400);
        stage.setScene(scene);
        stage.show();
    }

    private void generatePerformanceReport(String agentName, Date fromDate, Date toDate) {
        String url = "jdbc:your_database_url";
        String user = "username";
        String password = "password";
        String query = "SELECT COUNT(policy_id) AS policies_sold FROM policies WHERE agent_name = ? AND sale_date BETWEEN ? AND ?";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, agentName);
            stmt.setDate(2, (java.sql.Date) fromDate);
            stmt.setDate(3, (java.sql.Date) toDate);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int policiesSold = rs.getInt("policies_sold");
                // Display or process the performance data as needed
                String reportMessage = "Agent: " + agentName + "\nPolicies Sold: " + policiesSold + "\nFrom: " + fromDate + "\nTo: " + toDate;
                showAlert("Performance Report", reportMessage);
            } else {
                showAlert("No Data", "No policies sold by " + agentName + " during the selected period.");
            }

        } catch (SQLException e) {
            System.out.println("Database connection error: " + e.getMessage());
        }
    }


    private void showReceipting() {
        Stage stage = new Stage();
        stage.setTitle("Receipting");

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));
        vbox.setAlignment(Pos.CENTER);

        // Create buttons for each sub-feature
        Button manualReceiptCaptureButton = new Button("Manual Receipt Capture");
        Button reprintReceiptButton = new Button("Reprint Receipt");

        manualReceiptCaptureButton.setOnAction(e -> showManualReceiptCapture());
        reprintReceiptButton.setOnAction(e -> showReprintReceipt());

        vbox.getChildren().addAll(manualReceiptCaptureButton, reprintReceiptButton);

        Scene scene = new Scene(vbox, 300, 200);
        stage.setScene(scene);
        stage.show();
    }

    // Manual Receipt Capture
    private void showManualReceiptCapture() {
        Stage stage = new Stage();
        stage.setTitle("Manual Receipt Capture");

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));
        vbox.setAlignment(Pos.CENTER);

        Label receiptNumberLabel = new Label("Receipt Number:");
        TextField receiptNumberField = new TextField();
        receiptNumberField.setPromptText("Enter receipt number");

        Button captureButton = new Button("Capture Receipt");
        captureButton.setOnAction(e -> {
            String receiptNumber = receiptNumberField.getText().trim();
            if (!receiptNumber.isEmpty()) {
                captureReceipt(receiptNumber);
            } else {
                showAlert("Input Error", "Please enter a receipt number.");
            }
        });

        vbox.getChildren().addAll(receiptNumberLabel, receiptNumberField, captureButton);

        Scene scene = new Scene(vbox, 300, 200);
        stage.setScene(scene);
        stage.show();
    }

    private void captureReceipt(String receiptNumber) {
        String url = "jdbc:your_database_url";
        String user = "username";
        String password = "password";

        String query = "INSERT INTO receipts (receipt_number, captured_at) VALUES (?, NOW())";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, receiptNumber);
            int rowsInserted = stmt.executeUpdate();

            if (rowsInserted > 0) {
                showAlert("Success", "Receipt captured successfully.");
            } else {
                showAlert("Failure", "Failed to capture receipt.");
            }

        } catch (SQLException e) {
            System.out.println("Database connection error: " + e.getMessage());
        }
    }

    // Reprint Receipt
    private void showReprintReceipt() {
        Stage stage = new Stage();
        stage.setTitle("Reprint Receipt");

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));
        vbox.setAlignment(Pos.CENTER);

        Label receiptNumberLabel = new Label("Receipt Number:");
        TextField receiptNumberField = new TextField();
        receiptNumberField.setPromptText("Enter receipt number");

        Button reprintButton = new Button("Reprint Receipt");
        reprintButton.setOnAction(e -> {
            String receiptNumber = receiptNumberField.getText().trim();
            if (!receiptNumber.isEmpty()) {
                reprintReceipt(receiptNumber);
            } else {
                showAlert("Input Error", "Please enter a receipt number.");
            }
        });

        vbox.getChildren().addAll(receiptNumberLabel, receiptNumberField, reprintButton);

        Scene scene = new Scene(vbox, 300, 200);
        stage.setScene(scene);
        stage.show();
    }

    private void reprintReceipt(String receiptNumber) {
        String url = "jdbc:your_database_url";
        String user = "username";
        String password = "password";

        String query = "SELECT * FROM receipts WHERE receipt_number = ?";
        String reprintQuery = "INSERT INTO reprint_records (receipt_number, reprinted_at) VALUES (?, NOW())";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(query);
             PreparedStatement reprintStmt = conn.prepareStatement(reprintQuery)) {

            // Check if the receipt exists
            stmt.setString(1, receiptNumber);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String details = "Receipt Number: " + rs.getString("receipt_number") + "\nCaptured At: " + rs.getTimestamp("captured_at");
                showAlert("Receipt Found", details);

                // Record the reprint action
                reprintStmt.setString(1, receiptNumber);
                reprintStmt.executeUpdate();
                showAlert("Reprint Recorded", "Reprint action has been recorded successfully.");
            } else {
                showAlert("Not Found", "Receipt number not found.");
            }

        } catch (SQLException e) {
            System.out.println("Database connection error: " + e.getMessage());
        }
    }

    private void showPOSDeviceManagement() {
        Stage stage = new Stage();
        stage.setTitle("POS Device Management");

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));
        vbox.setAlignment(Pos.CENTER);

        Label actionLabel = new Label("Select Action:");
        Button addPOSButton = new Button("Add New POS Machine");
        Button managePOSButton = new Button("Manage Existing POS Machines");

        addPOSButton.setOnAction(e -> showAddPOSForm());
        managePOSButton.setOnAction(e -> showManagePOSForm());

        vbox.getChildren().addAll(actionLabel, addPOSButton, managePOSButton);

        Scene scene = new Scene(vbox, 300, 200);
        stage.setScene(scene);
        stage.show();
    }

    private void showAddPOSForm() {
        Stage stage = new Stage();
        stage.setTitle("Add New POS Machine");

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));
        vbox.setAlignment(Pos.CENTER);

        Label bankLabel = new Label("Bank Provider:");
        TextField bankField = new TextField();

        Label serialLabel = new Label("Serial Number:");
        TextField serialField = new TextField();

        Label modelLabel = new Label("Model Number:");
        TextField modelField = new TextField();

        Label locationLabel = new Label("Location:");
        TextField locationField = new TextField();

        Label installationDateLabel = new Label("Installation Date:");
        DatePicker installationDatePicker = new DatePicker();

        Button addButton = new Button("Add POS");
        addButton.setOnAction(e -> {
            String bank = bankField.getText().trim();
            String serial = serialField.getText().trim();
            String model = modelField.getText().trim();
            String location = locationField.getText().trim();
            LocalDate installationDate = installationDatePicker.getValue();

            if (!bank.isEmpty() && !serial.isEmpty() && !model.isEmpty() && !location.isEmpty() && installationDate != null) {
                addPOS(bank, serial, model, location, java.sql.Date.valueOf(installationDate));
                stage.close();
            } else {
                showAlert("Input Error", "Please fill in all fields.");
            }
        });

        vbox.getChildren().addAll(bankLabel, bankField, serialLabel, serialField, modelLabel, modelField, locationLabel, locationField, installationDateLabel, installationDatePicker, addButton);

        Scene scene = new Scene(vbox, 300, 350);
        stage.setScene(scene);
        stage.show();
    }

    private void addPOS(String bank, String serial, String model, String location, java.sql.Date installationDate) {
        String url = "jdbc:your_database_url";
        String user = "username";
        String password = "password";

        String query = "INSERT INTO pos_machines (bank_provider, serial_number, model_number, location, installation_date) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, bank);
            stmt.setString(2, serial);
            stmt.setString(3, model);
            stmt.setString(4, location);
            stmt.setDate(5, installationDate);

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                showAlert("Success", "POS machine added successfully.");
            } else {
                showAlert("Failure", "Failed to add POS machine.");
            }

        } catch (SQLException e) {
            System.out.println("Database connection error: " + e.getMessage());
        }
    }

    private void showManagePOSForm() {
        Stage stage = new Stage();
        stage.setTitle("Manage POS Machines");

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));
        vbox.setAlignment(Pos.CENTER);

        Label selectPOSLabel = new Label("Select POS Machine:");
        ComboBox<String> posComboBox = new ComboBox<>();

        populatePOSComboBox(posComboBox);

        posComboBox.setOnAction(e -> {
            String selectedPOS = posComboBox.getValue();
            if (selectedPOS != null) {
                showEditPOSForm(selectedPOS);
            }
        });

        vbox.getChildren().addAll(selectPOSLabel, posComboBox);

        Scene scene = new Scene(vbox, 300, 200);
        stage.setScene(scene);
        stage.show();
    }

    private void populatePOSComboBox(ComboBox<String> posComboBox) {
        String url = "jdbc:your_database_url";
        String user = "username";
        String password = "password";

        String query = "SELECT pos_id FROM pos_machines";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String posId = rs.getString("pos_id");
                posComboBox.getItems().add(posId);
            }

        } catch (SQLException e) {
            System.out.println("Database connection error: " + e.getMessage());
        }
    }

    private void showEditPOSForm(String posId) {
        Stage stage = new Stage();
        stage.setTitle("Edit POS Machine");

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));
        vbox.setAlignment(Pos.CENTER);

        Label bankLabel = new Label("Bank Provider:");
        TextField bankField = new TextField();

        Label serialLabel = new Label("Serial Number:");
        TextField serialField = new TextField();

        Label modelLabel = new Label("Model Number:");
        TextField modelField = new TextField();

        Label locationLabel = new Label("Location:");
        TextField locationField = new TextField();

        Label installationDateLabel = new Label("Installation Date:");
        DatePicker installationDatePicker = new DatePicker();

        Button updateButton = new Button("Update POS");
        updateButton.setOnAction(e -> {
            String bank = bankField.getText().trim();
            String serial = serialField.getText().trim();
            String model = modelField.getText().trim();
            String location = locationField.getText().trim();
            LocalDate installationDate = installationDatePicker.getValue();

            if (!bank.isEmpty() && !serial.isEmpty() && !model.isEmpty() && !location.isEmpty() && installationDate != null) {
                updatePOS(posId, bank, serial, model, location, java.sql.Date.valueOf(installationDate));
                stage.close();
            } else {
                showAlert("Input Error", "Please fill in all fields.");
            }
        });

        vbox.getChildren().addAll(bankLabel, bankField, serialLabel, serialField, modelLabel, modelField, locationLabel, locationField, installationDateLabel, installationDatePicker, updateButton);

        Scene scene = new Scene(vbox, 300, 350);
        stage.setScene(scene);
        stage.show();

        // Fetch and populate existing POS details here
        fetchAndPopulatePOSDetails(posId, bankField, serialField, modelField, locationField, installationDatePicker);
    }

    private void fetchAndPopulatePOSDetails(String posId, TextField bankField, TextField serialField, TextField modelField, TextField locationField, DatePicker installationDatePicker) {
        String url = "jdbc:your_database_url";
        String user = "username";
        String password = "password";

        String query = "SELECT * FROM pos_machines WHERE pos_id = ?";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, posId);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                bankField.setText(rs.getString("bank_provider"));
                serialField.setText(rs.getString("serial_number"));
                modelField.setText(rs.getString("model_number"));
                locationField.setText(rs.getString("location"));
                installationDatePicker.setValue(rs.getDate("installation_date").toLocalDate());
            }

        } catch (SQLException e) {
            System.out.println("Database connection error: " + e.getMessage());
        }
    }

    private void updatePOS(String posId, String bank, String serial, String model, String location, java.sql.Date installationDate) {
        String url = "jdbc:your_database_url";
        String user = "username";
        String password = "password";

        String query = "UPDATE pos_machines SET bank_provider = ?, serial_number = ?, model_number = ?, location = ?, installation_date = ? WHERE pos_id = ?";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, bank);
            stmt.setString(2, serial);
            stmt.setString(3, model);
            stmt.setString(4, location);
            stmt.setDate(5, installationDate);
            stmt.setString(6, posId);

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                showAlert("Success", "POS machine updated successfully.");
            } else {
                showAlert("Failure", "Failed to update POS machine.");
            }

        } catch (SQLException e) {
            System.out.println("Database connection error: " + e.getMessage());
        }
    }



    // Method to launch the chatbot
    private void showChatbot() {
        SystemChatbot chatbot = new SystemChatbot();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to the System Assistant! How can I help you today?");
        while (true) {
            System.out.print("You: ");
            String userInput = scanner.nextLine();

            if (userInput.equalsIgnoreCase("exit")) {
                System.out.println("Thank you for using the System Assistant. Goodbye!");
                break;
            }

            String response = chatbot.getResponse(userInput);
            System.out.println("Chatbot: " + response);
        }

        scanner.close();
    }

    // Example main method to test calling the chatbot
    public static void main(String[] args) {
        OtherFeaturesManager manager = new OtherFeaturesManager();
        manager.showChatbot(); // This will start the chatbot
    }

    private void showAlert(String title, String message) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        }
}


