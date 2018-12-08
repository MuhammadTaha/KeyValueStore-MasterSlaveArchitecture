package de.tub.ise.ec;

import de.tub.ise.ec.kv.FileSystemKVStore;
import de.tub.ise.ec.kv.KeyValueInterface;
import de.tub.ise.hermes.IRequestHandler;
import de.tub.ise.hermes.Request;
import de.tub.ise.hermes.Response;
import de.tub.ise.hermes.Sender;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.Serializable;
import java.util.List;

public class SlaveMessageHandler implements IRequestHandler {

    private static final Logger LOG = LogManager.getLogger(Main.class);
    KeyValueInterface store = new FileSystemKVStore();

    @Override
    public Response handleRequest(Request req) {

        List<Serializable> messages = req.getItems();
        Message msg = (Message) messages.get(0);
        System.out.println("Slave Node Message received: "+msg.id);
        LOG.info("Slave received: "+msg.id);

        /*
        * Testing delay to verify the async behavior
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        */
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
        LOG.info("Slave committed: "+msg.id);

        return new Response("Echo okay for target: " + req.getTarget(), true, req, req.getItems());
    }


    @Override
    public boolean requiresResponse() {
        //Socket connection should wait for the request completion and avoid sending the response right away
        return true;
    }
}
