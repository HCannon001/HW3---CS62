package darwin;

import java.util.ArrayList;
import java.util.Random;
import java.io.*;

/**
 * This class controls the simulation. The design is entirely up to you. You
 * should include a main method that takes the array of species file names
 * passed in and populates a world with species of each type. You class should
 * be able to support anywhere between 1 to 4 species.
 * 
 * Be sure to call the WorldMap.pause() method every time through the main
 * simulation loop or else the simulation will be too fast. For example:
 * 
 * 
 * public void simulate() { 
 * 	for (int rounds = 0; rounds < numRounds; rounds++) {
 * 		giveEachCreatureOneTurn(); 
 * 		WorldMap.pause(500); 
 * 	} 
 * }
 * 
 */
class Darwin {

	private World world;
	private Species[] species;
	private ArrayList<Creature> creatures = new ArrayList<Creature>();
	
	/**
	 * Populates species array and creatures array to fill the board starting them in random direction and locations
	 * @param speciesFilenames
	*/
	public Darwin(String[] speciesFilenames) {
		//create world for creatures to live in
		world = new World(15, 15);

		//populate species array with inputed species types
		species = new Species[speciesFilenames.length];
		for (int i = 0; i < speciesFilenames.length; i++) {
			try {
				species[i] = new Species(new BufferedReader(new FileReader(speciesFilenames[i])));
			}
			catch (IOException e) {
				System.err.println(e.getMessage());
			}
		}
		//create 10 creatures for each species
		for (int i = 0; i < species.length; i++) {
			createCreatures(10, species[i]);
		}
	}

	/**
	 * Input an amount and a species type and the function will create the inputed amount of creatures on the map
	 * They will be put into the creatures ArrayList
	 * @param count The amount of creatures to create
	 * @param species The species we are Creating
	 */
	private void createCreatures(int count, Species species) {
		//create random object
		Random random = new Random();

		for (int j = 0; j < count; j++) {
			//generate a random start location
			int randX;
			int randY;
			do {
				randX = random.nextInt(world.width());
				randY = random.nextInt(world.height());
			} while (world.get(new Position(randX, randY)) != null);
			//starting position of creature
			Position position = new Position(randX, randY);
			//starting direction of creature
			int dir = random.nextInt(4);
			creatures.add(new Creature(species, world, position, dir));
		}
	}

	/**
	 * Itterate through each creature and call the takeOneTurn function for each creature
	 */
	private void giveEachCreatureOneTurn() {
		for (Creature creature : creatures) {
			creature.takeOneTurn();
		}
	}

	/**
	 * The array passed into main will include the arguments that appeared on the
	 * command line. For example, running "java Darwin Hop.txt Rover.txt" will call
	 * the main method with s being an array of two strings: "Hop.txt" and
	 * "Rover.txt".
	 * 
	 * The autograder will always call the full path to the creature files, for
	 * example "java Darwin /home/user/Desktop/Assignment02/Creatures/Hop.txt" So
	 * please keep all your creates in the Creatures in the supplied
	 * Darwin/Creatures folder.
	 *
	 * To run your code you can either: supply command line arguments through
	 * VS code (add a launch.json file in the folder .vscode according to instructions and add/edit the following attribute
     * `"args": ["whateverArgumentsYouWantSeparatedByComma"]`) or by creating a temporary array
	 * with the filenames and passing it to the Darwin constructor. If you choose
	 * the latter options, make sure to change the code back to: Darwin d = new
	 * Darwin(s); before submitting. If you want to use relative filenames for the
	 * creatures they should be of the form "./Creatures/Hop.txt".
	 * 
	 */
	public static void main(String s[]) {
		Darwin d = new Darwin(s);
		d.simulate();
	}

	public void simulate() {
		// don't forget to call pause somewhere in the simulator's loop...
		// make sure to pause using WorldMap so that TAs can modify pause time
		// when grading
		for (int i = 0; i < 200; i ++) {
			giveEachCreatureOneTurn();
			WorldMap.pause(500);
		}
	}
}
