package com.mytoshika.spring;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.internal.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {
    @Autowired
    OrderRepository repository;

    @Value("${aws.s3.bucket.name}")
    private String bucketName;

    @Autowired
    private AmazonS3 s3Client;

    @Autowired
    private S3MultipartUpload s3MultipartUpload;

    final int UPLOAD_PART_SIZE = 5 * Constants.MB;
    final int BATCH_SIZE = 300000;

    public List<Order> getAllOrders(){
        List<Order> orders = new ArrayList<>();
        repository.findAll().forEach(order -> orders.add(order));
        return orders;
    }

    public void uploadPartsFile(String filename){
        List<Order> orders = new ArrayList<>();
        repository.findAll().forEach(order -> orders.add(order));
        s3MultipartUpload.initializeUpload(bucketName, filename);
        List<Order> tempList = null;
        int count = 0;
        tempList = new ArrayList<>(BATCH_SIZE);
        for(Order order: orders){
            count++;
            tempList.add(order);
            if(count % BATCH_SIZE == 0){
                boolean isEnd = count == orders.size();
                processDbToS3(tempList,isEnd);
                tempList = new ArrayList<>(BATCH_SIZE);
            }
        }
        if(!tempList.isEmpty()){
            processDbToS3(tempList, true);
        }
    }

    private void processDbToS3(List<Order> tempList, boolean isEndPart) {
        StringBuilder builder = new StringBuilder();
        for(Order order: tempList){
            builder.append(order.getOrder_id()).append(',').append(order.getItem()).append(',').append(order.getAmount()).append(System.lineSeparator());
        }
        ByteArrayInputStream stream = new ByteArrayInputStream(builder.toString().getBytes());
        if(!isEndPart){
            s3MultipartUpload.uploadPartAsync(stream);
        }else{
            s3MultipartUpload.uploadFinalPartAsync(stream);
        }
    }
}
