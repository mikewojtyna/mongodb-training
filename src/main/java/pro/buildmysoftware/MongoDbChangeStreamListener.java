package pro.buildmysoftware;

import com.mongodb.client.model.changestream.ChangeStreamDocument;
import org.bson.Document;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.messaging.ChangeStreamRequest;
import org.springframework.data.mongodb.core.messaging.MessageListener;
import org.springframework.data.mongodb.core.messaging.MessageListenerContainer;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

@Component
@Profile("change-stream")
public class MongoDbChangeStreamListener implements ApplicationRunner {

	private MessageListenerContainer messageListenerContainer;

	public MongoDbChangeStreamListener(MessageListenerContainer messageListenerContainer) {
		this.messageListenerContainer = messageListenerContainer;
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		MessageListener<ChangeStreamDocument<Document>, Order> messageListener = message -> {
			System.out.println("Created order with id: " + message
				.getBody().getId());
		};
		ChangeStreamRequest<Order> changeStreamRequest =
			buildChangeStream(messageListener);
		messageListenerContainer
			.register(changeStreamRequest, Order.class);
	}

	private ChangeStreamRequest<Order> buildChangeStream(MessageListener<ChangeStreamDocument<Document>, Order> messageListener) {
		return ChangeStreamRequest.builder().collection("order")
			.filter(Aggregation.newAggregation(Aggregation
				.match(Criteria.where("operationType")
					.is("insert"))))
			.publishTo(messageListener).build();
	}
}
