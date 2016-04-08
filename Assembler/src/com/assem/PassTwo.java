package com.assem;

import java.io.File;
import java.math.BigInteger;
import java.util.Formatter;
import java.util.Scanner;

import com.assem.Tables;

/**
 * 
 Created by ckhenson on 4/4/2016, with code from Assembler.java Created by efetsko on 3/28/2016.
 */
public class PassTwo 
{

    private static Scanner scan;
	private static Formatter obj;
	private static String fileName = "intermediateFile.txt";

    public static void passTwo(String fileName) {

        //open assembly the file
        openFile();
        //make the intermediate file that will be passed onto passTwo
        makeFile();

        //Loop as long as there's more data in the file
        while(scan.hasNext()) {

            //Local variables
            String line = scan.nextLine();
            String label = null;
            String opcode = null;
            String parameters = null;
            String address = null;
			String code = null;


            //Setup the values for the current line's address, label, opcode, and parameters if it exists
            if (!line.trim().isEmpty() && !line.startsWith(".")) {

                //First setup the values of this line's label, opcode, and parameters
            	
                String[] values = readLine(line);
                address = values[0];
                label = values[1];
                opcode = values[2];
                parameters = values[3];
                /***
                System.out.println(address);
                System.out.println(label);
                System.out.println(opcode);
                System.out.println(parameters);
                /****/
		if (parameters != null)
		{
			if (Tables.SYMTAB.get(parameters) != null)
				parameters = Tables.SYMTAB.get(parameters);
		}
				
		code = generate_code(opcode, parameters);
		//writeObjectCode(code);
            }

        }
        //close the files
        closeFile();
        closeObj();

    }
	
	private static String generate_code(String op, String params)
	{
		
		//need to filter different parameters
		// also need to filter instruction set extensions
		//lets assume format 3
		/**These functions filter different formats, modes, and data**/
		// this filters the opcode allowing different formats, modes, and data filtering
		String opFilter = opFilter(op);
		// this function will later generate addressing mode from parameters
		String AddressingMode = filterParamaters(params);
		/**After this there will be if statements to set up n, i x, b, p, and e for each formats, modes, and data**/
		// get the object code for the OPCODE
		op = Tables.OPTAB.get(op);
		/**for testing purposes, params will always be op (for cases such as #3**/
		params = op;
		/************************************************************************/
		// convert OPCODE and Params to binary
		op = hexToBin(op);
		params = hexToBin(params);
		/***
		System.out.println(params);
		System.out.println(op);
		/****/
		// pad with extra zeroes (for format 3)
		params = String.format("%012d", Integer.parseInt(params));
		//set up default values for testing purposes
		/****/
		int n = 0;
		int i = 0;
		int x = 0;
		int b = 0;
		int p = 0;
		int e = 0;
		/****/
		//need to figure out generation of code that leads to flags being set, flags for reference
		/******
		flags n & i:
			n=0 & i=1 immediate addressing - TA is used as an operand value (no memory reference)
			n=1 & i=0 indirect addressing - word at TA (in memory) is fetched & used as an address to fetch the operand from
			n=0 & i=0 simple addressing TA is the location of the operand
			n=1 & i=1 simple addressing same as n=0 & i=0
		flag x:
			x=1 indexed addressing add contents of X register to TA calculation
		flag b & p (Format 3 only):
			b=0 & p=0 direct addressing displacement/address field contains TA (note Format 4 always uses direct addressing)
			b=0 & p=1 PC relative addressing - TA=(PC)+disp (-2048<=disp<=2047)*
			b=1 & p=0 Base relative addressing - TA=(B)+disp (0<=disp<=4095)**
		flag e:
			e=0 use Format 3
			e=1 use Format 4
		***/
		// concatenate Opcode, flags, and Params
		String out = op + n + i + x + b + p + e + params;
		//System.out.println(out);
		// convert back to hex
		String ObjCode = binaryToHex(out);
		// print for testing
		System.out.println(ObjCode);
		// later will write to a file, or send to another function for writing to file
		return ObjCode;
	}

	private static String opFilter(String op) {
		// TODO Auto-generated method stub
		//filters different opcodes, for different instructions, such as STA, LDA, JLT, COMPR, etc
		return op;
	}

	private static String filterParamaters(String mode) {
		// TODO Auto-generated method stub
		//filters different paramets, such as LABEL,X or Register,Register or #3 and so on
		return mode;
	}

	public static void writeObjectCode(Integer code) 
	{
		// write code to file
	}
/**
ERROR HERE
 **/
	public static String hexToBin(String s) {
	  //return new BigInteger(s, 16).toString(2);
		int k = Integer.parseInt(s,16); 
		s = Integer.toBinaryString(k);
		return s;
	}

	public static String binaryToHex(String bin) {
	   return String.format("%21X", Long.parseLong(bin,2)) ;
	}
	
	

    private static void openIntermediate() {
        try {
            scan = new Scanner(new File("intermediateFile.txt"));
        }
        catch(Exception e){
            System.out.println("could not find this file");
        }

    }
	// read line function
	private static String[] readLine(String line) {

        String[] values = new String[4];
        //Checks to see if the line starts with a tab
        if (line.startsWith("\u0009")) {
            line = line.trim();
            String[] s = line.split("\u0009");
            values[0] = null;
            values[1] = s[0];
            if (s.length > 1)
                values[2] = s[1];
        }

        else {
            line = line.trim();
            String[] s = line.split("\u0009");
            values[0] = s[0];
            values[1] = s[1];
            values[2] = s[2];
            if (s.length > 3)
                values[3] = s[3];
        }
        return values;
    }

    //Readfile functions


    public void closeIntermediate() {
        scan.close();
    }

    //Writefile functions

    public static void makeFile() {
        try {
            obj = new Formatter("objectCode.txt");
        }
        catch(Exception e) {
            System.out.println("Failed to create file.");
        }
    }
	
	public static void closeObj() {
        obj.close();
    }

	 public static void openFile() {
	     try{
	         scan = new Scanner(new File(fileName));
	     }
	     catch(Exception e) {
	         System.out.println("could not find this file");
	     }
	 }

	 public static void closeFile() {
	     scan.close();
	 }
}

