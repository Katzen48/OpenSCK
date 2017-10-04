package de.katzen48.scsdk.network;

public enum MessageTarget
{
	CLIENT(-1), ALL(-2), SERVER(-3);
	
	
	public final int value;
	
	private MessageTarget(int pValue)
	{
		this.value = pValue;
	}
}
