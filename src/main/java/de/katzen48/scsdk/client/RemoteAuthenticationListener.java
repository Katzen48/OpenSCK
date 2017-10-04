package de.katzen48.scsdk.client;

import de.katzen48.scsdk.event.EventHandler;
import de.katzen48.scsdk.event.IListener;
import de.katzen48.scsdk.event.events.client.RemoteAuthenticatedEvent;

public class RemoteAuthenticationListener implements IListener
{
	private Client client;
	
	
	protected RemoteAuthenticationListener(Client pClient)
	{
		this.client = pClient;
	}
	
	
	@EventHandler
	public void onRemoteAuthenticate(RemoteAuthenticatedEvent pEvent)
	{
		client.setClientID(pEvent.getClientID());
	}
}
