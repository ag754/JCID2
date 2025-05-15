package cmd;

public class CmdLineArgs
{
	public CmdLineArgs(String[] args)
	{
		this.cfgFile = "jcid2.cfg";
		this.noZip = false;
		
		if (args != null && args.length != 0)
		{
			for (String arg : args)
			{
				process(arg);
			}
		}
	}
	
	private boolean isValid(String arg)
	{
		return arg.startsWith("--");
	}
	
	private void process(String arg)
	{
		if (!isValid(arg))
			return;
		
		if (!arg.contains("="))
		{
			String var = arg.substring(2);
			
			// Check for non-value setting arguments
			if (var.equalsIgnoreCase("noZip")) // --noZip
			{
				noZip = true;
			}
		}
		else
		{
			String var = arg.substring(2, arg.indexOf("="));
			String val = arg.substring(arg.indexOf("=") + 1);
			
			// Check for value setting arguments
			if (var.equalsIgnoreCase("cfgFile")) // --cfgFile=jcid2.cfg
			{
				cfgFile = val.replace("\\", "/");
			}
		}
	}
	
	public boolean doNotZip()
	{
		return noZip;
	}
	
	public String getConfigFile()
	{
		return cfgFile;
	}
	
	private boolean noZip;
	private String cfgFile;
}
