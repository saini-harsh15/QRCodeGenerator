package com.harsh.qrgenerator.service;

import com.google.zxing.WriterException;
import com.harsh.qrgenerator.dto.request.QRCodeRequest;
import com.harsh.qrgenerator.dto.response.QRCodeResult;

import java.io.IOException;

public interface QRCodeService {

    QRCodeResult generateQRCode(QRCodeRequest request) throws WriterException, IOException;
}
