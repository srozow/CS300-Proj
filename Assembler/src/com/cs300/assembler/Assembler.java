package com.cs300.assembler;

import java.io.File;
import java.util.Formatter;
import java.util.Scanner;

/**
 * Created by efetsko on 3/15/2016.
 */
public class Assembler {

    private Scanner scan;
    private Formatter inter;
    private int LOCCTR = 0;

    public void openFile() {
        try{
            scan = new Scanner(new File("assemblyCode/control_section.txt"));
        }
        catch(Exception e) {
            System.out.println("could not find this file");
        }
    }

    public void passOne() {

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


            //Setup the values for the current line's label, opcode, and parameters if it exists
            if (!line.trim().isEmpty()) {

                if (!line.startsWith(".")) {

                    //Checks to see if the line starts with a tab
                    if (line.startsWith("\u0009")) {
                        line = line.trim();
                        String[] s = line.split("\u0009");
                        opcode = s[0];
                        if (s.length > 1)
                            parameters = s[1];
                    }
                    else {
                        line = line.trim();
                        String[] s = line.split("\u0009");
                        label = s[0];
                        opcode = s[1];
                        if (s.length > 2)
                            parameters = s[2];
                    }

                    //write this line to the intermediate file with the current location converted to hexadecimal
                    //as the address
                    writeLine(toHex(LOCCTR), label, opcode, parameters);


                    //Next we need to check opcodes
                    if (opcode != null) {
                        if (opcode.equals("START")) {
                            LOCCTR = Integer.parseInt(parameters);
                            System.out.print("Starting location is ");
                            System.out.println(LOCCTR);
                        }
                        else if (Tables.OPTAB.get(opcode) != null) {
                            if (opcode.equals("CLEAR") || opcode.equals("COMPR") || opcode.equals("TIXR"))
                                LOCCTR += 2;
                            else
                                LOCCTR += 3;
                        }
                        else if (opcode.startsWith("+")) {
                            LOCCTR += 4;
                        }
                        else if (opcode.equals("WORD")) {
                            LOCCTR += 3;
                        }
                        else if (opcode.equals("RESW")) {
                            //assign address

                            //push the LOCCTR forward by 3 bytes per the amount of words.
                            LOCCTR = LOCCTR + (Integer.parseInt(parameters) * 3);
                        }
                        else if (opcode.equals("RESB")) {
                            //assign address

                            //push the LOCCTR forward by 1 per byte reserved
                            LOCCTR += Integer.parseInt(parameters);
                        }
                        else if (opcode.equals("BYTE")) {
                            //if X'EF' every two digits in quotes will be a byte
                            //if C'F' every digit will be a byte
                            if (parameters.startsWith("X")) {
                                int digits = parameters.substring(2, opcode.length()).length();
                                //System.out.println(digits);
                                LOCCTR += digits/2;
                            }
                            else if (parameters.startsWith("C")) {
                                int digits = parameters.substring(2, opcode.length()).length();
                                //System.out.println(digits);
                                LOCCTR += digits*2;
                            }

                        }
                        else if (opcode.equals("CSECT")) {
                            LOCCTR = 0;
                        }
                        else if (opcode.equals("BASE") || opcode.equals("LTORG") || opcode.equals("EXTDEF") || opcode.equals("EXTREF")) {
                            //don't increment the LOCCTR
                        }


                    }
                }

            }

        }


        //close the files
        closeFile();
        closeInter();

    }

    public void closeFile() {
        scan.close();
    }

    public void makeFile() {
        try {
            inter = new Formatter("intermediateFile.txt");
        }
        catch(Exception e) {
            System.out.println("Failed to create file.");
        }
    }

    public void writeLine(String address, String label, String opcode, String params) {
        String a;
        String l;
        String o;
        String p;

        //Assigns the variable to an empty string if the variable is null.
        if (address == null)
            a = "";
        else
            a = address;
        if (label == null)
            l = "";
        else
            l = label;
        if (opcode == null)
            o = "";
        else
            o = opcode;
        if (params == null)
            p = "";
        else
            p = params;

        if (opcode.equals("BASE") || opcode.equals("LTORG") || opcode.equals("CSECT") || opcode.equals("EXTDEF") || opcode.equals("EXTREF")) {
            a = "";
        }

        inter.format("%s\u0009%s\u0009%s\u0009%s%n", a, l, o, p);
    }

    public void closeInter() {
        inter.close();
    }

    //Takes a decimal number and converts it to a 4 digit hexadecimal value
    public String toHex(int dec) {
        String hex = Integer.toHexString(dec);

        while (hex.length() < 4)
            hex = "0" + hex;

        return hex;
    }

}
