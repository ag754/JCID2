package dir;

import java.io.*;
import java.util.zip.ZipEntry;

public abstract class DirectoryTreeExplorer
{
	public DirectoryTreeExplorer(String directory)
	{
		if (directory == null)
		{
			rootDirectory = "./";
		}
		else
		{
			directory = directory.replace("\\", "/");
			this.rootDirectory = directory.endsWith("/") ? directory : directory + "/";
		}
		
		try
		{
			this.dir = new File(rootDirectory);
		}
		catch (SecurityException e)
		{
			System.err.println("Error grabbing File handle to directory " + rootDirectory);
			e.printStackTrace();
		}
		
		this.currWorkDir = "";
	}
	
	public void transverse()
	{
		onEntry();
		for (File f : dir.listFiles())
		{
			if (f.isDirectory())
				handleSubdirectory(f.getName());
			else
				handleFile(f);
		}
		onExit();
	}
	
	private void handleSubdirectory(String subPath)
	{
		if (subPath == null)
			return;
		
		currWorkDir += subPath + "/";
		File subdir = new File(rootDirectory + currWorkDir);
		
		onEnterSubDirectory(currWorkDir);
		
		for (File f : subdir.listFiles())
		{
			if (f.isDirectory())
			{
				handleSubdirectory(f.getName());
			}
			else // File
			{
				handleFile(f);
			}
		}
		
		onExitSubDirectory(currWorkDir);
		currWorkDir = "";
	}
	
	private void handleFile(File f)
	{
		onFile(f, currWorkDir);
	}
	
	protected abstract void onEntry();
	protected abstract void onEnterSubDirectory(String directory);
	protected abstract void onExitSubDirectory(String directory);
	protected abstract void onFile(File f, String directory);
	protected abstract void onExit();
	
	protected final String rootDirectory;
	private File dir;
	private String currWorkDir;
}
