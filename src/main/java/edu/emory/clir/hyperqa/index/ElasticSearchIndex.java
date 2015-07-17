package edu.emory.clir.hyperqa.index;

import edu.emory.clir.clearnlp.util.BinUtils;
import edu.emory.clir.hyperqa.representation.DecomposedSentence;
import edu.emory.clir.hyperqa.representation.JSONBuilder;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.NoNodeAvailableException;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.indices.IndexMissingException;

/**
 * @author Tomasz Jurczyk ({@code tomasz.jurczyk@emory.edu})
 * @since 1.0
 */
public class ElasticSearchIndex implements Index{
    private Client client;
    private String ESServerAddress = "127.0.0.1";
    private int    ESServerPort    = 9300;

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

        // Make sure index is fresh and clean
        performClearIndex();
    }

    public void clearIndex()
    {
        performClearIndex();
    }

    private void performClearIndex() throws NoNodeAvailableException
    {
        client.admin().indices().delete(new DeleteIndexRequest("index")).actionGet();

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

    public void index(DecomposedSentence representation)
    {
        String json = JSONBuilder.buildJSONString(representation);

        System.out.println("Json to sent: " + json);
        client.prepareIndex("index", "sentence")
                .setSource(json.toString())
                .execute()
                .actionGet();
    }

    public DecomposedSentence query()
    {
        return null;
    }

    public void shutdown()
    {
        client.close();
    }
}
