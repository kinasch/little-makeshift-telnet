package ueb1;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SMTP {
    public static void main(String[] args){
        mail(args);
    }
    public static void mail(String[] args){
        if (args.length!=2){
            System.err.println("Usage: java smtp.java <mail address 1> <mail address 2>");
            System.exit(1);
        }
        try  {
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
            Socket clientSocket = new Socket("smtp.htw-berlin.de", 25);
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            out.println("ehlo smtp.htw-berlin.de");
            System.out.println("ehlo smtp.htw-berlin.de");
            String answer;
            while((answer = in.readLine())!=null){
                System.out.println(answer);
            }

        } catch (Exception e) {
            System.err.println("Error: "+e);
            e.printStackTrace();
            System.exit(1);
        }
    }
}
