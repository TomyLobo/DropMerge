package de.tomylobo.dropmerge;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class DropMerge extends JavaPlugin implements Runnable {
	private static final int DELAY_MERGE_TICKS = 40;
	private static final int NUM_CHECKED_PER_TICK = 1;
	private static final int CHECK_DISTANCE = 1;

	List<Item> items = new LinkedList<Item>();
	Set<Item> unloadedItems = new HashSet<Item>();
	private int index = 0;

	@Override
	public void onEnable() {
		new DropMergeEntityListener(this);
		new DropMergeWorldListener(this);

		getServer().getScheduler().scheduleSyncRepeatingTask(this, this, 0, 1);

		// Start a cleanup task to remove dead items that would otherwise accumulate
		getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() { public void run() {
			for (Iterator<Item> it = unloadedItems.iterator(); it.hasNext() ;) {
				Item item = it.next();
				if (item.isDead()) {
					it.remove();
				}
			}
		}}, 0, 20);

		System.out.println("DropMerge enabled.");
	}

	@Override
	public void onDisable() {
		System.out.println("DropMerge disabled.");
	}

	@Override
	public void run() {
		if (items.isEmpty()) return;

		for (int i = 0; i < NUM_CHECKED_PER_TICK; ++i) {
			final Item item = items.get(index);

			if (unloadedItems.remove(item) || item.isDead()) {
				// Get rid of removed entries
				//System.out.println("Removing "+item.getEntityId());
				items.remove(index);
				if (index >= items.size()) {
					index = 0;
				}
				continue;
			}

			if (item.getTicksLived() < DELAY_MERGE_TICKS) {
				continue;
			}

			final ItemStack itemStack = item.getItemStack();
			final int typeId = itemStack.getTypeId();
			final short durability = itemStack.getDurability();

			int amount = itemStack.getAmount();

			final List<Entity> nearbyEntities = item.getNearbyEntities(CHECK_DISTANCE, CHECK_DISTANCE, CHECK_DISTANCE);
			for (Entity entity : nearbyEntities) {
				if (!(entity instanceof Item)) {
					// Exclude non-items
					continue;
				}

				final Item otherItem = (Item) entity;

				if (otherItem.isDead()) {
					// These shouldn't occur, but we still skip them, just to be sure
					continue;
				}

				final ItemStack otherItemStack = otherItem.getItemStack();
				if (typeId != otherItemStack.getTypeId()) {
					// Make sure the type matches
					continue;
				}

				if (durability != otherItemStack.getDurability()) {
					// Make sure the damage value matches
					continue;
				}

				amount += otherItemStack.getAmount();

				otherItem.remove();
				//System.out.println("Merged "+otherItem.getEntityId()+" into "+item.getEntityId());
			}

			itemStack.setAmount(amount);

			++index;
			if (index >= items.size()) {
				index = 0;
			}
		}
	}
}
