package hu.sol.parser.bean;

public class Row {
	
	private Field field;
	private String description, UOM;
		
	public Row() {
	}

	public Row(Field field, String description, String UOM) {
		this.field = field;
		this.description = description;
		this.UOM = UOM;
	}
	
	public Row(Field field, String description) {
		this.field = field;
		this.description = description;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getUOM() {
		return UOM;
	}
	
	public void setUOM(String uOM) {
		UOM = uOM;
	}
	
	public Field getField() {
		return field;
	}
	
	public void setField(Field field) {
		this.field = field;
	}
	
	@Override
	public String toString() {
		StringBuilder strBuilder = new StringBuilder(field.toString()).append("\n\tleiras: ").append(description);
		
		if(UOM != null) {
			strBuilder.append("\n\tUOM: ").append(UOM);
		}
		
		return strBuilder.toString();
	}
}