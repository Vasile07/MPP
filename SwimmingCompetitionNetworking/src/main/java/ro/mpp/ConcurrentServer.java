package ro.mpp;

import java.net.Socket;

public class ConcurrentServer extends AbstractServer {
    private ServiceInterface service;

    public ConcurrentServer(int port, ServiceInterface service) {
        super(port);
        this.service = service;
    }

    @Override
    protected Thread createWorker(Socket client){
        SwimmingCompetitionClientWorker worker = new SwimmingCompetitionClientWorker(service,client);
        Thread tw = new Thread(worker);
        return tw;
    }
}
