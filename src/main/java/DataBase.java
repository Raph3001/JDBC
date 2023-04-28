import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class DataBase {

    private static DataBase db_access;
    private Connection connection;

    private DataBase() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void createDatabase(String dbName) {

        try {
            Statement statement = connection.createStatement();
            String sql = "DROP DATABASE " + dbName;
            statement.executeUpdate(sql);
            statement.close();
        } catch (Exception e) {

        }

        try {
            Statement statement = connection.createStatement();
            String sql = "CREATE DATABASE " + dbName;
            statement.executeUpdate(sql);
            statement.close();
        } catch (Exception e) {

        }
        System.out.println("DB " + dbName + " created successfully");
    }

    public void createTable() throws SQLException {
        String sqlSt = "CREATE TABLE employee (emp_no SERIAL PRIMARY KEY, lastname VARCHAR(50), firstname VARCHAR(50), birthdate DATE, salary NUMERIC, dept_no NUMERIC, gender CHAR(1))";

        try {
            Statement statement = connection.createStatement();
            String sql = "DROP TABLE employee";
            statement.executeUpdate(sql);
            statement.close();
        } catch (Exception e) {

        }

        int datasets = 0;
        try {
            Statement statement = connection.createStatement();
            datasets = statement.executeUpdate(sqlSt);
            statement.close();
        } catch (Exception e) {

        }
    }

    public void connect(String databaseName) throws SQLException {
        disconnect();
        String url = "jdbc:postgresql://localhost:5432/" + databaseName;
        String username = "postgres";
        String password = "postgres";
        connection = DriverManager.getConnection(url, username, password);
    }

    public void disconnect() throws SQLException {
        if (connection != null) {
                connection.close();
        }

    }

    public static DataBase getDb_access() {
        if (db_access == null) db_access = new DataBase();
        return db_access;
    }

    public List<Employee> getAllEmployees() throws SQLException {
        String sql = "SELECT * FROM employee";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql);
        List<Employee> employees = new ArrayList<>();
        while (rs.next()) {
            //System.out.println(new Employee(rs.getInt("emp_no"), rs.getString("lastname"), rs.getString("firstname"), LocalDate.parse(rs.getDate("birthdate").toString()), rs.getInt("salary"), rs.getInt("dept_no"), rs.getString("gender").charAt(0)));
            employees.add(new Employee(rs.getString("lastname"), rs.getString("firstname"), LocalDate.parse(rs.getDate("birthdate").toString()), rs.getInt("salary"), rs.getInt("dept_no"), rs.getString("gender").charAt(0)));
        }
        return employees;
    }

    public void getEmployeesOfDepartment(int deptno) throws SQLException {

        String sql = "SELECT * FROM employee WHERE dept_no = " + deptno + " ORDER BY lastname, firstname";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql);
        List<Employee> employees = new ArrayList<>();
        while (rs.next()) {
            employees.add(new Employee(rs.getString("lastname"), rs.getString("firstname"), LocalDate.parse(rs.getDate("birthdate").toString()), rs.getInt("salary"), rs.getInt("dept_no"), rs.getString("gender").charAt(0)));
        }
        statement.close();
        for (Employee employee : employees) {
            System.out.println(employee);
        }
    }

    public void getAverageSalaryOfGender(char gender) throws SQLException {

        String sql = "SELECT AVG(salary) FROM employee WHERE gender = '" + gender + "'";
        Statement statement = connection.createStatement();
        ResultSet average = statement.executeQuery(sql);
        average.next();
        System.out.println("Durchschnittsgehalt der " + ((gender == 'M') ? "Männer: " : "Frauen: ") +average.getInt("avg") + " €");
    }

    public void insertEmployee(Employee employee) throws SQLException {
        List<Employee> employeeList = getAllEmployees();
        boolean cont = true;

        for (Employee emp:
             employeeList) {
            if (emp.getLastname().equals(employee.getLastname()) && emp.getFirstname().equals(employee.getFirstname()) && emp.getBirthdate().equals(employee.getBirthdate())) {
                System.out.println("Employee already exists");
                cont = false;
            }
        }
        if (cont) {
            Statement statement = connection.createStatement();
            String sql = String.format(Locale.US, "INSERT INTO employee (lastname, firstname, birthdate, salary, dept_no, gender) VALUES ('%s', '%s', '%s', '%d', '%d', '%c')", employee.getLastname(), employee.getFirstname(), employee.getBirthdate(), employee.getSalary(), employee.getDept_no(), employee.getGender());
            System.out.println(sql);
                statement.executeUpdate(sql);

            statement.close();
        }
    }

    public void deleteEmployee(int emp_no) throws SQLException {
        Statement statement = connection.createStatement();
        String sql = "DELETE FROM employee WHERE emp_no = " + emp_no;
        int count = statement.executeUpdate(sql);
        System.out.println((count >= 1) ? "Successfully deleted " + count + " employees" : "Employee not found");
    }

    public static void main(String[] args) {
        /*DataBase db = DataBase.getDb_access();
        try {
            db.connect("postgres");
            //db.createDatabase("employee");
            db.connect("employee");
            //db.createTable();
            db.getEmployeesOfDepartment(1);
            db.getAverageSalaryOfGender('M');
            db.getAverageSalaryOfGender('F');
            db.getAllEmployees();
            System.out.println("Inserting Franz: ");
            db.insertEmployee(new Employee(1331, "Franz", "Gerold", LocalDate.of(1970, 1, 1), 3500, 1, 'M'));
            db.getAllEmployees();
            System.out.println("Deleting Franz: ");
            db.deleteEmployee(1331);
            db.getAllEmployees();
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        DataBase db_access = DataBase.getDb_access();
        try {
            db_access.connect("postgres");
            db_access.createDatabase("employee");
            db_access.createDatabase("employee");
            db_access.connect("employee");
        } catch (Exception e) {
            System.out.println("An error occured");
        }
        Scanner sc = new Scanner(System.in);

        while (true) {
            boolean rightSelection = false;
            boolean error = true;
            int selection = 0;

            System.out.println("(1) zur Datenbank employeedb verbinden\n" +
                    "(2) die Verbindung zur Datenbank employeedb schließen\n" +
                    "(3) Datenbank 'employeedb' erstellen\n" +
                    "(4) Tabelle 'employee' erstellen\n" +
                    "(5) Mitarbeiter aus Abteilung abrufen\n" +
                    "(6) Durchschnittsgehalt berechnen\n" +
                    "(7) neuen Mitarbeiter in die Datenbank einfügen\n" +
                    "(8) Mitarbeiter aus der Datenbank entfernen\n" +
                    "(9) Beenden");
            while (!rightSelection) {
                try {
                    selection = sc.nextInt();
                    rightSelection = true;
                } catch (Exception e) {
                    System.out.println("Not a number");
                }
            }
            switch (selection) {
                case 1:
                    try {
                        db_access.connect("employee");
                        System.out.println("successfully connected");
                    } catch (Exception e) {
                        System.out.println("An error occured");
                    }
                break;
                case 2:
                    try {
                        db_access.disconnect();
                        System.out.println("Successfully disconnected");
                    } catch (Exception e) {
                        System.out.println("An error occured");
                    }
                break;
                case 3:db_access.createDatabase("employee");
                break;
                case 4:
                    try {
                        db_access.createTable();
                        System.out.println("successfully created");
                    } catch (Exception e) {
                        System.out.println("An error occured");
                    }
                    break;
                case 5:
                    int dept = 0;
                    while (dept != 1 && dept != 2) {
                        System.out.println("Department number (1 or 2): ");
                        try {
                            dept = Integer.parseInt(sc.next());
                        } catch (NumberFormatException e) {
                            System.out.println("Not a number");
                            dept = 0;
                        }
                    }
                    try {
                        db_access.getEmployeesOfDepartment(dept);
                    } catch (Exception e) {
                        System.out.println("An error occured while executing this statement");
                    }
                    break;
                case 6:
                    String gender = "S";
                    while (gender.charAt(0) != 'M' && gender.charAt(0) != 'F') {
                        gender = "";
                        System.out.println("Gender (M or F) ");
                        gender = sc.next();
                    }
                    try {
                        db_access.getAverageSalaryOfGender(gender.charAt(0));
                    } catch (Exception e) {
                        System.out.println("An error occured");
                    }
                    break;
                case 7:
                    System.out.println("lastname of employee: ");
                    String lastname = sc.next();
                    System.out.println("firstname of employee: ");
                    String firstname = sc.next();
                    LocalDate bdate = LocalDate.of(1970, 1, 1);
                    error = true;
                    while (error) {
                        try {
                            System.out.println("Brithdate of employee (dd.MM.yyyy): ");
                            bdate = LocalDate.parse(sc.next(), DateTimeFormatter.ofPattern("dd.MM.yyyy"));
                            error = false;
                        } catch (Exception e) {
                            System.out.println("not a valid date");
                            error = true;
                        }
                    }
                    error = true;
                    int salary = 0;
                    while (error) {
                        System.out.println("Salary of employee: ");
                        try {
                            salary = Integer.parseInt(sc.next());
                            error = false;
                        } catch (Exception e) {
                            System.out.println("Not a valid number");
                            error = true;
                        }
                    }
                    error = true;
                    int deptno = 0;
                    while (error || (deptno != 1 && deptno != 2)) {
                        error = true;
                        System.out.println("Department number of employee: ");
                        try {
                            deptno = Integer.parseInt(sc.next());
                            error = false;
                        } catch (Exception e) {
                            System.out.println("Not a valid number");
                            error = true;
                        }
                    }
                    System.out.println("gender of employee (M / F): ");
                    String gend = sc.next();
                    Employee newEmployee = new Employee(lastname, firstname, bdate, salary, deptno, gend.charAt(0));
                    try {
                        db_access.insertEmployee(newEmployee);
                    } catch (Exception e) {
                        System.out.println("Error occured while inserting");
                    }
                    break;
                case 8:
                    error = true;
                    int emp_id = 0;
                    while (error) {
                        System.out.println("Employee id of employee to be deleted: ");
                        try {
                            emp_id = sc.nextInt();
                            error = false;
                        } catch (Exception e) {
                            System.out.println("Not a valid number");
                            error = true;
                        }
                    }
                    try {
                        db_access.deleteEmployee(emp_id);
                    } catch (Exception e) {
                        System.out.println("An error ococured");
                    }
                    break;
                case 9:
                    return;

            }
        }

    }

}
