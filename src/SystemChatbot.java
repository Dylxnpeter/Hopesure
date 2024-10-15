import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class SystemChatbot {

    private Map<String, String> responses;

    public SystemChatbot() {
        responses = new HashMap<>();

        // General Questions
        responses.put("log in", "To log in, enter your username and password on the login page. If you face any issues, contact support.");
        responses.put("reset password", "To reset your password, click on the 'Forgot Password' link and follow the instructions.");
        responses.put("main features", "The system's main features include Member Management, Claims Management, Reports, User Access Level Management, Work Performance Analysis, Receipting, and POS Device Management.");
        responses.put("navigate", "Use the top menu to navigate through different features. Hover over each category to see the available actions.");
        responses.put("user manual", "The user manual can be found under the 'Help' section in the top menu.");

        // Member Management
        responses.put("add member", "To add a new member, navigate to the 'Member Management' section and select 'Member Capture'. Fill in the details and click 'Submit'.");
        responses.put("search member", "To search for an existing member, go to 'Member Management' and select 'Member Enquiry'. Enter the search criteria and press 'Search'.");
        responses.put("update member", "To update member details, search for the member first, and then select 'Edit' to modify their details.");
        responses.put("capture member data", "Go to 'Member Management', select 'Member Capture', fill in the necessary details, and click 'Submit'.");
        responses.put("generate member report", "To generate member reports, go to 'Reports' and choose 'Member Reports'. Select the desired filters and click 'Generate'.");

        // Claims Management
        responses.put("file claim", "To file a new claim, go to the 'Claims Management' section and select 'File New Claim'. Enter the claim details and submit.");
        responses.put("check claim status", "To check the status of an existing claim, go to 'Claims Management' and select 'Claim Status'. Enter the claim ID to view details.");
        responses.put("update claim", "To update claim information, search for the claim first, then select 'Edit' to modify the details.");
        responses.put("generate claims report", "To generate claims-related reports, go to 'Reports' and select 'Claims Reports'. Choose the desired filters and generate the report.");

        // Reports
        responses.put("generate reports", "To generate reports, navigate to the 'Reports' section, select the report type, choose filters, and click 'Generate'.");
        responses.put("filter reports", "In the 'Reports' section, you can filter by date, status, and other criteria before generating the report.");
        responses.put("payment status report", "To generate a payment status report, go to 'Reports' -> 'Payment Status'. Select the status and date range, then click 'Generate'.");
        responses.put("policy expiry report", "To generate a policy expiry report, go to 'Reports' -> 'Policy Expiry'. Select the start and end dates, then click 'Generate'.");

        // User Access Level
        responses.put("set access level", "To set an agent's access level, go to 'User Management' -> 'User Access Level'. Select the agent and assign an access level.");
        responses.put("modify access level", "To modify an agent's access level, search for the agent in 'User Access Level', and adjust their level accordingly.");
        responses.put("job titles access levels", "Job titles correspond to different access levels. For example, a 'Manager' might have level 90 access, while a 'Clerk' might have level 20.");

        // Work Performance
        responses.put("check performance", "To check an agent's work performance, go to 'Work Performance'. Select the agent and time period to generate a performance report.");
        responses.put("generate performance report", "In 'Work Performance', select the agent and time period, then click 'Generate' to view the performance analysis.");

        // Receipting
        responses.put("manual receipt", "To manually capture a receipt, go to 'Receipting' -> 'Manual Receipt Capture'. Enter the receipt number and other details, then click 'Submit'.");
        responses.put("reprint receipt", "To reprint a receipt, go to 'Receipting' -> 'Reprint Receipt'. Enter the receipt number to generate a new copy.");
        responses.put("record reprint", "All reprinted receipts are automatically recorded in the system for audit purposes.");

        // POS Device Management
        responses.put("add pos", "To add a new POS device, go to 'POS Device Management' -> 'Add POS'. Enter the bank, serial number, and other details, then click 'Submit'.");
        responses.put("manage pos", "To manage existing POS devices, go to 'POS Device Management' -> 'Manage POS'. Select a device from the list and edit its details as needed.");
    }

    private String getBestMatch(String userInput) {
        String bestMatch = null;
        int highestScore = 0;

        for (String keyword : responses.keySet()) {
            int score = getSimilarityScore(userInput, keyword);
            if (score > highestScore) {
                highestScore = score;
                bestMatch = keyword;
            }
        }
        return bestMatch;
    }

    private int getSimilarityScore(String userInput, String keyword) {
        // Simple similarity measure: count the number of matching words
        int score = 0;
        String[] userWords = userInput.split("\\s+");
        String[] keywordWords = keyword.split("\\s+");

        for (String userWord : userWords) {
            for (String keywordWord : keywordWords) {
                if (userWord.equalsIgnoreCase(keywordWord)) {
                    score++;
                }
            }
        }
        return score;
    }

    public String getResponse(String userInput) {
        userInput = userInput.toLowerCase();
        String bestMatch = getBestMatch(userInput);

        if (bestMatch != null) {
            return responses.get(bestMatch);
        } else {
            return "I'm sorry, I don't understand the question. Please try asking in a different way or contact support.";
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        SystemChatbot chatbot = new SystemChatbot();

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
}
