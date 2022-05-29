package view;

public class ViewStorage {
    public static String[] tools = {"Add", "Edit", "Delete", "Print"};
    public static String[] entities = {"Apartments", "Bookings", "Customers", "Employees"};
    public static String[] titleTableApartment = {"Id", "Address", "Seats", "Price"};
    public static String[] titleTableBooking = {"Id", "Start", "End", "Apartment Id", "Customer Id"};
    public static String[] titleTableCustomer = {"Id", "Name", "Surname", "Email"};
    public static String[] titleTableEmployee = {"Id", "Name", "Surname", "Position", "Salary"};
    public static String[] titleTableBookingOfCustomer = {"Id", "Start", "End", "Apartment Id", "Customer Id", "Days"};
}
