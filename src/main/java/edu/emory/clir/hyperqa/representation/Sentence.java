package edu.emory.clir.hyperqa.representation;

import edu.emory.clir.clearnlp.dependency.DEPNode;
import edu.emory.clir.clearnlp.dependency.DEPTree;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tomasz Jurczyk ({@code tomasz.jurczyk@emory.edu})
 * @since 1.0
 */
public class Sentence {
    private String        text;
    private List<DEPTree> d_trees = new ArrayList();
    private int           Id;

    public Sentence(DEPTree tree)
    {
        addTree(tree);
        Id = -1;
        init();
    }

    public Sentence(DEPTree... trees)
    {
        for (DEPTree t: trees)
        {
            addTree(t);
        }
        Id = -1;

        init();
    }

    public Sentence(int _Id, DEPTree tree)
    {
        addTree(tree);
        Id = _Id;

        init();
    }

    public Sentence(int _Id, DEPTree... trees)
    {
        for (DEPTree t: trees)
        {
            addTree(t);
        }
        Id = _Id;

        init();
    }

    public Sentence(List<DEPTree>... trees)
    {
        for (List<DEPTree> ts: trees)
        {
            addTrees(ts);
        }

        init();
    }

    public Sentence(int _Id, List<DEPTree>... trees)
    {
        for (List<DEPTree> ts: trees)
        {
            addTrees(ts);
        }
        Id = _Id;

        init();
    }

    private void addTree(DEPTree t)
    {
        d_trees.add(t);
    }

    private void addTrees(List<DEPTree> ts)
    {
        d_trees.addAll(ts);
    }

    private void init()
    {
        StringBuilder builder = new StringBuilder();

        for (DEPTree tree: d_trees)
        {
            for (DEPNode node: tree)
            {
                builder.append(node.getWordForm() + " ");
            }
        }

        text = builder.toString();
    }

    public String getText() {
        return text;
    }

    public List<DEPTree> getDepTrees() {
        return d_trees;
    }

    public int getID()
    {
        return this.Id;
    }

    public Sentence concatenate(Sentence s)
    {
        return new Sentence(-1, this.getDepTrees(), s.getDepTrees());
    }
}
