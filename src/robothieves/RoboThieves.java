/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package robothieves;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;

/**
 *
 * @author zhuan
 */
public class RoboThieves {

    /**
     * @param args the command line arguments
     */
    static char[][] map;
    static int[][] route;
    static int n,m;
    static int step=0;
    static ArrayList<Position> cameras=new ArrayList();
    static ArrayList<Position> spaces=new ArrayList();
    public static void main(String[] args) {
        // TODO code application logic here
        Scanner sc=new Scanner(System.in);
        n=sc.nextInt();
        m=sc.nextInt();
        sc.nextLine();
        map=new char[n][m];
        route=new int[n][m];
        Position start=null;
        for (int i=0;i<n;i++) {
            String row=sc.nextLine();
            for (int j=0;j<m;j++) {
                char c=row.charAt(j);
                map[i][j]=c;
                route[i][j]=-2;
                if (c=='S') {
                    start=new Position(i,j);
                    route[i][j]=0;
                } else if (c=='C') {
                    route[i][j]=-1;
                    cameras.add(new Position(i,j));
                } else if (c=='W') {
                    route[i][j]=-1;
                } else if (c=='.'){
                    spaces.add(new Position(i,j));
                }
            }
        }  
        ArrayList<Position> al=new ArrayList();
        ArrayList<Position> nextAl=new ArrayList();
        al.add(start);
        
        cameraCovers();
        while (!al.isEmpty()) {
            step++;
            for (Position pos: al) {
                nextAl.addAll(goNextStep(pos));
            }
            al=nextAl;
            nextAl=new ArrayList();
            
        }
        
        for (Position pos :spaces) {
            System.out.println(route[pos.x][pos.y]>0?route[pos.x][pos.y]:-1);
        }
    }

    private static ArrayList<Position> goNextStep(Position pos) {
        
        ArrayList<Position> ret=new ArrayList();
        if (route[pos.x][pos.y]==-1) return ret;
        moveTo(pos.x-1,pos.y,ret);
        moveTo(pos.x+1,pos.y,ret);
        moveTo(pos.x,pos.y-1,ret);
        moveTo(pos.x,pos.y+1,ret);
        return ret;
    }

    private static void moveTo(int x, int y, ArrayList<Position> ret) {
        if (!isInBoundry(x,y)) return;
        if (route[x][y]!=-2) return;
        if (map[x][y]=='W' || map[x][y]=='C') {
            return;
        }
        switch (map[x][y]) {
            case 'L':
                route[x][y]=-1;
                moveTo(x,y-1,ret);
                break;
            case 'R':
                route[x][y]=-1;
                moveTo(x,y+1,ret);
                break;
            case 'U':
                route[x][y]=-1;
                moveTo(x-1,y,ret);
                break;
            case 'D':
                route[x][y]=-1;
                moveTo(x+1,y,ret);
                break;
            case '.':
                route[x][y]=step;
                ret.add(new Position(x,y));
                break;
            default:
                break;
        }
    }

    private static boolean isInBoundry(int x, int y) {
        return x>0 && x<n && y>0 && y<m;
    }

    private static void cameraCovers() {
        for (Position pos : cameras) {
            int i=pos.x;
            char c;
            while (i>=0) {
                i--;
                c=map[i][pos.y];
                if (c=='W') {
                    break;
                } else if (c=='.' || c=='S'){
                    route[i][pos.y]=-1;
                }
            }
            i=pos.x;
            while (i<n) {
                i++;
                c=map[i][pos.y];
                if (c=='W') {
                    break;
                } else if (c=='.' || c=='S'){
                    route[i][pos.y]=-1;
                }
            }
            i=pos.y;
            while (i>=0) {
                i--;
                c=map[pos.x][i];
                if (c=='W') {
                    break;
                } else if (c=='.'||c=='S'){
                    route[pos.x][i]=-1;
                }
            }
            i=pos.y;
            while (i<m) {
                i++;
                c=map[pos.x][i];
                if (c=='W') {
                    break;
                } else if (c=='.'||c=='S'){
                    route[pos.x][i]=-1;
                }
            }
        }
    }

    private static class Position {
        int x;
        int y;
        public Position(int i,int j) {
            this.x=i;
            this.y=j;
        }
    }
}