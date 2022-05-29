package view;

import com.itextpdf.text.DocumentException;
import entity.Apartment;
import entity.Booking;
import entity.Customer;
import entity.Employee;
import exception.NoSuchEntity;
import exception.NoSuchRow;
import report.Report;
import repository.ApartmentRepository;
import repository.BookingRepository;
import repository.CustomerRepository;
import repository.EmployeeRepository;
import service.ApartmentService;
import service.BookingService;
import service.CustomerService;
import service.EmployeeService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.List;

import static view.ViewStorage.*;

public class Frame {
    private ApartmentService apartmentService;
    private CustomerService customerService;
    private BookingService bookingService;
    private EmployeeService employeeService;

    private JFrame frame;
    private JToolBar toolBar;
    private JButton[] buttonsToolBar;

    private JPanel entityPanel;
    private JButton[] buttonEntityPanel;

    private JPanel customerPanel;
    private JPanel apartmentPanel;
    private JScrollPane scrollApartments;
    private JButton makeBooking;
    private JButton viewBookings;
    private JButton viewInformation;
    private JButton searchApartment;

    private DefaultTableModel tableModelApartments;
    private JTable tableApartments;

    private int current = -1;
    private DefaultTableModel tableModel;
    private JTable table;
    private JScrollPane scroll;

    public void show() {
        apartmentService = new ApartmentService(new ApartmentRepository());
        customerService = new CustomerService(new CustomerRepository());
        bookingService = new BookingService(apartmentService, customerService, new BookingRepository());
        employeeService = new EmployeeService(new EmployeeRepository());

        frame = new JFrame("Booking Service");
        frame.setSize(720, 480);
        frame.setLocation(100, 100);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                int input = JOptionPane.showConfirmDialog(frame,
                        "Are you sure you want to exit?", "Exit",
                        JOptionPane.YES_NO_OPTION);
                if (input == JOptionPane.YES_OPTION)
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                else if (input == JOptionPane.NO_OPTION)
                    frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            }
        });
        frame.setLayout(new BorderLayout());

        buttonsToolBar = new JButton[tools.length];
        toolBar = new JToolBar("Toolbar");
        for (int i = 0; i < tools.length; i++) {
            buttonsToolBar[i] = new JButton(tools[i], new ImageIcon(
                    "src/main/resources/img/" + tools[i] + ".png"));
            toolBar.add(buttonsToolBar[i]);
        }
        frame.add(toolBar, BorderLayout.NORTH);

        buttonEntityPanel = new JButton[entities.length];
        entityPanel = new JPanel(new GridLayout(entities.length, 1));
        for (int i = 0; i < entities.length; i++) {
            buttonEntityPanel[i] = new JButton(entities[i]);
            entityPanel.add(buttonEntityPanel[i]);
        }
        JPanel west = new JPanel(new FlowLayout());
        west.add(entityPanel);
        frame.add(west, BorderLayout.WEST);

        makeBooking = new JButton("Make Booking");
        viewBookings = new JButton("View Bookings");
        viewInformation = new JButton("View Information");
        customerPanel = new JPanel();
        customerPanel.add(makeBooking);
        customerPanel.add(viewBookings);
        customerPanel.add(viewInformation);

        apartmentPanel = new JPanel(new BorderLayout());
        JPanel south = new JPanel();
        south.add(new Label("Start"));
        TextField dataStartField = new TextField(5);
        south.add(dataStartField);
        south.add(new Label("End"));
        TextField dataEndField = new TextField(5);
        south.add(dataEndField);
        south.add(new Label("Min price"));
        TextField minPriceField = new TextField(5);
        south.add(minPriceField);
        south.add(new Label("Max price"));
        TextField maxPriceField = new TextField(5);
        south.add(maxPriceField);
        searchApartment = new JButton("Search available Apartment");
        south.add(searchApartment);
        apartmentPanel.add(south, BorderLayout.CENTER);

        frame.setVisible(true);

        for (int i = 0; i < entities.length; i++) {
            final int next = i;
            buttonEntityPanel[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent event) {
                    if (current != -1) {
                        frame.remove(scroll);
                    }
                    if (current == 2) {
                        frame.remove(customerPanel);
                    }
                    current = next;
                    switch (current) {
                        case 0 -> chooseApartments(apartmentService.getAllApartments());
                        case 1 -> chooseBookings(bookingService.getAllBookings());
                        case 2 -> chooseCustomers(customerService.getAllCustomers());
                        case 3 -> chooseEmployees(employeeService.getAllEmployee());
                    }
                    frame.revalidate();
                    frame.repaint();
                }
            });
        }

        buttonsToolBar[0].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                try {
                    didNotSelectEntity();
                    JPanel panel = new JPanel();
                    int n = tableModel.getColumnCount() - 1;
                    TextField[] fields = new TextField[n];
                    for (int i = 0; i < n; i++) {
                        panel.add(new Label(tableModel.getColumnName(i + 1)));
                        panel.add(fields[i] = new TextField(10));
                    }
                    int input = JOptionPane.showConfirmDialog(frame, panel, "Add"
                            , JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (input == JOptionPane.OK_OPTION) {
                        String[] row = new String[n];
                        for (int i = 0; i < n; i++)
                            row[i] = fields[i].getText();
                        switch (current) {
                            case 0 -> {
                                Apartment apartment = new Apartment();
                                apartmentService.setApartment(apartment, row);
                                apartmentService.saveApartment(apartment);
                                tableModel.addRow(apartment.toArrayString());
                            }
                            case 1 -> {
                                Booking booking = new Booking();
                                bookingService.setBooking(booking, row);
                                bookingService.saveBooking(booking);
                                tableModel.addRow(booking.toArrayString());
                            }
                            case 2 -> {
                                Customer customer = new Customer();
                                customerService.setCustomer(customer, row);
                                customerService.saveCustomer(customer);
                                tableModel.addRow(customer.toArrayString());
                            }
                            case 3 -> {
                                Employee employee = new Employee();
                                employeeService.setEmployee(employee, row);
                                employeeService.saveEmployee(employee);
                                tableModel.addRow(employee.toArrayString());
                            }
                        }
                        JOptionPane.showMessageDialog(frame, "Added successfully!");
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, ex.getMessage(), "Warning!",
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        buttonsToolBar[1].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                try {
                    didNotSelectEntity();
                    didNotSelectRow(table);
                    JPanel panel = new JPanel();
                    int n = tableModel.getColumnCount() - 1;
                    int currentRow = table.getSelectedRow();
                    TextField[] fields = new TextField[n];
                    for (int i = 0; i < n; i++) {
                        panel.add(new Label(tableModel.getColumnName(i + 1)));
                        panel.add(fields[i] = new TextField((String) tableModel.
                                getValueAt(currentRow, i + 1), 10));
                    }

                    int input = JOptionPane.showConfirmDialog(frame, panel, "Edit"
                            , JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (input == JOptionPane.OK_OPTION) {
                        int id = Integer.parseInt(tableModel.getValueAt(currentRow, 0).toString());
                        String[] row = new String[n];
                        for (int i = 0; i < n; i++)
                            row[i] = fields[i].getText();
                        switch (current) {
                            case 0 -> {
                                Apartment apartment = apartmentService.getApartment(id);
                                apartmentService.setApartment(apartment, row);
                                apartmentService.updateApartment(apartment);
                            }
                            case 1 -> {
                                Booking booking = bookingService.getBooking(id);
                                bookingService.setBooking(booking, row);
                                bookingService.updateBooking(booking);
                            }
                            case 2 -> {
                                Customer customer = customerService.getCustomer(id);
                                customerService.setCustomer(customer, row);
                                customerService.updateCustomer(customer);
                            }
                            case 3 -> {
                                Employee employee = employeeService.getEmployee(id);
                                employeeService.setEmployee(employee, row);
                                employeeService.updateEmployee(employee);
                            }
                        }
                        for (int i = 0; i < n; i++)
                            tableModel.setValueAt(fields[i].getText(), currentRow, i + 1);
                        JOptionPane.showMessageDialog(frame, "Edited successfully!");
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, ex.getMessage(), "Warning!",
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        buttonsToolBar[2].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                try {
                    didNotSelectEntity();
                    didNotSelectRow(table);
                    int input = JOptionPane.showConfirmDialog(frame,
                            "Are you sure you want to delete selected rows?", "Delete", JOptionPane.YES_NO_OPTION);
                    if (input == JOptionPane.YES_OPTION) {
                        int id = Integer.parseInt(tableModel.getValueAt(
                                table.getSelectedRow(), 0).toString());
                        switch (current) {
                            case 0 -> apartmentService.deleteApartment(apartmentService.getApartment(id));
                            case 1 -> bookingService.deleteBooking(bookingService.getBooking(id));
                            case 2 -> customerService.deleteCustomer(customerService.getCustomer(id));
                            case 3 -> employeeService.deleteEmployee(employeeService.getEmployee(id));
                        }
                        tableModel.removeRow(table.getSelectedRow());
                        JOptionPane.showMessageDialog(frame, "Deleted successfully!");
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, ex.getMessage(), "Warning!",
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        buttonsToolBar[3].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                try {
                    new Report(apartmentService, bookingService, customerService).createReport();
                    JOptionPane.showMessageDialog(frame, "Report created!");
                } catch (DocumentException | IOException ex) {
                    JOptionPane.showMessageDialog(frame, ex.getMessage(), "Warning!",
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        makeBooking.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                try {
                    didNotSelectRow(table);
                    if (scrollApartments != null)
                        apartmentPanel.remove(scrollApartments);
                    tableModelApartments = new DefaultTableModel(
                            apartmentService.toArrayArrayString(apartmentService.getAllApartments()), titleTableApartment);
                    tableApartments = new JTable(tableModelApartments);
                    scrollApartments = new JScrollPane(tableApartments);
                    apartmentPanel.add(scrollApartments, BorderLayout.NORTH);
                    dataStartField.setText("");dataEndField.setText("");
                    maxPriceField.setText("");minPriceField.setText("");
                    boolean flag = false;
                    while (!flag) {
                        try {
                            int input = JOptionPane.showConfirmDialog(frame, apartmentPanel, "Make Booking"
                                    , JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
                            if (input == JOptionPane.OK_OPTION) {
                                didNotSelectRow(tableApartments);
                                String[] row = new String[]{dataStartField.getText(), dataEndField.getText(),
                                        tableModelApartments.getValueAt(tableApartments.getSelectedRow(), 0).toString(),
                                        tableModel.getValueAt(table.getSelectedRow(), 0).toString()};
                                Booking booking = new Booking();
                                bookingService.setBooking(booking, row);
                                bookingService.saveBooking(booking);
                                JOptionPane.showMessageDialog(frame, "Booking made successfully!");
                                flag = true;
                            }
                            else
                                flag = true;
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(frame, ex.getMessage(), "Warning!",
                                    JOptionPane.WARNING_MESSAGE);
                        }

                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, ex.getMessage(), "Warning!",
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        viewBookings.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                try {
                    didNotSelectRow(table);
                    int id = Integer.parseInt(tableModel.getValueAt(table.getSelectedRow(), 0).toString());
                    if (current != -1) {
                        frame.remove(scroll);
                    }
                    if (current == 2) {
                        frame.remove(customerPanel);
                    }
                    current = 1;
                    chooseBookings(customerService.getCustomer(id).getBookings());
                    frame.revalidate();
                    frame.repaint();
                    if (tableModel.getRowCount() == 0)
                        JOptionPane.showMessageDialog(frame, "No matching bookings!");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, ex.getMessage(), "Warning!",
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        viewInformation.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                try {
                    didNotSelectRow(table);
                    JPanel panel = new JPanel(new BorderLayout());
                    JPanel north = new JPanel();
                    int id = Integer.parseInt(tableModel.getValueAt(table.getSelectedRow(), 0).toString());
                    Customer customer = customerService.getCustomer(id);
                    String[] row = customer.toArrayString();
                    for (int i = 0; i < 4; i++) {
                        north.add(new Label(tableModel.getColumnName(i) + ": " + row[i]));
                    }
                    panel.add(north, BorderLayout.NORTH);

                    DefaultTableModel tableModelBookings = new DefaultTableModel(
                            bookingService.toArrayArrayStringWithDuration(customer.getBookings()),
                            titleTableBookingOfCustomer);
                    JTable tableBookings = new JTable(tableModelBookings);
                    JScrollPane scrollBookings = new JScrollPane(tableBookings);
                    panel.add(scrollBookings, BorderLayout.CENTER);

                    JOptionPane.showMessageDialog(frame, panel, "Information of customers with id " + id,
                            JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, ex.getMessage(), "Warning!",
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        searchApartment.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                try {
                    apartmentPanel.remove(scrollApartments);
                    String[] row = {dataStartField.getText(), dataEndField.getText(),
                            minPriceField.getText(), maxPriceField.getText()};
                    tableModelApartments = new DefaultTableModel(apartmentService.toArrayArrayString(
                            apartmentService.getApartmentsByParameters(row)), titleTableApartment);
                    tableApartments = new JTable(tableModelApartments);
                    scrollApartments = new JScrollPane(tableApartments);
                    if (tableModelApartments.getRowCount() == 0)
                        JOptionPane.showMessageDialog(frame, "No matching apartments!");
                    apartmentPanel.add(scrollApartments, BorderLayout.NORTH);
                    apartmentPanel.revalidate();
                    apartmentPanel.repaint();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, ex.getMessage(), "Warning!",
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        });
    }

    void chooseApartments(List<Apartment> apartments) {
        tableModel = new DefaultTableModel(apartmentService.toArrayArrayString(apartments),
                titleTableApartment);
        table = new JTable(tableModel);
        scroll = new JScrollPane(table);
        frame.add(scroll, BorderLayout.CENTER);
    }

    void chooseBookings(List<Booking> bookings) {
        tableModel = new DefaultTableModel(bookingService.toArrayArrayString(bookings),
                titleTableBooking);
        table = new JTable(tableModel);
        scroll = new JScrollPane(table);
        frame.add(scroll, BorderLayout.CENTER);
    }

    void chooseCustomers(List<Customer> customers) {
        tableModel = new DefaultTableModel(customerService.toArrayArrayString(customers),
                titleTableCustomer);
        table = new JTable(tableModel);
        scroll = new JScrollPane(table);
        frame.add(scroll, BorderLayout.CENTER);
        frame.add(customerPanel, BorderLayout.SOUTH);
    }

    void chooseEmployees(List<Employee> employees) {
        tableModel = new DefaultTableModel(employeeService.toArrayArrayString(employees),
                titleTableEmployee);
        table = new JTable(tableModel);
        scroll = new JScrollPane(table);
        frame.add(scroll, BorderLayout.CENTER);
    }

    void initApartmentPanel(){

    }

    public void didNotSelectEntity() throws NoSuchEntity {
        if (current == -1)
            throw new NoSuchEntity();
    }

    public void didNotSelectRow(JTable table) throws NoSuchRow {
        if (table.getSelectedRow() == -1)
            throw new NoSuchRow();
    }
}
