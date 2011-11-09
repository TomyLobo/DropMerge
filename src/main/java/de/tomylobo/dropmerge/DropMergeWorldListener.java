package de.tomylobo.dropmerge;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.event.world.WorldListener;
import org.bukkit.plugin.PluginManager;

public class DropMergeWorldListener extends WorldListener {
	DropMerge plugin;

	public DropMergeWorldListener(DropMerge plugin) {
		this.plugin = plugin;

		PluginManager pm = plugin.getServer().getPluginManager();
		pm.registerEvent(Type.CHUNK_LOAD, this, Priority.Monitor, plugin);
		pm.registerEvent(Type.CHUNK_UNLOAD, this, Priority.Monitor, plugin);
	}


	@Override
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
	
	@Override
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
