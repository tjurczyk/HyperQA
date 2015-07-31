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
public class FieldRootVerb extends Field{
    public FieldRootVerb(FieldsConfiguration conf)
    {
        super(conf);
        type = FieldType.ROOTVERB;
    }

    public String decomposeSentence(Sentence sentence)
    {
        StringBuilder verbs = new StringBuilder();

        for (DEPTree tree: sentence.getDepTrees())
        {
            DEPNode root = tree.getFirstRoot();
            if (POSLibEn.isVerb(root.getPOSTag()))
            {
                verbs.append(root.getLemma() + " ");
            }
        }

        return verbs.toString();
    }
}
