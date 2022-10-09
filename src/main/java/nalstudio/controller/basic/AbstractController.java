package nalstudio.controller.basic;

import nalstudio.httpmethod.HttpRequest;
import nalstudio.httpmethod.HttpResponse;

public abstract class AbstractController implements Controller{

    abstract public void doGet(HttpRequest request, HttpResponse response);

    abstract public void doPost(HttpRequest request, HttpResponse response);
}
