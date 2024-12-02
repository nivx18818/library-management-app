package app.libmgmt.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import app.libmgmt.service.external.GoogleBooksApiService;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.json.JSONObject;

public class QRCodeGenerator {
    public static Image generateQRCode(String url, int width, int height) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(url, BarcodeFormat.QR_CODE, width, height);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);

        return new Image(new ByteArrayInputStream(outputStream.toByteArray()));
    }

    public static void setWebReaderQrCode(String title, ImageView imageView, int width, int height) {
        if (title == null || title.trim().isEmpty()) {
            return;
        }

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                JSONObject response = GoogleBooksApiService.searchBook(title, 1);
                if (response != null && response.has("items")) {
                    var items = response.getJSONArray("items");
                    var book = items.getJSONObject(0).getJSONObject("accessInfo");
                    if (items.length() > 0 && book != null) {
                        String webReaderLink = book.optString("webReaderLink", "No Web Reader Link");
                        System.out.println("Link: " + webReaderLink);
                        if (!"No Web Reader Link".equals(webReaderLink)) {
                            try {
                                Platform.runLater(() -> {
                                    try {
                                        imageView
                                                .setImage(QRCodeGenerator.generateQRCode(webReaderLink, width, height));
                                    } catch (WriterException | IOException e) {
                                        e.printStackTrace();
                                    }
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }
                }

                return null;
            }
        };

        new Thread(task).start();
    }
}
