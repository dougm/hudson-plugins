/* Copyright (c) 2007, http://www.codeviation.org project 
 * This program is made available under the terms of the MIT License. 
 */

package hudson.plugins.codeviation;

import hudson.model.Hudson;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openide.util.Lookup;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;

/**
 *Default lookup for hpi plugin. It adds Meta-inf/services from classloader of Hudson to lookup. 
 * @author pzajac
 */
public class HPILookup extends Lookup {
    static final Logger logger = Logger.getLogger(Lookup.class.getName());
    private AbstractLookup lookup; 
    private InstanceContent content;
    public HPILookup() {
        content = new InstanceContent();
        lookup = new AbstractLookup(content);
    }

    @SuppressWarnings("unchecked")
    public <T> T lookup(Class<T> clazz) {
        T t = lookup.lookup(clazz);
        if (t != null) {
            return t;
        }
        Collection<? extends Class> classes = Hudson.getInstance().getPluginManager().discover(clazz);
        for (Class c : classes ) {
            try {
                Object obj =  c.newInstance();
                content.add(obj);
                return (T)obj;

            } catch (InstantiationException instantiationException) {
                logger.log(Level.SEVERE,null,instantiationException);
            } catch (IllegalAccessException cnfe) {
                logger.log(Level.SEVERE,null,cnfe);
            } 
        }
        return null;
    }
    @SuppressWarnings("unchecked")
    public <T> Result<T> lookup(Template<T> templ) {
        Class<T> clazz = templ.getType();
 
        Collection<? extends Class> classes = Hudson.getInstance().getPluginManager().discover(clazz);
        for (Class c : classes ) {
                if (lookup.lookup(c) == null) {
                    try {
                        Object obj =  c.newInstance();
                        content.add(obj);
                    } catch (InstantiationException instantiationException) {
                        logger.log(Level.SEVERE,null,instantiationException);
                    } catch (IllegalAccessException cnfe) {
                        logger.log(Level.SEVERE,null,cnfe);
                    } 
                }
        }
        return lookup.lookup(templ);
    }
}
