package pro.buildmysoftware;

import lombok.EqualsAndHashCode;
import lombok.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Value
@Document
@EqualsAndHashCode(of = "id")
public class Order {
	@Id
	private String id;
	private List<OrderLine> orderLines;
	private LocalDateTime creationDate;
}
