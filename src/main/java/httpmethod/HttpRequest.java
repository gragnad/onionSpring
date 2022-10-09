package httpmethod;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;


import static util.HttpRequestUtils.*;

public class HttpRequest {
    private static final Logger log = LoggerFactory.getLogger(HttpRequest.class);
    interface HttpMethod {
        String GET = "GET";
        String POST = "POST";
        String PUT = "PUT";
        String DELETE = "DELETE";
    }

    private String httpMethod;
    private String path;
    private Map<String, String> header;
    private Map<String, String> responseBody;

    public HttpRequest(InputStream inputStream) throws IOException {
        InputStreamReader streamReader = new InputStreamReader(inputStream, "UTF-8");
        BufferedReader br = new BufferedReader(streamReader);
        header = new HashMap<>();
        //request Data first Line
        String readLine = br.readLine();

        String[] requestMain = requestUrl(readLine);
        settingHttpMethod(requestMain);
        while (!"".equals(readLine)) {
            readLine = br.readLine();
            makeHeader(readLine);
            if(readLine == null) {
                return;
            }
        }
        //post body 에 들어오는 데이터는 전체 헤더 다음줄에 위치
        settingRequestBody(httpMethod, br);
    }

    public String getMethod() {
        return httpMethod;
    }

    public String getPath() {
        return path;
    }

    public String getHeader(String keyValue) {
        return header.get(keyValue);
    }

    public String getParameter(String keyValue) {
        return responseBody.get(keyValue);
    }

    private void urlGetQueryParamCheck(String requestUrl) {
        String urlQuery = "";
        if(requestUrl.contains("?")) {
            int index = requestUrl.indexOf("?");
            path = requestUrl.substring(0, index);
            String params = requestUrl.substring(index + 1);
            responseBody = parseQueryString(params);
        } else {
            path = requestUrl;
        }
    }

    private void urlPostBody(BufferedReader br, int contentLength) throws IOException {
        String postData = IOUtils.readData(br, contentLength);
        responseBody = parseQueryString(postData);
    }

    private void makeHeader(String readLine) {
        HttpRequestUtils.Pair pair = parseHeader(readLine);
        if(pair != null) {
            header.put(pair.getKey(), pair.getValue());
        }
    }


    private void settingRequestBody(String httpMethod, BufferedReader br) throws IOException {
        switch (httpMethod) {
            case HttpMethod.POST:
                urlPostBody(br, Integer.parseInt(header.get("Content-Length")));
                break;
            case HttpMethod.PUT:
                break;
            case HttpMethod.DELETE:
                break;
        }
    }

    private void settingHttpMethod(String[] readFirstLine) {
        switch (readFirstLine[0]) {
            case HttpMethod.GET:
                httpMethod = HttpMethod.GET;
                urlGetQueryParamCheck(readFirstLine[1]);
                break;
            case HttpMethod.POST:
                httpMethod = HttpMethod.POST;
                path = readFirstLine[1];
                break;
            case HttpMethod.PUT:
                httpMethod = HttpMethod.PUT;
                path = readFirstLine[1];
                break;
            case HttpMethod.DELETE:
                httpMethod = HttpMethod.DELETE;
                path = readFirstLine[1];
                break;
        }
    }
}
