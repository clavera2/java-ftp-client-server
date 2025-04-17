public enum FTPRequestMethod {
    PULL, // Get request to download a file
    PUSH, // Put request to upload a file
    LIST, // Request to list available files
    QUIT // Close the connection
}
