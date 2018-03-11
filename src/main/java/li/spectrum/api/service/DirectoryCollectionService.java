package li.spectrum.api.service;

import li.spectrum.api.exception.ApiServiceException;
import li.spectrum.api.model.DirectoryCollection;

public interface DirectoryCollectionService {

	DirectoryCollection getFolders(String matchTerm, Long start, Boolean includeHidden) throws ApiServiceException;

}
