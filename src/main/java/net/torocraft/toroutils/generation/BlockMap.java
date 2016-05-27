package net.torocraft.toroutils.generation;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;

import com.google.common.base.Predicate;

import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockMap {

	private final BlockPos origin;
	private final World world;
	private String blockMap;
	private String[] lines;
	private String delimiter;
	private int lineIndex;
	private String line;
	private int y = -1;
	private int x;
	private int z;
	private int pass = 1;
	private int maxPass = 1;
	private Map<Character, PaletteEntry> palette;
	private boolean paletteParsed = false;

	private static final PaletteEntry DEFAULT_PALETTE_ENTRY;

	static {
		DEFAULT_PALETTE_ENTRY = new PaletteEntry();
		DEFAULT_PALETTE_ENTRY.block = Blocks.air.getDefaultState();
		DEFAULT_PALETTE_ENTRY.pass = 1;
	}

	public static class PaletteEntry {
		IBlockState block;
		int pass = 1;
	}

	public BlockMap(World world, BlockPos origin) {
		this.world = world;
		this.origin = origin;
	}

	public void loadLocalFile(String path) {
		InputStream is = this.getClass().getResourceAsStream(path);
		if (is == null) {
			throw new RuntimeException("file not found: " + path);
		}
		try {
			blockMap = IOUtils.toString(is);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void generate() {
		palette = new HashMap<Character, PaletteEntry>();
		palette.put(' ', DEFAULT_PALETTE_ENTRY);
		splitIntoLines();
		parseDelimiter();

		

		for (pass = 1; pass <= maxPass; pass++) {
			System.out.println("starting block scan MAXPASS[" + maxPass + "] PASS[" + pass + "]");
			for (lineIndex = 1; lineIndex < lines.length; lineIndex++) {
				line = lines[lineIndex];
				handleLine();
			}
			y = -1;
		}

	}

	private void handleLine() {

		if (line == null) {
			return;
		}

		if (delimiter.equals(line)) {
			y++;
			z = 0;
			return;
		}

		palette.put(' ', null);

		if (y < 0) {
			parsePalette();
		} else {
			paletteParsed = true;
			generateLine();
			z++;
		}
	}

	private void parsePalette() {

		if (paletteParsed) {
			return;
		}

		if (line.length() < 3) {
			System.out.println("Invalid palette line [" + line + "]");
			return;
		}

		System.out.println("parsing palette line [" + line + "]");
		char c = line.substring(0, 1).toCharArray()[0];
		String blockType = line.substring(2);

		// N=minecraft:dark_oak_stairs|facing:north

		System.out.println("palette: [" + c + "][" + blockType + "]");

		palette.put(c, parsePaleteBlock(blockType));

	}

	private PaletteEntry parsePaleteBlock(String blockString) {

		PaletteEntry entry = new PaletteEntry();

		String[] a = blockString.split("\\|");

		entry.block = Block.blockRegistry.getObject(new ResourceLocation(a[0])).getDefaultState();
		
		
		if(a[0].equals("minecraft:wooden_slab")){
			entry.block = entry.block.withProperty(BlockSlab.HALF, BlockSlab.EnumBlockHalf.TOP);
		}

		if (a.length > 0) {
			for (int i = 1; i < a.length; i++) {
				String propertyString = a[i];

				if (propertyString == null) {
					continue;
				}

				if (propertyString.matches("^\\(\\d+\\)$")) {
					System.out.println("found pass number [" + propertyString + "]");
					parsePaletePassNumber(propertyString, entry);
				}else{
					parsePaleteBlockParameter(a[0], propertyString, entry);
				}
			}
		}

		return entry;
	}

	private void parsePaletePassNumber(String propertyString, PaletteEntry entry) {
		System.out.println("parsing pass [" + propertyString + "]");
		entry.pass = Integer.parseInt(propertyString.replaceAll("[^0-9]", ""), 10);
		maxPass = Math.max(maxPass, entry.pass);
	}

	private void parsePaleteBlockParameter(String blockname, String propertyString, PaletteEntry entry) {
		String[] aProp = propertyString.split(":");

		if (aProp.length != 2) {
			return;
		}

		IProperty prop = null;
		Object value = null;

		if ("facing".equals(aProp[0])) {
			// 

			// prop = PropertyDirection.create("facing", new Predicate() {
			// };<EnumFacing><EnumFacing>();

			
			if(blockname.equals("minecraft:torch")){
				prop = PropertyDirection.create("facing", new Predicate<EnumFacing>() {
					public boolean apply(EnumFacing p_apply_1_) {
						return p_apply_1_ != EnumFacing.DOWN;
					}
				});
				//FIXME
				//return;
			}else{
				prop = BlockHorizontal.FACING;
			}
			
			
		} // PropertyDirection

		if ("north".equals(aProp[1])) {
			value = EnumFacing.NORTH;
		} else if ("south".equals(aProp[1])) {
			value = EnumFacing.SOUTH;
		}
		
		

		if (value == null || prop == null) {
			return;
		}

		System.out.println("adding property [" + aProp[0] + "] [" + aProp[1] + "]");

		entry.block = entry.block.withProperty(prop, value);
	}

	private void parseDelimiter() {
		delimiter = lines[0];
		System.out.println("delim: [" + delimiter + "]");
	}

	private void splitIntoLines() {
		lines = blockMap.split("\n\r?");
	}

	private void generateLine() {
		System.out.println("placing line PASS[" + pass + "] [" + line + "] [" + x + "][" + y + "][" + z + "]");
		x = 0;
		char[] a = line.toCharArray();
		for (char c : a) {
			placeBlock(c);
			x++;
		}
	}

	private void placeBlock(char c) {

		if (c == ' ') {
			return;
		}
		
		PaletteEntry entry = palette.get(c);

		if (entry == null) {
			entry = DEFAULT_PALETTE_ENTRY;
			System.out.println("palette entry not found for [" + c + "]");
		}
		
		if(entry.pass != pass){
			return;
		}

		world.setBlockState(cursorCoords(), entry.block, 3);
	}

	private boolean onAirBlock() {
		IBlockState currentBlock = world.getBlockState(cursorCoords());
		return !currentBlock.isOpaqueCube();
	}

	private BlockPos cursorCoords() {
		return origin.add(x, y, z);
	}

}
