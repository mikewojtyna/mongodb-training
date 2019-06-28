package pro.buildmysoftware;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;

@SpringBootTest
public class AggregationExampleIntegrationTest {

	@Autowired
	private MongoTemplate mongoTemplate;

	@DisplayName("show aggregation examples and client session")
	@Test
	void test() throws Exception {
		// given
		LocalDateTime now = LocalDateTime.now().withNano(0);
		Order order = new Order(UUID.randomUUID().toString(), List
			.of(new OrderLine(new Product("tea",
				new BigDecimal(100.00)), 2)), now);
		mongoTemplate.save(order);

		// when
		AggregationResults<OrderProjection> aggregationResults =
			mongoTemplate
			.aggregate(newAggregation(Order.class, project(
				"creationDate")), OrderProjection.class);

		// then
		assertThat(aggregationResults.getMappedResults())
			.containsOnly(new OrderProjection(now));
	}
}
