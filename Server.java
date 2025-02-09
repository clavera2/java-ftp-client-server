import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/* An FTP Server that implements the client-server architecture
 * This server listens for client connections and handles them using a -
 * - clienthandler inner class. It also tries to handle multiple client connections concurrently
 */

public class Server implements Runnable {
    private int port;
    private ServerSocket serverSocket; //to listen to incoming client connections

    public Server(int port) {
        this.port = port;
        try {
            serverSocket = new ServerSocket(this.port);
        } catch (IOException e) {
            System.out.println("could not start server");
        }
    }

    public void launch() {
        System.out.println("Attempting to launch server at port " + port);
        if (serverSocket == null) {
            System.out.println("Server is not connected to a socket");
            return;
        }
        while (true) { 
            try {
                Socket connectionSocket = serverSocket.accept();
                Thread clientThread = new Thread(new ClientHandler(connectionSocket));
            } catch (IOException e) {
                System.out.println("problem connecting to client");
            }
        }
    }

    @Override
    public void run() {
        //this might be a problem
        launch();
    }


    private static class ClientHandler implements Runnable {
        //class for handling client requests
        private BufferedReader serverReader; //read client data
        private BufferedWriter serverWriter; //write to client
        Socket connectionSocket;

        public ClientHandler(Socket connectionSocket) {
            this.connectionSocket = connectionSocket;
            try {
                this.serverWriter = new BufferedWriter(new PrintWriter(this.connectionSocket.getOutputStream()));
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            greetClient();
        }

        public void greetClient() {
            try {
                serverWriter.write("Hello client\n");
                serverWriter.flush();
                System.out.println("written to client");
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            
        }
        
        //will handle client connections
        @Override
        public void run() {

            //logic for handling requests
        }
    }

    public static void main(String[] args) {
        //start the server application
        new Server(8080).launch();
    }
}