package pro.buildmysoftware;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class OrderProjection {
	private LocalDateTime creationDate;
}
