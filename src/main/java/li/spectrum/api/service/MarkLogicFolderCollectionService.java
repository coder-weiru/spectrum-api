package li.spectrum.api.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.marklogic.client.pojo.PojoPage;
import com.marklogic.client.pojo.PojoQueryBuilder;
import com.marklogic.client.pojo.PojoQueryDefinition;
import com.marklogic.client.pojo.PojoRepository;

import li.spectrum.api.ApiProperties;
import li.spectrum.api.exception.ApiServiceException;
import li.spectrum.data.model.FileModel;
import li.spectrum.data.model.Folder;
import li.spectrum.data.model.FolderCollection;

@Service
public class MarkLogicFolderCollectionService implements FolderCollectionService {
	private static final Logger logger = LoggerFactory.getLogger(MarkLogicFolderCollectionService.class);

	@Autowired
	private ApiProperties apiProperties;

	@Autowired
	private PojoRepository<FileModel, String> repository;

	@Autowired
	public MarkLogicFolderCollectionService(PojoRepository<FileModel, String> repository) {
		super();
		Assert.notNull(repository, "'PojoRepository<FileModel, String>' must not be null");
		this.repository = repository;
	}

	@Override
	public FolderCollection getAllFolders() throws ApiServiceException {
		PojoQueryBuilder<FileModel> qb = repository.getQueryBuilder();

		PojoQueryDefinition query = qb.containerQuery("file",
				qb.containerQuery("_metadata", qb.value("type", Folder.class.getSimpleName())));
		repository.setPageLength(apiProperties.getSearch().getPageSize());

		FolderCollection folderCollection = new FolderCollection();
		PojoPage<FileModel> matches;
		int start = 1;
		do {
			matches = repository.search(query, start);
			logger.debug("Results " + start + " thru " + (start + matches.size() - 1));

			if (matches.hasContent()) {
				while (matches.hasNext()) {
					FileModel fm = matches.next();
					folderCollection.add((Folder) fm.getFile());
				}
			} else {
				logger.debug("  No matches");
			}

			start += matches.size();

		} while (matches.hasNextPage());

		return folderCollection;
	}

}
