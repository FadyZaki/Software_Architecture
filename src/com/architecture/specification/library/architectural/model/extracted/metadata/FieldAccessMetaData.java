package com.architecture.specification.library.architectural.model.extracted.metadata;

public class FieldAccessMetaData {

	private String fieldIdentifier;
	private String fieldDeclaringClass;
	private String fieldType;
	private boolean isRead;
	private boolean isWritten;

	public FieldAccessMetaData(String fieldIdentifier, String fieldDeclaringClass, String fieldType) {
		this.fieldIdentifier = fieldIdentifier;
		this.fieldDeclaringClass = fieldDeclaringClass;
		this.fieldType = fieldType;
		this.isRead = false;
		this.isWritten = false;
	}

	public String getFieldIdentifier() {
		return fieldIdentifier;
	}

	public String getFieldDeclaringClass() {
		return fieldDeclaringClass;
	}

	public String getFieldType() {
		return fieldType;
	}

	public boolean isRead() {
		return isRead;
	}

	public boolean isWritten() {
		return isWritten;
	}
	
	public void setRead(boolean isRead) {
		this.isRead = isRead;
	}
	
	public void setWritten(boolean isWritten) {
		this.isWritten = isWritten;
	}
	
	public int hashCode() {
		return (this.getFieldIdentifier() + this.getFieldDeclaringClass() + this.getFieldType()).hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof FieldAccessMetaData))
			return false;
		if (obj == this)
			return true;

		FieldAccessMetaData fmd = (FieldAccessMetaData) obj;
		return this.getFieldIdentifier().equals(fmd.getFieldIdentifier()) && this.getFieldDeclaringClass().equals(fmd.getFieldDeclaringClass())
				&& this.getFieldType().equals(fmd.getFieldType());
	}

}
