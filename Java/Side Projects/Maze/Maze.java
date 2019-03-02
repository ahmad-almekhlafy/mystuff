import java.awt.*;
import java.util.ArrayList;
import java.util.Stack;
import java.util.Random;
import javax.swing.*;
import java.awt.event.*;

class Maze extends JPanel implements ActionListener, MouseMotionListener {
    Timer timer = new Timer(0, this);
    static ArrayList<ArrayList<cell>> cells;
    static ArrayList<wall> walls = new ArrayList<>();
    Stack<cell> stack = new Stack<cell>();
    Stack<cell> stack2 = new Stack<cell>();
    static final int cellSize = 20;
    static final int wallSize = 10;
    static final int height = 500;
    static final int width = 900;
    static Random r = new Random();
    int startX = r.nextInt(width / cellSize);
    int startY = r.nextInt(height / cellSize);
    JTextField cell1 = new JTextField();
    JTextField cell2 = new JTextField();

    public static void main(String[] args) {
        new Maze();
    }

    Maze() {
        JFrame frame = new JFrame("Maze");
        frame.setResizable(false);
        setPreferredSize(new Dimension(width - wallSize, height - wallSize));
        frame.setContentPane(this);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Do...");
        JMenuItem solveMaze = new JMenuItem("Solve");
        JMenuItem findPath = new JMenuItem("Find a path from cell x to cell y");
        JMenuItem newMaze = new JMenuItem("Gen. New Maze");
        JToolTip cellId = new JToolTip();
        this.addMouseMotionListener(this);
        frame.add(cellId);
        newMaze.addActionListener(this);
        solveMaze.addActionListener(this);
        findPath.addActionListener(this);
        menu.add(solveMaze);
        menu.add(newMaze);
        menu.add(findPath);
        menuBar.add(menu);

        frame.setJMenuBar(menuBar);

        frame.pack();
        frame.setVisible(true);
        cells = new ArrayList<>();
        int id = 0;
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
        if (e.getActionCommand() == (("Gen. New Maze"))) {
            startX = r.nextInt(width / cellSize);
            startY = r.nextInt(height / cellSize);
            stack.push(cells.get(startY).get(startX));
            for (int h = 0; h < height / cellSize; h++) {
                for (int w = 0; w < width / cellSize; w++) {
                    cells.get(h).get(w).isVisited = false;
                    cells.get(h).get(w).secondVisit=false;
                }
            }
            for (int i = 0; i < walls.size(); i++) {
                walls.get(i).isBroken = false;
                walls.get(i).isVisited=false;
            }

            timer.start();
        } else if (e.getActionCommand() == (("Find a path from cell x to cell y"))) {

            Object[] message = { "Start Cell:", cell1, "Destination Cell:", cell2 };
            JOptionPane input = new JOptionPane(message);
            input.showConfirmDialog(this, message, "Enter Cells' Numbers:", JOptionPane.OK_CANCEL_OPTION);

            if ((!cell1.getText().equals("")) && (!cell2.getText().equals(""))) {
                cell startCell = cells.get((Integer.parseInt(cell1.getText()) / 45))
                        .get((Integer.parseInt(cell1.getText()) % 45));

                for (int h = 0; h < height / cellSize; h++) {
                    for (int w = 0; w < width / cellSize; w++) {
                        cells.get(h).get(w).isVisited = false;
                        cells.get(h).get(w).secondVisit = true;
                    }
                }
                walls.forEach((n) -> {

                    n.isVisited = false;
                });
                startCell.isVisited = true;
                stack2.push(startCell);
                
                timer.start();

            }
        } else {

            if (!stack.isEmpty()) {
                cell nextCell = stack.peek().visitRandomNeighbour();
                if (nextCell.id == stack.peek().id) {
                    stack.pop();
                } else {
                    stack.push(nextCell);
                }
            }

            if (!stack2.isEmpty()) {
                cell nextCell = stack2.peek().visitConnectedNeighnour();
                if (nextCell.id == stack2.peek().id) {

                    stack2.peek().secondVisit = false;
                    walls.forEach((n) -> {
                        if (stack2.size() > 1) {
                            if (((n.idCell1 == stack2.get(stack2.size() - 2).id) && (n.idCell2 == stack2.peek().id))
                                    || ((n.idCell2 == stack2.get(stack2.size() - 2).id)
                                            && (n.idCell1 == stack2.peek().id)))

                                n.isVisited = false;
                        }
                    });

                    stack2.pop();
                } else if (nextCell.id == cells.get((Integer.parseInt(cell2.getText()) / 45))
                        .get((Integer.parseInt(cell2.getText()) % 45)).id) {
                    stack2.push(nextCell);
                    cell1 = new JTextField();
                   cell2 = new JTextField();

                    timer.stop();
                    System.out.println("stopped");

                } else {
                    stack2.push(nextCell);
                }
            }

            repaint();
        }

    }

    public void paintComponent(Graphics g) {

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, width - wallSize, height - wallSize);

        for (int h = 0; h < height / cellSize; h++) {
            for (int w = 0; w < width / cellSize; w++) {
                cells.get(h).get(w).draw(g);
            }
        }

        for (int i = 0; i < walls.size(); i++) {
            walls.get(i).draw(g);
        }

        if (!stack.isEmpty()) {
            g.setColor(new Color(255, 51, 51));
            g.fillRect(stack.peek().xCoor, stack.peek().yCoor, 10, 10);
        }

        if ((stack.isEmpty()) && (stack2.isEmpty()) && timer.isRunning()) {
            System.out.println("stopped");
            timer.stop();
        }
    }

    public void mouseMoved(MouseEvent e) {
        Point mouseLocation = e.getPoint();

        if ((((mouseLocation.x / 10) % 2) == 0) && (((mouseLocation.y / 10) % 2) == 0)) {
            int xCoor = (mouseLocation.x / 10) * 10;
            int yCoor = (mouseLocation.y / 10) * 10;
            int cellId = (yCoor / 20) * 45 + (xCoor / 20);
            this.setToolTipText("Cell " + cellId);
        } else {
            this.setToolTipText(null);
        }

    }

    public void mouseDragged(MouseEvent e) {

    }

    class cell {
        int size;
        int xCoor;
        int yCoor;
        boolean isVisited;
        int visitedNeighbours;
        ArrayList<cell> neighbours = new ArrayList<cell>();
        int id;
        boolean secondVisit;

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

        public cell visitConnectedNeighnour() {
            ArrayList<cell> connectedNeighbours = new ArrayList<cell>();
            for (int i = 0; i < walls.size(); i++) {
                if (walls.get(i).idCell1 == this.id) {
                    if (walls.get(i).isBroken)
                        connectedNeighbours.add(cells.get((walls.get(i).idCell2 / 45)).get(walls.get(i).idCell2 % 45));
                } else if (walls.get(i).idCell2 == this.id) {
                    if (walls.get(i).isBroken)
                        connectedNeighbours.add(cells.get((walls.get(i).idCell1 / 45)).get(walls.get(i).idCell1 % 45));
                }
            }

            ArrayList<cell> unvisitedNeighbours = new ArrayList<cell>();
            for (int i = 0; i < connectedNeighbours.size(); i++) {
                if (!connectedNeighbours.get(i).isVisited)
                    unvisitedNeighbours.add(connectedNeighbours.get(i));
            }
            if (unvisitedNeighbours.isEmpty())
                return this;

            int randomNeighbour = r.nextInt(unvisitedNeighbours.size());
            cell childCell = unvisitedNeighbours.get(randomNeighbour);
            childCell.isVisited = true;
            walls.forEach((n) -> {
                if (((n.idCell1 == childCell.id) && (n.idCell2 == this.id))
                        || ((n.idCell2 == childCell.id) && (n.idCell1 == this.id)))

                    n.isVisited = true;
            });

            return childCell;

        }

        public void draw(Graphics g) {

            if ((isVisited) && (!secondVisit)) {
                g.setColor(Color.WHITE);

            } else if ((isVisited) && (secondVisit)) {
                g.setColor(new Color(0, 153, 0));

            } else if ((!isVisited) && (!secondVisit)) {
                g.setColor(Color.BLACK);

            } else if ((!isVisited) && (secondVisit)) {
                g.setColor(Color.WHITE);

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
        boolean isVisited = false;

        wall(int xCoor, int yCoor, int size, boolean isVertical, int idCell1, int idCell2) {
            this.size = size;
            this.xCoor = xCoor;
            this.yCoor = yCoor;
            this.isVertical = isVertical;
            this.idCell1 = idCell1;
            this.idCell2 = idCell2;
        }

        public void draw(Graphics g) {

            if ((isBroken) && (!isVisited)) {
                g.setColor(Color.WHITE);
            } else if ((isBroken) && (isVisited)) {
                g.setColor(new Color(0, 153, 0));

            } else {
                g.setColor(Color.BLACK);
            }

            if ((isVertical) && (!isBroken)) {
                // g.setColor(Color.BLACK);
                // g.fillRect(xCoor, yCoor, size, cellSize);
            } else if ((!isVertical) && (!isBroken)) {
                // g.setColor(Color.BLACK);
                // g.fillRect(xCoor, yCoor, cellSize, size);
            } else if ((isVertical) && (isBroken) && (!isVisited)) {
                g.setColor(Color.WHITE);
                g.fillRect(xCoor, yCoor, size, cellSize - size);
            } else if ((!isVertical) && (isBroken) && (!isVisited)) {
                g.setColor(Color.WHITE);
                g.fillRect(xCoor, yCoor, cellSize - size, size);
            } else if ((isVertical) && (isBroken) && (isVisited)) {
                g.setColor(new Color(0, 153, 0));
                g.fillRect(xCoor, yCoor, size, cellSize - size);
            } else if ((!isVertical) && (isBroken) && (isVisited)) {
                g.setColor(new Color(0, 153, 0));
                g.fillRect(xCoor, yCoor, cellSize - size, size);
            }

        }
    }

}