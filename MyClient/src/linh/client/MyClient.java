package linh.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
		Scanner	inScan = null;

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
			if(str.equalsIgnoreCase("exit")){
				System.out.println("______EXIT_____!");
				break;
			}
		}
	}
}