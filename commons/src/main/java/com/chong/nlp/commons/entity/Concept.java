package com.chong.nlp.commons.entity;

import java.util.List;

public class Concept {
	private String name;
	private String desc;
	private List<String> alias;
	private Concept parentConcept;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public List<String> getAlias() {
		return alias;
	}
	public void setAlias(List<String> alias) {
		this.alias = alias;
	}
	public Concept getParentConcept() {
		return parentConcept;
	}
	public void setParentConcept(Concept parentConcept) {
		this.parentConcept = parentConcept;
	}
}
