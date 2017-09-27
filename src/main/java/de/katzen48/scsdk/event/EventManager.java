package de.katzen48.scsdk.event;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class EventManager
{
	private List<Method> eventMethods;
	
	
	public EventManager()
	{
		this.eventMethods = new ArrayList<Method>();
	}
	
	
	public void registerEvents(Class<? extends IListener> pClass)
	{
		for(Method lMethod : pClass.getMethods())
		{
			registerEvent(lMethod);
		}
		sortEvents();
	}
	
	public void registerEvent(Method pMethod)
	{
		if(pMethod.isAnnotationPresent(EventHandler.class))
		{
			eventMethods.add(pMethod);
		}
	}
	
	private void sortEvents()
	{
		for(int i = 0 ; i < eventMethods.size() ; i++)
		{
			for(int lKey = 0 ; i < eventMethods.size() ; lKey++)
			{
				if(eventMethods.get(lKey).getAnnotation(EventHandler.class).priority().value > eventMethods.get(lKey).getAnnotation(EventHandler.class).priority().value)
				{
					Method lMethod = eventMethods.get(0);
					eventMethods.set(0, eventMethods.get(1));
					eventMethods.set(1, lMethod);
				}
			}
		}
	}
	
	public void fireEvent(Event pEvent)
	{
		for(Method lMethod : eventMethods)
		{
			for(Class<?> lClass : lMethod.getParameterTypes())
			{
				if(lClass.equals(pEvent.getClass()))
				{
					if(!pEvent.isCancelled() || lMethod.getAnnotation(EventHandler.class).ignoreCancelled())
					{
						try
						{
							lMethod.invoke(lMethod.getClass().newInstance(), pEvent);
						}
						catch (Exception e)
						{
							e.printStackTrace();
						}
					}
				}
			}
		}
	}
}