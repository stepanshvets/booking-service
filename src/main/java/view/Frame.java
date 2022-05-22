package view;

import com.itextpdf.text.*;
import entity.Apartment;
import entity.Booking;
import entity.Customer;
import exception.NoSuchEntity;
import exception.NoSuchRow;
import report.Report;
import repository.ApartmentRepository;
import repository.BookingRepository;
import repository.CustomerRepository;
import service.ApartmentService;
import service.BookingService;
import service.CustomerService;

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

    private JFrame frame;
    private JToolBar toolBar;
    private JButton[] buttonsToolBar;

    private JPanel entityPanel;
    private JButton[] buttonEntityPanel;

    private JPanel customerPanel;
    private JPanel searchPanelCustomer;
    private JScrollPane scrollApartments;
    private JButton makeBooking;
    private JButton viewBookings;

    private JPanel apartmentPanel;
    private JPanel searchPanel;
    private JButton searchApartment;

    private int current = -1;
    private DefaultTableModel tableModel;
    private JTable table;
    private JScrollPane scroll;

    public void show() {
        apartmentService = new ApartmentService(new ApartmentRepository());
        customerService = new CustomerService(new CustomerRepository());
        bookingService = new BookingService(apartmentService, customerService, new BookingRepository());

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
        customerPanel = new JPanel();
        customerPanel.add(makeBooking);
        customerPanel.add(viewBookings);

        apartmentPanel = new JPanel(new GridLayout(2, 1));
        JPanel panel = new JPanel();
        panel.add(new Label("Start"));
        TextField dataStartField = new TextField(5);
        panel.add(dataStartField);
        panel.add(new Label("End"));
        TextField dataEndField = new TextField(5);
        panel.add(dataEndField);
        panel.add(new Label("Min price"));
        TextField minPriceField = new TextField(5);
        panel.add(minPriceField);
        panel.add(new Label("Max price"));
        TextField maxPriceField = new TextField(5);
        panel.add(maxPriceField);
        apartmentPanel.add(panel);
        searchApartment = new JButton("Search available Apartment");
        JPanel south = new JPanel(new FlowLayout());
        south.add(searchApartment);
        apartmentPanel.add(south);

        frame.setVisible(true);

        for (int i = 0; i < entities.length; i++) {
            final int next = i;
            buttonEntityPanel[i].addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent event) {
                    if (current != -1) {
                        frame.remove(scroll);
                    }
                    if (current == 0) {
                        frame.remove(apartmentPanel);
                    }
                    if (current == 2) {
                        frame.remove(customerPanel);
                    }
                    current = next;
                    switch (current) {
                        case 0 -> chooseApartments(apartmentService.getAllApartments());
                        case 1 -> chooseBookings();
                        case 2 -> chooseCustomers();
                    }
                    frame.revalidate();
                    frame.repaint();
                }
            });
        }

        buttonsToolBar[0].addActionListener(new ActionListener() {
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
            public void actionPerformed(ActionEvent ae) {
                try {
                    new Report(apartmentService, bookingService, customerService).createReport();
                } catch (DocumentException | IOException ex) {
                    JOptionPane.showMessageDialog(frame, ex.getMessage(), "Warning!",
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        makeBooking.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                try {
                    didNotSelectEntity();
                    didNotSelectRow(table);
                    JPanel panel = new JPanel();

                    JPanel left = new JPanel(new BorderLayout());
                    JPanel upOfLeft = new JPanel();
                    int n = titleTableBooking.length - 3;
                    TextField[] fields = new TextField[n];
                    upOfLeft.add(new Label(titleTableBooking[1]));
                    upOfLeft.add(fields[0] = new TextField(10));
                    upOfLeft.add(new Label(titleTableBooking[2]));
                    upOfLeft.add(fields[1] = new TextField(10));
                    left.add(upOfLeft, BorderLayout.NORTH);
                    left.add(new Label("Enter date: yyyy-mm-dd"), BorderLayout.CENTER);
                    DefaultTableModel tableModelApartment = new DefaultTableModel(
                            apartmentService.toArrayArrayString(apartmentService.getAllApartments()), titleTableApartment);
                    JTable tableApartment = new JTable(tableModelApartment);
                    scrollApartments = new JScrollPane(tableApartment);

                    searchPanelCustomer= new JPanel(new BorderLayout());
                    searchPanelCustomer.add(scrollApartments, BorderLayout.NORTH);
                    searchPanelCustomer.add(apartmentPanel, BorderLayout.CENTER);

                    panel.add(left);
                    panel.add(searchPanelCustomer);
                    int input = JOptionPane.showConfirmDialog(frame, panel, "Make Booking"
                            , JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (input == JOptionPane.OK_OPTION) {
                        didNotSelectRow(tableApartment);
                        String[] row = new String[n + 2];
                        for (int i = 0; i < n; i++)
                            row[i] = fields[i].getText();
                        row[2] = tableModelApartment.getValueAt(tableApartment.getSelectedRow(), 0).toString();
                        row[3] = tableModel.getValueAt(table.getSelectedRow(), 0).toString();
                        Booking booking = new Booking();
                        bookingService.setBooking(booking, row);
                        bookingService.saveBooking(booking);
                    }

                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, ex.getMessage(), "Warning!",
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        viewBookings.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                try {
                    didNotSelectEntity();
                    didNotSelectRow(table);
                    int id = Integer.parseInt(tableModel.getValueAt(table.getSelectedRow(), 0).toString());
                    JPanel panel = new JPanel();
                    DefaultTableModel tableModelBooking = new DefaultTableModel(bookingService.toArrayArrayString(
                            customerService.getCustomer(id).getBookings()), titleTableBooking);
                    JTable tableBooking = new JTable(tableModelBooking);
                    JScrollPane scrollBooking = new JScrollPane(tableBooking);
                    panel.add(scrollBooking);
                    if (tableModelBooking.getRowCount() == 0)
                        JOptionPane.showMessageDialog(frame, "No matching bookings!");
                    JOptionPane.showMessageDialog(frame, panel, "Bookings of customers with id " + id,
                            JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, ex.getMessage(), "Warning!",
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        searchApartment.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                try {
                    if (searchPanelCustomer != null)
                        searchPanelCustomer.remove(scrollApartments);
                    String[] row = {dataStartField.getText(), dataEndField.getText(),
                            minPriceField.getText(), maxPriceField.getText()};
                    DefaultTableModel tableModelApartments = new DefaultTableModel(apartmentService.toArrayArrayString(
                            apartmentService.getApartmentsByParameters(row)), titleTableApartment);
                    JTable tableApartments = new JTable(tableModelApartments);
                    scrollApartments = new JScrollPane(tableApartments);
                    if (tableModelApartments.getRowCount() == 0)
                        JOptionPane.showMessageDialog(frame, "No matching apartments!");
                    if (current == 0) {
                        searchPanel = new JPanel();
                        searchPanel.add(scrollApartments);
                        JOptionPane.showMessageDialog(frame, searchPanel, "Available apartments",
                                JOptionPane.INFORMATION_MESSAGE);
                    }
                    else {
                        searchPanelCustomer.add(scrollApartments, BorderLayout.NORTH);
                        searchPanelCustomer.revalidate();
                        searchPanelCustomer.repaint();
                    }
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
        frame.add(apartmentPanel, BorderLayout.SOUTH);
    }

    void chooseBookings() {
        tableModel = new DefaultTableModel(bookingService.toArrayArrayString(bookingService.getAllBookings()),
                titleTableBooking);
        table = new JTable(tableModel);
        scroll = new JScrollPane(table);
        frame.add(scroll, BorderLayout.CENTER);
    }

    void chooseCustomers() {
        tableModel = new DefaultTableModel(customerService.toArrayArrayString(customerService.getAllCustomers()),
                titleTableCustomer);
        table = new JTable(tableModel);
        scroll = new JScrollPane(table);
        frame.add(scroll, BorderLayout.CENTER);
        frame.add(customerPanel, BorderLayout.SOUTH);
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
