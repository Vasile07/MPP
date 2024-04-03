package ro.mpp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public abstract class AbsServer {
    private int port;

    public AbsServer(int port) {
        this.port = port;
    }

    public void start(){
        ServerSocket server=null;
        try{
            server=new ServerSocket(port);
            while(true){
                Socket client=server.accept();
                proccesRequest(client);
            }
        }catch(IOException ex){
            throw new RuntimeException(ex.getMessage());
        }finally{
            if(server!=null){
                try{
                    server.close();
                }catch(IOException ex){
                    throw new RuntimeException(ex.getMessage());
                }
            }
        }
    }

    protected abstract void proccesRequest(Socket client);

}
