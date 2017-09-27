package de.katzen48.scsdk.server.listener;

import de.katzen48.scsdk.event.EventHandler;
import de.katzen48.scsdk.event.EventPriority;
import de.katzen48.scsdk.event.IListener;
import de.katzen48.scsdk.event.events.server.MessageRedirectEvent;
import de.katzen48.scsdk.server.Server;

public class MessageRedirectListener implements IListener
{
	private Server server;
	
	
	public MessageRedirectListener(Server pServer)
	{
		this.server = pServer;
	}
	
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onMessageRedirect(MessageRedirectEvent pEvent)
	{
		server.getNetworkDispatcher().sendPacketToClient(pEvent.getTarget().getClientID(), pEvent.getMessage(), pEvent.getOrigin().getClientID());
	}
}
