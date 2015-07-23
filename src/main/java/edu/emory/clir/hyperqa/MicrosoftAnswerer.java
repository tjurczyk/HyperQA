package edu.emory.clir.hyperqa;

import edu.emory.clir.hyperqa.decomposition.FieldType;
import edu.emory.clir.hyperqa.decomposition.FieldsConfiguration;
import edu.emory.clir.hyperqa.index.Index;
import edu.emory.clir.hyperqa.representation.DecomposedSentence;
import edu.emory.clir.hyperqa.representation.MicrosoftDocument;
import edu.emory.clir.hyperqa.representation.Sentence;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Tomasz Jurczyk ({@code tomasz.jurczyk@emory.edu})
 * @since 1.0
 */
public class MicrosoftAnswerer {
    MicrosoftDocument document;
    FieldsConfiguration configuration;
    Index index;

    public MicrosoftAnswerer(MicrosoftDocument _d, FieldsConfiguration _f_conf, Index _index)
    {
        document = _d;
        configuration = _f_conf;
        index = _index;
    }

    /**
     * Make a test of all questions in the Microsoft Document.
     * @return the result String with the summary
     */
    public String test()
    {
        // Refresh the index
        index.refresh();

        List<Sentence> l_s = document.get_sentences();


        // For each question, decompose it and query the index to obtain scores
        for (Sentence q: document.get_questions())
        {
            DecomposedSentence ds = new DecomposedSentence(q, configuration);

            // Query for just the question
            Map<Integer,Double> m_question;
            Map<Integer,Map<Integer,Double>> m_questionanswer;
            Map<Integer,Double> m_answer;

            m_question = mergeResults(index.query(ds));
        }

        return null;
    }

    /**
     * Merge results from different fields into one map.
     * @param map Map of results from different fields with their results
     * @return    A merged map that contains a sum of hits across all fields
     */
    private Map<Integer,Double> mergeResults(Map<FieldType,Map<Integer,Double>> map)
    {
        Map<Integer,Double> m_merged = new HashMap();

        for (Map.Entry<FieldType,Map<Integer,Double>> entry: map.entrySet())
        {
            for (Map.Entry<Integer,Double> entry_field: entry.getValue().entrySet())
            {
                m_merged.merge(entry_field.getKey(), entry_field.getValue(), (v1, v2) -> v1 + v2);
            }
        }

        return m_merged;
    }
}
