package main_test

import (
	"examples"
	"fmt"
	"testing"
)

func TestTimeConsuming(t *testing.T) {
	if testing.Short() {
		t.Skip("skipping test in short mode.")
	}

}

func ExampleHello() {
	fmt.Println("hello")
	// Output: hello
}

func ExampleFindBucket() {
	main.FindBucket("my-bucketname")
	// Output: Bucket not found.
}

func ExampleCreateBucket() {
	main.CreateBucket("my-bucketname")
	// Output: Bucket my-bucketname created successfully.
}

func TestListDirectories(t *testing.T) {
	if len(main.ListDirectories()) <= 0 {
		t.Errorf("Should list at least one directory")
	}
}

func TestGetDirectory(t *testing.T) {
	if len(main.GetDirectory("my-bucketname")) <= 0 {
		t.Errorf("Contents should return at least one file")
	}
}

func ExampleUploadFile() {
	main.UploadFile("my-bucketname", "my-testfile.csv")
	// Output: Uploaded my-testfile.csv of size: 15 Successfully to: my-bucketname / my-testfile.csv
}

func ExampleDownloadFile() {
	main.DownloadFile("my-bucketname", "my-testfile.csv", "my-download.csv")
	// Output: Successfully saved my-download.csv
}

func ExampleDeleteFile() {
	main.DeleteFile("my-bucketname", "my-testfile.csv")
	// Output: Successfully deleted my-testfile.csv
}

func ExampleDeleteDirectory() {
	main.DeleteDirectory("my-bucketname")
	// Output: Successfully deleted my-bucketname
}
