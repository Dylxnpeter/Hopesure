import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.sql.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class MemberManager {

    private Stage primaryStage;
    private String memberID; // Added memberID field

    public MemberManager(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void showMemberActionsPage() {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(10);
        grid.setHgap(10);

        Button memberCaptureButton = new Button("Member Capture");
        memberCaptureButton.setOnAction(e -> showMemberCaptureForm());
        grid.add(memberCaptureButton, 0, 0);

        Button memberEnquiryButton = new Button("Member Enquiry & Search");
        memberEnquiryButton.setOnAction(e -> showMemberEnquiryForm());
        grid.add(memberEnquiryButton, 1, 0);

        // Set scene and show
        Scene scene = new Scene(grid, 300, 100);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Connection connectToDatabase() throws SQLException {
        // Database URL, username, and password
        String url = "jdbc:mysql://localhost:3306/hopesure";
        String user = "root";
        String password = "passwordngori";

        // Establish connection
        return DriverManager.getConnection(url, user, password);
    }

    private void showMemberCaptureForm() {
        Stage captureStage = new Stage();
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(10);
        grid.setHgap(10);

        // Dropdown for Title selection
        ComboBox<String> titleComboBox = new ComboBox<>();
        titleComboBox.getItems().addAll("Mr", "Mrs", "Sir", "Miss");
        titleComboBox.setPromptText("Select Title");
        grid.add(new Label("Title:"), 0, 0);
        grid.add(titleComboBox, 1, 0);

        // Dropdown for Gender selection
        ComboBox<String> genderComboBox = new ComboBox<>();
        genderComboBox.getItems().addAll("Male", "Female");
        genderComboBox.setPromptText("Select Gender");
        grid.add(new Label("Gender:"), 0, 1);
        grid.add(genderComboBox, 1, 1);

        addFormField(grid, "First Name:", 2);
        addFormField(grid, "Surname:", 3);
        addFormField(grid, "Member ID:", 4);
        addFormField(grid, "Phone Number:", 5);
        addFormField(grid, "Email Address:", 6);
        addFormField(grid, "Employer:", 7);
        addFormField(grid, "Street Address:", 8);
        addFormField(grid, "Date of Birth (dd-MM-yyyy):", 9);
        addFormField(grid, "City:", 10);
        addFormField(grid, "Province:", 11);
        addFormField(grid, "Telephone Number:", 12);
        addFormField(grid, "Alternative Number:", 13);
        addFormField(grid, "Occupation:", 14);
        addFormField(grid, "Postal Code:", 15);

        // Button to open Beneficiaries Form
        Button addBeneficiariesButton = new Button("Add Beneficiaries");
        addBeneficiariesButton.setOnAction(e -> showBeneficiariesForm());
        grid.add(addBeneficiariesButton, 0, 16, 2, 1);

        // File upload button
        Button uploadButton = new Button("Upload Form");
        FileChooser fileChooser = new FileChooser();
        uploadButton.setOnAction(e -> {
            File file = fileChooser.showOpenDialog(captureStage);
            if (file != null) {
                // Handle the file upload (save the file path or upload to server)
                System.out.println("Selected file: " + file.getAbsolutePath());
            }
        });
        grid.add(uploadButton, 0, 17, 2, 1);

        // Submit button
        Button submitButton = new Button("Submit");
        submitButton.setOnAction(e -> {
            try {
                // Capture and save member details to the database
                captureMemberDetails(grid);

                // Close the form
                captureStage.close();

                // Optionally, show the home page again
                primaryStage.show();
            } catch (DateTimeParseException ex) {
                // Show an error if date parsing fails
                showAlert(Alert.AlertType.ERROR, "Invalid Date", "Please enter the date in the format dd-MM-yyyy.");
            } catch (SQLException sqlEx) {
                // Show an error if database connection or query fails
                showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to save member details.");
            }
        });
        grid.add(submitButton, 1, 18);  // Ensure it's in the right position

        // Create a Scene and add it to the Stage
        Scene scene = new Scene(grid, 500, 750);  // Increase size to fit all elements
        captureStage.setScene(scene);
        captureStage.setTitle("Member Capture Form");
        captureStage.show();
    }

    private void showBeneficiariesForm() {
        Stage beneficiariesStage = new Stage();
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10, 10, 10, 10));

        // List to hold beneficiary fields
        VBox beneficiariesList = new VBox(10);
        vbox.getChildren().add(new Label("Beneficiaries:"));
        vbox.getChildren().add(beneficiariesList);

        // Add button to add new beneficiary fields
        Button addBeneficiaryButton = new Button("Add Beneficiary");
        addBeneficiaryButton.setOnAction(e -> {
            // Create new beneficiary fields
            GridPane beneficiaryGrid = new GridPane();
            beneficiaryGrid.setHgap(10);
            beneficiaryGrid.setVgap(5);
            beneficiaryGrid.add(new Label("First Name:"), 0, 0);
            TextField beneficiaryFirstName = new TextField();
            beneficiaryGrid.add(beneficiaryFirstName, 1, 0);
            beneficiaryGrid.add(new Label("Surname:"), 0, 1);
            TextField beneficiarySurname = new TextField();
            beneficiaryGrid.add(beneficiarySurname, 1, 1);
            beneficiaryGrid.add(new Label("Phone Number:"), 0, 2);
            TextField beneficiaryPhone = new TextField();
            beneficiaryGrid.add(beneficiaryPhone, 1, 2);
            beneficiaryGrid.add(new Label("Relation:"), 0, 3);
            ComboBox<String> relationComboBox = new ComboBox<>();
            relationComboBox.getItems().addAll("Spouse", "Child", "Parent", "Sibling", "Other");
            beneficiaryGrid.add(relationComboBox, 1, 3);
            beneficiariesList.getChildren().add(beneficiaryGrid);
        });
        vbox.getChildren().add(addBeneficiaryButton);

        // Submit button for beneficiaries
        Button submitBeneficiariesButton = new Button("Save Beneficiaries");
        submitBeneficiariesButton.setOnAction(e -> {
            try {
                // Save beneficiaries details
                saveBeneficiariesDetails(beneficiariesList);

                // Close the beneficiaries form
                beneficiariesStage.close();
            } catch (SQLException sqlEx) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to save beneficiaries.");
            }
        });
        vbox.getChildren().add(submitBeneficiariesButton);

        // Create a Scene and add it to the Stage
        Scene scene = new Scene(vbox, 400, 600);
        beneficiariesStage.setScene(scene);
        beneficiariesStage.setTitle("Beneficiaries Form");
        beneficiariesStage.show();
    }

    private void saveBeneficiariesDetails(VBox beneficiariesList) throws SQLException {
        // Implement logic to save beneficiaries details to the database
        Connection connection = connectToDatabase();  // Method to get DB connection
        PreparedStatement stmt = connection.prepareStatement(
                "INSERT INTO Beneficiaries (MemberID, FirstName, Surname, PhoneNumber, Relation) VALUES (?, ?, ?, ?, ?)");

        for (Node node : beneficiariesList.getChildren()) {
            if (node instanceof GridPane) {
                GridPane grid = (GridPane) node;
                TextField firstNameField = (TextField) grid.getChildren().get(1);
                TextField surnameField = (TextField) grid.getChildren().get(3);
                TextField phoneField = (TextField) grid.getChildren().get(5);
                ComboBox<String> relationComboBox = (ComboBox<String>) grid.getChildren().get(7);
                stmt.setString(1, memberID);  // Use the memberID field
                stmt.setString(2, firstNameField.getText());
                stmt.setString(3, surnameField.getText());
                stmt.setString(4, phoneField.getText());
                stmt.setString(5, relationComboBox.getValue());
                stmt.addBatch();
            }
        }
        stmt.executeBatch();
        stmt.close();
        connection.close();
    }

    private void showMemberEnquiryForm() {
        Stage enquiryStage = new Stage();
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(10);
        grid.setHgap(10);

        // Option to search by ID or Phone Number
        ToggleGroup searchToggleGroup = new ToggleGroup();
        RadioButton searchByIdRadio = new RadioButton("Search by Member ID");
        searchByIdRadio.setToggleGroup(searchToggleGroup);
        searchByIdRadio.setSelected(true);
        RadioButton searchByPhoneRadio = new RadioButton("Search by Phone Number");
        searchByPhoneRadio.setToggleGroup(searchToggleGroup);

        HBox radioBox = new HBox(10, searchByIdRadio, searchByPhoneRadio);
        grid.add(radioBox, 0, 0, 2, 1);

        // Search Field
        TextField searchField = new TextField();
        grid.add(new Label("Search Term:"), 0, 1);
        grid.add(searchField, 1, 1);

        // Button to search and display results
        Button searchButton = new Button("Search");
        searchButton.setOnAction(e -> {
            String searchTerm = searchField.getText();
            if (searchByIdRadio.isSelected()) {
                searchMemberDetails(searchTerm, "ID");
            } else if (searchByPhoneRadio.isSelected()) {
                searchMemberDetails(searchTerm, "PhoneNumber");
            }
        });
        grid.add(searchButton, 2, 1);

        // Create a Scene and add it to the Stage
        Scene scene = new Scene(grid, 500, 200);
        enquiryStage.setScene(scene);
        enquiryStage.setTitle("Member Enquiry Form");
        enquiryStage.show();
    }

    private void searchMemberDetails(String criteria, String searchValue) {
        // Implement logic to search and display member details based on criteria
        String query = "";
        if ("Search by Member ID".equals(criteria)) {
            query = "SELECT * FROM MemberDetails WHERE MemberID = ?";
        } else if ("Search by Phone Number".equals(criteria)) {
            query = "SELECT * FROM MemberDetails WHERE PhoneNumber = ?";
        }

        try {
            Connection connection = connectToDatabase();
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, searchValue);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Display member details
                String details = "Member Details:\n" +
                        "ID: " + rs.getString("MemberID") + "\n" +
                        "Name: " + rs.getString("FirstName") + " " + rs.getString("Surname") + "\n" +
                        "Phone Number: " + rs.getString("PhoneNumber") + "\n" +
                        // Add more details as needed
                        "Email Address: " + rs.getString("EmailAddress") + "\n" +
                        "Address: " + rs.getString("StreetAddress") + ", " + rs.getString("City") + ", " + rs.getString("Province") + ", " + rs.getString("PostalCode");
                showAlert(Alert.AlertType.INFORMATION, "Member Details", details);
            } else {
                showAlert(Alert.AlertType.INFORMATION, "Not Found", "No member found with this value.");
            }

            rs.close();
            stmt.close();
            connection.close();
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to retrieve member details.");
        }
    }


    private void addFormField(GridPane grid, String labelText, int rowIndex) {
        Label label = new Label(labelText);
        grid.add(label, 0, rowIndex);
        TextField textField = new TextField();
        grid.add(textField, 1, rowIndex);
    }

    private void captureMemberDetails(GridPane grid) throws SQLException, DateTimeParseException {
        // Get details from the form
        String title = ((ComboBox<String>) grid.getChildren().get(1)).getValue();
        String gender = ((ComboBox<String>) grid.getChildren().get(3)).getValue();
        String firstName = ((TextField) grid.getChildren().get(5)).getText();
        String surname = ((TextField) grid.getChildren().get(7)).getText();
        memberID = ((TextField) grid.getChildren().get(9)).getText();  // Added memberID assignment
        String phoneNumber = ((TextField) grid.getChildren().get(11)).getText();
        String email = ((TextField) grid.getChildren().get(13)).getText();
        String employer = ((TextField) grid.getChildren().get(15)).getText();
        String streetAddress = ((TextField) grid.getChildren().get(17)).getText();
        String dateOfBirthString = ((TextField) grid.getChildren().get(19)).getText();
        String city = ((TextField) grid.getChildren().get(21)).getText();
        String province = ((TextField) grid.getChildren().get(23)).getText();
        String telephoneNumber = ((TextField) grid.getChildren().get(25)).getText();
        String alternativeNumber = ((TextField) grid.getChildren().get(27)).getText();
        String occupation = ((TextField) grid.getChildren().get(29)).getText();
        String postalCode = ((TextField) grid.getChildren().get(31)).getText();

        // Parse date of birth
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        java.time.LocalDate dateOfBirth = java.time.LocalDate.parse(dateOfBirthString, formatter);

        // Insert into database
        Connection connection = connectToDatabase();
        PreparedStatement stmt = connection.prepareStatement(
                "INSERT INTO Members (Title, Gender, FirstName, Surname, MemberID, PhoneNumber, Email, Employer, " +
                        "StreetAddress, DateOfBirth, City, Province, TelephoneNumber, AlternativeNumber, Occupation, PostalCode) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

        stmt.setString(1, title);
        stmt.setString(2, gender);
        stmt.setString(3, firstName);
        stmt.setString(4, surname);
        stmt.setString(5, memberID);
        stmt.setString(6, phoneNumber);
        stmt.setString(7, email);
        stmt.setString(8, employer);
        stmt.setString(9, streetAddress);
        stmt.setDate(10, java.sql.Date.valueOf(dateOfBirth));
        stmt.setString(11, city);
        stmt.setString(12, province);
        stmt.setString(13, telephoneNumber);
        stmt.setString(14, alternativeNumber);
        stmt.setString(15, occupation);
        stmt.setString(16, postalCode);

        stmt.executeUpdate();
        stmt.close();
        connection.close();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
