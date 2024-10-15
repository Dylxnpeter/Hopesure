import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MemberReportsManager {

    private final Stage primaryStage;

    public MemberReportsManager(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void showMemberReportsPage() {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(10);
        grid.setHgap(10);

        Label reportTypeLabel = new Label("Select Report Type:");
        ComboBox<String> reportTypeComboBox = new ComboBox<>();
        reportTypeComboBox.getItems().addAll(
                "Members Captured",
                "Member Extract",
                "Member Listing",
                "Non-Payment Report",
                "Member Status Report",
                "Member Comment Listing",
                "Multiple ID No Extract",
                "Sales by Agent"
        );
        grid.add(reportTypeLabel, 0, 0);
        grid.add(reportTypeComboBox, 1, 0);

        Button generateButton = new Button("Generate Report");
        generateButton.setOnAction(e -> handleReportGeneration(reportTypeComboBox.getValue()));
        grid.add(generateButton, 1, 1);

        Scene scene = new Scene(grid, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void handleReportGeneration(String reportType) {
        if (reportType == null) {
            showAlert(Alert.AlertType.ERROR, "Invalid Selection", "Please select a valid report type.");
            return;
        }

        switch (reportType) {
            case "Members Captured":
                showMembersCapturedOptions();
                break;
            case "Member Extract":
                showMemberExtractOptions();
                break;
            case "Member Listing":
                showMemberListingOptions();
                break;
            case "Non-Payment Report":
                showNonPaymentReportOptions();
                break;
            case "Member Status Report":
                showMemberStatusReportOptions();
                break;
            case "Member Comment Listing":
                showMemberCommentListingOptions();
                break;
            case "Multiple ID No Extract":
                showMultipleIDNoExtractOptions();
                break;
            case "Sales by Agent":
                showSalesByAgentOptions();
                break;
            default:
                showAlert(Alert.AlertType.ERROR, "Invalid Choice", "Please try again.");
                break;
        }
    }

    private void showMembersCapturedOptions() {
        Stage optionsStage = new Stage();
        optionsStage.setTitle("Members Captured Report Options");

        GridPane grid = createFormGrid();

        Label fromDateLabel = new Label("From Date:");
        DatePicker startDatePicker = new DatePicker();
        Label toDateLabel = new Label("To Date:");
        DatePicker endDatePicker = new DatePicker();

        grid.add(fromDateLabel, 0, 0);
        grid.add(startDatePicker, 1, 0);
        grid.add(toDateLabel, 0, 1);
        grid.add(endDatePicker, 1, 1);

        addBrokerSelectionField(grid, 2);

        Button generateButton = new Button("Generate");
        generateButton.setOnAction(e -> {
            showAlert(Alert.AlertType.INFORMATION, "Report Generated", "The Members Captured report has been generated.");
            optionsStage.close();
        });
        grid.add(generateButton, 1, 3);

        Scene scene = new Scene(grid, 400, 300);
        optionsStage.setScene(scene);
        optionsStage.show();
    }

    private void showMemberExtractOptions() {
        Stage optionsStage = new Stage();
        optionsStage.setTitle("Member Extract Report Options");

        GridPane grid = createFormGrid();

        CheckBox selectAllMembersCheckBox = new CheckBox("Generate report for all members");
        grid.add(selectAllMembersCheckBox, 0, 0, 2, 1);

        VBox memberCheckBoxContainer = new VBox();
        ScrollPane scrollPane = new ScrollPane(memberCheckBoxContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setDisable(true);
        grid.add(scrollPane, 0, 1, 2, 1);

        List<CheckBox> memberCheckBoxes = new ArrayList<>();

        try {
            Connection conn = DriverManager.getConnection("jdbc:your_database_url", "username", "password");
            Statement stmt = conn.createStatement();
            String query = "SELECT m.member_id, m.title, m.gender, m.first_name, m.surname, m.id_number, m.phone_number, " +
                    "m.email_address, m.employer, m.street_address, m.date_of_birth, m.city, m.province, " +
                    "m.telephone_number, m.alternative_number, m.occupation, m.postal_code, " +
                    "p.policy_id, p.policy_name, b.beneficiary_name " +
                    "FROM members m " +
                    "LEFT JOIN policies p ON m.member_id = p.member_id " +
                    "LEFT JOIN beneficiaries b ON m.member_id = b.member_id";
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                String memberId = rs.getString("member_id");
                String title = rs.getString("title");
                String gender = rs.getString("gender");
                String firstName = rs.getString("first_name");
                String surname = rs.getString("surname");
                String idNumber = rs.getString("id_number");
                String phoneNumber = rs.getString("phone_number");
                String emailAddress = rs.getString("email_address");
                String employer = rs.getString("employer");
                String streetAddress = rs.getString("street_address");
                String dateOfBirth = rs.getString("date_of_birth");
                String city = rs.getString("city");
                String province = rs.getString("province");
                String telephoneNumber = rs.getString("telephone_number");
                String alternativeNumber = rs.getString("alternative_number");
                String occupation = rs.getString("occupation");
                String postalCode = rs.getString("postal_code");
                String policyId = rs.getString("policy_id");
                String policyName = rs.getString("policy_name");
                String beneficiaryName = rs.getString("beneficiary_name");

                String details = String.format("%s %s %s (%s)\nID: %s\nPhone: %s\nEmail: %s\nEmployer: %s\n" +
                                "Address: %s, %s, %s\nDOB: %s\nCity: %s\nProvince: %s\nTelephone: %s\n" +
                                "Alternative: %s\nOccupation: %s\nPostal Code: %s",
                        title, firstName, surname, memberId, idNumber, phoneNumber, emailAddress,
                        employer, streetAddress, city, province, dateOfBirth, city, province,
                        telephoneNumber, alternativeNumber, occupation, postalCode);
                if (policyId != null) {
                    details += String.format("\nPolicy: %s (%s)", policyName, policyId);
                }
                if (beneficiaryName != null) {
                    details += String.format("\nBeneficiary: %s", beneficiaryName);
                }

                CheckBox checkBox = new CheckBox(details);
                checkBox.setUserData(memberId);
                memberCheckBoxes.add(checkBox);
                memberCheckBoxContainer.getChildren().add(checkBox);
            }

            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println("Database connection error: " + e.getMessage());
        }

        selectAllMembersCheckBox.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
            scrollPane.setDisable(isSelected);
            if (isSelected) {
                memberCheckBoxes.forEach(cb -> cb.setSelected(false));
            }
        });

        Button generateButton = new Button("Generate");
        generateButton.setOnAction(e -> {
            if (selectAllMembersCheckBox.isSelected()) {
                generateReportForAllMembers();
            } else {
                List<String> selectedMemberIds = memberCheckBoxes.stream()
                        .filter(CheckBox::isSelected)
                        .map(cb -> (String) cb.getUserData())
                        .collect(Collectors.toList());
                generateReportForSelectedMembers(selectedMemberIds);
            }
            showAlert(Alert.AlertType.INFORMATION, "Report Generated", "The Member Extract report has been generated.");
            optionsStage.close();
        });
        grid.add(generateButton, 1, 2);

        Scene scene = new Scene(grid, 500, 500);
        optionsStage.setScene(scene);
        optionsStage.show();
    }

    private void generateReportForAllMembers() {
        // Implement the logic to generate a report for all members
    }

    private void generateReportForSelectedMembers(List<String> selectedMemberIds) {
        // Implement the logic to generate a report for the selected members
    }

    private void showMemberListingOptions() {
        Stage optionsStage = new Stage();
        optionsStage.setTitle("Member Listing Report Options");

        GridPane grid = createFormGrid();

        Label statusLabel = new Label("Status:");
        ComboBox<String> statusComboBox = new ComboBox<>();
        statusComboBox.getItems().addAll("All", "Active", "Lapsed", "Reinstated", "Not Taken Up");
        statusComboBox.getSelectionModel().select(0);
        grid.add(statusLabel, 0, 0);
        grid.add(statusComboBox, 1, 0);

        VBox memberListContainer = new VBox();
        ScrollPane scrollPane = new ScrollPane(memberListContainer);
        scrollPane.setFitToWidth(true);
        grid.add(scrollPane, 0, 1, 2, 1);

        List<CheckBox> memberCheckBoxes = new ArrayList<>();

        Button generateButton = new Button("Generate");
        generateButton.setOnAction(e -> {
            String selectedStatus = statusComboBox.getValue();
            memberCheckBoxes.clear();
            memberListContainer.getChildren().clear();

            try {
                Connection conn = DriverManager.getConnection("jdbc:your_database_url", "username", "password");
                Statement stmt = conn.createStatement();

                String query = "SELECT member_id, title, first_name, surname, id_number, phone_number FROM members";
                if (!"All".equals(selectedStatus)) {
                    query += " WHERE status = '" + selectedStatus + "'";
                }

                ResultSet rs = stmt.executeQuery(query);

                while (rs.next()) {
                    String memberId = rs.getString("member_id");
                    String title = rs.getString("title");
                    String firstName = rs.getString("first_name");
                    String surname = rs.getString("surname");
                    String idNumber = rs.getString("id_number");
                    String phoneNumber = rs.getString("phone_number");

                    String details = String.format("%s %s %s\nID: %s\nPhone: %s", title, firstName, surname, idNumber, phoneNumber);

                    CheckBox checkBox = new CheckBox(details);
                    checkBox.setUserData(memberId);
                    memberCheckBoxes.add(checkBox);
                    memberListContainer.getChildren().add(checkBox);
                }

                rs.close();
                stmt.close();
                conn.close();
            } catch (SQLException ex) {
                System.out.println("Database connection error: " + ex.getMessage());
            }

            showAlert(Alert.AlertType.INFORMATION, "Report Generated", "The Member Listing report has been generated.");
            optionsStage.close();
        });

        grid.add(generateButton, 1, 2);

        Scene scene = new Scene(grid, 400, 300);
        optionsStage.setScene(scene);
        optionsStage.show();
    }

    private void showNonPaymentReportOptions() {
        Stage optionsStage = new Stage();
        optionsStage.setTitle("Non-Payment Report Options");

        GridPane grid = createFormGrid();

        Label fromDateLabel = new Label("From Date:");
        DatePicker startDatePicker = new DatePicker();
        Label toDateLabel = new Label("To Date:");
        DatePicker endDatePicker = new DatePicker();

        grid.add(fromDateLabel, 0, 0);
        grid.add(startDatePicker, 1, 0);
        grid.add(toDateLabel, 0, 1);
        grid.add(endDatePicker, 1, 1);

        VBox nonPaymentListContainer = new VBox();
        ScrollPane scrollPane = new ScrollPane(nonPaymentListContainer);
        scrollPane.setFitToWidth(true);
        grid.add(scrollPane, 0, 3, 2, 1);

        List<CheckBox> nonPaymentCheckBoxes = new ArrayList<>();

        Button generateButton = new Button("Generate");
        generateButton.setOnAction(e -> {
            LocalDate fromDate = startDatePicker.getValue();
            LocalDate toDate = endDatePicker.getValue();

            if (fromDate == null || toDate == null) {
                showAlert(Alert.AlertType.WARNING, "Invalid Date Range", "Please select both the start and end dates.");
                return;
            }

            nonPaymentCheckBoxes.clear();
            nonPaymentListContainer.getChildren().clear();

            try {
                Connection conn = DriverManager.getConnection("jdbc:your_database_url", "username", "password");
                PreparedStatement stmt = conn.prepareStatement(
                        "SELECT m.member_id, m.title, m.first_name, m.surname, m.id_number, m.phone_number " +
                                "FROM members m " +
                                "LEFT JOIN payments p ON m.member_id = p.member_id " +
                                "WHERE (p.payment_date IS NULL OR p.payment_date < ?) " +
                                "AND (p.payment_date > ? OR p.payment_date IS NULL)"
                );
                stmt.setDate(1, java.sql.Date.valueOf(toDate));
                stmt.setDate(2, java.sql.Date.valueOf(fromDate));

                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    String memberId = rs.getString("member_id");
                    String title = rs.getString("title");
                    String firstName = rs.getString("first_name");
                    String surname = rs.getString("surname");
                    String idNumber = rs.getString("id_number");
                    String phoneNumber = rs.getString("phone_number");

                    String details = String.format("%s %s %s\nID: %s\nPhone: %s", title, firstName, surname, idNumber, phoneNumber);

                    CheckBox checkBox = new CheckBox(details);
                    checkBox.setUserData(memberId);
                    nonPaymentCheckBoxes.add(checkBox);
                    nonPaymentListContainer.getChildren().add(checkBox);
                }

                rs.close();
                stmt.close();
                conn.close();
            } catch (SQLException ex) {
                System.out.println("Database connection error: " + ex.getMessage());
            }

            showAlert(Alert.AlertType.INFORMATION, "Report Generated", "The Non-Payment report has been generated.");
            optionsStage.close();
        });

        grid.add(generateButton, 1, 4);

        Scene scene = new Scene(grid, 400, 350);
        optionsStage.setScene(scene);
        optionsStage.show();
    }


    private void showMemberStatusReportOptions() {
        Stage optionsStage = new Stage();
        optionsStage.setTitle("Member Status Report Options");

        GridPane grid = createFormGrid();

        Label statusLabel = new Label("Status:");
        ComboBox<String> statusComboBox = new ComboBox<>();
        statusComboBox.getItems().addAll("All", "Active", "Lapsed", "Reinstated", "Not Taken Up");
        statusComboBox.getSelectionModel().select(0);

        Label searchLabel = new Label("Search By:");
        ComboBox<String> searchComboBox = new ComboBox<>();
        searchComboBox.getItems().addAll("ID", "Phone Number");

        TextField searchField = new TextField();
        searchField.setPromptText("Enter ID or Phone Number");

        grid.add(statusLabel, 0, 0);
        grid.add(statusComboBox, 1, 0);
        grid.add(searchLabel, 0, 1);
        grid.add(searchComboBox, 1, 1);
        grid.add(searchField, 0, 2, 2, 1);

        VBox memberStatusListContainer = new VBox();
        ScrollPane scrollPane = new ScrollPane(memberStatusListContainer);
        scrollPane.setFitToWidth(true);
        grid.add(scrollPane, 0, 4, 2, 1);

        List<CheckBox> memberStatusCheckBoxes = new ArrayList<>();

        Button generateButton = new Button("Generate");
        generateButton.setOnAction(e -> {
            String status = statusComboBox.getValue();
            String searchType = searchComboBox.getValue();
            String searchText = searchField.getText();

            memberStatusCheckBoxes.clear();
            memberStatusListContainer.getChildren().clear();

            String query = "SELECT m.member_id, m.title, m.first_name, m.surname, m.id_number, m.phone_number, m.status " +
                    "FROM members m WHERE 1=1";

            if (!"All".equals(status)) {
                query += " AND m.status = ?";
            }

            if ("ID".equals(searchType) && !searchText.isEmpty()) {
                query += " AND m.id_number = ?";
            } else if ("Phone Number".equals(searchType) && !searchText.isEmpty()) {
                query += " AND m.phone_number = ?";
            }

            try {
                Connection conn = DriverManager.getConnection("jdbc:your_database_url", "username", "password");
                PreparedStatement stmt = conn.prepareStatement(query);

                int paramIndex = 1;
                if (!"All".equals(status)) {
                    stmt.setString(paramIndex++, status);
                }
                if (!searchText.isEmpty()) {
                    stmt.setString(paramIndex, searchText);
                }

                ResultSet rs = stmt.executeQuery();

                while (rs.next()) {
                    String memberId = rs.getString("member_id");
                    String title = rs.getString("title");
                    String firstName = rs.getString("first_name");
                    String surname = rs.getString("surname");
                    String idNumber = rs.getString("id_number");
                    String phoneNumber = rs.getString("phone_number");
                    String memberStatus = rs.getString("status");

                    String details = String.format("%s %s %s\nID: %s\nPhone: %s\nStatus: %s",
                            title, firstName, surname, idNumber, phoneNumber, memberStatus);

                    CheckBox checkBox = new CheckBox(details);
                    checkBox.setUserData(memberId);
                    memberStatusCheckBoxes.add(checkBox);
                    memberStatusListContainer.getChildren().add(checkBox);
                }

                rs.close();
                stmt.close();
                conn.close();
            } catch (SQLException ex) {
                System.out.println("Database connection error: " + ex.getMessage());
            }

            showAlert(Alert.AlertType.INFORMATION, "Report Generated", "The Member Status report has been generated.");
            optionsStage.close();
        });

        grid.add(generateButton, 1, 5);

        Scene scene = new Scene(grid, 500, 400);
        optionsStage.setScene(scene);
        optionsStage.show();
    }


    private void showMemberCommentListingOptions() {
        Stage optionsStage = new Stage();
        optionsStage.setTitle("Member Comment Listing Options");

        GridPane grid = createFormGrid();

        Label fromDateLabel = new Label("From Date:");
        DatePicker startDatePicker = new DatePicker();
        Label toDateLabel = new Label("To Date:");
        DatePicker endDatePicker = new DatePicker();

        Label agentLabel = new Label("Agent:");
        ComboBox<String> agentComboBox = new ComboBox<>();

        try {
            Connection conn = DriverManager.getConnection("jdbc:your_database_url", "username", "password");
            Statement stmt = conn.createStatement();
            String query = "SELECT agent_name FROM agents"; // Assuming you have an 'agents' table with a 'agent_name' column
            ResultSet rs = stmt.executeQuery(query);

            agentComboBox.getItems().add("All");
            while (rs.next()) {
                String agentName = rs.getString("agent_name");
                agentComboBox.getItems().add(agentName);
            }

            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println("Database connection error: " + e.getMessage());
        }

        grid.add(fromDateLabel, 0, 0);
        grid.add(startDatePicker, 1, 0);
        grid.add(toDateLabel, 0, 1);
        grid.add(endDatePicker, 1, 1);
        grid.add(agentLabel, 0, 2);
        grid.add(agentComboBox, 1, 2);

        Button generateButton = new Button("Generate");
        generateButton.setOnAction(e -> {
            // Implement logic to generate member comment listing report
            showAlert(Alert.AlertType.INFORMATION, "Report Generated", "The Member Comment Listing report has been generated.");
            optionsStage.close();
        });
        grid.add(generateButton, 1, 3);

        Scene scene = new Scene(grid, 400, 250);
        optionsStage.setScene(scene);
        optionsStage.show();
    }



    private void showMultipleIDNoExtractOptions() {
        Stage optionsStage = new Stage();
        optionsStage.setTitle("Multiple ID No Extract Options");

        GridPane grid = createFormGrid();

        List<String> multipleIDNos = new ArrayList<>();

        try {
            Connection conn = DriverManager.getConnection("jdbc:your_database_url", "username", "password");
            Statement stmt = conn.createStatement();

            String query = "SELECT id_number, COUNT(policy_id) as policy_count " +
                    "FROM policies " +
                    "GROUP BY id_number " +
                    "HAVING COUNT(policy_id) > 1";

            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                String idNumber = rs.getString("id_number");
                int policyCount = rs.getInt("policy_count");
                multipleIDNos.add("ID: " + idNumber + " | Policies: " + policyCount);
            }

            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println("Database connection error: " + e.getMessage());
        }

        ListView<String> listView = new ListView<>();
        listView.getItems().addAll(multipleIDNos);
        grid.add(listView, 0, 0, 2, 1);

        Button generateButton = new Button("Generate");
        generateButton.setOnAction(e -> {
            showAlert(Alert.AlertType.INFORMATION, "Report Generated", "The Multiple ID No Extract report has been generated.");
            optionsStage.close();
        });
        grid.add(generateButton, 1, 1);

        Scene scene = new Scene(grid, 400, 300);
        optionsStage.setScene(scene);
        optionsStage.show();
    }

    private void showSalesByAgentOptions() {
        Stage optionsStage = new Stage();
        optionsStage.setTitle("Sales by Agent Report Options");

        GridPane grid = createFormGrid();

        Label fromDateLabel = new Label("From Date:");
        DatePicker startDatePicker = new DatePicker();
        Label toDateLabel = new Label("To Date:");
        DatePicker endDatePicker = new DatePicker();

        Label policyLabel = new Label("Policy:");
        ComboBox<String> policyComboBox = new ComboBox<>();

        try {
            Connection conn = DriverManager.getConnection("jdbc:your_database_url", "username", "password");
            Statement stmt = conn.createStatement();
            String query = "SELECT policy_id, policy_name FROM policies";
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                String policyId = rs.getString("policy_id");
                String policyName = rs.getString("policy_name");
                policyComboBox.getItems().add(policyId + " - " + policyName);
            }

            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println("Database connection error: " + e.getMessage());
        }

        grid.add(fromDateLabel, 0, 0);
        grid.add(startDatePicker, 1, 0);
        grid.add(toDateLabel, 0, 1);
        grid.add(endDatePicker, 1, 1);
        grid.add(policyLabel, 0, 2);
        grid.add(policyComboBox, 1, 2);

        Button generateButton = new Button("Generate");
        generateButton.setOnAction(e -> {
            String selectedPolicy = policyComboBox.getValue();
            String fromDate = startDatePicker.getValue().toString();
            String toDate = endDatePicker.getValue().toString();

            // Implement logic to generate the sales by agent report using selectedPolicy, fromDate, and toDate
            showAlert(Alert.AlertType.INFORMATION, "Report Generated", "The Sales by Agent report has been generated.");
            optionsStage.close();
        });
        grid.add(generateButton, 1, 3);

        Scene scene = new Scene(grid, 400, 300);
        optionsStage.setScene(scene);
        optionsStage.show();
    }

    private GridPane createFormGrid() {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(10);
        grid.setHgap(10);
        return grid;
    }

    private void addBrokerSelectionField(GridPane grid, int row) {
        Label brokerLabel = new Label("Broker:");
        ComboBox<String> brokerComboBox = new ComboBox<>();
        brokerComboBox.getItems().addAll("All", "Broker 1", "Broker 2"); // Populate with actual brokers
        grid.add(brokerLabel, 0, row);
        grid.add(brokerComboBox, 1, row);
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
