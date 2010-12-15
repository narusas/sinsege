package net.narusas.aceauction.data.updater;

public interface UpdaterListener {

	void log(String msg);

	void progress(int progress);

	void updateWorkSize(int size);

}
