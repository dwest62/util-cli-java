import java.util.ArrayList;
import java.util.List;

public class Table<T> {
	private RowMapper<T> rowMapper;
	private final List<T> entries;
	private final List<Column> columns;
	
	public Table(RowMapper<T> rowMapper) {
		this(rowMapper, new ArrayList<>(), new ArrayList<>());
	}
	
	public Table(RowMapper<T> rowMapper, List<T> entries, List<Column> columns) {
		this.rowMapper = rowMapper;
		this.entries = entries;
		this.columns = columns;
	}
	
	
	public RowMapper<T> getRowMapper() {
		return rowMapper;
	}
	
	public void setRowMapper(RowMapper<T> rowMapper) {
		this.rowMapper = rowMapper;
	}
	
	public List<T> getEntries() {
		return entries;
	}
	
	public List<Column> getColumns() {
		return columns;
	}
	
	public Table<T> addEntry(T entry) {
		entries.add(entry);
		return this;
	}
	
	public Table<T> addEntry(T entry, int rowNumber) {
		entries.add(rowNumber - 1, entry);
		return this;
	}
	
	public Table<T> addEntries(List<T> entries) {
		this.entries.addAll(entries);
		return this;
	}
	
	public Table<T> addColumn(Column column) {
		columns.add(column);
		return this;
	}
	
	public Table<T> addColumn(Column column, int colNumber) {
		this.columns.add(colNumber - 1, column);
		return this;
	}
	
	public Table<T> addColumns(List<Column> columns) {
		this.columns.addAll(columns);
		return this;
	}
	
	public static class Column {
		public enum Alignment {
			LEFT,
			RIGHT,
			CENTER
		}
		
		private String header;
		
		private int width;
		
		private Alignment alignment;
		
		public Alignment getAlignment() {
			return alignment;
		}
		
		public Column(String header, int width) {
			this.header = header;
			this.width = width;
		}
		
		public Column(String header, int width, Alignment alignment) {
			this.header = header;
			this.width = width;
			this.alignment = alignment;
		}
		
		public void setAlignment(Alignment alignment) {
			this.alignment = alignment;
		}
		
		
		public String getHeader() {
			return header;
		}
		
		public void setHeader(String header) {
			this.header = header;
		}
		
		public int getWidth() {
			return width;
		}
		
		public void setWidth(int width) {
			this.width = width;
		}
		
	}
	
}

