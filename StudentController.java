import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class StudentController{

private Student student;    

public Student getStudentInfo(String studentID){


    try(BufferedReader br = new BufferedReader(new FileReader("student.txt"))){
        
        String line;
        while((line=br.readLine())!=null){

           String[]data = line.split("\\|");
           if(data[0].equals(studentID)){

            return new Student(
                data[0],
                data[1],
                data[2],
                data[3],
                data[4],
                data[5],
                data.length>6? data[6] : ""
            );
           }

        }

    }
    catch (FileNotFoundException e) {
        // THIS is what you want
        System.out.println("student.txt not found. No students registered yet.");}
    catch(IOException e){
             System.out.println("Fail to get studentinfo");
    }

    return student;


}

 public void updateStudentFile(String studentId, String newFilePath) {

    List<String> lines = new ArrayList<>();

    try (BufferedReader br = new BufferedReader(new FileReader("student.txt"))) {
        String line;

        while ((line = br.readLine()) != null) {

            // split into max 7 parts ONLY
            String[] data = line.split("\\|", 7);

            if (data.length < 7) {
                lines.add(line); // corrupted line, skip touching it
                continue;
            }

            if (data[0].equals(studentId)) {
                data[6] = newFilePath; // update ONLY filepath
                line = String.join("|", data);
            }

            lines.add(line);
        }

    } catch (IOException e) {
        System.out.println("Failed to obtain studentID");
        return;
    }

    try (PrintWriter pw = new PrintWriter(new FileWriter("student.txt"))) {
        for (String l : lines) {
            pw.println(l);
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
}
   

}





