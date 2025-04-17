import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

public class Client {
    private final String host;
    private final int port;

    public Client(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void run() {
        try (
            Socket socket = new Socket(host, port);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in  = new ObjectInputStream(socket.getInputStream());
            Scanner scanner        = new Scanner(System.in)
        ) {
            System.out.println("Connected to FTP server " + host + ":" + port);

            while (true) {
                System.out.print("Enter command (LIST, GET <filename>, PUT <filepath>, QUIT): ");
                String line = scanner.nextLine().trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split("\\s+", 2);
                String cmd = parts[0].toUpperCase();

                FTPRequest request;
                switch (cmd) {
                    //the actions to the commands sent to the server
                    case "LIST":
                        request = new FTPRequest(FTPRequestMethod.LIST, null, null);
                        break;

                    case "GET":
                        if (parts.length < 2) {
                            System.out.println("Usage: GET <filename>");
                            continue;
                        }
                        request = new FTPRequest(FTPRequestMethod.PULL, parts[1], null);
                        break;

                    case "PUT":
                        if (parts.length < 2) {
                            System.out.println("Usage: PUT <filepath>");
                            continue;
                        }
                        String path = parts[1];
                        File file = new File(path);
                        if (!file.exists() || !file.isFile()) {
                            System.out.println("File not found: " + path);
                            continue;
                        }
                        byte[] data;
                        try {
                            data = Files.readAllBytes(Paths.get(path));
                        } catch (IOException e) {
                            System.out.println("Error reading file: " + e.getMessage());
                            continue;
                        }
                        request = new FTPRequest(FTPRequestMethod.PUSH, file.getName(), data);
                        break;

                    case "QUIT":
                        request = new FTPRequest(FTPRequestMethod.QUIT, null, null);
                        break;

                    default:
                        System.out.println("Unknown command: " + cmd);
                        continue;
                }

                // Send request
                out.writeObject(request);
                out.flush();

                // Read response
                FTPResponse response = (FTPResponse) in.readObject();
                System.out.println("Server: " + response.getMessage());

                if (!response.isSuccess()) {
                    if (request.getMethod() == FTPRequestMethod.QUIT) {
                        break;
                    }
                    continue;
                }

                switch (request.getMethod()) {
                    case LIST:
                        List<String> names = response.getFilenames();
                        if (names == null || names.isEmpty()) {
                            System.out.println("No files on server.");
                        } else {
                            System.out.println("Files:");
                            names.forEach(n -> System.out.println("  " + n));
                        }
                        break;

                    case PULL:
                        byte[] fileData = response.getFileData();
                        try (FileOutputStream fos = new FileOutputStream(request.getFileName())) {
                            fos.write(fileData);
                            System.out.println("Downloaded file: " + request.getFileName());
                        } catch (IOException e) {
                            System.out.println("Error saving file: " + e.getMessage());
                        }
                        break;

                    case QUIT:
                        System.out.println("Closing connection.");
                        return;

                    default:
                        // PUT succeeded; message already printed.
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        //create a client object and ivoke the run() method on the Client object
        new Client("localhost", 8080).run();
    }
}
