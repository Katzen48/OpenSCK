package de.katzen48.scsdk.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import de.katzen48.scsdk.event.events.MessageReceiveEvent;
import de.katzen48.scsdk.event.events.server.ClientDisconnectEvent;
import de.katzen48.scsdk.event.events.server.MessageBroadcastEvent;
import de.katzen48.scsdk.event.events.server.MessageRedirectEvent;
import de.katzen48.scsdk.network.ByteBuf;
import de.katzen48.scsdk.network.MessageTarget;
import de.katzen48.scsdk.network.packet.IMessage;

class ServerSocketReaderRunnable implements Runnable
{
	private List<ConnectedClient> clients;
	private Server server;
	
	
	protected ServerSocketReaderRunnable(Server pServer)
	{
		this.server = pServer;
	}
	
	
	@Override
	public void run()
	{
		while(true)
		{
			this.clients = server.getConnectedClients();
			for(Iterator<ConnectedClient> it= clients.iterator() ; it.hasNext();)
			{
				ConnectedClient lClient = it.next();
				if(!lClient.isConnected())
				{
					server.getEventManager().fireEvent(new ClientDisconnectEvent(lClient));
					it.remove();
					continue;
				}
				
				try
				{
					InputStream lInput = lClient.getSocket().getInputStream();
					if(lInput.available() != 0)
					{
					    ObjectInputStream is = new ObjectInputStream(lInput);
						try
						{
							byte lChannel = is.readByte();
							int lTarget = (int) is.readByte();
							UUID lClientID = UUID.fromString(is.readUTF());
							byte[][] lArray = (byte[][])is.readObject();
							IMessage lPacket = server.getNetworkDispatcher().getRegisteredPackets().get((byte) lChannel).newInstance();
							lPacket.fromBytes(new ByteBuf(lArray));
							fireEvent(lClient.getSocket(), lChannel, lTarget, lClientID, lPacket);
						}
						catch (Exception e)
						{
							e.printStackTrace();
						}
					}
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
	}
	
	private void fireEvent(Socket pSocket, int pChannel, int pTarget, UUID pClientID, IMessage pMessage)
	{
		if(pTarget == MessageTarget.CLIENT.value)
		{
			server.getEventManager().fireEvent(new MessageRedirectEvent(server.getClient(pSocket), server.getClient(pClientID), pMessage));
		}
		else if(pTarget == MessageTarget.ALL.value)
		{
			server.getEventManager().fireEvent(new MessageBroadcastEvent(server.getClient(pSocket), pMessage));
		}
		else if(pTarget == MessageTarget.SERVER.value)
		{
			server.getEventManager().fireEvent(new MessageReceiveEvent(server.getClient(pSocket).getClientID(), pMessage));
		}
		
	}
}
