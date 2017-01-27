import java.util.List;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.InvalidKeyException;

import org.xmlpull.v1.XmlPullParserException;

import io.minio.MinioClient;
import io.minio.errors.MinioException;
import io.minio.messages.Bucket;
import io.minio.messages.Item;
import io.minio.Result;

public class Atmos {
    public static void main(String[] args)
    throws IOException, NoSuchAlgorithmException, InvalidKeyException, XmlPullParserException {
        FindBucket("testing");
        CreateBucket("my-bucketname");
        ListDirectories();
        GetDirectory("my-bucketname");
        UploadFile("my-bucketname", "my-testfile.csv");
        DownloadFile("my-bucketname", "my-testfile.csv", "my-download.csv");
        DeleteFile("my-bucketname", "my-testfile.csv");
        DeleteDirectory("my-bucketname");

    }

    public static void FindBucket(String name)
    throws IOException, NoSuchAlgorithmException, InvalidKeyException, XmlPullParserException {
        try {

            MinioClient minioClient = new MinioClient(
                System.getenv("UKCLOUD_S3_HOST"),
                String.format("%s/%s", System.getenv("UKCLOUD_S3_UID"), System.getenv("UKCLOUD_S3_SUBTENANT")),
                System.getenv("UKCLOUD_S3_SECRET"));

            // Check whether 'my-bucketname' exist or not.
            boolean found = minioClient.bucketExists(name);
            if (found) {
                System.out.println(name + " exists");
            } else {
                System.out.println(name + " does not exist");
            }

        } catch (MinioException e) {
            System.out.println("Error occurred: " + e);
        }
    }

    public static void CreateBucket(String name)
    throws IOException, NoSuchAlgorithmException, InvalidKeyException, XmlPullParserException {
        try {
            MinioClient minioClient = new MinioClient(
                System.getenv("UKCLOUD_S3_HOST"),
                String.format("%s/%s", System.getenv("UKCLOUD_S3_UID"), System.getenv("UKCLOUD_S3_SUBTENANT")),
                System.getenv("UKCLOUD_S3_SECRET"));


            // Create bucket if it doesn't exist.
            boolean found = minioClient.bucketExists(name);
            if (found) {
                System.out.println(name + " already exists");
            } else {
                // Create bucket 'my-bucketname'.
                minioClient.makeBucket(name);
                System.out.println(name + "was created successfully");
            }
        } catch (MinioException e) {
            System.out.println("Error occurred: " + e);
        }
    }



    public static void ListDirectories()
    throws IOException, NoSuchAlgorithmException, InvalidKeyException, XmlPullParserException {
        try {
            MinioClient minioClient = new MinioClient(
                System.getenv("UKCLOUD_S3_HOST"),
                String.format("%s/%s", System.getenv("UKCLOUD_S3_UID"), System.getenv("UKCLOUD_S3_SUBTENANT")),
                System.getenv("UKCLOUD_S3_SECRET"));


            // List buckets we have atleast read access.
            List<Bucket> bucketList = minioClient.listBuckets();
            for (Bucket bucket : bucketList) {
                System.out.println(bucket.creationDate() + ", " + bucket.name());
            }
        } catch (MinioException e) {
            System.out.println("Error occurred: " + e);
        }
    }

    public static void GetDirectory(String name)
    throws IOException, NoSuchAlgorithmException, InvalidKeyException, XmlPullParserException {
        try {
            MinioClient minioClient = new MinioClient(
                System.getenv("UKCLOUD_S3_HOST"),
                String.format("%s/%s", System.getenv("UKCLOUD_S3_UID"), System.getenv("UKCLOUD_S3_SUBTENANT")),
                System.getenv("UKCLOUD_S3_SECRET"));

            // Check whether 'my-bucketname' exist or not.
            boolean found = minioClient.bucketExists(name);
            if (found) {
                // List objects from 'my-bucketname'
                Iterable<Result<Item>> myObjects = minioClient.listObjects(name);
                for (Result<Item> result : myObjects) {
                    Item item = result.get();
                    System.out.println(item.lastModified() + ", " + item.size() + ", " + item.objectName());
                }
            } else {
                System.out.println(name + " does not exist");
            }
        } catch (MinioException e) {
            System.out.println("Error occurred: " + e);
        }
    }

    public static void UploadFile(String directoryName, String filePath)
    throws IOException, NoSuchAlgorithmException, InvalidKeyException, XmlPullParserException {
        try {
            MinioClient minioClient = new MinioClient(
                System.getenv("UKCLOUD_S3_HOST"),
                String.format("%s/%s", System.getenv("UKCLOUD_S3_UID"), System.getenv("UKCLOUD_S3_SUBTENANT")),
                System.getenv("UKCLOUD_S3_SECRET"));

            String objectName = "my-testfile.csv";

            minioClient.putObject(directoryName, objectName, filePath);
            System.out.println(filePath + " uploaded to my-objectname successfully");
        } catch (MinioException e) {
            System.out.println("Error occurred: " + e);
        }
    }

    public static void DownloadFile(String directoryName, String fileName, String downloadPath)
    throws IOException, NoSuchAlgorithmException, InvalidKeyException, XmlPullParserException {
        try {
            MinioClient minioClient = new MinioClient(
                System.getenv("UKCLOUD_S3_HOST"),
                String.format("%s/%s", System.getenv("UKCLOUD_S3_UID"), System.getenv("UKCLOUD_S3_SUBTENANT")),
                System.getenv("UKCLOUD_S3_SECRET"));

            // Check whether the object exists using statObject().  If the object is not found,
            // statObject() throws an exception.  It means that the object exists when statObject()
            // execution is successful.
            minioClient.statObject(directoryName, fileName);

            // Download 'my-objectname' from 'my-bucketname' to 'my-filename'
            minioClient.getObject(directoryName, fileName, downloadPath);
            System.out.println(directoryName + " successfully downloaded to " + downloadPath);
        } catch (MinioException e) {
            System.out.println("Error occurred: " + e);
        }
    }


    public static void DeleteFile(String name, String name2) {
        // Prints "Hello, World" in the terminal window.
        System.out.println(name);
        System.out.println(name2);
    }

    public static void DeleteDirectory(String name) {
        // Prints "Hello, World" in the terminal window.
        System.out.println(name);
    }

}
