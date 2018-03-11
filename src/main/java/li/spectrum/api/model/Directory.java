package li.spectrum.api.model;

import li.spectrum.data.model.Folder;

public class Directory extends Resource {

	private Folder folder;

	public Directory() {
		super();
	}

	public Folder getFolder() {
		return folder;
	}

	public void setFolder(Folder folder) {
		this.folder = folder;
	}

	public static Directory of(Folder folder) {
		Directory doc = new Directory();
		doc.setFolder(folder);
		return doc;
	}
}
