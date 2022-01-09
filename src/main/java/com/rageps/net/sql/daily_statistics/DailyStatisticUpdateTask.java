package com.rageps.net.sql.daily_statistics;

import com.rageps.task.Task;
import com.rageps.util.DateTimeUtil;
import com.rageps.util.Duration;
import com.rageps.world.World;

import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.Set;

/**
 * Created by Jason M on 2017-04-03 at 2:15 PM
 *
 * A task that will update the sql table(s) when necessary as well as information
 * stored during runtime.
 */
public final class DailyStatisticUpdateTask extends Task {

	/**
	 * The single instance of this class. This supports the singleton pattern.
	 */
	private static final DailyStatisticUpdateTask SINGLETON = new DailyStatisticUpdateTask();

	/**
	 * The slow delay between task executions. This is apparent when the time until the next update is greater than an hour.
	 */
	private static final int SLOW_DELAY = 1000;

	/**
	 * The fast delay between task executions. This is apparent when the time until the update is less than an hour or
	 * the {@link #nextUpdate} refernce is null.
	 */
	private static final int FAST_DELAY = 25;

	/**
	 * The date and time of the next update.
	 */
	private ZonedDateTime nextUpdate;

	/**
	 * The transaction made to the database for determining the newest record.
	 */
	private DailyStatisticGetAllTransaction newestRecordTransaction;

	/**
	 * The transaction made to the database to create a new record.
	 */
	private DailyStatisticCreateRecordTransaction createRecordTransaction;

	/**
	 * The transaction to the database that will truncate the table entries.
	 */
	private DailyStatisticTruncateTransaction truncateTransaction;

	/**
	 * A private constructor, following the singleton pattern.
	 */
	private DailyStatisticUpdateTask() {
		super(FAST_DELAY, false);
	}

	@Override
	protected void execute() {
		if (nextUpdate == null) {
			if (newestRecordTransaction == null) {
				newestRecordTransaction = new DailyStatisticGetAllTransaction();
				World.get().getDatabaseWorker().submit(newestRecordTransaction);
			}
			if (newestRecordTransaction.isFinished()) {
				Set<DailyStatisticRecordRow> recordSet = newestRecordTransaction.getResult();

				DailyStatisticRecordRow lastRecordUpdate = recordSet == null ? null : findNewestOrNull(recordSet);

				ZonedDateTime now = ZonedDateTime.now(DateTimeUtil.ZONE);

				if (lastRecordUpdate == null) {
					nextUpdate = timeOfUpdateToday();
				} else {
					ZonedDateTime lastRecordUpdateDate = lastRecordUpdate.getTimestamp();

					if (lastRecordUpdateDate.getMonth().getValue() == now.getMonth().getValue() && lastRecordUpdateDate.getDayOfMonth() != now.getDayOfMonth()) {
						if (truncateTransaction == null) {
							truncateTransaction = new DailyStatisticTruncateTransaction();

							World.get().getDatabaseWorker().submit(truncateTransaction);
						} else {
							if (truncateTransaction.isFinished()) {
								nextUpdate = timeOfUpdateToday();
								truncateTransaction = null;
							}
						}
					} else {
						nextUpdate = timeOfUpdateToday();
					}
				}
			}
		} else {
			final ZonedDateTime now = ZonedDateTime.now(DateTimeUtil.ZONE);

			if (now.isAfter(nextUpdate)) {
				if (createRecordTransaction == null) {
					createRecordTransaction = new DailyStatisticCreateRecordTransaction();

					World.get().getDatabaseWorker().submit(createRecordTransaction);
				} else {
					if (createRecordTransaction.isFinished()) {
						if (truncateTransaction == null) {
							truncateTransaction = new DailyStatisticTruncateTransaction();

							World.get().getDatabaseWorker().submit(truncateTransaction);
						} else {
							if (truncateTransaction.isFinished()) {
								nextUpdate = timeOfNextUpdate();
								truncateTransaction = null;
								createRecordTransaction = null;
								setDelay(SLOW_DELAY);
							}
						}
					}
				}
			}
		}

		if (nextUpdate != null) {
			Duration duration = new Duration(ZonedDateTime.now(DateTimeUtil.ZONE), nextUpdate);

			if (duration.getHours() == 0) {
				if (getDelay() == SLOW_DELAY) {
					setDelay(FAST_DELAY);
				}
			} else {
				if (getDelay() == FAST_DELAY) {
					setDelay(SLOW_DELAY);
				}
			}
		}
	}

	/**
	 * Determines the newest record from the set of records.
	 *
	 * @param rows
	 * 			  the rows of records.
	 * @return the newest record from teh set.
	 */
	private DailyStatisticRecordRow findNewestOrNull(Set<DailyStatisticRecordRow> rows) {
		return rows.stream().max(Comparator.comparing(DailyStatisticRecordRow::getTimestamp)).orElse(null);
	}

	/**
	 * The time of an update for today, which would be tonight at hour 23, minute 50, and second 0.
	 *
	 * @return the time of the update today.
	 */
	private ZonedDateTime timeOfUpdateToday() {
		return ZonedDateTime.now(DateTimeUtil.ZONE).withHour(23).withMinute(50).withSecond(0);
	}

	/**
	 * The time of the next update, based on tonight update plus a day.
	 *
	 * @return the time of the next update.
	 */
	private ZonedDateTime timeOfNextUpdate() {
		return timeOfUpdateToday().plusDays(1);
	}

	public static DailyStatisticUpdateTask getSingleton() {
		return SINGLETON;
	}
}
