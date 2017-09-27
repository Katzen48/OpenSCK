package de.katzen48.scsdk.event;

public enum EventPriority
{
	MONITOR(0), HIGHEST(1), HIGH(2), NORMAL(3), LOW(4), LOWEST(5);
	
	
	final int value;
	
	private EventPriority(int pValue)
	{
		this.value = pValue;
	}
};
