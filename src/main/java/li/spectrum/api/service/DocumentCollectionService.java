package li.spectrum.api.service;

import li.spectrum.api.exception.ApiServiceException;
import li.spectrum.data.model.DocumentCollection;

public interface DocumentCollectionService {

	DocumentCollection getDocuments(String matchTerm, Long start, Boolean includeHidden) throws ApiServiceException;

}
