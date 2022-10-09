package controller.basic;

import httpmethod.HttpRequest;
import httpmethod.HttpResponse;

import java.util.Collection;

public abstract class AbstractController implements Controller{
    @Override
    public void service(HttpRequest request, HttpResponse response) {

    }

    abstract public void doGet(HttpRequest request, HttpResponse response);

    abstract public void doPost(HttpRequest request, HttpResponse response);
}
