package li.spectrum.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;

@Configuration
@EnableConfigurationProperties(ApiProperties.class)
@ComponentScan(basePackageClasses = AppConfig.class)
@Import(SwaggerConfig.class)
public class AppConfig {

	private static final Logger LOGGER = LoggerFactory.getLogger(AppConfig.class);

	@Autowired
	Environment environment;

	@Autowired
	private ApiProperties apiProperties;

}
