package edu.emory.clir.hyperqa.index;

import edu.emory.clir.hyperqa.decomposition.FieldType;

import java.util.Map;

/**
 * @author Tomasz Jurczyk ({@code tomasz.jurczyk@emory.edu})
 * @since 1.0
 */
public interface Index<T> {
    void index(T sentence);
    void shutdown();
    Map<FieldType,Map<Integer,Double>> query(T sentence);
    void clearIndex();
    void refresh();

}
