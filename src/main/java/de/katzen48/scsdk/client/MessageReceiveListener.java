package de.katzen48.scsdk.client;

import de.katzen48.scsdk.event.EventHandler;
import de.katzen48.scsdk.event.IListener;
import de.katzen48.scsdk.event.events.MessageReceiveEvent;
import de.katzen48.scsdk.event.events.client.RemoteAuthenticatedEvent;
import de.katzen48.scsdk.event.events.client.RemoteReadyEvent;
import de.katzen48.scsdk.network.AuthenticationPacket;
import de.katzen48.scsdk.network.ClientListPacket;

public class MessageReceiveListener implements IListener
{
	Client client;
	
	protected MessageReceiveListener(Client pClient) 
	{
		this.client = pClient;
	}
	
	 @EventHandler
	 public void onMessageReceive(MessageReceiveEvent pEvent)
	 {
		 if(pEvent.getMessage() instanceof AuthenticationPacket)
		 {
			 client.getEventManager().fireEvent(new RemoteAuthenticatedEvent(client.getSocket(), ((AuthenticationPacket) pEvent.getMessage()).getUUID()));
		 } 
		 else if(pEvent.getMessage() instanceof ClientListPacket && !client.ready)
		 {
			 client.ready = true;
			 client.getEventManager().fireEvent(new RemoteReadyEvent(((ClientListPacket)pEvent.getMessage()).getClients(), client));
		 }
	 }
}
