package tracker;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class Main {


    enum StateProgram {
        BEGIN, ADD, ADD_POINTS, FIND, STATISTICS
    }
    private final double JAVA_POINTS = 600.0;
    private final double DSA_POINTS = 400.0;
    private final double DB_POINTS = 480.0;
    private final double SPRING_POINTS = 550.0;

    Set<String> emails = new HashSet<>();
    Map<Integer, Student> ids = new HashMap<>();
    AtomicInteger atomicInteger = new AtomicInteger();

    List<Course> courses = new ArrayList<>(List.of(new Course("Java"), new Course("DSA"),
            new Course("Databases"), new Course("Spring")));

    public static boolean checkSpecCharacters(String name) {
        if(name.matches(".+[\\'\\-]+[\\-\\']+.+")) {
            return false;
        }
        return true;
    }

    public static boolean validateName(String name) {
        if (checkSpecCharacters(name)) {
            if (name.matches("^[a-zA-Z][a-zA-Z\\-\\']*[a-zA-Z]$")) {
                return true;
            }
        }
        System.out.println("Incorrect first name.");
        return false;
    }

    public static boolean validateSurname(String surname) {
        if (checkSpecCharacters(surname)) {
            if (surname.matches("^[a-zA-Z][a-zA-Z\\-\\'\\s]*[a-zA-Z]$")) {
                return true;
            }
        }
        System.out.println("Incorrect last name.");
        return false;
    }

    public static boolean checkEmail(String email) {
        if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z0-9]+$")) {
            System.out.println("Incorrect email.");
            return false;
        }
        return true;
    }

    public int processCredentials(String line) {

        String parts[] = line.split(" ");
        List<String> list = new ArrayList<>(List.of(parts));
        if (parts.length < 3) {
            System.out.println("Incorrect credentials.");
            return 0;
        }
        String email = parts[parts.length - 1];
        if (!checkEmail(email)) {
            return 0;
        }
        if (emails.contains(email)) {
            System.out.println("This email is already taken.");
            return 0;
        }
        String name = parts[0];
        if(!validateName(name)) {
            return 0;
        }
        list.remove(list.size() - 1);
        list.remove(0);
        String surname = String.join("", list);//Arrays.toString(list.toArray());
        if(!validateSurname(surname)) {
            return 0;
        }
        System.out.println("The student has been added.");
        emails.add(email);
        int id = atomicInteger.addAndGet(1);
        ids.put(id, new Student(id, email, name, surname));
        return 1;
    }


    public int processPoints(String line) {
        String[] points = line.split(" ");
        if (points.length != 5) {
            System.out.println("Incorrect points format");
            return 0;
        }
        for (int i = 1; i < points.length; i++) {
            try {
                if (Integer.parseInt(points[i]) < 0) {
                    System.out.println("Incorrect points format");
                    return 0;
                }
            } catch (NumberFormatException ex) {
                System.out.println("Incorrect points format");
                return 0;
            }
        }
        int id = 0;
        try {
            id = Integer.parseInt(points[0]);
        } catch  (NumberFormatException ex) {
            System.out.println("No student is found for id=" + points[0]);
            return 0;
        }
        if (!ids.containsKey(id)) {
            System.out.println("No student is found for id=" + id);
            return 0;
        }
        Student st = ids.get(id);
        st.setJavaPoints(st.getJavaPoints() + Integer.parseInt(points[1]));
        st.setDsaPoints(st.getDsaPoints() + Integer.parseInt(points[2]));
        st.setDbPoints(st.getDbPoints() + Integer.parseInt(points[3]));
        st.setSpringPoints(st.getSpringPoints() + Integer.parseInt(points[4]));

        courses.get(0).parsePoint(Integer.parseInt(points[1]), st);
        courses.get(1).parsePoint(Integer.parseInt(points[2]), st);
        courses.get(2).parsePoint(Integer.parseInt(points[3]), st);
        courses.get(3).parsePoint(Integer.parseInt(points[4]), st);

        System.out.println("Points updated");

        return 1;
    }

    public void find(String cmd) {
        try {
            if (ids.containsKey(Integer.parseInt(cmd))) {
                Student st = ids.get(Integer.parseInt(cmd));
                System.out.printf("%s points: Java=%d; DSA=%d; Databases=%d; Spring=%d",
                        cmd, st.getJavaPoints(), st.getDsaPoints(), st.getDbPoints(), st.getSpringPoints());
            } else {
                System.out.println("No student is found for id=" + cmd);
            }
        } catch (NumberFormatException ex) {
            System.out.println("No student is found for id=" + cmd);
        }
    }

    public List<String> getTop(Map<String, Integer> map) {
        int max = 0;
        List<String> most = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            if (entry.getValue() > max) {
                most.clear();
                most.add(entry.getKey());
                max = entry.getValue();
            } else if (entry.getValue() > 0 && entry.getValue() == max) {
                most.add(entry.getKey());
            }
        }
        return most;
    }
    public List<String> getLow(Map<String, Integer> map) {
        List<String> top = getTop(map);
        List<String> minPop = new ArrayList<>();
        int min = Integer.MAX_VALUE;
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            if (entry.getValue() == 0) {
                continue;
            }
            if (!top.contains(entry.getKey()) && entry.getValue() > 0) {
                if (entry.getValue() < min) {
                    minPop.clear();
                    minPop.add(entry.getKey());
                    min = entry.getValue();
                } else if (entry.getValue() == min) {
                    minPop.add(entry.getKey());
                }
            }
        }
        return minPop;

    }

    public String printList(List<String> list) {
        String str = "";
        if (list.size() == 0) {
            str = "n/a";
        } else {
            for (String strCourse : list) {
                str += strCourse + ", ";
            }
            str = str.substring(0, str.length() - 2);
        }
        return str;
    }

    public void printPoints(String cmd) {
        List<Student> students = new ArrayList<>(ids.values());
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        DecimalFormat df = new DecimalFormat("0.0", symbols);
        df.setRoundingMode(RoundingMode.HALF_UP);
        if("java".equals(cmd.toLowerCase())) {
            System.out.println("Java");
            System.out.println("id     points completed");
            Comparator<Student> javaPointsComparator = (st1, st2) -> {
                if (st1.getJavaPoints() == st2.getJavaPoints()) {
                    return Integer.compare(st1.getId(), st2.getId());
                }
                return Integer.compare(st2.getJavaPoints(), st1.getJavaPoints());
            };
            students.sort(javaPointsComparator);
            for (Student st : students) {
                if (st.getJavaPoints() > 0) {
                    System.out.printf("%d %d    %s%%\n", st.getId(), st.getJavaPoints(),
                            df.format(st.getJavaPoints() / JAVA_POINTS * 100.0));
                }
            }
        } else if ("dsa".equals(cmd.toLowerCase())) {
            System.out.println("DSA");
            System.out.println("id     points completed");
            Comparator<Student> dsaPointsComparator = (st1, st2) -> {
                if (st1.getDsaPoints() == st2.getDsaPoints()) {
                    return Integer.compare(st1.getId(), st2.getId());
                }
                return Integer.compare(st2.getDsaPoints(), st1.getDsaPoints());
            };
            students.sort(dsaPointsComparator);
            for (Student st : students) {
                if (st.getDsaPoints() > 0) {
                    System.out.printf("%d %d    %s%%\n", st.getId(), st.getDsaPoints(),
                            df.format(st.getDsaPoints() / DSA_POINTS * 100.0));
                }
            }
        } else if ("databases".equals(cmd.toLowerCase())) {
            System.out.println("Databases");
            System.out.println("id     points completed");
            Comparator<Student> dbPointsComparator = (st1, st2) -> {
                if (st1.getDbPoints() == st2.getDbPoints()) {
                    return Integer.compare(st1.getId(), st2.getId());
                }
                return Integer.compare(st2.getDbPoints(), st1.getDbPoints());
            };
            students.sort(dbPointsComparator);
            for (Student st : students) {
                if (st.getDbPoints() > 0) {
                    System.out.printf("%d %d    %s%%\n", st.getId(), st.getDbPoints(),
                            df.format(st.getDbPoints() / DB_POINTS * 100.0));
                }
            }
        } else if ("spring".equals(cmd.toLowerCase())) {
            System.out.println("Spring");
            System.out.println("id     points completed");
            Comparator<Student> springPointsComparator = (st1, st2) -> {
                if (st1.getSpringPoints() == st2.getSpringPoints()) {
                    return Integer.compare(st1.getId(), st2.getId());
                }
                return Integer.compare(st2.getSpringPoints(), st1.getSpringPoints());
            };
            students.sort(springPointsComparator);
            for (Student st : students) {
                if (st.getSpringPoints() > 0) {
                    System.out.printf("%d %d    %s%%\n", st.getId(), st.getSpringPoints(),
                            df.format(st.getSpringPoints() / SPRING_POINTS * 100.0));
                }
            }
        } else {
            System.out.println("Unknown course.");
        }
    }


    public void printStat() {
        Map<String, Integer> mapPopularity = new HashMap<>();
        Map<String, Integer> mapActivity = new HashMap<>();
        Map<String, Integer> mapHard = new HashMap<>();
        for (Course course : courses) {
            mapPopularity.put(course.getName(), course.getStudents().size());
            mapActivity.put(course.getName(), course.getNum_submissions());
            if (course.getNum_submissions() == 0) {
                mapHard.put(course.getName(), 0);
            } else {
                mapHard.put(course.getName(), course.getTotal_points() * 1000000 / course.getNum_submissions());
            }
        }
        System.out.println("Most popular: " + printList(getTop(mapPopularity)));
        System.out.println("Least popular: " + printList(getLow(mapPopularity)));
        System.out.println("Highest activity: " + printList(getTop(mapActivity)));
        System.out.println("Lowest activity: " + printList(getLow(mapActivity)));
        System.out.println("Easiest course: " +  printList(getTop(mapHard)));
        System.out.println("Hardest course: " + printList(getLow(mapHard)));
    }

    public void printList() {
        if (ids.isEmpty()) {
            System.out.println("No students found");
        } else {
            System.out.println("Students:");
            for (Integer val : ids.keySet()) {
                System.out.println(val);
            }
        }
    }

    public void printNotify(Student st, String course) {
        System.out.printf("To: %s\nRe: Your Learning Progress\nHello, %s %s! You have accomplished our %s course!\n",
                st.getEmail(), st.getFirstName(), st.getLastName(), course);
    }
    public void notifyStudents() {
        int count = 0;

        for (Student st : ids.values()) {
            boolean notified = false;
            if (st.getJavaPoints() >= JAVA_POINTS && !st.notifyCourses.contains("Java")) {
                notified = true;
                st.notifyCourse("Java");
                printNotify(st, "Java");
            }
            if (st.getDbPoints() >= DB_POINTS && !st.notifyCourses.contains("Databases")) {
                notified = true;
                st.notifyCourse("Databases");
                printNotify(st, "Databases");
            }
            if (st.getDsaPoints() >= DSA_POINTS && !st.notifyCourses.contains("DSA")) {
                notified = true;
                st.notifyCourse("DSA");
                printNotify(st, "DSA");
            }
            if (st.getSpringPoints() >= SPRING_POINTS && !st.notifyCourses.contains("Spring")) {
                notified = true;
                st.notifyCourse("Spring");
                printNotify(st, "Spring");
            }
            if (notified) {
                count++;
            }
        }
        System.out.printf("Total %d students have been notified.\n", count);
    }

    public static void main(String[] args) {
        System.out.println("Learning Progress Tracker");
        Scanner scanner = new Scanner(System.in);
        int students = 0;
        StateProgram state = StateProgram.BEGIN;
        Main main = new Main();
        while (true) {
            String cmd = scanner.nextLine();
            if (state == StateProgram.ADD) {
                if ("back".equals(cmd)) {
                    System.out.printf("Total %d students have been added.\n", students);
                    state = StateProgram.BEGIN;
                } else {
                    students += main.processCredentials(cmd);
                }
            } else if (state == StateProgram.ADD_POINTS) {
                if ("back".equals(cmd)) {
                    state = StateProgram.BEGIN;
                } else {
                    main.processPoints(cmd);
                }
            } else if (state == StateProgram.FIND) {
                if ("back".equals(cmd)) {
                    state = StateProgram.BEGIN;
                } else {
                    main.find(cmd);
                }
            } else if (state == StateProgram.STATISTICS) {
                if ("back".equals(cmd)) {
                    state = StateProgram.BEGIN;
                } else {
                    main.printPoints(cmd);
                }
            } else {
                if (cmd.trim().isEmpty()) {
                    System.out.println("No input.");
                } else if ("back".equals(cmd)) {
                    System.out.println("Enter 'exit' to exit the program");
                } else if ("exit".equals(cmd)) {
                    System.out.println("Bye!");
                    break;
                } else if ("add students".equals(cmd)) {
                    state = StateProgram.ADD;
                    System.out.println("Enter student credentials or 'back' to return");
                } else if("add points".equals(cmd)) {
                    state = StateProgram.ADD_POINTS;
                    System.out.println("Enter an id and points or 'back' to return");
                } else if ("find".equals(cmd)) {
                    System.out.println("Enter an id and points or 'back' to return");
                    state = StateProgram.FIND;
                } else if ("list".equals(cmd)) {
                    main.printList();
                } else if ("notify".equals(cmd)) {
                    main.notifyStudents();
                } else if ("statistics".equals(cmd)) {
                    System.out.println("Type the name of a course to see details or 'back' to quit");
                    state = StateProgram.STATISTICS;
                    main.printStat();
                } else {
                    System.out.println("Error: unknown command!");
                }
            }
        }
    }
}
