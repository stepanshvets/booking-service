package service;

import entity.Customer;
import exception.ValidationException;
import repository.CustomerRepository;

import java.util.List;

public class CustomerService {
    final private CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.getAll();
    }

    public Customer getCustomer(int id) {
        return customerRepository.get(id);
    }

    public void saveCustomer(Customer customer) {
        customerRepository.save(customer);
    }

    public void updateCustomer(Customer customer) {
        customerRepository.update(customer);
    }

    public void deleteCustomer(Customer customer) {
        customerRepository.delete(customer);
    }

    public String[][] toArrayArrayString(List<Customer> customers) {
        if (customers == null)
            return new String[][]{};
        String[][] arrayString = new String[customers.size()][];
        int i = 0;
        for (Customer customer : customers)
            arrayString[i++] = customer.toArrayString();
        return arrayString;
    }

    public void setCustomer(Customer customer, String[] arrayString) throws ValidationException {
        isNotEmpty(arrayString);
        customer.setName(arrayString[0]);
        customer.setSurname(arrayString[1]);
        customer.setEmail(arrayString[2]);
    }

    void isNotEmpty(String[] arrayString) throws ValidationException {
        for (String string : arrayString)
            if (string.equals(""))
                throw new ValidationException("Some fields are empty!");
    }
}
