package hudson.plugins.iphoneview;

import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.scm.ChangeLogSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class IPhoneJob<P extends AbstractProject<P, B>, B extends AbstractBuild<P, B>>  {

    private final P project;

    private final List<B> changedBuilds;

    public IPhoneJob(final P project) {
        this.project = project;
        this.changedBuilds = changedBuildsAll();
    }

    public List<B> getChangedBuilds(final int size) {
        final List<B> builds = new ArrayList<B>();
        final int buildsSize = changedBuilds.size();
        for (int i = 0; i < Math.min(buildsSize, size);  i++) {
            builds.add(changedBuilds.get(i));
        }
        return Collections.unmodifiableList(builds);
    }

    public List<B> getChangedBuildsAll() {
        return changedBuilds;
    }

    private List<B> changedBuildsAll() {
        final List<B> builds = new ArrayList<B>();
        for (B build : project.getBuilds()) {
            final Iterator<? extends ChangeLogSet.Entry> it = build.getChangeSet().iterator();
            if (it.hasNext()) {
                builds.add(build);
            }
        }
        return Collections.unmodifiableList(builds);
    }

    public P getJob() {
        return project;
    }

}
