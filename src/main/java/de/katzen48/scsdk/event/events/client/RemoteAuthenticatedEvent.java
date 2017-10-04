package de.katzen48.scsdk.event.events.client;

import java.net.Socket;
import java.util.UUID;

import de.katzen48.scsdk.event.Event;

public class RemoteAuthenticatedEvent extends Event
{
	private Socket socket;
	private UUID clientID;
	
	
	public RemoteAuthenticatedEvent(Socket pSocket, UUID pUUID)
	{
		this.socket = pSocket;
		this.clientID = pUUID;
	}


	public Socket getSocket()
	{
		return socket;
	}

	public UUID getClientID()
	{
		return clientID;
	}
}
