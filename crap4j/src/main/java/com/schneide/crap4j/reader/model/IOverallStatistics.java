package com.schneide.crap4j.reader.model;

public interface IOverallStatistics {
	
	public String getName();
	
	public double getTotalCrap();
	
	public int getMethodCount();
	
	public int getCrapMethodCount();
	
	public double getCrapMethodPercent();
	
	public int getCrapLoad();
}
