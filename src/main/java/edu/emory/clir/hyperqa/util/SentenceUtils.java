package edu.emory.clir.hyperqa.util;

import edu.emory.clir.clearnlp.dependency.DEPNode;
import edu.emory.clir.clearnlp.dependency.DEPTree;
import edu.emory.clir.clearnlp.util.StringUtils;

/**
 * @author Tomasz Jurczyk ({@code tomasz.jurczyk@emory.edu})
 * @since 1.0
 */
public class SentenceUtils {
    private SentenceUtils(){}

    public static boolean isQuestion(DEPTree depTree)
    {
        for (DEPNode node: depTree)
        {
            if (node.getWordForm().equals("?")) return true;
        }

        return false;
    }

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
