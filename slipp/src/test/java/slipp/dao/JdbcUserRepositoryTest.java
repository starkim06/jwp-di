package slipp.dao;

import nextstep.jdbc.ConnectionManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import slipp.domain.User;
import slipp.dto.UserUpdatedDto;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class JdbcUserRepositoryTest {
    @BeforeEach
    public void setup() {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("jwp.sql"));
        DatabasePopulatorUtils.execute(populator, ConnectionManager.getDataSource());
    }

    @Test
    public void crud() throws Exception {
        User expected = new User("userId", "password", "name", "javajigi@email.com");
        JdbcUserRepository jdbcUserRepository = new JdbcUserRepository();
        jdbcUserRepository.insert(expected);
        User actual = jdbcUserRepository.findByUserId(expected.getUserId());
        assertThat(actual).isEqualTo(expected);

        expected.update(new UserUpdatedDto("password2", "name2", "sanjigi@email.com"));
        jdbcUserRepository.update(expected);
        actual = jdbcUserRepository.findByUserId(expected.getUserId());
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void findAll() throws Exception {
        JdbcUserRepository jdbcUserRepository = new JdbcUserRepository();
        List<User> users = jdbcUserRepository.findAll();
        assertThat(users).hasSize(1);
    }
}