package edu.emory.clir.hyperqa.parse;

import java.io.InputStream;

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
