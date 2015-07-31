package edu.emory.clir.hyperqa.decomposition;

import edu.emory.clir.hyperqa.decomposition.fields.*;

/**
 * @author Tomasz Jurczyk ({@code tomasz.jurczyk@emory.edu})
 * @since 1.0
 */
public class FieldFactory {
    public static Field createField(FieldType type, FieldsConfiguration conf)
    {
        switch(type)
        {
            case ID:
                return new FieldID(conf);
            case TEXT:
                return new FieldText(conf);
            case LEMMA_TEXT:
                return new FieldLemmatizedText(conf);
            case ROOTVERB:
                return new FieldRootVerb(conf);
            case SEM_A1:
                return new FieldA1Role(conf);
            default:
                throw new UnsupportedOperationException();
        }
    }
}
