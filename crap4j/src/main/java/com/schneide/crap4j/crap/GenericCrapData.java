package com.schneide.crap4j.crap;

import com.schneide.crap4j.reader.model.IContextedCrapData;
import com.schneide.crap4j.reader.model.ICrapData;

public class GenericCrapData<O> implements IContextedCrapData<O> {

	private final O context;
	private final int crappyMethods;
	private final int totalMethods;
	private final double crap;
	private final int crapLoad;
	private final double coverage;
	private final double complexity;

	public GenericCrapData(O context, ICrapData data) {
		this(context, data.getCrappyMethods(),
				data.getTotalMethods(),
				data.getCrap(), data.getCrapLoad(),
				data.getCoverage(), data.getComplexity());
	}

	public GenericCrapData(O context,
			int crappyMethods,
			int totalMethods,
			double crap, int crapLoad,
			double coverage, double complexity) {
		super();
		this.context = context;
		this.crappyMethods = crappyMethods;
		this.totalMethods = totalMethods;
		this.crap = crap;
		this.crapLoad = crapLoad;
		this.coverage = coverage;
		this.complexity = complexity;
	}

	//@Override
	public O getContext() {
		return this.context;
	}

	//@Override
	public double getCoverage() {
		return this.coverage;
	}

	//@Override
	public double getCrap() {
		return this.crap;
	}

	//@Override
	public int getCrapLoad() {
		return this.crapLoad;
	}

	public int getCrappyMethods() {
		return this.crappyMethods;
	}

	public int getTotalMethods() {
		return this.totalMethods;
	}

	//@Override
	public double getCrapPercentage() {
		return (getCrappyMethods() / ((double) getTotalMethods()));
	}

	//@Override
	public double getComplexity() {
		return this.complexity;
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append(String.valueOf(getContext()));
		result.append(": crapPercentage="); //$NON-NLS-1$
		result.append(getCrapPercentage());
		result.append(", crap="); //$NON-NLS-1$
		result.append(getCrap());
		result.append(", crapLoad="); //$NON-NLS-1$
		result.append(getCrapLoad());
		result.append(", coverage="); //$NON-NLS-1$
		result.append(getCoverage());
		result.append(", complexity="); //$NON-NLS-1$
		result.append(getComplexity());
		return result.toString();
	}
}
