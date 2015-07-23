package edu.emory.clir.hyperqa.representation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Tomasz Jurczyk ({@code tomasz.jurczyk@emory.edu})
 * @since 1.0
 */
public class Document implements Iterable<Sentence>{
    protected List<Sentence> l_sentences;

    public Document()
    {
        l_sentences = new ArrayList();
    }

    public List<Sentence> get_sentences() {
        return l_sentences;
    }

    public void add_sentence(Sentence sentence) {
        l_sentences.add(sentence);
    }

    public Iterator<Sentence> iterator()
    {
        return l_sentences.iterator();
    }
}
