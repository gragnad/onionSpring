package webserver;


import com.sun.xml.internal.ws.api.ha.StickyFeature;
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

    interface  Status {
        String URL_PATH = "responseUrl";
        String STATUS_CODE = "statusCode";
        String COOKIE = "cookie";
        String ACCEPT = "Accept";
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
            Map<String, Object> executeData = requestInput(in);
            byte[] body =  returnPage(executeData.get(Status.URL_PATH).toString());
            if(body != null) {//page
                responseHeader(dos, body.length,
                        executeData.get(Status.STATUS_CODE).toString(),
                        executeData.get(Status.COOKIE) != null ? executeData.get(Status.COOKIE).toString() : null,
                        executeData.get(Status.URL_PATH).toString().contains("css") ? "text/css" : "text/html");
                responseBody(dos, body);
            } else {
                //
            }
        } catch (IOException | NullPointerException e) {
            log.error(e.getMessage());
        }
    }

    private Map<String, Object> requestInput(InputStream ins) {
        try {
            InputStreamReader streamReader = new InputStreamReader(ins);
            BufferedReader br = new BufferedReader(streamReader);
            String readLine = "";
            String getRestUrl = "";
            Map<String, String> header = new HashMap<>();
            int count = 0;
            while (true) {
                readLine = br.readLine();
                //log.info(readLine);
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

    private Map<String, Object> restUrlExeCute(String[] restUrl, BufferedReader br, Map<String, String> header) throws IOException {
        Map<String, Object> executeResult = new HashMap<>();
        executeResult.put(Status.ACCEPT , header.get(Status.ACCEPT));
        switch (restUrl[0]) {
            case HttpMethod.GET:
                GetMethod getMethod = new GetMethod();
                Map<String, String> loginCookie = parseCookies(header.get("Cookie"));
                executeResult.put(Status.URL_PATH, getMethod.execute(restUrl[1]));
                if(loginCookie.containsKey("logined")) {
                    if(loginCookie.get("logined").equals("false")) {
                        executeResult.put(Status.STATUS_CODE, "302");
                    } else {
                        executeResult.put(Status.STATUS_CODE, "200");
                    }
                } else {
                    if(restUrl[1].equals("/user/list.html")) {
                        executeResult.put(Status.STATUS_CODE, "302");
                    } else {
                        executeResult.put(Status.STATUS_CODE, "200");
                    }
                }

                break;
            case HttpMethod.POST:
                String postData = IOUtils.readData(br, Integer.parseInt(header.get("Content-Length")));
                PostMethod postMethod = new PostMethod();
                executeResult = postMethod.execute(restUrl[1], postData);
                executeResult.put(Status.URL_PATH, postMethod.getResultUrl());
                if(postMethod.isSuccess()) {
                    executeResult.put(Status.STATUS_CODE, "302");
                } else {
                    executeResult.put(Status.STATUS_CODE, "200");
                }
                executeResult.put(Status.COOKIE, postMethod.getMakeCookie());
                break;
            case HttpMethod.PUT:
            case HttpMethod.DELETE:
                executeResult.put(Status.URL_PATH, restUrl[1]);
                executeResult.put(Status.STATUS_CODE, "200");
                break;
        }
        //log.info(executeResult.toString());
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
//            log.info("=====URL PATH=====");
//            log.info(file.getAbsolutePath());
            return Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void responseHeader(DataOutputStream dos, int lengthOgBodyContent, String status,
                                String cookies, String accept) {
        try {
            switch (status) {
                case "200":
                    dos.writeBytes("HTTP/1.1 " + status + "\r\n");
                    break;
                case "302":
                    dos.writeBytes("HTTP/1.1 " + status + " Found \r\n");
                    dos.writeBytes("Location: " + "/index.html" + "\r\n");
                    break;
            }

            dos.writeBytes("Content-Type: " + accept + "; charset=utf-8 " + "\r\n");
            dos.writeBytes("Content-Length " + lengthOgBodyContent + "\r\n");
            if(cookies != null) {
                dos.writeBytes("Set-Cookie: " + cookies + "\r\n");
            }
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
