package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;

import static util.HttpRequestUtils.requestUrl;

public class RequestHandler extends Thread{
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;
    String requestUrl = null;
    String Accept = null;
    interface RequestType {
            int REST_URL = 0;
            int HOST = 1;
            int CONNECTION = 2;
            int ACCEPT = 9;
    }

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    @Override
    public void run() {
        log.debug("New Client Connect! Connect IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());
        //Stream : external data (read, send)
        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO this function is Process for client request
            DataOutputStream dos = new DataOutputStream(out);
            requestInput(in);
            byte[] body =  returnPage(requestUrl);
            response200Header(dos, body.length);
            responseBody(dos, body);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void requestInput(InputStream ins) {
        try {
            InputStreamReader streamReader = new InputStreamReader(ins);
            BufferedReader br = new BufferedReader(streamReader);
            String readLine = "";
            int count = 0;
            while (true) {
                readLine = br.readLine();
                if(!"".equals(readLine)) {
                    switch (count) {
                        case RequestType.REST_URL:
                            requestUrl = requestUrl(readLine);
                            break;
                    }
                }
                log.info(readLine);
                if(readLine == null || "".equals(readLine)) {
                    return;
                }
                count++;
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public byte[] returnPage(String requestUrl) {
        try {
            if("/".equals(requestUrl)) {
                requestUrl = "/index.html";
            }
            File file = new File("webapp" + requestUrl);
            return Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    private void response200Header(DataOutputStream dos, int lengthOgBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html; charset=utf-8\r\n");
            dos.writeBytes("Content-Length " + lengthOgBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
