package pro.buildmysoftware;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class OrderRepositoryIntegrationTest {

	@Autowired
	private OrderRepository orderRepository;

	private BigDecimal money(double value) {
		return BigDecimal.valueOf(value).setScale(2);
	}

	@BeforeEach
	void cleanDb() {
		orderRepository.deleteAll();
	}

	@DisplayName("should persist new order using MongoDB repository")
	@Test
	void test() throws Exception {
		// given
		Order order = new Order(UUID.randomUUID().toString(), List
			.of(new OrderLine(new Product("tea", money(100.00)),
				2)), LocalDateTime
			.now().withNano(0));

		// when
		orderRepository.save(order);

		// then
		Order orderFromDb = orderRepository.findById(order.getId())
			.get();
		assertThat(orderFromDb.getId()).isEqualTo(order.getId());
		assertThat(orderFromDb.getOrderLines())
			.isEqualTo(order.getOrderLines());
		assertThat(orderFromDb.getCreationDate())
			.isEqualTo(order.getCreationDate());
	}
}
