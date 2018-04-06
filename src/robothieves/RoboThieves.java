/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package robothieves;

import java.util.Scanner;

/**
 *
 * @author zhuan
 */
public class RoboThieves {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        FactoryMap map=readInMap();
        map.initialize();
        FactoryPosition[] positions=new FactoryPosition[1];
        positions[0]=map.getInitialPosition();
        while (positions!=null && positions.length!=0) {
            positions=map.walkOneStep(positions);
        }
        map.printSteps();
    }

    private static FactoryMap readInMap() {
        Scanner sc=new Scanner(System.in);
        int rows=sc.nextInt();
        int cols=sc.nextInt();
        sc.nextLine();
        FactoryMap map=new FactoryMap(rows,cols);
        for (int i=0;i<rows;i++) {
            String line=sc.nextLine();
            map.setRow(i,line);
        }
       
        
        return map;
    }
    
}
