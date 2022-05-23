package tracker;

import java.util.HashSet;
import java.util.Set;

public class Course {

    public Course(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;
    public Set<Student> getStudents() {
        return students;
    }

    public void setStudents(Set<Student> students) {
        this.students = students;
    }

    public void addStudent(Student student) {
        this.students.add(student);
    }


    public int getNum_submissions() {
        return num_submissions;
    }

    public void setNum_submissions(int num_submissions) {
        this.num_submissions = num_submissions;
    }

    public int getTotal_points() {
        return total_points;
    }

    public void setTotal_points(int total_points) {
        this.total_points = total_points;
    }

    public void parsePoint(int point, Student st) {
        if (point > 0) {
            this.students.add(st);
            this.num_submissions++;
            this.total_points += point;
        }
    }


    private Set<Student> students = new HashSet<>();
    private int num_submissions;
    private int total_points;

}
