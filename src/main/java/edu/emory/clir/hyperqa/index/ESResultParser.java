package edu.emory.clir.hyperqa.index;

import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Tomasz Jurczyk ({@code tomasz.jurczyk@emory.edu})
 * @since 1.0
 */
public class ESResultParser extends QueryResultParser<SearchHits> {
    public Map<Integer,Double> parse(SearchHits a_hits)
    {
        Map<Integer,Double> m_result = new HashMap();

        for (SearchHit sh: a_hits)
        {
            int sentence_id = Integer.parseInt((String) sh.getFields().get("ID").getValue());
            m_result.put(sentence_id, (double) sh.getScore());
        }

        return m_result;
    }
}
