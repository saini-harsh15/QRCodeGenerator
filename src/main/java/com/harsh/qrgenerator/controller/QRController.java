package com.harsh.qrgenerator.controller;

import com.google.zxing.WriterException;
import com.harsh.qrgenerator.dto.request.QRCodeRequest;
import com.harsh.qrgenerator.dto.response.QRCodeResult;
import com.harsh.qrgenerator.service.QRCodeService;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.io.IOException;

@Controller
public class QRController {

    private final QRCodeService qrCodeService;

    public QRController(QRCodeService qrCodeService) {
        this.qrCodeService = qrCodeService;
    }

    @PostMapping("/generate")
    @ResponseBody
    public ResponseEntity<byte[]> generateQRCode(
            @Valid @ModelAttribute QRCodeRequest request)
            throws WriterException, IOException {

        QRCodeResult qrCode = qrCodeService.generateQRCode(request);

        return ResponseEntity
                .ok()
                .contentType(qrCode.mediaType())
                .header(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.attachment()
                        .filename(qrCode.filename())
                        .build()
                        .toString())
                .body(qrCode.bytes());
    }
}
