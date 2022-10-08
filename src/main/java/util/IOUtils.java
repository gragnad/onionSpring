package util;

import java.io.BufferedReader;
import java.io.IOException;

public class IOUtils {
    /**
     *
     * @param br / request body 시작 시점 이고
     * @param contentLength / Request header 가가지고 있는 Content-length value
     * @return
     * @throws IOException
     */
    public static String readData(BufferedReader br, int contentLength) throws IOException {
        char[] body = new char[contentLength];
        br.read(body, 0, contentLength);
        return String.copyValueOf(body);
    }
}
