package de.katzen48.scsdk.network;

public enum MessageTarget
{
	CLIENT(0), ALL(1), SERVER(2);
	
	
	public final int value;
	
	private MessageTarget(int pValue)
	{
		this.value = pValue;
	}
}
