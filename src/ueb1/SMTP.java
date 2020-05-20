package ueb1;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Timer;

public class SMTP {

    public static void main(String[] args){
        mail(args);
    }
    public static void mail(String[] args){
        if (args.length!=2){
            System.err.println("Usage:  java SMTP <mail address 1> <mail address 2>");
            System.exit(1);
        }
        try  {
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
            Socket clientSocket = new Socket("smtp.htw-berlin.de", 25);
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String ans1;
            ans1 = in.readLine();
            System.out.println(ans1);

            out.print("ehlo smtp.htw-berlin.de\r\n");
            out.flush();
            System.out.println("ehlo smtp.htw-berlin.de");
            String ans2;
            while((ans2 = in.readLine())!=null){
                System.out.println(ans2);
            }
            out.print("mail from: "+args[0]+"\r\n");
            System.out.println("mail from: "+args[0]+"\r\n");

        } catch (Exception e) {
            System.err.println("Error: "+e);
            e.printStackTrace();
            System.exit(1);
        }
    }
}
