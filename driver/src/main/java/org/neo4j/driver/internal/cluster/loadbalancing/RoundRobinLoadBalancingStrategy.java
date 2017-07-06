/*
 * Copyright (c) 2002-2017 "Neo Technology,"
 * Network Engine for Objects in Lund AB [http://neotechnology.com]
 *
 * This file is part of Neo4j.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.neo4j.driver.internal.cluster.loadbalancing;

import org.neo4j.driver.internal.net.BoltServerAddress;

/**
 * Load balancing strategy that selects addresses in round-robin fashion. It maintains separate indices for readers and
 * writers.
 */
public class RoundRobinLoadBalancingStrategy implements LoadBalancingStrategy
{
    private final RoundRobinArrayIndex readersIndex = new RoundRobinArrayIndex();
    private final RoundRobinArrayIndex writersIndex = new RoundRobinArrayIndex();

    @Override
    public BoltServerAddress selectReader( BoltServerAddress[] knownReaders )
    {
        return select( knownReaders, readersIndex );
    }

    @Override
    public BoltServerAddress selectWriter( BoltServerAddress[] knownWriters )
    {
        return select( knownWriters, writersIndex );
    }

    private BoltServerAddress select( BoltServerAddress[] addresses, RoundRobinArrayIndex roundRobinIndex )
    {
        int length = addresses.length;
        if ( length == 0 )
        {
            return null;
        }
        int index = roundRobinIndex.next( length );
        return addresses[index];
    }
}
