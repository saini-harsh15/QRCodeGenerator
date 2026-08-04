package com.harsh.qrgenerator.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

public class QRCodeRequest {

    @NotBlank(message = "URL cannot be empty")
    @Size(max = 2048, message = "URL must be 2048 characters or fewer")
    @Pattern(
            regexp = "^(https?://).+",
            message = "URL must start with http:// or https://"
    )
    private String text;

    @Pattern(
            regexp = "^#[0-9a-fA-F]{6}$",
            message = "Foreground color must be a valid hex color"
    )
    private String foregroundColor = "#111827";

    @Pattern(
            regexp = "^#[0-9a-fA-F]{6}$",
            message = "Background color must be a valid hex color"
    )
    private String backgroundColor = "#ffffff";

    @Pattern(
            regexp = "^(png|svg)$",
            message = "Format must be png or svg"
    )
    private String format = "png";

    @Size(max = 80, message = "File name must be 80 characters or fewer")
    private String fileName = "quickqr";

    private MultipartFile logo;

    public QRCodeRequest() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getForegroundColor() {
        return foregroundColor;
    }

    public void setForegroundColor(String foregroundColor) {
        this.foregroundColor = foregroundColor;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public MultipartFile getLogo() {
        return logo;
    }

    public void setLogo(MultipartFile logo) {
        this.logo = logo;
    }
}
