# pengyifan-brat

A Java implementation of data structures and code to read/write Brat standoff format.

### Brat

(from [brat standoff format](http://brat.nlplab.org/standoff.html))

Annotations created in brat are stored on disk in a standoff format: annotations are stored separately from the annotated document text, which is never modified by the tool.

For each text document in the system, there is a corresponding annotation file. The two are associated by the file naming convention that their base name (file name without suffix) is the same: for example, the file `DOC-1000.ann` contains annotations for the file `DOC-1000.txt`.

Within the document, individual annotations are connected to specific spans of text through character offsets. For example, in a document beginning "Japan was today struck by ..." the text "Japan" is identified by the offset range 0..5. (All offsets all indexed from 0 and include the character at the start offset but exclude the character at the end offset.)

### Getting started

```XML
<repositories>
    <repository>
        <id>oss-sonatype</id>
        <name>oss-sonatype</name>
        <url>https://oss.sonatype.org/content/repositories/snapshots/</url>
        <snapshots>
            <enabled>true</enabled>
        </snapshots>
    </repository>
</repositories>
...
<dependency>
  <groupId>com.pengyifan.brat</groupId>
  <artifactId>pengyifan-brat</artifactId>
  <version>1.0.0-SNAPSHOT</version>
</dependency>
```

### Developers

* Yifan Peng (yfpeng@udel.edu)

### Webpage

The official Brat format webpage is available with all up-to-date instructions, code, and corpora in the Brat format, and other research on, based on and related to Brat. 

* [http://brat.nlplab.org/](http://brat.nlplab.org/)

A repository of biomedical corpora which uses Brat and BioC format

* [http://corpora.informatik.hu-berlin.de/](http://corpora.informatik.hu-berlin.de/)
