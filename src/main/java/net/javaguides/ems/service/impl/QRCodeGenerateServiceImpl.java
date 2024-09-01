package net.javaguides.ems.service.impl;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import lombok.AllArgsConstructor;
import net.javaguides.ems.service.QRCodeGenerateService;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.WriteAbortedException;
import java.util.Base64;

@Service
@AllArgsConstructor
public class QRCodeGenerateServiceImpl implements QRCodeGenerateService  {
    @Override
    public String generateQRCode(String text, int width, int height) throws WriterException, IOException {

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

       try (ByteArrayOutputStream png = new ByteArrayOutputStream()){
           MatrixToImageWriter.writeToStream(bitMatrix, "PNG", png );
           byte[] qrCode = png.toByteArray();
           return Base64.getEncoder().encodeToString(qrCode);
       } catch (IOException e) {
           throw new RuntimeException("Error generating QR Code", e);
       }


    }
}
