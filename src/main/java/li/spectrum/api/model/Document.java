package li.spectrum.api.model;

import li.spectrum.data.model.File;

public class Document extends Resource {

	private File file;

	public Document() {
		super();
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public static Document of(File file) {
		Document doc = new Document();
		doc.setFile(file);
		return doc;
	}
}
