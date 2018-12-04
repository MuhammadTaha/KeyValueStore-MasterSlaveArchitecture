package de.tub.ise.ec;

import de.tub.ise.ec.kv.FileSystemKVStore;
import de.tub.ise.ec.kv.KeyValueInterface;
import de.tub.ise.hermes.IRequestHandler;
import de.tub.ise.hermes.Request;
import de.tub.ise.hermes.Response;

public class SlaveMessageHandler implements IRequestHandler {

    KeyValueInterface store = new FileSystemKVStore();

    @Override
    public Response handleRequest(Request req) {

        //TODO: Handler crud

        //store.store("monkey","banana");
        //System.out.println("Received: " + store.getValue("monkey2"));
        //store.delete("monkey");
        return new Response("Echo okay for target: " + req.getTarget(), true, req, req.getItems());

    }

    @Override
    public boolean requiresResponse() {
        //Socket connection should wait for the request completion and avoid sending the response right away
        return false;
    }
}
