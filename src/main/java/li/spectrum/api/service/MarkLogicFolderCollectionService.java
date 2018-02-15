package li.spectrum.api.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.marklogic.client.pojo.PojoPage;
import com.marklogic.client.pojo.PojoQueryBuilder;
import com.marklogic.client.pojo.PojoRepository;
import com.marklogic.client.query.StructuredQueryDefinition;

import li.spectrum.api.ApiProperties;
import li.spectrum.api.exception.ApiServiceException;
import li.spectrum.data.model.FileModel;
import li.spectrum.data.model.Folder;
import li.spectrum.data.model.FolderCollection;
import li.spectrum.data.model.builder.FolderCollectionBuilder;

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
	public FolderCollection getAllFolders(Long start, Boolean includeHidden) throws ApiServiceException {
		PojoQueryBuilder<FileModel> qb = repository.getQueryBuilder();

		StructuredQueryDefinition query = qb.containerQuery("file",
				qb.containerQuery("_metadata", qb.value("type", Folder.class.getSimpleName())));

		if (includeHidden == null) {
			if (!apiProperties.getSearch().isIncludeHiddenFolder()) {
				query = qb.and(query, qb.containerQuery("file", qb.value("hidden", Boolean.FALSE)));
			}
			if (!apiProperties.getSearch().isIncludeFolderOfHiddenFolder()) {
				query = qb.and(query, qb.containerQuery("file", qb.value("parentHidden", Boolean.FALSE)));
			}
		} else {
			if (!includeHidden) {
				query = qb.and(query, qb.containerQuery("file", qb.value("hidden", Boolean.FALSE)));
				if (!apiProperties.getSearch().isIncludeFolderOfHiddenFolder()) {
					query = qb.and(query, qb.containerQuery("file", qb.value("parentHidden", Boolean.FALSE)));
				}
			}
		}

		repository.setPageLength(apiProperties.getSearch().getPageSize());

		PojoPage<FileModel> matches = repository.search(query, start);

		logger.debug("Results " + start + " thru " + (start + matches.size() - 1));

		FolderCollection fc = null;
		if (matches.hasContent()) {
			FolderCollectionBuilder fcb = new FolderCollectionBuilder();
			fcb.setFileModelPaGE(matches);
			fc = fcb.build();
		} else {
			logger.debug("  No matches");
		}
		return fc;
	}

}
