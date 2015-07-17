package edu.emory.clir.hyperqa.decomposition.fields;

import edu.emory.clir.clearnlp.dependency.DEPNode;
import edu.emory.clir.hyperqa.decomposition.FieldType;
import edu.emory.clir.hyperqa.representation.Sentence;

/**
 * @author Tomasz Jurczyk ({@code tomasz.jurczyk@emory.edu})
 * @since 1.0
 */
public class FieldText extends Field{
    public FieldText()
    {
        type = FieldType.TEXT;
    }

    public String decomposeSentence(Sentence sentence)
    {
        StringBuilder builder = new StringBuilder();

        for (DEPNode node: sentence.getDepTree())
        {
            builder.append(node.getWordForm() + " ");
        }

        return builder.toString();
    }
}
