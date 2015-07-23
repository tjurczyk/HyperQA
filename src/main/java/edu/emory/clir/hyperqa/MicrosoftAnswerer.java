package edu.emory.clir.hyperqa;

import edu.emory.clir.hyperqa.decomposition.FieldType;
import edu.emory.clir.hyperqa.decomposition.FieldsConfiguration;
import edu.emory.clir.hyperqa.index.Index;
import edu.emory.clir.hyperqa.representation.DecomposedSentence;
import edu.emory.clir.hyperqa.representation.MicrosoftDocument;
import edu.emory.clir.hyperqa.representation.Sentence;

import java.util.*;

/**
 * @author Tomasz Jurczyk ({@code tomasz.jurczyk@emory.edu})
 * @since 1.0
 */
public class MicrosoftAnswerer {
    private static final int NUMBER_OF_QUESTIONS = 4;
    private static final int NUMBER_OF_ANSWERS = 4;
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
        int q_index = 0;

        // For each question, decompose it and query the index to obtain scores
        for (Sentence q: document.get_questions())
        {
            DecomposedSentence ds = new DecomposedSentence(q, configuration);

            // Query for just the question
            Map<Integer,Double> m_question;
            Map<Integer,Map<Integer,Double>> m_questionanswer = new HashMap();
            Map<Integer,Map<Integer,Double>> m_answer         = new HashMap();

            Map<FieldType,Map<Integer,Double>> m = index.query(ds);
            System.out.println("Question: " + m);


            // Collect and merge results for questions
            m_question = mergeResults(index.query(ds));

            System.out.println("Question after merge: " + m_question);
            System.out.println("Question after top 5: " + getTopN(m_question, 5));

            // Collect and merge results for question+answer (each)
            for (int i = 0; i < NUMBER_OF_ANSWERS; i++)
            {
                Sentence jointSentence = q.concatenate(document.getAnswers(q_index, i));
                ds = new DecomposedSentence(jointSentence, configuration);

                m_questionanswer.put(i, mergeResults(index.query(ds)));
            }

            // Collect and merge results for answers
            for (int i = 0; i < NUMBER_OF_ANSWERS; i++)
            {
                Sentence answer = document.getAnswers(q_index, i);
                ds = new DecomposedSentence(answer, configuration);

                m_answer.put(i, mergeResults(index.query(ds)));
            }

            q_index++;
            System.exit(0);
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

    /**
     * Returns a map (LinkedHashMap) of top N entries in map
     * @param map Map to be get top N from
     * @param N   N top results to get
     * @return    A LinkedHashMap object with top N results
     */
    private Map<Integer,Double> getTopN(Map<Integer,Double> map, int N)
    {
        Map<Integer,Double> sortedMap = sortByValue(map);
        Map<Integer,Double> new_map = new LinkedHashMap();

        int i = 0;
        for (Map.Entry<Integer,Double> entry: sortedMap.entrySet())
        {
            if (i++ < N)
            {
                new_map.put(entry.getKey(), entry.getValue());
            }
        }

        return new_map;
    }

    public Map sortByValue(Map unsortedMap) {
        Map sortedMap = new TreeMap(new ValueComparator(unsortedMap));
        sortedMap.putAll(unsortedMap);
        return sortedMap;
    }

    class ValueComparator implements Comparator {

        Map map;

        public ValueComparator(Map map) {
            this.map = map;
        }

        public int compare(Object keyA, Object keyB) {
            Comparable valueA = (Comparable) map.get(keyA);
            Comparable valueB = (Comparable) map.get(keyB);
            return valueB.compareTo(valueA);
        }
    }
}
