package hudson.plugins.pmd.util;

import hudson.model.AbstractBuild;
import hudson.model.ModelObject;
import hudson.plugins.pmd.util.model.AnnotationContainer;
import hudson.plugins.pmd.util.model.JavaPackage;
import hudson.plugins.pmd.util.model.MavenModule;
import hudson.plugins.pmd.util.model.WorkspaceFile;

import java.util.Collection;

/**
 * Result object to visualize the package statistics of a module.
 *
 * @author Ulli Hafner
 */
public class ModuleDetail extends AbstractAnnotationsDetail {
    /** Unique identifier of this class. */
    private static final long serialVersionUID = -1854984151887397361L;
    /** The module to show the details for. */
    private final MavenModule module;

    /**
     * Creates a new instance of <code>ModuleDetail</code>.
     *
     * @param owner
     *            current build as owner of this action.
     * @param module
     *            the module to show the details for
     * @param header
     *            header to be shown on detail page
     */
    public ModuleDetail(final AbstractBuild<?, ?> owner, final MavenModule module, final String header) {
        super(owner, module.getAnnotations(), header, Hierarchy.MODULE);
        this.module = module;
    }

    /** {@inheritDoc} */
    public String getDisplayName() {
        return module.getName();
    }

    /**
     * Returns the header for the detail screen.
     *
     * @return the header
     */
    public String getHeader() {
        return getName() + " - " + Messages.ModuleDetail_header() + " " + module.getName();
    }

    /**
     * Returns the maven module.
     *
     * @return the maven module
     */
    public MavenModule getModule() {
        return module;
    }

    /**
     * Returns the packages of this module.
     *
     * @return the packages of this module
     */
    @Override
    public Collection<JavaPackage> getPackages() {
        return module.getPackages();
    }

    /**
     * Returns whether this module contains just one Java package. In this case
     * we show the warnings statistics instead of package statistics.
     *
     * @return <code>true</code> if this project contains just one Java
     *         package
     */
    public boolean isSinglePackageModule() {
        return getPackages().size() == 1;
    }

    /**
     * Returns a package detail object if there are more packages available.
     * Otherwise a <code>null</code> value is returned.
     *
     * @param link
     *            the link to identify the sub page to show
     * @return the dynamic result of this module detail view
     */
    @Override
    public ModelObject getDynamic(final String link) {
        if (isSinglePackageModule()) {
            return null;
        }
        else {
            return new PackageDetail(getOwner(), module.getPackage(link), getName());
        }
    }

    /**
     * Returns a tooltip showing the distribution of priorities for the selected
     * package.
     *
     * @param packageName
     *            the package to show the distribution for
     * @return a tooltip showing the distribution of priorities
     */
    public String getToolTip(final String packageName) {
        return module.getPackage(packageName).getToolTip();
    }

    /** {@inheritDoc} */
    @Override
    public Collection<WorkspaceFile> getFiles() {
        return module.getFiles();
    }

    /** {@inheritDoc} */
    @Override
    public WorkspaceFile getFile(final String fileName) {
        return module.getFile(fileName);
    }

    /** {@inheritDoc} */
    @Override
    protected Collection<? extends AnnotationContainer> getChildren() {
        return getPackages();
    }
}

