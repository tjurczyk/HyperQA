package edu.emory.clir.hyperqa.representation;

import edu.emory.clir.clearnlp.component.utils.NLPUtils;
import edu.emory.clir.clearnlp.dependency.DEPNode;
import edu.emory.clir.clearnlp.dependency.DEPTree;
import edu.emory.clir.clearnlp.reader.TSVReader;
import edu.emory.clir.clearnlp.tokenization.AbstractTokenizer;
import edu.emory.clir.clearnlp.util.IOUtils;
import edu.emory.clir.clearnlp.util.lang.TLanguage;

import java.util.List;

/**
 * @author Tomasz Jurczyk ({@code tomasz.jurczyk@emory.edu})
 * @since 1.0
 */
public class Sentence {
    private String  text;
    private DEPTree depTree;
    private int     ID = -1;

    public Sentence(DEPTree tree)
    {
        depTree = tree;
        constructText();
    }

    public Sentence(DEPTree tree, int _ID)
    {
        depTree = tree;
        this.ID = _ID;
        constructText();
    }

    private void constructText()
    {
        StringBuilder builder = new StringBuilder();

        for (DEPNode node: this.depTree)
        {
            builder.append(node.getWordForm() + " ");
        }

        text = builder.toString();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public DEPTree getDepTree() {
        return depTree;
    }

    public void setDepTree(DEPTree depTree) {
        this.depTree = depTree;
    }

    public int getID()
    {
        return this.ID;
    }
}
