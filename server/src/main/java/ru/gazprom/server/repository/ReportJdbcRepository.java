package ru.gazprom.server.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.gazprom.server.dto.CardRow;
import ru.gazprom.server.dto.CategoryDTO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ReportJdbcRepository {
    private static final String SQL_CATEGORY = """
                                select id, name
                                from categories
                                order by id
                                """;

    private static final String SQL_CATEGORY_BATCH = """
                                select id, category_id, title, price, image
                                from cards
                                where category_id in (:ids)
                                order by id
                                """;

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public List<CategoryDTO> findCategories() {
        return jdbcTemplate.query(SQL_CATEGORY, (rs, rowNum) -> new CategoryDTO(rs.getLong("id"), rs.getString("name")));
    }

    public List<CardRow> findCardsByCategory(List<Long> categoryIds) {
        if (categoryIds.isEmpty()) {
            return List.of();
        }

        return jdbcTemplate.query(SQL_CATEGORY_BATCH, new MapSqlParameterSource("ids", categoryIds), new CardDTORowMapper());
    }

    private static class CardDTORowMapper implements RowMapper<CardRow> {

        @Override
        public CardRow mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new CardRow(
                    rs.getLong("id"),
                    rs.getLong("category_id"),
                    rs.getString("title"),
                    rs.getBigDecimal("price"),
                    rs.getString("image"));
        }
    }
}
