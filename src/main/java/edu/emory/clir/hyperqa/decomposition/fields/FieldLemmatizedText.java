package edu.emory.clir.hyperqa.decomposition.fields;

import edu.emory.clir.clearnlp.dependency.DEPNode;
import edu.emory.clir.clearnlp.dependency.DEPTree;
import edu.emory.clir.clearnlp.pos.POSLibEn;
import edu.emory.clir.hyperqa.decomposition.FieldType;
import edu.emory.clir.hyperqa.decomposition.FieldsConfiguration;
import edu.emory.clir.hyperqa.representation.Sentence;

/**
 * @author Tomasz Jurczyk ({@code tomasz.jurczyk@emory.edu})
 * @since 1.0
 */
public class FieldLemmatizedText extends Field {
    public FieldLemmatizedText(FieldsConfiguration conf)
    {
        super(conf);
        type = FieldType.LEMMA_TEXT;
    }

    public String decomposeSentence(Sentence sentence)
    {
        StringBuilder builder = new StringBuilder();

        for (DEPTree tree: sentence.getDepTrees())
        {
            for (DEPNode node: tree)
            {
                if (POSLibEn.isPunctuation(node.getPOSTag()) && conf.isSkipPunctuationMarks()) continue;
                builder.append(node.getLemma() + " ");
            }
        }

        return builder.toString();
    }
}
