package com.github.DanL.Barebones;

import java.io.File;
import java.io.FileNotFoundException;

public class Main {

	public static void main(String[] args) throws FileNotFoundException, InvalidInstructionException {
		//For now, all files are text, interpreted script files. THIS WILL PROBABLY CHANGE.
		File program = new File(args[0]);
		Interpreter programRunner = new Interpreter(program);
		programRunner.execute(true);
	}

}
