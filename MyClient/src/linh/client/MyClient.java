package linh.client;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class MyClient {
	public static void main(String[] args) throws IOException {

		System.out.println("EchoClient.main()");

		Socket echoSocket = null;
		PrintStream out = null;
		BufferedReader in = null;
		Scanner inScan = null;

		try {
			echoSocket = new Socket("localhost", 11900);
			out = new PrintStream(echoSocket.getOutputStream());
			in = new BufferedReader(new InputStreamReader(
					echoSocket.getInputStream()));
			inScan = new Scanner(System.in);
		} catch (UnknownHostException e) {
			System.err.println("Don't know about host: localhost.");
			System.exit(1);
		} catch (IOException e) {
			System.err.println("Couldn't get I/O for "
					+ "the connection to: localhost.");
			System.exit(1);
		}

		BufferedReader stdIn = new BufferedReader(new InputStreamReader(
				System.in));
		// String userInput;
		System.out.println("connected!!");
		int counter = 0;

		// TODO monitor
		while (true) {
			String str = "";
			System.out.print("Enter a text: ");
			counter++;
			str = inScan.nextLine();
			out.println(counter + ":" + str);
			out.flush();
			if (str.equalsIgnoreCase("exit")) {
				System.out.println("______EXIT_____!");
				break;
			}
			if (str.startsWith("send")) {
				// get file name:
				String fname = str.substring(4).trim();
				System.out.println("Send file: " + fname + "to client...");
				File myFile = new File(fname);
				if(myFile.exists()){
					byte[] mybytearray = new byte[(int) myFile.length()];
					FileInputStream fis = new FileInputStream(myFile);
					BufferedInputStream bis = new BufferedInputStream(fis);
					bis.read(mybytearray, 0, mybytearray.length);
					OutputStream os = echoSocket.getOutputStream();
					System.out.println("Sending...");
					os.write(mybytearray, 0, mybytearray.length);
					os.flush();
				}else{
					System.out.println("File does not exist!!!");
				}
			}
		}
	}
}