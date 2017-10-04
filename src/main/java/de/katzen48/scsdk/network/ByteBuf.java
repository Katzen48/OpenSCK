package de.katzen48.scsdk.network;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ByteBuf
{
	private List<byte[]> bytes;
	private int currentIndex;
	
	
	public ByteBuf(byte[][] pBytes)
	{
		this.bytes = Arrays.asList(pBytes);
		this.currentIndex = 0;
	}
	
	protected ByteBuf() {}
	
	
	public String getNextAsString()
	{
		try
		{
			return new String(getNextBytes(), "UTF-8");
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
		return "";
	}
	
	public float getNextAsFloat()
	{
		return Float.valueOf(getNextAsString());
	}
	
	public double getNextAsDouble()
	{
		return Double.valueOf(getNextAsString());
	}
	
	public int getNextAsInteger()
	{
		return Integer.valueOf(getNextAsString());
	}
	
	public boolean getNextAsBoolean()
	{
		return Boolean.valueOf(getNextAsString());
	}
	
	public byte[] getNextBytes()
	{
		byte[] lBytes = bytes.get(currentIndex);
		currentIndex++;
		return lBytes;
	}
	
	public void setNextDouble(double pDouble)
	{
		setNextString(String.valueOf(pDouble));
	}
	
	public void setNextFloat(float pFloat)
	{
		setNextString(String.valueOf(pFloat));
	}
	
	public void setNextInteger(int pInteger)
	{
		setNextString(String.valueOf(pInteger));
	}
	
	public void setNextBoolean(boolean pBoolean)
	{
		setNextString(String.valueOf(pBoolean));
	}
	
	public void setNextString(String pString)
	{
		
		setNextBytes(pString.getBytes());
	}
	
	public void setNextBytes(byte[]pBytes)
	{
		if(bytes == null) bytes = new ArrayList<byte[]>();
		bytes.add(pBytes);
		currentIndex++;
	}
	
	public void addByteBuf(ByteBuf pByteBuf)
	{
		bytes.addAll(Arrays.asList(pByteBuf.getBytes()));
	}
	
	public byte[][] getBytes()
	{
		return bytes.toArray(new byte[bytes.size()][]);
	}
}
