import java.io.Serializable;

public class FTPRequest implements Serializable {
    private FTPRequestMethod method;
    private String fileName;
    private byte[] fileData;

    public FTPRequest(FTPRequestMethod method, String fileName, byte[] fileData) {
        this.method = method;
        this.fileName = fileName;
        this.fileData = fileData;
    }

    public FTPRequestMethod getMethod() {
        return method;
    }

    public void setMethod(FTPRequestMethod method) {
        this.method = method;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte[] getFileData() {
        return fileData;
    }

    public void setFileData(byte[] fileData) {
        this.fileData = fileData;
    }
}
