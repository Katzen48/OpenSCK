package de.katzen48.scsdk.event;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventManager
{
	private List<Method> eventMethods;
	private Map<Method,IListener> eventHandlers;


	public EventManager()
	{
		this.eventMethods = new ArrayList<Method>();
		this.eventHandlers = new HashMap<Method,IListener>();
	}


	public void registerEvents(IListener pListener)
	{
		for(Method lMethod : pListener.getClass().getMethods())
		{
			registerEvent(lMethod, pListener);
		}
		sortEvents();
	}

	public void registerEvent(Method pMethod, IListener pListener)
	{
		if(pMethod.isAnnotationPresent(EventHandler.class))
		{
			eventMethods.add(pMethod);
			eventHandlers.put(pMethod, pListener);
		}
	}

	private void sortEvents()
	{
		if(eventMethods.size() < 2) return;
		for(int i = 0 ; i < eventMethods.size() ; i++)
		{
			for(int lKey = 0 ; lKey < eventMethods.size() ; lKey++)
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
							lMethod.invoke(eventHandlers.get(lMethod), pEvent);
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
