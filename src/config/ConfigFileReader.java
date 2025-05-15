package config;

import java.io.*;

public class ConfigFileReader
{
	public ConfigFileReader(String cfgFile)
	{
		this.cfgFile = cfgFile;
		this.contents = new ConfigFileContents();
		try
		{
			this.reader = new BufferedReader(new FileReader(new File(cfgFile)));
		}
		catch (IOException e)
		{
			System.err.println("Could not create reader for config file: " + cfgFile);
			e.printStackTrace();
		}
	}

	public void read()
	{
		System.out.println("Reading config file: " + cfgFile);
		try
		{
			String line;
			while ((line = reader.readLine()) != null)
			{
				parse(line);
			}
			System.out.println("Config file loaded.");
		}
		catch (IOException e)
		{
			System.err.println("Error reading in contents of " + cfgFile);
			e.printStackTrace();
		}
	}
	
	public ConfigFileContents getContents()
	{
		return contents;
	}
	
	private void parse(String line)
	{
		if (line == null || line.startsWith("#") || line.isEmpty())
			return; // EOF, comment, or whitespace
		
		String operation = line.substring(0, line.indexOf("{")).trim();
		String lhs = line.substring(line.indexOf("{") + 1, line.indexOf(",")).trim();
		String rhs = line.substring(line.indexOf(",") + 1, line.indexOf("}")).trim();
		
		if (operation.equalsIgnoreCase("env"))
		{
			processEnv(lhs, rhs);
		}
		else if (operation.equalsIgnoreCase("zip"))
		{
			processZip(lhs, rhs);
		}
		else if (operation.equalsIgnoreCase("crc"))
		{
			processCrc(lhs, rhs);
		}
		else if (operation.equalsIgnoreCase("arc"))
		{
			processArc(lhs, rhs);
		}
		else if (operation.equalsIgnoreCase("macro"))
		{
			processMacro(lhs, rhs);
		}
		else
		{
			System.err.println("Invalid line found in " + cfgFile + ":\n>>" + line);
		}
	}
	
	private void processEnv(String var, String val)
	{
		if (var.equalsIgnoreCase("root"))
		{
			contents.setRootDir(val);
		}
		else if (var.equalsIgnoreCase("resIdsDest"))
		{
			contents.setResIdsDest(val);
		}
		else if (var.equalsIgnoreCase("arcIdsDest"))
		{
			contents.setArcIdsDest(val);
		}
	}
	private void processZip(String dir, String dest)
	{
		contents.addZip(dir, dest);
	}
	private void processCrc(String dir, String dest)
	{
		contents.addCrc(dir, dest);
	}
	private void processArc(String arc, String macro)
	{
		contents.addArc(arc, macro);
	}
	private void processMacro(String file, String macro)
	{
		contents.addMacro(file, macro);
	}
	
	private BufferedReader reader;
	private String cfgFile;
	private ConfigFileContents contents;
}