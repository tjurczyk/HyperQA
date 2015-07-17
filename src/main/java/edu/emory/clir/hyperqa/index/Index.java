package edu.emory.clir.hyperqa.index;

import edu.emory.clir.hyperqa.representation.DecomposedSentence;

/**
 * @author Tomasz Jurczyk ({@code tomasz.jurczyk@emory.edu})
 * @since 1.0
 */
public interface Index {
    void index(DecomposedSentence sentence);
    void shutdown();
    DecomposedSentence query();
    void clearIndex();

}
