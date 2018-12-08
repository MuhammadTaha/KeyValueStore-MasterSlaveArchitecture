package de.tub.ise.ec;

import de.tub.ise.ec.kv.FileSystemKVStore;
import de.tub.ise.ec.kv.KeyValueInterface;
import de.tub.ise.hermes.*;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.Serializable;
import java.util.List;

public class MasterMessageHandler implements IRequestHandler {

    private static final Logger LOG = LogManager.getLogger(Main.class);
    KeyValueInterface store = new FileSystemKVStore();
    static Sender sender = new Sender(Main.slaveHost, Main.slavePort); //Not a better way but

    @Override
    public Response handleRequest(Request req) {

        List<Serializable> messages = req.getItems();
        Message msg = (Message) messages.get(0);
        System.out.println("Master Node Message received: "+msg.id);
        LOG.info("Server received: "+msg.id);
        switch (msg.operation)
        {
            case  "R":
                break;
            case  "U":
            case "C":
                store.store(msg.key,msg.value);
                break;
            case  "D":
                store.delete(msg.key);
                break;
        }
        LOG.info("Server committed: "+msg.id);

        ReplicateData(msg);

        return new Response("Echo okay for target: " + req.getTarget(), true, req, req.getItems());
    }

    public void ReplicateData(Message msg)
    {
        Request req = new Request(msg,"SlaveMessageHandler", "MasterMessageHandler");
        LOG.info("Server Replication started for Message" + msg.id);

        if(Main.serverType ==0) {//sync server
            Response res = sender.sendMessage(req, 5000);
        }
        else{
            sender.sendMessageAsync(req, new AsyncCallbackRecipient() {
                @Override
                public void callback(Response response) {
                    System.out.println("Async response Received: " + response.getResponseMessage());
                }
            });

        }
        LOG.info("Server Replication Completed for Message" + msg.id);
    }

    @Override
    public boolean requiresResponse() {
        //Socket connection should wait for the request completion and avoid sending the response right away
        return true;
    }
}
