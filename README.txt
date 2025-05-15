Java Compression & Resource Identifier Utility 2
Coded by: Alexander Gibbons (C) 2018-2025

Java based tool that enables automatic compression of directory ZIP files and 
production of a list of CRC32 checksums that correspond to file entries within 
those folders; each value can be assigned a unique identifier that will then 
be written to macros within C++ compatible header files. 

Note that the checksums assigned to the macros are equivalent to those of the
entries (found at the end of the central directory within the produced directory 
ZIP file) for those individual files. 

This allows a C++ application to run JCID2 as a pre-build tool on whatever assets
need to be packaged and/or accessed at runtime after compilation. The intended use
of the CRC32 checksums is to enable targeted INFLATE procedures which only seek
to grab a particularly desired file that is DEFLATE'd within a ZIP file, rather 
than having to expand the whole thing and waste memory when no other files are 
needed.
