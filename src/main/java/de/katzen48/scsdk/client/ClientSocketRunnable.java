package de.katzen48.scsdk.client;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.UUID;

import de.katzen48.scsdk.event.events.MessageReceiveEvent;
import de.katzen48.scsdk.event.events.client.RemoteDisconnectEvent;
import de.katzen48.scsdk.network.ByteBuf;
import de.katzen48.scsdk.network.packet.IMessage;

public class ClientSocketRunnable implements Runnable
{
	Client client;
	
	
	public ClientSocketRunnable(Client pClient)
	{
		this.client = pClient;
	}	
	
	
	@Override
	public void run()
	{
		try
		{
			InputStream lInput = client.socket.getInputStream();
			
			while(true)
			{
				if(client.socket.isClosed() && client.ready)
				{
					client.ready = false;
					client.getEventManager().fireEvent(new RemoteDisconnectEvent(client.socket));
					continue;
				}
				
				if(lInput.available() != 0)
				{
			    	ObjectInputStream is = new ObjectInputStream(lInput);
					try
					{
						byte lChannel = is.readByte();
						UUID lOrigin = UUID.fromString(is.readUTF());
						byte[][] lArray = (byte[][])is.readObject();
						IMessage lPacket = client.getNetworkDispatcher().getRegisteredPackets().get((byte) lChannel).newInstance();
						lPacket.fromBytes(new ByteBuf(lArray));
						client.getEventManager().fireEvent(new MessageReceiveEvent(lOrigin, lPacket));
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
