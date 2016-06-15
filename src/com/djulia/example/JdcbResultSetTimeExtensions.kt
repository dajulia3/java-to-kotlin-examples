package com.djulia.example;

import java.sql.ResultSet
import java.time.Instant

fun ResultSet.getInstant(columnLabel: String): Instant {
    return this.getTimestamp(columnLabel).toInstant();
}
