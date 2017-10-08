# OpenSCK - Easy Server-Client Creation Kit

----------
## **Installation**
To use OpenSCK you need a dependency manager like [Maven](https://maven.apache.org/) or [Gradle](https://gradle.org/).
The downloading and updating process will be fully automaticly.
Just put the package into the configuration of your dependency manager.
### **Maven**
Just put this into your pom.xml:
```xml
<groupId>de.katzen48</groupId>
<artifactId>opensck</artifactId>
<version>latest</version>
  
<name>Katzen48-Repo</name>
<url>http://artifactory.katzen48.de/artifactory/public</url>
```
### **Gradle**
Just put this into your build.gradle:
```java
repositories {
	maven {
		url "http://artifactory.katzen48.de/artifactory/public"
	}
}

dependencies {
	compile group: 'de.katzen48', name: 'opensck', version: 'latest'
}
```
----------
## **Usage**
It's very easy to get started. Just create a server and a client and you're done. 
When you want more functions you can create custom events, eventhandlers and new message to be sent to other parts of your network.

### **Getting Started**

At first you need to create a Client:
```java
import de.katzen48.scsdk.client.Client;

public class SckClient extends Client
{
	public static SckClient instance; 
	
	public SckClient()
	{
		instance = this; //static instance to access attributes from the outside
		start("localhost", 12345); //connect your client to the server
		
		while(true) //loop to keep your client alive
		{
			
		}
	}
	
	public static void main(String[] pArgs)
	{
		new SckClient();
	}
}
```
and a Server:
```java
import de.katzen48.scsdk.server.Server;

public class SckServer extends Server
{
	public SckServer()
	{
		super(12345); //set the port for your server
		start(); //readyup yoour server for incoming connections
	}
	
	public static void main(String[] pArgs)
	{
		new SckServer();
	}
}
```
### **EventHandling**
Now you're ready to do some more advanced stuff.
The beginning part is, to create a custom eventhandler to print out a message, when a client connects to your server.
```java
import de.katzen48.scsdk.event.EventHandler;
import de.katzen48.scsdk.event.IListener;
import de.katzen48.scsdk.event.events.server.ClientConnectEvent;

public class ClientConnectedListener implements IListener
{
	@EventHandler
	public void onClientConnected(ClientConnectEvent pEvent)
	{
		System.out.println("Client conntected");
	}
}
```
You need to register every Listener to your application.
```java
getEventManager().registerEvents(new ClientConnectedListener());
```
At this point, everytime a client connects to your server
```java
Client connected
```
will be displayed in your console.
Not only the server also the client can register events but only for their side.
### **Messages**
If you want to send message from and to another client or the server, you need to get used to **Messages**.
A message is a simple class, which will be serialized and deserialized automaticly.
Just create a class implementing **IListener** with the following content:
```java
import de.katzen48.scsdk.network.ByteBuf;
import de.katzen48.scsdk.network.packet.IMessage;

public class TestPacket implements IMessage
{
	private String message; //your attributes

	
	public TestPacket() {} //this is important, don't miss it
	
	public TestPacket(String pMessage)
	{
		this.message = pMessage;
	}
	
	@Override
	public void toBytes(ByteBuf byteBuf) //serialization before networktransfer
	{
		byteBuf.setNextString(message);
	}
	
	@Override
	public void fromBytes(ByteBuf byteBuf) //deserialization after network transfer
	{
		this.message = byteBuf.getNextAsString();
	}
}
```
After you created your message, register it in your application:
```java
getNetworkDispatcher().registerPacket(TestPacket.class, (byte) 0);
```
The last argument is the channel you want to use.
The use of positive numbers is recommend because the negative numbered channels are reserved for internal operations.
**Important:** Register your messages on both ends. If you not, OpenSCK can't deserialize your messages.
### **Custom Events**
You can use custom events, if you want, but you have to fire them manually.
To create an event just create a class extending **Event**:
```java
import de.katzen48.scsdk.event.Event;

public class TestEvent extends Event
{
	private String message;
	
	public RemoteReadyEvent(String pMessage)
	{
		this.message = pMessage;
	}
	
	public String getMessage()
	{
		return message;
	}
}
```
and fire it:
```java
getEventManager().fireEvent(new TestEvent());
```
The EventManager will fire your event to all implementing EventListeners according to their registration order and EventPriority, which can be set as a parameter at the `@EventHandler` Annotation.
```java
@EventHandler(priority=EventPriority.LOWEST)
```
There are 6 priority levels, which are triggered in the following order:
```java
1. Monitor
2. HIGHEST
3. HIGH
4. NORMAL
5. LOW
6. LOWEST
```
If you cancel an event at a higher stage, the lower stages will not get fired, when they not explicit have `ignoreCancelled=true` at their EventHandler Annotation.
**Important:** Never ever cancel an Event at the Monitor Stage. It's just for (what the name says) Monitoring.

----------
## Supporters
Thanks going out to  **GhostZero** and **MansenC**, which were helping me with socket communication and multi threading.

----------
## License
Copyright (c) 2017 Katzen48

You are free to use this software for non-commercial purposes. If you plan to use it commercially, please inform me by mail at [admin@katzen48.de](mailto:admin@katzen48.de)