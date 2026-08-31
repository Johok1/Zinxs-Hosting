package zinxshosting.backend;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationHook;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.ConnectException;
import java.net.SocketException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Scanner;

@SpringBootApplication
@RestController
@RequestMapping("/zinxshosting/backend")
public class BackendApplication {

	private final String SSH_username = "ubuntu";
	private final String SSH_hostname = "158.69.52.39";
	private final String SSH_password = "Rrr70dACi41gHrkY";

	private final String FTP_hostname="158.69.52.39";
	private final int FTP_port = 21;
	private final String FTP_user = "mart";
	private final String FTP_pass ="6381";

	private final String FTP_Migration_hostname ="na976.pebblehost.com";

	private final String FTP_Migration_user ="Martin.weiner.aguilera@gmail.com.477859";

	private final String FTP_Migration_pass ="Martin0034";


	private final int SSH_port = 22;

	private final Session session;

	private ArrayList<String> args;

	private ArrayList<String> ports;

	private ChannelExec mcExec;



	public BackendApplication() throws JSchException, IOException {
		session = new JSch().getSession(SSH_username, SSH_hostname, SSH_port);
		if(!session.isConnected()) {
			session.setPassword(SSH_password);
			session.setConfig("StrictHostKeyChecking", "no");
			session.connect();
		}
		args = new ArrayList<>();
		ports = new ArrayList<>();

		//InputStream consoleStream = new ByteArrayInputStream(new byte[30]);
		//session.setInputStream(consoleStream);


		 mcExec = (ChannelExec) session.openChannel("exec");

	}


	@CrossOrigin()
	@GetMapping("/getCPU")
	public String getCPU() throws JSchException, IOException {
		try {
			ChannelExec channelExec = (ChannelExec) session.openChannel("exec");

			channelExec.setCommand("cd /opt/ftp && ./cpu_usage.sh");
			Scanner inStrm = new Scanner(channelExec.getInputStream());
			//System.out.println("Session Connected: " + session.isConnected());
			//System.out.println("Execution channel Connected: " + channelExec.isConnected());
			channelExec.connect();
			//System.out.println("Execution channel Connected: " + channelExec.isConnected() + "\n");


			//System.out.println("Execution channel Still Connected: " +mcExec.isConnected());

			String response = "";
			try {
				while (inStrm.hasNext()) {
					response = inStrm.nextLine();
				}
				channelExec.disconnect();
				return response;
			} catch (IndexOutOfBoundsException e) {
				channelExec.disconnect();
				return "" + Arrays.toString(e.getStackTrace());
			}
		}catch (JSchException e){
			return "";
		}
	}

	@CrossOrigin()
	@GetMapping("/getFirewallPorts")
	public String getFirewallPorts() throws JSchException, IOException {
		if (ports.isEmpty()) {
			return "";
		} else {
			String popped = ports.get(0);
			if (popped.contains("-") || popped.contains("To") || popped.isBlank()) {
				ports.remove(0);
				System.out.println("Removed token: " + popped);
				getFirewallPorts();
			} else {
				ports.remove(0);
                if((popped.contains("v6"))) {
                    System.out.println("Port: " + popped);
                    return popped;
                }
			}
		}
		return "";

	}

	@CrossOrigin()
	@GetMapping("/getBackups")
	public String getBackups(){
		File backups = new File("backups/");
		ArrayList<String> backup_names = new ArrayList<>();
		if(!backups.exists()){
			backups.mkdirs();
		}
		for(File file : backups.listFiles()){
			backup_names.add(file.getName());
		}
		String output = "";
		for(String s : backup_names){
			output+= s + ":";
		}


		return output;
	}

	@CrossOrigin()
	@GetMapping("/executeBackupNow")
	public String executeBackupNow() throws Exception {

		File first = new File("backups/");
		executeBackupOnPath("");
		return "";

	}

	@CrossOrigin()
	@GetMapping("/startMigrationProcess")
	public String executeMigrationNow() throws Exception {


		File first = new File("migrations/");
		executeMigrationOnPath("");
		return "";

	}

	private void executeMigrationOnPath(String path) throws Exception {

		try {
			FTPClient ftp = new FTPClient();
			//ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));

			ftp.connect(FTP_Migration_hostname, FTP_port);

			//ftp.enterLocalPassiveMode();

			ftp.login(FTP_Migration_user, FTP_Migration_pass);

			ftp.setFileType(FTP.BINARY_FILE_TYPE);

			FTPFile[] files = ftp.listFiles(path);

			if (files != null && files.length > 0) {
				String currentPath;
				for (FTPFile file : files) {

					if (path.isEmpty()) {
						currentPath = file.getName();
					} else {
						currentPath = path + "/" + file.getName();
					}
					System.out.println(currentPath);
					if (file.isDirectory()) {
						executeMigrationOnPath(currentPath); // Recursive call to handle nested directories
					} else {
						//File f = new File("backups/"+currentPath);

						//TODO: extract the file name from the path, use the path to make directories and file name to
						// fetch from the ftp server

						String[] tokens = currentPath.split("/");
						String filename = "";
						String dir = "";
						filename = tokens[tokens.length - 1];
						for (int i = 0; i < tokens.length; i++) {
							if (i != tokens.length - 1) {
								dir += tokens[i] + "/";
							}
						}

						downloadFTPFileMigrate(currentPath, dir, filename);
						//f.mkdirs();
					}
				}
			}


			ftp.disconnect();
		} catch (FTPConnectionClosedException e) {
				executeMigrationOnPath("");
		}
	}
	public File downloadFTPFileMigrate(String filepath, String dir, String filename) throws Exception {
		try {
			LocalDate date = LocalDate.now();

			LocalTime time = LocalTime.now();
			FTPClient ftp = new FTPClient();
			// ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));

			ftp.connect(FTP_Migration_hostname, FTP_port);

			ftp.enterLocalPassiveMode();

			ftp.login(FTP_Migration_user, FTP_Migration_pass);

			ftp.setFileType(FTP.BINARY_FILE_TYPE);


			String[] tokens = filename.split(".");
			if (false) {
				InputStream in = ftp.retrieveFileStream(filepath);
				if (!ftp.completePendingCommand()) {
					throw new Exception("Ftp command not completed");
				}

				Scanner scan = new Scanner(in);
				File dirs = new File("migrations/" + date + "/" + dir);
				if (!dirs.exists()) {
					dirs.mkdirs();
				}
				File file = new File("migrations/" + date + "/" + filepath);
				if (!file.exists()) {
					FileWriter writer = new FileWriter(file);


					while (scan.hasNext()) {
						writer.write(scan.nextLine() + "\n");
					}

					writer.close();
				}
				in.close();
				scan.close();
				ftp.disconnect();
				return file;
			} else {
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				try {
					if (!ftp.retrieveFile(filepath, out)) {
						if (!ftp.retrieveFile(filepath, out)) {
							if (!ftp.retrieveFile(filepath, out)) {
								if (!ftp.retrieveFile(filepath, out)) {
									throw new Exception("Couldn't get file, lots!");
								}
							}
						}
					}
				} catch (SocketException e) {
					ftp.disconnect();
					ftp.connect(FTP_Migration_hostname, FTP_port);
					ftp.login(FTP_Migration_user, FTP_Migration_pass);
					if (!ftp.retrieveFile(filepath, out)) {
						if (!ftp.retrieveFile(filepath, out)) {
							if (!ftp.retrieveFile(filepath, out)) {
								if (!ftp.retrieveFile(filepath, out)) {
									throw new Exception("Couldn't get file, lots!");
								}
							}
						}
					}
				}
				System.out.println("Check!");

				System.out.println("Out size: " + out.size());


				File dirs = new File("migrations/" + date + "/" + dir);
				if(!dirs.exists()) {
					dirs.mkdirs();
				}else {
					System.out.println("Directory Exists!");
				}
				File file = new File("migrations/" + date + "/" + filepath);


				if(!file.exists()) {
					ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
					FileOutputStream outputStream = new FileOutputStream(file);
					System.out.println("Bytes: " + in.available());
					outputStream.write(in.readAllBytes());
					System.out.println("Bytes: " + in.available());
					in.close();
					outputStream.close();
				}else{
					System.out.println("File Exists!");
				}

				out.close();
				ftp.disconnect();
				return file;
			}
		}catch (FTPConnectionClosedException e){
			return downloadFTPFileMigrate(filepath,dir,filename);
		}
	}


	private void executeBackupOnPath(String path) throws Exception {
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
					executeBackupOnPath(currentPath); // Recursive call to handle nested directories
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
		LocalDate date = LocalDate.now();
		LocalTime time = LocalTime.now();
		FTPClient ftp = new FTPClient();
		// ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));

		ftp.connect(FTP_hostname, FTP_port);

		ftp.enterLocalPassiveMode();

		ftp.login(FTP_user, FTP_pass);

		ftp.setFileType(FTP.BINARY_FILE_TYPE);





		String[] tokens = filename.split(".");
		if(false) {
			InputStream in = ftp.retrieveFileStream(filepath);
			if (!ftp.completePendingCommand()) {
				throw new Exception("Ftp command not completed");
			}

			Scanner scan = new Scanner(in);
			File dirs = new File("backups/"+date+"/"+dir);
			if(!dirs.exists()) {
				dirs.mkdirs();
			}
			File file = new File("backups/"+date+"/"+filepath);
			if(!file.exists()) {
				FileWriter writer = new FileWriter(file);


				while (scan.hasNext()) {
					writer.write(scan.nextLine() + "\n");
				}

				writer.close();
			}
			in.close();
			scan.close();
			ftp.disconnect();
			return file;
		}else {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			try{
				if(!ftp.retrieveFile(filepath,out)){
					if(!ftp.retrieveFile(filepath,out)){
						if(!ftp.retrieveFile(filepath,out)){
							if(!ftp.retrieveFile(filepath,out)){
								throw new Exception("Couldn't get file, lots!");
							}
						}
					}
				}
			}catch (SocketException e){
				ftp.disconnect();
				ftp.connect(FTP_hostname,FTP_port);
				ftp.login(FTP_user,FTP_pass);
				if(!ftp.retrieveFile(filepath,out)){
					if(!ftp.retrieveFile(filepath,out)){
						if(!ftp.retrieveFile(filepath,out)){
							if(!ftp.retrieveFile(filepath,out)){
								throw new Exception("Couldn't get file, lots!");
							}
						}
					}
				}
			}
			System.out.println("Check!");

			System.out.println("Out size: " + out.size());




			File dirs = new File("backups/"+date+"/"+dir);
			dirs.mkdirs();
			File file = new File("backups/"+date+"/"+filepath);
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
	@CrossOrigin()
	@GetMapping("/getFirewallStatus")
	public String getFirewallStatus() throws JSchException, IOException {
		try {
			ChannelExec chanExec = (ChannelExec) session.openChannel("exec");

			chanExec.setCommand("sudo ufw status");
			Scanner server = new Scanner(chanExec.getInputStream());
			chanExec.connect();

			String response = "";
			try {
				if (server.hasNext()) {
					response = server.nextLine();
					//System.out.println("Server Status: " +response);
					while (server.hasNext()) {
						ports.add(server.nextLine());
					}
				}
				//chanExec.disconnect();
				return response;
			} catch (IndexOutOfBoundsException e) {
				//chanExec.disconnect();
				return "" + Arrays.toString(e.getStackTrace());
			}
		}catch (JSchException e){
			return "";
		}
	}

	@CrossOrigin()
	@GetMapping("/getRam")
	public String getRam() throws JSchException, IOException {

		try{
			ChannelExec channelExec = (ChannelExec) session.openChannel("exec");
			channelExec.setCommand("cd /opt/ftp && ./ram_usage.sh");
			Scanner inStrm = new Scanner(channelExec.getInputStream());
			//System.out.println("Session Connected: " + session.isConnected());
			//System.out.println("Execution channel Connected: " +channelExec.isConnected());
			channelExec.connect();
			//System.out.println("Execution channel Connected: " + channelExec.isConnected()+"\n");


			//System.out.println("Execution channel Still Connected: " +mcExec.isConnected());

			String response = "";
			try {
				while (inStrm.hasNext()) {
					response = inStrm.nextLine();
				}
				channelExec.disconnect();
				return response;
			}catch (IndexOutOfBoundsException e){
				channelExec.disconnect();
				return ""+ Arrays.toString(e.getStackTrace());
			}
		}catch (JSchException e){
			return "";
		}
	}

	@CrossOrigin()
	@GetMapping("/getConsole")
	public String getConsoleHelp() throws JSchException, IOException {
		if(args.isEmpty()){
			return "";
		}else{
			String popped = args.get(0);
			args.remove(0);
			return popped;
		}
	}

	@CrossOrigin
	@GetMapping("/getConsoleArgs")
	public String getConsole() throws JSchException, IOException {
		mcExec = (ChannelExec) session.openChannel("exec");

		Scanner scanner = new Scanner(mcExec.getInputStream());


		mcExec.setCommand("cd /opt/ftp && sudo java -Xms30g -Xmx32g -jar /opt/ftp/paper-1.19.1-111.jar nugui");
	//	System.out.println("Session Connected: " + session.isConnected());
	//	System.out.println("Execution channel Connected: " +mcExec.isConnected());
		mcExec.connect();
	//	System.out.println("Execution channel Connected: " +mcExec.isConnected()+"\n");


		try {
			while (scanner.hasNext()) {
				args.add(scanner.nextLine());
			}
			//mcExec.disconnect();
			//scanner.close();
			return String.valueOf("Args size: " +args.size());
		}catch (IndexOutOfBoundsException e){
			//mcExec.disconnect();
			//scanner.close();
			return ""+ Arrays.toString(e.getStackTrace());
		}
	}

	@CrossOrigin
	@GetMapping("/getConsoleCommand/{command}")
	public String getConsoleStuff(@PathVariable String command) throws JSchException, IOException {
		System.out.println("/getConsoleCommand/" + command + "\n");
		if(mcExec.isClosed()){
			mcExec.connect();
		}



	//	mcExec.setCommand(command);
		//scanner = new Scanner(mcExec.getInputStream());
	//	System.out.println("Session Connected: " + session.isConnected());
	//	System.out.println("Execution channel Connected: " +mcExec.isConnected());
		//mcExec.connect();
	//	System.out.println("Execution channel Connected: " +mcExec.isConnected()+"\n");
		PrintStream mc = new PrintStream(mcExec.getOutputStream());
		//Scanner scanner = new Scanner(mcExec.getInputStream());

		//System.out.println("Execution channel Still Connected: " +mcExec.isConnected());

		mc.println(command);



		try {
			/*while (scanner.hasNext()) {
				args.add(scanner.nextLine());
			}
*/
			//mcExec.disconnect();
			//scanner.close();
			boolean error = mc.checkError();
			mc.close();
			return ""+error;
		}catch (IndexOutOfBoundsException e){
			//mcExec.disconnect();
			//scanner.close();
			mc.close();
			return ""+ Arrays.toString(e.getStackTrace());
		}
	}

	@CrossOrigin
	@GetMapping("/getFtpEula")
	public String uploadEulaTest() throws IOException {

		FTPClient ftp = new FTPClient();
		ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));

		ftp.connect(FTP_hostname,FTP_port);
		ftp.enterLocalPassiveMode();


		ftp.login(FTP_user,FTP_pass);

		ftp.setFileType(FTP.BINARY_FILE_TYPE);

		File eula = new File("eula.txt");
		String secRemFile = "minecraft/eula.txt";
		// ftp.changeWorkingDirectory(secRemFile);

		FileInputStream inputStream = new FileInputStream(eula);

		System.out.println("Initiate the upload process for the second file");

		try{
			boolean isStore = ftp.storeFile(secRemFile,inputStream);
			System.out.println("\n\n\n eula " + isStore + " eula \n\n\n");
			return String.valueOf(isStore);
		}catch (ConnectException e){
			ftp.disconnect();
			ftp.connect(FTP_hostname,FTP_port);
			ftp.login(FTP_user,FTP_pass);

		}finally {
			boolean isStore = ftp.storeFile(secRemFile,inputStream);
			System.out.println("\n\n\n eula " + isStore + " eula \n\n\n");
			return String.valueOf(isStore);
		}

	}
	@CrossOrigin
	@PostMapping("/postFtpFile/{filepath}")
	public String uploadFtpFile(@PathVariable String filepath, @RequestBody String fileContent) throws Exception {
		FTPClient ftp = new FTPClient();
		ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));

		ftp.connect(FTP_hostname,FTP_port);
		ftp.enterLocalPassiveMode();


		ftp.login(FTP_user,FTP_pass);

		ftp.setFileType(FTP.BINARY_FILE_TYPE);
		fileContent = fileContent.replace("<br>","\n");
		String real_filepath = filepath.replace("&","/");
		System.out.println("Real Filepath POST: " + real_filepath);
		System.out.println(fileContent);
		//ByteArrayInputStream inStream = new ByteArrayInputStream(fileContent.getBytes());
		//ftp.enterLocalPassiveMode();
		String[] tokens = real_filepath.split("/");
		String filename = "";
		for(String s : tokens){
			if(s.contains(".")){
				filename = s;
			}
		}
		if(filename.equals("")){
			throw new Exception("No file included");
		}

		File eula = new File(filename);
		//InputStream fileInStream;
		FileWriter writer = new FileWriter(eula);
		String[] args = fileContent.split("\n");
		for(String arg : args){
			writer.write(arg+"\n");
		}
		writer.close();



			//fileInStream = ftp.retrieveFileStream(real_filepath);


			System.out.println("Got passed this one: one!");
/*
			if(!ftp.completePendingCommand()){
				throw new Exception("ftp command not completed!");
			}

 */
		System.out.println("Got passed this one: two!");
			/*
			if(fileInStream == null){
			throw new Exception("File in stream null");
		}

			 */
		ftp.disconnect();
		System.out.println("Got passed this one: three!");
		//FileOutputStream outputStream = new FileOutputStream(eula);
		//fileInStream.transferTo(outputStream);
		System.out.println("Got passed this one: four!");
		//outputStream.close();
		//fileInStream.close();
		FileInputStream inputStream = new FileInputStream(eula);
		System.out.println("Input Bytes remaining: " + inputStream.available());

		System.out.println("Initiate the upload process for " + filename +" at " +real_filepath);



		ftp.connect(FTP_hostname,FTP_port);

		ftp.enterLocalPassiveMode();

		ftp.login(FTP_user,FTP_pass);

		ftp.setFileType(FTP.BINARY_FILE_TYPE);
			boolean status = ftp.storeFile(real_filepath, inputStream);
		System.out.println("Got passed this one: five!");
		System.out.println("Input Bytes remaining: " + inputStream.available());
		inputStream.close();
			/*if(!ftp.completePendingCommand()){
				throw new Exception("ftp command not completed!");
			}

			 */
		System.out.println("Got passed this one: six!");
			System.out.println("\n\n\n status " + status + " status \n\n\n");
			ftp.disconnect();
			return String.valueOf(status);

	}

	@CrossOrigin
	@GetMapping("/getFtpFile/{filepath}")
	public String getFtpFile(@PathVariable String filepath) throws Exception {
		FTPClient ftp = new FTPClient();
		ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));

		ftp.connect(FTP_hostname,FTP_port);

		ftp.enterLocalPassiveMode();

		ftp.login(FTP_user,FTP_pass);

		ftp.setFileType(FTP.BINARY_FILE_TYPE);


		if(ftp.isConnected()){
			if(ftp.login(FTP_user,FTP_pass)){
				String real_filepath = filepath.replace("&","/");
				System.out.println("Real Filepath GET: " + real_filepath);
				InputStream in = ftp.retrieveFileStream(real_filepath);
				if(!ftp.completePendingCommand()){
					throw new Exception("Ftp command not completed");
				}

				Scanner scan = new Scanner(in);
				ArrayList<String> fileArgs = new ArrayList<>();

				while(scan.hasNext()){
					fileArgs.add(scan.nextLine()+"\n");
				}


				String output = Arrays.toString(fileArgs.toArray());
				System.out.println(output);
				in.close();
				scan.close();
				ftp.disconnect();
				return output;
			}else{
				ftp.disconnect();
				throw new Exception("Not logged in!");
			}
		}else{
			ftp.disconnect();
			throw new Exception("Not connected!");
		}






		}


		@CrossOrigin
	@GetMapping("/getConsoleDirectory/{path}")
	public String getConsoleDirectory(@PathVariable String path) throws JSchException, IOException {

		String real_path = path.replace("&","/");
		ChannelExec channelExec = (ChannelExec) session.openChannel("exec");
		channelExec.setCommand("sudo ls " + real_path);
		Scanner inStrm = new Scanner(channelExec.getInputStream());
		//System.out.println("Session Connected: " + session.isConnected());
		//System.out.println("Execution channel Connected: " +channelExec.isConnected());
		channelExec.connect();
		//System.out.println("Execution channel Connected: " + channelExec.isConnected()+"\n");


		//System.out.println("Execution channel Still Connected: " +mcExec.isConnected());

		ArrayList<String> strings = new ArrayList<>();
		try {
			while (inStrm.hasNext()) {
				strings.add(inStrm.nextLine());
			}
			channelExec.disconnect();
			return ""+Arrays.toString(strings.toArray());
		}catch (IndexOutOfBoundsException e){
			channelExec.disconnect();
			return ""+ Arrays.toString(e.getStackTrace());
		}
	}



	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

}
