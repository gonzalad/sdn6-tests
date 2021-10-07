/*
 * Copyright 2011-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.sdn6;

import java.io.*;
import java.util.stream.Collectors;
import org.neo4j.driver.Session;
import org.springframework.data.neo4j.core.Neo4jClient;

/**
 * @author Michael J. Simons
 */
public final class CypherUtils {

	public static void loadCypherFromResource(String resource, Neo4jClient neo4jClient) throws IOException {
		try (BufferedReader moviesReader = new BufferedReader(
				new InputStreamReader(CypherUtils.class.getResourceAsStream(resource)))) {
			for (String statement : moviesReader.lines().collect(Collectors.joining(" ")).split(";")) {
				neo4jClient.query(statement).run();
			}
		}
	}

	private CypherUtils() {
	}
}
