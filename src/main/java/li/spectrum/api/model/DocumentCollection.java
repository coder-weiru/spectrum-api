package li.spectrum.api.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DocumentCollection extends BasicPage<Document> {

	private List<Document> documents = new ArrayList<Document>();

	public static DocumentCollection emptyCollection() {
		return new DocumentCollection(new ArrayList<Document>(), 0, 0, 0);
	}

	public DocumentCollection(List<Document> list, long start, long pageSize, long totalSize) {
		this(list.iterator(), start, pageSize, totalSize);
		this.documents = list;
	}

	public DocumentCollection(Iterator<Document> iterator, long start, long pageSize, long totalSize) {
		super(iterator, start, pageSize, totalSize);
		
	}

	public List<Document> getDocument() {
		return documents;
	}

	public void setDocument(List<Document> documents) {
		this.documents = documents;
	}

	@Override
	public boolean hasNext() {
		return super.hasNext();
	}

	@Override
	public Document next() {
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
