package net.narusas.si.auction.app.onbid;

public interface FetcherCallback {

	void setTotal(int total);

	void progress(int progress);

	void log(String msg);

}
