require 'spec_helper'

describe Examples do 

  before(:all) do
    @dir = Time.now.to_i.to_s
    $filename = "#{@dir}_newfile"
    puts "Using #{@dir} as Test Directory Name"
  end

  before(:each) do
    @s3 = Examples.new
  end

  at_exit do
    Process.detach(Process.spawn("sleep 1; rm -f #{$filename} Gemfile.lock"))
  end

  it "creates a directory" do
    expect(@s3.create_directory(@dir)).to be_a Fog::Storage::AWS::Directory
  end

  it "lists all directories" do
    expect(@s3.list_directories.length).to be > 0
  end

  it "gets one directory" do
    expect(@s3.get_directory(@dir)).to be_a Fog::Storage::AWS::Directory
  end

  it "created a file in a directory" do
    cc = File.open($filename, 'w') { |fh| fh.write 'some dummy content' }

    uploaded_file = @s3.upload_file(@dir,$filename)

    expect(uploaded_file.content_length).to be == cc

#    File.unlink($filename)
  end

  it "lists all files in directory" do
    expect(@s3.get_files(@dir).length).to be > 0
  end

  it "gets specified file from directory" do
    expect(@s3.get_file(@dir,$filename)).to be_a Fog::Storage::AWS::File
  end

  it "downloads a file" do
    file = "#{@dir}_downloaded"

    @s3.download_file(@dir,$filename,file)
    expect(File.exists?(file)).to be true
    File.delete(file)
  end

  it "gets a public url for the file" do
    expect(@s3.get_public_url(@dir, $filename, 5)).to match(/^https:.+/)
  end

  it "deletes a file" do
    @s3.delete_file(@dir, $filename)
    expect(@s3.get_file(@dir, $filename)).to be nil
  end

  it "deletes a directory" do
    @s3.delete_directory(@dir)
    expect(@s3.get_directory(@dir)).to be nil
  end


end
