package de.katzen48.scsdk.server;

import java.io.IOException;
import java.net.Socket;
import java.util.UUID;

import de.katzen48.scsdk.event.events.server.ClientConnectEvent;

class ServerSocketRunnable implements Runnable
{
	Server server;


	protected ServerSocketRunnable(Server lServer)
	{
		this.server = lServer;
	}


	@Override
	public void run()
	{
		while(true)
		{
			if(server.isAcceptingClients())
			{
				try
				{
					Socket lSocket = server.serverSocket.accept();
					ClientConnectEvent lConnectEvent = new ClientConnectEvent(lSocket);
					server.getEventManager().fireEvent(lConnectEvent);
					ConnectedClient lClient = new ConnectedClient(UUID.randomUUID(), lSocket);
					server.addClient(lClient);
					if(lConnectEvent.isCancelled()) server.kickClient(lClient);
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
	}
}
