package de.katzen48.scsdk;

import de.katzen48.scsdk.event.EventManager;
import de.katzen48.scsdk.network.NetworkDispatcher;

public class Networkable
{
	protected Thread socketThread;
	private NetworkDispatcher dispatcher;
	private EventManager eventManager;
	private boolean client;
	
	
	protected Networkable()
	{
		this.dispatcher = new NetworkDispatcher(this);
		this.eventManager = new EventManager();
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
