package li.spectrum.api.controller;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

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
import li.spectrum.api.model.Directory;
import li.spectrum.api.model.DirectoryCollection;
import li.spectrum.api.model.FileCollection;
import li.spectrum.api.service.FileExplorerService;

@RestController
@Api(value = "File Explorer API", produces = "application/hal+json")
@RequestMapping("/files")
public class FileExplorerController {

	private static final Logger LOGGER = LoggerFactory.getLogger(FileExplorerController.class);

	private FileExplorerService fileExplorerService;
	@Autowired
	public FileExplorerController(FileExplorerService fileExplorerService) {
		Assert.notNull(fileExplorerService, "'fileExplorerService' must not be null");
		this.fileExplorerService = fileExplorerService;
	}

	@GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "Returns the files and folders under the specified folder directory. If the root folder is not specified, returns the files and folders under the folder with the shortest path.", notes = "Returns the files and folders under the specified folder directory. If the root folder is not specified, returns the files and folders under the folder with the shortest path.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success", response = DirectoryCollection.class),
			@ApiResponse(code = 400, message = "Input Validation Error"),
			@ApiResponse(code = 401, message = "Unauthorized"), 
			@ApiResponse(code = 403, message = "Forbidden"),
			@ApiResponse(code = 404, message = "Not Found"), 
			@ApiResponse(code = 500, message = "Failure") })
	public ResponseEntity<?> getFiles(@RequestParam(value = "folder", required = false) String folder,
			@RequestParam(value = "start", required = false) String start,
			@RequestParam(value = "includeHidden", required = false) String includeHidden)
 {
		long startNum = 1L;
		if (!StringUtils.isEmpty(start)) {
			startNum = Long.valueOf(start);
		}
		Boolean includeHiddenBool = null;
		if (!StringUtils.isEmpty(includeHidden)) {
			includeHiddenBool = Boolean.valueOf(includeHidden);
		}
		FileCollection fileCollection = fileExplorerService.getFiles(folder, startNum, includeHiddenBool);
		if (fileCollection.hasContent()) {
			fileCollection.getFiles().forEach(resource -> {
				if (resource instanceof Directory) {
					Directory dir = (Directory) resource;
					String dirName = dir.getFolder().getCanonicalPath();
					resource.add(linkTo(methodOn(FileExplorerController.class).getFiles(dirName, "0",
							includeHidden == null ? "false" : "true")).withSelfRel());
				}
			});
			return ResponseEntity.ok(fileCollection);
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