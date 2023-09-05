package clueGame;

import java.io.*;

/**
 * BadConfigException extends Exception class
 * 
 * @author Anastasia Velyhko
 * @author Gordon Dina
 *
 */

public class BadConfigFormatException extends Exception {
	public BadConfigFormatException() { super("Error: Incorrect Board Layout"); } //default message
	public BadConfigFormatException(String message) throws Exception {
		super(message); //custom message
		logMessage(message); //log messages into file
	}
	
	public void logMessage(String message) throws Exception {
		PrintWriter pw = new PrintWriter(new FileWriter("logfile.txt", true));
		pw.println(message);
		pw.close(); //close file
	}
}
