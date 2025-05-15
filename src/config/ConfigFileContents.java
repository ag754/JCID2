package config;

import java.util.*;

public class ConfigFileContents
{
	public ConfigFileContents()
	{
		macroMap = new HashMap<>();
		zipOutputs = new HashMap<>();
		crcOutputs = new HashMap<>();
		arcMap = new HashMap<>();
	}
	
	protected void addMacro(String filename, String macro)
	{
		macroMap.put(filename, macro);
	}
	protected void addZip(String directory, String destZip)
	{
		zipOutputs.put(directory, destZip);
	}
	protected void addCrc(String directory, String destCrc)
	{
		crcOutputs.put(directory, destCrc);
	}
	protected void addArc(String archive, String macro)
	{
		arcMap.put(archive, macro);
	}
	protected void setRootDir(String root)
	{
		this.rootDir = root;
	}
	protected void setResIdsDest(String resIdsDest)
	{
		this.resIdsDest = resIdsDest;
	}
	protected void setArcIdsDest(String arcIdsDest)
	{
		this.arcIdsDest = arcIdsDest;
	}
	
	public String getRootDir()
	{
		return rootDir;
	}
	public String getResIdsDest()
	{
		return resIdsDest;
	}
	public String getArcIdsDest()
	{
		return arcIdsDest;
	}
	public Map<String, String> getMacros()
	{
		return macroMap;
	}
	public Map<String, String> getArchiveMacros()
	{
		return arcMap;
	}
	public String getZipFilename(String directory)
	{
		return zipOutputs.get(directory);
	}
	public String getCrcFilename(String directory)
	{
		return crcOutputs.get(directory);
	}
	
	private String rootDir;
	private String resIdsDest;
	private String arcIdsDest;
	
	private Map<String, String> macroMap;
	private Map<String, String> zipOutputs;
	private Map<String, String> crcOutputs;
	private Map<String, String> arcMap;
}