package de.katzen48.scsdk;

import de.katzen48.scsdk.event.EventManager;
import de.katzen48.scsdk.network.AuthenticationPacket;
import de.katzen48.scsdk.network.ClientListPacket;
import de.katzen48.scsdk.network.NetworkDispatcher;

public abstract class Networkable
{
	protected Thread socketThread;
	private NetworkDispatcher dispatcher;
	private EventManager eventManager;
	private boolean client;
	
	
	public Networkable()
	{
		this.dispatcher = new NetworkDispatcher(this);
		this.eventManager = new EventManager();
		
		dispatcher.registerPacket(AuthenticationPacket.class, (byte) -2);
		dispatcher.registerPacket(ClientListPacket.class, (byte) -3);
	}
	
	
	public NetworkDispatcher getNetworkDispatcher()
	{
		return this.dispatcher;
	}
	
	public EventManager getEventManager()
	{
		return this.eventManager;
	}
	
	public boolean isClient()
	{
		return client;
	}
}
