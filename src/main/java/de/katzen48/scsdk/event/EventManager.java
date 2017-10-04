package de.katzen48.scsdk.event;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventManager
{
	private Map<IListener,List<Method>> eventHandlers;


	public EventManager()
	{
		this.eventHandlers = new HashMap<IListener,List<Method>>();
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
			if(!eventHandlers.containsKey(pListener))
			{
				eventHandlers.put(pListener, Arrays.asList(pMethod));
			}
			else
			{
				eventHandlers.get(pListener).add(pMethod);
			}
		}
	}

	private void sortEvents()
	{
		for(IListener lListener : eventHandlers.keySet())
		{
			List lEventMethods = eventHandlers.get(lListener);
			
			if(lEventMethods.size() < 2) return;
			for(int i = 0 ; i < lEventMethods.size() ; i++)
			{
				for(int lKey = 0 ; lKey < lEventMethods.size() ; lKey++)
				{
					if(((Method) lEventMethods.get(lKey)).getAnnotation(EventHandler.class).priority().value > ((Method) lEventMethods.get(lKey)).getAnnotation(EventHandler.class).priority().value)
					{
						Method lMethod = (Method) lEventMethods.get(0);
						lEventMethods.set(0, lEventMethods.get(1));
						lEventMethods.set(1, lMethod);
					}
				}
			}
		}
	}

	public void fireEvent(Event pEvent)
	{
		for(IListener lListener : eventHandlers.keySet())
		{
			List<Method> lEventMethods = eventHandlers.get(lListener);
			
			for(Method lMethod : lEventMethods)
			{
				for(Class<?> lClass : lMethod.getParameterTypes())
				{
					if(lClass.equals(pEvent.getClass()))
					{
						if(!pEvent.isCancelled() || lMethod.getAnnotation(EventHandler.class).ignoreCancelled())
						{
							try
							{
								lMethod.invoke(lListener, pEvent);
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
}
