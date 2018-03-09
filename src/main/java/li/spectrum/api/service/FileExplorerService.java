package li.spectrum.api.service;

import li.spectrum.api.exception.ApiServiceException;
import li.spectrum.data.model.FileCollection;

public interface FileExplorerService {

	FileCollection getFiles(String folder, Long start, Boolean includeHidden) throws ApiServiceException;

}
