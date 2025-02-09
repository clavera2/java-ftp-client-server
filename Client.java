import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
    private Socket clientSocket; //for connecting to the server
    private BufferedReader clientReader; //for reading data from server 
    private ObjectOutputStream clientWriter; //for writing data to server
    private FTPRequest request; //represents the request method for the client to be sent to the server



    public Client() {
        //initialize client socket, reader and writer
        try {
            clientSocket = new Socket("localhost", 8080);
            clientReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            //clientWriter = new ObjectOutputStream(clientSocket.getOutputStream());
            getResponse();
        } catch (UnknownHostException e) {
            System.out.println("unknown host");
        } catch (IOException e) {
            System.out.println("io exception thrown in constructor");

        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.out.println("couldnt close client socket");
            }
        }
    }

    public void getResponse() {
        //test to see if server is sending message
        try {
            String line = clientReader.readLine();
        //    while (line != null) {
        //         System.out.println(line);
        //         line = clientReader.readLine();
        //    }
        System.out.println(line);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            System.out.println("io exception thrown");
            e.printStackTrace();
        }
    }

    // public File getFile(String filepath) {
    //     request = new FTPRequest();
    //     request.setFileName(filepath);
    //     try {
    //         clientWriter = new ObjectOutputStream(clientSocket.getOutputStream());
    //         clientReader = new ObjectInputStream(clientSocket.getInputStream());
    //         clientWriter.writeObject(request);
    //     } catch (IOException e) {
    //         // TODO Auto-generated catch block
    //         e.printStackTrace();
    //     }
    //     return null;
    // }

    public static void main(String[] args) {
        //run the client app
        new Client();
    }
}