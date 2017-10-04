package de.katzen48.scsdk.server;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.katzen48.scsdk.event.EventHandler;
import de.katzen48.scsdk.event.EventPriority;
import de.katzen48.scsdk.event.IListener;
import de.katzen48.scsdk.event.events.server.ClientConnectEvent;
import de.katzen48.scsdk.network.AuthenticationPacket;
import de.katzen48.scsdk.network.ClientListPacket;

public class ClientConnectListener implements IListener
{
	private Server server;
	
	protected ClientConnectListener(Server pServer)
	{
		this.server = pServer;
	}
	
	@EventHandler(priority=EventPriority.MONITOR)
	public void onClientConnect(ClientConnectEvent pEvent)
	{
		UUID lClientID = server.getClient(pEvent.getSocket()).getClientID();
		server.getNetworkDispatcher().sendPacketToClient(lClientID, new AuthenticationPacket(lClientID), server.getServerID());
		server.getNetworkDispatcher().broadcastPacket(new ClientListPacket(getClientIdList(server)), server.getServerID());
	}
	
	
	private List<UUID> getClientIdList(Server pServer)
	{
		List<UUID> lClients = new ArrayList<UUID>();
		
		lClients.add(server.getServerID());
		
		for(ConnectedClient lClient : pServer.getConnectedClients())
		{
			lClients.add(lClient.getClientID());
		}
		
		return lClients;
	}
}
