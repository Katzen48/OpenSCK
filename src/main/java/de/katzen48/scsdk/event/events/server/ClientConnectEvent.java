package de.katzen48.scsdk.event.events.server;

import java.net.Socket;

import de.katzen48.scsdk.event.Event;

public class ClientConnectEvent extends Event
{
	private Socket socket;
	
	
	public ClientConnectEvent(Socket pSocket)
	{
		this.socket = pSocket;
	}
	
	
	public Socket getSocket()
	{
		return this.socket;
	}
}
