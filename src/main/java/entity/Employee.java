package entity;

import javax.persistence.*;


@Entity
@Table(name = "employees")
@AttributeOverride(name = "name", column = @Column(name = "employee_name"))
@AttributeOverride(name = "surname", column = @Column(name = "employee_surname"))
public class Employee extends Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employee_id")
    private int id;

    @Column(name = "employee_position")
    private String position;

    @Column(name = "employee_salary")
    private int salary;

    public Employee() {
    }

    public int getId() {
        return id;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", position='" + position + '\'' +
                ", salary=" + salary +
                '}';
    }

    public String[] toArrayString() {
        return new String[]{Integer.toString(id), getName(),
                getSurname(), getPosition(), Integer.toString(getSalary())};
    }
}
