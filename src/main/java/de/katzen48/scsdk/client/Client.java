package de.katzen48.scsdk.client;

import java.lang.reflect.Field;
import java.net.Socket;

import de.katzen48.scsdk.Networkable;
import de.katzen48.scsdk.event.events.client.RemoteConnectEvent;

public class Client extends Networkable
{
	protected Socket socket;


	public Client()
	{
		setClientFlag();
	}


	public void start(String pHost, int pPort)
	{
		try
		{
			this.socket = new Socket(pHost, pPort);
			RemoteConnectEvent lConnectEvent = new RemoteConnectEvent(this.socket);
			getEventManager().fireEvent(lConnectEvent);
			if(lConnectEvent.isCancelled()) socket.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
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

	public Socket getSocket()
	{
		return this.socket;
	}
}
