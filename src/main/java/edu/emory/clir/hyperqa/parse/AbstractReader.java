package edu.emory.clir.hyperqa.parse;

import edu.emory.clir.clearnlp.util.IOUtils;
import edu.emory.clir.hyperqa.representation.Document;
import edu.emory.clir.hyperqa.representation.MicrosoftDocument;

import java.io.*;
import java.util.Iterator;
import java.util.List;

/**
 * @author Tomasz Jurczyk ({@code tomasz.jurczyk@emory.edu})
 * @since 1.0
 */
abstract public class AbstractReader<T>{
    protected InputStream    f_in;

    public AbstractReader(InputStream in)
    {
        open(in);
    }

    public AbstractReader()
    {
    }

    abstract public T next();

    public void open(InputStream in)
    {
        f_in = in;
    }
}
