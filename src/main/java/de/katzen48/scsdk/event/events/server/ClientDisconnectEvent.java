package de.katzen48.scsdk.event.events.server;

import de.katzen48.scsdk.event.Event;
import de.katzen48.scsdk.server.ConnectedClient;

public class ClientDisconnectEvent extends Event
{
	private ConnectedClient client;
	
	
	public ClientDisconnectEvent(ConnectedClient pClient)
	{
		this.client = pClient;
	}
	
	
	public ConnectedClient getClient()
	{
		return this.client;
	}
}
