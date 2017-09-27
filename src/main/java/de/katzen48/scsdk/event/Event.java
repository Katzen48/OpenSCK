package de.katzen48.scsdk.event;

public class Event 
{
	private boolean cancelled = false;
	
	
	public boolean isCancelled()
	{
		return this.cancelled;
	}
	
	public void setCancelled(boolean pCancelled)
	{
		this.cancelled = pCancelled;
	}
}
