/*
 * Grakn - A Distributed Semantic Database
 * Copyright (C) 2016-2018 Grakn Labs Limited
 *
 * Grakn is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Grakn is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with Grakn. If not, see <http://www.gnu.org/licenses/agpl.txt>.
 */

package ai.grakn.bootup.config;

import ai.grakn.GraknSystemProperty;
import ai.grakn.engine.GraknConfig;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Factory class for configs.
 *
 * @author Kasper Piskorski
 */
public class Configs {

    private static final String STORAGE_CONFIG_PATH = "services/cassandra/";
    private static final String STORAGE_CONFIG_NAME = "cassandra.yaml";

    private static final String QUEUE_CONFIG_PATH = "services/redis/";
    private static final String QUEUE_CONFIG_NAME = "redis.conf";

    public static GraknConfig graknConfig(){
        return GraknConfig.read(graknConfigPath().toFile());
    }

    public static QueueConfig queueConfig(){
        return QueueConfig.from(queueConfigPath());
    }

    public static StorageConfig storageConfig(){
        return StorageConfig.from(storageConfigPath());
    }

    public static Path graknConfigPath(){
        return Paths.get(GraknSystemProperty.CONFIGURATION_FILE.value());
    }

    /** paths relative to dist dir **/

    public static Path queueConfigPath(){ return Paths.get(QUEUE_CONFIG_PATH, QUEUE_CONFIG_NAME); }

    public static Path storageConfigPath(){ return Paths.get(STORAGE_CONFIG_PATH, STORAGE_CONFIG_NAME); }
}
