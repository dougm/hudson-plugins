package hudson.plugins.coverage.model;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Set;
import java.util.TreeSet;

/**
 * Represents an element in the source code.
 *
 * @author Stephen Connolly
 * @since 26-Jun-2008 17:04:11
 */
public final class Element implements Comparable<Element> {
// ------------------------------ FIELDS ------------------------------

    /**
     * The parent source code element.
     */
    private final Element parent;

    /**
     * The name of this element.
     */
    private final String name;

    /**
     * {@code true} if the element has a one-to-one correspondance with source code files.
     */
    private final boolean fileLevel;

    /**
     * The child elements.
     */
    private final transient Set<Element> children = new TreeSet<Element>();

    /**
     * Lazily calculated full name of the element.
     */
    private transient String fullName = null;

    /**
     * Lazily calculated flag to indicate that this element is below the file level.
     */
    private transient Boolean subfileLevel = null;

// -------------------------- STATIC METHODS --------------------------

    /**
     * Gets the root element from which all elements must inherit.
     *
     * @return the root element from which all elements must inherit.
     */
    public static Element getRootElement() {
        return SingletonHolder.ROOT;
    }

    /**
     * Creates a new source code element.
     *
     * @param parent    The parent.
     * @param name      The name of the element.
     * @param fileLevel {@code true} if this element corresponds to a source file.
     * @return The new element.
     */
    public static Element newElement(Element parent, String name, boolean fileLevel) {
        Element result = new Element(parent, name, fileLevel);
        parent.addChild(result);
        return result;
    }

    /**
     * Adds a child to this, it's parent.
     *
     * @param child the child.
     */
    private synchronized void addChild(Element child) {
        if (child.getParent() != parent) {
            throw new IllegalArgumentException("Cannot add the child of a different parent");
        }
        children.add(child);
    }

    /**
     * Creates a new source code element as a child of {@code this}.
     *
     * @param name      The name of the element.
     * @param fileLevel {@code true} if this element corresponds to a source file.
     * @return The new element.
     */
    public Element newChild(String name, boolean fileLevel) {
        Element result = new Element(this, name, fileLevel);
        addChild(result);
        return result;
    }

// --------------------------- CONSTRUCTORS ---------------------------

    /**
     * Constructor for root element.
     */
    private Element() {
        name = "project";
        parent = null;
        fileLevel = false;
    }

    /**
     * Constructor for a child element.
     *
     * @param parent    The parent.
     * @param name      The name.
     * @param fileLevel {@code true} if this is a file level element.
     */
    private Element(Element parent, String name, boolean fileLevel) {
        parent.getClass(); // throw NPE if null
        name.getClass(); // throw NPE if null
        if (name.indexOf('/') != -1) {
            throw new IllegalArgumentException("The name of an element caonnot contain the '/' character");
        }
        this.name = name;
        this.parent = parent;
        this.fileLevel = fileLevel;
    }

// --------------------- GETTER / SETTER METHODS ---------------------

    /**
     * Gets the full name for the element.
     *
     * @return the full name for the element.
     */
    public String getFullName() {
        // don't need to worry about multiple threads as the result must be the same.
        if (fullName == null) {
            LinkedList<String> names = new LinkedList<String>();
            for (Element i = this; i.getParent() != null; i = i.getParent()) {
                names.addFirst(i.getName());
            }
            StringBuilder buf = new StringBuilder();
            boolean first = true;
            for (String name : names) {
                buf.append('/');
                buf.append(name);
            }
            fullName = buf.toString();
        }
        return fullName;
    }

    /**
     * Getter for property 'name'.
     *
     * @return Value for property 'name'.
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for property 'parent'.
     *
     * @return Value for property 'parent'.
     */
    public Element getParent() {
        return parent;
    }

    /**
     * Getter for property 'fileLevel'.
     *
     * @return Value for property 'fileLevel'.
     */
    public boolean isFileLevel() {
        return fileLevel;
    }

// ------------------------ CANONICAL METHODS ------------------------

    /**
     * {@inheritDoc}
     */
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Element element = (Element) o;

        if (!name.equals(element.name)) return false;
        if (parent != null ? !parent.equals(element.parent) : element.parent != null) return false;

        return true;
    }

    /**
     * {@inheritDoc}
     */
    public int hashCode() {
        int result;
        result = (parent != null ? parent.hashCode() : 0);
        result = 31 * result + name.hashCode();
        return result;
    }

// ------------------------ INTERFACE METHODS ------------------------

// --------------------- Interface Comparable ---------------------

    /**
     * {@inheritDoc}
     */
    public int compareTo(Element that) {
        if (parent == that.parent || (parent != null && parent.equals(that.parent))) {
            // same parent, so compare based on name
            return name.compareTo(that.name);
        }
        if (parent != null && that.parent != null) {
            // has two parents and both are not the root, so compare parents
            return parent.compareTo(that.parent);
        }
        if (parent == null) {
            // this is nearest the root, so comes first
            assert that.parent != null;
            return -1;
        }
        // that is nearest the root, so comes first.
        return +1;
    }

// -------------------------- OTHER METHODS --------------------------

    /**
     * Getter for property 'children'.
     *
     * @return Value for property 'children'.
     */
    public synchronized Set<Element> getChildren() {
        return Collections.unmodifiableSet(children);
    }

    /**
     * Getter for property 'subfileLevel'.
     *
     * @return Value for property 'subfileLevel'.
     */
    public boolean isSubfileLevel() {
        // don't need to worry about multiple threads as the result must be the same.
        if (subfileLevel == null) {
            if (fileLevel || this.getParent() == null) {
                subfileLevel = Boolean.FALSE;
            } else {
                subfileLevel = Boolean.valueOf(getParent().isSubfileLevel());
            }
        }
        return subfileLevel.booleanValue();
    }

// -------------------------- INNER CLASSES --------------------------

    /**
     * Holds the root element and ensures that the Element class has fully loaded before constructing it.
     */
    private static final class SingletonHolder {
// ------------------------------ FIELDS ------------------------------

        /**
         * The root element singleton.
         */
        private static final Element ROOT = new Element();

// --------------------------- CONSTRUCTORS ---------------------------

        /**
         * Do not instantiate SingletonHolder.
         */
        private SingletonHolder() {
        }
    }
}
