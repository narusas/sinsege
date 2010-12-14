package net.narusas.si.auction.app.build;

import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import net.narusas.si.auction.model.담당계;
import net.narusas.si.auction.model.물건;
import net.narusas.si.auction.model.법원;
import net.narusas.si.auction.model.사건;

public class EventNotifier {
	static int courtMax;
	static int courtProgress;
	static 법원 courtCurrent;

	static int chargeMax;
	static int chargeProgress;
	static 담당계 chargeCurrent;

	static int eventMax;
	static int eventProgress;
	static 사건 eventCurrent;

	static int goodsMax;
	static int goodsProgress;
	static 물건 goodsCurrent;
	static int 사진Max;
	static int 사진Progress;

	static int 업로드Max;
	static int 업로드Progress;

	private static JLabel chargeStatusLabel;

	private static JLabel chargeCurrentLabel;

	private static JLabel courtStatusLabel;

	private static JLabel courtCurrentLabel;

	private static JLabel eventStatusLabel;

	private static JLabel eventCurrentLabel;

	private static JLabel goodsStatusLabel;

	private static JLabel goodsCurrentLabel;
	private static JLabel picStatusLabel;
	private static JLabel uploadStatusLabel;

	public static void set담당계Size(int size) {
		chargeMax = size;
		updateUI();
	}

	public static void progress담당계(int i, 담당계 charge) {
		chargeProgress = i;
		chargeCurrent = charge;
		updateUI();
	}

	public static void set사건목록Size(int size) {
		eventMax = size;
		updateUI();
	}

	public static void progress사건(int i, 사건 사건) {
		eventProgress = i;
		eventCurrent = 사건;
		updateUI();
	}

	public static void progress물건(int i, 물건 goods) {
		goodsProgress = i;
		goodsCurrent = goods;
		updateUI();
	}

	public static void set물건목록Size(int size) {
		goodsMax = size;
		updateUI();
	}

	public static void set사진목록Size(int size) {
		사진Max = size;
		updateUI();
	}

	public static void progress사진목록Current(int v) {
		사진Progress = v;
		updateUI();
	}

	public static void set업로드Size(int size) {
		업로드Max = size;
		updateUI();
	}

	public static void progress업로드Current(int v) {
		업로드Progress = v;
		updateUI();
	}

	public static void set담당계StatusLabel(JLabel chargeStatusLabel) {
		EventNotifier.chargeStatusLabel = chargeStatusLabel;
	}

	public static void set담당계CurrentLabel(JLabel chargeCurrentLabel) {
		EventNotifier.chargeCurrentLabel = chargeCurrentLabel;

	}

	public static void set법원StatusLabel(JLabel courtStatus) {
		EventNotifier.courtStatusLabel = courtStatus;
	}

	public static void set법원CurrentLabel(JLabel courtCurrent) {
		EventNotifier.courtCurrentLabel = courtCurrent;
	}

	public static void set사건StatusLabel(JLabel eventStatus) {
		EventNotifier.eventStatusLabel = eventStatus;

	}

	public static void set사건CurrentLabel(JLabel eventCurrent) {
		EventNotifier.eventCurrentLabel = eventCurrent;
	}

	public static void set물건StatusLabel(JLabel goodsStatus) {
		EventNotifier.goodsStatusLabel = goodsStatus;
	}

	public static void set물건CurrentLabel(JLabel goodsCurrent) {
		EventNotifier.goodsCurrentLabel = goodsCurrent;

	}

	public static void set사진CurrentLabel(JLabel picCurrent) {
		EventNotifier.picStatusLabel = picCurrent;

	}

	private static void updateUI() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				setStatus(courtProgress, courtMax, courtCurrent, courtStatusLabel, courtCurrentLabel);
				setStatus(chargeProgress, chargeMax, chargeCurrent, chargeStatusLabel, chargeCurrentLabel);
				setStatus(eventProgress, eventMax, eventCurrent, eventStatusLabel, eventCurrentLabel);
				setStatus(goodsProgress, goodsMax, goodsCurrent, goodsStatusLabel, goodsCurrentLabel);
				setStatus(사진Progress, 사진Max, null, picStatusLabel, null);
				setStatus(업로드Progress, 업로드Max, null, uploadStatusLabel, null);
			}
		});

	}

	protected static void setStatus(int progress, int max, Object current, JLabel statusLabel,
			JLabel currentLabel) {
		setText(statusLabel, progress + "/" + max);
		if (current == null) {
			return;
		}
		setText(currentLabel, "(" + current + ")");

	}

	protected static void setText(JLabel label, Object msg) {
		if (label != null) {
			if (msg == null) {
				label.setText("");
			} else {
				label.setText(msg.toString());
			}
		}
	}

	public static void end사건() {
		eventProgress = eventMax;
		updateUI();
	}

	public static void end담당계() {
		chargeProgress = chargeMax;
		updateUI();
	}

	public static void set사진StatusLabel(JLabel componentByName) {
		picStatusLabel = componentByName;
	}

	public static void set업로드StatusLabel(JLabel label) {
		uploadStatusLabel = label;

	}

}
