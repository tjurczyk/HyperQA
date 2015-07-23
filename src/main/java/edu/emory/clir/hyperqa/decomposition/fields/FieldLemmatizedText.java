package edu.emory.clir.hyperqa.decomposition.fields;

import edu.emory.clir.clearnlp.dependency.DEPNode;
import edu.emory.clir.clearnlp.dependency.DEPTree;
import edu.emory.clir.hyperqa.decomposition.FieldType;
import edu.emory.clir.hyperqa.representation.Sentence;

/**
 * @author Tomasz Jurczyk ({@code tomasz.jurczyk@emory.edu})
 * @since 1.0
 */
public class FieldLemmatizedText extends Field {
    public FieldLemmatizedText()
    {
        type = FieldType.LEMMA_TEXT;
    }

    public String decomposeSentence(Sentence sentence)
    {
        StringBuilder builder = new StringBuilder();

        for (DEPTree tree: sentence.getDepTrees())
        {
            for (DEPNode node: tree)
            {
                builder.append(node.getLemma() + " ");
            }
        }

        return builder.toString();
    }
}
