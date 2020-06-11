package ueb3;
import java.net.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
        Runnable ro = new Runnable() {
          @Override
          public void run() {
            try {
              new HTTPConnection(ss.accept());
            } catch (Exception e){
              e.printStackTrace();
            }
          }
        };
        Thread t1 = new Thread(ro);
        t1.start();
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

  public void run ()
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

      try {
        // lies die erste Zeile des Requests ("Request-Line")
        // Format: "GET /xy.html HTTP/1.0"
        String request = httpreader.readLine();
        out.println("HTTP: " + request);

        // zerlege Request-Zeile in Worte
        StringTokenizer tokens = new StringTokenizer(request);
        String method = tokens.nextToken();    // z.B. "GET", "/index.html", "HTTP/1.0"

        // bearbeite akzeptierte Request "Methods"
        if (method.equals("GET") || method.equals("POST")) {
          // hole Request-URI
          String URI = tokens.nextToken();    // "/index.html"
          String URIparams = "";
          String HTTP_version = tokens.nextToken();    // "HTTP/1.0"

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
          if (URI.contains(".")) {        // Ressourcen-Typ wird angefragt
            // z.B. .html, .jpg
            String URIsub = URI.substring(URI.indexOf('.'),URI.length()).replaceAll("\\.","");
            // bestimme Dateityp anhand des Suffix des Dateinamens
            Path path = Paths.get(URI);
            if(Files.exists(path)) {

              // lies die Datei
              FileInputStream fileIn = new FileInputStream(URI);


              // liefere Response zurück
              httpwriter.println("HTTP/1.0 200 OK");
              // setze Content-Type entsprechend des Dateityps
              switch(URIsub){
                case("html"): httpwriter.println("Content-Type: text/html"); break;
                case("txt"): httpwriter.println("Content-Type: text/plan"); break;
                case("jpg"): httpwriter.println("Content-Type: image/jpeg"); break;
                case("jpeg"): httpwriter.println("Content-Type: image/jpeg"); break;
                case("gif"): httpwriter.println("Content-Type: image/gif"); break;
                default: httpwriter.println("Content-Type: unknown"); break;
              }

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

              int r;
              while ((r = fileIn.read()) != -1) {
                httpout.write(r);

              }
            } else{
              System.out.println("404");
              httpwriter.println("HTTP/1.0 404 File not found");
              httpwriter.flush();
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
        httpwriter.flush();
        out.println ("Bad Request");
      } catch (IOException e) {
        httpwriter.println ("HTTP/1.0 500 Internal Server Error");
        httpwriter.flush();
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
