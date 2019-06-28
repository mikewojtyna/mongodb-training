package pro.buildmysoftware;

import com.mongodb.ClientSessionOptions;
import com.mongodb.MongoClient;
import com.mongodb.client.ClientSession;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@TestPropertySource(properties = "spring.autoconfigure.exclude=org" +
	".springframework.boot.autoconfigure.mongo.embedded" +
	".EmbeddedMongoAutoConfiguration")
@SpringBootTest
public class CausallyConsistenSessionExampleIntegrationTest {
	@Autowired
	private MongoTemplate mongoTemplate;
	@Autowired
	private MongoClient mongoClient;

	@DisplayName("show client session example")
	@Test
	@Disabled
	void session() throws Exception {
		// given
		LocalDateTime now = LocalDateTime.now().withNano(0);
		Order order = new Order(UUID.randomUUID().toString(), List
			.of(new OrderLine(new Product("tea",
				new BigDecimal(100.00)), 2)), now);

		// when
		ClientSession clientSession = mongoClient
			.startSession(ClientSessionOptions.builder()
				.causallyConsistent(true).build());
		Order foundOrder = mongoTemplate
			.withSession(() -> clientSession)
			.execute(mongoOperations -> {
				Order createdOrder = mongoOperations
					.save(order);

				return mongoOperations
					.findOne(query(where("_id")
						.is(createdOrder
							.getId())),
						Order.class);
			});

		// then
		assertThat(foundOrder.getId()).isEqualTo(order.getId());

		clientSession.close();
	}
}
