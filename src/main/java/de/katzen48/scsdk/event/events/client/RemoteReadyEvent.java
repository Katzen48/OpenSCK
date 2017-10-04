package de.katzen48.scsdk.event.events.client;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.katzen48.scsdk.client.Client;
import de.katzen48.scsdk.event.Event;

public class RemoteReadyEvent extends Event
{
	private List<UUID> networkables;
	private Client client;
	
	
	public RemoteReadyEvent(List<UUID> pNetworkables, Client pClient)
	{
		this.networkables = pNetworkables;
		this.client = pClient;
	}
	
	
	public UUID getServerID()
	{
		return networkables.get(0);
	}
	
	public List<UUID> getClients()
	{
		List<UUID> lClients = new ArrayList<UUID>(networkables);
		lClients.remove(0);
		return lClients;
	}
	
	public Client getLocalClient()
	{
		return this.client;
	}
}
