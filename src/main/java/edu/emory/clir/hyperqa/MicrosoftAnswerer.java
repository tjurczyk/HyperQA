package edu.emory.clir.hyperqa;

import edu.emory.clir.hyperqa.decomposition.FieldType;
import edu.emory.clir.hyperqa.decomposition.FieldsConfiguration;
import edu.emory.clir.hyperqa.index.Index;
import edu.emory.clir.hyperqa.representation.DecomposedSentence;
import edu.emory.clir.hyperqa.representation.Document;
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
     *
     * @return the result score (double)
     */
    public double test()
    {
        // Refresh the index
        index.refresh();

        // Question id
        int q_index = 0;

        // Overall score of the test for this story
        double score_overall = 0;

        // Decompose each question and query the index to get scores
        for (Sentence q: document.get_questions())
        {
            DecomposedSentence ds = new DecomposedSentence(q, configuration);

            // Map for the question results
            Map<Integer,Double> m_question;

            // Maps for the answer results
            Map<Integer,Map<Integer,Double>> m_answer    = new HashMap();
            Map<Integer,Map<Integer,Double>> m_topanswer = new HashMap();

            // Maps for the question-answer results
            Map<Integer,Map<Integer,Double>> m_questionanswer    = new HashMap<>();
            Map<Integer,Map<Integer,Double>> m_topquestionanswer = new HashMap<>();

            //Map<FieldType,Map<Integer,Double>> m = index.query(ds);
            //System.out.println("Question: " + m);

            // Collect and merge results for questions
            m_question = mergeResults(index.query(ds));

            System.out.println("Question after merge: " + m_question);

            // Select top question hits
            m_question = getTopN(m_question, 5);
            System.out.println("Question after top 5: " + m_question);

            // Collect and merge results for answers
            for (int i = 0; i < NUMBER_OF_ANSWERS; i++)
            {
                Sentence answer = document.getAnswers(q_index, i);
                ds = new DecomposedSentence(answer, configuration);

                m_answer.put(i, mergeResults(index.query(ds)));
                System.out.println("Answer id: " + i + ", results: " + m_answer.get(i));
            }

            // Collect and merge results for question-answers
            for (int i = 0; i < NUMBER_OF_ANSWERS; i++)
            {
                Sentence joint = q.concatenate(document.getAnswers(q_index, i));
                DecomposedSentence d_joint = new DecomposedSentence(joint, configuration);

                m_questionanswer.put(i, mergeResults(index.query(d_joint)));
                System.out.println("Answer id: " + i + ", question-answer results: " + m_questionanswer.get(i));
            }

            // Select top answer hits
            for (int i = 0; i < NUMBER_OF_ANSWERS; i++)
            {
                m_topanswer.put(i, getTopN(m_answer.get(i), 5));
            }

            System.out.println("m_answer = " + m_answer);
            System.out.println("m_topanswer = " + m_topanswer);

            // For each question-selected seed, calculate best likelihood for each answer
            Map<Integer,Map<Integer,Double>> results = new HashMap<>();

            // For each selected top question
            for (Map.Entry<Integer,Double> entry: m_question.entrySet())
            {
                // For each selected top answer
                Map<Integer,Double> questionResultsMap = new HashMap<>();
                for (int j = 0; j < 4; j++)
                {
                    double likelihood = calculateHighestLikelihood
                            (entry.getKey(), entry.getValue(), m_topanswer.get(j), m_questionanswer.get(j));
                    questionResultsMap.put(j, likelihood);
                    System.out.println("For key = " + entry.getKey() + " max likelihood = " + likelihood);
                }

                results.put(entry.getKey(), questionResultsMap);
            }

            // Calculate score and add to the score_overall
            double score = calculateScore(results, document.getCorrectAnswer(q_index));
            score_overall += score;

            q_index++;
        }

        return score_overall;
    }

    /**
     * Calculate score of an answer for question.
     *
     * @param map            a map of question-seed and answer scores
     * @param correct_answer a correct answer id
     * @return a calculated score for an answer for a question
     */
    private double calculateScore(Map<Integer,Map<Integer,Double>> map, int correct_answer)
    {
        double max_likelihood_value       = -1;
        int    max_likelihood_question_id = -1;

        // Find the maximum value and index of question-answer
        // For each question entry
        for (Map.Entry<Integer,Map<Integer,Double>> entry: map.entrySet())
        {
            // For each answer entry
            for (int i = 0; i < NUMBER_OF_ANSWERS; i++)
            {
                double likelihood = entry.getValue().get(i);
                if (likelihood > max_likelihood_value)
                {
                    max_likelihood_value = likelihood;
                    max_likelihood_question_id = entry.getKey();
                }
            }
        }

        // Count and mark other values with the same value as max
        Set<Integer> equal_indexes = new HashSet<>();
        for (int i = 0; i < NUMBER_OF_ANSWERS; i++)
        {
            if (map.get(max_likelihood_question_id) != null &&
                    map.get(max_likelihood_question_id).get(i) == max_likelihood_value)
            {
                equal_indexes.add(i);
            }
        }

        if (equal_indexes.size() == 1)
        {
            // If no match found, return 0, otherwise it's the single highest result and return 1
            if (! equal_indexes.contains(correct_answer)) return 0;
            else return 1;

        }
        else
        {
            // If the correct highest is on the list, return 1/size, otherwise return 0
            if (equal_indexes.contains(correct_answer)) return (double)1/equal_indexes.size();
            else return 0;
        }
    }

    /**
     * Calculate highest likelihood between a specific question-seed sentence and each answer.
     *
     * @param sentence_seed_id an id of the question-seed sentence
     * @param question_score   a score of the question-seed sentence
     * @param answerMap        map of answers scores
     * @return a max likelihood between question-seed sentence and answer sentence
     */
    private double calculateHighestLikelihood(int sentence_seed_id, double question_score, Map<Integer,Double> answerMap,
                                              Map<Integer,Double> questionAnswerMap)
    {
        double likelihood = -1;

        // For each answer
        for (Map.Entry<Integer,Double> entry: answerMap.entrySet())
        {
            /**
             * Calculate distance as (in two cases):
             * - case 1: question-selected sentence (seed) is before answer
             *           distance = question_seed_id/(question_seed_id*2 - answer_id)
             * - case 2: question-selected sentence (seed) is after or same as answer
             *           distance = question_seed_id/answer_id
             */

            double distanceFactor = entry.getKey() < sentence_seed_id ?
                    (double)sentence_seed_id/((double)sentence_seed_id*2-entry.getKey()) :
                    (double)sentence_seed_id/((double)entry.getKey());

            /**
             * Likelihood is the accumulated score of:
             * - score (relevance) of question
             * - score (relevance) of answer
             * - score (relevance) of question-answer (joint)
             * - distance factor (distance between question and answer sentence)
             */

            double answer_score = entry.getValue();

            double question_answer_score = questionAnswerMap.containsKey(entry.getKey()) ?
                    questionAnswerMap.get(entry.getKey()) : 0;

            double calculated_likelihood = question_score + answer_score + question_answer_score + distanceFactor;

            if (likelihood == -1 || calculated_likelihood > likelihood)
            {
                likelihood = calculated_likelihood;
            }
        }

        return likelihood;
    }

    /**
     * Merge results from different fields into one map.
     *
     * @param map Map of results from different fields with their results
     * @return    A merged map that contains a sum of hits across all fields
     */
    private Map<Integer,Double> mergeResults(Map<FieldType,Map<Integer,Double>> map)
    {
        Map<Integer,Double> m_merged = new HashMap<>();

        for (Map.Entry<FieldType,Map<Integer,Double>> entry: map.entrySet())
        {
            if (entry.getValue() == null) continue;

            for (Map.Entry<Integer,Double> entry_field: entry.getValue().entrySet())
            {
                m_merged.merge(entry_field.getKey(), entry_field.getValue(), (v1, v2) -> v1 + v2);
            }
        }

        return m_merged;
    }

    private Map<Integer,Double> mergeMaps(Map<Integer,Double>... maps)
    {
        Map<Integer,Double> m_merged = new HashMap<>();

        for (Map<Integer,Double> map: maps)
        {
            m_merged.putAll(map);
        }

        return m_merged;
    }

    /**
     * Returns a map (LinkedHashMap) of top N entries in map.
     *
     * @param map Map to be get top N from
     * @param N   N top results to get
     * @return    A LinkedHashMap object with top N results
     */
    private Map<Integer,Double> getTopN(Map<Integer,Double> map, int N)
    {
        Map<Integer,Double> sortedMap = sortByValue(map);
        Map<Integer,Double> new_map = new LinkedHashMap<>();

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

    public TreeMap sortByValue(Map unsortedMap) {
        TreeMap sortedMap = new TreeMap(new ValueComparator(unsortedMap));
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
