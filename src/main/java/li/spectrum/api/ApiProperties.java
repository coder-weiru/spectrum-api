package li.spectrum.api;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "api")
public class ApiProperties {

	public static class Ignore {
		private List<String> folders;
		private List<String> files;

		public List<String> getFolders() {
			return folders;
		}

		public void setFolders(List<String> folders) {
			this.folders = folders;
		}

		public List<String> getFiles() {
			return files;
		}

		public void setFiles(List<String> files) {
			this.files = files;
		}
	}
	
	public static class Hidden {
		private boolean folders;
		private boolean files;
		private boolean childFolders;
		private boolean childFiles;

		public boolean isFolders() {
			return folders;
		}

		public void setFolders(boolean folders) {
			this.folders = folders;
		}

		public boolean isFiles() {
			return files;
		}

		public void setFiles(boolean files) {
			this.files = files;
		}

		public boolean isChildFolders() {
			return childFolders;
		}

		public void setChildFolders(boolean childFolders) {
			this.childFolders = childFolders;
		}

		public boolean isChildFiles() {
			return childFiles;
		}

		public void setChildFiles(boolean childFiles) {
			this.childFiles = childFiles;
		}

	}

	public static class Include {
		private Hidden hidden;

		public Hidden getHidden() {
			return hidden;
		}

		public void setHidden(Hidden hidden) {
			this.hidden = hidden;
		}
	}

	public static class Search {
		private boolean caseSensitive;
		private int pageSize;
		private Ignore ignore;
		private Include include;
		
		public boolean isCaseSensitive() {
			return caseSensitive;
		}

		public void setCaseSensitive(boolean caseSensitive) {
			this.caseSensitive = caseSensitive;
		}

		public int getPageSize() {
			return pageSize;
		}

		public void setPageSize(int pageSize) {
			this.pageSize = pageSize;
		}

		public Include getInclude() {
			return include;
		}

		public void setInclude(Include include) {
			this.include = include;
		}

		public Ignore getIgnore() {
			return ignore;
		}

		public void setIgnore(Ignore ignore) {
			this.ignore = ignore;
		}

		public boolean isIncludeHiddenFiles() {
			return getInclude().getHidden().isFiles();
		}

		public boolean isIncludeHiddenFolders() {
			return getInclude().getHidden().isFolders();
		}

		public boolean isIncludeHiddenChildFiles() {
			return getInclude().getHidden().isChildFiles();
		}

		public boolean isIncludeHiddenChildFolders() {
			return getInclude().getHidden().isChildFolders();
		}
	}

	private Search search;

	public Search getSearch() {
		return search;
	}

	public void setSearch(Search search) {
		this.search = search;
	}

}
