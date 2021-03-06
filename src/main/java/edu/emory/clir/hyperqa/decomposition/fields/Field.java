package edu.emory.clir.hyperqa.decomposition.fields;

import edu.emory.clir.hyperqa.decomposition.FieldType;
import edu.emory.clir.hyperqa.decomposition.FieldsConfiguration;
import edu.emory.clir.hyperqa.representation.Sentence;

/**
 * @author Tomasz Jurczyk ({@code tomasz.jurczyk@emory.edu})
 * @since 1.0
 */
public abstract class Field {
    protected FieldType           type;
    protected FieldsConfiguration conf;

    public Field(FieldsConfiguration conf)
    {
        this.conf = conf;
    }

    public FieldType getType()
    {
        return type;
    }

    public abstract String decomposeSentence(Sentence sentence);
}
