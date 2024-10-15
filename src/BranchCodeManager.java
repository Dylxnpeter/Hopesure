import java.util.HashMap;
import java.util.Map;

public class BranchCodeManager {
    private static final Map<String, String> BRANCH_CODES = new HashMap<>();

    static {
        BRANCH_CODES.put("Klerksdorp", "KLE");
        BRANCH_CODES.put("Mthatha", "MTH");
        BRANCH_CODES.put("Empangeni", "EMP");
        BRANCH_CODES.put("Taung", "TAU");
        BRANCH_CODES.put("Johannesburg", "JBG");
        BRANCH_CODES.put("Rustenburg", "RBG");
        BRANCH_CODES.put("Zeerust", "ZEE");
        BRANCH_CODES.put("New Castle", "NEW");
        BRANCH_CODES.put("Harts Water", "HWA");
        BRANCH_CODES.put("Botshabelo", "BTB");
        BRANCH_CODES.put("Mafikeng", "MFK");
    }

    public static String getBranchCode(String branchName) {
        return BRANCH_CODES.getOrDefault(branchName, "UNKNOWN");
    }
}

