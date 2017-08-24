package net.edge.net.host;

/**
 * An enumeration of possible host list types.
 * @author Artem Batutin <artembatutin@gmail.com>
 */
public enum HostListType {
	BANNED_MAC(0, "banned_macs"),
	BANNED_IP(1, "banned_ips"),
	MUTED_IP(2, "muted_ips"),
	STARTER_RECEIVED(3, "started_ips");

	/**
	 * The index in the list array.
	 */
	private final int index;

	/**
	 * The file name.
	 */
	private final String file;

	HostListType(int index, String file) {
		this.index = index;
		this.file = file;
	}

	public int getIndex() {
		return index;
	}

	public String getFile() {
		return file;
	}
}
