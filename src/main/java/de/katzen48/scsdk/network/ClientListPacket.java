package de.katzen48.scsdk.network;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.katzen48.scsdk.network.packet.IMessage;

public class ClientListPacket implements IMessage
{
	private List<UUID> clients;
	
	
	public ClientListPacket() {}
	
	public ClientListPacket(List<UUID> pClients)
	{
		this.clients = pClients;
	}
	

	@Override
	public void toBytes(ByteBuf byteBuf)
	{
		byteBuf.setNextInteger(clients.size());
		for(UUID lClient : clients)
		{
			byteBuf.setNextString(lClient.toString());
		}
	}

	@Override
	public void fromBytes(ByteBuf byteBuf)
	{
		clients = new ArrayList<UUID>();
		int lCount = byteBuf.getNextAsInteger();
		for(int i = 0 ; i < lCount ; i++)
		{
			clients.add(UUID.fromString(byteBuf.getNextAsString()));
		}
	}
	
	public List<UUID> getClients()
	{
		return clients;
	}

}
