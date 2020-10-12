package com.github.DanL.Barebones;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Main {

	private static enum Mode{
		RUN,
		COMPILE,
		JIT
	}
	
	public static void main(String[] args) throws InvalidInstructionException {
		try {
			File program = new File(args[0]);
			boolean hasVFlag = false; boolean hasTFlag = false;
			Mode m = Mode.RUN;
			for (String s: args) {
				if (s.contentEquals("-v")) {
					hasVFlag = true;
				}
				else if (s.contentEquals("-t")) {
					hasTFlag = true;
				}
				else if (s.contentEquals("-c")) {
					m = Mode.COMPILE;
				}
				else if (s.contentEquals("-j")) {
					m = Mode.JIT;
				}
			}
			if (m == Mode.RUN) {
				//We need to detect if this file need the text interpreter or bytecode interpreter.
				if (Compiler.isCompiledCode(program)) {
					CompiledExec programRunner = new CompiledExec(program);
					if (!hasTFlag) {
						programRunner.execute(!hasVFlag);
					}
					else {
						long timeTaken = programRunner.executeTimed(!hasVFlag);
						System.out.println("Execution completed in " + timeTaken + " ms.");
					}
				}
				else {
					Interpreter programRunner = new Interpreter(program);
					if (!hasTFlag) {
						programRunner.execute(!hasVFlag);
					}
					else {
						long timeTaken = programRunner.executeTimed(!hasVFlag);
						System.out.println("Execution completed in " + timeTaken + " ms.");
					}
				}
			}
			else if (m == Mode.COMPILE) {
				Compiler comp = new Compiler(program);
				ByteArrayOutputStream bytecode = comp.compile(!hasVFlag);
				File byteCodeOut = new File(args[0] + ".comp");
				FileOutputStream writer = new FileOutputStream(byteCodeOut);
				writer.write(bytecode.toByteArray());
				writer.close();
			}
			else if (m == Mode.JIT) {
				Compiler comp = new Compiler(program);
				ByteArrayOutputStream bytecode = comp.compile(!hasVFlag);
				CompiledExec programRunner = new CompiledExec(bytecode.toByteArray());
				if (!hasTFlag) {
					programRunner.execute(!hasVFlag);
				}
				else {
					long timeTaken = programRunner.executeTimed(!hasVFlag);
					System.out.println("Execution completed in " + timeTaken + " ms.");
				}
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
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Something went wrong either compiling or saving the result!");
		}
		
	}

}
