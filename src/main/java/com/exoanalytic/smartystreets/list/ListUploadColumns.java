package com.exoanalytic.smartystreets.list;

/**
 * Columns that SmartyStreets understands when submitting via the List Upload API.
 * @author jmink
 *
 */
public enum ListUploadColumns {
	Id,
	Street1,
	Street2,
	Unit,
	City,
	State,
	ZIPCode,
	CityStateZip,
	FullName,
	FirstName,
	LastName,
	Organization;

	/**
	 * Returns the field as it appears in the result files.
	 * @return
	 */
	public String inResult() {
		return "[" + this + "]";
	}
}
