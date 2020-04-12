package com.yuri.commanderscompanion.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import dbAPI.AbstractTable;
import dbAPI.ConstraintsEnum;
import dbAPI.DatabaseCell;
import dbAPI.DatabaseDataType;
import dbAPI.DatabaseValue;
import dbAPI.IColumn;
import dbAPI.IDatabaseHelper;
import dbAPI.IDatabaseReader;
import dbAPI.IRow;
import dbAPI.ITable;

/**An abstract database table implementation
 * @param <T> The type of rows in the table
 */
public abstract class Table<T extends IRow> extends AbstractTable implements ITable {
	/**A list of all the rows in the table*/
	protected ArrayList<T> rows;
	/**A HashMap of rows with key as row's primary key*/
	protected HashMap<Object, T> rowsMap;
	/**The helper for the database*/
	protected IDatabaseHelper helper;
	
	/**Initialize this table with a helper, and an array of columns
	 * @param columns The columns in the table
	 * @param helper The helper for the database
	 */
	public Table(IColumn[] columns, IDatabaseHelper helper) {
		super(columns);
		rows = new ArrayList<T>();
		rowsMap = new HashMap<Object, T>();
		this.helper = helper;
		
		IDatabaseReader reader = helper.selectAll(getName());
		for (IRow row : reader) {
			addTFromIRow(row);
		}
	}
	
	/**Add a row to the table
	 * @param t the row to add
	 */
	public void add(T t) {
		insert(t);
	}
	
	/**Add an array of rows to the table
	 * @param ts The array of rows to add
	 */
	public void add(T...ts) {
		for (T t : ts) {
			insert(t);
		}
	}
	
	/**Add an iterable of rows to the table
	 * @param ts The iterable of rows
	 */
	public void add(Iterable<T> ts) {
		for (T t : ts) {
			insert(t);
		}
	}
	
	/**Get a row with a specified key
	 * @param key The primary key of the row
	 * @return A row with the given key
	 */
	public T getByPrimeryKey(DatabaseValue key) {
		return rowsMap.get(key.Value);
	}
	
	/**Update a row with a specified primary key to a new value
	 * @param where The key of the row to update
	 * @param newValue The value to update the row with
	 */
	public void update(DatabaseValue where, T newValue) {
		IColumn pk = getPrimeryKey();
		String wrapper = pk.getType() == DatabaseDataType.STRING ? "'" : "";
		String whereCondition = pk.getName() + " = " + wrapper + where.toString() + wrapper;
		update(newValue, whereCondition);
	}
	
	/**Get a row from the HashMap
	 * @param row The row in {@link IRow} form
	 * @return The row in {@link T} form
	 */
	protected T getTFromIRow(IRow row) {
		DatabaseValue key = row.getCell(getPrimeryKey()).Value;
		return rowsMap.get(key.Value);
	}
	
	/**Remove a row just from rows and rowsMap, not from database
	 * @param t The row to remove in {@link T} form
	 */
	protected void removeT(T t) {
		rows.remove(t);
		DatabaseValue key = t.getCell(getPrimeryKey()).Value;
		rowsMap.remove(key.Value);
	}
	
	/**Create a new object of class {@link T} from {@link IRow} and add it to rows and rowsMap only
	 * @param row The row to add
	 */
	protected abstract void addTFromIRow(IRow row);
	
	/**Get the primary key column of the table
	 * @return The column of the primary key
	 * @throws RuntimeException If no primary key in table. unreachable
	 */
	protected IColumn getPrimeryKey() {
		for (IColumn column : columns) {
			if (column.hasConstraint(ConstraintsEnum.PRIMERY_KEY)) {
				return column;
			}
		}
		//unreachable code
		throw new RuntimeException("Table " + getName() + " has no primery key");
	}
	
	// ---- AbstractTable implementation ----

	@Override
	public IRow getRow(int index) {
		return rows.get(index);
	}

	/**{@inheritDoc}
	 * @throws IllegalArgumentException If either the value isn't found in the column, or the column isn't in the table
	 */
	@Override
	public IRow getRow(IColumn keyCol, DatabaseValue keyValue)
throws IllegalArgumentException{
		if (keyCol == getPrimeryKey()) {
			T t = getByPrimeryKey(keyValue);
			if (t == null) {
				throw new IllegalArgumentException("No row found with value '" + keyValue +
						"' in column '" + keyCol.getName() + "' in the table '" + getName() + "'");
			}
			return t;
		}
		boolean found = false;
		for (IColumn c : getColumns()) {
			if (c == keyCol) {
				found = true;
				break;
			}
		}
		if (!found) {
			throw new IllegalArgumentException("The column '" +
				keyCol.getName() + "' isn't in the table '" + getName() + "'");
		}
		for (IRow row : rows) {
			if (row.getCell(keyCol).Value == keyValue) {
				return row;
			}
		}
		throw new IllegalArgumentException("No row found with value '" + keyValue + 
			"' in column '" + keyCol.getName() + "' in the table '" + getName() + "'");
	}

	@Override
	public void insert(IRow newRow) {
		String[] columns = new String[newRow.getCells().length];
		String[] values = new String[newRow.getCells().length];
		
		for (DatabaseCell cell : newRow) {
			columns[cell.getColumn().getIndex()] = cell.getColumn().getName();
			values[cell.getColumn().getIndex()] = cell.Value.toString();
		}
		
		helper.insert(getName(), columns, values);
		addTFromIRow(newRow);
	}

	@Override
	public void insert(IRow[] rows) {
		for (IRow row : rows) {
			insert(row);
		}
	}

	@Override
	public IDatabaseReader select(IColumn[] columns) {
		String[] columnstring = new String[columns.length];
		for(IColumn c : columns) {
			columnstring[c.getIndex()] = c.getName();
		}
		return helper.select(getName(), columnstring);
	}

	@Override
	public IDatabaseReader select(IColumn[] columns, String whereCondition) {
		String[] columnstring = new String[columns.length];
		for(IColumn c : columns) {
			columnstring[c.getIndex()] = c.getName();
		}
		return helper.select(getName(), columnstring, whereCondition);
	}

	@Override
	public IDatabaseReader selectAll() {
		return helper.selectAll(getName());
	}

	@Override
	public IDatabaseReader selectAll(String whereCondition) {
		return helper.selectAll(getName(), whereCondition);
	}

	@Override
	public void update(IRow columnValueList) {
		helper.update(getName(), columnValueList);
		
		for(IRow row : rows) {
			for(DatabaseCell c : columnValueList) {
				row.setValue(c.getColumn(), c.Value);
			}
		}
	}

	@Override
	public void update(IRow columnValueList, String whereCondition) {
		helper.update(getName(), columnValueList, whereCondition);
		
		IDatabaseReader reader = selectAll(whereCondition);
		while(reader.hasNext()) {
			IRow next = reader.next();
			T t = getTFromIRow(next);
			for(DatabaseCell c : columnValueList) {
				t.setValue(c.getColumn(), c.Value);
			}
		}
	}

	@Override
	public void delete(String whereCondition) {
		helper.delete(getName(), whereCondition);
		
		IDatabaseReader reader = selectAll(whereCondition);
		for (IRow row : reader) {
			removeT(getTFromIRow(row));
		}
	}
	
	// ---- Iterator implementation ----

	@Override
	public Iterator<IRow> iterator() {
		return new RowIterator(rows);
	}
	
	/**An iterator for rows in {@link T} form*/
	public class RowIterator implements Iterator<IRow>{
		/**An iterator of type {@link T}*/
		protected Iterator<T> iterator;
		
		/**Initialize the iterator with a type {@link T} row collection
		 * @param rows The rows in {@link T} form
		 */
		protected RowIterator(Collection<T> rows) {
			this.iterator = rows.iterator();
		}
		@Override
		public boolean hasNext() {
			return iterator.hasNext();
		}

		@Override
		public IRow next() {
			return iterator.next();
		}
	}
}
