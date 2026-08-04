package com.harsh.qrgenerator.dto.response;

import org.springframework.http.MediaType;

public record QRCodeResult(
        byte[] bytes,
        MediaType mediaType,
        String filename
) {
}
