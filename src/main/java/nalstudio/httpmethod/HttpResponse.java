package nalstudio.httpmethod;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class HttpResponse {
    private static final Logger log = LoggerFactory.getLogger(HttpResponse.class);

    interface HttpStatus {
        int OK = 200;
        int REDIRECT = 302;
        int BAD_REQUEST = 400;
        int NOT_FOUND = 404;
        int SERVER_ERROR = 500;
    }

    private DataOutputStream dos;
    private Map<String, String> responseHeader;
    public HttpResponse(OutputStream os) {
        DataOutputStream dos = new DataOutputStream(os);
        this.dos = dos;
        this.responseHeader = new HashMap<>();
    }

    private void makeResponse200Header(String requestUrl, int lengthOgBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK " + "\r\n");
            if(requestUrl.endsWith("css")) {
                dos.writeBytes("Content-Type: text/css; " + "\r\n");
            } else if(requestUrl.endsWith("js")){
                dos.writeBytes("Content-Type: application/javascript " + "\r\n");
            } else {
                dos.writeBytes("Content-Type: text/html; charset=utf-8 " + "\r\n");
            }
            processHeader();
            dos.writeBytes("Content-Length " + lengthOgBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void makeResponse400Header(String requestUrl, int lengthOgBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 400 BAD REQUEST " + "\r\n");
            dos.writeBytes("Content-Type: text/html; charset=utf-8 " + "\r\n");
            processHeader();
            dos.writeBytes("Content-Length " + lengthOgBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void makeResponse302Header(String requestUrl, int lengthOgBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 302 FOUND " + "\r\n");
            if(requestUrl.endsWith("css")) {
                dos.writeBytes("Content-Type: text/css; charset=utf-8 " + "\r\n");
            } else {
                dos.writeBytes("Content-Type: text/html; charset=utf-8 " + "\r\n");
            }
            processHeader();
            dos.writeBytes("Location: " + requestUrl + " \r\n");
            dos.writeBytes("Content-Length " + lengthOgBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }


    private void responseBody(byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public void forward(String url) {
        try {
            File file = new File("webapp" + url);
            byte[] body = Files.readAllBytes(file.toPath());
            makeResponse200Header(url, body.length);
            responseBody(body);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public void failed(String url) {
        try {
            File file = new File("webapp" + url);
            byte[] body = Files.readAllBytes(file.toPath());
            makeResponse400Header(url, body.length);
            responseBody(body);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void forwardBody(String url ,String bodyS){
        byte[] body = bodyS.getBytes();
        makeResponse200Header(url, body.length);
        responseBody(body);
    }

    public void sendRedirect(String url) {
        try {
            File file = new File("webapp" + url);
            byte[] body = Files.readAllBytes(file.toPath());
            makeResponse302Header(url, body.length);
            responseBody(body);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void addHeader(String key, String value) throws IOException {
        responseHeader.put(key, value);
        //dos.writeBytes(key + ": " + value + " \r\n");
    }

    private void processHeader() {
        try {
            Set<String> keys = responseHeader.keySet();
            for(String key : keys) {
                dos.writeBytes(key + ": " + responseHeader.get(key) + " \r\n");
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
