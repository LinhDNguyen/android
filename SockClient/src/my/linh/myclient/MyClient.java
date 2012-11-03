package my.linh.myclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.BufferedWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class MyClient {
   public static void main(String[] args) throws IOException {

      System.out.println("EchoClient.main()");

      Socket echoSocket = null;
      PrintWriter sockOut = null;
      BufferedReader sockIn = null;
      Scanner in = new Scanner(System.in);
      String str = "";
      

      try {
         echoSocket = new Socket("localhost", 38300);
         sockOut = new PrintWriter(new BufferedWriter(new OutputStreamWriter(echoSocket.getOutputStream())),true);
//         sockOut = new PrintStream(echoSocket.getOutputStream());
         sockIn = new BufferedReader(new InputStreamReader(
               echoSocket.getInputStream()));
      } catch (UnknownHostException e) {
         System.err.println("Don't know about host: localhost.");
         System.exit(1);
      } catch (IOException e) {
         System.err.println("Couldn't get I/O for "
               + "the connection to: localhost.");
         System.exit(1);
      }
      if(echoSocket.isConnected()){
         System.out.println("Socket CONNECTED!");
         while(true){
            System.out.print("Enter a text: ");
            str = in.nextLine();
            System.out.println("Send \"" + str + "\" to server.");
            sockOut.println(str);
            sockOut.flush();

            if(str.equalsIgnoreCase("exit"))
               break;
         }
         sockOut.close();
         sockIn.close();
         echoSocket.close();
      }

      in.close();
      
      System.out.println("________EXIT_______!");
   }
}