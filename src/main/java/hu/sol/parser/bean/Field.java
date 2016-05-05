package hu.sol.parser.bean;

public class Field {
	private String fieldName, dataType;
	
	public Field() {
	}	
	
	public Field(String fieldName) {
		this.fieldName = fieldName;
	}
	
	public Field(String fieldName, String dataType) {
		this.fieldName = fieldName;
		this.dataType = dataType;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	
	@Override
	public String toString() {
		StringBuilder strBuilder = new StringBuilder(fieldName).append(": ").append(dataType);
		
		return strBuilder.toString();
	}
}