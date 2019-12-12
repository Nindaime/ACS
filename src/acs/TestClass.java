/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package acs;

/**
 *
 * @author PETER-PC
 */
public class TestClass {
    
    private static int[][] occupany = {
        {0, 0, 0, 0, 0},
        {0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0}};
    
    public static void main(String[] args) {
//        System.out.println("Enter Your Path ID: ");
//        String input = new Scanner(System.in).next();
//        String output = (input.matches("extP_[\\w]{1,}C[\\d]+") ? "It matches o" : "Bros wetin you type so");
//        System.out.println(output);
//        printOccupancyArray();
//        System.out.println("Occupied Parking space is: "+getOccupiedParkingLot());
//        System.out.println("Is C5 Occupied: "+isOccupied("C5"));
//        System.out.println("Is B2 Occupied: "+isOccupied("B2"));
//        System.out.println("Is B3 Occupied: "+isOccupied("B3"));
        System.out.println("Current Time is: "+getTime(1));
        System.out.println("19:15:19 matches reg expression [\\d]{2}:[\\d]{2}:[\\d]{2}: "+ ("19:15:19".matches("[\\d]{2}:[\\d]{2}:[\\d]{2}")));
    }
    
    private static String getTime(int extension) {
        long currentTime = System.currentTimeMillis();
        int minutes = (int)(currentTime / 60000) % 60;
        int hour = (int) (currentTime / 3600000) % 24;

        if ((extension + minutes) >= 60) {
            minutes = (extension + minutes) % 60;
            hour++;
        } else {
            minutes += extension;
        }

        return hour + ":" + minutes;
    }
    
    private static void printOccupancyArray(){
        for (int i = 0; i < occupany.length; i++) {
            for (int j = 0; j < occupany[i].length; j++) {
                System.out.print(occupany[i][j]+" ");
            }
            System.out.println("");
        }
    }
    
    private static boolean isOccupied(String allottedSpace) {
        String[] occupancyIndex = allottedSpace.split("[\\D]");
        int rowIndex = 0;
        switch (allottedSpace.charAt(0)) {
            case 'B':
                rowIndex = 1;
                break;
            case 'C':
                rowIndex = 2;
                break;
            case 'D':
                rowIndex = 3;
                break;
            case 'E':
                rowIndex = 4;
                break;
        }
        int columnIndex = Integer.parseInt(occupancyIndex[1]);
        System.out.println("Row Index: " + rowIndex + ", Column Index: " + (columnIndex - 1));

        return (occupany[rowIndex][--columnIndex] == 1);
    }
    
    public static String getOccupiedParkingLot(){
        String output = ""; boolean found = false;
        for (int i = 0; i < occupany.length; i++) {
            if(found){
               System.out.println("goal found, breaking loop");
               break; 
            }
            for (int j = 0; j < occupany[i].length; j++) {
                System.out.println("Accessing Occupancy Array @ row: "+i+", column: "+j);
                if(occupany[i][j] == 1){
                    System.out.println("Vehicle found @ Parking Space ["+j+"]["+i+"]");
                    found = true;
                    switch(i){
                        case 0:
                            output += "A";
                            break;
                        case 1:
                            output += "B";
                            break;
                        case 2:
                            output += "C";
                            break;
                        case 3:
                            output += "D";
                            break;
                        case 4:
                            output += "E";
                            break;
                    }
                    int parkinglotNumber = ++j;
                    output += parkinglotNumber;
                    break;
                }
            }
        }
        
        return output;
    }
    
    private static synchronized void updateOccupancy(String allottedSpace, Car.occupanyStatus status) {
        String[] occupancyIndex = allottedSpace.split("[\\D]");
        int rowIndex = 0, columnIndex = 0;
        switch (allottedSpace.charAt(0)) {
            case 'B':
                rowIndex = 1;
                break;
            case 'C':
                rowIndex = 2;
                break;
            case 'D':
                rowIndex = 3;
                break;
            case 'E':
                rowIndex = 4;
                break;
        }
        columnIndex = Integer.parseInt(occupancyIndex[1]);

        if (status == Car.occupanyStatus.PARKED) {
            occupany[rowIndex][--columnIndex] = 1;
        } else {
            occupany[rowIndex][--columnIndex] = 0;
        }
//        return 
    }
}
