package de.katzen48.scsdk.event.events.client;

import java.net.Socket;

import de.katzen48.scsdk.event.Event;

public class RemoteConnectEvent extends Event
{
	private Socket socket;
	
	
	public RemoteConnectEvent(Socket pSocket)
	{
		this.socket = pSocket;
	}
	
	
	public Socket getSocket()
	{
		return this.socket;
	}
}
