package darwin;

/**
 * This class includes the functions necessary to keep track of the creatures in
 * a two-dimensional world. 
 */

public class World {
	private Matrix<Creature> creatures;
	
	/**
	 * This method creates a new world consisting of width columns and height
	 * rows, each of which is numbered beginning at 0. A newly created world
	 * contains no objects.
	 * @param w Width of world
	 * @param h Height of world
	 */
	public World(int w, int h) {
		creatures = new Matrix<Creature>(h, w);
	}

	/**
	 * Returns the height of the world.
	 */
	public int height() {
		return creatures.numRows();
	}

	/**
	 * Returns the width of the world.
	 */
	public int width() {
		return creatures.numCols();
	}

	/**
	 * Returns whether pos is in the world or not.
	 * @param pos Position we're checking in world
	 * returns true *if* pos is an (x,y) location within the bounds of the board.
	 */
	public boolean inRange(Position pos) {
		if(pos == null) {
			return false;
		}
		int x = pos.getX();
		int y = pos.getY();
		return (x >= 0 && x < width() && y >= 0 && y < height());
	}

	/**
	 * Set a position on the board to contain e.
	 * @param pos Position we're checking in world
	 * @param e Creature we're filling in pos 
	 * @throws IllegalArgumentException if pos is not in range
	 */
	public void set(Position pos, Creature e) {
		if(!inRange(pos)){
			throw new IllegalArgumentException("Position out of bounds: " + pos);
		}
		creatures.set(pos.getY(), pos.getX(), e);
	}

	/**
	 * Return the contents of a position on the board.
	 * @param pos Position we're checking in world
	 * @throws IllegalArgumentException if pos is not in range
	 */
	public Creature get(Position pos) {
		if(!inRange(pos)){
			throw new IllegalArgumentException("Position out of bounds: " + pos);
		}
		return creatures.get(pos.getY(), pos.getX());
	}

}
