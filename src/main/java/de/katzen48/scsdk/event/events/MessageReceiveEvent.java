package de.katzen48.scsdk.event.events;

import java.util.UUID;

import de.katzen48.scsdk.event.Event;
import de.katzen48.scsdk.network.packet.IMessage;

public class MessageReceiveEvent extends Event
{
	private UUID origin;
	private IMessage message;
	
	
	public MessageReceiveEvent(UUID pAddress, IMessage pMessage)
	{
		this.origin = pAddress;
		this.message = pMessage;
	}
	
	
	public UUID getOrigin()
	{
		return this.origin;
	}
	
	public IMessage getMessage()
	{
		return this.message;
	}
}
