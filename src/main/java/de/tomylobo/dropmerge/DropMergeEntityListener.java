package de.tomylobo.dropmerge;

import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemSpawnEvent;

public class DropMergeEntityListener implements Listener {
	DropMerge plugin;

	public DropMergeEntityListener(DropMerge plugin) {
		this.plugin = plugin;

		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	private Item lastItem = null;

	@EventHandler(event = ItemSpawnEvent.class, priority = EventPriority.MONITOR)
	public void onItemSpawn(ItemSpawnEvent event) {
		final Item item = (Item) event.getEntity();

		// Filter out duplicate consecutive item spawn events (bukkit bug)
		if (item == lastItem) return;
		lastItem = item;

		//System.out.println("spawn: "+item.getEntityId()+" - "+item.getItemStack());

		switch (item.getItemStack().getTypeId()) {
		case 81: // cactus
			break;

		default:
			return;
		}

		plugin.items.add(item);
	}
}
