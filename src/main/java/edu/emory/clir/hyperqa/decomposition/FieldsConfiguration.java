package edu.emory.clir.hyperqa.decomposition;

import com.google.common.collect.Sets;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Tomasz Jurczyk ({@code tomasz.jurczyk@emory.edu})
 * @since 1.0
 */
public class FieldsConfiguration {
    private Set<FieldType> fields;

    public FieldsConfiguration(List<FieldType> fieldTypes)
    {
        fields = Sets.newHashSet(fieldTypes);
    }

    public List<FieldType> getFields()
    {
        return new ArrayList(fields);
    }

    public boolean isInConfiguration(FieldType fieldType)
    {
        return fields.contains(fieldType);
    }

    public String toString()
    {
        return fields.toString();
    }
}
