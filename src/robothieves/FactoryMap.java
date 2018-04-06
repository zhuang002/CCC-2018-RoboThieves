/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package robothieves;

import java.util.ArrayList;

/**
 *
 * @author zhuan
 */
class FactoryMap {

    char[][] table;
    int[][] stepRecords;
    FactoryMap(int rows, int cols) {
        this.table = new char[rows][cols];
        this.stepRecords = new int[rows][cols];
        for (int i=0;i<rows;i++) {
            for (int j=0;j<cols;j++) {
                this.stepRecords[i][j]=-1;
            }
        }
    }

    FactoryPosition getInitialPosition() {
        for (int i=0;i<table.length;i++) {
            for (int j=0;j<table[i].length;j++) {
                if (table[i][j]=='S') {
                    this.stepRecords[i][j]=0;
                    return new FactoryPosition(i,j);
                }
            }
        }
        return null;
    }

    FactoryPosition[] walkOneStep(FactoryPosition[] positions) {
        ArrayList<FactoryPosition> furtherPositions=new ArrayList<FactoryPosition>();
        for (int i=0;i<positions.length;i++) {
            FactoryPosition[] poses=walkOneStep(positions[i]);
            MergePositions(furtherPositions,poses);
        }
        return furtherPositions.toArray(new FactoryPosition[furtherPositions.size()]);
    }
    
    FactoryPosition[] walkOneStep(FactoryPosition position) {
        FactoryPosition[] positions=getNeibourPositions(position);
        ArrayList<FactoryPosition> returnPositions=new ArrayList<FactoryPosition>();
        int currentStep=getStepsAt(position);
        for (int i=0;i<positions.length;i++) {
            FactoryPosition pos=positions[i];
            pos=moveForward(pos);
            if (getStateAt(pos)=='.' && getStepsAt(pos)==-1) {
                setStepsAt(pos,currentStep+1);
                        returnPositions.add(pos);
            } 
        }
        return returnPositions.toArray(new FactoryPosition[returnPositions.size()]);
    }

    private void MergePositions(ArrayList<FactoryPosition> furtherPositions, FactoryPosition[] poses) {
        for (int i=0;i<poses.length;i++) {
            FactoryPosition pos=poses[i];
            boolean contains=false;
            for (int j=0;j<furtherPositions.size();j++) {
                if (pos.i==furtherPositions.get(j).i && pos.j==furtherPositions.get(j).j) {
                    contains=false;
                    break;
                }
            }
            if (!contains) furtherPositions.add(pos);
        }
    }

    private FactoryPosition[] getNeibourPositions(FactoryPosition pos) {
        FactoryPosition[] returnPositions = new FactoryPosition[4];
        returnPositions[0]=new FactoryPosition(pos.i-1,pos.j);
        returnPositions[1]=new FactoryPosition(pos.i+1,pos.j);
        returnPositions[2]=new FactoryPosition(pos.i,pos.j-1);
        returnPositions[3]=new FactoryPosition(pos.i,pos.j+1);
        
        return returnPositions;
    }

    private char getStateAt(FactoryPosition pos) {
        return this.table[pos.i][pos.j];
    }

    private int getStepsAt(FactoryPosition pos) {
        return this.stepRecords[pos.i][pos.j];
    }

    private void setStepsAt(FactoryPosition pos, int steps) {
        this.stepRecords[pos.i][pos.j]=steps;
    }

    private FactoryPosition moveForward(FactoryPosition pos) {
        FactoryPosition newPos;
        switch (getStateAt(pos)) {
            case 'L':
                if (pos.j==0) return null;
                newPos=new FactoryPosition(pos.i,pos.j-1);
                break;
            case 'R':
                if (pos.i>=this.table[0].length-1) return null;
                newPos=new FactoryPosition(pos.i,pos.j+1);
                break;
            case 'U':
                if (pos.i==0) return null;
                newPos=new FactoryPosition(pos.i-1,pos.j);
                break;
            case 'D':
                if (pos.i>=this.table.length-1) return null;
                newPos=new FactoryPosition(pos.i+1,pos.j);
                break;
            default:
                return pos;
        }
        return moveForward(newPos);
    }
    

    void printSteps() {
        for (int i=0;i<this.table.length;i++) {
            for (int j=0;j<this.table[i].length;j++) {
                int steps=this.getStepsAt(new FactoryPosition(i,j));
                if (steps!=0) {
                    System.out.println(steps);
                }
            }
        }
    }

    void setRow(int row,String line) {
        for (int i=0;i<this.table[row].length;i++) {
            char state=line.charAt(i);
            this.table[row][i]=state;
            if (state !='.')
                this.stepRecords[row][i]=0;
        }
    }

    void initialize() {
        for (int i=0;i<this.table.length;i++) {
            for (int j=0;j<this.table.length;j++) {
                if (this.table[i][j]=='C') {
                    blockCells(i,j);
                }
            }
        }
    }

    private void blockCells(int x, int y) {
        for (int i=x-1;i>=0;i--) {
            if (this.table[i][y]=='.')
                this.table[i][y]='B';
            if (this.table[i][y]=='W')
                break;
        }
        for (int i=x+1;i<this.table.length;i++) {
            if (this.table[i][y]=='.')
                this.table[i][y]='B';
            if (this.table[i][y]=='W')
                break;
        }
        
        for (int i=y-1;i>=0;i--) {
            if (this.table[x][i]=='.')
                this.table[x][i]='B';
            if (this.table[x][i]=='W')
                break;
        }
        for (int i=y+1;i<this.table[x].length;i++) {
            if (this.table[x][i]=='.')
                this.table[x][i]='B';
            if (this.table[x][i]=='W')
                break;
        }
    }
}
