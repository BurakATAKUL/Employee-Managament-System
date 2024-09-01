package net.javaguides.ems.service;

public interface QRCodeGenerateService {

    String generateQRCode(String text, int width, int height) throws Exception;

}
