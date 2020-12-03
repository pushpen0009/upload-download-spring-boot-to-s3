package com.mytoshika.spring;

import com.amazonaws.util.IOUtils;
import com.mytoshika.spring.utils.Utils;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.ProgressListener;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;

@RestController
public class FileUploadController {

    @Autowired
    private S3Utils s3Utils;

    @PostMapping(value = "/file/upload", consumes = "multipart/form-data")
    public ResponseEntity<String> uploadFile(HttpServletRequest request) {
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        if (isMultipart) {
            try {
                ServletFileUpload upload = new ServletFileUpload();
                ProgressListener progressListener = new TestProgressListener();
                upload.setProgressListener(progressListener);
                FileItemIterator itemIterator = upload.getItemIterator(request);
                while (itemIterator.hasNext()) {
                    FileItemStream item = itemIterator.next();
                    String name = item.getFieldName();
                    InputStream stream = item.openStream();
                    System.out.println("UPLOAD FILE NAME : " + item.getName());
                    s3Utils.uploadFileFromS3(item.getName(), stream);
                }
            } catch (FileUploadException | IOException e) {
                e.printStackTrace();
                return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
            return new ResponseEntity<String>(HttpStatus.OK);
        } else {
            return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/file/download")
    public ResponseEntity<byte[]> downloadFile(@RequestParam("fileName") String fileName) {
        System.out.println("DOWNLOAD FILE NAME : " + fileName);
        byte[] content = null;
        try {
            String key = fileName;
            InputStream io = s3Utils.downloadFileFromS3(key);
            System.out.println("DOWNLOAD FILE NAME : " + fileName /*+ " NO OF LINES : "+ Utils.countNoOfFiles(io, fileName)*/);
            content = IOUtils.toByteArray(io);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"").body(content);

    }
}
