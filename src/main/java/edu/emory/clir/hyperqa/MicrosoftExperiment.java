package edu.emory.clir.hyperqa;

import edu.emory.clir.hyperqa.representation.Document;
import edu.emory.clir.hyperqa.representation.DecomposedSentence;
import edu.emory.clir.hyperqa.decomposition.FieldType;
import edu.emory.clir.hyperqa.decomposition.FieldsConfiguration;
import edu.emory.clir.hyperqa.index.ElasticSearchIndex;
import edu.emory.clir.hyperqa.index.Index;
import edu.emory.clir.hyperqa.parse.MicrosoftReader;
import edu.emory.clir.hyperqa.representation.Sentence;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tomasz Jurczyk ({@code tomasz.jurczyk@emory.edu})
 * @since 1.0
 */
public class MicrosoftExperiment {
    static String filename = "mc160.test.";
    List<Document> l_documents;
    FieldsConfiguration fieldsConfiguration;
    Index index = new ElasticSearchIndex();

    // Experiment settings
    int mode = 1; // 0 - train, 1 - test, 2 - train/test (see setting below)
    int N = 10;   // how many iterations for training/

    public void parseFile()
    {
        try
        {
            MicrosoftReader reader = new MicrosoftReader(filename);
            l_documents = reader.readFile();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void experiment()
    {
        if (mode == 2)
        {
            for (int i = 0; i < N; i++)
            {
                for (Document document: l_documents)
                {
                    processDocument(document);
                    trainModel(document);
                }

                for (Document document: l_documents)
                {
                    processDocument(document);
                    validateModel(document);
                }

                for (Document document: l_documents)
                {
                    processDocument(document);
                    testModel(document);
                }
            }
        }
        else
        {
            for (Document document: l_documents)
            {
                processDocument(document);
            }
        }

    }

    private void processDocument(Document document)
    {
        // Index all sentences
        List<Sentence> sentences = document.get_sentences();

        for (int i = 0; i < sentences.size(); i++)
        {
            DecomposedSentence decomposedSentence = new DecomposedSentence(
                    sentences.get(i), fieldsConfiguration);
            System.out.println(decomposedSentence);
            index.index(decomposedSentence);
        }

        // Questions time
        if (mode == 0)
        {
            trainModel(document);
        }
        else if (mode == 1)
        {
            testModel(document);
        }

    }

    private void trainModel(Document document)
    {

    }

    private void validateModel(Document document)
    {

    }

    private void testModel(Document document)
    {
        Sentence question;


        // Collect each question and all answers

    }

    private void prepareFieldConfiguration()
    {
        List<FieldType> fieldTypes = new ArrayList();

        fieldTypes.add(FieldType.ID);
        fieldTypes.add(FieldType.TEXT);
        fieldTypes.add(FieldType.LEMMA_TEXT);

        fieldsConfiguration = new FieldsConfiguration(fieldTypes);
    }

    public static void main(String [] args)
    {
        MicrosoftExperiment exp = new MicrosoftExperiment();
        exp.prepareFieldConfiguration();
        exp.parseFile();
        exp.experiment();
    }
}
