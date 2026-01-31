public class EvaluatorEntity {

    private String evaluatorId;
    private String evaluatorName;
    private String expertiseArea;
    private String affiliation;
    private String email;
    private String assignedPresentationType;
    private String filePath; // optional (e.g. evaluation form upload)

    // ===== No-argument constructor (recommended) =====
    public EvaluatorEntity() {
    }

    // ===== Constructor without file upload =====
    public EvaluatorEntity(String id, String name, String expertiseArea,
                           String affiliation, String email,
                           String assignedPresentationType) {

        this.evaluatorId = id;
        this.evaluatorName = name;
        this.expertiseArea = expertiseArea;
        this.affiliation = affiliation;
        this.email = email;
        this.assignedPresentationType = assignedPresentationType;
    }

    // ===== Constructor with file upload =====
    public EvaluatorEntity(String id, String name, String expertiseArea,
                           String affiliation, String email,
                           String assignedPresentationType, String filePath) {

        this(id, name, expertiseArea, affiliation, email, assignedPresentationType);
        this.filePath = filePath;
    }

    // ===== Getters =====
    public String getEvaluatorId() { return evaluatorId; }
    public String getEvaluatorName() { return evaluatorName; }
    public String getExpertiseArea() { return expertiseArea; }
    public String getAffiliation() { return affiliation; }
    public String getEmail() { return email; }
    public String getAssignedPresentationType() { return assignedPresentationType; }
    public String getFilePath() { return filePath; }

    // ===== Setters =====
    public void setEvaluatorId(String evaluatorId) { this.evaluatorId = evaluatorId; }
    public void setEvaluatorName(String evaluatorName) { this.evaluatorName = evaluatorName; }
    public void setExpertiseArea(String expertiseArea) { this.expertiseArea = expertiseArea; }
    public void setAffiliation(String affiliation) { this.affiliation = affiliation; }
    public void setEmail(String email) { this.email = email; }
    public void setAssignedPresentationType(String assignedPresentationType) {
        this.assignedPresentationType = assignedPresentationType;
    }
    public void setFilePath(String filePath) { this.filePath = filePath; }
}
