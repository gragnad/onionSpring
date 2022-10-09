import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import nalstudio.RequestHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class requestTest {
    private static final Logger log = LoggerFactory.getLogger(requestTest.class);
    @Test
    public void requestHandlerTest() {
        try (ServerSocket listenSocket = new ServerSocket(8080)){
            log.info("Web Application Server started {} port.", 8080);

            //waiting connect to client
            Socket connection = listenSocket.accept();
            RequestHandler requestHandler = new RequestHandler(connection);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
