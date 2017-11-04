package de.katzen48.scsdk.client;

import java.lang.reflect.Field;
import java.net.Socket;
import java.util.UUID;

import de.katzen48.scsdk.Networkable;
import de.katzen48.scsdk.event.events.client.RemoteConnectEvent;

public abstract class Client extends Networkable
{
	protected Socket socket;
	protected String host;
	protected int port;
	private boolean autoReconnect;
	private UUID clientID;
	private Thread socketThread;
	protected boolean ready = false;


	public Client()
	{
		setClientFlag();
	}


	public void start(String pHost, int pPort)
	{
		try
		{
			host = pHost;
			port = pPort;
			getEventManager().registerEvents(new MessageReceiveListener(this));
			getEventManager().registerEvents(new RemoteAuthenticationListener(this));
			this.socket = new Socket(host, port);
			this.socketThread = new Thread(new ClientSocketRunnable(this));
			socketThread.start();
			RemoteConnectEvent lConnectEvent = new RemoteConnectEvent(this.socket);
			getEventManager().fireEvent(lConnectEvent);
			if(lConnectEvent.isCancelled())
			{
				socket.close();
				return;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void start(String pHost, int pPort, boolean pAutoReconnect)
	{
		this.autoReconnect = pAutoReconnect;
		if(autoReconnect) getEventManager().registerEvents(new RemoteDisconnectListener(this));
		start(pHost, pPort);
	}

	public boolean doesAutoReconnect()
	{
		return autoReconnect;
	}
	
	private void setClientFlag()
	{
		try
		{
			Field lFlag = searchField(this.getClass(), "client");
			lFlag.setAccessible(true);
			lFlag.set(this, true);
			lFlag.setAccessible(false);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private Field searchField(Class<?> pClass, String pFieldName)
	{
		Field lField = null;
		try
		{
			lField = pClass.getDeclaredField(pFieldName);
		}
		catch(Exception e)
		{
			lField = searchField(pClass.getSuperclass(), pFieldName);
		}
		return lField;
	}
	
	protected void setClientID(UUID pUUID)
	{
		this.clientID = pUUID;
	}

	public UUID getClientID()
	{
		return this.clientID;
	}
	
	public Socket getSocket()
	{
		return this.socket;
	}
}
