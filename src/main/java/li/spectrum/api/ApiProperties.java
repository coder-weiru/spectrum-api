package li.spectrum.api;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "api")
public class ApiProperties {

	public static class Ignore {
		private List<String> folders;

		public List<String> getFolders() {
			return folders;
		}

		public void setFolders(List<String> folders) {
			this.folders = folders;
		}
		
	}
	
	public static class Search {
		private boolean caseSensitive;
		private int pageSize;
		private boolean includeHiddenFolder;
		private boolean includeHiddenFile;
		private boolean includeFolderOfHiddenFolder;
		private boolean includeFileOfHiddenFolder;
		private Ignore ignore;
		
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

		public boolean isIncludeHiddenFolder() {
			return includeHiddenFolder;
		}

		public void setIncludeHiddenFolder(boolean includeHiddenFolder) {
			this.includeHiddenFolder = includeHiddenFolder;
		}

		public boolean isIncludeHiddenFile() {
			return includeHiddenFile;
		}

		public void setIncludeHiddenFile(boolean includeHiddenFile) {
			this.includeHiddenFile = includeHiddenFile;
		}

		public boolean isIncludeFolderOfHiddenFolder() {
			return includeFolderOfHiddenFolder;
		}

		public void setIncludeFolderOfHiddenFolder(boolean includeFolderOfHiddenFolder) {
			this.includeFolderOfHiddenFolder = includeFolderOfHiddenFolder;
		}

		public boolean isIncludeFileOfHiddenFolder() {
			return includeFileOfHiddenFolder;
		}

		public void setIncludeFileOfHiddenFolder(boolean includeFileOfHiddenFolder) {
			this.includeFileOfHiddenFolder = includeFileOfHiddenFolder;
		}

		public Ignore getIgnore() {
			return ignore;
		}

		public void setIgnore(Ignore ignore) {
			this.ignore = ignore;
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
