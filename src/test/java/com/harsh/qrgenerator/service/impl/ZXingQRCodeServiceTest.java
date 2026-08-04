package com.harsh.qrgenerator.service.impl;

import com.google.zxing.WriterException;
import com.harsh.qrgenerator.dto.request.QRCodeRequest;
import com.harsh.qrgenerator.dto.response.QRCodeResult;
import com.harsh.qrgenerator.exception.InvalidLogoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class ZXingQRCodeServiceTest {

    private ZXingQRCodeService service;

    @BeforeEach
    void setUp() {
        service = new ZXingQRCodeService();
    }

    @Test
    void generateQRCode_validPngRequest_returnsPngResult()
            throws WriterException, IOException {

        // Arrange
        QRCodeRequest request = new QRCodeRequest();
        request.setText("https://github.com");
        request.setFormat("png");
        request.setFileName("github");

        // Act
        QRCodeResult result = service.generateQRCode(request);

        // Assert
        assertNotNull(result);
        assertEquals(MediaType.IMAGE_PNG, result.mediaType());
        assertEquals("github.png", result.filename());
        assertNotNull(result.bytes());
        assertTrue(result.bytes().length > 0);
    }

    @Test
    void generateQRCode_validSvgRequest_returnsSvgResult()
            throws WriterException, IOException {

        // Arrange
        QRCodeRequest request = new QRCodeRequest();
        request.setText("https://spring.io");
        request.setFormat("svg");
        request.setFileName("spring");

        // Act
        QRCodeResult result = service.generateQRCode(request);

        // Assert
        assertNotNull(result);
        assertEquals(MediaType.valueOf("image/svg+xml"), result.mediaType());
        assertEquals("spring.svg", result.filename());

        String svg = new String(result.bytes());

        assertTrue(svg.startsWith("<svg"));
        assertTrue(svg.contains("</svg>"));
    }

    @Test
    void generateQRCode_blankFilename_returnsDefaultFilename()
            throws WriterException, IOException {

        // Arrange
        QRCodeRequest request = new QRCodeRequest();
        request.setText("https://openai.com");
        request.setFormat("png");
        request.setFileName("");

        // Act
        QRCodeResult result = service.generateQRCode(request);

        // Assert
        assertEquals("quickqr.png", result.filename());
    }

    @Test
    void generateQRCode_invalidCharactersInFilename_sanitizesFilename()
            throws WriterException, IOException {

        // Arrange
        QRCodeRequest request = new QRCodeRequest();
        request.setText("https://google.com");
        request.setFormat("png");
        request.setFileName("my*qr?/code");

        // Act
        QRCodeResult result = service.generateQRCode(request);

        // Assert
        assertEquals("my-qr--code.png", result.filename());
    }

    @Test
    void generateQRCode_svgWithLogo_throwsInvalidLogoException() {

        // Arrange
        QRCodeRequest request = new QRCodeRequest();
        request.setText("https://github.com");
        request.setFormat("svg");

        MockMultipartFile logo =
                new MockMultipartFile(
                        "logo",
                        "logo.png",
                        "image/png",
                        new byte[]{1, 2, 3}
                );

        request.setLogo(logo);

        // Act & Assert
        assertThrows(
                InvalidLogoException.class,
                () -> service.generateQRCode(request)
        );
    }

    @Test
    void generateQRCode_nullFilename_returnsDefaultFilename()
            throws WriterException, IOException {

        QRCodeRequest request = new QRCodeRequest();
        request.setText("https://openai.com");
        request.setFormat("png");
        request.setFileName(null);

        QRCodeResult result = service.generateQRCode(request);

        assertEquals("quickqr.png", result.filename());
    }

    @Test
    void generateQRCode_nullFormat_defaultsToPng()
            throws WriterException, IOException {

        QRCodeRequest request = new QRCodeRequest();
        request.setText("https://spring.io");
        request.setFormat(null);

        QRCodeResult result = service.generateQRCode(request);

        assertEquals(MediaType.IMAGE_PNG, result.mediaType());
        assertEquals("quickqr.png", result.filename());
    }

    @Test
    void generateQRCode_blankFormat_defaultsToPng()
            throws WriterException, IOException {

        QRCodeRequest request = new QRCodeRequest();
        request.setText("https://github.com");
        request.setFormat("");

        QRCodeResult result = service.generateQRCode(request);

        assertEquals(MediaType.IMAGE_PNG, result.mediaType());
    }

    @Test
    void generateQRCode_uppercaseFormat_returnsPng()
            throws WriterException, IOException {

        QRCodeRequest request = new QRCodeRequest();
        request.setText("https://google.com");
        request.setFormat("PNG");

        QRCodeResult result = service.generateQRCode(request);

        assertEquals(MediaType.IMAGE_PNG, result.mediaType());
    }

    @Test
    void generateQRCode_filenameEndingWithDot_removesTrailingDot()
            throws WriterException, IOException {

        QRCodeRequest request = new QRCodeRequest();
        request.setText("https://github.com");
        request.setFileName("myfile.");

        QRCodeResult result = service.generateQRCode(request);

        assertEquals("myfile.png", result.filename());
    }

    @Test
    void generateQRCode_invalidFilename_returnsDefaultFilename()
            throws WriterException, IOException {

        QRCodeRequest request = new QRCodeRequest();
        request.setText("https://github.com");
        request.setFileName("***???///");

        QRCodeResult result = service.generateQRCode(request);

        assertEquals("quickqr.png", result.filename());
    }

    @Test
    void generateQRCode_emptyLogo_generatesPng()
            throws WriterException, IOException {

        QRCodeRequest request = new QRCodeRequest();
        request.setText("https://spring.io");

        MockMultipartFile logo =
                new MockMultipartFile(
                        "logo",
                        "",
                        "image/png",
                        new byte[0]
                );

        request.setLogo(logo);

        QRCodeResult result = service.generateQRCode(request);

        assertEquals(MediaType.IMAGE_PNG, result.mediaType());
    }

    @Test
    void generateQRCode_invalidImageLogo_throwsInvalidLogoException() {

        QRCodeRequest request = new QRCodeRequest();
        request.setText("https://github.com");

        MockMultipartFile logo =
                new MockMultipartFile(
                        "logo",
                        "logo.txt",
                        "text/plain",
                        "This is not an image".getBytes()
                );

        request.setLogo(logo);

        assertThrows(
                InvalidLogoException.class,
                () -> service.generateQRCode(request)
        );
    }

    @Test
    void generateQRCode_logoLargerThanOneMb_throwsInvalidLogoException() {

        QRCodeRequest request = new QRCodeRequest();
        request.setText("https://github.com");

        byte[] largeFile = new byte[1_000_001];

        MockMultipartFile logo =
                new MockMultipartFile(
                        "logo",
                        "logo.png",
                        "image/png",
                        largeFile
                );

        request.setLogo(logo);

        assertThrows(
                InvalidLogoException.class,
                () -> service.generateQRCode(request)
        );
    }

}