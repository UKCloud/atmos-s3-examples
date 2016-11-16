/*
 * Example library for connecting Atmos S3 buckets.
 *
 * Strictly Work in Progress.
 * NOTE: File is very procedural just for example purposes.
 * Adding tests will fix this.
 *
 * Author: Bobby DeVeaux (bdeveaux@ukcloud.com)
 *
 */

package main

import (
	"fmt"
	"os"

	"github.com/minio/minio-go"
)

type s3 struct {
	Client *minio.Client
}

func main() {
	FindBucket("my-bucketname")
	CreateBucket("my-bucketname")
	ListDirectories()
	GetDirectory("my-bucketname")
	UploadFile("my-bucketname", "my-testfile.csv")
	DownloadFile("my-bucketname", "my-testfile.csv", "my-download.csv")
	DeleteFile("my-bucketname", "my-testfile.csv")
	DeleteDirectory("my-bucketname")
}

func GetClient() *minio.Client {
	s3Client, err := minio.NewV2(
		fmt.Sprintf("%s:%d", os.Getenv("UKCLOUD_S3_HOST"), 8443),
		fmt.Sprintf("%s/%s", os.Getenv("UKCLOUD_S3_UID"), os.Getenv("UKCLOUD_S3_SUBTENANT")),
		os.Getenv("UKCLOUD_S3_SECRET"),
		true)
	if err != nil {
		fmt.Println(err)
	}

	return s3Client
}

func FindBucket(bucketName string) {
	s3Client := GetClient()
	found, err := s3Client.BucketExists(bucketName)
	if err != nil {
		//fmt.Println(err)
	}

	if found {
		fmt.Println("Bucket found.")
	} else {
		fmt.Println("Bucket not found.")
	}
}

func CreateBucket(bucketName string) {
	s3Client := GetClient()
	err := s3Client.MakeBucket(bucketName, "us-east-1")
	if err != nil {
		fmt.Println(err)
	}
	fmt.Println("Bucket", bucketName, "created successfully.")
}

func ListDirectories() []minio.BucketInfo {
	s3Client := GetClient()
	buckets, err := s3Client.ListBuckets()
	if err != nil {
		fmt.Println(err)
	}
	//for _, bucket := range buckets {
	//fmt.Println(bucket)
	//}

	return buckets
}

/**
 * This method needs work as it assumes a limit of 50 objects
 */
func GetDirectory(directoryName string) []minio.ObjectInfo {
	s3Client := GetClient()
	// Create a done channel to control 'ListObjects' go routine.
	doneCh := make(chan struct{})

	// Indicate to our routine to exit cleanly upon return.
	defer close(doneCh)

	// grab the objects
	objects := make([]minio.ObjectInfo, 50)
	i := 0

	for object := range s3Client.ListObjects(directoryName, "", true, doneCh) {
		if object.Err != nil {
			//fmt.Println(object.Err)
		}
		objects[i] = object
		i++
	}

	return objects
}

func UploadFile(directoryName string, filePath string) {
	s3Client := GetClient()
	objectName := "my-testfile.csv"
	contentType := "plain/text"

	n, err := s3Client.FPutObject(directoryName, objectName, filePath, contentType)
	if err != nil {
		fmt.Println(err)
	}
	fmt.Println("Uploaded", objectName, "of size:", n, "Successfully to:", directoryName, "/", filePath)
}

func DownloadFile(directoryName string, fileName string, downloadPath string) {
	s3Client := GetClient()
	if err := s3Client.FGetObject(directoryName, fileName, downloadPath); err != nil {
		fmt.Println(err)
	}
	fmt.Println("Successfully saved my-download.csv")
}

func DeleteFile(directoryName string, fileName string) {
	s3Client := GetClient()
	err := s3Client.RemoveObject(directoryName, fileName)
	if err != nil {
		fmt.Println(err)
	}
	fmt.Println("Successfully deleted", fileName)
}

func DeleteDirectory(directoryName string) {
	s3Client := GetClient()
	err := s3Client.RemoveBucket(directoryName)
	if err != nil {
		fmt.Println(err)
	}
	fmt.Println("Successfully deleted", directoryName)
}

/*
	//### get_files(directory_name)
	//@@TODO

	//### get_file(directory_name, file_name)
	//@@TODO


	//### get_public_url(directory_name, file_name, expiration_minutes)
	//@@TODO

*/
