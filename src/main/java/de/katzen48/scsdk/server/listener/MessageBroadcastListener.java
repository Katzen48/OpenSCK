package de.katzen48.scsdk.server.listener;

import de.katzen48.scsdk.event.EventHandler;
import de.katzen48.scsdk.event.EventPriority;
import de.katzen48.scsdk.event.IListener;
import de.katzen48.scsdk.event.events.server.MessageBroadcastEvent;
import de.katzen48.scsdk.server.Server;

public class MessageBroadcastListener implements IListener
{
	private Server server;
	
	
	public MessageBroadcastListener(Server pServer)
	{
		this.server = pServer;
	}
	
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onMessageBroadcast(MessageBroadcastEvent pEvent)
	{
		server.getNetworkDispatcher().broadcastPacket(pEvent.getMessage(), pEvent.getOrigin().getClientID());
	}
}
