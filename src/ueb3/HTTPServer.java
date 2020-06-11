package ueb3;
import java.net.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import static java.lang.System.out;


public class HTTPServer
{
  public static void main (String argv [])
  throws IOException
  {
    if (argv.length > 0) {
      ServerSocket ss = new ServerSocket (Integer.parseInt (argv [0]));

      while (true) {
        // bearbeite die Verbindung asynchron
        new HTTPConnection (ss.accept());
      }
    }
  }
}


class HTTPConnection
extends Thread
{
  Socket sock;

  HTTPConnection (Socket s)
  {
    sock = s;
    setPriority (NORM_PRIORITY - 1);
    start (); // starte run() als neuen Thread
  }

  public
  void run ()
  {
    try {
      // hole die Input- und Output-Streams der Verbindung
      InputStream httpin = sock.getInputStream ();
      OutputStream httpout = sock.getOutputStream ();

      // ein Text-Leser für den Request zum bequemeren Lesen
      BufferedReader httpreader = 
        new BufferedReader (new InputStreamReader (httpin));
      // ein Text-Schreiber für die Antwort zum Schreiben der Header
      PrintWriter httpwriter = new PrintWriter (httpout);
      //ein DatenoutputStream für die Files
      DataOutputStream dout=new DataOutputStream(sock.getOutputStream());

      try {
        // lies die erste Zeile des Requests ("Request-Line")
        // Format: "GET /xy.html HTTP/1.0"
        String request = httpreader.readLine();
        out.println("HTTP: " + request);

        // zerlege Request-Zeile in Worte
        String[] tokens = request.split(" ");
        String method = tokens[0];    // z.B. "GET", "/index.html", "HTTP/1.0"

        // bearbeite akzeptierte Request "Methods"
        if (method.equals("GET") || method.equals("POST")) {
          System.out.println("testMEthod");
          // hole Request-URI
          String URI = tokens[(1)];    // "/index.html"
          String URIparams = "";
          String HTTP_version = tokens[(2)];    // "HTTP/1.0"

          //...URL verarbeiten
          // "/index.html"
          // "/bla/arg.html"
          // "/img.jpg"
          // "/uhrzeit"
          // "/uhrzeit?format=am/pm"

          // Header-Zeilen lesen
          /*
          int tocolon;
          do {
            request = httpreader.readLine();
            tocolon = request.indexOf(':');
            if (tocolon > 0) {

              // ...Header - Zeile verarbeiten    // <- optional

            }
          } while (tocolon > 0);
          */

          /*
          // lies Body, falls vorhanden;	// <- optional
          // Indizien sind Headerzeilen Content-Type oder Transfer-Encoding
          if (has_content || has_body) {
            // lies Body (kann z.B. bei POST Parameter enthalten)
            ///...
          }
          */

          // bearbeite den Request

          // unterscheide nach einem Server-spezifischen Kriterium
          // verschiedene Ressourcentypen, hier:
          //	– Dateien (z.B. HTML-Seiten), wenn URI "." enthält
          //	– dynamische Server-Funktionen
          if (URI.contains(".") || URI.indexOf('.')!=-1) {        // Ressourcen-Typ wird angefragt
            System.out.println("hilfe");
            // z.B. .html, .jpg
            String URIsub = URI.substring(URI.indexOf('.'),URI.length());
            // bestimme Dateityp anhand des Suffix des Dateinamens


            // lies die Datei



            // liefere Response zurück
            httpwriter.println("HTTP/1.0 200 OK");
            // setze Content-Type entsprechend des Dateityps
            httpwriter.println("Content-Type: "+URIsub);
            // text/html
            // text/plain
            // image/jpeg
            // image/gif


            // Ende der Header-Zeilen
            httpwriter.println("");
            httpwriter.flush();

            // schreibe Dateiinhalt in Response-Body
            // (binär, daher nicht über den Writer,
            // sondern den darunterliegenden OutputStream,
            // deshalb vorher flush erforderlich)

            //ein Fileleser
            FileInputStream fileIn=new FileInputStream("index.html");
            int r;
            while((r=fileIn.read())!=-1)
            {
              httpout.write(r);

            }
          }
          if (URI.equals("/uhrzeit")) {
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            Date date = new Date();
            System.out.println(formatter.format(date));
            httpwriter.println(formatter.format(date));
            httpwriter.flush();
          }
        } else {

            httpwriter.println ("HTTP/1.0 405 Method Not Allowed");
            out.println ("HTTP Method abgelehnt: " + method);

        }





      } catch (NoSuchElementException e) {
        // keine Tokens (z.B. URI) im Request
        httpwriter.println ("HTTP/1.0 400 Bad Request");
        out.println ("Bad Request");
      } catch (IOException e) {
        httpwriter.println ("HTTP/1.0 500 Internal Server Error");
        out.println ("I/O error " + e);
      }

      sock.close();
    } catch (IOException e) {
      // falls hier schon eine Exception auftrat, besteht keine 
      // Möglichkeit, eine Antwort zu übergeben
      out.println ("I/O error am Anfang " + e);
    }
  }
}
