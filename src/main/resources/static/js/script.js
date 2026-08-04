const form = document.getElementById("qr-form");
const urlInput = document.getElementById("text");
const formatInput = document.getElementById("format");
const logoInput = document.getElementById("logo");
const qrImage = document.getElementById("qr-image");
const qrCaption = document.getElementById("qr-caption");
const downloadButton = document.getElementById("download-btn");
const generateButton = document.getElementById("generate-btn");
const generateButtonLabel = generateButton.querySelector(".button-label");
const spinner = generateButton.querySelector(".spinner");
const resetButton = document.getElementById("reset-btn");
const copyUrlButton = document.getElementById("copy-url-btn");
const notification = document.getElementById("notification");
const themeToggle = document.getElementById("theme-toggle");
const themeIcon = document.getElementById("theme-icon");

let currentObjectUrl = null;

applySavedTheme();
syncLogoAvailability();

form.addEventListener("submit", async function (event) {
    event.preventDefault();

    clearFieldErrors();
    clearQrPreview();
    hideNotification();
    setLoading(true);

    const formData = new FormData(form);

    try {
        const response = await fetch("/generate", {
            method: "POST",
            body: formData
        });

        if (!response.ok) {
            const error = await readErrorResponse(response);
            showFieldErrors(error.fieldErrors);
            showNotification(error.message || "Unable to generate QR code.", "error");
            return;
        }

        const blob = await response.blob();
        if (!blob.size) {
            throw new Error("The server returned an empty QR code.");
        }

        currentObjectUrl = URL.createObjectURL(blob);

        qrImage.src = currentObjectUrl;
        qrImage.classList.remove("hidden");
        qrCaption.classList.remove("hidden");

        downloadButton.href = currentObjectUrl;
        downloadButton.download = getDownloadFileName(response);
        downloadButton.classList.remove("hidden");

        showNotification("QR code generated successfully.", "success");
    } catch (error) {
        clearQrPreview();
        showNotification(error.message || "Network error. Please try again.", "error");
    } finally {
        setLoading(false);
    }
});

resetButton.addEventListener("click", function () {
    form.reset();
    clearFieldErrors();
    clearQrPreview();
    hideNotification();
    syncLogoAvailability();
});

copyUrlButton.addEventListener("click", async function () {
    const url = urlInput.value.trim();
    if (!url) {
        setFieldError("text", "Enter a URL before copying.");
        return;
    }

    try {
        await navigator.clipboard.writeText(url);
        showNotification("URL copied.", "success");
    } catch (error) {
        showNotification("Could not copy the URL.", "error");
    }
});

formatInput.addEventListener("change", syncLogoAvailability);

themeToggle.addEventListener("click", function () {
    document.body.classList.toggle("dark");
    const isDark = document.body.classList.contains("dark");
    localStorage.setItem("quickqr-theme", isDark ? "dark" : "light");
    updateThemeIcon(isDark);
});

form.addEventListener("input", function (event) {
    const field = event.target.name;
    if (field) {
        setFieldError(field, "");
    }
});

function setLoading(isLoading) {
    generateButton.disabled = isLoading;
    generateButtonLabel.textContent = isLoading ? "Generating" : "Generate QR";
    spinner.classList.toggle("hidden", !isLoading);
}

function clearQrPreview() {
    if (currentObjectUrl) {
        URL.revokeObjectURL(currentObjectUrl);
        currentObjectUrl = null;
    }

    qrImage.removeAttribute("src");
    qrImage.classList.add("hidden");
    qrCaption.classList.add("hidden");
    downloadButton.removeAttribute("href");
    downloadButton.classList.add("hidden");
}

async function readErrorResponse(response) {
    const contentType = response.headers.get("content-type") || "";
    if (contentType.includes("application/json")) {
        return response.json();
    }

    return {
        message: response.status >= 500
            ? "Server error. Please try again."
            : "Request failed. Please check your input.",
        fieldErrors: {}
    };
}

function showFieldErrors(fieldErrors = {}) {
    Object.entries(fieldErrors).forEach(([field, message]) => {
        setFieldError(field, message);
    });
}

function setFieldError(field, message) {
    const errorElement = document.getElementById(`${field}-error`);
    const input = form.elements[field];

    if (errorElement) {
        errorElement.textContent = message;
    }

    if (input) {
        input.classList.toggle("invalid", Boolean(message));
    }
}

function clearFieldErrors() {
    form.querySelectorAll(".field-error").forEach((element) => {
        element.textContent = "";
    });

    form.querySelectorAll(".invalid").forEach((element) => {
        element.classList.remove("invalid");
    });
}

function showNotification(message, type) {
    notification.textContent = message;
    notification.className = `notification ${type}`;
}

function hideNotification() {
    notification.textContent = "";
    notification.className = "notification hidden";
}

function getDownloadFileName(response) {
    const contentDisposition = response.headers.get("content-disposition") || "";
    const match = contentDisposition.match(/filename="?([^"]+)"?/i);

    if (match) {
        return match[1];
    }

    const baseName = (form.elements.fileName.value || "quickqr").trim().replace(/[^a-zA-Z0-9._-]/g, "-");
    const format = form.elements.format.value || "png";
    return baseName.endsWith(`.${format}`) ? baseName : `${baseName}.${format}`;
}

function syncLogoAvailability() {
    const isSvg = formatInput.value === "svg";
    logoInput.disabled = isSvg;
    if (isSvg) {
        logoInput.value = "";
        setFieldError("logo", "");
    }
}

function applySavedTheme() {
    const isDark = localStorage.getItem("quickqr-theme") === "dark";
    if (isDark) {
        document.body.classList.add("dark");
    }
    updateThemeIcon(isDark);
}

function updateThemeIcon(isDark) {
    themeIcon.innerHTML = isDark ? "&#9789;" : "&#9728;";
    themeToggle.setAttribute("aria-label", isDark ? "Switch to light mode" : "Switch to dark mode");
}