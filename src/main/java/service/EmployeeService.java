package service;

import entity.Employee;
import exception.ValidationException;
import repository.EmployeeRepository;

import java.util.List;

public class EmployeeService {
    final private EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<Employee> getAllEmployee() {
        return employeeRepository.getAll();
    }

    public Employee getEmployee(int id) {
        return employeeRepository.get(id);
    }

    public void saveEmployee(Employee employee) {
        employeeRepository.save(employee);
    }

    public void updateEmployee(Employee employee) {
        employeeRepository.update(employee);
    }

    public void deleteEmployee(Employee employee) {
        employeeRepository.delete(employee);
    }

    public String[][] toArrayArrayString(List<Employee> employees) {
        if (employees == null)
            return new String[][]{};
        String[][] arrayString = new String[employees.size()][];
        int i = 0;
        for (Employee employee : employees)
            arrayString[i++] = employee.toArrayString();
        return arrayString;
    }

    public void setEmployee(Employee employee, String[] arrayString) throws ValidationException {
        isNotEmpty(arrayString);
        employee.setName(arrayString[0]);
        employee.setSurname(arrayString[1]);
        employee.setPosition(arrayString[2]);
        try {
            employee.setSalary(Integer.parseInt(arrayString[3]));
        } catch (IllegalArgumentException exception) {
            throw new ValidationException("Wrong Filling!");
        }
    }

    void isNotEmpty(String[] arrayString) throws ValidationException {
        for (String string : arrayString)
            if (string.equals(""))
                throw new ValidationException("Some fields are empty!");
    }
}
