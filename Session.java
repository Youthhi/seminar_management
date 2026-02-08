public class Session {
    private String studentId, evaluatorId, date, venue, type;

    public Session(String sid, String eid, String date, String venue, String type) {
        this.studentId = sid;
        this.evaluatorId = eid;
        this.date = date;
        this.venue = venue;
        this.type = type;
    }

    // Getters
    public String getStudentId() { return studentId; }
    public String getEvaluatorId() { return evaluatorId; }
    public String getDate() { return date; }
    public String getVenue() { return venue; }
    public String getType() { return type; }

    @Override
    public String toString() {
        return studentId + "|" + evaluatorId + "|" + date + "|" + venue + "|" + type;
    }
}