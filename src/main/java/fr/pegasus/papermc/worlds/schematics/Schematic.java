package fr.pegasus.papermc.worlds.schematics;

import com.google.common.collect.Sets;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.session.PasteBuilder;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class Schematic {

    private final Clipboard clipboard;
    private final File schematicFile;
    private final Set<SchematicFlags> flags;

    public Schematic(final @NotNull Plugin plugin, final @NotNull String name, final SchematicFlags... flags) {
        File pluginFile = new File("schematics/" + name + ".schem");
        this.schematicFile = new File(plugin.getDataFolder() + "/" + pluginFile.getPath());
        this.flags = Sets.newHashSet(flags);
        if(!schematicFile.exists()) {
            plugin.saveResource(pluginFile.getPath(), false);
        }
        try {
            this.clipboard = this.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Schematic(final @NotNull File schematicFile, final SchematicFlags... flags) {
        this.schematicFile = schematicFile;
        this.flags = Sets.newHashSet(flags);
        try {
            this.clipboard = this.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Clipboard load() throws IOException {
        Clipboard localClipboard;
        ClipboardFormat format = ClipboardFormats.findByFile(this.schematicFile);

        assert format != null;
        try (ClipboardReader reader = format.getReader(new FileInputStream(this.schematicFile))) {
            localClipboard = reader.read();
        }

        return localClipboard;
    }

    public void paste(final @NotNull Location location) throws WorldEditException {
        try (EditSession editSession = WorldEdit.getInstance().newEditSession(BukkitAdapter.adapt(location.getWorld()))) {
            ClipboardHolder clipboardHolder = new ClipboardHolder(this.clipboard);
            PasteBuilder pasteBuilder = clipboardHolder.createPaste(editSession)
                    .to(BlockVector3.at(location.getX(), location.getY(), location.getZ()))
                    .ignoreAirBlocks(this.flags.contains(SchematicFlags.IGNORE_AIR))
                    .copyEntities(this.flags.contains(SchematicFlags.COPY_ENTITIES))
                    .copyBiomes(this.flags.contains(SchematicFlags.COPY_BIOMES));
            Operation operation = pasteBuilder.build();
            Operations.complete(operation);
        }
    }

    public Map<Material, Integer> getBlockCount(){
        HashMap<Material, Integer> blockCount = new HashMap<>();
        for(BlockVector3 blockVector3: this.clipboard.getRegion()){
            Material material = BukkitAdapter.adapt(this.clipboard.getBlock(blockVector3).getBlockType());
            if(blockCount.containsKey(material)){
                blockCount.put(material, blockCount.get(material) + 1);
            }else{
                blockCount.put(material, 1);
            }
        }
        return blockCount;
    }
}
