package de.katzen48.scsdk.server;

import java.net.Socket;
import java.util.UUID;

public class ConnectedClient
{
	private UUID clientID;
	private Socket socket;
	
	
	public ConnectedClient(UUID pUUID, Socket pSocket)
	{
		this.clientID = pUUID;
		this.socket = pSocket;
	}


	public UUID getClientID()
	{
		return clientID;
	}

	public Socket getSocket()
	{
		return socket;
	}
}
