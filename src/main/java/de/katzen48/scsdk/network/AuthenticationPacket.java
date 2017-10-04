package de.katzen48.scsdk.network;

import java.util.UUID;

import de.katzen48.scsdk.network.packet.IMessage;

public class AuthenticationPacket implements IMessage
{
	private UUID uuid;

	
	public AuthenticationPacket() {}
	
	public AuthenticationPacket(UUID pUUID)
	{
		this.uuid = pUUID;
	}
	
	
	@Override
	public void toBytes(ByteBuf byteBuf)
	{
		byteBuf.setNextString(uuid.toString());
	}

	@Override
	public void fromBytes(ByteBuf byteBuf)
	{
		this.uuid = UUID.fromString(byteBuf.getNextAsString());	
	}

	public UUID getUUID()
	{
		return this.uuid;
	}
	
}
