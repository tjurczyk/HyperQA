package edu.emory.clir.hyperqa.parse;

import edu.emory.clir.clearnlp.dependency.DEPTree;
import edu.emory.clir.clearnlp.reader.TSVReader;
import edu.emory.clir.hyperqa.representation.Document;
import edu.emory.clir.hyperqa.representation.Sentence;
import jdk.nashorn.internal.runtime.arrays.ArrayIndex;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Tomasz Jurczyk ({@code tomasz.jurczyk@emory.edu})
 * @since 1.0
 */
public class LineSeparatedReader extends AbstractReader{
    public LineSeparatedReader(String _filename)
            throws NoSuchMethodException, IOException
    {
        super(_filename);
    }

    public List<Document> readFile() throws IOException
    {
        List<Document> list = new ArrayList();
        int file_counter = 0;
        String filename = this.filename + file_counter++ + ".txt.cnlp";
        String s;
        TSVReader reader = new TSVReader(0,1,2,3,4,5,6);

        Document d = new Document();
        Sentence sentence;

        while(true)
        {
            DEPTree tree;
            System.out.println("Trying to open: " + filename);
            FileInputStream a;

            try
            {
                a = new FileInputStream(filename);
            }
            catch (FileNotFoundException e)
            {
                break;
            }
            reader.open(a);

            while ((tree = reader.next()) != null) {
                sentence = new Sentence(tree);
                d.add_sentence(sentence);
            }

            list.add(d);
            d = new Document();
            filename = this.filename + file_counter++ + ".txt.cnlp";
        }

        return list;
    }
}
