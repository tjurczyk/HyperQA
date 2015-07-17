package edu.emory.clir.hyperqa.parse;

import edu.emory.clir.hyperqa.representation.Document;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author Tomasz Jurczyk ({@code tomasz.jurczyk@emory.edu})
 * @since 1.0
 */
public abstract class AbstractReader {
    protected String filename;

    public AbstractReader(String _filename)
            throws IOException
    {
        filename = _filename;
    }

    public abstract List<Document> readFile() throws IOException;
}
