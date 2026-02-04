package studentregistrationapp;
import javax.swing.*;
import java.awt.*;
import java.time.*;
import java.util.regex.*;

/**
 *
 * @author anita
 */
public class StudentRegistrationApp extends JFrame {

    JTextField txtFirst, txtLast, txtEmail, txtConfirmEmail;
    JPasswordField txtPassword, txtConfirmPassword;
    JComboBox<Integer> cmbYear, cmbDay;
    JComboBox<String> cmbMonth, cmbDept;
    JRadioButton rbMale, rbFemale;
    JTextArea txtOutput;

    DatabaseHelper db = new DatabaseHelper();

    public StudentRegistrationApp() {

        setTitle("Student Registration");
        setSize(700, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel form = new JPanel(new GridLayout(12,2,5,5));

        txtFirst = new JTextField();
        txtLast = new JTextField();
        txtEmail = new JTextField();
        txtConfirmEmail = new JTextField();
        txtPassword = new JPasswordField();
        txtConfirmPassword = new JPasswordField();

        cmbYear = new JComboBox<>();
        cmbMonth = new JComboBox<>(new String[]{
                "January","February","March","April","May","June",
                "July","August","September","October","November","December"});
        cmbDay = new JComboBox<>();

        cmbDept = new JComboBox<>(new String[]{
                "Civil","CSE","Electrical","E&C","Mechanical"});

        rbMale = new JRadioButton("Male");
        rbFemale = new JRadioButton("Female");

        ButtonGroup group = new ButtonGroup();
        group.add(rbMale);
        group.add(rbFemale);

        for(int y = 1960; y <= LocalDate.now().getYear(); y++)
            cmbYear.addItem(y);

        updateDays();

        cmbYear.addActionListener(e -> updateDays());
        cmbMonth.addActionListener(e -> updateDays());

        form.add(new JLabel("First Name"));
        form.add(txtFirst);

        form.add(new JLabel("Last Name"));
        form.add(txtLast);

        form.add(new JLabel("Email"));
        form.add(txtEmail);

        form.add(new JLabel("Confirm Email"));
        form.add(txtConfirmEmail);

        form.add(new JLabel("Password"));
        form.add(txtPassword);

        form.add(new JLabel("Confirm Password"));
        form.add(txtConfirmPassword);

        form.add(new JLabel("DOB Year"));
        form.add(cmbYear);

        form.add(new JLabel("DOB Month"));
        form.add(cmbMonth);

        form.add(new JLabel("DOB Day"));
        form.add(cmbDay);

        JPanel genderPanel = new JPanel();
        genderPanel.add(rbMale);
        genderPanel.add(rbFemale);

        form.add(new JLabel("Gender"));
        form.add(genderPanel);

        form.add(new JLabel("Department"));
        form.add(cmbDept);

        JButton btnSubmit = new JButton("Submit");
        JButton btnCancel = new JButton("Cancel");

        btnSubmit.addActionListener(e -> submitForm());
        btnCancel.addActionListener(e -> clearForm());

        form.add(btnSubmit);
        form.add(btnCancel);

        add(form, BorderLayout.WEST);

        txtOutput = new JTextArea();
        txtOutput.setEditable(false);
        txtOutput.setBorder(BorderFactory.createTitledBorder("Your Data is Below:"));

        add(new JScrollPane(txtOutput), BorderLayout.CENTER);
    }

    private void updateDays() {

        cmbDay.removeAllItems();

        int year = (Integer)cmbYear.getSelectedItem();
        int month = cmbMonth.getSelectedIndex() + 1;

        int days = YearMonth.of(year, month).lengthOfMonth();

        for(int i=1;i<=days;i++)
            cmbDay.addItem(i);
    }

    private void submitForm() {

        String first = txtFirst.getText().trim();
        String last = txtLast.getText().trim();
        String email = txtEmail.getText().trim();
        String confirmEmail = txtConfirmEmail.getText().trim();
        String pass = new String(txtPassword.getPassword());
        String confirmPass = new String(txtConfirmPassword.getPassword());

        if(first.isEmpty() || last.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this,"All fields required");
            return;
        }

        if(!email.equals(confirmEmail)) {
            JOptionPane.showMessageDialog(this,"Emails do not match");
            return;
        }

        if(!Pattern.matches("^[A-Za-z0-9+_.-]+@(.+)$", email)) {
            JOptionPane.showMessageDialog(this,"Invalid Email");
            return;
        }

        if(!pass.equals(confirmPass)) {
            JOptionPane.showMessageDialog(this,"Passwords do not match");
            return;
        }

        if(pass.length()<8 || pass.length()>20 ||
                !pass.matches(".*[A-Za-z].*") ||
                !pass.matches(".*\\d.*")) {

            JOptionPane.showMessageDialog(this,"Weak password");
            return;
        }

        String gender = rbMale.isSelected() ? "M" :
                rbFemale.isSelected() ? "F" : "";

        if(gender.isEmpty()) {
            JOptionPane.showMessageDialog(this,"Select gender");
            return;
        }

        int year = (Integer)cmbYear.getSelectedItem();
        int month = cmbMonth.getSelectedIndex()+1;
        int day = (Integer)cmbDay.getSelectedItem();

        LocalDate dob = LocalDate.of(year,month,day);

        int age = Period.between(dob, LocalDate.now()).getYears();

        if(age < 16 || age > 60) {
            JOptionPane.showMessageDialog(this,"Age must be 16-60");
            return;
        }

        String dept = cmbDept.getSelectedItem().toString();

        String id = db.generateID();

        Student student = new Student(id, first, last, gender,
                dept, dob.toString(), email);

        db.insertStudent(student);

        txtOutput.append(student.toRecord() + "\n");

        CSVHelper.save(student.toRecord());

        JOptionPane.showMessageDialog(this,"Saved Successfully");
    }

    private void clearForm() {
        txtFirst.setText("");
        txtLast.setText("");
        txtEmail.setText("");
        txtConfirmEmail.setText("");
        txtPassword.setText("");
        txtConfirmPassword.setText("");
    }

    public static void main(String[] args) {
        new StudentRegistrationApp().setVisible(true);
    }
}

