package darwin;

import java.util.*;

/**
 * This class represents one creature on the board. Each creature must remember
 * its species, position, direction, and the world in which it is living.
 * In addition, the Creature must remember the next instruction out of its
 * program to execute.
 * The creature is also responsible for making itself appear in the WorldMap. In
 * fact, you should only update the WorldMap from inside the Creature class.
 */
public class Creature {
	private Species species;
	private final World world;
	private Position pos;
	private int dir;
	private int nextInstruction; // 1-indexed per spec
	private static final Random RNG = new Random();

	/**
	 * Create a creature of the given species, with the indicated position and
	 * direction. Note that we also pass in the world-- remember this world, so
	 * that you can check what is in front of the creature and update the board
	 * when the creature moves.
	 */
	public Creature(Species species, World world, Position pos, int dir) {
		if (species == null || world == null || pos == null) {
			throw new IllegalArgumentException("species/world/pos must not be null");
		}
		if (!world.inRange(pos)) {
			throw new IllegalArgumentException("Position out of bounds: " + pos);
		}
		if (world.get(pos) != null) {
			throw new IllegalArgumentException("Position already occupied: " + pos);
		}

		this.species = species;
		this.world = world;
		this.pos = pos;
		this.dir = dir;
		this.nextInstruction = 1;

		world.set(pos, this);
		WorldMap.displaySquare(pos, species.getSpeciesChar(), dir, species.getColor());
	}

	/**
	 * Return the species of the creature.
	 */
	public Species species() {
		return species;
	}

	/**
	 * Return the current direction of the creature.
	 */
	public int direction() {
		return dir;
	}

	/**
	 * Sets the current direction of the creature to the given value 
	 * 	@param dir the new direction (Position.NORTH/EAST/SOUTH/WEST)
	 */
	public void setDirection(int dir){
		this.dir = dir;
		WorldMap.displaySquare(pos, species.getSpeciesChar(), this.dir, species.getColor());
	}

	/**
	 * Return the position of the creature.
	 */
	public Position position() {
		return pos;
	}

	/**
	 * Execute steps from the Creature's program
	 *   starting at step #1
	 *   continue until a hop, left, right, or infect instruction is executed.
	 */
	public void takeOneTurn() {
		while (true) {
			if (nextInstruction < 1 || nextInstruction > species.programSize()) {
				nextInstruction = 1;
			}

			Instruction inst = species.programStep(nextInstruction);
			int op = inst.getOpcode();

			Position ahead = pos.getAdjacent(dir);
			boolean inRangeAhead = world.inRange(ahead);
			Creature aheadCreature = inRangeAhead ? world.get(ahead) : null; 

			switch (op) {
				case Instruction.HOP: {
					nextInstruction++;
					if (inRangeAhead && aheadCreature == null) { // only move into empty in-bounds cell
						WorldMap.displaySquare(pos, ' ', dir, "black"); // clear old cell
						world.set(pos, null);

						pos = ahead;
						world.set(pos, this);
						WorldMap.displaySquare(pos, species.getSpeciesChar(), dir, species.getColor());
					}
					return;
				}

				case Instruction.LEFT: {
					nextInstruction++;
					dir = leftFrom(dir);
					WorldMap.displaySquare(pos, species.getSpeciesChar(), dir, species.getColor());
					return;
				}

				case Instruction.RIGHT: {
					nextInstruction++;
					dir = rightFrom(dir);
					WorldMap.displaySquare(pos, species.getSpeciesChar(), dir, species.getColor());
					return;
				}

				case Instruction.INFECT: {
					nextInstruction++;
					if (inRangeAhead && aheadCreature != null && aheadCreature.species != this.species) {
						aheadCreature.species = this.species; // convert enemy
						aheadCreature.nextInstruction = 1; // restart program
						WorldMap.displaySquare(
							aheadCreature.pos,
							aheadCreature.species.getSpeciesChar(),
							aheadCreature.dir,
							aheadCreature.species.getColor()
						);
					}
					return;
				}

				case Instruction.IFEMPTY: {
					int addr = inst.getAddress();
					if (addr == 0) throw new IllegalArgumentException("ifempty missing address");
					nextInstruction = (inRangeAhead && aheadCreature == null) ? addr : (nextInstruction + 1);
					break;
				}

				case Instruction.IFWALL: {
					int addr = inst.getAddress();
					if (addr == 0) throw new IllegalArgumentException("ifwall missing address");
					nextInstruction = (!inRangeAhead) ? addr : (nextInstruction + 1);
					break;
				}

				case Instruction.IFSAME: {
					int addr = inst.getAddress();
					if (addr == 0) throw new IllegalArgumentException("ifsame missing address");
					boolean same = (inRangeAhead && aheadCreature != null && aheadCreature.species == this.species);
					nextInstruction = same ? addr : (nextInstruction + 1);
					break;
				}

				case Instruction.IFENEMY: {
					int addr = inst.getAddress();
					if (addr == 0) throw new IllegalArgumentException("ifenemy missing address");
					boolean enemy = (inRangeAhead && aheadCreature != null && aheadCreature.species != this.species);
					nextInstruction = enemy ? addr : (nextInstruction + 1);
					break;
				}

				case Instruction.IFRANDOM: {
					int addr = inst.getAddress();
					if (addr == 0) throw new IllegalArgumentException("ifrandom missing address");
					nextInstruction = RNG.nextBoolean() ? addr : (nextInstruction + 1);
					break;
				}

				case Instruction.GO: {
					int addr = inst.getAddress();
					if (addr == 0) throw new IllegalArgumentException("go missing address");
					nextInstruction = addr;
					break;
				}

				default:
					throw new IllegalStateException("Unknown opcode: " + op);
			}
		}
	}

	/**
	 * Return the compass direction the is 90 degrees left of the one passed in.
	 */
	public static int leftFrom(int direction) {
		return (4 + direction - 1) % 4;
	}

	/**
	 * Return the compass direction the is 90 degrees right of the one passed
	 * in.
	 */
	public static int rightFrom(int direction) {
		return (direction + 1) % 4;
	}
}
