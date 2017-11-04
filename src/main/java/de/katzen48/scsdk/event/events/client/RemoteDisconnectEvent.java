package de.katzen48.scsdk.event.events.client;

import java.net.Socket;

import de.katzen48.scsdk.event.Event;

public class RemoteDisconnectEvent extends Event
{
	private Socket socket;
	
	
	public RemoteDisconnectEvent(Socket pSocket)
	{
		this.socket = pSocket;
	}
	
	
	public Socket getSocket()
	{
		return this.socket;
	}
}
