package com.kor.payments.controller;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.kor.payments.domain.Transaction;
import com.kor.payments.domain.Utils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ControllerUtils {

    static Map<String, String> getErrors(BindingResult bindingResult) {
//        TODO stream
        Collector<FieldError, ?, Map<String, String>> collector = Collectors.toMap(
                fieldError -> fieldError.getField() + "Error",
                FieldError::getDefaultMessage);
        return bindingResult.getFieldErrors().stream().collect(collector);
    }

    static boolean getCheck(Transaction payment) throws IOException, DocumentException, URISyntaxException {

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
//        addTableHeader(table);
        addRows(table, payment);
        document.add(table);
        document.add(paragraph);
        document.close();

        return true;
    }

//    private static void addTableHeader(PdfPTable table) {
//        Stream.of("column header 1", "column header 2", "column header 3")
//                .forEach(columnTitle -> {
//                    PdfPCell header = new PdfPCell();
//                    header.setBackgroundColor(BaseColor.LIGHT_GRAY);
//                    header.setBorderWidth(2);
//                    header.setPhrase(new Phrase(columnTitle));
//                    table.addCell(header);
//                });
//    }

    private static void addRows(PdfPTable table, Transaction payment) {
        table.addCell("recipient's account");
        table.addCell("UA " + payment.getReceiver().getId());
        table.addCell("receiver");
        table.addCell(payment.getReceiver().getUser().getLastName() + " " +
                payment.getReceiver().getUser().getName() + " " +
                payment.getReceiver().getUser().getMiddleName());
        table.addCell("accrual to the recipient");
        table.addCell(payment.getAccrual()/100 + " " + payment.getReceiver().getCurrency().name());
//        table.addCell("receiver, currency");
//        table.addCell(payment.getReceiver().getCurrency().name());
        table.addCell("payer");
        table.addCell(payment.getPayer().getUser().getLastName() + " " +
                payment.getPayer().getUser().getName() + " " +
                payment.getPayer().getUser().getMiddleName());
        table.addCell("payer`s account");
        table.addCell("UA " + payment.getPayer().getId());
//        table.addCell("payer, currency");
//        table.addCell(payment.getCurrency().name());
        table.addCell("payment amount");
        table.addCell(payment.getAmount()/100 + " " + payment.getCurrency().name());
        table.addCell("payment destination");
        table.addCell(String.valueOf(payment.getDestination()));
        table.addCell("date");
        table.addCell(Utils.getFormatedDate(payment.getRegistered()));
    }





}
