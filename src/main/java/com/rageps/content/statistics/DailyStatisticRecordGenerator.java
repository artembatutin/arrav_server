package com.rageps.content.statistics;


import com.rageps.net.sql.daily_statistics.DailyStatisticGetAllTransaction;
import com.rageps.net.sql.daily_statistics.DailyStatisticRecordRow;
import com.rageps.task.Task;
import com.rageps.world.World;
import com.rageps.world.entity.actor.player.Player;

import java.time.ZonedDateTime;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Jason M on 2017-04-04 at 12:51 PM
 */
public class DailyStatisticRecordGenerator extends Task {

	/**
	 * The amount of executions that can occur before the task automatically shuts down.
	 */
	private static final int EXECUTION_THRESHOLD = 10;

	/**
	 * The set of records obtained from the database.
	 */
	DailyStatisticGetAllTransaction records;

	/**
	 * The start, inclusive date, that we are starting at in the range.
	 */
	private final ZonedDateTime start;

	/**
	 * The end, inclusive date, that we are ending at in the range.
	 */
	private final ZonedDateTime end;

	/**
	 * The subset or range of records that may contains 0 or more elements. If the
	 * start and end date are the same, the record for that day if retained, if possible.
	 */
	private Set<DailyStatisticRecordRow> subsetOfRecords;

	public DailyStatisticRecordGenerator(Player player, ZonedDateTime start, ZonedDateTime end) {
		super(3, player, true);
		this.start = start;
		this.end = end;
	}

	public DailyStatisticRecordGenerator(Player player, ZonedDateTime singleDay) {
		this(player, singleDay, singleDay);
	}

	@Override
	protected void execute() {
		Player player = (Player) getAttachment().get();

		if (player == null/* || !player.isRegistered()*/) {
			cancel();
			return;
		}
		if (getExecutions() > EXECUTION_THRESHOLD) {
			cancel();
			player.message("Unable to gather statistical records, try later.");
			return;
		}
		if (records == null) {
			records = new DailyStatisticGetAllTransaction();

			World.get().getDatabaseWorker().submit(records);
		}
		if (records.isFinished()) {
			cancel();

			subsetOfRecords = records.getResult().stream().filter(record -> start == end ?
																			 record.getTimestamp().getDayOfMonth() == start.getDayOfMonth()
																			  && record.getTimestamp().getYear() == start.getYear() :
																			 record.getTimestamp().isAfter(start) && record.getTimestamp().isBefore(end))
							   .collect(Collectors.toSet());
		}
	}
	/**
	 * The records retained by the underlying transaction made to the database.
	 *
	 * @return either the set of records obtained, if any, or null if none can be found.
	 */
	public Set<DailyStatisticRecordRow> getRecords() {
		return subsetOfRecords;
	}
}
