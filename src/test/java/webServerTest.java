
import nalstudio.httpmethod.HttpRequest;
import org.junit.Test;

import java.io.*;

import static org.junit.Assert.assertEquals;

public class webServerTest {
    private String testDirectory = "./src/test/resources/";

    @Test
    public void request_GET() {
        try {
            InputStream in = new FileInputStream(new File(testDirectory + "Http_Get.txt"));
            HttpRequest request = new HttpRequest(in);

            assertEquals("GET", request.getMethod());
            assertEquals("/user/create", request.getPath());
            assertEquals("keep-alive", request.getHeader("Connection"));
            assertEquals("javajigi", request.getParameter("userId"));

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    public void request_Post() {
        try {
            InputStream in = new FileInputStream(new File(testDirectory + "Http_POST.txt"));
            HttpRequest request = new HttpRequest(in);

            assertEquals("POST", request.getMethod());
            assertEquals("/user/create", request.getPath());
            assertEquals("keep-alive", request.getHeader("Connection"));
            assertEquals("javajigi", request.getParameter("userId"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


}
