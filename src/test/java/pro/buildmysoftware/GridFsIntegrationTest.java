package pro.buildmysoftware;

import com.mongodb.client.gridfs.model.GridFSFile;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.gridfs.GridFsOperations;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@SpringBootTest
public class GridFsIntegrationTest {

	@Autowired
	private GridFsOperations gridFsOperations;

	@DisplayName("should store file")
	@Test
	void test() throws Exception {
		// given
		InputStream textFileIs = new ByteArrayInputStream("hello"
			.getBytes("UTF-8"));

		// when
		ObjectId storedFile = gridFsOperations
			.store(textFileIs, "hello.txt");

		// then
		GridFSFile foundFile = gridFsOperations
			.findOne(query(where("_id").is(storedFile)));
		assertThat(foundFile.getFilename()).isEqualTo("hello.txt");
		InputStream storedFileContent = gridFsOperations
			.getResource("hello.txt").getInputStream();
		assertThat(storedFileContent).hasContent("hello");
	}
}
