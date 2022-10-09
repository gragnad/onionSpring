package controller.basic;

import httpmethod.HttpRequest;
import httpmethod.HttpResponse;

public interface Controller {
    void service(HttpRequest request, HttpResponse response);
}
