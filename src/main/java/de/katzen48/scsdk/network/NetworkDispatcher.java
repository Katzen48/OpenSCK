package de.katzen48.scsdk.network;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.ListIterator;
import java.util.Map;
import java.util.UUID;

import de.katzen48.scsdk.Networkable;
import de.katzen48.scsdk.client.Client;
import de.katzen48.scsdk.network.packet.IMessage;
import de.katzen48.scsdk.server.ConnectedClient;
import de.katzen48.scsdk.server.Server;

public class NetworkDispatcher
{
	protected Map<Byte,Class<? extends IMessage>> registeredPackets;
	private Networkable networkable;
	
	
	public NetworkDispatcher(Networkable pNetworkable)
	{
		registeredPackets = new HashMap<Byte,Class<? extends IMessage>>();
		networkable = pNetworkable;
	}
	
	
	public void registerPacket(Class<? extends IMessage> lPacketClass, Byte pChannel)
	{
		if(isChannelRegistered(pChannel)) return;
		
		byte lChannel = pChannel;
		System.out.println(lChannel);
		registeredPackets.put(lChannel, lPacketClass);
	}
	
	private boolean isChannelRegistered(byte pChannel)
	{
		return registeredPackets.containsKey(pChannel);
	}
	
	public Map<Byte,Class<? extends IMessage>> getRegisteredPackets()
	{
		return this.registeredPackets;
	}
	
	public void sendPacketToClient(UUID pClientID, IMessage pMessage, UUID pOrigin)
	{
		if(networkable.isClient())
		{
			sendBytes(((Client)networkable).getSocket(), pMessage, MessageTarget.CLIENT, pClientID);
		}
		else
		{
			sendBytes(((Server)networkable).getClient(pClientID).getSocket(), pMessage, MessageTarget.CLIENT, pOrigin);
		}
	}
	
	public void sendPacketToServer(IMessage pMessage)
	{
		sendBytes(((Client)networkable).getSocket(), pMessage, MessageTarget.SERVER, ((Client)networkable).getClientID());
	}
	
	public void broadcastPacket(IMessage pMessage, UUID pUUID)
	{
		if(!networkable.isClient())
		{
			for(ListIterator<ConnectedClient> it = ((Server)networkable).getConnectedClients().listIterator() ; it.hasNext();)
			{
				ConnectedClient lClient = it.next();
				if(pUUID.equals(lClient.getClientID())) continue;
				sendBytes(lClient.getSocket(), pMessage, MessageTarget.ALL, pUUID);
			}
		}
		else
		{
			sendBytes(((Client)networkable).getSocket(), pMessage, MessageTarget.ALL, ((Client)networkable).getClientID());
		}
	}
	
	private void sendBytes(Socket pSocket, IMessage pMessage, MessageTarget pTarget, UUID clientID)
	{
		try
		{
			if(networkable.isClient())
			{
				ObjectOutputStream lOutput = new ObjectOutputStream(pSocket.getOutputStream());
				lOutput.writeByte(getChannel(pMessage));
				lOutput.writeByte(pTarget.value);
				lOutput.writeUTF(clientID.toString());
				ByteBuf lByteBuf = new ByteBuf();
				pMessage.toBytes(lByteBuf);
				lOutput.writeObject(lByteBuf.getBytes());
				lOutput.flush();
			}
			else
			{
				ObjectOutputStream lOutput = new ObjectOutputStream(pSocket.getOutputStream());
				lOutput.writeByte(getChannel(pMessage));
				lOutput.writeUTF(clientID.toString());
				ByteBuf lByteBuf = new ByteBuf();
				pMessage.toBytes(lByteBuf);
				lOutput.writeObject(lByteBuf.getBytes());
				lOutput.flush();
			}
			
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	private byte getChannel(IMessage pMessage)
	{
		for(byte lChannel : registeredPackets.keySet())
		{
			if(registeredPackets.get(lChannel).equals(pMessage.getClass()))
			{
				return lChannel;
			}
		}
		return -1;
	}
}
