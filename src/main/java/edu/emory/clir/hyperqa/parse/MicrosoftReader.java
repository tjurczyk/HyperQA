package edu.emory.clir.hyperqa.parse;

import edu.emory.clir.clearnlp.dependency.DEPTree;
import edu.emory.clir.clearnlp.reader.TSVReader;
import edu.emory.clir.hyperqa.MicrosoftExperiment;
import edu.emory.clir.hyperqa.representation.Document;
import edu.emory.clir.hyperqa.representation.MicrosoftDocument;
import edu.emory.clir.hyperqa.representation.Sentence;
import edu.emory.clir.hyperqa.util.SentenceUtils;
import jdk.nashorn.internal.runtime.arrays.ArrayIndex;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Tomasz Jurczyk ({@code tomasz.jurczyk@emory.edu})
 * @since 1.0
 */
public class MicrosoftReader extends AbstractReader{
    public static final int NUMBER_OF_QUESTIONS = 4;
    public static final int NUMBER_OF_ANSWERS   = 4;

    public MicrosoftReader(String _filename)
            throws NoSuchMethodException, IOException
    {
        super(_filename);
    }

    public List<Document> readFile() throws IOException
    {
        List<Document> list = new ArrayList();
        int file_counter = 0;
        String filename = this.filename + file_counter++ + ".txt.cnlp";
        TSVReader reader = new TSVReader(0,1,2,3,4,5,6);

        MicrosoftDocument d = new MicrosoftDocument();
        Sentence sentence;

        while(true)
        {
            DEPTree tree;
            FileInputStream a;

            try
            {
                a = new FileInputStream(filename);
            }
            catch (FileNotFoundException e)
            {
                if (list.isEmpty()) throw new FileNotFoundException();
                else break;
            }
            reader.open(a);
            int sentenceID = 0;

            while ((tree = reader.next()) != null) {
                if (SentenceUtils.isQuestion(tree))
                {
                    // Read 4 questions + 4 possible answers + 4 correct answers
                    for (int i = 0; i < NUMBER_OF_QUESTIONS; i++)
                    {
                        if (i > 0) tree = reader.next();

                        // Add the question
                        d.addQuestion(new Sentence(tree), i);

                        // Add 4 possible answers
                        for (int j = 0; j < NUMBER_OF_ANSWERS; j++)
                        {
                            tree = reader.next();
                            d.addAnswer(i, j, new Sentence(tree));
                        }

                        // Add the correct answer
                        tree = reader.next();
                        d.addCorrectAnswer(SentenceUtils.getFirstIntOccurrence(tree), i);
                    }
                }
                else
                {
                    sentence = new Sentence(tree, sentenceID++);
                    d.add_sentence(sentence);
                }
            }

            list.add(d);
            d = new MicrosoftDocument();
            filename = this.filename + file_counter++ + ".txt.cnlp";
        }

        return list;
    }
}
