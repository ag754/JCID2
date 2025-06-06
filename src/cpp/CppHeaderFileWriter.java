package cpp;

import java.io.*;

public class CppHeaderFileWriter
{
    public CppHeaderFileWriter(String destFile)
    {
    	this.filename = destFile;
    	
        constructWriter(destFile);
        extractMacro(destFile);
    }

    private void constructWriter(String destFile)
    {
        try
        {
        	File f = new File(destFile);
        	if (!f.exists())
	        {
	            File parentDir = new File(f.getParent());
	            parentDir.mkdirs();
	            f.createNewFile();
	        }
            writer = new FileWriter(f);
        }
        catch (IOException e)
        {
            System.err.println("Cannot create writer for .h file: " + destFile);
            e.printStackTrace();
        }
    }

    private void extractMacro(String filename)
    {
    	int lastSlash = filename.lastIndexOf("/");
    	if (lastSlash != -1)
	    {
	        filename = filename.substring(lastSlash + 1);
	    }
	    
    	macro = filename.replaceAll("\\.", "_").toUpperCase();
        if (!macro.endsWith("_H"))
        {
            macro += "_H";
        }
        macro += "_";       // end of macro: _H_
    }

    private void writeStartMacroGuard()
    {
        try
        {
            writer.write("#ifndef " + macro + System.lineSeparator());
            writer.write("#define " + macro + System.lineSeparator() + System.lineSeparator());
        }
        catch (IOException e)
        {
            System.err.println("Could not write top of macro guard " + macro);
            e.printStackTrace();
        }
    }

    private void writeComment()
    {
        try
        {
            writer.write("/*" + System.lineSeparator() +
                             " * This file was generated by the JCID2 utility." + System.lineSeparator() +
                             " * Do not alter by hand. To make a change, adjust the" + System.lineSeparator() +
                             " * corresponding entry in the configuration file." + System.lineSeparator() +
                             " */" + System.lineSeparator() + System.lineSeparator());
        }
        catch (IOException e)
        {
            System.err.println("Could not write comment for " + filename);
            e.printStackTrace();
        }
    }

    private void writeEndMacroGuard()
    {
        try
        {
            writer.write(System.lineSeparator() + "#endif // " + macro);
        }
        catch (IOException e)
        {
            System.err.println("Could not write bottom of macro guard " + macro);
            e.printStackTrace();
        }
    }

    public void write(String contents)
    {
    	System.out.println("Writing C++ header " + filename);
    	
        this.writeStartMacroGuard();
        this.writeComment();
        try
        {
            writer.write(contents);
        }
        catch (IOException e)
        {
            System.err.println("Could not write contents to header " + macro);
            e.printStackTrace();
        }
        this.writeEndMacroGuard();
        this.cleanup();
    }

    public void cleanup()
    {
        try
        {
            writer.flush();
            writer.close();
        }
        catch (IOException e)
        {
            System.err.println("Could not cleanup " + filename + "'s writer properly.");
            e.printStackTrace();
        }
    }

    private FileWriter writer;

    /**
     * Filename WITHOUT the trailing '.h'
     * foo.h --> FOO_H_
     */
    private String macro;
    private String filename;
}