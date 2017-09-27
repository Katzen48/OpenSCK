package de.katzen48.scsdk.event.events.server;

import de.katzen48.scsdk.event.Event;
import de.katzen48.scsdk.network.packet.IMessage;
import de.katzen48.scsdk.server.ConnectedClient;

public class MessageRedirectEvent extends Event
{
	private ConnectedClient originClient;
	private ConnectedClient targetClient;
	private IMessage message;
	
	
	public MessageRedirectEvent(ConnectedClient pOrigin, ConnectedClient pTarget, IMessage pMessage)
	{
		this.originClient = pOrigin;
		this.targetClient = pTarget;
		this.message = pMessage;
	}


	public ConnectedClient getOrigin()
	{
		return originClient;
	}

	public ConnectedClient getTarget()
	{
		return targetClient;
	}

	public IMessage getMessage()
	{
		return message;
	}
}
