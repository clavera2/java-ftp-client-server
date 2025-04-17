import java.io.Serializable;
import java.util.List;


//IMPLEMENTATION OF THE FTPRESPONSE
public class FTPResponse implements Serializable {
    private boolean success;
    private String message;
    private List<String> filenames; //For listing files
    private byte[] fileData; //For sending file data

    public FTPResponse(boolean success, String message, List<String> filenames, byte[] fileData) {
        this.success = success;
        this.message = message;
        this.filenames = filenames;
        this.fileData = fileData;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public List<String> getFilenames() {
        return filenames;
    }

    public byte[] getFileData() {
        return fileData;
    }
}
