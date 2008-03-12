package hudson.plugins.crap4j.model;

import hudson.model.AbstractBuild;
import hudson.plugins.crap4j.Crap4JBuildAction;
import hudson.plugins.crap4j.calculation.CrapDataComparer;
import hudson.plugins.crap4j.display.ICrapComparison;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import com.schneide.crap4j.reader.model.IMethodCrapData;
import com.schneide.crap4j.reader.model.IOverallStatistics;

public class ProjectCrapBean implements Serializable {
	
	private static final long serialVersionUID = -5174742165631443987L;
	private final IMethodCrap[] crapMethods;
	private final String name;
	private final double totalCrap;
	private final int methodCount;
	private final int crapMethodCount;
	private final double crapMethodPercent;
	private final int crapLoad;
	private final AbstractBuild<?,?> build;
	private final int newCrapMethodsCount;
	private final int fixedCrapMethodsCount;
	
	private transient WeakReference<IMethodCrap[]> newCrapMethods;
	private transient WeakReference<IMethodCrap[]> fixedCrapMethods;
	
	public ProjectCrapBean(AbstractBuild<?,?> build,
			IOverallStatistics statistics,
			IMethodCrapData... data) {
		super();
		checkParameters(statistics, data);
		this.build = build;
		this.crapMethods = extractCrapMethods(data);
		this.name = statistics.getName();
		this.totalCrap = statistics.getTotalCrap();
		this.methodCount = statistics.getMethodCount();
		this.crapMethodCount = statistics.getCrapMethodCount();
		this.crapMethodPercent = statistics.getCrapMethodPercent();
		this.crapLoad = statistics.getCrapLoad();
		// Getting the delta counts to the previous build (if one exists)
		ICrapComparison comparison = compareWithPreviousCrap();
		this.newCrapMethodsCount = comparison.getNewCrapMethods().length;
		this.fixedCrapMethodsCount = comparison.getFixedCrapMethods().length;
		loadCrapMethodComparison(comparison);
	}
	
	private ICrapComparison compareWithPreviousCrap() {
		IMethodCrap[] previousCrapMethods = new IMethodCrap[0];
		ProjectCrapBean previousCrap = getPrevious();
		if (null != previousCrap) {
			previousCrapMethods = previousCrap.getCrapMethods();
		}
		ICrapComparison comparison = new CrapDataComparer(previousCrapMethods, this.crapMethods);
		return comparison;
	}
	
	private void loadCrapMethodComparison() {
		loadCrapMethodComparison(compareWithPreviousCrap());
	}
	
	public IMethodCrap[] getFixedMethods() {
		if (null == this.fixedCrapMethods) {
			loadCrapMethodComparison();
		}
		if (null == this.fixedCrapMethods.get()) {
			loadCrapMethodComparison();
		}
		return this.fixedCrapMethods.get();
	}
	
	public IMethodCrap[] getNewMethods() {
		if (null == this.newCrapMethods) {
			loadCrapMethodComparison();
		}
		if (null == this.newCrapMethods.get()) {
			loadCrapMethodComparison();
		}
		return this.newCrapMethods.get();
	}
	
	private void loadCrapMethodComparison(ICrapComparison comparison) {
		this.newCrapMethods = new WeakReference<IMethodCrap[]>(comparison.getNewCrapMethods());
		this.fixedCrapMethods = new WeakReference<IMethodCrap[]>(comparison.getFixedCrapMethods());
	}
	
	private static IMethodCrap[] extractCrapMethods(IMethodCrapData... data) {
		List<MethodCrapBean> crapBeans = new ArrayList<MethodCrapBean>();
		for (int i = 0; i < data.length; i++) {
			if (data[i].isCrappy()) {
				crapBeans.add(new MethodCrapBean(data[i]));
			}
		}
		return crapBeans.toArray(new MethodCrapBean[crapBeans.size()]);
	}

	private void checkParameters(IOverallStatistics statistics, IMethodCrapData... data) {
		if (statistics.getMethodCount() == data.length) {
			return;
		}
		StringBuilder message = new StringBuilder();
		message.append("Method count does not match: statistic say ");
		message.append(statistics.getMethodCount());
		message.append(" but there were ");
		message.append(data.length);
		message.append(" data objects given.");
		throw new IllegalArgumentException(message.toString());
	}
	
	public IMethodCrap[] getCrapMethods() {
		return this.crapMethods;
	}

	public String getName() {
		return this.name;
	}

	public double getTotalCrap() {
		return this.totalCrap;
	}

	public int getMethodCount() {
		return this.methodCount;
	}

	public int getCrapMethodCount() {
		return this.crapMethodCount;
	}

	public double getCrapMethodPercent() {
		return this.crapMethodPercent;
	}

	public int getCrapLoad() {
		return this.crapLoad;
	}
	
	public int getNewCrapMethodsCount() {
		return this.newCrapMethodsCount;
	}
	
	public int getFixedCrapMethodsCount() {
		return this.fixedCrapMethodsCount;
	}
	
	public ProjectCrapBean getPrevious() {
		AbstractBuild<?,?> previous = getBuild().getPreviousBuild();
		while (null != previous) {
			Crap4JBuildAction action = previous.getAction(Crap4JBuildAction.class);
			if (null != action) {
				return action.getCrap();
			}
			previous = previous.getPreviousBuild();
		}
		return null;
	}
	
	public AbstractBuild<?,?> getBuild() {
		return this.build;
	}
}
