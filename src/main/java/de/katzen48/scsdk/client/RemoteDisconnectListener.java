package de.katzen48.scsdk.client;


import java.net.Socket;

import de.katzen48.scsdk.event.EventHandler;
import de.katzen48.scsdk.event.EventPriority;
import de.katzen48.scsdk.event.IListener;
import de.katzen48.scsdk.event.events.client.RemoteDisconnectEvent;

public class RemoteDisconnectListener implements IListener
{
	private Client client;
	
	
	protected RemoteDisconnectListener(Client pClient)
	{
		this.client = pClient;
	}
	
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void onDisconnect(RemoteDisconnectEvent pEvent)
	{
		new Thread(()->{
			while(!client.ready)
			{
				try
				{
					client.socket = new Socket(client.host, client.port);
				}
				catch (Exception e) {}
				finally
				{
					client.ready = true;
				}
			}
		}).start();	
	}
}
