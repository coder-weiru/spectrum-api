package li.spectrum.api.service;

import li.spectrum.api.exception.ApiServiceException;
import li.spectrum.data.model.FolderCollection;

public interface FolderCollectionService {

	FolderCollection getAllFolders() throws ApiServiceException;

}
