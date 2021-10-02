package com.kor.payments.services;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.kor.payments.utils.Utils;
import com.kor.payments.domain.Transaction;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

public class PDFBuilderService {

    public static boolean getCheck(Transaction payment) throws IOException, DocumentException, URISyntaxException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream("check.pdf"));
        document.open();
        Path path = Paths.get(ClassLoader.getSystemResource("payments.png").toURI());
        Image img = Image.getInstance(path.toAbsolutePath().toString());
        img.scalePercent(30);
        document.add(img);
        Paragraph paragraph = new Paragraph();
        String now = Utils.getFormatedDate(LocalDateTime.now());
        Font font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);
        Chunk chunk = new Chunk("Check of " + now, font);
        document.add(paragraph);
        document.add(chunk);
        PdfPTable table = new PdfPTable(2);
        addRows(table, payment);
        document.add(table);
        document.add(paragraph);
        document.close();
        return true;
    }

    private static void addRows(PdfPTable table, Transaction payment) {
        Font font = FontFactory.getFont("HelveticaRegular.ttf", BaseFont.IDENTITY_H, true);
        PdfPCell cell;
        table.addCell("recipient's account");
        table.addCell("UA " + payment.getReceiver().getId());
        table.addCell("receiver");
        cell = new PdfPCell(new Phrase(payment.getReceiver().getUser().getLastName() + " " +
                payment.getReceiver().getUser().getName() + " " +
                payment.getReceiver().getUser().getMiddleName(), font));
        table.addCell(cell);
        table.addCell("accrual to the recipient");
        table.addCell((double) payment.getAccrual() / 100 + " " + payment.getReceiver().getCurrency().name());
        table.addCell("payer");
        cell = new PdfPCell(new Phrase(payment.getPayer().getUser().getLastName() + " " +
                payment.getPayer().getUser().getName() + " " +
                payment.getPayer().getUser().getMiddleName(), font));
        table.addCell(cell);
        table.addCell("payer`s account");
        table.addCell("UA " + payment.getPayer().getId());
        table.addCell("payment amount");
        table.addCell((double) payment.getAmount() / 100 + " " + payment.getCurrency().name());
        table.addCell("payment destination");
        cell = new PdfPCell(new Phrase(String.valueOf(payment.getDestination()), font));
        table.addCell(cell);
        table.addCell("date");
        table.addCell(Utils.getFormatedDate(payment.getRegistered()));
    }
}
