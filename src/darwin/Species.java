package darwin;

import java.io.*;
import java.util.ArrayList;

/**
 * The individual creatures in the world are all representatives of some
 * species class and share certain common characteristics, such as the species
 * name and the program they execute. Rather than copy this information into
 * each creature, this data can be recorded once as part of the description for
 * a species and then each creature can simply include the appropriate species
 * reference as part of its internal data structure.
 * 
 * Note: The instruction addresses start at one, not zero.
 */
public class Species {
	private String name;
	private String color;
	private char speciesChar; // the first character of Species name
	private ArrayList<Instruction> program;

	/**
	 * Create a species for the given fileReader. 
	 * @param BufferedReader formatted from appendix to read species type
	 */
	public Species(BufferedReader fileReader) {
		try {
			this.program = new ArrayList<Instruction>();
			name = fileReader.readLine();
			color = fileReader.readLine();
			speciesChar = name.charAt(0);
			String line = fileReader.readLine();
			while (line != null) {
				String[] commands = line.split(" ");
				int opcode = 0;
				switch (commands[0]) {
					case "hop":
						opcode = 1;
						break;
					case "left":
						opcode = 2;
						break;
					case "right":
						opcode = 3;
						break;
					case "infect":
						opcode = 4;
						break;
					case "ifempty":
						opcode = 5;
						break;
					case "ifwall":
						opcode = 6;
						break;
					case "ifsame":
						opcode = 7;
						break;
					case "ifenemy":
						opcode = 8;
						break;
					case "ifrandom":
						opcode = 9;
						break;
					case "go":
						opcode = 10;
						break;
					default:
						break;
				}
				if (opcode >= 5 && opcode < 11) {
					program.add(new Instruction(opcode, Integer.valueOf(commands[1])));
				} else if (opcode > 0) {
					program.add(new Instruction(opcode));
				}
				if (line != "") {
					line = fileReader.readLine();
				} else {
					line = null;
				}
			}
			
		} catch (IOException e) {
			System.out.println(
				"Could not read file '"
					+ fileReader
					+ "'");
			System.exit(1);
		}
		System.out.println(program);
	}


	/**
	* Return the char for the species
	* @return species character, first letter in speices
	*/
	public char getSpeciesChar() {
		return speciesChar;
	}

	/**
	 * Return the name of the species.
	 * @return name of the species
	 */
	public String getName() {
		return name;
	}

	/**
	 * Return the color of the species.
	 * @return color of the species
	 */
	public String getColor() {
		return color;
	}

	/**
	 * Return the number of instructions in the program.
	 * @return size of program
	 */
	public int programSize() {
		return program.size();
	}

	/**
	 * Return an instruction from the program.
	 * @pre 1 <= i <= programSize().
	 * @post returns instruction i of the program.
	 */
	public Instruction programStep(int i) {
		return program.get(i - 1);
	}

	/**
	 * Return a String representation of the program.
	 * 
	 * do not change
	 */
	public String programToString() {
		String s = "";
		for (int i = 1; i <= programSize(); i++) {
			s = s + (i) + ": " + programStep(i) + "\n";
		}
		return s;
	}
}