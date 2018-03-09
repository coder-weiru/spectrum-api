package li.spectrum.api.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

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
	public FolderCollection getFolders(String matchTerm, Long start, Boolean includeHidden) throws ApiServiceException {
		PojoQueryBuilder<FileModel> qb = repository.getQueryBuilder();

		// File type is 'Folder'
		StructuredQueryDefinition query = qb.containerQuery("file",
				qb.containerQuery("_metadata", qb.value("type", Folder.class.getSimpleName())));

		// If term is specified, matching term in the path
		if (!StringUtils.isEmpty(matchTerm)) {
			query = qb.and(query, qb.term("path", matchTerm));
		}

		// Exclude folders or their parent folders that match specified ignore folders
		List<String> ignoreFolders = apiProperties.getSearch().getIgnore().getFolders();
		if (!ignoreFolders.isEmpty()) {
			String[] flds = new String[ignoreFolders.size()];
			flds = ignoreFolders.toArray(flds);
            
			query = qb.andNot(query, qb.value("name", flds));	
			// parent folders that match ignore folders should be excluded as well
			query = qb.andNot(query, qb.word("parentFolders", flds));
		}
		
		// Exclude hidden folders
		if (includeHidden == null) {
			if (!apiProperties.getSearch().isIncludeHiddenFolders()) {
				query = qb.and(query, qb.containerQuery("file", qb.value("hidden", Boolean.FALSE)));
			}
			if (!apiProperties.getSearch().isIncludeHiddenChildFolders()) {
				query = qb.and(query, qb.containerQuery("file", qb.value("parentHidden", Boolean.FALSE)));
			}
		} else {
			if (!includeHidden) {
				query = qb.and(query, qb.containerQuery("file", qb.value("hidden", Boolean.FALSE)));
				if (!apiProperties.getSearch().isIncludeHiddenChildFolders()) {
					query = qb.and(query, qb.containerQuery("file", qb.value("parentHidden", Boolean.FALSE)));
				}
			}
		}

		repository.setPageLength(apiProperties.getSearch().getPageSize());

		PojoPage<FileModel> matches = repository.search(query, start);

		logger.debug("Results " + start + " thru " + (start + matches.size() - 1));

		FolderCollection fc = FolderCollection.emptyCollection();
		if (matches.hasContent()) {
			fc = FolderCollectionBuilder.newBuilder().setFileModelPage(matches).build();
		} else {
			logger.debug("No matches.");
		}
		return fc;
	}

}
