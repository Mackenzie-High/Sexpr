package com.mackenziehigh.sexpr.internal;

import high.mackenzie.snowflake.ITreeNode;
import high.mackenzie.snowflake.ITreeNodeVisitor;

/**
 * This class was auto-generated using the Snowflake parser-generator.
 *
 * <p>
 * Generated On: Sat Jun 17 14:12:45 EDT 2017</p>
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

        if ("input".equals(name))
        {
            visit_input(node);
        }
        else if ("sexpr".equals(name))
        {
            visit_sexpr(node);
        }
        else if ("satom".equals(name))
        {
            visit_satom(node);
        }
        else if ("satom_form1".equals(name))
        {
            visit_satom_form1(node);
        }
        else if ("satom_form1_text".equals(name))
        {
            visit_satom_form1_text(node);
        }
        else if ("satom_form2".equals(name))
        {
            visit_satom_form2(node);
        }
        else if ("satom_form2_text".equals(name))
        {
            visit_satom_form2_text(node);
        }
        else if ("satom_form3".equals(name))
        {
            visit_satom_form3(node);
        }
        else if ("satom_form3_text".equals(name))
        {
            visit_satom_form3_text(node);
        }
        else if ("satom_form4".equals(name))
        {
            visit_satom_form4(node);
        }
        else if ("satom_form4_text".equals(name))
        {
            visit_satom_form4_text(node);
        }
        else if ("satom_form5".equals(name))
        {
            visit_satom_form5(node);
        }
        else if ("satom_form5_text".equals(name))
        {
            visit_satom_form5_text(node);
        }
        else if ("slist".equals(name))
        {
            visit_slist(node);
        }
        else if ("slist_body".equals(name))
        {
            visit_slist_body(node);
        }
        else if ("slist_start".equals(name))
        {
            visit_slist_start(node);
        }
        else if ("slist_end".equals(name))
        {
            visit_slist_end(node);
        }
        else if ("slist_elements".equals(name))
        {
            visit_slist_elements(node);
        }
        else if ("slist_element".equals(name))
        {
            visit_slist_element(node);
        }
        else if ("ANY_CHAR".equals(name))
        {
            visit_ANY_CHAR(node);
        }
        else if ("ESCAPE".equals(name))
        {
            visit_ESCAPE(node);
        }
        else if ("ESCAPE_B".equals(name))
        {
            visit_ESCAPE_B(node);
        }
        else if ("ESCAPE_T".equals(name))
        {
            visit_ESCAPE_T(node);
        }
        else if ("ESCAPE_N".equals(name))
        {
            visit_ESCAPE_N(node);
        }
        else if ("ESCAPE_F".equals(name))
        {
            visit_ESCAPE_F(node);
        }
        else if ("ESCAPE_R".equals(name))
        {
            visit_ESCAPE_R(node);
        }
        else if ("ESCAPE_SL".equals(name))
        {
            visit_ESCAPE_SL(node);
        }
        else if ("ESCAPE_SQ".equals(name))
        {
            visit_ESCAPE_SQ(node);
        }
        else if ("ESCAPE_DQ".equals(name))
        {
            visit_ESCAPE_DQ(node);
        }
        else if ("ESCAPE_U".equals(name))
        {
            visit_ESCAPE_U(node);
        }
        else if ("HEX_DIGIT".equals(name))
        {
            visit_HEX_DIGIT(node);
        }
        else if ("WS".equals(name))
        {
            visit_WS(node);
        }
        else if ("SP".equals(name))
        {
            visit_SP(node);
        }
        else if ("COMMENT".equals(name))
        {
            visit_COMMENT(node);
        }
        else
        {
            visitUnknown(node);
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
