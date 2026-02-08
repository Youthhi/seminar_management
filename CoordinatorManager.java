import java.io.*;
import java.util.*;

public class CoordinatorManager {
    private final String SESSION_FILE = "sessions.txt";

    public List<Session> getAllSessions() {
        List<Session> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(SESSION_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] d = line.split("\\|");
                if (d.length >= 5) {
                    list.add(new Session(d[0], d[1], d[2], d[3], d[4]));
                }
            }
        } catch (IOException e) { /* File might not exist yet */ }
        return list;
    }

    public void saveOrUpdateSession(Session session, boolean isUpdate) {
        List<Session> sessions = getAllSessions();
        if (isUpdate) {
            sessions.removeIf(s -> s.getStudentId().equals(session.getStudentId()));
        }
        sessions.add(session);
        writeAll(sessions);
    }

    public void deleteSession(String sid) {
        List<Session> sessions = getAllSessions();
        sessions.removeIf(s -> s.getStudentId().equals(sid));
        writeAll(sessions);
    }

    private void writeAll(List<Session> sessions) {
        try (PrintWriter out = new PrintWriter(new FileWriter(SESSION_FILE))) {
            for (Session s : sessions) out.println(s.toString());
        } catch (IOException e) { e.printStackTrace(); }
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
}