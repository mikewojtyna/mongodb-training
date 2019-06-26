package pro.buildmysoftware;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode
public class OrderLine {
	private Product product;
	private int quantity;
}
