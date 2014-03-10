package org.irods.jargon.transfer.engine.synch;

import java.util.Date;
import java.util.List;

import junit.framework.Assert;

import org.irods.jargon.transfer.dao.domain.FrequencyType;
import org.irods.jargon.transfer.dao.domain.LocalIRODSTransfer;
import org.irods.jargon.transfer.dao.domain.Synchronization;
import org.irods.jargon.transfer.dao.domain.SynchronizationType;
import org.irods.jargon.transfer.dao.domain.TransferState;
import org.irods.jargon.transfer.dao.domain.TransferStatus;
import org.irods.jargon.transfer.engine.TransferQueueService;
import org.irods.jargon.transfer.util.HibernateUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:transfer-dao-beans.xml",
		"classpath:transfer-dao-hibernate-spring.cfg.xml",
		"classpath:test-beans.xml" })
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Transactional
public class SynchManagerServiceImplTest {

	@Autowired
	private SynchManagerService synchManagerService;

	@Autowired
	private TransferQueueService transferQueueService;

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testCreateNewSynchConfiguration() throws Exception {
		Synchronization synchConfiguration = new Synchronization();
		synchConfiguration.setCreatedAt(new Date());
		synchConfiguration.setDefaultResourceName("test");
		synchConfiguration.setIrodsHostName("host");
		synchConfiguration.setIrodsPassword("xxx");
		synchConfiguration.setIrodsPort(1247);
		synchConfiguration.setIrodsSynchDirectory("/synchdir");
		synchConfiguration.setIrodsUserName("userName");
		synchConfiguration.setIrodsZone("zone");
		synchConfiguration.setFrequencyType(FrequencyType.EVERY_DAY);

		synchConfiguration.setLastSynchronizationStatus(TransferStatus.OK);
		synchConfiguration.setLocalSynchDirectory("/localdir");
		synchConfiguration
				.setSynchronizationMode(SynchronizationType.ONE_WAY_LOCAL_TO_IRODS);
		synchConfiguration.setName("testCreateNewSynchConfiguration");
		synchManagerService.createNewSynchConfiguration(synchConfiguration);
	}

	@Test(expected = ConflictingSynchException.class)
	public void testCreateNewSynchConfigurationDuplicateName() throws Exception {
		Synchronization synchConfiguration = new Synchronization();
		synchConfiguration.setCreatedAt(new Date());
		synchConfiguration.setDefaultResourceName("test");
		synchConfiguration.setIrodsHostName("host");
		synchConfiguration.setIrodsPassword("xxx");
		synchConfiguration.setIrodsPort(1247);
		synchConfiguration.setIrodsSynchDirectory("/synchdir");
		synchConfiguration.setIrodsUserName("userName");
		synchConfiguration.setIrodsZone("zone");
		synchConfiguration.setLastSynchronizationStatus(TransferStatus.OK);
		synchConfiguration.setLocalSynchDirectory("/localdir");
		synchConfiguration.setFrequencyType(FrequencyType.EVERY_DAY);

		synchConfiguration
				.setSynchronizationMode(SynchronizationType.ONE_WAY_LOCAL_TO_IRODS);
		synchConfiguration.setName("testCreateNewSynchConfiguration");
		synchManagerService.createNewSynchConfiguration(synchConfiguration);
		synchConfiguration = new Synchronization();
		synchConfiguration.setCreatedAt(new Date());
		synchConfiguration.setDefaultResourceName("test");
		synchConfiguration.setIrodsHostName("host");
		synchConfiguration.setIrodsPassword("xxx");
		synchConfiguration.setIrodsPort(1247);
		synchConfiguration.setIrodsSynchDirectory("/synchdir2");
		synchConfiguration.setIrodsUserName("userName");
		synchConfiguration.setIrodsZone("zone");
		synchConfiguration.setLastSynchronizationStatus(TransferStatus.OK);
		synchConfiguration.setFrequencyType(FrequencyType.EVERY_DAY);

		synchConfiguration.setLocalSynchDirectory("/localdir2");
		synchConfiguration
				.setSynchronizationMode(SynchronizationType.ONE_WAY_LOCAL_TO_IRODS);
		synchConfiguration.setName("testCreateNewSynchConfiguration");
		synchManagerService.createNewSynchConfiguration(synchConfiguration);
	}

	@Test(expected = ConflictingSynchException.class)
	public void testCreateNewSynchConfigurationDuplicateLocal()
			throws Exception {
		Synchronization synchConfiguration = new Synchronization();
		synchConfiguration.setCreatedAt(new Date());
		synchConfiguration.setDefaultResourceName("test");
		synchConfiguration.setIrodsHostName("host");
		synchConfiguration.setIrodsPassword("xxx");
		synchConfiguration.setIrodsPort(1247);
		synchConfiguration.setIrodsSynchDirectory("/synchdir");
		synchConfiguration.setIrodsUserName("userName");
		synchConfiguration.setIrodsZone("zone");
		synchConfiguration.setLastSynchronizationStatus(TransferStatus.OK);
		synchConfiguration.setLocalSynchDirectory("/localdir");
		synchConfiguration.setFrequencyType(FrequencyType.EVERY_DAY);

		synchConfiguration
				.setSynchronizationMode(SynchronizationType.ONE_WAY_LOCAL_TO_IRODS);
		synchConfiguration
				.setName("testCreateNewSynchConfigurationDuplicateLocal");
		synchManagerService.createNewSynchConfiguration(synchConfiguration);
		synchConfiguration = new Synchronization();
		synchConfiguration.setCreatedAt(new Date());
		synchConfiguration.setDefaultResourceName("test");
		synchConfiguration.setIrodsHostName("host");
		synchConfiguration.setIrodsPassword("xxx");
		synchConfiguration.setIrodsPort(1247);
		synchConfiguration.setIrodsSynchDirectory("/synchdir2");
		synchConfiguration.setIrodsUserName("userName");
		synchConfiguration.setIrodsZone("zone");
		synchConfiguration.setFrequencyType(FrequencyType.EVERY_DAY);

		synchConfiguration.setLastSynchronizationStatus(TransferStatus.OK);
		synchConfiguration.setLocalSynchDirectory("/localdir");
		synchConfiguration
				.setSynchronizationMode(SynchronizationType.ONE_WAY_LOCAL_TO_IRODS);
		synchConfiguration
				.setName("testCreateNewSynchConfigurationDuplicateLocal2");
		synchManagerService.createNewSynchConfiguration(synchConfiguration);
	}

	@Test(expected = ConflictingSynchException.class)
	public void testCreateNewSynchConfigurationDuplicateIrods()
			throws Exception {
		Synchronization synchConfiguration = new Synchronization();
		synchConfiguration.setCreatedAt(new Date());
		synchConfiguration.setDefaultResourceName("test");
		synchConfiguration.setIrodsHostName("host");
		synchConfiguration.setIrodsPassword("xxx");
		synchConfiguration.setIrodsPort(1247);
		synchConfiguration.setIrodsSynchDirectory("/synchdir");
		synchConfiguration.setIrodsUserName("userName");
		synchConfiguration.setIrodsZone("zone");
		synchConfiguration.setLastSynchronizationStatus(TransferStatus.OK);
		synchConfiguration.setLocalSynchDirectory("/localdir");
		synchConfiguration
				.setSynchronizationMode(SynchronizationType.ONE_WAY_LOCAL_TO_IRODS);
		synchConfiguration
				.setName("testCreateNewSynchConfigurationDuplicateIrods");
		synchConfiguration.setFrequencyType(FrequencyType.EVERY_DAY);

		synchManagerService.createNewSynchConfiguration(synchConfiguration);
		synchConfiguration = new Synchronization();
		synchConfiguration.setCreatedAt(new Date());
		synchConfiguration.setDefaultResourceName("test");
		synchConfiguration.setIrodsHostName("host");
		synchConfiguration.setIrodsPassword("xxx");
		synchConfiguration.setIrodsPort(1247);
		synchConfiguration.setIrodsSynchDirectory("/synchdir");
		synchConfiguration.setIrodsUserName("userName");
		synchConfiguration.setIrodsZone("zone");
		synchConfiguration.setLastSynchronizationStatus(TransferStatus.OK);
		synchConfiguration.setLocalSynchDirectory("/localdir2");
		synchConfiguration
				.setSynchronizationMode(SynchronizationType.ONE_WAY_LOCAL_TO_IRODS);
		synchConfiguration
				.setName("testCreateNewSynchConfigurationDuplicateIrods2");
		synchConfiguration.setFrequencyType(FrequencyType.EVERY_DAY);

		synchManagerService.createNewSynchConfiguration(synchConfiguration);
	}

	@Test
	public void testCreateNewSynchConfigurationDuplicateIrodsDiffZone()
			throws Exception {
		Synchronization synchConfiguration = new Synchronization();
		synchConfiguration.setCreatedAt(new Date());
		synchConfiguration.setDefaultResourceName("test");
		synchConfiguration.setIrodsHostName("host");
		synchConfiguration.setIrodsPassword("xxx");
		synchConfiguration.setIrodsPort(1247);
		synchConfiguration.setIrodsSynchDirectory("/synchdir");
		synchConfiguration.setIrodsUserName("userName");
		synchConfiguration.setIrodsZone("zone");
		synchConfiguration.setLastSynchronizationStatus(TransferStatus.OK);
		synchConfiguration.setLocalSynchDirectory("/localdir");
		synchConfiguration
				.setSynchronizationMode(SynchronizationType.ONE_WAY_LOCAL_TO_IRODS);
		synchConfiguration
				.setName("testCreateNewSynchConfigurationDuplicateIrodsDiffZone");
		synchConfiguration.setFrequencyType(FrequencyType.EVERY_DAY);

		synchManagerService.createNewSynchConfiguration(synchConfiguration);
		synchConfiguration = new Synchronization();
		synchConfiguration.setCreatedAt(new Date());
		synchConfiguration.setDefaultResourceName("test");
		synchConfiguration.setIrodsHostName("host");
		synchConfiguration.setIrodsPassword("xxx");
		synchConfiguration.setIrodsPort(1247);
		synchConfiguration.setIrodsSynchDirectory("/synchdir");
		synchConfiguration.setIrodsUserName("userName");
		synchConfiguration.setIrodsZone("zone2");
		synchConfiguration.setLastSynchronizationStatus(TransferStatus.OK);
		synchConfiguration.setLocalSynchDirectory("/localdir2");
		synchConfiguration
				.setSynchronizationMode(SynchronizationType.ONE_WAY_LOCAL_TO_IRODS);
		synchConfiguration.setFrequencyType(FrequencyType.EVERY_DAY);

		synchConfiguration
				.setName("testCreateNewSynchConfigurationDuplicateIrodsDiffZone2");
		synchManagerService.createNewSynchConfiguration(synchConfiguration);
		Assert.assertTrue(true);
	}

	@Test
	public void testListAllSynchConfiguration() throws Exception {
		Synchronization synchConfiguration = new Synchronization();
		synchConfiguration.setCreatedAt(new Date());
		synchConfiguration.setDefaultResourceName("test");
		synchConfiguration.setIrodsHostName("host");
		synchConfiguration.setIrodsPassword("xxx");
		synchConfiguration.setIrodsPort(1247);
		synchConfiguration.setIrodsSynchDirectory("/synchdir");
		synchConfiguration.setIrodsUserName("userName");
		synchConfiguration.setIrodsZone("zone");
		synchConfiguration.setLastSynchronizationStatus(TransferStatus.OK);
		synchConfiguration.setLocalSynchDirectory("/localdir");
		synchConfiguration
				.setSynchronizationMode(SynchronizationType.ONE_WAY_LOCAL_TO_IRODS);
		synchConfiguration.setFrequencyType(FrequencyType.EVERY_DAY);

		synchConfiguration.setName("testCreateNewSynchConfiguration");
		synchManagerService.createNewSynchConfiguration(synchConfiguration);
		List<Synchronization> allSynchs = synchManagerService
				.listAllSynchronizations();
		Assert.assertTrue("did not list synchs", allSynchs.size() > 0);

	}

	@Test
	public void testFindById() throws Exception {
		Synchronization synchConfiguration = new Synchronization();
		synchConfiguration.setCreatedAt(new Date());
		synchConfiguration.setDefaultResourceName("test");
		synchConfiguration.setIrodsHostName("host");
		synchConfiguration.setIrodsPassword("xxx");
		synchConfiguration.setIrodsPort(1247);
		synchConfiguration.setIrodsSynchDirectory("/synchdir");
		synchConfiguration.setIrodsUserName("userName");
		synchConfiguration.setIrodsZone("zone");
		synchConfiguration.setLastSynchronizationStatus(TransferStatus.OK);
		synchConfiguration.setLocalSynchDirectory("/localdir");

		synchConfiguration.setFrequencyType(FrequencyType.EVERY_DAY);

		synchConfiguration
				.setSynchronizationMode(SynchronizationType.ONE_WAY_LOCAL_TO_IRODS);
		synchConfiguration.setName("testFindById");
		synchManagerService.createNewSynchConfiguration(synchConfiguration);

		// now find
		Synchronization actual = synchManagerService
				.findById(synchConfiguration.getId());
		Assert.assertNotNull("did not find synch I just added", actual);

	}

	@Test
	public void testFindByName() throws Exception {
		String testName = "testFindByName";
		Synchronization synchConfiguration = new Synchronization();
		synchConfiguration.setCreatedAt(new Date());
		synchConfiguration.setDefaultResourceName("test");
		synchConfiguration.setIrodsHostName("host");
		synchConfiguration.setIrodsPassword("xxx");
		synchConfiguration.setIrodsPort(1247);
		synchConfiguration.setIrodsSynchDirectory("/synchdir");
		synchConfiguration.setIrodsUserName("userName");
		synchConfiguration.setIrodsZone("zone");
		synchConfiguration.setLastSynchronizationStatus(TransferStatus.OK);
		synchConfiguration.setLocalSynchDirectory("/localdir");
		synchConfiguration
				.setSynchronizationMode(SynchronizationType.ONE_WAY_LOCAL_TO_IRODS);
		synchConfiguration.setFrequencyType(FrequencyType.EVERY_DAY);

		synchConfiguration.setName(testName);
		synchManagerService.createNewSynchConfiguration(synchConfiguration);

		// now find
		Synchronization actual = synchManagerService.findByName(testName);
		Assert.assertNotNull("did not find synch I just added", actual);

	}

	@Test
	public void testUpdateSynchConfiguration() throws Exception {
		Synchronization synchConfiguration = new Synchronization();
		synchConfiguration.setCreatedAt(new Date());
		synchConfiguration.setDefaultResourceName("test");
		synchConfiguration.setIrodsHostName("host");
		synchConfiguration.setIrodsPassword("xxx");
		synchConfiguration.setIrodsPort(1247);
		synchConfiguration.setIrodsSynchDirectory("/synchdir");
		synchConfiguration.setIrodsUserName("userName");
		synchConfiguration.setIrodsZone("zone");
		synchConfiguration.setLastSynchronizationStatus(TransferStatus.OK);
		synchConfiguration.setLocalSynchDirectory("/localdir");
		synchConfiguration
				.setSynchronizationMode(SynchronizationType.ONE_WAY_LOCAL_TO_IRODS);
		synchConfiguration.setName("testUpdateSynchConfiguration");
		synchConfiguration.setFrequencyType(FrequencyType.EVERY_DAY);

		synchManagerService.createNewSynchConfiguration(synchConfiguration);

		synchConfiguration.setUpdatedAt(new Date());
		synchConfiguration.setLocalSynchDirectory("/localdir2");
		synchConfiguration
				.setSynchronizationMode(SynchronizationType.ONE_WAY_LOCAL_TO_IRODS);
		synchConfiguration.setFrequencyType(FrequencyType.EVERY_DAY);

		synchConfiguration
				.setName("testCreateNewSynchConfigurationDuplicateIrods2");
		synchManagerService.updateSynchConfiguration(synchConfiguration);
		// now find
		Synchronization actual = synchManagerService
				.findByName(synchConfiguration.getName());
		Assert.assertNotNull("did not find synch I just added", actual);
		Assert.assertEquals("did not get updated local synch directory",
				synchConfiguration.getLocalSynchDirectory(),
				actual.getLocalSynchDirectory());

	}

	@Test(expected = ConflictingSynchException.class)
	public void testUpdateSynchConfigurationDuplicateName() throws Exception {
		String testName = "testUpdateSynchConfigurationDuplicateName";
		Synchronization synchConfiguration = new Synchronization();
		synchConfiguration.setCreatedAt(new Date());
		synchConfiguration.setDefaultResourceName("test");
		synchConfiguration.setIrodsHostName("host");
		synchConfiguration.setIrodsPassword("xxx");
		synchConfiguration.setIrodsPort(1247);
		synchConfiguration.setIrodsSynchDirectory("/synchdir");
		synchConfiguration.setIrodsUserName("userName");
		synchConfiguration.setIrodsZone("zone");
		synchConfiguration.setLastSynchronizationStatus(TransferStatus.OK);
		synchConfiguration.setLocalSynchDirectory("/localdir");
		synchConfiguration
				.setSynchronizationMode(SynchronizationType.ONE_WAY_LOCAL_TO_IRODS);
		synchConfiguration.setName(testName);
		synchConfiguration.setFrequencyType(FrequencyType.EVERY_DAY);

		synchManagerService.createNewSynchConfiguration(synchConfiguration);

		Synchronization synchConfiguration2 = new Synchronization();
		synchConfiguration2.setCreatedAt(new Date());
		synchConfiguration2.setDefaultResourceName("test");
		synchConfiguration2.setIrodsHostName("host");
		synchConfiguration2.setIrodsPassword("xxx");
		synchConfiguration2.setIrodsPort(1247);
		synchConfiguration2.setIrodsSynchDirectory("/synchdirx");
		synchConfiguration2.setIrodsUserName("userName");
		synchConfiguration2.setIrodsZone("zone");
		synchConfiguration2.setLastSynchronizationStatus(TransferStatus.OK);
		synchConfiguration2.setLocalSynchDirectory("/localdir2");
		synchConfiguration2
				.setSynchronizationMode(SynchronizationType.ONE_WAY_LOCAL_TO_IRODS);
		synchConfiguration2.setFrequencyType(FrequencyType.EVERY_DAY);

		synchConfiguration2.setName(testName + "first");
		synchManagerService.createNewSynchConfiguration(synchConfiguration2);

		synchConfiguration2.setName(testName);

		synchManagerService.updateSynchConfiguration(synchConfiguration2);

	}

	@Test(expected = ConflictingSynchException.class)
	public void testUpdateSynchConfigurationDuplicateIrodsPath()
			throws Exception {
		String testName = "testUpdateSynchConfigurationDuplicateIrodsPath";
		Synchronization synchConfiguration = new Synchronization();
		synchConfiguration.setCreatedAt(new Date());
		synchConfiguration.setDefaultResourceName("test");
		synchConfiguration.setIrodsHostName("host");
		synchConfiguration.setIrodsPassword("xxx");
		synchConfiguration.setIrodsPort(1247);
		synchConfiguration.setIrodsSynchDirectory(testName);
		synchConfiguration.setIrodsUserName("userName");
		synchConfiguration.setIrodsZone("zone");
		synchConfiguration.setLastSynchronizationStatus(TransferStatus.OK);
		synchConfiguration.setLocalSynchDirectory("/localdir");
		synchConfiguration
				.setSynchronizationMode(SynchronizationType.ONE_WAY_LOCAL_TO_IRODS);
		synchConfiguration.setName(testName);
		synchConfiguration.setFrequencyType(FrequencyType.EVERY_DAY);

		synchManagerService.createNewSynchConfiguration(synchConfiguration);

		Synchronization synchConfiguration2 = new Synchronization();
		synchConfiguration2.setCreatedAt(new Date());
		synchConfiguration2.setDefaultResourceName("test");
		synchConfiguration2.setIrodsHostName("host");
		synchConfiguration2.setIrodsPassword("xxx");
		synchConfiguration2.setIrodsPort(1247);
		synchConfiguration2.setIrodsSynchDirectory(testName + "second");
		synchConfiguration2.setIrodsUserName("userName");
		synchConfiguration2.setIrodsZone("zone");
		synchConfiguration2.setLastSynchronizationStatus(TransferStatus.OK);
		synchConfiguration2.setLocalSynchDirectory("/localdir2");
		synchConfiguration2
				.setSynchronizationMode(SynchronizationType.ONE_WAY_LOCAL_TO_IRODS);
		synchConfiguration2.setFrequencyType(FrequencyType.EVERY_DAY);

		synchConfiguration2.setName(testName + "second");
		synchManagerService.createNewSynchConfiguration(synchConfiguration2);

		synchConfiguration2.setIrodsSynchDirectory(testName);

		synchManagerService.updateSynchConfiguration(synchConfiguration2);

	}

	@Test(expected = ConflictingSynchException.class)
	public void testUpdateSynchConfigurationDuplicateLocalPath()
			throws Exception {
		String testName = "testUpdateSynchConfigurationDuplicateLocalPath";
		Synchronization synchConfiguration = new Synchronization();
		synchConfiguration.setCreatedAt(new Date());
		synchConfiguration.setDefaultResourceName("test");
		synchConfiguration.setIrodsHostName("host");
		synchConfiguration.setIrodsPassword("xxx");
		synchConfiguration.setIrodsPort(1247);
		synchConfiguration.setIrodsSynchDirectory(testName + "first");
		synchConfiguration.setIrodsUserName("userName");
		synchConfiguration.setIrodsZone("zone");
		synchConfiguration.setLastSynchronizationStatus(TransferStatus.OK);
		synchConfiguration.setLocalSynchDirectory(testName);
		synchConfiguration
				.setSynchronizationMode(SynchronizationType.ONE_WAY_LOCAL_TO_IRODS);
		synchConfiguration.setName(testName);
		synchConfiguration.setFrequencyType(FrequencyType.EVERY_DAY);

		synchManagerService.createNewSynchConfiguration(synchConfiguration);

		Synchronization synchConfiguration2 = new Synchronization();
		synchConfiguration2.setCreatedAt(new Date());
		synchConfiguration2.setDefaultResourceName("test");
		synchConfiguration2.setIrodsHostName("host");
		synchConfiguration2.setIrodsPassword("xxx");
		synchConfiguration2.setIrodsPort(1247);
		synchConfiguration2.setIrodsSynchDirectory(testName + "second");
		synchConfiguration2.setIrodsUserName("userName");
		synchConfiguration2.setIrodsZone("zone");
		synchConfiguration2.setLastSynchronizationStatus(TransferStatus.OK);
		synchConfiguration2.setLocalSynchDirectory(testName);
		synchConfiguration2
				.setSynchronizationMode(SynchronizationType.ONE_WAY_LOCAL_TO_IRODS);
		synchConfiguration2.setName(testName + "second");
		synchConfiguration2.setFrequencyType(FrequencyType.EVERY_DAY);

		synchManagerService.createNewSynchConfiguration(synchConfiguration2);

		synchConfiguration2.setIrodsSynchDirectory(testName);

		synchManagerService.updateSynchConfiguration(synchConfiguration2);

	}

	@Test
	public void testDeleteSynchronizationNoTxfrsInQueue() throws Exception {
		String testName = "testDeleteSynchronizationNoTxfrsInQueue";
		Synchronization synchConfiguration = new Synchronization();
		synchConfiguration.setCreatedAt(new Date());
		synchConfiguration.setDefaultResourceName("test");
		synchConfiguration.setIrodsHostName("host");
		synchConfiguration.setIrodsPassword("xxx");
		synchConfiguration.setIrodsPort(1247);
		synchConfiguration.setIrodsSynchDirectory("/synchdir");
		synchConfiguration.setIrodsUserName("userName");
		synchConfiguration.setIrodsZone("zone");
		synchConfiguration.setLastSynchronizationStatus(TransferStatus.OK);
		synchConfiguration.setLocalSynchDirectory("/localdir");
		synchConfiguration
				.setSynchronizationMode(SynchronizationType.ONE_WAY_LOCAL_TO_IRODS);
		synchConfiguration.setName(testName);
		synchConfiguration.setFrequencyType(FrequencyType.EVERY_DAY);

		synchManagerService.createNewSynchConfiguration(synchConfiguration);

		Synchronization actual = synchManagerService.findByName(testName);
		synchManagerService.deleteSynchronization(actual);
		actual = synchManagerService.findByName(testName);
		Assert.assertNull("synch should be deleted", actual);
	}

	@Test
	public void testDeleteSynchronizationCompletedTxfrsInQueue()
			throws Exception {
		transferQueueService.purgeQueue();
		String testName = "testDeleteSynchronizationCompletedTxfrsInQueue";
		Synchronization synchConfiguration = new Synchronization();
		synchConfiguration.setCreatedAt(new Date());
		synchConfiguration.setDefaultResourceName("test");
		synchConfiguration.setIrodsHostName("host");
		synchConfiguration
				.setIrodsPassword(HibernateUtil.obfuscate("jjjjfjfj"));
		synchConfiguration.setIrodsPort(1247);
		synchConfiguration.setIrodsSynchDirectory(testName);
		synchConfiguration.setIrodsUserName("userName");
		synchConfiguration.setIrodsZone("zone");
		synchConfiguration.setLastSynchronizationStatus(TransferStatus.OK);
		synchConfiguration.setLocalSynchDirectory("/localdir");
		synchConfiguration
				.setSynchronizationMode(SynchronizationType.ONE_WAY_LOCAL_TO_IRODS);
		synchConfiguration.setName(testName);
		synchConfiguration.setFrequencyType(FrequencyType.EVERY_DAY);

		synchManagerService.createNewSynchConfiguration(synchConfiguration);

		Synchronization actual = synchManagerService.findByName(testName);
		transferQueueService.enqueueSynchTransfer(actual,
				actual.buildIRODSAccountFromSynchronizationData());
		// tweak txfr to complete

		for (LocalIRODSTransfer localIRODSTransfer : actual
				.getLocalIRODSTransfers()) {
			localIRODSTransfer.setTransferState(TransferState.COMPLETE);
			transferQueueService.setTransferAsCancelled(localIRODSTransfer);
		}

		synchManagerService.deleteSynchronization(actual);
		actual = synchManagerService.findByName(testName);
		Assert.assertNull("synch should be deleted", actual);
		List<LocalIRODSTransfer> queue = transferQueueService.getRecentQueue();
		
		for (LocalIRODSTransfer transfer : queue) {
			Assert.assertFalse("should be no txfrs related to synch in queue", transfer.getIrodsAbsolutePath().equals(testName));
		}
		
		
	}

	@Test(expected = ConflictingSynchException.class)
	public void testDeleteSynchronizationEnqueuedTxfrsInQueue()
			throws Exception {
		String testName = "testDeleteSynchronizationEnqueuedTxfrsInQueue";
		Synchronization synchConfiguration = new Synchronization();
		synchConfiguration.setCreatedAt(new Date());
		synchConfiguration.setDefaultResourceName("test");
		synchConfiguration.setIrodsHostName("host");
		synchConfiguration
				.setIrodsPassword(HibernateUtil.obfuscate("jjjjfjfj"));
		synchConfiguration.setIrodsPort(1247);
		synchConfiguration.setIrodsSynchDirectory("/synchdir");
		synchConfiguration.setIrodsUserName("userName");
		synchConfiguration.setIrodsZone("zone");
		synchConfiguration.setLastSynchronizationStatus(TransferStatus.OK);
		synchConfiguration.setLocalSynchDirectory("/localdir");
		synchConfiguration
				.setSynchronizationMode(SynchronizationType.ONE_WAY_LOCAL_TO_IRODS);
		synchConfiguration.setName(testName);
		synchConfiguration.setFrequencyType(FrequencyType.EVERY_DAY);

		synchManagerService.createNewSynchConfiguration(synchConfiguration);

		Synchronization actual = synchManagerService.findByName(testName);
		transferQueueService.enqueueSynchTransfer(actual,
				actual.buildIRODSAccountFromSynchronizationData());
		synchManagerService.deleteSynchronization(actual);

	}

	@Autowired
	public void setSynchManagerService(
			final SynchManagerService synchManagerService) {
		this.synchManagerService = synchManagerService;
	}

	public SynchManagerService getSynchManagerService() {
		return synchManagerService;
	}

	/**
	 * @return the transferQueueService
	 */
	public TransferQueueService getTransferQueueService() {
		return transferQueueService;
	}

	/**
	 * @param transferQueueService
	 *            the transferQueueService to set
	 */
	public void setTransferQueueService(
			final TransferQueueService transferQueueService) {
		this.transferQueueService = transferQueueService;
	}

}