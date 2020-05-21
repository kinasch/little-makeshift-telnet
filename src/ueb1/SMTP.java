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
            System.err.println("Usage:  java SMTP <sender mail> <recipient mail>");
            System.exit(1);
        }
        try  {
            BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
            Socket clientSocket = new Socket("smtp.htw-berlin.de", 25);
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream());
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String[] befehle = {"ehlo smtp.htw-berlin.de","mail from: "+args[0], "rcpt to: "+args[1],"data"};
            String ans1;
            ans1 = in.readLine();
            System.out.println(ans1);
            out.print(befehle[0]+"\r\n");
            out.flush();

            for(int i=1;i<befehle.length;i++){
                String ans2="";
                do{
                    System.out.println(ans2);
                }while((ans2 = in.readLine()).contains("-"));
                out.print(befehle[i]+"\r\n");
                out.flush();
                System.out.println(befehle[i]);
            }
            System.out.println(in.readLine());
            System.out.println("Sie koennen nun den Header bearbeiten\nund dann abgetrennt durch eine Leerzeile den Inhalt\n\nBeenden der Mail mit einem Punkt (.) in einer Zeile alleine");
            String nachricht;
            while (!(nachricht = stdIn.readLine()).equals(".")){
                out.print(nachricht+"\r\n");
            }
            out.print("."+"\r\n");
            out.flush();
            System.out.println(in.readLine());


        } catch (Exception e) {
            System.err.println("Error: "+e);
            e.printStackTrace();
            System.exit(1);
        }
    }
}
