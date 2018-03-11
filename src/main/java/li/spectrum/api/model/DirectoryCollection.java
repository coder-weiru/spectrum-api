package li.spectrum.api.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DirectoryCollection extends BasicPage<Directory> {

	private List<Directory> directories = new ArrayList<Directory>();

	public static DirectoryCollection emptyCollection() {
		return new DirectoryCollection(new ArrayList<Directory>(), 0, 0, 0);
	}

	public DirectoryCollection(List<Directory> list, long start, long pageSize, long totalSize) {
		this(list.iterator(), start, pageSize, totalSize);
		this.directories = list;
	}

	public DirectoryCollection(Iterator<Directory> iterator, long start, long pageSize, long totalSize) {
		super(iterator, start, pageSize, totalSize);
		
	}

	public List<Directory> getDirectories() {
		return directories;
	}

	public void setDirectories(List<Directory> directories) {
		this.directories = directories;
	}

	@Override
	public boolean hasNext() {
		return super.hasNext();
	}

	@Override
	public Directory next() {
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
