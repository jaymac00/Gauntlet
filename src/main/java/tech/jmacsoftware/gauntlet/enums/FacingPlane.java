package tech.jmacsoftware.gauntlet.enums;

public enum FacingPlane {
	XY_PLANE("XY"),
	YZ_PLANE("YZ"),
	XZ_PLANE("XZ");

	private String plane;

	FacingPlane(String plane) {
		this.plane = plane;
	}

	public static FacingPlane resolveByPitchAndYaw(float pitch, float yaw) {
		if (pitch < -47.5 || pitch > 47.5) {
			return XZ_PLANE;
		} else if (yaw < -135.0 || (yaw > -45.0 && yaw < 45.0) || yaw > 135.0) {
			return XY_PLANE;
		} else {
			return YZ_PLANE;
		}
	}

	public static FacingPlane resolveByYaw(float yaw) {
		if (yaw < -135.0 || (yaw > -45.0 && yaw < 45.0) || yaw > 135.0) {
			return XY_PLANE;
		} else {
			return YZ_PLANE;
		}
	}
}
