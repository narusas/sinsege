/*
 * 
 */
package net.narusas.aceauction.pdf;

import java.util.LinkedList;
import java.util.List;

import org.pdfbox.util.TextPosition;

// TODO: Auto-generated Javadoc
/**
 * The Class EntityParser.
 */
public class EntityParser {
	
	/** The privious. */
	TextPosition privious = null;

	/** The start. */
	TextPosition start = null;

	/** The working value. */
	String workingValue = "";

	/**
	 * Parses the values.
	 * 
	 * @param pageNo the page no
	 * @param values the values
	 * 
	 * @return the list< entity>
	 */
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

	/**
	 * Adds the entity.
	 * 
	 * @param pageNo the page no
	 * @param res the res
	 */
	private void addEntity(int pageNo, List<Entity> res) {
		res.add(new Entity(workingValue.trim(), pageNo, start.getY(), start.getX(), start
				.getWidth()));
	}

	/**
	 * Reset working value.
	 */
	private void resetWorkingValue() {
		setWorkingValue("");
	}

	/**
	 * Sets the up working value.
	 * 
	 * @param now the new up working value
	 */
	private void setupWorkingValue(TextPosition now) {
		setWorkingValue(now.getCharacter() + "\n");
		start = now;
	}

	/**
	 * Sets the working value.
	 * 
	 * @param text the new working value
	 */
	void setWorkingValue(String text) {
		workingValue = text;
		privious = null;
	}
}
