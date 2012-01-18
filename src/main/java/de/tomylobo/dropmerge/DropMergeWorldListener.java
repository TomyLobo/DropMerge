package de.tomylobo.dropmerge;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

public class DropMergeWorldListener implements Listener {
	DropMerge plugin;

	public DropMergeWorldListener(DropMerge plugin) {
		this.plugin = plugin;

		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler(event = ChunkLoadEvent.class, priority = EventPriority.MONITOR)
	public void onChunkLoad(ChunkLoadEvent event) {
		for (Entity entity : event.getChunk().getEntities()) {
			if (!(entity instanceof Item)) {
				// Exclude non-items
				continue;
			}

			final Item item = (Item) entity;

			//System.out.println("load: "+item.getEntityId()+" - "+item.getItemStack());

			switch (item.getItemStack().getTypeId()) {
			case 81: // cactus
				break;

			default:
				return;
			}

			plugin.items.add(item);
		}
	}

	@EventHandler(event = ChunkUnloadEvent.class, priority = EventPriority.MONITOR)
	public void onChunkUnload(ChunkUnloadEvent event) {
		for (Entity entity : event.getChunk().getEntities()) {
			if (!(entity instanceof Item)) {
				// Exclude non-items
				continue;
			}

			final Item item = (Item) entity;

			//System.out.println("unload: "+item.getEntityId()+" - "+item.getItemStack());

			switch (item.getItemStack().getTypeId()) {
			case 81: // cactus
				break;

			default:
				return;
			}

			plugin.unloadedItems.add(item);
		}
	}
}
