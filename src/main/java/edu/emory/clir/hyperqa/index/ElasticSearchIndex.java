package edu.emory.clir.hyperqa.index;

import edu.emory.clir.clearnlp.util.BinUtils;
import edu.emory.clir.hyperqa.decomposition.FieldType;
import edu.emory.clir.hyperqa.representation.DecomposedSentence;
import edu.emory.clir.hyperqa.util.JSONBuilder;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.NoNodeAvailableException;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.indices.IndexMissingException;
import org.elasticsearch.search.SearchHits;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Tomasz Jurczyk ({@code tomasz.jurczyk@emory.edu})
 * @since 1.0
 */
public class ElasticSearchIndex implements Index<DecomposedSentence>{
    private Client               client;
    private SearchRequestBuilder r_builder;
    private ESResultParser       resultParser;

    private String ESServerAddress = "127.0.0.1";
    private int    ESServerPort    = 9300;

    private final int    ESResultSizeLimit = 10;

    public ElasticSearchIndex(){
        prepareClient();
    }
    public ElasticSearchIndex(String _serverAddress, int _serverPort)
    {
        ESServerAddress = _serverAddress;
        ESServerPort = _serverPort;
        prepareClient();
    }

    private void prepareClient()
    {
        client = new TransportClient()
                .addTransportAddress(new InetSocketTransportAddress(ESServerAddress, ESServerPort));

        resultParser = new ESResultParser();

        SearchRequestBuilder requestBuilder = client.prepareSearch("index").setTypes("sentence");
        requestBuilder.setSize(ESResultSizeLimit);
        requestBuilder.addField(FieldType.ID.toString());

        // Make sure index is fresh and clean
        performClearIndex();
    }

    public void clearIndex()
    {
        performClearIndex();
    }

    private void performClearIndex() throws NoNodeAvailableException
    {
        try
        {
            client.admin().indices().delete(new DeleteIndexRequest("index")).actionGet();
        }
        catch (NoNodeAvailableException e)
        {
            throw new NoNodeAvailableException("No node available");
        }
        catch (IndexMissingException e)
        {
            BinUtils.LOG.warn("Tried to refresh the index, but it is missing.");
        }
    }

    public void index(DecomposedSentence sentence)
    {
        String json = JSONBuilder.buildJSONString(sentence);

        //System.out.println("Json to sent: " + json);
        client.prepareIndex("index", "sentence")
                .setSource(json.toString())
                .execute()
                .actionGet();
    }

    public Map<FieldType,Map<Integer,Double>> query(DecomposedSentence sentence)
    {
        Map<FieldType,Map<Integer,Double>> m_result = new HashMap();

        for (Map.Entry<FieldType,String> entry: sentence.getFieldRepresentation().entrySet())
        {
            m_result.put(entry.getKey(), query(entry));
        }

        return m_result;
    }

    private Map<Integer,Double> query(Map.Entry<FieldType,String> entry)
    {
        QueryStringQueryBuilder sq;

        sq = new QueryStringQueryBuilder(entry.getValue());
        sq.defaultField(entry.getKey().toString());

        SearchRequestBuilder requestBuilder = client.prepareSearch("index").setTypes("sentence");
        requestBuilder.setSize(ESResultSizeLimit);
        requestBuilder.addField(FieldType.ID.toString());
        requestBuilder.setQuery(sq);

        SearchResponse searchResponse = requestBuilder.execute().actionGet();

        SearchHits sHits = searchResponse.getHits();

        // If no hits, return null
        if (sHits.getTotalHits() == 0)
        {
            return null;
        }

        return resultParser.parse(sHits);
    }

    public void shutdown()
    {
        client.close();
    }

    public void refresh()
    {
        try
        {
            client.admin().indices().prepareRefresh().execute().actionGet();
        }
        catch (NoNodeAvailableException e)
        {
            throw new NoNodeAvailableException("No node available");
        }
        catch (IndexMissingException e)
        {
            BinUtils.LOG.warn("Tried to refresh the index, but it is missing.");
        }
    }
}
