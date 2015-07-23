package edu.emory.clir.hyperqa.representation;

import edu.emory.clir.clearnlp.util.BinUtils;
import edu.emory.clir.hyperqa.decomposition.FieldFactory;
import edu.emory.clir.hyperqa.decomposition.FieldType;
import edu.emory.clir.hyperqa.decomposition.FieldsConfiguration;
import edu.emory.clir.hyperqa.decomposition.fields.Field;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Tomasz Jurczyk ({@code tomasz.jurczyk@emory.edu})
 * @since 1.0
 */
public class DecomposedSentence {
    Sentence sentence;
    private final FieldsConfiguration configuration;
    Map<FieldType, String> m_fieldRepresentations;

    public DecomposedSentence(Sentence s, FieldsConfiguration _configuration)
    {
        sentence = s;
        configuration = _configuration;
        generateFieldRepresentation();
    }

    public Map<FieldType, String> getFieldRepresentation()
    {
        return m_fieldRepresentations;
    }

    public void generateFieldRepresentation()
    {
        m_fieldRepresentations = new HashMap();

        for (FieldType fieldType: configuration.getFields())
        {
            // If fieldType is ID, but ID is not valid (<0), skip it
            if (fieldType.equals(FieldType.ID) && sentence.getID() == -1)
            {
                continue;
            }

            try
            {
                Field field = FieldFactory.createField(fieldType, configuration);
                m_fieldRepresentations.put(fieldType, field.decomposeSentence(sentence));
            }
            catch (UnsupportedOperationException e)
            {
                BinUtils.LOG.warn("FieldType " + fieldType + " is not supported");
            }
        }

        // Add sentence ID if has a proper ID (>-1)
    }

    public String toString()
    {
        StringBuilder builder = new StringBuilder();

        for(FieldType fieldType: configuration.getFields())
        {
            builder.append("Field: " + fieldType + ": " + m_fieldRepresentations.get(fieldType) + "\t");
        }

        return builder.toString();
    }
}
