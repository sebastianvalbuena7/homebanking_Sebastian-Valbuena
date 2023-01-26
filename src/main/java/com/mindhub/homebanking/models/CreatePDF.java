package com.mindhub.homebanking.models;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.util.Set;
import java.util.stream.Stream;

public class CreatePDF {
    public static void generatePDF(Set<Transaction> transactions, Client client, HttpServletResponse response) throws Exception {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=transactions.pdf");

        Font fontLogo = FontFactory.getFont("Helvetica", 30, Color.BLACK);
        Font fontTitle = FontFactory.getFont("Helvetica", 20, Color.BLACK);
        Font fontText = FontFactory.getFont("Helvetica", 14, Color.BLACK);

        Paragraph logo = new Paragraph("MINDHUB BROTHERS", fontLogo);
        logo.setAlignment(Element.ALIGN_CENTER);
        logo.setSpacingAfter(12);

        Paragraph title = new Paragraph("Your transactions", fontTitle);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(12);

        Paragraph subTitle = new Paragraph("Client: " + client.getFirstName() + " " + client.getLastName(), fontText);
        subTitle.setAlignment(Element.ALIGN_CENTER);
        subTitle.setSpacingAfter(20);

        PdfPTable tableTransactions = new PdfPTable(4);
        Stream.of("Amount", "Description", "Date", "Type").forEach(tableTransactions::addCell);

        transactions.forEach(transaction -> {
            tableTransactions.addCell(String.valueOf(transaction.getAmount()));
            tableTransactions.addCell(transaction.getDescription());
            tableTransactions.addCell(String.valueOf(transaction.getDate()));
            tableTransactions.addCell(String.valueOf(transaction.getType()));
        });

        //crear un nuevo documento
        Document document = new Document();
        document.setPageSize(PageSize.LETTER.rotate());
        PdfWriter.getInstance(document, response.getOutputStream());
        //abrir el documento
        document.open();
        //agregar contenido al documento
        document.add(logo);
        document.add(title);
        document.add(subTitle);
        document.add(tableTransactions);
        //cerrar el documento
        document.close();
    }
}
