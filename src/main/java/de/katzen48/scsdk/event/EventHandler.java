package de.katzen48.scsdk.event;

public @interface EventHandler
{
	EventPriority priority() default EventPriority.NORMAL;
	boolean ignoreCancelled() default false;
}
