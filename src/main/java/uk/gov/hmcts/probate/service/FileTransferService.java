package uk.gov.hmcts.probate.service;

import feign.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import uk.gov.hmcts.probate.exception.BadRequestException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Slf4j
public class FileTransferService {

    private final FileTransferApi fileTransferApi;
    private static final String SV_VALID_FROM = "2019-02-02";
    private static final String SS = "bfqt";
    private static final String SRT = "sco";
    private static final String SP = "rwdlacup";
    private static final String SE_SIG_EXPIRY_DATE = "2029-02-10T18:49:08Z";
    private static final String ST_SIG_CREATION_DATE = "2020-02-10T10:49:08Z";
    private static final String SPR = "https,http";

    @Value("${ftp.client.signature}")
    private String signature;

    @Value("${ftp.client.environment}")
    private String targetEnv;

    @Autowired
    public FileTransferService(FileTransferApi fileTransferApi) {
        this.fileTransferApi = fileTransferApi;
    }

    public int uploadFile(File file) {
        log.info("Starting file upload to ftp for file:" +file.toPath() + ":" + file.getName());
        log.info("signature="+signature);
        log.info("targetEnv="+targetEnv);
        Response response;
        try {
            log.info("file.bytes.len="+Files.readAllBytes(file.toPath()).length);
            response = fileTransferApi.sendFile(
                    file,
                    targetEnv,
                    file.getName(),
                    SV_VALID_FROM,
                    SS,
                    SRT,
                    SP,
                    SE_SIG_EXPIRY_DATE,
                    ST_SIG_CREATION_DATE,
                    SPR,
                    signature);
            log.info("File transfer response: {}", response.status());
            Files.delete(file.toPath());
        } catch (IOException e) {
            log.error("Error handling file: " + e.getMessage());
            throw new BadRequestException("Failed to initiate file transfer request: " + e.getMessage());
        }
        return response.status();
    }
}
