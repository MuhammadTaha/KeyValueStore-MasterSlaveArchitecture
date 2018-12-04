package de.tub.ise.ec;

import de.tub.ise.ec.kv.FileSystemKVStore;
import de.tub.ise.ec.kv.KeyValueInterface;
import de.tub.ise.hermes.IRequestHandler;
import de.tub.ise.hermes.Request;
import de.tub.ise.hermes.Response;

import java.io.Serializable;
import java.util.List;

public class MasterMessageHandler implements IRequestHandler {

    KeyValueInterface store = new FileSystemKVStore();

    @Override
    public Response handleRequest(Request req) {

        //TODO: Handler crud

        List<Serializable> messages = req.getItems();
        Message msg = (Message) messages.get(0);
        System.out.println("Master Node Message received: "+msg.id);

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
        return new Response("Echo okay for target: " + req.getTarget(), true, req, req.getItems());
    }

    @Override
    public boolean requiresResponse() {
        //Socket connection should wait for the request completion and avoid sending the response right away
        return true;
    }
}
