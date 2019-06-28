package com.ai.astar.domain;

import java.io.Serializable;

public class Point implements Serializable {
    private int g;
    private int f;
    private int h;
    private int row;
    private int col;
    private boolean isBlock;
    private Point parent;

    public Point(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public void calculateHeuristic(Point finalNode) {
        this.h = Math.abs(finalNode.getRow() - getRow()) + Math.abs(finalNode.getCol() - getCol());
    }
    public void setNodeData(Point currentNode, int cost) {
        int gCost = currentNode.getG() + cost;
        setParent(currentNode);
        setG(gCost);
        calculateFinalCost();
    }
    public boolean checkBetterPath(Point currentNode, int cost) {
        int gCost = currentNode.getG() + cost;
        if (gCost < getG()) {
            setNodeData(currentNode, cost);
            return true;
        }
        return false;
    }
    private void calculateFinalCost() {
        int finalCost = getG() + getH();
        setF(finalCost);
    }
    @Override
    public boolean equals(Object a) {
        Point other = (Point) a;
        return this.getRow() == other.getRow() && this.getCol() == other.getCol();
    }

    @Override
    public String toString() {
        return "Point [row=" + row + ", col=" + col + "]";
    }

    public int getG() {
        return g;
    }

    public void setG(int g) {
        this.g = g;
    }

    public int getF() {
        return f;
    }

    public void setF(int f) {
        this.f = f;
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public boolean isBlock() {
        return isBlock;
    }

    public void setBlock(boolean block) {
        isBlock = block;
    }

    public Point getParent() {
        return parent;
    }

    public void setParent(Point parent) {
        this.parent = parent;
    }
}
