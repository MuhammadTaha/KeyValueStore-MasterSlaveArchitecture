package de.tub.ise.ec;

//import com.sun.java.util.jar.pack.Instruction;
import de.tub.ise.ec.kv.FileSystemKVStore;
import de.tub.ise.ec.kv.KeyValueInterface;
import de.tub.ise.hermes.*;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.lang.invoke.SwitchPoint;

public class Main {

	private static final Logger LOG = LogManager.getLogger(Main.class);

	public static void main(String[] args) {

		int instanceType = 0; //0 client, 1 write server, 2 read server

		// HERMES
		int port = 8080;
		String host = "127.0.0.1"; // localhost

		////args0 = type, arg1 = port, arg2 = host
		if(args.length>1)
		{
			instanceType = Integer.parseInt(args[0]);
			port = Integer.parseInt(args[1]);
			host = args[2];
		}

		switch(instanceType)
		{
			case 0: //Client
				try {
					SetupClient(host,port,1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				break;

			case 1: //Writing server
                SetupMasterServer(host,port);
				break;

			case 2: //Read Server
                SetupSlaveServer(host,port);
				break;
		}

	}

    public static void SetupMasterServer(String host, int port) /*throws InterruptedException */{

        // Server: register handler
        RequestHandlerRegistry reg = RequestHandlerRegistry.getInstance();
        reg.registerHandler("MasterMessageHandler", new SampleMessageHandler());

        // Server: start receiver
        try {
            Receiver receiver = new Receiver(port);
            receiver.start();
        } catch (IOException e) {
            System.out.println("Connection error: " + e);
        }


    }


    public static void SetupSlaveServer(String host, int port) /*throws InterruptedException */{

        // Server: register handler
        RequestHandlerRegistry reg = RequestHandlerRegistry.getInstance();
        reg.registerHandler("MasterMessageHandler", new SampleMessageHandler());

        // Server: start receiver
        try {
            Receiver receiver = new Receiver(port);
            receiver.start();
        } catch (IOException e) {
            System.out.println("Connection error: " + e);
        }


    }

	public static void SetupClient(String host, int port, int iteration) throws InterruptedException {

		Sender sender = new Sender(host, port);




		while (iteration-- >=0) {
			// Client: send message

			// create request with different message ID every time and send it
			Request req = new Request("Message"+iteration, "sampleMessageHandler", "Group35Client");

			LOG.info("Client Start Writing Message"+iteration);
			Response res = sender.sendMessage(req, 5000);
			LOG.info("Client Committed Message"+iteration);

			System.out.println("Received: " + res.getResponseMessage());

			//sleep the thread for a second
			Thread.sleep(1000);
		}
	}
}
