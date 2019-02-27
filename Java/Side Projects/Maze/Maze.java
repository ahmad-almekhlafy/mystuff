import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Stack;
import java.util.Random;
import javax.swing.*;
import java.awt.event.*;

class Maze extends JPanel implements ActionListener {
    Timer timer = new Timer(100, this); 
    static ArrayList<ArrayList<cell>> cells;
    static ArrayList<wall> walls = new ArrayList<>();
    Stack<cell> stack = new Stack<cell>();

    static final int cellSize = 20;
    static final int wallSize = 10;
    static final int height = 500;
    static final int width = 900;
    static Random r = new Random();
    int startX = r.nextInt(width / cellSize);
    int startY = r.nextInt(height / cellSize);
<<<<<<< HEAD
    //new branch;
    //master;
=======
    //shitshitishf;
    //tetsef;
>>>>>>> testing-branching
    public static void main(String[] args) {
        new Maze();
    }

    Maze() {
        JFrame frame = new JFrame("Maze");
        frame.setResizable(false);
        setPreferredSize(new Dimension(width - wallSize, height - wallSize));
        frame.setContentPane(this);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        cells = new ArrayList<>();

        for (int h = 0; h < height / cellSize; h++) {
            cells.add(new ArrayList<>());
            for (int w = 0; w < width / cellSize; w++) {
                cells.get(h).add(new cell(w * cellSize, h * cellSize, cellSize - wallSize, id++));

                int idCell1 = cells.get(h).size() - 1;
                if (w < (width / cellSize) - 1)
                    walls.add(new wall(w * cellSize + cellSize - wallSize, h * cellSize, wallSize, true,
                            idCell1 + (width / cellSize) * h, idCell1 + 1 + (width / cellSize) * h));

                if (h < (height / cellSize) - 1)
                    walls.add(new wall(w * cellSize, h * cellSize + cellSize - wallSize, wallSize, false,
                            idCell1 + (width / cellSize) * h, idCell1 + (width / cellSize) * (1 + h)));

            }

        }

        for (int h = 0; h < height / cellSize; h++) {
            for (int w = 0; w < width / cellSize; w++) {
                cells.get(h).get(w).lookForNeighbours();
            }
        }

        cells.get(startY).get(startX).isVisited = true;
        stack.push(cells.get(startY).get(startX));
        timer.start();

    }

    public void actionPerformed(ActionEvent e) {

        if (!stack.isEmpty()) {
            cell nextCell = stack.peek().visitRandomNeighbour();
            if (nextCell.id == stack.peek().id) {
                stack.pop();
            } else {
                stack.push(nextCell);
            }
        }
        repaint();
    }

    public void paintComponent(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, width, height);
        for (int h = 0; h < height / cellSize; h++) {
            for (int w = 0; w < width / cellSize; w++) {
                cells.get(h).get(w).draw(g);
            }
        }

        for (int i = 0; i < walls.size(); i++) {
            walls.get(i).draw(g);
        }
        if (!stack.isEmpty()) {
            g.setColor(new Color(255, 51 , 51));
            g.fillRect(stack.peek().xCoor, stack.peek().yCoor, 10, 10);
        }
    }

    class cell {
        int size;
        int xCoor;
        int yCoor;
        boolean isVisited;
        int visitedNeighbours;
        ArrayList<cell> neighbours = new ArrayList<cell>();
        int id;

        cell(int xCoor, int yCoor, int size, int id) {
            this.size = size;
            this.xCoor = xCoor;
            this.yCoor = yCoor;
            this.id = id;
        }

        public boolean isAtLeftEdge() {

            if (xCoor == 0) {
                return true;
            }
            return false;

        }

        public boolean isAtRightEdge() {

            if (xCoor == width - cellSize) {
                return true;
            }
            return false;

        }

        public boolean isAtCorner() {
            if (((xCoor == 0) && (yCoor == 0)) || ((xCoor == width - size) && (yCoor == height - size))
                    || ((yCoor == 0) && (xCoor == width - size)) || ((yCoor == height - size) && (xCoor == 0))) {
                return true;
            }
            return false;
        }

        public cell visitRandomNeighbour() {

            ArrayList<cell> unvisitedNeighbours = new ArrayList<cell>();
            for (int i = 0; i < neighbours.size(); i++) {
                if (!neighbours.get(i).isVisited)
                    unvisitedNeighbours.add(neighbours.get(i));
            }
            if (unvisitedNeighbours.isEmpty())
                return this;

            int randomNeighbour = r.nextInt(unvisitedNeighbours.size());
            cell childCell = unvisitedNeighbours.get(randomNeighbour);
            childCell.isVisited = true;

            int idParentCell = this.id;
            int idChildCell = childCell.id;
            walls.forEach((n) -> {
                if (((n.idCell1 == idParentCell) && (n.idCell2 == idChildCell))
                        || ((n.idCell2 == idParentCell) && (n.idCell1 == idChildCell)))
                    n.isBroken = true;
            });
            return childCell;
        }

        public void draw(Graphics g) {

            if (isVisited) {
                g.setColor(Color.WHITE);
            } else {
                g.setColor(Color.BLACK);
            }
            g.fillRect(xCoor, yCoor, size, size);

        }

        public void lookForNeighbours() {
            for (int h = 0; h < height / cellSize; h++) {

                for (int w = 0; w < width / cellSize; w++) {
                    if (((cells.get(h).get(w).id + 1 == this.id) && (!this.isAtLeftEdge()))
                            || (cells.get(h).get(w).id - 1 == this.id)
                            || (cells.get(h).get(w).id + (width / cellSize) == this.id)
                            || (cells.get(h).get(w).id - (width / cellSize) == this.id)) {
                        neighbours.add(cells.get(h).get(w));
                    }
                }
            }
        }
    }

    class wall {
        int size;
        int xCoor;
        int yCoor;
        boolean isBroken;
        boolean isVertical;
        int idCell1;
        int idCell2;

        wall(int xCoor, int yCoor, int size, boolean isVertical, int idCell1, int idCell2) {
            this.size = size;
            this.xCoor = xCoor;
            this.yCoor = yCoor;
            this.isVertical = isVertical;
            this.idCell1 = idCell1;
            this.idCell2 = idCell2;
        }

        public void draw(Graphics g) {

            if (isBroken) {
                g.setColor(Color.WHITE);
            } else {
                g.setColor(Color.BLACK);
            }

            if ((isVertical) && (!isBroken)) {
                // g.setColor(Color.BLACK);
                // g.fillRect(xCoor, yCoor, size, cellSize);
            } else if ((!isVertical) && (!isBroken)) {
                // g.setColor(Color.BLACK);
                // g.fillRect(xCoor, yCoor, cellSize, size);
            } else if ((isVertical) && (isBroken)) {
                g.setColor(Color.WHITE);
                g.fillRect(xCoor, yCoor, size, cellSize - size);
            } else {
                g.setColor(Color.WHITE);
                g.fillRect(xCoor, yCoor, cellSize - size, size);
            }

        }
    }

}