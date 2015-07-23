package edu.emory.clir.hyperqa;

import edu.emory.clir.hyperqa.decomposition.FieldType;
import edu.emory.clir.hyperqa.decomposition.FieldsConfiguration;
import edu.emory.clir.hyperqa.index.ElasticSearchIndex;
import edu.emory.clir.hyperqa.index.Index;
import edu.emory.clir.hyperqa.parse.MicrosoftReader;
import edu.emory.clir.hyperqa.representation.DecomposedSentence;
import edu.emory.clir.hyperqa.representation.MicrosoftDocument;
import edu.emory.clir.hyperqa.representation.Sentence;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tomasz Jurczyk ({@code tomasz.jurczyk@emory.edu})
 * @since 1.0
 */
public class MicrosoftExperiment {
    static String filename = "mc160.test.";
    List<MicrosoftDocument> l_documents;
    FieldsConfiguration fieldsConfiguration;
    Index index = new ElasticSearchIndex();

    // Experiment settings
    int mode = 1; // 0 - train, 1 - test, 2 - train/test (see setting below)
    int N = 10;   // how many iterations for training/

    public MicrosoftExperiment()
    {
        l_documents = new ArrayList();
    }

    public void parseFile()
    {
        try
        {
            MicrosoftReader reader = new MicrosoftReader(filename);
            MicrosoftDocument d;

            while((d = reader.next()) != null)
            {
                l_documents.add(d);
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void experiment()
    {
        if (mode == 0)
        {

        }
        else if (mode == 1)
        {
            for (MicrosoftDocument document: l_documents)
            {
                indexDocument(document);
                MicrosoftAnswerer answerer = new MicrosoftAnswerer(document, fieldsConfiguration, index);
                System.out.println(answerer.test());
            }
        }
        else if (mode == 2)
        {
            for (int i = 0; i < N; i++)
            {
//                for (MicrosoftDocument document: l_documents)
//                {
//                    processDocument(document);
//                    trainModel(document);
//                }
//
//                for (MicrosoftDocument document: l_documents)
//                {
//                    processDocument(document);
//                    validateModel(document);
//                }
//
//                for (MicrosoftDocument document: l_documents)
//                {
//                    processDocument(document);
//                    testModel(document);
//                }
            }
        }
    }

    private void indexDocument(MicrosoftDocument document)
    {
        List<Sentence> sentences = document.get_sentences();

        for (int i = 0; i < sentences.size(); i++)
        {
            DecomposedSentence decomposedSentence = new DecomposedSentence(
                    sentences.get(i), fieldsConfiguration);
            System.out.println(decomposedSentence);
            index.index(decomposedSentence);
        }
    }


    private void trainModel(MicrosoftDocument document)
    {
    }

    private void validateModel(MicrosoftDocument document)
    {

    }

    private void testModel(MicrosoftDocument document)
    {

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
