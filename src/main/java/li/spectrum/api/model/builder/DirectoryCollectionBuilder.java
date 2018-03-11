package li.spectrum.api.model.builder;

import java.util.ArrayList;
import java.util.List;

import com.marklogic.client.pojo.PojoPage;

import li.spectrum.api.model.Directory;
import li.spectrum.api.model.DirectoryCollection;
import li.spectrum.data.model.FileModel;
import li.spectrum.data.model.Folder;

public class DirectoryCollectionBuilder {

	private PojoPage<FileModel> fileModelPage;

	private DirectoryCollectionBuilder() {
		super();
	}

	public static DirectoryCollectionBuilder newBuilder() {
		return new DirectoryCollectionBuilder();
	}

	public DirectoryCollectionBuilder setFileModelPage(PojoPage<FileModel> fileModelPage) {
		this.fileModelPage = fileModelPage;
		return this;
	}

	public DirectoryCollection build() {
		DirectoryCollection directoryCollection = null;
		List<Directory> directoryList = new ArrayList<Directory>();
		if (fileModelPage != null && fileModelPage.hasContent()) {
			while (fileModelPage.hasNext()) {
				FileModel fm = fileModelPage.next();
				directoryList.add(Directory.of((Folder) fm.getFile()));
			}
			long start = fileModelPage.getStart();
			long pageSize = fileModelPage.getPageSize();
			long totalSize = fileModelPage.getTotalSize();
			directoryCollection = new DirectoryCollection(directoryList, start, pageSize, totalSize);

		}
		return directoryCollection;
	}
}