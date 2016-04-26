package com.assem;

import java.io.File;
import java.util.Scanner;

/**
 * Created by efetsko on 3/28/2016.
 */
public class PassTwo {

    private static Scanner scan;

    public static void passTwo(String fileName) {

        //Sets up a scanner to read from the intermediate file.
        openIntermediate();


    }

    private static void openIntermediate() {
        try {
            scan = new Scanner(new File("intermediateFile.txt"));
        }
        catch(Exception e){
            System.out.println("could not find this file");
        }

    }


}