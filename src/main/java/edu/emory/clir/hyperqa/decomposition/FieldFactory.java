package edu.emory.clir.hyperqa.decomposition;

import edu.emory.clir.hyperqa.decomposition.fields.Field;
import edu.emory.clir.hyperqa.decomposition.fields.FieldID;
import edu.emory.clir.hyperqa.decomposition.fields.FieldLemmatizedText;
import edu.emory.clir.hyperqa.decomposition.fields.FieldText;

/**
 * @author Tomasz Jurczyk ({@code tomasz.jurczyk@emory.edu})
 * @since 1.0
 */
public class FieldFactory {
    public static Field createField(FieldType type)
    {
        switch(type)
        {
            case ID:
                return new FieldID();
            case TEXT:
                return new FieldText();
            case LEMMA_TEXT:
                return new FieldLemmatizedText();
            default:
                throw new UnsupportedOperationException();
        }
    }
}
