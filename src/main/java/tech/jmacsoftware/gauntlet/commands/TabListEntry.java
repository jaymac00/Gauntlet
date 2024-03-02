package tech.jmacsoftware.gauntlet.commands;

import javax.annotation.Nullable;
import java.util.List;

public class TabListEntry {

	private String keyword;

	@Nullable
	private Integer intValue;

	@Nullable
	private Double doubleValue;

	@Nullable
	private String stringValue;

	private List<String> owners;

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public int getIntValue() {
		return intValue;
	}

	public void setOwners(List<String> owners) {
		this.owners = owners;
	}

	public double getDoubleValue() {
		return doubleValue;
	}

	public void setDoubleValue(double doubleValue) {
		this.doubleValue = doubleValue;
	}

	public String getStringValue() {
		return stringValue;
	}

	public void setStringValue(String stringValue) {
		this.stringValue = stringValue;
	}

	public void setIntValue(int intValue) {
		this.intValue = intValue;
	}

	public List<String> getOwners() {
		return owners;
	}
}
