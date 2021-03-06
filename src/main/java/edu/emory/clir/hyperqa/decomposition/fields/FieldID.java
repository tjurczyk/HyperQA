package edu.emory.clir.hyperqa.decomposition.fields;

import edu.emory.clir.hyperqa.decomposition.FieldType;
import edu.emory.clir.hyperqa.decomposition.FieldsConfiguration;
import edu.emory.clir.hyperqa.representation.Sentence;

/**
 * @author Tomasz Jurczyk ({@code tomasz.jurczyk@emory.edu})
 * @since 1.0
 */
public class FieldID extends Field {
    public FieldID(FieldsConfiguration conf)
    {
        super(conf);
        type = FieldType.ID;
    }

    public String decomposeSentence(Sentence sentence)
    {
        StringBuilder builder = new StringBuilder();

        builder.append(sentence.getID());

        return builder.toString();
    }
}
