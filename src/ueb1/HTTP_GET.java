package ueb1;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class HTTP_GET {
    public static void main(String[] args){
        get(args);
    }
    public static void get(String[] args){
        if (args.length!=1){
            System.err.println("Usage: java HTTP_GET <website URL>");
            System.exit(1);
        }
        String host = cutter(args[0]);
        try  {
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
            Socket clientSocket = new Socket(args[0], 80);
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream());
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            out.print("GET / HTTP/1.0"+"\r\n");
            out.print("Host: "+host+"\r\n");
            System.out.println("GET / HTTP/1.0");
            System.out.println("Host: "+host);

            out.print("\r\n");
            out.flush();
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
    public static String cutter(String s){
        s = s.replaceAll("http://","");
        return s;
    }
}
