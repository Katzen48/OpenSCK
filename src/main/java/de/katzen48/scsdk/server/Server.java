package de.katzen48.scsdk.server;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.katzen48.scsdk.Networkable;

public class Server extends Networkable
{
	protected ServerSocket serverSocket;
	private List<ConnectedClient> clients;
	private int maxClients;
	private boolean acceptingClients;
	private UUID uuid;
	
	
	public Server(int pPort, int pMaxClients)
	{
		try
		{
			setClientFlag();
			this.serverSocket = new ServerSocket(pPort);
			this.maxClients = pMaxClients;
			this.acceptingClients = false;
			this.uuid = UUID.randomUUID();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public Server(int pPort)
	{
		this(pPort, -1);
	}
	
	
	public void start()
	{
		this.clients = new ArrayList<ConnectedClient>();
		this.socketThread = new Thread(new ServerSocketRunnable(this));
		socketThread.start();
	}
	
	protected void addClient(ConnectedClient pClient)
	{
		clients.add(pClient);
	}
	
	public boolean isAcceptingClients()
	{
		return (acceptingClients && (clients.size() < maxClients || maxClients < 0));
	}
	
	private void setClientFlag()
	{
		try
		{
			Field lFlag = this.getClass().getSuperclass().getDeclaredField("client");
			lFlag.setAccessible(true);
			lFlag.set(this, false);
			lFlag.setAccessible(false);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public List<ConnectedClient> getConnectedClients()
	{
		return this.clients;
	}
	
	public ConnectedClient getClient(UUID pUUID)
	{
		ConnectedClient lClient = null;
		for(ConnectedClient lIteratedClient : clients)
		{
			if(lIteratedClient.getClientID().equals(pUUID)) return lIteratedClient;
		}
		return lClient;
	}
	
	protected ConnectedClient getClient(Socket pSocket)
	{
		ConnectedClient lClient = null;
		for(ConnectedClient lIteratedClient : clients)
		{
			if(lIteratedClient.getSocket().equals(pSocket)) return lIteratedClient;
		}
		return lClient;
	}
	
	public UUID getServerID()
	{
		return this.uuid;
	}
}
