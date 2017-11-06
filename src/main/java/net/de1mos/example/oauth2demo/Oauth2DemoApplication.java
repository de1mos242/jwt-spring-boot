package net.de1mos.example.oauth2demo;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.de1mos.example.oauth2demo.entities.TestEntity;
import net.de1mos.example.oauth2demo.repository.StrangeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@SpringBootApplication
public class Oauth2DemoApplication implements CommandLineRunner {

	@Autowired
	private StrangeDao strangeDao;

	@Autowired
	private ObjectMapper objectMapper;

	public static void main(String[] args) {
		SpringApplication.run(Oauth2DemoApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		TestEntity entity = new TestEntity();
		entity.setSomeUuid(UUID.randomUUID());
		entity.setSomeCidr("255.255.255.16");
		Map<String, String> map = new HashMap<String, String>() {{
			put("key1", "val1");
			put("key2", "val2");
			put("key3", "val5");
		}};
		entity.setSomeJson(objectMapper.writeValueAsString(map));
		strangeDao.save(entity);

		strangeDao.findAll().forEach(TestEntity::toString);
	}
}
