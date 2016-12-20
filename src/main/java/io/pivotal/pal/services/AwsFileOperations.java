package io.pivotal.pal.services;


import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Profile("S3")
public class AwsFileOperations implements FileOperations{

    @Autowired
    private AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Override
    public void create(String filePath, String content) throws Exception {
        ByteArrayInputStream is = new ByteArrayInputStream(content.getBytes());
        upload(is, fileName(filePath));
    }

    @Override
    public void delete(String filePath) {
        String key = keyFromS3ObjectString(filePath);
        amazonS3Client.deleteObject(bucket, key);
    }


    /**
     * Sanitize s3 object key from passed string
     * @param objectString metadata represenging soted s3 object
     * @return key of stored s3 object
     */
    private String keyFromS3ObjectString(String objectString) {
        String parts[] = objectString.split(",");
        Pattern p = Pattern.compile("\'([^\"]*)\'");
        for (String part : parts) {
            if (part.trim().startsWith("key")){
                Matcher m = p.matcher(part);
                while (m.find()) {
                    return m.group(1);
                }
            }
        }
        return "";
    }

    public String listCreatedFile(String key) {
        ObjectListing objectListing = amazonS3Client.listObjects(new ListObjectsRequest().withBucketName(bucket));

        List<S3ObjectSummary> s3ObjectSummaries = objectListing.getObjectSummaries();

        for (S3ObjectSummary objectSummary : s3ObjectSummaries) {
            if (key.equals(objectSummary.getKey())) return objectSummary.toString();
        }
        return "NOT FOUND.";
    }

    private String fileName(String filePath) {
        Path p = Paths.get(filePath);
        return p.getFileName().toString();
    }

    private PutObjectResult upload(InputStream inputStream, String uploadKey) {
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, uploadKey, inputStream, new ObjectMetadata());
        putObjectRequest.setCannedAcl(CannedAccessControlList.PublicRead);
        PutObjectResult putObjectResult = amazonS3Client.putObject(putObjectRequest);
        IOUtils.closeQuietly(inputStream);

        return putObjectResult;
    }
}
