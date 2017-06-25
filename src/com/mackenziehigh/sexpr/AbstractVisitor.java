package com.mackenziehigh.sexpr;

import high.mackenzie.snowflake.ITreeNode;
import high.mackenzie.snowflake.ITreeNodeVisitor;

/**
 * This class was auto-generated using the Snowflake parser-generator.
 *
 * <p>
 * Generated On: Sat Jun 24 00:16:46 EDT 2017</p>
 */
abstract class AbstractVisitor
        implements ITreeNodeVisitor
{

    /**
     * {@inheritDoc}
     */
    @Override
    public void visit (ITreeNode node)
    {
        final String name = node.rule();

        if (null == name)
        {
            visitUnknown(node);
        }
        else
        {
            switch (name)
            {
                case "input":
                    visit_input(node);
                    break;
                case "sexpr":
                    visit_sexpr(node);
                    break;
                case "satom":
                    visit_satom(node);
                    break;
                case "satom_form1":
                    visit_satom_form1(node);
                    break;
                case "satom_form1_text":
                    visit_satom_form1_text(node);
                    break;
                case "satom_form2":
                    visit_satom_form2(node);
                    break;
                case "satom_form2_text":
                    visit_satom_form2_text(node);
                    break;
                case "satom_form3":
                    visit_satom_form3(node);
                    break;
                case "satom_form3_text":
                    visit_satom_form3_text(node);
                    break;
                case "satom_form4":
                    visit_satom_form4(node);
                    break;
                case "satom_form4_text":
                    visit_satom_form4_text(node);
                    break;
                case "satom_form5":
                    visit_satom_form5(node);
                    break;
                case "satom_form5_text":
                    visit_satom_form5_text(node);
                    break;
                case "slist":
                    visit_slist(node);
                    break;
                case "slist_body":
                    visit_slist_body(node);
                    break;
                case "slist_start":
                    visit_slist_start(node);
                    break;
                case "slist_end":
                    visit_slist_end(node);
                    break;
                case "slist_elements":
                    visit_slist_elements(node);
                    break;
                case "slist_element":
                    visit_slist_element(node);
                    break;
                case "ANY_CHAR":
                    visit_ANY_CHAR(node);
                    break;
                case "ESCAPE":
                    visit_ESCAPE(node);
                    break;
                case "ESCAPE_B":
                    visit_ESCAPE_B(node);
                    break;
                case "ESCAPE_T":
                    visit_ESCAPE_T(node);
                    break;
                case "ESCAPE_N":
                    visit_ESCAPE_N(node);
                    break;
                case "ESCAPE_F":
                    visit_ESCAPE_F(node);
                    break;
                case "ESCAPE_R":
                    visit_ESCAPE_R(node);
                    break;
                case "ESCAPE_SL":
                    visit_ESCAPE_SL(node);
                    break;
                case "ESCAPE_SQ":
                    visit_ESCAPE_SQ(node);
                    break;
                case "ESCAPE_DQ":
                    visit_ESCAPE_DQ(node);
                    break;
                case "ESCAPE_U":
                    visit_ESCAPE_U(node);
                    break;
                case "HEX_DIGIT":
                    visit_HEX_DIGIT(node);
                    break;
                case "WS":
                    visit_WS(node);
                    break;
                case "SP":
                    visit_SP(node);
                    break;
                case "COMMENT":
                    visit_COMMENT(node);
                    break;
                case "COMMENT_TEXT":
                    visit_COMMENT_TEXT(node);
                    break;
                default:
                    visitUnknown(node);
                    break;
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void visitUnknown (ITreeNode node)
    {
        // You should *not* place your code right here.
        // Instead, you should override this method via a subclass.
    }

    /**
     * This method visits a parse-tree node created by rule "ANY_CHAR".
     */
    protected void visit_ANY_CHAR (ITreeNode node)
    {
        // You should *not* place your code right here.
        // Instead, you should override this method via a subclass.
        visitUnknown(node); // Default Behavior
    }

    /**
     * This method visits a parse-tree node created by rule "COMMENT".
     */
    protected void visit_COMMENT (ITreeNode node)
    {
        // You should *not* place your code right here.
        // Instead, you should override this method via a subclass.
        visitUnknown(node); // Default Behavior
    }

    /**
     * This method visits a parse-tree node created by rule "COMMENT_TEXT".
     */
    protected void visit_COMMENT_TEXT (ITreeNode node)
    {
        // You should *not* place your code right here.
        // Instead, you should override this method via a subclass.
        visitUnknown(node); // Default Behavior
    }

    /**
     * This method visits a parse-tree node created by rule "ESCAPE".
     */
    protected void visit_ESCAPE (ITreeNode node)
    {
        // You should *not* place your code right here.
        // Instead, you should override this method via a subclass.
        visitUnknown(node); // Default Behavior
    }

    /**
     * This method visits a parse-tree node created by rule "ESCAPE_B".
     */
    protected void visit_ESCAPE_B (ITreeNode node)
    {
        // You should *not* place your code right here.
        // Instead, you should override this method via a subclass.
        visitUnknown(node); // Default Behavior
    }

    /**
     * This method visits a parse-tree node created by rule "ESCAPE_DQ".
     */
    protected void visit_ESCAPE_DQ (ITreeNode node)
    {
        // You should *not* place your code right here.
        // Instead, you should override this method via a subclass.
        visitUnknown(node); // Default Behavior
    }

    /**
     * This method visits a parse-tree node created by rule "ESCAPE_F".
     */
    protected void visit_ESCAPE_F (ITreeNode node)
    {
        // You should *not* place your code right here.
        // Instead, you should override this method via a subclass.
        visitUnknown(node); // Default Behavior
    }

    /**
     * This method visits a parse-tree node created by rule "ESCAPE_N".
     */
    protected void visit_ESCAPE_N (ITreeNode node)
    {
        // You should *not* place your code right here.
        // Instead, you should override this method via a subclass.
        visitUnknown(node); // Default Behavior
    }

    /**
     * This method visits a parse-tree node created by rule "ESCAPE_R".
     */
    protected void visit_ESCAPE_R (ITreeNode node)
    {
        // You should *not* place your code right here.
        // Instead, you should override this method via a subclass.
        visitUnknown(node); // Default Behavior
    }

    /**
     * This method visits a parse-tree node created by rule "ESCAPE_SL".
     */
    protected void visit_ESCAPE_SL (ITreeNode node)
    {
        // You should *not* place your code right here.
        // Instead, you should override this method via a subclass.
        visitUnknown(node); // Default Behavior
    }

    /**
     * This method visits a parse-tree node created by rule "ESCAPE_SQ".
     */
    protected void visit_ESCAPE_SQ (ITreeNode node)
    {
        // You should *not* place your code right here.
        // Instead, you should override this method via a subclass.
        visitUnknown(node); // Default Behavior
    }

    /**
     * This method visits a parse-tree node created by rule "ESCAPE_T".
     */
    protected void visit_ESCAPE_T (ITreeNode node)
    {
        // You should *not* place your code right here.
        // Instead, you should override this method via a subclass.
        visitUnknown(node); // Default Behavior
    }

    /**
     * This method visits a parse-tree node created by rule "ESCAPE_U".
     */
    protected void visit_ESCAPE_U (ITreeNode node)
    {
        // You should *not* place your code right here.
        // Instead, you should override this method via a subclass.
        visitUnknown(node); // Default Behavior
    }

    /**
     * This method visits a parse-tree node created by rule "HEX_DIGIT".
     */
    protected void visit_HEX_DIGIT (ITreeNode node)
    {
        // You should *not* place your code right here.
        // Instead, you should override this method via a subclass.
        visitUnknown(node); // Default Behavior
    }

    /**
     * This method visits a parse-tree node created by rule "SP".
     */
    protected void visit_SP (ITreeNode node)
    {
        // You should *not* place your code right here.
        // Instead, you should override this method via a subclass.
        visitUnknown(node); // Default Behavior
    }

    /**
     * This method visits a parse-tree node created by rule "WS".
     */
    protected void visit_WS (ITreeNode node)
    {
        // You should *not* place your code right here.
        // Instead, you should override this method via a subclass.
        visitUnknown(node); // Default Behavior
    }

    /**
     * This method visits a parse-tree node created by rule "input".
     */
    protected void visit_input (ITreeNode node)
    {
        // You should *not* place your code right here.
        // Instead, you should override this method via a subclass.
        visitUnknown(node); // Default Behavior
    }

    /**
     * This method visits a parse-tree node created by rule "satom".
     */
    protected void visit_satom (ITreeNode node)
    {
        // You should *not* place your code right here.
        // Instead, you should override this method via a subclass.
        visitUnknown(node); // Default Behavior
    }

    /**
     * This method visits a parse-tree node created by rule "satom_form1".
     */
    protected void visit_satom_form1 (ITreeNode node)
    {
        // You should *not* place your code right here.
        // Instead, you should override this method via a subclass.
        visitUnknown(node); // Default Behavior
    }

    /**
     * This method visits a parse-tree node created by rule "satom_form1_text".
     */
    protected void visit_satom_form1_text (ITreeNode node)
    {
        // You should *not* place your code right here.
        // Instead, you should override this method via a subclass.
        visitUnknown(node); // Default Behavior
    }

    /**
     * This method visits a parse-tree node created by rule "satom_form2".
     */
    protected void visit_satom_form2 (ITreeNode node)
    {
        // You should *not* place your code right here.
        // Instead, you should override this method via a subclass.
        visitUnknown(node); // Default Behavior
    }

    /**
     * This method visits a parse-tree node created by rule "satom_form2_text".
     */
    protected void visit_satom_form2_text (ITreeNode node)
    {
        // You should *not* place your code right here.
        // Instead, you should override this method via a subclass.
        visitUnknown(node); // Default Behavior
    }

    /**
     * This method visits a parse-tree node created by rule "satom_form3".
     */
    protected void visit_satom_form3 (ITreeNode node)
    {
        // You should *not* place your code right here.
        // Instead, you should override this method via a subclass.
        visitUnknown(node); // Default Behavior
    }

    /**
     * This method visits a parse-tree node created by rule "satom_form3_text".
     */
    protected void visit_satom_form3_text (ITreeNode node)
    {
        // You should *not* place your code right here.
        // Instead, you should override this method via a subclass.
        visitUnknown(node); // Default Behavior
    }

    /**
     * This method visits a parse-tree node created by rule "satom_form4".
     */
    protected void visit_satom_form4 (ITreeNode node)
    {
        // You should *not* place your code right here.
        // Instead, you should override this method via a subclass.
        visitUnknown(node); // Default Behavior
    }

    /**
     * This method visits a parse-tree node created by rule "satom_form4_text".
     */
    protected void visit_satom_form4_text (ITreeNode node)
    {
        // You should *not* place your code right here.
        // Instead, you should override this method via a subclass.
        visitUnknown(node); // Default Behavior
    }

    /**
     * This method visits a parse-tree node created by rule "satom_form5".
     */
    protected void visit_satom_form5 (ITreeNode node)
    {
        // You should *not* place your code right here.
        // Instead, you should override this method via a subclass.
        visitUnknown(node); // Default Behavior
    }

    /**
     * This method visits a parse-tree node created by rule "satom_form5_text".
     */
    protected void visit_satom_form5_text (ITreeNode node)
    {
        // You should *not* place your code right here.
        // Instead, you should override this method via a subclass.
        visitUnknown(node); // Default Behavior
    }

    /**
     * This method visits a parse-tree node created by rule "sexpr".
     */
    protected void visit_sexpr (ITreeNode node)
    {
        // You should *not* place your code right here.
        // Instead, you should override this method via a subclass.
        visitUnknown(node); // Default Behavior
    }

    /**
     * This method visits a parse-tree node created by rule "slist".
     */
    protected void visit_slist (ITreeNode node)
    {
        // You should *not* place your code right here.
        // Instead, you should override this method via a subclass.
        visitUnknown(node); // Default Behavior
    }

    /**
     * This method visits a parse-tree node created by rule "slist_body".
     */
    protected void visit_slist_body (ITreeNode node)
    {
        // You should *not* place your code right here.
        // Instead, you should override this method via a subclass.
        visitUnknown(node); // Default Behavior
    }

    /**
     * This method visits a parse-tree node created by rule "slist_element".
     */
    protected void visit_slist_element (ITreeNode node)
    {
        // You should *not* place your code right here.
        // Instead, you should override this method via a subclass.
        visitUnknown(node); // Default Behavior
    }

    /**
     * This method visits a parse-tree node created by rule "slist_elements".
     */
    protected void visit_slist_elements (ITreeNode node)
    {
        // You should *not* place your code right here.
        // Instead, you should override this method via a subclass.
        visitUnknown(node); // Default Behavior
    }

    /**
     * This method visits a parse-tree node created by rule "slist_end".
     */
    protected void visit_slist_end (ITreeNode node)
    {
        // You should *not* place your code right here.
        // Instead, you should override this method via a subclass.
        visitUnknown(node); // Default Behavior
    }

    /**
     * This method visits a parse-tree node created by rule "slist_start".
     */
    protected void visit_slist_start (ITreeNode node)
    {
        // You should *not* place your code right here.
        // Instead, you should override this method via a subclass.
        visitUnknown(node); // Default Behavior
    }

}
