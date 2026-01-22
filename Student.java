public class Student {
    private String student_id;
    private String student_name;
    private String researchTitle;
    private String abstractText;
    private String supervisor;
    private String presentationType;
    private String filePath; // optional, only when upload happens

    public Student(String id,String name, String researchTitle, String abstractText,
                   String supervisor, String presentationType) {
         
        this.student_id = id;            
        this.student_name = name;
        this.researchTitle = researchTitle;
        this.abstractText = abstractText;
        this.supervisor = supervisor;
        this.presentationType = presentationType;
    }

    public Student(String id,String name, String researchTitle, String abstractText,
                   String supervisor, String presentationType,String filepath) {
         
        this.student_id = id;            
        this.student_name = name;
        this.researchTitle = researchTitle;
        this.abstractText = abstractText;
        this.supervisor = supervisor;
        this.presentationType = presentationType;
        this.filePath = filepath;
    }




    // getters and setters
    public String getStudentID() {return student_id;};
    public String getName() { return student_name; }
    public String getResearchTitle() { return researchTitle; }
    public String getAbstractText() { return abstractText; }
    public String getSupervisor() { return supervisor; }
    public String getPresentationType() { return presentationType; }

    public void setFilePath(String filePath) { this.filePath = filePath; }
    public String getFilePath() { return filePath; }
}
