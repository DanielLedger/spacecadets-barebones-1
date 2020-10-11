package com.github.DanL.Barebones;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Instant;

public class CompiledExec {
    int[] mem; //The program memory.
	byte[] prog; //The actual bytecode.
	
	private byte INCR = (byte) 0x80;
	private byte DECR = 0x40;
	private byte CLEAR = 0x0;
	private byte GOTO = (byte) 0xb0;
	
	/**
	 * Loads the bytecode from f into memory.
	 * @param f - The bytecode to run.
	 * @throws IOException 
	 */
	public CompiledExec(File f) throws IOException {
		FileInputStream in = new FileInputStream(f);
		byte[] metadata = new byte[10];
		in.read(metadata);
		for (byte b: metadata) {
			System.out.println(b);
		}
		//We're assuming we haven't been tricked into attempting to run something dumb like a zip file, so we won't check.
		//Bonus points for doing that and seeing what the mess of the output is (probably an ArrayIndexOutOfBoundsException).
		short memLen = (short) ((metadata[4] << 8) + metadata[5]);
		int progLen = 0;
		for (byte i = 6; i < 10; i++) {
			progLen = progLen << 8;
			progLen += metadata[i];
		}
		System.out.println("Memory length: " + memLen);
		System.out.println("Program length: " + progLen);
		mem = new int[memLen];
		prog = new byte[progLen];
		in.read(prog);
		in.close();
	}
	
	private void printMem() {
		//The memory has no context, just vomit it onto the screen.
		System.out.println("----------------");
		for (int memCell: mem) {
			System.out.println(memCell);
		}
	}
	
	/**
	 * This is the method to actually run our bytecode.
	 * @param printMem - Should we output the contents of our memory after every run, or at the end?
	 * @throws InvalidInstructionException 
	 */
	public void execute(boolean printMem) throws InvalidInstructionException {
		int pc = 0;
		while (pc < prog.length) {
			byte instruction = prog[pc];
			int varPointer = prog[pc+1] << 8 + prog[pc + 2]; //It's fine, it'll work the same way even though it should be a short.
			if (instruction == INCR) {
				//Instruction is INC
				mem[varPointer] = mem[varPointer] + 1;
			}
			else if (instruction == DECR) {
				//Instruction is DEC
				mem[varPointer] = mem[varPointer] - 1;
			}
			else if (instruction == CLEAR) {
				//CLEAR
				mem[varPointer] = 0;
			}
			else if (instruction == GOTO) {
				//GOTO IF NOT ZERO
				System.out.println("GOTO");
				if (mem[varPointer] != 0) {
					int newPointer = 0;
					for (int i = pc + 3; i < pc + 7; i++) {
						newPointer = newPointer << 8;
						newPointer += prog[i];
					}
					System.out.println(newPointer);
					pc = newPointer;
					pc -= 7; //We're about to add 7, so we need to ensure the pointer lands in the right place.
				}
			}
			else {
				throw new InvalidInstructionException(String.valueOf(instruction), pc);
			}
			pc += 7;
			if (printMem) {
				printMem();
			}
		}
		System.out.println("END");
		printMem();
	}
	
	/**
	 * Times the program's execution.
	 * @param debugOutput - Should we print variable values at every step, or just at the end?
	 * @return - The time in milliseconds to run the program.
	 * @throws InvalidInstructionException - if an instruction couldn't be parsed.
	 */
	public long executeTimed(boolean debugOutput) throws InvalidInstructionException {
		long timeNow = Instant.now().toEpochMilli();
		execute(debugOutput);
		return Instant.now().toEpochMilli() - timeNow;
	}
}
