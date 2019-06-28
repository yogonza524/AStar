package com.ai.astar.domain;

import java.util.*;

public class AStar {
    private int hvCost;
    private int diagonalCost;
    private Point[][] searchArea;
    private PriorityQueue<Point> openList;
    private Set<Point> closedSet;
    private Point initialNode;
    private Point finalNode;
    private boolean enableDiagonalMove;

    private AStar(){}
    private AStar(Point[][] searchArea, Point initialNode, Point finalNode, int hvCost, int diagonalCost, boolean enableDiagonalMove) {
        this.hvCost = hvCost;
        this.diagonalCost = diagonalCost;
        this.initialNode = initialNode;
        this.finalNode = finalNode;
        this.searchArea = searchArea;
        this.openList = new PriorityQueue<Point>(Comparator.comparing(Point::getF));
        this.closedSet = new HashSet<>();
        this.enableDiagonalMove = enableDiagonalMove;
    }

    private AStar(Point[][] searchArea, Point initialNode, Point finalNode, int hvCost, int diagonalCost, boolean enableDiagonalMove, int[][] obstacles) {
        this(searchArea,initialNode,finalNode,hvCost,diagonalCost,enableDiagonalMove);
        this.setObstacles(obstacles);
    }

    public void setObstacles(int[][] blocksArray) {
        for (int i = 0; i < blocksArray.length; i++) {
            int row = blocksArray[i][0];
            int col = blocksArray[i][1];
            this.searchArea[row][col].setBlock(true);
        }
    }
    public List<Point> findPath() {
        openList.add(initialNode);
        while (!openList.isEmpty()) {
            Point currentNode = openList.poll();
            closedSet.add(currentNode);
            if (currentNode.equals(finalNode)) {
                return getPath(currentNode);
            } else {
                addAdjacentNodes(currentNode);
            }
        }
        return new ArrayList<Point>();
    }
    private List<Point> getPath(Point currentNode) {
        List<Point> path = new ArrayList<Point>();
        path.add(currentNode);
        Point parent;
        while ((parent = currentNode.getParent()) != null) {
            path.add(0, parent);
            currentNode = parent;
        }
        return path;
    }
    private void addAdjacentNodes(Point currentNode) {
        addAdjacentUpperRow(currentNode);
        addAdjacentMiddleRow(currentNode);
        addAdjacentLowerRow(currentNode);
    }

    private void addAdjacentLowerRow(Point currentNode) {
        int row = currentNode.getRow();
        int col = currentNode.getCol();
        int lowerRow = row + 1;
        if (lowerRow < this.searchArea.length) {
            if (enableDiagonalMove) {
                if (col - 1 >= 0) {
                    checkPoint(currentNode, col - 1, lowerRow, this.diagonalCost);
                }
                if (col + 1 < this.searchArea[0].length) {
                    checkPoint(currentNode, col + 1, lowerRow, this.diagonalCost);
                }
            }
            checkPoint(currentNode, col, lowerRow, this.hvCost);
        }
    }

    private void addAdjacentMiddleRow(Point currentPoint) {
        int row = currentPoint.getRow();
        int col = currentPoint.getCol();
        int middleRow = row;
        if (col - 1 >= 0) {
            checkPoint(currentPoint, col - 1, middleRow, this.hvCost);
        }
        if (col + 1 < this.searchArea[0].length) {
            checkPoint(currentPoint, col + 1, middleRow, this.hvCost);
        }
    }

    private void addAdjacentUpperRow(Point currentNode) {
        int row = currentNode.getRow();
        int col = currentNode.getCol();
        int upperRow = row - 1;
        if (upperRow >= 0) {
            if (enableDiagonalMove) {
                if (col - 1 >= 0) {
                    checkPoint(currentNode, col - 1, upperRow, this.diagonalCost);
                }
                if (col + 1 < this.searchArea[0].length) {
                    checkPoint(currentNode, col + 1, upperRow, this.diagonalCost);
                }
            }
            checkPoint(currentNode, col, upperRow, this.hvCost);
        }
    }

    private void checkPoint(Point currentNode, int col, int row, int cost) {
        Point adjacentNode = this.searchArea[row][col];
        if (!adjacentNode.isBlock() && !this.closedSet.contains(adjacentNode)) {
            if (!this.openList.contains(adjacentNode)) {
                adjacentNode.setNodeData(currentNode, cost);
                this.openList.add(adjacentNode);
            } else {
                boolean changed = adjacentNode.checkBetterPath(currentNode, cost);
                if (changed) {
                    // Remove and Add the changed node, so that the PriorityQueue can sort again its
                    // contents with the modified "finalCost" value of the modified node
                    this.openList.remove(adjacentNode);
                    this.openList.add(adjacentNode);
                }
            }
        }
    }

    public int getHvCost() {
        return hvCost;
    }

    public int getDiagonalCost() {
        return diagonalCost;
    }

    public Point[][] getSearchArea() {
        return searchArea;
    }

    public PriorityQueue<Point> getOpenList() {
        return openList;
    }

    public Set<Point> getClosedSet() {
        return closedSet;
    }

    public Point getInitialNode() {
        return initialNode;
    }

    public Point getFinalNode() {
        return finalNode;
    }

    public boolean isEnableDiagonalMove() {
        return enableDiagonalMove;
    }

    public static AStarBuilder builder(int rows, int cols) {
        return new AStarBuilder(rows, cols);
    }

    public static class AStarBuilder {
        private int hvCost;
        private int diagonalCost;
        private Point[][] searchArea;
        private PriorityQueue<Point> openList;
        private Set<Point> closedSet;
        private Point initialNode;
        private Point finalNode;
        private boolean enableDiagonalMove;
        private int[][] obstacles = null;

        private AStar instance;

        public AStarBuilder(int rows, int cols) {
            this.searchArea = new Point[rows][cols];
        }
        public AStarBuilder from(int row, int col) {
            if (row > this.searchArea.length - 1 || col > this.searchArea[0].length - 1) {
                throw new RuntimeException("Initial position is invalid");
            }
            this.initialNode = new Point(row,col);
            return this;
        }
        public AStarBuilder from(Point initialNode) {
            if (initialNode == null) {
                throw new RuntimeException("Initial Point can't be null");
            }
            if (initialNode.getRow() > this.searchArea.length -1 || initialNode.getCol() > this.searchArea[0].length - 1) {
                throw new RuntimeException("Initial Point is invalid");
            }
            this.initialNode = initialNode;
            return this;
        }
        public AStarBuilder to(int row, int col) {
            if (row > this.searchArea.length - 1 || col > this.searchArea[0].length - 1) {
                throw new RuntimeException("Final position is invalid");
            }
            this.finalNode = new Point(row,col);
            return this;
        }
        public AStarBuilder to(Point finalNode) {
            if (finalNode == null) {
                throw new RuntimeException("Final Point can't be null");
            }
            if (finalNode.getRow() > this.searchArea.length -1 || finalNode.getCol() > this.searchArea[0].length - 1) {
                throw new RuntimeException("Final Point is invalid");
            }
            this.finalNode = finalNode;
            return this;
        }
        public AStarBuilder canMoveOverDiagonals() {
            this.enableDiagonalMove = true;
            return this;
        }
        public AStarBuilder withHVCost(int hvCost) {
            this.hvCost = hvCost;
            return this;
        }
        public AStarBuilder withDiagonalCost(int diagonalCost) {
            this.diagonalCost = diagonalCost;
            return this;
        }
        private void setNodes() {
            for (int i = 0; i < searchArea.length; i++) {
                for (int j = 0; j < searchArea[0].length; j++) {
                    Point node = new Point(i, j);
                    node.calculateHeuristic(finalNode);
                    this.searchArea[i][j] = node;
                }
            }
        }
        public AStarBuilder withObstacles(int[][] obstacles) {
            if (obstacles[0].length > searchArea[0].length || obstacles.length > searchArea.length) {
                throw new RuntimeException("Obstacles dimension are invalid");
            }
            this.obstacles = obstacles;
            return this;
        }
        public AStar create() {
            setNodes();
            if (this.hvCost == 0) {this.hvCost = 10;}
            if (this.diagonalCost == 0) {this.diagonalCost = 10;}
            if (this.obstacles == null) {
                return new AStar(searchArea, initialNode, finalNode,hvCost, diagonalCost, enableDiagonalMove);
            }
            return new AStar(searchArea, initialNode, finalNode,hvCost, diagonalCost, enableDiagonalMove, obstacles);
        }
    }
}
