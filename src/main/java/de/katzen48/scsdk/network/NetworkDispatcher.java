package de.katzen48.scsdk.network;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
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
	}
	
	
	public void registerPacket(Class<? extends IMessage> lPacketClass, Byte pChannel)
	{
		registeredPackets.put(pChannel, lPacketClass);
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
		sendBytes(((Client)networkable).getSocket(), pMessage, MessageTarget.SERVER, null);
	}
	
	public void broadcastPacket(IMessage pMessage, UUID pUUID)
	{
		if(!networkable.isClient())
		{
			for(ConnectedClient lClient : ((Server)networkable).getConnectedClients())
			{
				if(pUUID.equals(lClient.getClientID())) continue;
				sendBytes(lClient.getSocket(), pMessage, MessageTarget.ALL, null);
			}
		}
		else
		{
			sendBytes(((Client)networkable).getSocket(), pMessage, MessageTarget.ALL, null);
		}
	}
	
	private void sendBytes(Socket pSocket, IMessage pMessage, MessageTarget pTarget, UUID clientID)
	{
		try
		{
			if(networkable.isClient())
			{
				pSocket.getOutputStream().write(getChannel(pMessage));
				pSocket.getOutputStream().write(pTarget.value);
				ObjectOutputStream lOutput = (ObjectOutputStream) pSocket.getOutputStream();
				lOutput.write(clientID.toString().getBytes());
				ByteBuf lByteBuf = new ByteBuf();
				pMessage.toBytes(lByteBuf);
				lOutput.writeObject(lByteBuf.getBytes());
			}
			else
			{
				pSocket.getOutputStream().write(getChannel(pMessage));
				ObjectOutputStream lOutput = (ObjectOutputStream) pSocket.getOutputStream();
				lOutput.write(clientID.toString().getBytes());
				ByteBuf lByteBuf = new ByteBuf();
				pMessage.toBytes(lByteBuf);
				lOutput.writeObject(lByteBuf.getBytes());
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
			if(registeredPackets.get(lChannel).getClass().equals(pMessage.getClass()))
			{
				return lChannel;
			}
		}
		return -1;
	}
}
