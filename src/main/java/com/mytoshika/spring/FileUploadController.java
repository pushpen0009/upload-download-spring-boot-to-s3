package com.mytoshika.spring;

import com.amazonaws.services.s3.internal.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.List;

@RestController("/api")
public class FileUploadController {

    final int UPLOAD_PART_SIZE = 5 * Constants.KB;

    @Autowired
    private S3Utils s3Utils;

    @Autowired
    private OrderService service;
    @PostMapping(value = "/file/upload", consumes = "multipart/form-data")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            File fileObj = convertMultiPartToFile(file);
            s3Utils.uploadFileFromS3(file.getOriginalFilename(), new FileInputStream(fileObj));
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<String>(HttpStatus.OK);
    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }

    @GetMapping(value = "/file/download")
    public ResponseEntity<String> downloadFile(@RequestParam("fileName") String fileName) {
        System.out.println("DOWNLOAD FILE NAME : " + fileName);
        try {
            String key = fileName;
            final int UPLOAD_PART_SIZE = 10 * Constants.KB;
            InputStream io = s3Utils.downloadFileFromS3(key);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE,"application/json").body("{status: DONE}");
    }

    @GetMapping(value = "/orders")
    public List<Order> getOrders(){
        return service.getAllOrders();
    }

    @GetMapping(value = "/order/import")
    public ResponseEntity<String> importFileToS3(@RequestParam("fileName") String fileName) {
        System.out.println("EXPORT FILE NAME : " + fileName);
        try {
            service.uploadPartsFile(fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "application/json").body("{status: DONE}");
    }
}
