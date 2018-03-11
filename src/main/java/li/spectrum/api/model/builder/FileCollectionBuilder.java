package li.spectrum.api.model.builder;

import java.util.ArrayList;
import java.util.List;

import com.marklogic.client.pojo.PojoPage;

import li.spectrum.api.model.Directory;
import li.spectrum.api.model.Document;
import li.spectrum.api.model.FileCollection;
import li.spectrum.api.model.Resource;
import li.spectrum.data.model.File;
import li.spectrum.data.model.FileModel;
import li.spectrum.data.model.Folder;

public class FileCollectionBuilder {

	private PojoPage<FileModel> fileModelPage;

	private FileCollectionBuilder() {
		super();
	}

	public static FileCollectionBuilder newBuilder() {
		return new FileCollectionBuilder();
	}

	public FileCollectionBuilder setFileModelPage(PojoPage<FileModel> fileModelPage) {
		this.fileModelPage = fileModelPage;
		return this;
	}

	public FileCollection build() {
		FileCollection fileCollection = null;
		List<Resource> fileList = new ArrayList<Resource>();
		if (fileModelPage != null && fileModelPage.hasContent()) {
			while (fileModelPage.hasNext()) {
				FileModel fm = fileModelPage.next();
				File f = (File) fm.getFile();
				if (File.class.getSimpleName().equals(f.getType())) {
					Document doc = Document.of((File) fm.getFile());
					fileList.add(doc);
				} else {
					Directory dir = Directory.of((Folder) fm.getFile());
					fileList.add(dir);
				}
			}
			long start = fileModelPage.getStart();
			long pageSize = fileModelPage.getPageSize();
			long totalSize = fileModelPage.getTotalSize();
			fileCollection = new FileCollection(fileList, start, pageSize, totalSize);

		}
		return fileCollection;
	}
}