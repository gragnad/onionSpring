package webserver;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import requestmethod.GetMethod;
import requestmethod.PostMethod;
import util.IOUtils;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import static util.HttpRequestUtils.*;

public class RequestHandler extends Thread{
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
    private Socket connection;

    interface RequestType {
            int REST_URL = 0;
    }

    interface HttpMethod {
        String GET = "GET";
        String POST = "POST";
        String PUT = "PUT";
        String DELETE = "DELETE";
    }

    interface Status {
        String URL_PATH = "requestUrl";
        String STATUS_CODE = "httpStatus";
        String HTTP_METHOD = "httpMethod";
    }

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    @Override
    public void run() {
        //log.debug("New Client Connect! Connect IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());
        //Stream : external data (read, send)
        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO this function is Process for client request
            DataOutputStream dos = new DataOutputStream(out);
            Map<String, String> executeData = requestInput(in);
            byte[] body =  returnPage(executeData.get(Status.URL_PATH));
            if(body != null) {//page
                responseHeader(dos, body.length, executeData.get(Status.STATUS_CODE));
                responseBody(dos, body);
            } else {
                //
            }
        } catch (IOException | NullPointerException e) {
            log.error(e.getMessage());
        }
    }

    private Map<String, String> requestInput(InputStream ins) {
        try {
            InputStreamReader streamReader = new InputStreamReader(ins);
            BufferedReader br = new BufferedReader(streamReader);
            String readLine = "";
            String getRestUrl = "";
            Map<String, String> header = new HashMap<>();
            int count = 0;
            while (true) {
                readLine = br.readLine();
                if(!"".equals(readLine)) {
                    switch (count) {
                        case RequestType.REST_URL:
                            getRestUrl = readLine;
                            break;
                        default:
                            Pair pair = parseHeader(readLine);
                            if(pair != null) {
                                header.put(pair.getKey(), pair.getValue());
                            }
                            break;
                    }
                }
                if(readLine == null || "".equals(readLine)) {
                    if(getRestUrl != null) {
                        return restUrlExeCute(requestUrl(getRestUrl), br, header);
                    }
                    return null;
                }
                count++;
            }
        } catch (IOException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    private Map<String, String> restUrlExeCute(String[] restUrl, BufferedReader br, Map<String, String> header) throws IOException {
        Map<String, String> executeResult = new HashMap<>();
        executeResult.put(Status.HTTP_METHOD, restUrl[0]);
        switch (restUrl[0]) {
            case HttpMethod.GET:
                executeResult.put(Status.URL_PATH, GetMethod.execute(restUrl[1]));
                break;
            case HttpMethod.POST:
                String postData = IOUtils.readData(br, Integer.parseInt(header.get("Content-Length")));
                executeResult.put(Status.URL_PATH, "/index.html");
                if(PostMethod.execute(restUrl[1], postData)) {
                    executeResult.put(Status.STATUS_CODE, "200");
                } else {
                    executeResult.put(Status.STATUS_CODE, "302");
                }
                break;
            case HttpMethod.PUT:
            case HttpMethod.DELETE:
                executeResult.put(Status.URL_PATH, restUrl[1]);
                executeResult.put(Status.STATUS_CODE, "200");
                break;
        }
        return executeResult;
    }

    public byte[] returnPage(String requestUrl) {
        try {
            switch (requestUrl) {
                case "/":
                    requestUrl = "/index.html";
                    break;
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

    private void responseHeader(DataOutputStream dos, int lengthOgBodyContent, String status) {
        try {
            dos.writeBytes("HTTP/1.1 " + status + "\r\n");
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
