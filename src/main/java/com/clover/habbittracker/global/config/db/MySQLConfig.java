package com.clover.habbittracker.global.config.db;

import org.hibernate.dialect.DatabaseVersion;
import org.hibernate.dialect.MySQLDialect;
import org.hibernate.query.spi.QueryEngine;
import org.hibernate.query.sqm.function.SqmFunctionRegistry;
import org.hibernate.type.BasicTypeRegistry;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MySQLConfig {

	@Bean
	public MySQLDialect mySQLCustomDialect() {
		return new MySQLCustomDialect();
	}

	public static class MySQLCustomDialect extends MySQLDialect {

		public MySQLCustomDialect() {
			super(DatabaseVersion.make(8, 0, 13));
		}

		@Override
		public void initializeFunctionRegistry(QueryEngine queryEngine) {
			super.initializeFunctionRegistry(queryEngine);
			BasicTypeRegistry basicTypeRegistry = queryEngine.getTypeConfiguration().getBasicTypeRegistry();
			SqmFunctionRegistry functionRegistry = queryEngine.getSqmFunctionRegistry();

			functionRegistry.registerPattern( // 1개 매칭
				"match",
				"match(?1) against (?2 in boolean mode)",
				basicTypeRegistry.resolve(StandardBasicTypes.DOUBLE)
			);

			functionRegistry.registerPattern( // 2개 매칭
				"matches",
				"match(?1, ?2) against (?3 in boolean mode)",
				basicTypeRegistry.resolve(StandardBasicTypes.DOUBLE)
			);
		}
	}

}



