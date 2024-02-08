package com.example.accessingdatamysql.Repository;

import com.example.accessingdatamysql.Entity.Url;
import io.micrometer.observation.annotation.Observed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;


@Repository
@Observed

public class UrlRepository {
    private final JdbcClient jdbcClient;

    @Autowired
    public UrlRepository(JdbcClient jdbcClient ) {
        this.jdbcClient = jdbcClient;
    }


    @Transactional(readOnly = true)
    public Optional<Url> findKey(String key) {
        String query = """ 
                SELECT * FROM url WHERE ukey=:key
                """;
        return jdbcClient.sql(query)
                .param("key", key)
                .query(Url.class)
                .optional();
    }

    @Transactional
    public void saveUrl(Url url) {

        String query = """
                INSERT INTO url (name, ukey, url)
                VALUES (:name, :ukey, :url)
                """;

        jdbcClient.sql(query)
                .params(Map.of(
                        "name", url.getName(),
                        "ukey", url.getUkey(),
                        "url", url.getUrl()
                ))
                .update();
    }

    public List<Url> findall() {

        String query = """
                SELECT * FROM url
                """;
        return jdbcClient.sql(query).query(Url.class).list();
    }

    public void delete(String key) {

        String query = """
                DELETE FROM url WHERE ukey=:key
                """;
         jdbcClient.sql(query).param("key",key).update();

    }
}
