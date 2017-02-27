require 'fog/aws'

class Examples
  def initialize
    params = {
     :aws_access_key_id => "#{ENV['UKCLOUD_S3_UID']}/#{ENV['UKCLOUD_S3_SUBTENANT']}",
     :aws_secret_access_key => ENV['UKCLOUD_S3_SECRET'],
     :host => ENV['UKCLOUD_S3_HOST'],
     :port => '8443',
     :scheme => 'https',
     :path_style => true,
     :aws_signature_version=> 2,
     :region => 'eu',
    }

    #connect to Cloud Storage
    @s3 = Fog::Storage::AWS.new(params)
  end



  def create_directory(directory_name)
    @s3.directories.create({:key => directory_name})
  end



  def list_directories
    @s3.directories.all
  end



  def get_directory(directory_name)
    @s3.directories.get(directory_name)
  end



  def upload_file(directory_name, file_path)
    params = {
      :key => file_path.split('/').last,
      :body => File.open(file_path),
      :public => false
    }

    dir = @s3.directories.get(directory_name)
    dir.files.create(params)
  end



  def get_files(directory_name)
    dir = @s3.directories.get(directory_name)
    dir.files.all
  end



  def get_file(directory_name, file_name)
    dir = @s3.directories.get(directory_name)
    dir.files.head(file_name)
  end



  def download_file(directory_name, file_name, download_location)
    dir = @s3.directories.get(directory_name)
    file = dir.files.head(file_name)

    File.open(download_location,'w') do |f|
      f.write(file.body)
    end
  end



  def get_public_url(directory_name, file_name, expiration_minutes)
    dir = @s3.directories.get(directory_name)
    file = dir.files.head(file_name)

    expiration = Time.now + (expiration_minutes * 60)

    file.url(expiration)

  end



  def delete_file(directory_name, file_name)
    dir = @s3.directories.get(directory_name)
    file = dir.files.head(file_name)

    file.destroy
  end



  def delete_directory(directory_name)
    dir = @s3.directories.get(directory_name)
    dir.destroy
  end

end
