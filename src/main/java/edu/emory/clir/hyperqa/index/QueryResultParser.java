package edu.emory.clir.hyperqa.index;

import java.util.Map;

/**
 * @author Tomasz Jurczyk ({@code tomasz.jurczyk@emory.edu})
 * @since 1.0
 */
abstract class QueryResultParser<T> {
    abstract Map<Integer,Double> parse(T hits);
}
