package com.streetLeague.backend.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.draw.SolidLine;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.streetLeague.backend.entity.Reservation;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Service
public class ReservationPdfService {

    private static final DateTimeFormatter FMT      = DateTimeFormatter.ofPattern("dd MMM yyyy  •  HH:mm");
    private static final DateTimeFormatter FMT_FULL = DateTimeFormatter.ofPattern("dd/MM/yyyy à HH:mm");
    private static final DateTimeFormatter FMT_QR   = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private static final Color GREEN        = new DeviceRgb(34, 197, 94);
    private static final Color GREEN_DARK   = new DeviceRgb(21, 128, 61);
    private static final Color GREEN_LIGHT  = new DeviceRgb(220, 252, 231);
    private static final Color DARK         = new DeviceRgb(15, 23, 42);
    private static final Color GRAY         = new DeviceRgb(100, 116, 139);
    private static final Color LIGHT_BG     = new DeviceRgb(248, 250, 252);
    private static final Color WHITE        = new DeviceRgb(255, 255, 255);
    private static final Color ORANGE       = new DeviceRgb(249, 115, 22);
    private static final Color ORANGE_LIGHT = new DeviceRgb(255, 237, 213);
    private static final Color BORDER_COLOR = new DeviceRgb(226, 232, 240);

    public byte[] generateReservationPdf(Reservation reservation) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfDocument pdf = new PdfDocument(new PdfWriter(out));
        Document doc = new Document(pdf, PageSize.A4);
        doc.setMargins(0, 0, 40, 0);

        PdfFont bold    = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
        PdfFont regular = PdfFontFactory.createFont(StandardFonts.HELVETICA);
        PdfFont oblique = PdfFontFactory.createFont(StandardFonts.HELVETICA_OBLIQUE);

        // ── HEADER ──────────────────────────────────────────────────────
        Table header = new Table(UnitValue.createPercentArray(new float[]{60, 40}))
                .setWidth(UnitValue.createPercentValue(100))
                .setBackgroundColor(DARK).setPadding(0).setMargin(0);

        Cell brandCell = new Cell().setBorder(Border.NO_BORDER).setBackgroundColor(DARK)
                .setPaddingLeft(40).setPaddingTop(28).setPaddingBottom(28);
        brandCell.add(new Paragraph("StreetLeague")
                .setFont(bold).setFontSize(22).setFontColor(GREEN));
        brandCell.add(new Paragraph("Plateforme de reservation de terrains")
                .setFont(regular).setFontSize(9).setFontColor(GRAY));
        header.addCell(brandCell);

        Cell badgeCell = new Cell().setBorder(Border.NO_BORDER).setBackgroundColor(DARK)
                .setPaddingRight(40).setPaddingTop(28).setPaddingBottom(28)
                .setTextAlignment(TextAlignment.RIGHT);
        badgeCell.add(new Paragraph("CONFIRMATION DE RESERVATION")
                .setFont(bold).setFontSize(10).setFontColor(GREEN).setCharacterSpacing(1.5f));
        badgeCell.add(new Paragraph("N " + reservation.getId())
                .setFont(bold).setFontSize(20).setFontColor(WHITE).setMarginTop(4));
        header.addCell(badgeCell);
        doc.add(header);

        // green accent line
        Table accent = new Table(1).setWidth(UnitValue.createPercentValue(100))
                .setBackgroundColor(GREEN).setHeight(4).setPadding(0).setMargin(0);
        accent.addCell(new Cell().setBorder(Border.NO_BORDER).setHeight(4));
        doc.add(accent);

        // ── STATUT BANNER ────────────────────────────────────────────────
        boolean confirmed = "CONFIRMEE".equals(
                reservation.getStatut() != null ? reservation.getStatut().name() : "");
        Color sBg  = confirmed ? GREEN_LIGHT  : ORANGE_LIGHT;
        Color sTxt = confirmed ? GREEN_DARK   : ORANGE;
        String sLbl = confirmed ? "Reservation confirmee" : "En attente de confirmation";

        Table statutBanner = new Table(1).setWidth(UnitValue.createPercentValue(100))
                .setBackgroundColor(sBg).setPadding(0).setMargin(0);
        Cell sc = new Cell().setBorder(Border.NO_BORDER).setBackgroundColor(sBg)
                .setPaddingTop(11).setPaddingBottom(11).setPaddingLeft(40);
        sc.add(new Paragraph(sLbl).setFont(bold).setFontSize(12).setFontColor(sTxt));
        statutBanner.addCell(sc);
        doc.add(statutBanner);

        // ── BODY: left details + right QR ────────────────────────────────
        Table body = new Table(UnitValue.createPercentArray(new float[]{62, 38}))
                .setWidth(UnitValue.createPercentValue(100)).setPadding(0).setMargin(0);

        // Left column
        Cell left = new Cell().setBorder(Border.NO_BORDER)
                .setPaddingLeft(40).setPaddingRight(16).setPaddingTop(28).setPaddingBottom(20);

        left.add(new Paragraph("Details de la reservation")
                .setFont(bold).setFontSize(13).setFontColor(DARK).setMarginBottom(14));

        Table grid = new Table(UnitValue.createPercentArray(new float[]{50, 50}))
                .setWidth(UnitValue.createPercentValue(100)).setMarginBottom(18);
        addInfoCard(grid, bold, regular, "Endroit",
                reservation.fetchEndroitNom() != null ? reservation.fetchEndroitNom() : "-");
        addInfoCard(grid, bold, regular, "Sous-espace",
                reservation.fetchSousEspaceNom() != null ? reservation.fetchSousEspaceNom() : "-");
        addInfoCard(grid, bold, regular, "Debut",
                reservation.getDateDebut() != null ? reservation.getDateDebut().format(FMT) : "-");
        addInfoCard(grid, bold, regular, "Fin",
                reservation.getDateFin() != null ? reservation.getDateFin().format(FMT) : "-");
        left.add(grid);

        // Prix total
        if (reservation.getPrixTotal() != null) {
            Table prixBox = new Table(1).setWidth(UnitValue.createPercentValue(100)).setMarginBottom(14);
            Cell pc = new Cell().setBorder(new SolidBorder(GREEN, 2f))
                    .setBackgroundColor(GREEN_LIGHT).setPadding(12).setTextAlignment(TextAlignment.CENTER);
            pc.add(new Paragraph("Prix total").setFont(regular).setFontSize(9).setFontColor(GREEN_DARK));
            pc.add(new Paragraph(String.format("%.2f DT", reservation.getPrixTotal()))
                    .setFont(bold).setFontSize(22).setFontColor(GREEN_DARK).setMarginTop(2));
            prixBox.addCell(pc);
            left.add(prixBox);
        }

        // Duration
        if (reservation.getDateDebut() != null && reservation.getDateFin() != null) {
            long mins = java.time.Duration.between(reservation.getDateDebut(), reservation.getDateFin()).toMinutes();
            long h = mins / 60, m = mins % 60;
            String dur = h > 0 ? h + "h" + (m > 0 ? m + "min" : "") : m + " min";
            Table durBox = new Table(1).setWidth(UnitValue.createPercentValue(100)).setMarginBottom(16);
            Cell dc = new Cell().setBorder(new SolidBorder(GREEN, 1.5f))
                    .setBackgroundColor(LIGHT_BG).setPadding(12).setTextAlignment(TextAlignment.CENTER);
            dc.add(new Paragraph("Duree totale").setFont(regular).setFontSize(9).setFontColor(GRAY));
            dc.add(new Paragraph(dur).setFont(bold).setFontSize(24).setFontColor(GREEN).setMarginTop(2));
            durBox.addCell(dc);
            left.add(durBox);
        }

        if (reservation.getMotifAnnulation() != null && !reservation.getMotifAnnulation().isBlank()) {
            Table mb = new Table(1).setWidth(UnitValue.createPercentValue(100)).setMarginBottom(14);
            Cell mc = new Cell().setBorder(new SolidBorder(ORANGE, 1.5f))
                    .setBackgroundColor(ORANGE_LIGHT).setPadding(10);
            mc.add(new Paragraph("Motif d'annulation").setFont(bold).setFontSize(9).setFontColor(ORANGE));
            mc.add(new Paragraph(reservation.getMotifAnnulation()).setFont(regular).setFontSize(11).setFontColor(DARK));
            mb.addCell(mc);
            left.add(mb);
        }

        left.add(new LineSeparator(new SolidLine(0.5f)).setMarginBottom(10).setStrokeColor(BORDER_COLOR));
        left.add(new Paragraph("Creee le : " + (reservation.getDateCreation() != null
                ? reservation.getDateCreation().format(FMT_FULL) : "-"))
                .setFont(oblique).setFontSize(8).setFontColor(GRAY).setMarginBottom(3));
        left.add(new Paragraph("Presentez ce document a l'accueil.")
                .setFont(oblique).setFontSize(8).setFontColor(GRAY));

        body.addCell(left);

        // Right column: QR code
        Cell right = new Cell().setBorder(Border.NO_BORDER)
                .setPaddingRight(36).setPaddingLeft(10).setPaddingTop(28).setPaddingBottom(20)
                .setTextAlignment(TextAlignment.CENTER);

        right.add(new Paragraph("QR Code Admin")
                .setFont(bold).setFontSize(10).setFontColor(DARK)
                .setTextAlignment(TextAlignment.CENTER).setMarginBottom(10));

        try {
            byte[] qrBytes = generateQrCode(buildQrContent(reservation), 220);
            Image qrImage = new Image(ImageDataFactory.create(qrBytes))
                    .setWidth(155).setHorizontalAlignment(HorizontalAlignment.CENTER);
            right.add(qrImage);
        } catch (WriterException e) {
            right.add(new Paragraph("QR indisponible").setFont(regular).setFontSize(9).setFontColor(GRAY));
        }

        right.add(new Paragraph("Scannez pour voir\nles details complets")
                .setFont(oblique).setFontSize(8).setFontColor(GRAY)
                .setTextAlignment(TextAlignment.CENTER).setMarginTop(8));

        body.addCell(right);
        doc.add(body);

        // ── FOOTER ───────────────────────────────────────────────────────
        Table footer = new Table(1).setWidth(UnitValue.createPercentValue(100))
                .setBackgroundColor(DARK)
                .setFixedPosition(0, 0, PageSize.A4.getWidth()).setHeight(34);
        Cell fc = new Cell().setBorder(Border.NO_BORDER).setBackgroundColor(DARK)
                .setTextAlignment(TextAlignment.CENTER).setPaddingTop(9);
        fc.add(new Paragraph("streetleague.tn  |  contact@streetleague.tn  |  +216 XX XXX XXX")
                .setFont(regular).setFontSize(8).setFontColor(GRAY));
        footer.addCell(fc);
        doc.add(footer);

        doc.close();
        return out.toByteArray();
    }

    private String buildQrContent(Reservation reservation) {
        return "https://valid-monetary-avenue.ngrok-free.dev/api/reservations/" + reservation.getId() + "/pdf";
    }

    private byte[] generateQrCode(String content, int size) throws WriterException, IOException {
        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix matrix = writer.encode(content, BarcodeFormat.QR_CODE, size, size,
                Map.of(EncodeHintType.MARGIN, 1));
        BufferedImage image = MatrixToImageWriter.toBufferedImage(matrix);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "PNG", baos);
        return baos.toByteArray();
    }

    private void addInfoCard(Table grid, PdfFont bold, PdfFont regular, String label, String value) {
        Cell card = new Cell().setBorder(new SolidBorder(BORDER_COLOR, 1))
                .setBackgroundColor(LIGHT_BG).setPadding(12).setMargin(3);
        card.add(new Paragraph(label).setFont(bold).setFontSize(9).setFontColor(GRAY).setMarginBottom(3));
        card.add(new Paragraph(value).setFont(bold).setFontSize(11).setFontColor(DARK));
        grid.addCell(card);
    }
}
