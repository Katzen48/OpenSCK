package de.katzen48.scsdk.network.packet;

import de.katzen48.scsdk.network.ByteBuf;

public interface IMessage
{
	public abstract void toBytes(ByteBuf byteBuf);
	public abstract void fromBytes(ByteBuf byteBuf);
}
