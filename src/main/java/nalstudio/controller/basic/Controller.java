package nalstudio.controller.basic;

import nalstudio.httpmethod.HttpRequest;
import nalstudio.httpmethod.HttpResponse;

public interface Controller {
    void service(HttpRequest request, HttpResponse response);
}
