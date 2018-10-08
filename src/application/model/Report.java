package application.model;

import java.io.File;

public class Report {
	private int id;
	private String name;
	private File file;
	
	public Report() {
		id=0;
		name="";
		file=null;
	}
	
	public Report(int id, String name, File file) {
		super();
		this.id=id;
		this.name=name;
		this.file=file;
	}
	
	//Setter methods
	public void setId(int id) {
		this.id = id;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setFile(File file) {
		this.file = file;
	}
	
	//Getter methods
	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public File getFile() {
		return file;
	}
}
