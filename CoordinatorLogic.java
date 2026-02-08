import java.io.*;
import java.util.*;

public class CoordinatorLogic {

    public List<Object[]> getSessionData() {
        List<Object[]> data = new ArrayList<>();
        File file = new File("sessions.txt");
        if (!file.exists()) return data;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] d = line.trim().split("\\|");
                if (d.length >= 5) data.add(new Object[]{d[0], d[1], d[4], d[2], d[3]});
            }
        } catch (IOException e) { e.printStackTrace(); }
        return data;
    }

// Ensure this is the ONLY deleteSession method in this file
public void deleteSession(String sidToDelete) throws IOException {
    File file = new File("sessions.txt");
    StringBuilder sb = new StringBuilder();

    if (!file.exists()) return;

    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
        String line;
        while ((line = br.readLine()) != null) {
            String[] d = line.split("\\|");
            // Only keep the line if the ID does NOT match
            if (d.length > 0 && !d[0].equals(sidToDelete)) {
                sb.append(line).append("\n");
            }
        }
    }

    // False here means overwrite the file
    try (PrintWriter out = new PrintWriter(new FileWriter(file, false))) {
        out.print(sb.toString());
    }
}

    public boolean isValidEvaluator(String eid) {
        try (BufferedReader br = new BufferedReader(new FileReader("evaluators.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith(eid + "|")) return true;
            }
        } catch (IOException e) { return true; }
        return false;
    }

    public String getFullReport() {
    StringBuilder report = new StringBuilder("--- FULL SEMINAR EVALUATION REPORT ---\n\n");
    Map<String, Integer> scores = new HashMap<>();
    Map<String, Integer> votes = new HashMap<>();
    
    // 1. Load Scores
    try (BufferedReader br = new BufferedReader(new FileReader("evaluations.txt"))) {
        String line;
        while ((line = br.readLine()) != null) {
            String[] d = line.split("\\|");
            if (d.length >= 2) scores.put(d[0], Integer.parseInt(d[1]));
        }
    } catch (IOException e) {}

    // 2. Load Votes
    try (BufferedReader br = new BufferedReader(new FileReader("votes.txt"))) {
        String line;
        while ((line = br.readLine()) != null) {
            String id = line.trim();
            votes.put(id, votes.getOrDefault(id, 0) + 1);
        }
    } catch (IOException e) {}

    // 3. Process Awards (Oral/Poster)
    String bestOral = "None", bestPoster = "None";
    int topOral = -1, topPoster = -1;

    try (BufferedReader br = new BufferedReader(new FileReader("student.txt"))) {
        String line;
        while ((line = br.readLine()) != null) {
            String[] d = line.split("\\|");
            if (d.length < 6) continue;
            String id = d[0], name = d[1], type = d[5];
            int s = scores.getOrDefault(id, 0);

            if (type.equalsIgnoreCase("Oral") && s > topOral) {
                topOral = s; bestOral = name + " (ID: " + id + ")";
            } else if (type.equalsIgnoreCase("Poster") && s > topPoster) {
                topPoster = s; bestPoster = name + " (ID: " + id + ")";
            }
            report.append(String.format("ID: %-5s | Name: %-15s | Score: %d/40\n", id, name, s));
        }
    } catch (IOException e) { return "Error: student.txt missing"; }

    // 4. Final Formatting
    report.append("\n" + "=".repeat(35) + "\n ✨ 🏆 OFFICIAL WINNERS 🏆 ✨ \n" + "=".repeat(35) + "\n");
    report.append("⭐ BEST ORAL: " + bestOral.toUpperCase() + " (" + topOral + "/40)\n");
    report.append("⭐ BEST POSTER: " + bestPoster.toUpperCase() + " (" + topPoster + "/40)\n");
    
    return report.toString();
}

public void updateSession(String sid, String newEid, String newDate, String newVenue, String newType) throws IOException {
    File file = new File("sessions.txt");
    StringBuilder allData = new StringBuilder();
    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
        String line;
        while ((line = br.readLine()) != null) {
            String[] d = line.split("\\|");
            if (d[0].equals(sid)) {
                allData.append(sid).append("|").append(newEid).append("|")
                       .append(newDate).append("|").append(newVenue).append("|")
                       .append(newType).append("\n");
            } else {
                allData.append(line).append("\n");
            }
        }
    }
    try (PrintWriter out = new PrintWriter(new FileWriter(file, false))) {
        out.print(allData.toString());
    }
}

public String getConsolidatedStatus() {
    StringBuilder sb = new StringBuilder("--- CONSOLIDATED SEMINAR STATUS ---\n");
    Map<String, String[]> students = new HashMap<>(); 
    Map<String, Integer> evaluations = new HashMap<>(); 

    try {
        // 1. Read Student Names
        BufferedReader brS = new BufferedReader(new FileReader("student.txt"));
        String line;
        while ((line = brS.readLine()) != null) {
            String[] d = line.split("\\|");
            if (d.length >= 2) students.put(d[0], new String[]{d[1]});
        }
        brS.close();

        // 2. Read Scores
        File evalFile = new File("evaluations.txt");
        if (evalFile.exists()) {
            BufferedReader brE = new BufferedReader(new FileReader(evalFile));
            while ((line = brE.readLine()) != null) {
                String[] d = line.split("\\|");
                if (d.length >= 2) evaluations.put(d[0], Integer.parseInt(d[1]));
            }
            brE.close();
        }

        // 3. Combine
        for (String id : students.keySet()) {
            String name = students.get(id)[0];
            int score = evaluations.getOrDefault(id, -1);
            String status = (score >= 0) ? "GRADED" : "WAITING";
            String scoreStr = (score >= 0) ? score + "/40" : "N/A";
            
            sb.append(String.format("[%s] ID: %-5s | Name: %-15s | Score: %s\n", 
                                   status, id, name, scoreStr));
        }
    } catch (IOException e) {
        return "Backend Error: " + e.getMessage();
    }
    return sb.toString();
}

public void updateSessionInFile(String sid, String newEid, String newDate, String newVenue, String newType) throws IOException {
    File file = new File("sessions.txt");
    StringBuilder allData = new StringBuilder();

    try (BufferedReader br = new BufferedReader(new FileReader(file))) {
        String line;
        while ((line = br.readLine()) != null) {
            if (line.trim().isEmpty()) continue;
            String[] d = line.split("\\|");
            if (d[0].equals(sid)) {
                // This is the updated line
                allData.append(sid).append("|").append(newEid).append("|")
                       .append(newDate).append("|").append(newVenue).append("|")
                       .append(newType).append("\n");
            } else {
                allData.append(line).append("\n");
            }
        }
    }
    
    // Write the whole thing back
    try (PrintWriter out = new PrintWriter(new FileWriter(file, false))) {
        out.print(allData.toString());
    }
}

}