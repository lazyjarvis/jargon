/**
 * 
 */
package org.irods.jargon.core.transfer;

import java.io.File;
import java.io.FileNotFoundException;

import org.irods.jargon.core.connection.IRODSAccount;
import org.irods.jargon.core.exception.JargonException;
import org.irods.jargon.core.exception.JargonRuntimeException;
import org.irods.jargon.core.pub.IRODSAccessObjectFactory;
import org.irods.jargon.core.service.AbstractJargonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mike Conway - (DICE) Restart processor abstract superclass. This
 *         defines a service that can restart a get or put transfer within a
 *         file.
 * 
 */
public abstract class AbstractTransferRestartProcessor extends
		AbstractJargonService {

	private final AbstractRestartManager restartManager;
	private static Logger log = LoggerFactory
			.getLogger(AbstractTransferRestartProcessor.class);

	public static final long RESTART_FILE_UPDATE_SIZE = 32 * 1024 * 1024;

	/**
	 * Constructor with required dependencies
	 * 
	 * @param irodsAccessObjectFactory
	 *            {@IRODSAccessObjectFactory
	 *            irodsAccessObjectFactory}
	 * @param irodsAccount
	 *            {@link IRODSAccount}
	 * @param restartManager
	 *            {@link AbstractRestartManager}
	 */
	public AbstractTransferRestartProcessor(
			final IRODSAccessObjectFactory irodsAccessObjectFactory,
			final IRODSAccount irodsAccount,
			final AbstractRestartManager restartManager) {
		super(irodsAccessObjectFactory, irodsAccount);
		if (restartManager == null) {
			throw new IllegalArgumentException("null restartManager");
		}
		this.restartManager = restartManager;
	}

	/**
	 * @return the restartManager
	 */
	public AbstractRestartManager getRestartManager() {
		return restartManager;
	}

	/**
	 * Check the need to restart the file, and do the restart processing if
	 * needed, based on the data held by the restart manager.
	 * 
	 * @return {@link RestartResult} with details of the restart processing
	 * @throws JargonException
	 */
	public abstract void restartIfNecessary(final String irodsAbsolutePath)
			throws JargonException;

	/**
	 * Given the restart info return the local file and make sure it exists
	 * 
	 * @param fileRestartInfo
	 *            {@link FileRestartInfo} that describes the transfer
	 * @return
	 * @throws FileNotFoundException
	 * @throws JargonException
	 */
	protected File localFileAsFileAndCheckExists(
			final FileRestartInfo fileRestartInfo)
			throws FileNotFoundException, JargonException {
		log.info("localFileAsFileAndCheckExists()");

		File localFile = localFileAsFile(fileRestartInfo);
		if (!localFile.exists()) {
			throw new FileNotFoundException("unable to find local file");
		}
		return localFile;

	}

	/**
	 * Get the local file that is being operated upon
	 * 
	 * @param fileRestartInfo
	 *            fileRestartInfo {@link FileRestartInfo} that describes the
	 *            transfer
	 * @return {@link File} that represents the local part of the transfer
	 * @throws JargonException
	 */
	protected File localFileAsFile(final FileRestartInfo fileRestartInfo)
			throws JargonException {
		log.info("localFileAsFileAndCheckExists()");
		if (fileRestartInfo == null) {
			throw new IllegalArgumentException("null fileRestartInfo");
		}

		if (fileRestartInfo.getLocalAbsolutePath() == null
				|| fileRestartInfo.getLocalAbsolutePath().isEmpty()) {
			log.error("no localFilePath in restart info for:{}",
					fileRestartInfo);
			throw new JargonException(
					"unable to find a local file path in the restart info");
		}

		return new File(fileRestartInfo.getLocalAbsolutePath());

	}

	/**
	 * Method to retrieve the restart info from the manager, this may end up
	 * being <code>null</code>
	 * 
	 * @param fileRestartInfoIdentifier
	 *            {@link FileRestartInfoIdentifier}
	 * @return {@link FileRestartInfo}
	 * @throws FileRestartManagementException
	 */
	protected FileRestartInfo retrieveFileRestartInfoForIdentifier(
			final FileRestartInfoIdentifier fileRestartInfoIdentifier)
			throws FileRestartManagementException {
		if (fileRestartInfoIdentifier == null) {
			throw new IllegalArgumentException("null fileRestartInfoIdentifier");
		}
		if (this.getRestartManager() == null) {
			throw new JargonRuntimeException("no restart manager configured");
		}
		return getRestartManager().retrieveRestart(fileRestartInfoIdentifier);
	}

}