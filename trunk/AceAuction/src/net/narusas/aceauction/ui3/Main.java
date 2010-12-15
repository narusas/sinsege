package net.narusas.aceauction.ui3;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import javax.swing.JFileChooser;

import net.narusas.aceauction.pdf.jpedal.등기부등본Parser;
import net.narusas.aceauction.pdf.jpedal.등기부등본_사항;

public class Main {
	static String dir = getDir();

	public static void main(String[] args) {

		final UI3 f = new UI3();

		f.fileChooseButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				fc.setCurrentDirectory(new File(dir));
				if (fc.showOpenDialog(f) != JFileChooser.APPROVE_OPTION) {
					return;
				}
				File file = fc.getSelectedFile();
				saveDir(file);
				String log = work(f.ownersTextField.getText(), file);
				f.logTextArea.setText(log);
			}

		});

		f.setSize(640, 480);
		f.setVisible(true);
	}

	private static String getDir() {
		return java.util.prefs.Preferences.userRoot().get("filechoose", ".");
	}

	private static void saveDir(File file) {
		dir = file.getAbsolutePath();
		java.util.prefs.Preferences.userRoot().put("filechoose", file.getAbsolutePath());
	}

	private static String[] splitOnwers(String owners) {
		if (owners == null) {
			return new String[] {};
		}
		return owners.split(",");
	}

	private static String work(String owners, File file) {
		등기부등본Parser parser = new 등기부등본Parser();
		List<등기부등본_사항> ok;
		try {
			String res = "";
			ok = parser.parseAll(file.getAbsolutePath(), splitOnwers(owners));
			for (등기부등본_사항 s : ok) {
				s.processRight();
				String format1 = "%-5s:%-5b:%-12s:%s:%s:%-20s:%s:%s:%s:  %s:\n";
				String format2 = "{\"%s\",\"%b\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\"},\n";
				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				pw.format(format2, (s.getStage() == 1 ? "갑" : "을") + s.getText(), s.willDelete(), s
						.getAcceptDate(), s.getFlagString(), s.flat(s.getPurpose()), s.flat(s
						.getBigo()),

				s.getRightPeoples(), s.getRightPrice(), s.flat(s.getBecause()), s.getRightPrice());
				res += sw.getBuffer().toString() + "\n";
			}
			return res;
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			return sw.getBuffer().toString();
		}

	}

}
