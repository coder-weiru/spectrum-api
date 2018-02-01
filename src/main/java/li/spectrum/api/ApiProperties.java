package li.spectrum.api;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile({ "dev", "sys", "uat" })
@ConfigurationProperties(prefix = "api")

public class ApiProperties {

	private Map<String, Object> search = new HashMap<String, Object>();

	public Map<String, Object> getSearch() {
		return search;
	}

	public void setSearch(Map<String, Object> search) {
		this.search = search;
	}

	public boolean getSearchCaseSensitive() {
		return (boolean) search.get("caseSensitive");
	}

	public int getSearchPageSize() {
		return (int) search.get("pageSize");
	}
}
