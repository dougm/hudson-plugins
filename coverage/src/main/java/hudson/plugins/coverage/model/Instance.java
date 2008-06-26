package hudson.plugins.coverage.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * An instance of an {@linkplain Element}.
 *
 * @author Stephen Connolly
 * @since 26-Jun-2008 21:45:31
 */
public class Instance {
    private final Instance parent;
    private final String name;
    private final Element element;
    private final Map<Element, Map<String, Instance>> children =
            new TreeMap<Element, Map<String, Instance>>();
    private final Map<Metric, Measurement> measurements = new HashMap<Metric, Measurement>();

    public Instance(Element element, Instance parent, String name) {
        element.getClass(); // throw NPE if null
        parent.getClass(); // throw NPE if null
        name.getClass(); // throw NPE if null
        this.element = element;
        this.name = name;
        this.parent = parent;
        for (Element child : element.getChildren()) {
            children.put(child, Collections.synchronizedMap(new TreeMap<String, Instance>()));
        }
    }

    public Instance newChild(Element element, String name) {
        Instance child = new Instance(element, this, name);
        addChild(child);
        return child;
    }

    private void addChild(Instance child) {
        child.getClass(); // throw NPE if null
        if (!element.getChildren().contains(child.element)) {
            throw new IllegalArgumentException("A " + child.element + " is not a child of " + element);
        }
        final Map<String, Instance> map = children.get(child.element);
        map.put(child.name, child);
    }

    public Map<String, Instance> getChildren(Element element) {
        if (!this.element.getChildren().contains(element)) {
            throw new IllegalArgumentException("A " + element + " is not a child of " + this.element);
        }
        return Collections.unmodifiableMap(children.get(element));
    }
}
