package com.orderprocessing.services;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;

/**
 * Service for FTP functions.
 *
 * @author Gabor Tavali
 */
@Service
@Slf4j
public class FtpService {

    @Value("${ftp.server}")
    private String server;

    @Value("${ftp.port}")
    private int port;

    @Value("${ftp.user}")
    private String user;

    @Value("${ftp.passw}")
    private String password;

    private FTPClient ftpClient = new FTPClient();

    public void upload(File file) {
        try {
            ftpClient.connect(server, port);
            ftpClient.login(user, password);
            ftpClient.enterLocalPassiveMode();

            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

            String fileName = "response-" + LocalDate.now() + ".csv";
            InputStream inputStream = new FileInputStream(file);

            log.info("Upload is started to server " + server + ".");

            if (ftpClient.storeFile(fileName, inputStream)) {
                inputStream.close();
                log.info("Upload to " + server + " was successful.");
            }
        } catch (IOException ex) {
            log.error("FTP server is unavailable.", ex);
        }
    }

}
