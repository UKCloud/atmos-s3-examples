import boto.s3.connection
import os
from boto.s3.key import Key
import time



s3 = boto.connect_s3(
  aws_access_key_id = "%s/%s" % (os.environ['UKCLOUD_S3_UID'], os.environ['UKCLOUD_S3_SUBTENANT']),
  aws_secret_access_key = os.environ['UKCLOUD_S3_SECRET'],
  host = os.environ['UKCLOUD_S3_HOST'],
  port = 8443,
  calling_format = boto.s3.connection.OrdinaryCallingFormat(),
)

#create directory name from epoch time
directory_name = str(int(time.time()))
print "Using directory name: %s for example S3 functions" % directory_name
file_name = "%s_newfile" % directory_name
print "Using file name: %s for example S3 functions" % file_name



#create a directory
print "\nCreating directory: %s" % directory_name

directory = s3.create_bucket(directory_name)

print "  Created directory: %s" % directory.name



#list all directories
print "\nListing all directories"

dirs = s3.get_all_buckets()

print "  Found %d directories" % len(list(dirs))



#get a single directory
print "\nGetting directory: %s" % directory_name

dir = s3.get_bucket(directory_name)

print "  Got directory: %s" % dir.name



#create a file in a directory
print "\nUploading file: %s" % file_name

print "  Creating local file"

file = open(file_name, 'w')
file.write("Some demo text")

file = open(file_name, 'r')
print "  Created file with content: \n\n  %s\n\n" % file.read()
file.close()

print "  Uploading file: %s" % file_name

dir = s3.get_bucket(directory_name)
object = dir.new_key(file_name)
object.set_contents_from_filename(file_name)

print "  File uploaded succesfully"
print "  Deleting local file: %s" % file_name
os.remove(file_name)



#list all files in a directory
print "\nGetting all files in directory: %s" % directory_name

dir = s3.get_bucket(directory_name)
files = dir.list()

print "  Directory contains %s files" % len(list(files))



#get specified file from directory
print "\nGetting file: %s from directory: %s" % (file_name, directory_name)

dir = s3.get_bucket(directory_name)
file = dir.get_key(file_name)

print "  Got file: %s from cloud storage" % file.name



#download a file
file_downloaded = "%s_downloaded" % directory_name
print "\nDownloading file: %s from directory: %s to: %s" % (file_name, directory_name, file_downloaded)

dir = s3.get_bucket(directory_name)
file = dir.get_key(file_name)
file.get_contents_to_filename(file_downloaded)

print "  Downloaded file from cloud storage with contents: \n\n  %s\n\n" % open(file_downloaded, 'r').read()
os.remove(file_downloaded)



#get a public url for a file (with an expiry time)
print "\nGetting a time-limited public url for file: %s" % file_name

dir = s3.get_bucket(directory_name)
file = dir.get_key(file_name)
url = file.generate_url(3600, query_auth=True, force_http=False)

print "  Generated url: %s" % url



#delete a file
print "\nDeleting file: %s from directory: %s" % (file_name, directory_name)

dir = s3.get_bucket(directory_name)
dir.delete_key(file_name)

print "  File: %s deleted succesfully" % file_name



#delete a directory
print "\nDeleting directory: %s" % directory_name

s3.delete_bucket(directory_name)
