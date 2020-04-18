package com.yuri.commanderscompanion.api;

import dbAPI.DatabaseCell;
import dbAPI.IRow;

/**Represents a medical note type and a row in a table*/
public class MedicalNote extends NoteType {

	/**Initialize a new medical note type with the given cells
	 * @param cells The cells of the row
	 */
	protected MedicalNote(DatabaseCell... cells) { super(cells); }

	/**Initialize a new medical note type for a given owner
	 * @param owner The owner
	 */
	public MedicalNote(Soldier owner) {
		super(owner, "Medical");
	}

	/**Initialize a new medical note type from a row
	 * @param row The row
	 */
	public MedicalNote(IRow row) {
		super(row);
	}
}
