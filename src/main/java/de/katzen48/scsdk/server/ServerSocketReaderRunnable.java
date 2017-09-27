package de.katzen48.scsdk.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.List;
import java.util.UUID;

import de.katzen48.scsdk.event.events.MessageReceiveEvent;
import de.katzen48.scsdk.event.events.server.MessageBroadcastEvent;
import de.katzen48.scsdk.event.events.server.MessageRedirectEvent;
import de.katzen48.scsdk.network.ByteBuf;
import de.katzen48.scsdk.network.MessageTarget;
import de.katzen48.scsdk.network.packet.IMessage;

class ServerSocketReaderRunnable implements Runnable
{
	private List<Socket> sockets;
	private Server server;
	
	
	protected ServerSocketReaderRunnable(List<Socket> pSockets, Server pServer)
	{
		this.sockets = pSockets;
		this.server = pServer;
	}
	
	
	@Override
	public void run()
	{
		while(true)
		{
			for(Socket lSocket : sockets)
			{
				try
				{
					InputStream lInput = lSocket.getInputStream();
					if(lInput.available() > 0)
					{
					    ObjectInputStream is = new ObjectInputStream(lInput);
						try
						{
							int lChannel = lInput.read();
							int lTarget = lInput.read();
							UUID lClientID = null;
							byte[] lIDArray = (byte[])is.readObject();
							if(MessageTarget.CLIENT.value == lTarget) lClientID = UUID.fromString(new String(lIDArray, "UTF-8"));
							byte[][] lArray = (byte[][])is.readObject();
							IMessage lPacket = server.getNetworkDispatcher().getRegisteredPackets().get((byte) lChannel).newInstance();
							lPacket.fromBytes(new ByteBuf(lArray));
							fireEvent(lSocket, lChannel, lTarget, lClientID, lPacket);
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
