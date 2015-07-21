package edu.emory.clir.hyperqa.decomposition;

/**
 * @author Tomasz Jurczyk ({@code tomasz.jurczyk@emory.edu})
 * @since 1.0
 */
public enum FieldType {
    ID         (Group.OTHER),

    TEXT       (Group.LEXICAL),
    LEMMA_TEXT (Group.LEXICAL),

    DEPNODE    (Group.SYNTACTIC),
    DEPWORD    (Group.SYNTACTIC),

    VERB       (Group.SEMANTIC),
    SEMROLES   (Group.SEMANTIC),
    SEM_A0     (Group.SEMANTIC),
    SEM_A1     (Group.SEMANTIC),
    SEM_A2     (Group.SEMANTIC),
    SEM_A3     (Group.SEMANTIC),
    SEM_A4     (Group.SEMANTIC);


    private Group group;

    FieldType(Group group) {
        this.group = group;
    }

    public boolean isInGroup(Group group) {
        return this.group == group;
    }

    public Group getGroup()
    {
        return this.group;
    }

    public enum Group {
        LEXICAL,
        SYNTACTIC,
        SEMANTIC,
        OTHER;
    }
}