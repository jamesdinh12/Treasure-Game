import java.io.BufferedWriter;
import java.io.IOException;

public interface FileWriteable {
    void writeToFile(BufferedWriter writer) throws IOException;
}
