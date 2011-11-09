package de.tomylobo.dropmerge;

import org.bukkit.entity.Item;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.event.entity.EntityListener;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.plugin.PluginManager;

public class DropMergeEntityListener extends EntityListener {
	DropMerge plugin;

	public DropMergeEntityListener(DropMerge plugin) {
		this.plugin = plugin;

		PluginManager pm = plugin.getServer().getPluginManager();
		pm.registerEvent(Type.ITEM_SPAWN, this, Priority.Monitor, plugin);
	}

	private Item lastItem = null;

	@Override
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
