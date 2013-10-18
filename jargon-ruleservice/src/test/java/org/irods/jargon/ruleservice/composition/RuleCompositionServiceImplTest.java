package org.irods.jargon.ruleservice.composition;

import java.io.StringReader;
import java.util.Properties;

import junit.framework.Assert;

import org.irods.jargon.core.connection.IRODSAccount;
import org.irods.jargon.core.connection.IRODSServerProperties;
import org.irods.jargon.core.exception.JargonException;
import org.irods.jargon.core.pub.EnvironmentalInfoAO;
import org.irods.jargon.core.pub.IRODSAccessObjectFactory;
import org.irods.jargon.core.pub.IRODSFileSystem;
import org.irods.jargon.core.pub.io.IRODSFile;
import org.irods.jargon.core.pub.io.IRODSFileWriter;
import org.irods.jargon.core.utils.LocalFileUtils;
import org.irods.jargon.testutils.TestingPropertiesHelper;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class RuleCompositionServiceImplTest {

	private static Properties testingProperties = new Properties();
	private static org.irods.jargon.testutils.TestingPropertiesHelper testingPropertiesHelper = new TestingPropertiesHelper();
	private static org.irods.jargon.testutils.filemanip.ScratchFileUtils scratchFileUtils = null;
	public static final String IRODS_TEST_SUBDIR_PATH = "RuleCompositionServiceImplTest";
	private static org.irods.jargon.testutils.IRODSTestSetupUtilities irodsTestSetupUtilities = null;
	private static IRODSFileSystem irodsFileSystem;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		org.irods.jargon.testutils.TestingPropertiesHelper testingPropertiesLoader = new TestingPropertiesHelper();
		testingProperties = testingPropertiesLoader.getTestProperties();
		scratchFileUtils = new org.irods.jargon.testutils.filemanip.ScratchFileUtils(
				testingProperties);
		scratchFileUtils
				.clearAndReinitializeScratchDirectory(IRODS_TEST_SUBDIR_PATH);
		irodsTestSetupUtilities = new org.irods.jargon.testutils.IRODSTestSetupUtilities();
		irodsTestSetupUtilities.initializeIrodsScratchDirectory();
		irodsTestSetupUtilities
				.initializeDirectoryForTest(IRODS_TEST_SUBDIR_PATH);
		irodsFileSystem = IRODSFileSystem.instance();
	}

	@AfterClass
	public static void tearDown() throws Exception {
		irodsFileSystem.closeAndEatExceptions();
	}

	@Before
	public void cleanUpIrods() throws Exception {
		irodsFileSystem.closeAndEatExceptions();

	}

	@Before
	public void before() throws Exception {
		irodsFileSystem.closeAndEatExceptions();

	}

	@Test
	public void testParseIrodsFileIntoRule() throws Exception {
		IRODSAccount irodsAccount = testingPropertiesHelper
				.buildIRODSAccountFromTestProperties(testingProperties);
		IRODSAccessObjectFactory accessObjectFactory = irodsFileSystem
				.getIRODSAccessObjectFactory();

		EnvironmentalInfoAO environmentalInfoAO = irodsFileSystem
				.getIRODSAccessObjectFactory().getEnvironmentalInfoAO(
						irodsAccount);
		IRODSServerProperties props = environmentalInfoAO
				.getIRODSServerPropertiesFromIRODSServer();

		if (!props.isTheIrodsServerAtLeastAtTheGivenReleaseVersion("rods3.0")) {
			return;
		}

		String ruleFile = "/rules/rulemsiDataObjChksum.r";
		String irodsRuleFile = "testParseIrodsFileIntoRule.r";
		String targetIrodsCollection = testingPropertiesHelper
				.buildIRODSCollectionAbsolutePathFromTestProperties(
						testingProperties, IRODS_TEST_SUBDIR_PATH);

		String ruleString = LocalFileUtils
				.getClasspathResourceFileAsString(ruleFile);
		IRODSFile irodsRuleFileAsFile = accessObjectFactory
				.getIRODSFileFactory(irodsAccount).instanceIRODSFile(
						targetIrodsCollection + "/" + irodsRuleFile);
		irodsRuleFileAsFile.deleteWithForceOption();
		IRODSFileWriter irodsFileWriter = accessObjectFactory
				.getIRODSFileFactory(irodsAccount).instanceIRODSFileWriter(
						targetIrodsCollection + "/" + irodsRuleFile);
		char[] buff = new char[1024];
		StringReader reader = new StringReader(ruleString);

		int len = 0;
		while ((len = reader.read(buff)) > -1) {
			irodsFileWriter.write(buff, 0, len);
		}

		irodsFileWriter.close();
		reader.close();

		RuleCompositionService ruleCompositionService = new RuleCompositionServiceImpl(
				accessObjectFactory, irodsAccount);

		Rule rule = ruleCompositionService
				.loadRuleFromIrods(targetIrodsCollection + "/" + irodsRuleFile);

		Assert.assertNotNull("null rule", rule);

		Assert.assertFalse("empty rule body", rule.getRuleBody().isEmpty());
		Assert.assertFalse("empty input parms", rule.getInputParameters()
				.isEmpty());
		Assert.assertFalse("empty output parms", rule.getOutputParameters()
				.isEmpty());

	}

	@Test(expected = JargonException.class)
	public void testParseIrodsFileIntoRuleMissing() throws Exception {
		IRODSAccount irodsAccount = testingPropertiesHelper
				.buildIRODSAccountFromTestProperties(testingProperties);
		IRODSAccessObjectFactory accessObjectFactory = irodsFileSystem
				.getIRODSAccessObjectFactory();

		EnvironmentalInfoAO environmentalInfoAO = irodsFileSystem
				.getIRODSAccessObjectFactory().getEnvironmentalInfoAO(
						irodsAccount);
		IRODSServerProperties props = environmentalInfoAO
				.getIRODSServerPropertiesFromIRODSServer();

		if (!props.isTheIrodsServerAtLeastAtTheGivenReleaseVersion("rods3.0")) {
			return;
		}

		String irodsRuleFile = "testParseIrodsFileIntoRuleMissing.r";
		String targetIrodsCollection = testingPropertiesHelper
				.buildIRODSCollectionAbsolutePathFromTestProperties(
						testingProperties, IRODS_TEST_SUBDIR_PATH);

		RuleCompositionService ruleCompositionService = new RuleCompositionServiceImpl(
				accessObjectFactory, irodsAccount);

		ruleCompositionService.loadRuleFromIrods(targetIrodsCollection + "/"
				+ irodsRuleFile);

	}

	@Test
	public void testParseStringIntoRule() throws Exception {
		IRODSAccount irodsAccount = testingPropertiesHelper
				.buildIRODSAccountFromTestProperties(testingProperties);
		IRODSAccessObjectFactory accessObjectFactory = irodsFileSystem
				.getIRODSAccessObjectFactory();

		EnvironmentalInfoAO environmentalInfoAO = irodsFileSystem
				.getIRODSAccessObjectFactory().getEnvironmentalInfoAO(
						irodsAccount);
		IRODSServerProperties props = environmentalInfoAO
				.getIRODSServerPropertiesFromIRODSServer();

		if (!props.isTheIrodsServerAtLeastAtTheGivenReleaseVersion("rods3.0")) {
			return;
		}

		String ruleFile = "/rules/rulemsiDataObjChksum.r";

		String ruleString = LocalFileUtils
				.getClasspathResourceFileAsString(ruleFile);

		RuleCompositionService ruleCompositionService = new RuleCompositionServiceImpl(
				accessObjectFactory, irodsAccount);

		Rule rule = ruleCompositionService.parseStringIntoRule(ruleString);

		Assert.assertNotNull("null rule", rule);

		Assert.assertFalse("empty rule body", rule.getRuleBody().isEmpty());
		Assert.assertFalse("empty input parms", rule.getInputParameters()
				.isEmpty());
		Assert.assertFalse("empty output parms", rule.getOutputParameters()
				.isEmpty());

	}

	@Test(expected = IllegalArgumentException.class)
	public void testParseBlankStringIntoRule() throws Exception {
		IRODSAccount irodsAccount = testingPropertiesHelper
				.buildIRODSAccountFromTestProperties(testingProperties);
		IRODSAccessObjectFactory accessObjectFactory = irodsFileSystem
				.getIRODSAccessObjectFactory();

		EnvironmentalInfoAO environmentalInfoAO = irodsFileSystem
				.getIRODSAccessObjectFactory().getEnvironmentalInfoAO(
						irodsAccount);
		IRODSServerProperties props = environmentalInfoAO
				.getIRODSServerPropertiesFromIRODSServer();

		if (!props.isTheIrodsServerAtLeastAtTheGivenReleaseVersion("rods3.0")) {
			return;
		}

		String ruleString = "";

		RuleCompositionService ruleCompositionService = new RuleCompositionServiceImpl(
				accessObjectFactory, irodsAccount);

		ruleCompositionService.parseStringIntoRule(ruleString);

	}

	@Test(expected = IllegalArgumentException.class)
	public void testParseNullStringIntoRule() throws Exception {
		IRODSAccount irodsAccount = testingPropertiesHelper
				.buildIRODSAccountFromTestProperties(testingProperties);
		IRODSAccessObjectFactory accessObjectFactory = irodsFileSystem
				.getIRODSAccessObjectFactory();

		EnvironmentalInfoAO environmentalInfoAO = irodsFileSystem
				.getIRODSAccessObjectFactory().getEnvironmentalInfoAO(
						irodsAccount);
		IRODSServerProperties props = environmentalInfoAO
				.getIRODSServerPropertiesFromIRODSServer();

		if (!props.isTheIrodsServerAtLeastAtTheGivenReleaseVersion("rods3.0")) {
			return;
		}

		String ruleString = null;

		RuleCompositionService ruleCompositionService = new RuleCompositionServiceImpl(
				accessObjectFactory, irodsAccount);

		ruleCompositionService.parseStringIntoRule(ruleString);

	}
}