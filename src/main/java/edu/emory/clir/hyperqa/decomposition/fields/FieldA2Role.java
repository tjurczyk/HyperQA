package edu.emory.clir.hyperqa.decomposition.fields;

import edu.emory.clir.clearnlp.dependency.DEPNode;
import edu.emory.clir.clearnlp.dependency.DEPTree;
import edu.emory.clir.hyperqa.decomposition.FieldType;
import edu.emory.clir.hyperqa.decomposition.FieldsConfiguration;
import edu.emory.clir.hyperqa.representation.Sentence;

/**
 * @author Tomasz Jurczyk ({@code tomasz.jurczyk@emory.edu})
 * @since 1.0
 */
public class FieldA2Role extends Field {
    public FieldA2Role(FieldsConfiguration conf)
    {
        super(conf);
        type = FieldType.SEM_A2;
    }

    public String decomposeSentence(Sentence sentence)
    {
        StringBuilder builder = new StringBuilder();

        for (DEPTree tree : sentence.getDepTrees())
        {
            DEPNode root = tree.getFirstRoot();

            for (DEPNode node : root.getDependentList())
            {
                if (node.getSemanticHeadArc(root) != null &&
                        node.getSemanticHeadArc(root).toString().contains("A2"))
                {
                    builder.append(node.getLemma() + " ");
                }
            }
        }

        return builder.toString();
    }
}
