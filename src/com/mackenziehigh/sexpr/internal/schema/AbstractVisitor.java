/*
 * Copyright 2017 Michael Mackenzie High
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mackenziehigh.sexpr.internal.schema;

import high.mackenzie.snowflake.ITreeNode;
import high.mackenzie.snowflake.ITreeNodeVisitor;

/**
 * This class was auto-generated using the Snowflake parser-generator.
 *
 * <p>
 * Generated On: Sat Jul 08 20:03:48 EDT 2017</p>
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
        else if ("statement".equals(name))
        {
            visit_statement(node);
        }
        else if ("root_directive".equals(name))
        {
            visit_root_directive(node);
        }
        else if ("assignment".equals(name))
        {
            visit_assignment(node);
        }
        else if ("rule".equals(name))
        {
            visit_rule(node);
        }
        else if ("atom_rule".equals(name))
        {
            visit_atom_rule(node);
        }
        else if ("constant_rule".equals(name))
        {
            visit_constant_rule(node);
        }
        else if ("regex_rule".equals(name))
        {
            visit_regex_rule(node);
        }
        else if ("range_rule".equals(name))
        {
            visit_range_rule(node);
        }
        else if ("minimum_operator".equals(name))
        {
            visit_minimum_operator(node);
        }
        else if ("minimum_inclusive".equals(name))
        {
            visit_minimum_inclusive(node);
        }
        else if ("minimum_exclusive".equals(name))
        {
            visit_minimum_exclusive(node);
        }
        else if ("maximum_operator".equals(name))
        {
            visit_maximum_operator(node);
        }
        else if ("maximum_inclusive".equals(name))
        {
            visit_maximum_inclusive(node);
        }
        else if ("maximum_exclusive".equals(name))
        {
            visit_maximum_exclusive(node);
        }
        else if ("number".equals(name))
        {
            visit_number(node);
        }
        else if ("float".equals(name))
        {
            visit_float(node);
        }
        else if ("FLOAT".equals(name))
        {
            visit_FLOAT(node);
        }
        else if ("and_rule".equals(name))
        {
            visit_and_rule(node);
        }
        else if ("and_start".equals(name))
        {
            visit_and_start(node);
        }
        else if ("or_rule".equals(name))
        {
            visit_or_rule(node);
        }
        else if ("or_start".equals(name))
        {
            visit_or_start(node);
        }
        else if ("not_rule".equals(name))
        {
            visit_not_rule(node);
        }
        else if ("predicate_rule".equals(name))
        {
            visit_predicate_rule(node);
        }
        else if ("sequence_rule".equals(name))
        {
            visit_sequence_rule(node);
        }
        else if ("sequence_start".equals(name))
        {
            visit_sequence_start(node);
        }
        else if ("elements".equals(name))
        {
            visit_elements(node);
        }
        else if ("element".equals(name))
        {
            visit_element(node);
        }
        else if ("element_modifier".equals(name))
        {
            visit_element_modifier(node);
        }
        else if ("one_modifier".equals(name))
        {
            visit_one_modifier(node);
        }
        else if ("zoo_modifier".equals(name))
        {
            visit_zoo_modifier(node);
        }
        else if ("zom_modifier".equals(name))
        {
            visit_zom_modifier(node);
        }
        else if ("oom_modifier".equals(name))
        {
            visit_oom_modifier(node);
        }
        else if ("minmax_modifier".equals(name))
        {
            visit_minmax_modifier(node);
        }
        else if ("exact_modifier".equals(name))
        {
            visit_exact_modifier(node);
        }
        else if ("minimum".equals(name))
        {
            visit_minimum(node);
        }
        else if ("maximum".equals(name))
        {
            visit_maximum(node);
        }
        else if ("operand".equals(name))
        {
            visit_operand(node);
        }
        else if ("nested_rule".equals(name))
        {
            visit_nested_rule(node);
        }
        else if ("rule_reference".equals(name))
        {
            visit_rule_reference(node);
        }
        else if ("name".equals(name))
        {
            visit_name(node);
        }
        else if ("string".equals(name))
        {
            visit_string(node);
        }
        else if ("single_string".equals(name))
        {
            visit_single_string(node);
        }
        else if ("SINGLE_STRING_BODY".equals(name))
        {
            visit_SINGLE_STRING_BODY(node);
        }
        else if ("SINGLE_STRING_ELEMENT".equals(name))
        {
            visit_SINGLE_STRING_ELEMENT(node);
        }
        else if ("SINGLE_STRING_CHAR".equals(name))
        {
            visit_SINGLE_STRING_CHAR(node);
        }
        else if ("double_string".equals(name))
        {
            visit_double_string(node);
        }
        else if ("DOUBLE_STRING_BODY".equals(name))
        {
            visit_DOUBLE_STRING_BODY(node);
        }
        else if ("DOUBLE_STRING_ELEMENT".equals(name))
        {
            visit_DOUBLE_STRING_ELEMENT(node);
        }
        else if ("DOUBLE_STRING_CHAR".equals(name))
        {
            visit_DOUBLE_STRING_CHAR(node);
        }
        else if ("ANY_CHAR".equals(name))
        {
            visit_ANY_CHAR(node);
        }
        else if ("DIGITS".equals(name))
        {
            visit_DIGITS(node);
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
        else if ("SINGLE_COMMENT".equals(name))
        {
            visit_SINGLE_COMMENT(node);
        }
        else if ("MULTI_COMMENT".equals(name))
        {
            visit_MULTI_COMMENT(node);
        }
        else if ("MULTI_COMMENT_TEXT".equals(name))
        {
            visit_MULTI_COMMENT_TEXT(node);
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
     * This method visits a parse-tree node created by rule "DIGITS".
     */
    protected void visit_DIGITS (ITreeNode node)
    {
        // You should *not* place your code right here.
        // Instead, you should override this method via a subclass.
        visitUnknown(node); // Default Behavior
    }

    /**
     * This method visits a parse-tree node created by rule "DOUBLE_STRING_BODY".
     */
    protected void visit_DOUBLE_STRING_BODY (ITreeNode node)
    {
        // You should *not* place your code right here.
        // Instead, you should override this method via a subclass.
        visitUnknown(node); // Default Behavior
    }

    /**
     * This method visits a parse-tree node created by rule "DOUBLE_STRING_CHAR".
     */
    protected void visit_DOUBLE_STRING_CHAR (ITreeNode node)
    {
        // You should *not* place your code right here.
        // Instead, you should override this method via a subclass.
        visitUnknown(node); // Default Behavior
    }

    /**
     * This method visits a parse-tree node created by rule "DOUBLE_STRING_ELEMENT".
     */
    protected void visit_DOUBLE_STRING_ELEMENT (ITreeNode node)
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
     * This method visits a parse-tree node created by rule "FLOAT".
     */
    protected void visit_FLOAT (ITreeNode node)
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
     * This method visits a parse-tree node created by rule "MULTI_COMMENT".
     */
    protected void visit_MULTI_COMMENT (ITreeNode node)
    {
        // You should *not* place your code right here.
        // Instead, you should override this method via a subclass.
        visitUnknown(node); // Default Behavior
    }

    /**
     * This method visits a parse-tree node created by rule "MULTI_COMMENT_TEXT".
     */
    protected void visit_MULTI_COMMENT_TEXT (ITreeNode node)
    {
        // You should *not* place your code right here.
        // Instead, you should override this method via a subclass.
        visitUnknown(node); // Default Behavior
    }

    /**
     * This method visits a parse-tree node created by rule "SINGLE_COMMENT".
     */
    protected void visit_SINGLE_COMMENT (ITreeNode node)
    {
        // You should *not* place your code right here.
        // Instead, you should override this method via a subclass.
        visitUnknown(node); // Default Behavior
    }

    /**
     * This method visits a parse-tree node created by rule "SINGLE_STRING_BODY".
     */
    protected void visit_SINGLE_STRING_BODY (ITreeNode node)
    {
        // You should *not* place your code right here.
        // Instead, you should override this method via a subclass.
        visitUnknown(node); // Default Behavior
    }

    /**
     * This method visits a parse-tree node created by rule "SINGLE_STRING_CHAR".
     */
    protected void visit_SINGLE_STRING_CHAR (ITreeNode node)
    {
        // You should *not* place your code right here.
        // Instead, you should override this method via a subclass.
        visitUnknown(node); // Default Behavior
    }

    /**
     * This method visits a parse-tree node created by rule "SINGLE_STRING_ELEMENT".
     */
    protected void visit_SINGLE_STRING_ELEMENT (ITreeNode node)
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
     * This method visits a parse-tree node created by rule "and_rule".
     */
    protected void visit_and_rule (ITreeNode node)
    {
        // You should *not* place your code right here.
        // Instead, you should override this method via a subclass.
        visitUnknown(node); // Default Behavior
    }

    /**
     * This method visits a parse-tree node created by rule "and_start".
     */
    protected void visit_and_start (ITreeNode node)
    {
        // You should *not* place your code right here.
        // Instead, you should override this method via a subclass.
        visitUnknown(node); // Default Behavior
    }

    /**
     * This method visits a parse-tree node created by rule "assignment".
     */
    protected void visit_assignment (ITreeNode node)
    {
        // You should *not* place your code right here.
        // Instead, you should override this method via a subclass.
        visitUnknown(node); // Default Behavior
    }

    /**
     * This method visits a parse-tree node created by rule "atom_rule".
     */
    protected void visit_atom_rule (ITreeNode node)
    {
        // You should *not* place your code right here.
        // Instead, you should override this method via a subclass.
        visitUnknown(node); // Default Behavior
    }

    /**
     * This method visits a parse-tree node created by rule "constant_rule".
     */
    protected void visit_constant_rule (ITreeNode node)
    {
        // You should *not* place your code right here.
        // Instead, you should override this method via a subclass.
        visitUnknown(node); // Default Behavior
    }

    /**
     * This method visits a parse-tree node created by rule "double_string".
     */
    protected void visit_double_string (ITreeNode node)
    {
        // You should *not* place your code right here.
        // Instead, you should override this method via a subclass.
        visitUnknown(node); // Default Behavior
    }

    /**
     * This method visits a parse-tree node created by rule "element".
     */
    protected void visit_element (ITreeNode node)
    {
        // You should *not* place your code right here.
        // Instead, you should override this method via a subclass.
        visitUnknown(node); // Default Behavior
    }

    /**
     * This method visits a parse-tree node created by rule "element_modifier".
     */
    protected void visit_element_modifier (ITreeNode node)
    {
        // You should *not* place your code right here.
        // Instead, you should override this method via a subclass.
        visitUnknown(node); // Default Behavior
    }

    /**
     * This method visits a parse-tree node created by rule "elements".
     */
    protected void visit_elements (ITreeNode node)
    {
        // You should *not* place your code right here.
        // Instead, you should override this method via a subclass.
        visitUnknown(node); // Default Behavior
    }

    /**
     * This method visits a parse-tree node created by rule "exact_modifier".
     */
    protected void visit_exact_modifier (ITreeNode node)
    {
        // You should *not* place your code right here.
        // Instead, you should override this method via a subclass.
        visitUnknown(node); // Default Behavior
    }

    /**
     * This method visits a parse-tree node created by rule "float".
     */
    protected void visit_float (ITreeNode node)
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
     * This method visits a parse-tree node created by rule "maximum".
     */
    protected void visit_maximum (ITreeNode node)
    {
        // You should *not* place your code right here.
        // Instead, you should override this method via a subclass.
        visitUnknown(node); // Default Behavior
    }

    /**
     * This method visits a parse-tree node created by rule "maximum_exclusive".
     */
    protected void visit_maximum_exclusive (ITreeNode node)
    {
        // You should *not* place your code right here.
        // Instead, you should override this method via a subclass.
        visitUnknown(node); // Default Behavior
    }

    /**
     * This method visits a parse-tree node created by rule "maximum_inclusive".
     */
    protected void visit_maximum_inclusive (ITreeNode node)
    {
        // You should *not* place your code right here.
        // Instead, you should override this method via a subclass.
        visitUnknown(node); // Default Behavior
    }

    /**
     * This method visits a parse-tree node created by rule "maximum_operator".
     */
    protected void visit_maximum_operator (ITreeNode node)
    {
        // You should *not* place your code right here.
        // Instead, you should override this method via a subclass.
        visitUnknown(node); // Default Behavior
    }

    /**
     * This method visits a parse-tree node created by rule "minimum".
     */
    protected void visit_minimum (ITreeNode node)
    {
        // You should *not* place your code right here.
        // Instead, you should override this method via a subclass.
        visitUnknown(node); // Default Behavior
    }

    /**
     * This method visits a parse-tree node created by rule "minimum_exclusive".
     */
    protected void visit_minimum_exclusive (ITreeNode node)
    {
        // You should *not* place your code right here.
        // Instead, you should override this method via a subclass.
        visitUnknown(node); // Default Behavior
    }

    /**
     * This method visits a parse-tree node created by rule "minimum_inclusive".
     */
    protected void visit_minimum_inclusive (ITreeNode node)
    {
        // You should *not* place your code right here.
        // Instead, you should override this method via a subclass.
        visitUnknown(node); // Default Behavior
    }

    /**
     * This method visits a parse-tree node created by rule "minimum_operator".
     */
    protected void visit_minimum_operator (ITreeNode node)
    {
        // You should *not* place your code right here.
        // Instead, you should override this method via a subclass.
        visitUnknown(node); // Default Behavior
    }

    /**
     * This method visits a parse-tree node created by rule "minmax_modifier".
     */
    protected void visit_minmax_modifier (ITreeNode node)
    {
        // You should *not* place your code right here.
        // Instead, you should override this method via a subclass.
        visitUnknown(node); // Default Behavior
    }

    /**
     * This method visits a parse-tree node created by rule "name".
     */
    protected void visit_name (ITreeNode node)
    {
        // You should *not* place your code right here.
        // Instead, you should override this method via a subclass.
        visitUnknown(node); // Default Behavior
    }

    /**
     * This method visits a parse-tree node created by rule "nested_rule".
     */
    protected void visit_nested_rule (ITreeNode node)
    {
        // You should *not* place your code right here.
        // Instead, you should override this method via a subclass.
        visitUnknown(node); // Default Behavior
    }

    /**
     * This method visits a parse-tree node created by rule "not_rule".
     */
    protected void visit_not_rule (ITreeNode node)
    {
        // You should *not* place your code right here.
        // Instead, you should override this method via a subclass.
        visitUnknown(node); // Default Behavior
    }

    /**
     * This method visits a parse-tree node created by rule "number".
     */
    protected void visit_number (ITreeNode node)
    {
        // You should *not* place your code right here.
        // Instead, you should override this method via a subclass.
        visitUnknown(node); // Default Behavior
    }

    /**
     * This method visits a parse-tree node created by rule "one_modifier".
     */
    protected void visit_one_modifier (ITreeNode node)
    {
        // You should *not* place your code right here.
        // Instead, you should override this method via a subclass.
        visitUnknown(node); // Default Behavior
    }

    /**
     * This method visits a parse-tree node created by rule "oom_modifier".
     */
    protected void visit_oom_modifier (ITreeNode node)
    {
        // You should *not* place your code right here.
        // Instead, you should override this method via a subclass.
        visitUnknown(node); // Default Behavior
    }

    /**
     * This method visits a parse-tree node created by rule "operand".
     */
    protected void visit_operand (ITreeNode node)
    {
        // You should *not* place your code right here.
        // Instead, you should override this method via a subclass.
        visitUnknown(node); // Default Behavior
    }

    /**
     * This method visits a parse-tree node created by rule "or_rule".
     */
    protected void visit_or_rule (ITreeNode node)
    {
        // You should *not* place your code right here.
        // Instead, you should override this method via a subclass.
        visitUnknown(node); // Default Behavior
    }

    /**
     * This method visits a parse-tree node created by rule "or_start".
     */
    protected void visit_or_start (ITreeNode node)
    {
        // You should *not* place your code right here.
        // Instead, you should override this method via a subclass.
        visitUnknown(node); // Default Behavior
    }

    /**
     * This method visits a parse-tree node created by rule "predicate_rule".
     */
    protected void visit_predicate_rule (ITreeNode node)
    {
        // You should *not* place your code right here.
        // Instead, you should override this method via a subclass.
        visitUnknown(node); // Default Behavior
    }

    /**
     * This method visits a parse-tree node created by rule "range_rule".
     */
    protected void visit_range_rule (ITreeNode node)
    {
        // You should *not* place your code right here.
        // Instead, you should override this method via a subclass.
        visitUnknown(node); // Default Behavior
    }

    /**
     * This method visits a parse-tree node created by rule "regex_rule".
     */
    protected void visit_regex_rule (ITreeNode node)
    {
        // You should *not* place your code right here.
        // Instead, you should override this method via a subclass.
        visitUnknown(node); // Default Behavior
    }

    /**
     * This method visits a parse-tree node created by rule "root_directive".
     */
    protected void visit_root_directive (ITreeNode node)
    {
        // You should *not* place your code right here.
        // Instead, you should override this method via a subclass.
        visitUnknown(node); // Default Behavior
    }

    /**
     * This method visits a parse-tree node created by rule "rule".
     */
    protected void visit_rule (ITreeNode node)
    {
        // You should *not* place your code right here.
        // Instead, you should override this method via a subclass.
        visitUnknown(node); // Default Behavior
    }

    /**
     * This method visits a parse-tree node created by rule "rule_reference".
     */
    protected void visit_rule_reference (ITreeNode node)
    {
        // You should *not* place your code right here.
        // Instead, you should override this method via a subclass.
        visitUnknown(node); // Default Behavior
    }

    /**
     * This method visits a parse-tree node created by rule "sequence_rule".
     */
    protected void visit_sequence_rule (ITreeNode node)
    {
        // You should *not* place your code right here.
        // Instead, you should override this method via a subclass.
        visitUnknown(node); // Default Behavior
    }

    /**
     * This method visits a parse-tree node created by rule "sequence_start".
     */
    protected void visit_sequence_start (ITreeNode node)
    {
        // You should *not* place your code right here.
        // Instead, you should override this method via a subclass.
        visitUnknown(node); // Default Behavior
    }

    /**
     * This method visits a parse-tree node created by rule "single_string".
     */
    protected void visit_single_string (ITreeNode node)
    {
        // You should *not* place your code right here.
        // Instead, you should override this method via a subclass.
        visitUnknown(node); // Default Behavior
    }

    /**
     * This method visits a parse-tree node created by rule "statement".
     */
    protected void visit_statement (ITreeNode node)
    {
        // You should *not* place your code right here.
        // Instead, you should override this method via a subclass.
        visitUnknown(node); // Default Behavior
    }

    /**
     * This method visits a parse-tree node created by rule "string".
     */
    protected void visit_string (ITreeNode node)
    {
        // You should *not* place your code right here.
        // Instead, you should override this method via a subclass.
        visitUnknown(node); // Default Behavior
    }

    /**
     * This method visits a parse-tree node created by rule "zom_modifier".
     */
    protected void visit_zom_modifier (ITreeNode node)
    {
        // You should *not* place your code right here.
        // Instead, you should override this method via a subclass.
        visitUnknown(node); // Default Behavior
    }

    /**
     * This method visits a parse-tree node created by rule "zoo_modifier".
     */
    protected void visit_zoo_modifier (ITreeNode node)
    {
        // You should *not* place your code right here.
        // Instead, you should override this method via a subclass.
        visitUnknown(node); // Default Behavior
    }

}
