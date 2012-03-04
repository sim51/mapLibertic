package service;

import java.io.IOException;

import jlibdiff.Diff;
import jlibdiff.HunkAdd;
import jlibdiff.HunkChange;
import jlibdiff.HunkDel;
import jlibdiff.HunkVisitor;

/**
 * Class to make HTML diff between two string or file.
 * 
 * @author bsimard
 * 
 */
public class DiffUtil extends HunkVisitor {

    private Diff         diff;
    private StringBuffer buf;
    private StringBuffer bufOut;
    private int          currentPos  = 0;
    private int          softChanges = 0;
    private int          hardChanges = 0;

    /**
     * Constructor of the class.
     * 
     * @param str1
     * @param str2
     * @throws IOException
     */
    public DiffUtil(String str1, String str2) throws IOException {
        buf = new StringBuffer();
        buf.append(str1);
        bufOut = new StringBuffer();
        diff = new Diff();
        diff.diffString(str1, str2);
        diff.accept(this);
    }

    /**
     * Add the added element to the output.
     */
    public void visitHunkAdd(HunkAdd hunk) {
        String text = hunk.getNewContents().trim();
        // si la position courante est différente du début de la modif
        if (currentPos != hunk.lowLine(0)) {
            // on ajoute le texte d'avant
            bufOut.append(buf.substring(currentPos, hunk.lowLine(0)));
            currentPos = hunk.highLine(0);
        }
        // on ajoute le texte ajouté entre deux spans
        if (text != null && !text.equals("")) {
            text = "<span class='added'>" + text + "</span>";
            bufOut.append(text);
            ++hardChanges;
        }
        else {
            ++softChanges;
        }
    }

    /**
     * Add the deleted element to the output.
     */
    public void visitHunkDel(HunkDel hunk) {
        String text = hunk.getOldContents().trim();
        // si la position courante est différente du début de la modif
        if (currentPos != hunk.lowLine(0)) {
            // on ajoute le texte d'avant
            bufOut.append(buf.substring(currentPos, hunk.lowLine(0) - 1));
            currentPos = hunk.highLine(0);
        }
        if (text != null && !text.equals("")) {
            text = "<span class='removed'>" + text + "</span>";
            bufOut.append(text);
            ++hardChanges;
        }
        else {
            ++softChanges;
        }
    }

    /**
     * Add the modified element to the output.
     */
    public void visitHunkChange(HunkChange hunk) {
        String text = hunk.getNewContents().trim();
        // si la position courante est différente du début de la modif
        if (currentPos != hunk.lowLine(0)) {
            // on ajoute le texte d'avant
            bufOut.append(buf.substring(currentPos, hunk.lowLine(0) - 1));
            currentPos = hunk.highLine(0);
        }
        if (text != null && !text.equals("")) {
            text = "<span class='changed'>" + "<span class='oldValue'>"
                    + buf.substring(hunk.lowLine(0) - 1, hunk.highLine(0)) + "</span>" + text + "</span>";
            bufOut.append(text);
            ++hardChanges;
        }
        else {
            ++softChanges;
        }
    }

    /**
     * Return the output.
     */
    public String toString() {
        String ret = "";
        ret = bufOut.toString();
        return ret;
    }

}
