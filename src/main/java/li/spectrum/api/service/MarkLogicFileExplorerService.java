package li.spectrum.api.service;

import java.util.List;

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
import li.spectrum.data.model.FileCollection;
import li.spectrum.data.model.FileModel;
import li.spectrum.data.model.builder.FileCollectionBuilder;

@Service
public class MarkLogicFileExplorerService implements FileExplorerService {
	private static final Logger logger = LoggerFactory.getLogger(MarkLogicFileExplorerService.class);

	@Autowired
	private ApiProperties apiProperties;

	@Autowired
	private PojoRepository<FileModel, String> repository;

	@Autowired
	public MarkLogicFileExplorerService(PojoRepository<FileModel, String> repository) {
		super();
		Assert.notNull(repository, "'PojoRepository<FileModel, String>' must not be null");
		this.repository = repository;
	}

	@Override
	public FileCollection getFiles(String folder, Long start, Boolean includeHidden)
			throws ApiServiceException {
		PojoQueryBuilder<FileModel> qb = repository.getQueryBuilder();

		// File path matches anything directly under specified folder
		StructuredQueryDefinition query = qb.value("parentPath", folder);

		// Exclude files that match specified ignore patterns
		List<String> ignoreFiles = apiProperties.getSearch().getIgnore().getFiles();
		if (!ignoreFiles.isEmpty()) {
			String[] files = new String[ignoreFiles.size()];
			files = ignoreFiles.toArray(files);
			query = qb.andNot(query, qb.value("name", files));
		}
		
		// Exclude hidden files
		if (includeHidden == null) {
			if (!apiProperties.getSearch().isIncludeHiddenFiles()) {
				query = qb.and(query, qb.containerQuery("file", qb.value("hidden", Boolean.FALSE)));
			}
			if (!apiProperties.getSearch().isIncludeHiddenChildFiles()) {
				query = qb.and(query, qb.containerQuery("file", qb.value("parentHidden", Boolean.FALSE)));
			}
		} else {
			if (!includeHidden) {
				query = qb.and(query, qb.containerQuery("file", qb.value("hidden", Boolean.FALSE)));
				if (!apiProperties.getSearch().isIncludeHiddenChildFiles()) {
					query = qb.and(query, qb.containerQuery("file", qb.value("parentHidden", Boolean.FALSE)));
				}
			}
		}

		repository.setPageLength(apiProperties.getSearch().getPageSize());

		PojoPage<FileModel> matches = repository.search(query, start);

		logger.debug("Results " + start + " thru " + (start + matches.size() - 1));

		FileCollection fc = FileCollection.emptyCollection();
		if (matches.hasContent()) {
			fc = FileCollectionBuilder.newBuilder().setFileModelPage(matches).build();
		} else {
			logger.debug("Empty folder %s", folder);
		}
		return fc;
	}

}
