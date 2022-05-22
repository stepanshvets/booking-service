package report;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import entity.Apartment;
import service.ApartmentService;
import service.BookingService;
import service.CustomerService;

import java.io.FileOutputStream;
import java.io.IOException;

import static view.ViewStorage.titleTableApartment;

public class Report {
    final private ApartmentService apartmentService;
    final private BookingService bookingService;
    final private CustomerService customerService;

    public Report(ApartmentService apartmentService, BookingService bookingService, CustomerService customerService) {
        this.apartmentService = apartmentService;
        this.bookingService = bookingService;
        this.customerService = customerService;
    }

    public void createReport() throws DocumentException, IOException{
        Document document = new com.itextpdf.text.Document(PageSize.A4, 30, 30, 60, 35);
        BaseFont bfComic = BaseFont.createFont("src/main/resources/fonts/arial.ttf",
                    BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

        Font fontTitle = new Font(bfComic, 18);
        Font font = new Font(bfComic, 12);

        Paragraph paragraphTitle = new Paragraph("Report", fontTitle);
        paragraphTitle.setAlignment(Paragraph.ALIGN_CENTER);

        Paragraph paragraph1 = new Paragraph("List of apartments", font);
        paragraph1.setAlignment(Paragraph.ALIGN_CENTER);

        PdfPTable t = new PdfPTable(3);
        PdfWriter.getInstance(document, new FileOutputStream("Report.pdf"));

        for (String nameColumn : titleTableApartment)
            t.addCell(new PdfPCell(new Phrase(nameColumn, font)));
        for (Apartment apartment : apartmentService.getAllApartments())
            for (String string : apartment.toArrayString())
                t.addCell(new Phrase(string, font));

        document.open();
        document.add(paragraphTitle);
        document.add(new Paragraph("\n"));
        document.add(new Paragraph("Number of apartments: " + apartmentService.getAllApartments().size(), font));
        document.add(new Paragraph("Number of bookings: " + bookingService.getAllBookings().size(), font));
        document.add(new Paragraph("Number of customers: " + customerService.getAllCustomers().size(), font));
        document.add(new Paragraph("\n"));
        document.add(paragraph1);
        document.add(new Paragraph("\n"));
        document.add(t);
        document.close();
    }
}
