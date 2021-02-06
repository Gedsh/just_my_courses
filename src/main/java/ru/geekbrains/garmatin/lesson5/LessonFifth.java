package ru.geekbrains.garmatin.lesson5;

public class LessonFifth {
    public static void main(String[] args) {
        Employee[] employees = new Employee[5];
        addEmployees(employees);
        printEmployeesOlderForty(employees);
    }

    private static void addEmployees(Employee[] employees) {
        employees[0] = new Employee(
                "Ivanov Ivan",
                "Engineer",
                "vivan@mailbox.com" ,
                "892312311",
                30000,
                30
        );
        employees[1] = new Employee(
                "One One",
                "Engineer",
                "one@mailbox.com" ,
                "892312312",
                40000,
                40
        );
        employees[2] = new Employee(
                "Two Two",
                "Engineer",
                "two@mailbox.com" ,
                "892312313",
                50000,
                50
        );
        employees[3] = new Employee(
                "Three Three",
                "Engineer",
                "three@mailbox.com" ,
                "892312314",
                60000,
                60
        );
        employees[4] = new Employee(
                "Four Four",
                "Engineer",
                "four@mailbox.com" ,
                "892312315",
                70000,
                70
        );
    }

    private static void printEmployeesOlderForty(Employee[] employees) {
        for (Employee employee: employees) {
            if (employee.getAge() > 40) {
                System.out.println(employee);
            }
        }
    }
}
