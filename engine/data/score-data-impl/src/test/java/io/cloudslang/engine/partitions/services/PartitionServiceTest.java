/*******************************************************************************
* (c) Copyright 2014 Hewlett-Packard Development Company, L.P.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Apache License v2.0 which accompany this distribution.
*
* The Apache License is available at
* http://www.apache.org/licenses/LICENSE-2.0
*
*******************************************************************************/

package io.cloudslang.engine.partitions.services;

import io.cloudslang.engine.partitions.entities.PartitionGroup;
import io.cloudslang.engine.partitions.repositories.PartitionGroupRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import static org.mockito.Mockito.*;
import static org.fest.assertions.Assertions.assertThat;


/**
 * Date: 4/23/12
 *
 * @author
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class PartitionServiceTest {
	private static final String TABLE_NAME = "TEST_TABLE";
	private static final int GROUP_SIZE = 4;
	private static final long TIMEOUT = 10000L;
	private static final long MAX_RECORDS = 1000L;

	private static PartitionGroup partitionGroup = new PartitionGroup(TABLE_NAME, GROUP_SIZE, TIMEOUT, MAX_RECORDS);

	@Autowired
	private PartitionService partitionService;

	// Mocks
	@Autowired private PartitionGroupRepository repository;
	@Autowired private TransactionTemplate transactionTemplate;
	@Autowired private JdbcTemplate jdbcTemplate;

	@Before
	public void before(){
		// reset all mocks
		reset(
				repository,
				transactionTemplate,
				jdbcTemplate
		);
	}

	@Test
	public void partitionRollFalse(){
		when(repository.lock(TABLE_NAME)).thenReturn(1);
		when(repository.findByName(TABLE_NAME)).thenReturn(partitionGroup);
		boolean rollResult = partitionService.rollPartitions(TABLE_NAME);
		assertThat(rollResult).isFalse();
	}

	//@Test
	public void partitionRollOnTimeout(){
		partitionGroup.setLastRollTime(0);
		when(repository.findByName(TABLE_NAME)).thenReturn(partitionGroup);
		boolean rollResult = partitionService.rollPartitions(TABLE_NAME);
		assertThat(rollResult).isTrue();
	}

	@Configuration
	static class Configurator{
		@Bean PartitionService createPartitionManager(){
			return new PartitionServiceImpl();
		}

		@Bean(name = TABLE_NAME)
        PartitionTemplate template(){
			return new PartitionTemplateImpl();
		}

		@Bean PartitionGroupRepository repository(){
			return mock(PartitionGroupRepository.class);
		}

		@Bean JdbcTemplate jdbcTemplate(){
			return mock(JdbcTemplate.class);
		}

		@Bean TransactionTemplate createTransactionTemplate(){
			TransactionTemplate template = mock(TransactionTemplate.class);
			//noinspection unchecked
			when(template.<PartitionGroup>execute(any(TransactionCallback.class)))
					.thenReturn(partitionGroup);
			return template;
		}

        @Bean
        PartitionUtils partitionUtils() {
            return new PartitionUtils();
        }
	}
}
