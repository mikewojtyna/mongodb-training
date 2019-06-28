package pro.buildmysoftware.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.messaging.DefaultMessageListenerContainer;
import org.springframework.data.mongodb.core.messaging.MessageListenerContainer;

@Configuration
@Profile("change-stream")
public class MessageListenerContainerConfig {
	@Bean
	public MessageListenerContainer messageListenerContainer(MongoTemplate mongoTemplate) {
		return new DefaultMessageListenerContainer(mongoTemplate) {
			@Override
			public boolean isAutoStartup() {
				return true;
			}
		};
	}
}
