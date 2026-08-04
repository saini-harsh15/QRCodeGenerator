package com.harsh.qrgenerator.service.impl;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.harsh.qrgenerator.dto.request.QRCodeRequest;
import com.harsh.qrgenerator.dto.response.QRCodeResult;
import com.harsh.qrgenerator.exception.InvalidLogoException;
import com.harsh.qrgenerator.service.QRCodeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.EnumMap;
import java.util.Map;

@Service
public class ZXingQRCodeService implements QRCodeService {

    private static final Logger log = LoggerFactory.getLogger(ZXingQRCodeService.class);
    private static final int SIZE = 500;
    private static final int MAX_LOGO_SIZE_BYTES = 1_000_000;

    @Override
    public QRCodeResult generateQRCode(QRCodeRequest request) throws WriterException, IOException {
        String format = normalizeFormat(request.getFormat());
        String filename = buildFilename(request.getFileName(), format);
        BitMatrix bitMatrix = createBitMatrix(request.getText());

        log.info("Generating {} QR code for URL length {}", format.toUpperCase(), request.getText().length());

        if ("svg".equals(format)) {
            if (hasLogo(request.getLogo())) {
                throw new InvalidLogoException("Logo upload is only supported for PNG downloads");
            }
            return new QRCodeResult(
                    createSvg(bitMatrix, request.getForegroundColor(), request.getBackgroundColor())
                            .getBytes(StandardCharsets.UTF_8),
                    MediaType.valueOf("image/svg+xml"),
                    filename
            );
        }

        byte[] bytes = createPng(bitMatrix, request.getForegroundColor(), request.getBackgroundColor(), request.getLogo());
        return new QRCodeResult(bytes, MediaType.IMAGE_PNG, filename);
    }

    private BitMatrix createBitMatrix(String text) throws WriterException {
        Map<EncodeHintType, Object> hints = new EnumMap<>(EncodeHintType.class);
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.MARGIN, 2);

        return new QRCodeWriter().encode(text, BarcodeFormat.QR_CODE, SIZE, SIZE, hints);
    }

    private byte[] createPng(BitMatrix bitMatrix, String foregroundColor, String backgroundColor, MultipartFile logo)
            throws IOException {

        BufferedImage image = MatrixToImageWriter.toBufferedImage(
                bitMatrix,
                new com.google.zxing.client.j2se.MatrixToImageConfig(
                        Color.decode(foregroundColor).getRGB(),
                        Color.decode(backgroundColor).getRGB()
                )
        );

        if (hasLogo(logo)) {
            drawLogo(image, logo);
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "PNG", outputStream);
        return outputStream.toByteArray();
    }

    private void drawLogo(BufferedImage qrImage, MultipartFile logoFile) throws IOException {
        if (logoFile.getSize() > MAX_LOGO_SIZE_BYTES) {
            throw new InvalidLogoException("Logo image must be 1 MB or smaller");
        }

        BufferedImage logo = ImageIO.read(logoFile.getInputStream());
        if (logo == null) {
            throw new InvalidLogoException("Logo must be a valid image file");
        }

        int logoSize = qrImage.getWidth() / 5;
        int x = (qrImage.getWidth() - logoSize) / 2;
        int y = (qrImage.getHeight() - logoSize) / 2;

        Graphics2D graphics = qrImage.createGraphics();
        try {
            graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            graphics.setColor(Color.WHITE);
            graphics.fillRoundRect(x - 8, y - 8, logoSize + 16, logoSize + 16, 20, 20);
            graphics.drawImage(logo, x, y, logoSize, logoSize, null);
        } finally {
            graphics.dispose();
        }
    }

    private String createSvg(BitMatrix bitMatrix, String foregroundColor, String backgroundColor) {
        StringBuilder svg = new StringBuilder();
        svg.append("<svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 ")
                .append(SIZE)
                .append(' ')
                .append(SIZE)
                .append("\" role=\"img\">");
        svg.append("<rect width=\"100%\" height=\"100%\" fill=\"")
                .append(backgroundColor)
                .append("\"/>");
        svg.append("<path fill=\"")
                .append(foregroundColor)
                .append("\" d=\"");

        int moduleSize = SIZE / bitMatrix.getWidth();
        for (int y = 0; y < bitMatrix.getHeight(); y++) {
            for (int x = 0; x < bitMatrix.getWidth(); x++) {
                if (bitMatrix.get(x, y)) {
                    svg.append('M')
                            .append(x * moduleSize)
                            .append(' ')
                            .append(y * moduleSize)
                            .append("h")
                            .append(moduleSize)
                            .append("v")
                            .append(moduleSize)
                            .append("h-")
                            .append(moduleSize)
                            .append('z');
                }
            }
        }

        svg.append("\"/></svg>");
        return svg.toString();
    }

    private boolean hasLogo(MultipartFile logo) {
        return logo != null && !logo.isEmpty();
    }

    private String normalizeFormat(String format) {
        return format == null || format.isBlank() ? "png" : format.toLowerCase();
    }

    private String buildFilename(String requestedName, String format) {
        String baseName = requestedName == null || requestedName.isBlank() ? "quickqr" : requestedName;
        String sanitized = baseName.replaceAll("[^a-zA-Z0-9._-]", "-");
        sanitized = sanitized.replaceAll("\\.+$", "");

        if (!sanitized.matches(".*[a-zA-Z0-9].*")) {
            sanitized = "quickqr";
        }else if (sanitized.isBlank()) {
            sanitized = "quickqr";
        }
        return sanitized.endsWith("." + format) ? sanitized : sanitized + "." + format;
    }
}
