package dir;

import java.io.*;
import java.util.zip.*;

public class DirectoryZipper extends DirectoryTreeExplorer
{
    public DirectoryZipper(String directory, String destFile)
    {
    	super(directory);
    	
        this.destZipFilename = destFile;
        try
        {
            this.zipOut = new ZipOutputStream(new FileOutputStream(destFile));
        }
        catch (IOException e)
        {
            System.err.println("Failed to create Zip IO handle for " + directory);
            e.printStackTrace();
        }
    }
    
    protected void onEntry()
    {
    	// Set up the ZipOutputStream before we transverse
	    zipOut.setMethod(ZipOutputStream.DEFLATED);
	    zipOut.setLevel(9);
	
	    System.out.println("Zipping contents of " + rootDirectory + " to " + destZipFilename);
    }
    
    protected void onEnterSubDirectory(String directory)
    {
	    System.out.println("Handling directory " + directory);
	    try
	    {
		    ZipEntry entry = new ZipEntry(directory);
		    zipOut.putNextEntry(entry); // Open a new directory within the Zip
	    }
	    catch (IOException e)
	    {
		    System.err.println("Error adding directory entry for " + rootDirectory);
		    e.printStackTrace();
	    }
    }
    
    protected void onExitSubDirectory(String directory)
    {
	    try
	    {
		    zipOut.closeEntry(); // Close out the directory
	    }
	    catch (IOException e)
	    {
		    System.err.println("Could not close directory entry for " + directory);
		    e.printStackTrace();
	    }
    }
    
    protected void onFile(File f, String directory)
    {
	    if (f == null)
		    return;
	
	    try
	    {
		    ZipEntry entry = new ZipEntry(directory + f.getName());
		    zipOut.putNextEntry(entry);
		
		    FileInputStream in = new FileInputStream(f);
		    byte data[] = new byte[(int) f.length()];
		    int c;
		
		    while ((c = in.read(data)) > 0)
			    zipOut.write(data, 0, c);
		
		    in.close();
		    zipOut.closeEntry();
		    
		    System.out.println("Compressed file " + f.getName());
	    }
	    catch (IOException e)
	    {
		    System.err.println("Error zipping file " + f.getName() + " for " + rootDirectory);
		    e.printStackTrace();
	    }
    }
    
    protected void onExit()
    {
    	System.out.println("Zipping complete.");
        try
        {
        	zipOut.flush();
            zipOut.close();
        }
        catch (IOException e)
        {
            System.err.println("Error cleaning up ZipOutputStream for " + rootDirectory);
            e.printStackTrace();
        }
    }
    
    private ZipOutputStream zipOut;
    private String destZipFilename;
}