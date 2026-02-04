package studentregistrationapp;

/**
 *
 * @author anita
 */
public class Student {
    private String id;
    private String first;
    private String last;
    private String gender;
    private String dept;
    private String dob;
    private String email;

    public Student(String id, String first, String last,
                   String gender, String dept,
                   String dob, String email) {

        this.id = id;
        this.first = first;
        this.last = last;
        this.gender = gender;
        this.dept = dept;
        this.dob = dob;
        this.email = email;
    }

    public String toRecord() {
        return id + " | " + first + " " + last + " | " +
                gender + " | " + dept + " | " + dob + " | " + email;
    }

    public String getId() { return id; }
    public String getFirst() { return first; }
    public String getLast() { return last; }
    public String getGender() { return gender; }
    public String getDept() { return dept; }
    public String getDob() { return dob; }
    public String getEmail() { return email; }
}
