package tracker;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Student {
    private int id;
    private String email;
    private int javaPoints;
    private int dsaPoints;
    private int dbPoints;

    public Student(int id, String email, String firstName, String lastName) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    Set<String> notifyCourses = new HashSet<>();

    public Student(int id, String email) {
        this.id = id;
        this.email = email;
    }

    public Student(int id, String email, int javaPoints, int dsaPoints, int dbPoints, int springPoints) {
        this.id = id;
        this.email = email;
        this.javaPoints = javaPoints;
        this.dsaPoints = dsaPoints;
        this.dbPoints = dbPoints;
        this.springPoints = springPoints;
    }

    public void notifyCourse(String course) {
        notifyCourses.add(course);
    }

    public Set<String> notifiesCourses() {
        return notifyCourses;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getJavaPoints() {
        return javaPoints;
    }

    public void setJavaPoints(int javaPoints) {
        this.javaPoints = javaPoints;
    }

    public int getDsaPoints() {
        return dsaPoints;
    }

    public void setDsaPoints(int dsaPoints) {
        this.dsaPoints = dsaPoints;
    }

    public int getDbPoints() {
        return dbPoints;
    }

    public void setDbPoints(int dbPoints) {
        this.dbPoints = dbPoints;
    }

    public int getSpringPoints() {
        return springPoints;
    }

    public void setSpringPoints(int springPoints) {
        this.springPoints = springPoints;
    }

    private int springPoints;

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    private String firstName;
    private String lastName;

}
