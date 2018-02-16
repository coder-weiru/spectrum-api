package li.spectrum.api.controller;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import li.spectrum.api.exception.ApiServiceException;
import li.spectrum.api.service.FolderCollectionService;
import li.spectrum.data.model.FolderCollection;

@RestController
@Api(value = "Folder Collection API", produces = "application/hal+json")
@RequestMapping("/folders")
public class FolderCollectionController {

	private static final Logger LOGGER = LoggerFactory.getLogger(FolderCollectionController.class);

	private FolderCollectionService folderCollectionService;

	@Autowired
	public FolderCollectionController(FolderCollectionService folderCollectionService) {
		Assert.notNull(folderCollectionService, "'folderCollectionService' must not be null");
		this.folderCollectionService = folderCollectionService;
	}

	@GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Returns all the folders.", notes = "Returns all the folders")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success", response = FolderCollection.class),
			@ApiResponse(code = 400, message = "Input Validation Error"),
			@ApiResponse(code = 401, message = "Unauthorized"), 
			@ApiResponse(code = 403, message = "Forbidden"),
			@ApiResponse(code = 404, message = "Not Found"), 
			@ApiResponse(code = 500, message = "Failure") })
	public ResponseEntity<?> getFolders(@RequestParam(value = "term", required = false) String term,
			@RequestParam(value = "start", required = false) String start,
			@RequestParam(value = "includeHidden", required = false) String includeHidden)
			throws IOException {
		long startNum = 1L;
		if (!StringUtils.isEmpty(start)) {
			startNum = Long.valueOf(start);
		}
		Boolean includeHiddenBool = null;
		if (!StringUtils.isEmpty(includeHidden)) {
			includeHiddenBool = Boolean.valueOf(includeHidden);
		}
		FolderCollection folderCollection = folderCollectionService.getFolders(term, startNum, includeHiddenBool);
		if (folderCollection.hasContent()) {
			return ResponseEntity.ok(folderCollection);
		}
		return ResponseEntity.notFound().build();
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Map<String, Object>> handleIOException(IOException exe) {
		LOGGER.error("Error:", exe);

		Map<String, Object> responseMap = new LinkedHashMap<String, Object>();
		responseMap.put("message", exe.getMessage());
		responseMap.put("code", HttpStatus.BAD_REQUEST);

		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);

		return new ResponseEntity<Map<String, Object>>(responseMap, httpHeaders, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(ApiServiceException.class)
	public ResponseEntity<Map<String, Object>> handleApiServiceException(ApiServiceException exe) {
		LOGGER.error("Error:", exe);

		Map<String, Object> responseMap = new LinkedHashMap<String, Object>();
		responseMap.put("message", exe.getMessage());
		responseMap.put("code", HttpStatus.INTERNAL_SERVER_ERROR);

		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);

		return new ResponseEntity<Map<String, Object>>(responseMap, httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}