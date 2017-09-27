package de.katzen48.scsdk.event.events.server;

import de.katzen48.scsdk.event.Event;
import de.katzen48.scsdk.network.packet.IMessage;
import de.katzen48.scsdk.server.ConnectedClient;

public class MessageBroadcastEvent extends Event
{
	private ConnectedClient originClient;
	private IMessage message;
	
	
	public MessageBroadcastEvent(ConnectedClient pOrigin, IMessage pMessage)
	{
		this.originClient = pOrigin;
		this.message = pMessage;
	}
	
	
	public ConnectedClient getOrigin()
	{
		return this.originClient;
	}
	
	public IMessage getMessage()
	{
		return this.message;
	}
}
