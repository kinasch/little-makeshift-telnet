package ueb1;

import java.net.*;
import java.io.*;
public class SERVER {
    public static void main(String[] args) throws IOException {

        if (args.length != 1) {
            System.err.println("Usage: java EchoServer <port number>");
            System.exit(1);
        }

        int portNumber = Integer.parseInt(args[0]);

        try (
                ServerSocket serverSocket = new ServerSocket(Integer.parseInt(args[0]));
                Socket clientSocket = serverSocket.accept();
                //antwort an den client (oder so ähnlich halt ne)
                PrintWriter outding = new PrintWriter(clientSocket.getOutputStream(), true);
                //input vom client
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                //eingaben in der server konsole
                BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))
        ) {
            String inputLine;

            while ((inputLine = in.readLine()) != null) {


                //echo ding
                outding.println(inputLine);

                System.out.println("client: "+inputLine);
                if(inputLine.equals("quit")){
                    clientSocket.close();
                    serverSocket.close();
                }

                //liest eingabe und gibt an client antwort
                //String serverA = stdIn.readLine();
                //System.out.println("input: " + serverA);
                //outding.println(serverA);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
