package com.schneide.crap4j.reader.model;

public interface IContextedCrapData<O> extends ICrapData {

	public O getContext();
}
