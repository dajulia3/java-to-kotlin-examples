package djulia.example

import com.djulia.example.getInstant
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.datasource.SingleConnectionDataSource
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.time.Instant

class JdbcResultSetTimeExtensionsTest {

    private var connection: Connection? = null;
    private var jdbcTemplate: JdbcTemplate? = null

    @Before
    fun setup() {
        try {
            Class.forName("org.h2.Driver")
            connection = DriverManager.getConnection("jdbc:h2:mem:testdb;MODE=PostgreSQL")
            val dataSource = SingleConnectionDataSource(connection!!, false)
            jdbcTemplate = JdbcTemplate(dataSource)
        } catch (e: Exception) {
            throw RuntimeException("error getting db connection")
        }
    }

    @After
    fun tearDown() {
        try {
            connection!!.close()
        } catch (e: SQLException) {
            throw RuntimeException("error tearing down db connection")
        }
    }

    @Test
    fun testInstantExtension() {
        jdbcTemplate!!.execute("CREATE TABLE incidents(" +
                "occurred_at timestamp" +
                ")")

        jdbcTemplate!!.execute("INSERT INTO incidents VALUES (timestamp '2016-06-15 19:56:54 UTC')")
        val occurredAt: Instant = jdbcTemplate!!.queryForObject("SELECT * FROM incidents",
                { resultSet, i ->  resultSet.getInstant("occurred_at") }
        )

        assertEquals(Instant.ofEpochSecond(1466020614), occurredAt)
    }

}
