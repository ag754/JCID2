package cpp;

import java.util.*;

public class CrcHeaderWriter
{
	public CrcHeaderWriter(String destFile)
	{
		this.writer = new CppHeaderFileWriter(destFile);
		this.crcs = new HashMap<>();
		this.macros = new HashMap<>();
	}
	
	public void addCrcs(Map<String, Long> crcs)
	{
		for (Map.Entry<String, Long> e : crcs.entrySet())
		{
			this.crcs.put(e.getKey(), e.getValue());
		}
	}
	public void addCrc(CrcPair crc)
	{
		crcs.put(crc.getKey(), crc.getValue());
	}
	public void setMacros(Map<String, String> macros)
	{
		this.macros = macros;
	}
	
	public void write()
	{
		if (crcs.isEmpty() || macros.isEmpty())
			return;
		
		StringBuilder str = new StringBuilder();
		for (Map.Entry<String, Long> entry : crcs.entrySet())
		{
			str.append("#define " + macros.get(entry.getKey()) + " 0x" + Long.toHexString(entry.getValue()) + System.lineSeparator());
		}
		writer.write(str.toString());
	}
	
	private CppHeaderFileWriter writer;
	
	private Map<String, Long> crcs;
	private Map<String, String> macros;
}