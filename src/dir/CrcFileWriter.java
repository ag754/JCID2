package dir;

import java.io.*;
import java.nio.file.*;
import java.util.zip.*;
import java.util.*;
import cpp.CrcPair;

public class CrcFileWriter extends DirectoryTreeExplorer
{
	public CrcFileWriter(String directory, String destFile)
	{
		super(directory);
		this.destFile = destFile;
		this.crcs = new HashMap<>();
		
		try
		{
			File f = new File(destFile);
			if (!f.exists())
			{
				File parentDir = new File(f.getParent());
				parentDir.mkdirs();
				f.createNewFile();
			}
			this.writer = new FileWriter(f);
		} catch (IOException e)
		{
			System.err.println("Could not create writer for CRC file " + destFile);
			e.printStackTrace();
		}
	}
	
	@Override
	protected void onEntry()
	{
		System.out.println("Writing CRCs for " + rootDirectory + " to " + destFile);
	}
	
	@Override
	protected void onEnterSubDirectory(String directory)
	{
	
	}
	
	@Override
	protected void onExitSubDirectory(String directory)
	{
	
	}
	
	@Override
	protected void onFile(File f, String directory)
	{
		Checksum crc = new CRC32();
		
		byte[] b = readBytes(f);
		if (b != null)
		{
			crc.update(b, 0, b.length);
		}
		
		writeCrcEntry(directory + f.getName(), crc, b.length);
	}
	
	private byte[] readBytes(File f)
	{
		try
		{
			byte data[] = Files.readAllBytes(Paths.get(f.toURI()));
			return data;
		} catch (IOException e)
		{
			System.err.println("Error reading file " + f.getName());
			e.printStackTrace();
		}
		return null;
	}
	
	private void writeCrcEntry(String filename, Checksum crc, long filesize)
	{
		try
		{
			// Because this is being fed into the C++ Cache system
			// we need to format the CRCs as:
			// @file <filename> <CRC32> <filesize>
			writer.write("@file " + filename + " " + String.format("0x%08x", crc.getValue()) + " " + filesize + System.lineSeparator());
			crcs.put(filename, crc.getValue());
			System.out.println("Processed file " + filename + " (CRC: " + String.format("0x%08x", crc.getValue()) + ")");
		} catch (IOException e)
		{
			System.err.println("Error writing CRC for " + filename);
			e.printStackTrace();
		}
	}
	
	@Override
	protected void onExit()
	{
		System.out.println("CRCs written.");
		try
		{
			writer.flush();
			writer.close();
		} catch (IOException e)
		{
			System.err.println("Could not cleanup writer to " + destFile);
			e.printStackTrace();
		}
	}
	
	public Map<String, Long> dumpCrcs()
	{
		return crcs;
	}
	
	public CrcPair getDirectoryCrc()
	{
		// Compute CRC
		Checksum dirCrc = new CRC32();
		byte[] b = rootDirectory.getBytes();
		dirCrc.update(b, 0, b.length);
		
		// Trim to only lowest directory name with trailing slash
		String dirName = rootDirectory;
		if (dirName.endsWith("/"))
		{
			dirName = dirName.substring(0, dirName.length() - 1);
		}
		int lastSlash = dirName.lastIndexOf("/");
		if (lastSlash != -1)
		{
			dirName = dirName.substring(lastSlash + 1);
		}
		dirName += "/";
		
		return new CrcPair(dirName, dirCrc.getValue());
	}
	
	private FileWriter writer;
	private String destFile;
	private Map<String, Long> crcs;
}