# UKCloud Atmos S3 Storage Examples

This project contains sample code for common operations against
UKCloud's Atmos based Cloud Storage service. Whilst Atmos itslef
supports both an S3 compatible and an EMC native API the samples in this
repository are designed to work with the S3 compatible endpoint only.

## Credentials
In order to provide a common way to pass credentials to the sample code,
each example will expect to be able to get credentials from environment
vairables. Please ensure the following are exported:

```
UKCLOUD_S3_UID
UKCLOUD_S3_SUBTENANT
UKCLOUD_S3_SECRET
```

Linux:
```
export UKCLOUD_S3_UID=abc12345543434324234jdlfkjwsadde
export UKCLOUD_S3_SUBTENANT=A123456678912345
export UKCLOUD_S3_SECRET=ABC2346253HFDG453=
```

Windows:
```
set UKCLOUD_S3_UID=abc12345543434324234jdlfkjwsadde
set UKCLOUD_S3_SUBTENANT=A123456678912345
set UKCLOUD_S3_SECRET=ABC2346253HFDG453=
```

**n.b if you have been provided an Atmos username in the following
format: "abc12345543434324234jdlfkjwsadde/A123456678912345"**  
**The uid is *before* the "/" and the subtenant is *after* the "/"**

Test that the variables are set with:

Linux:
```
echo $UKCLOUD_S3_UID
```

Windows:
```
echo %UKCLOUD_S3_UID%
```

## Languages
Examples are available for the following languages / SDKs
- Ruby (Fog)
- Python (Boto)
- Java (JetS3t)

Language specific instructions are available in each directory

## Functions
The following common functions are demonstrated in these samples:

- Connect to Atmos S3
- List all directories
- List a specific directory
- Create a directory
- Delete a directory
- List all files in a directory
- Upload a file
- Download a file
- Get a public url for a file

## Contribution
This repository is a work in progress and as such will be updated over
time. If you would like to contribute examples, please Fork this repo
and create a Pull Request with your contribution. 
If you would like to see examples of other functions not listed here but
do not have time to contribute then please raise an Issue in Github and
we will look at your request ASAP.
