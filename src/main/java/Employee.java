import java.time.LocalDate;
import java.util.Date;

public class Employee {

    private String lastname, firstname;
    private LocalDate birthdate;
    private Integer salary, dept_no;
    private Character gender;

    public Employee(String lastname, String firstname, LocalDate birthdate, Integer salary, Integer dept_no, Character gender) {
        this.lastname = lastname;
        this.firstname = firstname;
        this.birthdate = birthdate;
        this.salary = salary;
        this.dept_no = dept_no;
        this.gender = gender;
    }


    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public Integer getSalary() {
        return salary;
    }

    public void setSalary(Integer salary) {
        this.salary = salary;
    }

    public Integer getDept_no() {
        return dept_no;
    }

    public void setDept_no(Integer dept_no) {
        this.dept_no = dept_no;
    }

    public Character getGender() {
        return gender;
    }

    public void setGender(Character gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return "Employee{" +
                ", lastname='" + lastname + '\'' +
                ", firstname='" + firstname + '\'' +
                ", birthdate=" + birthdate +
                ", salary=" + salary +
                ", dept_no=" + dept_no +
                ", gender='" + gender + '\'' +
                '}';
    }
}
