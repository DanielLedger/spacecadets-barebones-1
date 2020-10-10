package com.github.DanL.Barebones;

import java.io.File;
import java.io.FileNotFoundException;

public class Main {

	public static void main(String[] args) throws InvalidInstructionException {
		//For now, all files are text, interpreted script files. THIS WILL PROBABLY CHANGE.
		try {
			File program = new File(args[0]);
			boolean hasVFlag = false; boolean hasTFlag = false;
			for (String s: args) {
				if (s.contentEquals("-v")) {
					hasVFlag = true;
				}
				else if (s.contentEquals("-t")) {
					hasTFlag = true;
				}
			}
			Interpreter programRunner = new Interpreter(program);
			if (!hasTFlag) {
				programRunner.execute(!hasVFlag);
			}
			else {
				long timeTaken = programRunner.executeTimed(!hasVFlag);
				System.out.println("Execution completed in " + timeTaken + " ms.");
			}
		}
		catch (InvalidInstructionException e) {
			e.printFailedLine();
		} 
		catch (FileNotFoundException e) {
			System.out.println("Invalid filename specified!");
		}
		catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("Usage: java -jar Challenge-2.jar <filename> [options]");
		}
		
	}

}
