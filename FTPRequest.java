
import java.io.Serializable;

public class FTPRequest implements Serializable {
    //represents an ftp request - this class encapsulates the normal ftp requests with files included
    private FTPRequestMethod method;
    private String fileName;

    public FTPRequest() {
        //defaults to the ftp pull method
        method = FTPRequestMethod.PULL; 
    }

    public FTPRequest(FTPRequestMethod method) {
        this.method = method;
    }

    public void setMethod(FTPRequestMethod method) {
        this.method = method;
    }

    public FTPRequestMethod getMethod() {
        return method;
    }

    public void setFileName(String filename) {
        this.fileName = filename;
    }
}

