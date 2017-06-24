package com.mackenziehigh.sexpr;

import java.util.function.Predicate;

/**
 * Symbolic Expression.
 *
 * <p>
 * The textual representations of symbolic expressions are case-sensitive.
 * </p>
 *
 * <p>
 * Symbolic expressions are naturally comparable
 * based on their textual representations.
 * </p>
 *
 * <p>
 * Two symbolic expressions (X) and (Y) are equal,
 * iff X.toString() equals Y.toString().
 * </p>
 *
 * <p>
 * All instances of this interface are immutable.
 * </p>
 *
 * @param <T>
 */
public interface Sexpr<T extends Sexpr<T>>
        extends Comparable<Sexpr>
{
    /**
     * This method counts the leaf nodes in the tree rooted at this node.
     *
     * <p>
     * This is a constant-time operation.
     * </p>
     *
     * @return the number of leaf nodes.
     */
    public int treeLeafCount ();

    /**
     * This method determines the height of the tree rooted at this node.
     *
     * <p>
     * This is a constant-time operation.
     * </p>
     *
     * @return the height of this tree.
     */
    public int treeHeight ();

    /**
     * This method counts the nodes in the tree rooted at this node.
     *
     * <p>
     * This is a constant-time operation.
     * </p>
     *
     * @return the number of nodes, including this node.
     */
    public int treeSize ();

    /**
     * This method performs a breadth-first-search of the tree rooted at this node.
     *
     * @param condition will return true, when the sought after node is found.
     * @return true, if the node was found.
     */
    public boolean bfs (final Predicate<Sexpr> condition);

    /**
     * This method performs a depth-first-search of the tree rooted at this node.
     *
     * @param condition will return true, when the sought after node is found.
     * @return true, if the node was found.
     */
    public boolean dfs (final Predicate<Sexpr> condition);

    /**
     * This method performs a pre-order-search of the tree rooted at this node.
     *
     * @param condition will return true, when the sought after node is found.
     * @return true, if the node was found.
     */
    public boolean preorder (final Predicate<Sexpr> condition);

    /**
     * This method performs a post-order-search of the tree rooted at this node.
     *
     * @param condition will return true, when the sought after node is found.
     * @return true, if the node was found.
     */
    public boolean postorder (final Predicate<Sexpr> condition);

    /**
     * This method determines whether this object is a SAtom.
     *
     * @return true, iff this object is a SAtom.
     */
    public boolean isAtom ();

    /**
     * This method determines whether this object is a SList.
     *
     * @return true, iff this object is a SAtom.
     */
    public boolean isList ();

    /**
     * This method obtains a mutator that can be used
     * to non-destructively modify this node.
     *
     * @return the mutator.
     */
    public SexprMutator<T> mutator ();

    /**
     * This method retrieves an object describing the source
     * from which this object we obtained.
     *
     * @return the source-code location of this Sexpr.
     */
    public SourceLocation location ();

    /**
     * {@inheritDoc}
     */
    @Override
    public default int compareTo (final Sexpr other)
    {
        return other == null ? 1 : toString().compareTo(other.toString());
    }

    /**
     * This method retrieves the textual representation of this symbolic expression.
     *
     * @return this object as a string.
     */
    @Override
    public String toString ();
}
