package com.rageps.content.statistics;


import com.rageps.net.sql.daily_statistics.DailyStatisticRecordRow;
import com.rageps.task.Task;
import com.rageps.world.entity.actor.player.Player;
import com.rageps.world.entity.actor.player.assets.group.PrivilegedAttributes;

import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Jason M on 2017-04-04 at 1:33 PM
 */
public class  DailyStatisticRecordDisplay extends Task {

	private final DailyStatisticRecordGenerator generator;

	public DailyStatisticRecordDisplay(Player key, DailyStatisticRecordGenerator generator) {
		super(3, key, false);
		this.generator = generator;
	}

	/**
	 * Performs this task's action.
	 */
	@Override
	protected void execute() {
		Player player = (Player) getAttachment().get();

		if (/*player == null || player.isNotRegistered() || */!generator.isRunning() && generator.getRecords() == null) {
			cancel();
			return;
		}
		if (!generator.isRunning()) {
			cancel();

			List<DailyStatisticRecordRow> records = generator.getRecords().stream().sorted(Comparator.comparing(DailyStatisticRecordRow::getTimestamp)).collect(Collectors.toList());

			if (records.isEmpty()) {
				player.message("There are no records that can be found.");
			} else {
				records.forEach(record -> player.message(String.format("Date=[%s], New Players=[%s], Return Players=[%s].",
				 record.getTimestamp().format(DateTimeFormatter.ISO_DATE), record.getNewAccounts(), record.getReturnAccounts())));
			}
		}
	}

	/**
	 * Blank method, called when the task is stopped.
	 */
	@Override
	protected void onCancel() {
		Player player = (Player) getAttachment().get();

		player.getAttributeMap().reset(PrivilegedAttributes.DAILY_STATISTICS_GENERATOR_TASK);
	}
}
