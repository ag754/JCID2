package entry;

import cmd.CmdLineArgs;
import config.*;
import cpp.*;
import dir.*;
import java.io.*;

public class Main
{
	public static void main(String args[])
	{
		long start = System.currentTimeMillis();
		
		System.out.println("Java Compression & Resource Identifier Utility 2 (JCID2)");
		System.out.println("Alex Gibbons (C) 2018");
		
		CmdLineArgs flags = new CmdLineArgs(args);
		
		ConfigFileReader reader = new ConfigFileReader(flags.getConfigFile());
		reader.read();
		ConfigFileContents contents = reader.getContents();
		
		CrcHeaderWriter resIdHeader = new CrcHeaderWriter(contents.getResIdsDest());
		resIdHeader.setMacros(contents.getMacros());
		
		CrcHeaderWriter arcIdHeader = new CrcHeaderWriter(contents.getArcIdsDest());
		arcIdHeader.setMacros(contents.getArchiveMacros());
		
		File root = new File(contents.getRootDir());
		for (File subdir : root.listFiles(File::isDirectory))
		{
			String directoryPath = contents.getRootDir() + subdir.getName() + "/";
			
			if (!flags.doNotZip())
			{
				// Zip the directory (if needed)
				String zipDest = contents.getRootDir() + contents.getZipFilename(subdir.getName() + "/");
				DirectoryTreeExplorer zipper = new DirectoryZipper(directoryPath, zipDest);
				zipper.transverse();
			}
			
			// Write the CRC32 File
			String crcDest = contents.getRootDir() + contents.getCrcFilename(subdir.getName() + "/");
			CrcFileWriter crcer = new CrcFileWriter(directoryPath, crcDest);
			crcer.transverse();
			
			// Update the headers with the new CRC info
			resIdHeader.addCrcs(crcer.dumpCrcs());
			arcIdHeader.addCrc(crcer.getDirectoryCrc());
		}
		
		// Write the header files
		resIdHeader.write();
		arcIdHeader.write();
		
		System.out.println("Done. (Elapsed Time: " + (System.currentTimeMillis() - start) + " ms)");
	}
}