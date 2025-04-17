import java.io.*;
import java.net.*;
import java.util.*;


//IMPLEMENTATION OF THE FTP-SERVER IN THE CLIENT-SERVER ARCHITECTURE

public class Server {
    private int port;
    private ServerSocket serverSocket;

    public Server(int port) {
        this.port = port;
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server started on port: " + port);
        } catch (IOException e) {
            System.out.println("Could not start server: " + e.getMessage());
        }
    }

    public void launch() {
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                Thread clientThread = new Thread(new ClientHandler(socket));
                clientThread.start();
            } catch (IOException e) {
                System.out.println("Error connecting to client: " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("............................................................");
        System.out.println("Starting FTP-Server on port 8080...");
        Server server = new Server(8080);
        server.launch();
    }

    private static class ClientHandler implements Runnable {
        private Socket socket;
        private ObjectInputStream in;
        private ObjectOutputStream out;

        public ClientHandler(Socket socket) {
            this.socket = socket;
            try {
                in = new ObjectInputStream(socket.getInputStream());
                out = new ObjectOutputStream(socket.getOutputStream());
            } catch (IOException e) {
                System.out.println("Error creating I/O streams: " + e.getMessage());
            }
        }

        @Override
        public void run() {
            try {
                FTPRequest request;
                while ((request = (FTPRequest) in.readObject()) != null) {
                    switch (request.getMethod()) {
                        case LIST:
                            handleListFiles();
                            break;
                        case PULL:
                            handleGetFile(request);
                            break;
                        case PUSH:
                            handlePutFile(request);
                            break;
                        case QUIT:
                            socket.close();
                            return;
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Error processing request: " + e.getMessage());
            }
        }

        private void handleListFiles() throws IOException {
            File folder = new File("server_files");
            File[] files = folder.listFiles();
            List<String> filenames = new ArrayList<>();
            for (File file : files) {
                filenames.add(file.getName());
            }
            FTPResponse response = new FTPResponse(true, "List of files", filenames, null);
            out.writeObject(response);
        }

        private void handleGetFile(FTPRequest request) throws IOException {
            File file = new File("server_files/" + request.getFileName());
            if (file.exists() && file.isFile()) {
                byte[] fileData = new byte[(int) file.length()];
                try (FileInputStream fis = new FileInputStream(file);
                     BufferedInputStream bis = new BufferedInputStream(fis)) {
                    bis.read(fileData);
                }
                FTPResponse response = new FTPResponse(true, "File data", null, fileData);
                out.writeObject(response);
            } else {
                FTPResponse response = new FTPResponse(false, "File not found", null, null);
                out.writeObject(response);
            }
        }

        private void handlePutFile(FTPRequest request) throws IOException {
            File file = new File("server_files/" + request.getFileName());
            try (FileOutputStream fos = new FileOutputStream(file);
                 BufferedOutputStream bos = new BufferedOutputStream(fos)) {
                bos.write(request.getFileData());
            }
            FTPResponse response = new FTPResponse(true, "File uploaded successfully", null, null);
            out.writeObject(response);
        }
    }
}
