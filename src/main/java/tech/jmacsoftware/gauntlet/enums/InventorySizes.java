package tech.jmacsoftware.gauntlet.enums;

public enum InventorySizes {

	GRAVE(45);

	private int size;

	InventorySizes(int size) {
		this.size = size;
	}

	public int getSize() {
		return this.size;
	}
}
