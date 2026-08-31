package zinxshosting.backend;

import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MoreTests {
    private final String FTP_hostname="15.204.204.53";
    private final int FTP_port = 21;
    private final String FTP_user = "server";
    private final String FTP_pass ="6381";

    @BeforeAll
    public void before() throws IOException {

    }
    @Test
    public void test() throws IOException {



    }

    @Test
    public void test2() throws IOException {


    }



    public void test4() throws Exception {
        //ftp.login(user,password);
        File first = new File("backups/");
        //first.mkdirs();
        assert first.exists();
        //printFilesAndDirectories("");
    }

    private void printFilesAndDirectories(String path) throws Exception {
        FTPClient ftp = new FTPClient();
       //ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));

        ftp.connect(FTP_hostname,FTP_port);

        //ftp.enterLocalPassiveMode();

        ftp.login(FTP_user,FTP_pass);

        ftp.setFileType(FTP.BINARY_FILE_TYPE);

        FTPFile[] files = ftp.listFiles(path);
        if (files != null && files.length > 0) {
            for (FTPFile file : files) {
                String currentPath;
                if (path.isEmpty()) {
                    currentPath = file.getName();
                } else {
                    currentPath = path + "/" + file.getName();
                }
                System.out.println(currentPath);
                if (file.isDirectory()) {
                    printFilesAndDirectories(currentPath); // Recursive call to handle nested directories
                }else{
                    //File f = new File("backups/"+currentPath);

                    //TODO: extract the file name from the path, use the path to make directories and file name to
                    // fetch from the ftp server

                    String[] tokens = currentPath.split("/");
                    String filename = "";
                    String dir = "";
                    filename = tokens[tokens.length-1];
                    for(int i = 0; i < tokens.length; i++){
                        if(i != tokens.length-1){
                            dir+=tokens[i]+"/";
                        }
                    }

                    downloadFTPFile(currentPath,dir,filename);
                    //f.mkdirs();
                }
            }
        }


        ftp.disconnect();
    }

    public File downloadFTPFile(String filepath, String dir, String filename) throws Exception {
        FTPClient ftp = new FTPClient();
       // ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));

        ftp.connect(FTP_hostname, FTP_port);

        //ftp.enterLocalPassiveMode();

        ftp.login(FTP_user, FTP_pass);

        ftp.setFileType(FTP.BINARY_FILE_TYPE);





                String[] tokens = filename.split(".");
                if(false) {
                    InputStream in = ftp.retrieveFileStream(filepath);
                    if (!ftp.completePendingCommand()) {
                        throw new Exception("Ftp command not completed");
                    }

                    Scanner scan = new Scanner(in);
                    File dirs = new File("backups/"+dir);
                    dirs.mkdirs();
                    File file = new File(filepath);
                    FileWriter writer = new FileWriter(file);


                    while (scan.hasNext()) {
                        writer.write(scan.nextLine() + "\n");
                    }
                    writer.close();
                    in.close();
                    scan.close();
                    ftp.disconnect();
                    return file;
                }else {
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    if(!ftp.retrieveFile(filepath,out)){
                        if(!ftp.retrieveFile(filepath,out)){
                            if(!ftp.retrieveFile(filepath,out)){
                                if(!ftp.retrieveFile(filepath,out)){
                                    throw new Exception("Couldn't get file, lots!");
                                }
                            }
                        }
                    }
                    System.out.println("Check!");

                    System.out.println("Out size: " + out.size());


                    File dirs = new File("backups/"+dir);
                    dirs.mkdirs();
                    File file = new File("backups/"+filepath);
                    ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
                    FileOutputStream outputStream = new FileOutputStream(file);
                    System.out.println("Bytes: " + in.available());
                    outputStream.write(in.readAllBytes());
                    System.out.println("Bytes: " + in.available());

                    in.close();
                    out.close();
                    outputStream.close();
                    ftp.disconnect();
                    return file;
                }
                }

    /*
    @Test
    public void test5() throws IOException {
        for(FTPFile file : ftp.listFiles()){
            listFiles(file);
        }
    }
    public String listFiles(FTPFile file) throws IOException {
        if(file.isDirectory()){
            //Make a directory
            System.out.println("Directory: " + file.getName());
            for(FTPFile file2 : ftp.listFiles(file.getName())){
                if(file2.isDirectory()){
                    listFiles(file2);
                }else {
                    //Make a file
                    System.out.println("File: " +file2.getName());
                }
            }
        }else{
            System.out.println("File: " + file.getName());
        }
        return null;
    }

*/


    private String disconnectFTP(FTPClient ftp) throws IOException {
        try{
            ftp.disconnect();
            return "Disconnected FTP";
        }catch (IOException e){
            throw e;
        }
    }
}
