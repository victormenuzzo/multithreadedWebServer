/*
TRABALHO REALIZADO POR: PAULA GIOVANNA RODRIGUES - 1906526
          			    VICTOR ANTONIO MENUZZO - 1906550

PARA COMPILAR E RODAR NO LINUX UTILIZAR OS SEGUINTES COMANDOS:
javac WebServer.java
java WebServer

PARA VER FUNCIONAR DEIXAR O java WebServer RODANDO E COLOCAR A SEGUINTE LINHA NO BROWSER:

http://IP_DO_PC_COM_O_CODIGO:PORTA_USADA/ARQUIVO_A_SER_RODADO

ONDE:
IP_DO_PC_COM_O_CODIGO - IP DO COMPUTADOR QUE ESTÁ RODANDO O WebServer (para descobrir seu ip no linux: ~$ ip addr)
PORTA_USADA - PORTA PRÉ ESTABELECIDA NO WebServer.java (int port)
ARQUIVO_A_SER_RODADO - UM DOS ARQUIVOS DEFINIDOS NA PASTA (gatinho.gif ou pantera.jpg)

PARA VER A RESPOSTA DADA PELO HTTP: ~$ curl -I http://IP_DO_PC_COM_O_CODIGO:PORTA_USADA/ARQUIVO_A_SER_RODADO
*/

import java.io.* ;
import java.net.* ;
import java.util.* ;

public final class WebServer
{
	public static void main(String argv[]) throws Exception
	{
		// Set the port number.
		int port = 6789;

		// Establish the listen socket.
       	ServerSocket listenSocket = new ServerSocket(port);
		
		// Process HTTP service requests in an infinite loop.
		while (true) {
			Socket connectionSocket = listenSocket.accept(); 
			// Construct an object to process the HTTP request message.
			HttpRequest request = new HttpRequest(connectionSocket);

			// Create a new thread to process the request.
			Thread thread = new Thread(request);

			// Start the thread.
			thread.start();
		}

	}
}

final class HttpRequest implements Runnable
{
	
	final static String CRLF = "\r\n";
	Socket socket;

	// Constructor
	public HttpRequest(Socket socket) throws Exception 
	{
		this.socket = socket;
	}

	// Implement the run() method of the Runnable interface.
	public void run(){
		try {
			processRequest();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	private void processRequest() throws Exception
	{
		// Get a reference to the socket's input and output streams.
		InputStream is = this.socket.getInputStream();
		DataOutputStream os = new DataOutputStream(this.socket.getOutputStream());;

		// Set up input stream filters.
		BufferedReader br = new BufferedReader(new InputStreamReader(is));

		// Get the request line of the HTTP request message.
		String requestLine = br.readLine();

		// Display the request line.
		System.out.println();
		System.out.println(requestLine);

		// Get and display the header lines.
		/*String headerLine = null;
		while ((headerLine = br.readLine()).length() != 0) {
			System.out.println(headerLine);
		}*/

		// Extract the filename from the request line.
		StringTokenizer tokens = new StringTokenizer(requestLine);
		tokens.nextToken();  // skip over the method, which should be "GET"
		String fileName = tokens.nextToken();

		// Prepend a "." so that file request is within the current directory.
		fileName = "." + fileName;

		// Open the requested file.
		FileInputStream fis = null;
		boolean fileExists = true;
		try {
			fis = new FileInputStream(fileName);
		} catch (FileNotFoundException e) {
			fileExists = false;
		}

		// Construct the response message.
		String statusLine = null;
		String contentTypeLine = null;
		String entityBody = null;
		if (fileExists) {
			statusLine = "HTTP/1.0 200 OK" + CRLF;
			contentTypeLine = "Content-type: " + 
				contentType( fileName ) + CRLF;
		} else {
			statusLine = "HTTP/1.0 404 Page not found" + CRLF;
			contentTypeLine = "Content-type: text/html" + CRLF;
			entityBody = "<HTML> <HEAD><TITLE>Not Found</TITLE></HEAD> <BODY>404 Error: Not Found</BODY></HTML>";
		}

		// Send the status line.
		os.writeBytes(statusLine);

		// Send the content type line.
		os.writeBytes(contentTypeLine);

		// Send a blank line to indicate the end of the header lines.
		os.writeBytes(CRLF);

		// Send the entity body.
		if (fileExists)	{
			sendBytes(fis, os);
			fis.close();
		} else {
			os.writeBytes(entityBody);
		}

		os.close();
		br.close();
		socket.close();		
	}

	private static void sendBytes(FileInputStream fis, OutputStream os) throws Exception
	{
	   // Construct a 1K buffer to hold bytes on their way to the socket.
	   byte[] buffer = new byte[1024];
	   int bytes = 0;

	   // Copy requested file into the socket's output stream.
	   while((bytes = fis.read(buffer)) != -1 ) {
	      os.write(buffer, 0, bytes);
	   }
	}

	private static String contentType(String fileName)
	{
		if(fileName.endsWith(".htm") || fileName.endsWith(".html")) {
			return "text/html";
		}
		if(fileName.endsWith(".gif")) {
			return "image/gif";
		}
		if(fileName.endsWith(".jpg")) {
			return "image/jpeg";
		}
		return "application/octet-stream";
	}
}

