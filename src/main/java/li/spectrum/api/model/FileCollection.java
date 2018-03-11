package li.spectrum.api.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FileCollection extends BasicPage<Resource> {

	private List<Resource> files = new ArrayList<Resource>();

	public static FileCollection emptyCollection() {
		return new FileCollection(new ArrayList<Resource>(), 0, 0, 0);
	}

	public FileCollection(List<Resource> list, long start, long pageSize, long totalSize) {
		this(list.iterator(), start, pageSize, totalSize);
		this.files = list;
	}

	public FileCollection(Iterator<Resource> iterator, long start, long pageSize, long totalSize) {
		super(iterator, start, pageSize, totalSize);
		
	}

	public List<Resource> getFiles() {
		return files;
	}

	public void setFiles(List<Resource> files) {
		this.files = files;
	}

	@Override
	public boolean hasNext() {
		return super.hasNext();
	}

	@Override
	public Resource next() {
		return super.next();
	}

	@Override
	public long getStart() {
		return super.getStart();
	}

	@Override
	public long getPageSize() {
		return super.getPageSize();
	}

	@Override
	public long getTotalSize() {
		return super.getTotalSize();
	}

	@Override
	public long getTotalPages() {
		return super.getTotalPages();
	}

	@Override
	public boolean hasContent() {
		return super.hasContent();
	}

	@Override
	public boolean hasNextPage() {
		return super.hasNextPage();
	}

	@Override
	public boolean hasPreviousPage() {
		return super.hasPreviousPage();
	}

	@Override
	public long getPageNumber() {
		return super.getPageNumber();
	}

	@Override
	public boolean isFirstPage() {
		return super.isFirstPage();
	}

	@Override
	public boolean isLastPage() {
		return super.isLastPage();
	}

	@Override
	public long size() {
		return super.size();
	}

}
