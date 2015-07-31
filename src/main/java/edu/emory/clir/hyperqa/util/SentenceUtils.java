package edu.emory.clir.hyperqa.util;

import edu.emory.clir.clearnlp.dependency.DEPNode;
import edu.emory.clir.clearnlp.dependency.DEPTree;

/**
 * @author Tomasz Jurczyk ({@code tomasz.jurczyk@emory.edu})
 * @since 1.0
 */
public class SentenceUtils {
    private SentenceUtils(){}

    /**
     * Find a first occurrence of an integer in a Dep. Tree.
     *
     * @param tree Dependency tree of a sentence
     * @return first found integer in a sentence
     * @throws IndexOutOfBoundsException if no integer has been found
     */
    public static int getFirstIntOccurrence(DEPTree tree)
    {
        for (DEPNode node: tree)
        {
            try
            {
                int i = Integer.parseInt(node.getWordForm());
                return i;
            }
            catch (NumberFormatException e){}
        }

        throw new ArrayIndexOutOfBoundsException();
    }
}
