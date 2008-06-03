package hudson.plugins.crap4j.model;

import java.io.Serializable;

import com.schneide.crap4j.reader.model.IMethodCrapData;

/**
 * A serializable bean containing the crap data about one specific method.
 * @author dali
 */
public class MethodCrapBean implements Serializable, IMethodCrap {

	private static final long serialVersionUID = 6853892607068654098L;
	
	private final String packageName;
	private final String className;
	private final String methodName;
	private final String methodSignature;
	private final String fullMethod;
	
	private final double crap;
	private final double complexity;
	private final double coveragePercent;
	private final int crapLoad;
	
	public MethodCrapBean(IMethodCrapData crapData) {
		super();
		this.packageName = crapData.getContext().getPackageName();
		this.className = crapData.getContext().getClassName();
		this.methodName = getMethodNameFrom(crapData.getContext().getName());
		this.methodSignature = crapData.getContext().getSignature();
		this.fullMethod = crapData.getContext().getFullMethod();
		
		this.crap = crapData.getCrap();
		this.complexity = crapData.getComplexity();
		this.coveragePercent = crapData.getCoverage();
		this.crapLoad = crapData.getCrapLoad();
	}
	
	private static String getMethodNameFrom(String contextName) {
		if (0 == contextName.length()) {
			return "(init)";
		}
		if ("<init>".equals(contextName)) {
			return "(init)";
		}
		return contextName;
	}

	public String getPackageName() {
		return this.packageName;
	}

	public String getClassName() {
		return this.className;
	}

	public String getMethodName() {
		return this.methodName;
	}

	public String getMethodSignature() {
		return this.methodSignature;
	}

	public String getFullMethod() {
		return this.fullMethod;
	}

	public double getCrap() {
		return this.crap;
	}

	public double getComplexity() {
		return this.complexity;
	}

	public double getCoveragePercent() {
		return this.coveragePercent;
	}

	public int getCrapLoad() {
		return this.crapLoad;
	}
}
