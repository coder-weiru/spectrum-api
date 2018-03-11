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
import li.spectrum.api.model.DocumentCollection;
import li.spectrum.api.model.builder.DocumentCollectionBuilder;
import li.spectrum.data.model.File;
import li.spectrum.data.model.FileModel;

@Service
public class MarkLogicDocumentCollectionService implements DocumentCollectionService {
	private static final Logger logger = LoggerFactory.getLogger(MarkLogicDocumentCollectionService.class);

	@Autowired
	private ApiProperties apiProperties;

	@Autowired
	private PojoRepository<FileModel, String> repository;

	@Autowired
	public MarkLogicDocumentCollectionService(PojoRepository<FileModel, String> repository) {
		super();
		Assert.notNull(repository, "'PojoRepository<FileModel, String>' must not be null");
		this.repository = repository;
	}

	@Override
	public DocumentCollection getDocuments(String matchTerm, Long start, Boolean includeHidden)
			throws ApiServiceException {
		PojoQueryBuilder<FileModel> qb = repository.getQueryBuilder();

		// File type is 'File'
		StructuredQueryDefinition query = qb.containerQuery("file", qb.value("type", File.class.getSimpleName()));

		// If term is specified, matching term in the path
		if (!StringUtils.isEmpty(matchTerm)) {
			query = qb.and(query, qb.term("name", matchTerm));
		}

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

		DocumentCollection dc = DocumentCollection.emptyCollection();
		if (matches.hasContent()) {
			dc = DocumentCollectionBuilder.newBuilder().setFileModelPage(matches).build();
		} else {
			logger.debug("No matches.");
		}
		return dc;
	}

}
