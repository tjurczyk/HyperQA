package edu.emory.clir.hyperqa;

import edu.emory.clir.hyperqa.representation.Document;
import edu.emory.clir.hyperqa.representation.DecomposedSentence;
import edu.emory.clir.hyperqa.decomposition.FieldType;
import edu.emory.clir.hyperqa.decomposition.FieldsConfiguration;
import edu.emory.clir.hyperqa.index.ElasticSearchIndex;
import edu.emory.clir.hyperqa.index.Index;
import edu.emory.clir.hyperqa.parse.LineSeparatedReader;
import edu.emory.clir.hyperqa.representation.Sentence;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tomasz Jurczyk ({@code tomasz.jurczyk@emory.edu})
 * @since 1.0
 */
public class MicrosoftExperiment {
    static String filename = "mc160.train.";
    List<Document> l_documents;
    FieldsConfiguration fieldsConfiguration;
    Index index = new ElasticSearchIndex();

    public void parseFile()
    {
        try
        {
            LineSeparatedReader reader = new LineSeparatedReader(filename);
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
        for (Document document: l_documents)
        {
            processDocument(document);
        }
    }

    private void processDocument(Document document)
    {
        // Index all sentences
        List<Sentence> sentences = document.get_sentences();

        for (int i = 0; i < sentences.size() - 4; i++)
        {
            DecomposedSentence decomposedSentence = new DecomposedSentence(
                    sentences.get(i), fieldsConfiguration);
            System.out.println(decomposedSentence);
            index.index(decomposedSentence);
        }
    }

    private void prepareFieldConfiguration()
    {
        List<FieldType> fieldTypes = new ArrayList();

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
