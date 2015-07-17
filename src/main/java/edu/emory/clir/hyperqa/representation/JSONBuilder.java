package edu.emory.clir.hyperqa.representation;

import edu.emory.clir.hyperqa.decomposition.FieldType;

import java.util.Map;

/**
 * @author Tomasz Jurczyk ({@code tomasz.jurczyk@emory.edu})
 * @since 1.0
 */
public class JSONBuilder {
    public static String buildJSONString(DecomposedSentence sentence)
    {
        StringBuilder builder = new StringBuilder();

        builder.append("{");

        for (Map.Entry<FieldType,String> entry: sentence.getFieldRepresentation().entrySet())
        {
            builder.append("\"" + entry.getKey() + "\": \"" + entry.getValue() + "\",");
        }

        // Remove the last character (dot)
        builder.deleteCharAt(builder.length()-1);
        builder.append("};");

        return builder.toString();
    }
}
