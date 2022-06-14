package crm.wealth.management.service;

import java.io.IOException;
import java.util.*;

import com.amazonaws.services.s3.model.*;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.amazonaws.services.s3.AmazonS3Client;

@Service
public class AWSS3Service {
    @Value("${cloud.aws.credentials.bucketName}")
    private String bucketName;
    @Autowired
    private AmazonS3Client awsS3Client;

    public String uploadFile(MultipartFile file) {
        String key = generateFileName(file);
        ObjectMetadata metaData = new ObjectMetadata();
        metaData.setContentLength(file.getSize());
        metaData.setContentType(file.getContentType());

        try {
            awsS3Client.putObject(bucketName, key, file.getInputStream(), metaData);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An exception occured while uploading the file");
        }

        awsS3Client.setObjectAcl(bucketName, key, CannedAccessControlList.PublicRead);

        return awsS3Client.getResourceUrl(bucketName, key);
    }

    private String generateFileName(MultipartFile multiPart) {
        return new LocalDate() + "-" + multiPart.getOriginalFilename().replace(" ", "_");
    }

    public Map<String, String> listFiles() {

        ListObjectsRequest listObjectsRequest =
                new ListObjectsRequest()
                        .withBucketName(bucketName);

        List<String> keys = new ArrayList<>();

        ObjectListing objects = awsS3Client.listObjects(listObjectsRequest);

        while (true) {
            List<S3ObjectSummary> objectSummaries = objects.getObjectSummaries();
            if (objectSummaries.size() < 1) {
                break;
            }

            for (S3ObjectSummary item : objectSummaries) {
                if (!item.getKey().endsWith("/"))
                    keys.add(item.getKey());
            }

            objects = awsS3Client.listNextBatchOfObjects(objects);
        }

        Map<String, String> response = new HashMap<>();
        for (String nameFile : keys) {
            response.put(nameFile, awsS3Client.getResourceUrl(bucketName,nameFile));
        }
        return response;
    }
}
