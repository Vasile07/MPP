package ro.mpp;

import java.net.Socket;

public abstract class AbstractServer extends AbsServer {

    public AbstractServer(Integer port) {
        super(port);
    }

    @Override
    protected void proccesRequest(Socket client){
        Thread tw = createWorker(client);
        tw.start();
    }

    protected abstract Thread createWorker(Socket client);
}
