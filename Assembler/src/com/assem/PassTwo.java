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
				if (parameters != null)
				{
					if (Tables.SYMTAB.get(parameters) != null)
						parameters = Tables.SYMTAB.get(parameters);
				}
				
				code = generate_code(opcode, parameters);
				System.out.println(code);
				//writeObjectCode(code);
            }

        }
        //close the files
        closeFile();
        closeObj();

    }
	
	private static String generate_code(String op, String params)
	{
		// filter OPCODE for instruction modes
		String opFilter = opFilter(op);
		// filter parameters for addressing and instruction modes
		String AddressingMode = filterParamaters(params);
		// get the object code for the OPCODE
		op = Tables.OPTAB.get(op);
		// initialize variables
		int n = 0;
		int i = 0;
		int x = 0;
		int b = 0;
		int p = 0;
		int e = 0;
		int r1 = 0;
		int r2 = 0;
		String out = "";
		String ObjCode = "";
		if (opFilter == 1)
		{
			// format 1 is simple, simply return the opcode for the line
			return op;
		}
		else if (opFilter == 2)
		{
			//convert op to binary
			op = hexToBin(op);
			// need to get params to form r1 and r2 from CHAR1,CHAR2
			if (params == String.format(params, (%a,%s))
			{
				// the first string of the parameter will be r1, the second will be r2
				r1 = a;
				r2 = s;
				// start filtering r1 and r2
				if (Table.REG.get(r2) != null)
				{
					r1 = Table.REG.get(r2);
					if (Table.REG.get(r1) != null)
					{
						r2 = Table.REG.get(r1);
					}
					else
					{
						return "INCORRECT R1 VALUE"
					}
				}
				else
				{
					return "INCORRECT R2 VALUE"
				}
			}
			// concatenate Opcode, r1 and r2
			out = op + r1 + r2;
			// convert to hex
			ObjCode = binaryToHex(out);
			// return the code for the line
			return ObjCode;
		}
		// set e to 0 (form 3)
		// or to 1 (form 4)
		// then continue
		else if (opFilter == 3)
		{
			e = 0;
		}
		else if (opFilter == 4)
		{
			e = 1;
		}
		// here is where we have statements for the parameters
		if (AddressingMode == indirect)
		{
			n = 1;
			i = 0;
		}
		if (AddressingMode == immediate)
		{
			n = 0;
			i = 1;
		}
		if (AddressingMode == simple)
		{
			// should we use 0 for SIC and 1 for SIC/XE?
			n = 0;
			i = 0;
		}
		//convert op and params to binary
		op = hexToBin(op);
		// params may need to be filtered, such as for literals and to remove the #, =, and @ symbols
		params = hexToBin(params);
		// padding for format 3 or format 4, ensures correct length
		if (e == 0)
		{
			// format the address for format 3
			params = String.format("%012d", Integer.parseInt(params));
		}
		else if (e = 1)
		{
			// format the address for format 4
			params = String.format("%020d", Integer.parseInt(params));
		}
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
		//before concatenation remove the last 2 bits from op
		op.substring(0,op.length() - 2);
		// concatenate Opcode, flags, and Params
		out = op + n + i + x + b + p + e + params;
		//System.out.println(out);
		// convert back to hex
		ObjCode = binaryToHex(out);
		// print for testing
		System.out.println(ObjCode);
		// later will write to a file, or send to another function for writing to file
		return ObjCode;
	}

	private static String opFilter(String opcode) {
		// TODO Auto-generated method stub
		//filters different opcodes, for different instructions, such as STA, LDA, JLT, COMPR, etc
		if (Table.OPFORM.get(opcode) != null)
		{
			opFilter = Table.OPFORM.get(opcode);
		}
		else
		{
			opFilter = 3;
			if (opcode.startsWith("+"))
			{
				opFilter = 4;
			}
		}
		return opFilter;
	}

	private static String filterParamaters(String mode) {
		// TODO Auto-generated method stub
		//filters different paramets, such as LABEL,X or Register,Register or #3 and so on
		if (mode.startsWith("=")
		{
			return literal; //should this be the label??
		}
		else if (mode.startsWith("#")
		{
			return immediate;
		}
		else if (mode.startsWith("@")
		{
			return indirect;
		}
		/**
		else if (format is char, char (reg1, reg2))
		{
			return form2param;
		}
		**/
		/**
		else if (format is label, x)
		{
			return indexed;
		}**/
		else
		{
			return simple;
		}
		
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

