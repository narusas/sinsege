package net.narusas.aceauction.pdf;

import java.util.LinkedList;
import java.util.List;

import org.pdfbox.util.TextPosition;

public class EntityParser {
	TextPosition privious = null;

	TextPosition start = null;

	String workingValue = "";

	public List<Entity> parseValues(int pageNo, List<TextPosition> values) {
		List<Entity> res = new LinkedList<Entity>();
		resetWorkingValue();
		for (TextPosition now : values) {
			// System.out.println(now.getCharacter()+":"+now.getY());
			if (privious == null) {
				setupWorkingValue(now);
			} else if (privious != null && now.getY() > privious.getY()
					&& (Math.abs(now.getY() - privious.getY()) < 20)) {
				workingValue += now.getCharacter() + "\n";
			} else if (privious != null && now.getY() == privious.getY()) {
				workingValue += now.getCharacter() + ' ';
			} else {
				addEntity(pageNo, res);
				setupWorkingValue(now);
			}
			privious = now;
		}
		if (!"".equals(workingValue.trim())) {
			addEntity(pageNo, res);
		}
		return res;
	}

	private void addEntity(int pageNo, List<Entity> res) {
		res.add(new Entity(workingValue.trim(), pageNo, start.getY(), start.getX(), start
				.getWidth()));
	}

	private void resetWorkingValue() {
		setWorkingValue("");
	}

	private void setupWorkingValue(TextPosition now) {
		setWorkingValue(now.getCharacter() + "\n");
		start = now;
	}

	void setWorkingValue(String text) {
		workingValue = text;
		privious = null;
	}
}
