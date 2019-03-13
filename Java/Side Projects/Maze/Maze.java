import java.awt.*;
import java.util.ArrayList;
import java.util.Stack;
import java.util.Random;
import javax.swing.*;
import java.awt.event.*;

class Maze extends JPanel implements ActionListener, MouseMotionListener {
    static final int cellSize = 20;
    static final int wallSize = 10;
    static final int height = (500 / (cellSize + wallSize)) * (cellSize + wallSize);
    static final int width = (900 / (cellSize + wallSize)) * (cellSize + wallSize);

    int cellsPerRow = width / (cellSize + wallSize);
    int numOfRows = height / (cellSize + wallSize);
    Timer timer = new Timer(30, this);
    Random r = new Random();

    JFrame frame = new JFrame("Maze");
    JMenuBar menuBar = new JMenuBar();
    JMenu menu = new JMenu("Do...");
    JMenuItem solveMaze = new JMenuItem("Solve");
    JMenuItem findPath = new JMenuItem("Find a path from cell x to cell y");
    JMenuItem newMaze = new JMenuItem("Gen. New Maze");

    ArrayList<wall> walls = new ArrayList<>();
    ArrayList<cell> cells = new ArrayList<>();
    Stack<cell> stack = new Stack<cell>(); // used for generating mazes
    Stack<cell> stack2 = new Stack<cell>(); // used for solving mazes

    int startId;
    JTextField cell1 = new JTextField();
    JTextField cell2 = new JTextField();

    public static void main(String[] args) {
        new Maze();
    }

    Maze() {
        // Configuring main frame
        setPreferredSize(new Dimension(width - wallSize, height - wallSize));
        frame.setResizable(false);
        frame.setContentPane(this);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Configuring the menu and the tooltip
        menu.add(solveMaze);
        menu.add(newMaze);
        menu.add(findPath);
        menuBar.add(menu);
        frame.setJMenuBar(menuBar);
        JToolTip cellId = new JToolTip();
        frame.add(cellId);

        // Adding this panel as a listener to different components
        newMaze.addActionListener(this);
        solveMaze.addActionListener(this);
        findPath.addActionListener(this);
        this.addMouseMotionListener(this);

        // Showing frame
        frame.pack();
        frame.setVisible(true);

        for (int h = 0; h < numOfRows; h++) {
            for (int w = 0; w < cellsPerRow; w++) {
                cells.add(new cell(w * (cellSize + wallSize), h * (cellSize + wallSize), cellSize, cells.size()));

                /*
                 * Adding null-Walls makes it possible to access a wall through its idCell1 und
                 * idCell2. All vertical walls have an even index and all horizontal walls have
                 * an odd index. Example (1): The vertical wall separating cell 4 and cell 5 has
                 * the index 4*2=8. Example (2): The horizontal wall separating cell 4 from the
                 * cell underneath it (cellId = 4+cellsPerRow) has index 4*2+1 = 9.
                 */
                int idCell1 = cells.size() - 1;
                if (w < cellsPerRow - 1) {
                    walls.add(new wall(w * (cellSize + wallSize) + cellSize, h * (cellSize + wallSize), wallSize, true,
                            idCell1, idCell1 + 1));
                } else if (w == cellsPerRow - 1) {
                    walls.add(null);

                }

                if (h < numOfRows - 1) {
                    walls.add(new wall(w * (cellSize + wallSize), h * (cellSize + wallSize) + cellSize, wallSize, false,
                            idCell1, idCell1 + cellsPerRow));
                } else if (h == numOfRows - 1) {
                    walls.add(null);
                }
            }
        }

        // after creating the cells, find the neighbors of each cell
        for (int i = 0; i < cellsPerRow * numOfRows; i++)
            cells.get(i).lookForNeighbors();

        // choose a random cell and start the depth first search with it being the cell
        // at the top of the stack
        startId = r.nextInt(cells.size());
        cells.get(startId).firstVisit = true;
        stack.push(cells.get(startId));
        menu.setEnabled(false);
        timer.start();
    }

    public void actionPerformed(ActionEvent e) {

        if (e.getActionCommand() == (("Gen. New Maze"))) {
            stack2 = new Stack<cell>();

            cells.forEach((n) -> {
                n.firstVisit = false;
                n.secondVisit = false;
            });

            walls.forEach((n) -> {
                if (n != null) {
                    n.isBroken = false;
                    n.isVisited = false;
                }
            });

            startId = r.nextInt(cells.size());
            stack.push(cells.get(startId));
            menu.setEnabled(false);
            timer.start();

        } else if ((e.getActionCommand() == (("Find a path from cell x to cell y"))
                || (e.getActionCommand() == ("Solve")))) {

            if (!(e.getActionCommand() == ("Solve"))) {
                Object[] message = { "Start Cell:", cell1, "Destination Cell:", cell2 };
                JOptionPane input = new JOptionPane(message);
                input.showConfirmDialog(this, message, "Enter Cells' Numbers:", JOptionPane.OK_CANCEL_OPTION);
            } else {
                cell1.setText("0");
                cell2.setText(Integer.toString(numOfRows * cellsPerRow - 1)); // Set cell2 to be the last cell
            }

            if ((!cell1.getText().isEmpty()) && (!cell2.getText().isEmpty())) {
                cell startCell = cells.get(Integer.parseInt(cell1.getText()));

                cells.forEach((n) -> {
                    n.firstVisit = false;
                    n.secondVisit = true;
                });

                walls.forEach((n) -> {
                    if (n != null)
                        n.isVisited = false;
                });

                startCell.firstVisit = true;
                stack2.push(startCell);
                menu.setEnabled(false);
                timer.start();
            }

        } else { // if timer is running

            // if a maze is being generated
            if (!stack.isEmpty()) {
                cell nextCell = stack.peek().visitRandomNeighbor();
                if (nextCell.id == stack.peek().id) {
                    stack.pop();
                } else {
                    stack.push(nextCell);
                }
            }

            // if a maze is being solved
            if (!stack2.isEmpty()) {
                cell nextCell = stack2.peek().visitConnectedNeighbor();
                if (nextCell.id == stack2.peek().id) {
                    stack2.peek().secondVisit = false;
                    stack2.peek().getWall(stack2.get(stack2.size() - 2).id).isVisited = false;
                    stack2.pop();

                } else if (nextCell.id == cells.get(Integer.parseInt(cell2.getText())).id) {
                    stack2.push(nextCell);
                    cell1 = new JTextField();
                    cell2 = new JTextField();
                    menu.setEnabled(true);
                    timer.stop();

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

        cells.forEach((n) -> n.draw(g));

        walls.forEach((n) -> {
            if (n != null)
                n.draw(g);

        });

        // color the cell at the top of the stack with the color red, so it looks cool
        // and we know what is actually happening
        if (!stack.isEmpty()) {
            g.setColor(new Color(255, 51, 51));
            g.fillRect(stack.peek().xCoor, stack.peek().yCoor, cellSize, cellSize);
        }

        // if both stacks are empty (nothing is being done), stop timer, so that
        // unnecessary actions fired by it are no longer fired
        if ((stack.isEmpty()) && (stack2.isEmpty()) && timer.isRunning()) {
            menu.setEnabled(true);
            timer.stop();
        }

    }

    public void mouseMoved(MouseEvent e) {
        Point mouseLocation = e.getPoint();
        int cellX = mouseLocation.x / (cellSize + wallSize);
        int cellY = mouseLocation.y / (cellSize + wallSize);
        this.setToolTipText(Integer.toString(cellX + (cellY * cellsPerRow)));

    }

    public void mouseDragged(MouseEvent e) {

    }

    class cell {
        int size;
        int xCoor;
        int yCoor;
        int id;

        ArrayList<cell> neighbors = new ArrayList<cell>();
        boolean firstVisit;
        boolean secondVisit;

        cell(int xCoor, int yCoor, int size, int id) {
            this.size = size;
            this.xCoor = xCoor;
            this.yCoor = yCoor;
            this.id = id;
        }

        public cell visitRandomNeighbor() {

            ArrayList<cell> unvisitedNeighbors = new ArrayList<cell>();
            for (int i = 0; i < neighbors.size(); i++) {
                if (!neighbors.get(i).firstVisit)
                    unvisitedNeighbors.add(neighbors.get(i));
            }
            if (unvisitedNeighbors.isEmpty())
                return this;

            int randomNeighborId = r.nextInt(unvisitedNeighbors.size());
            cell randomNeighbor = unvisitedNeighbors.get(randomNeighborId);
            randomNeighbor.firstVisit = true;

            getWall(randomNeighbor.id).isBroken = true;

            return randomNeighbor;
        }

        // a neighbor is connected when the wall separating it from this cell is broken
        public cell visitConnectedNeighbor() {
            ArrayList<cell> connectedNeighbors = new ArrayList<cell>();

            for (int i = 0; i < neighbors.size(); i++) {
                if (getWall(neighbors.get(i).id).isBroken)
                    connectedNeighbors.add(neighbors.get(i));
            }

            ArrayList<cell> unvisitedNeighbors = new ArrayList<cell>();
            for (int i = 0; i < connectedNeighbors.size(); i++) {
                if (!connectedNeighbors.get(i).firstVisit)
                    unvisitedNeighbors.add(connectedNeighbors.get(i));
            }

            if (unvisitedNeighbors.isEmpty())
                return this;

            int connectedNeighborId = r.nextInt(unvisitedNeighbors.size());
            cell connectedNeighbor = unvisitedNeighbors.get(connectedNeighborId);
            connectedNeighbor.firstVisit = true;
            getWall(connectedNeighbor.id).isVisited = true;

            return connectedNeighbor;

        }

        public wall getWall(int otherCellId) {
            if (Math.abs(id - otherCellId) == 1) {
                if (walls.get(Math.min(id, otherCellId) * 2) != null)
                    return walls.get(Math.min(id, otherCellId) * 2);
            } else {
                if (walls.get(Math.min(id, otherCellId) * 2 + 1) != null)
                    return walls.get(Math.min(id, otherCellId) * 2 + 1);
            }
            return null;
        }

        public void draw(Graphics g) {

            if ((firstVisit) && (!secondVisit)) {
                g.setColor(Color.WHITE);

            } else if ((firstVisit) && (secondVisit)) {
                g.setColor(new Color(0, 153, 0));

            } else if ((!firstVisit) && (!secondVisit)) {
                g.setColor(Color.BLACK);

            } else if ((!firstVisit) && (secondVisit)) {
                g.setColor(Color.WHITE);

            }
            g.fillRect(xCoor, yCoor, size, size);

        }

        public void lookForNeighbors() {

            if (xCoor != 0)
                neighbors.add(cells.get(id - 1));
            if (yCoor != 0)
                neighbors.add(cells.get(id - cellsPerRow));
            if (xCoor != width - (size + wallSize))
                neighbors.add(cells.get(id + 1));
            if (yCoor != height - (size + wallSize))
                neighbors.add(cells.get(id + cellsPerRow));

        }
    }

    class wall {
        int size;
        int xCoor;
        int yCoor;
        int idCell1;
        int idCell2;
        boolean isBroken;
        boolean isVertical;
        boolean isVisited;

        wall(int xCoor, int yCoor, int size, boolean isVertical, int idCell1, int idCell2) {
            this.size = size;
            this.xCoor = xCoor;
            this.yCoor = yCoor;
            this.isVertical = isVertical;
            this.idCell1 = idCell1;
            this.idCell2 = idCell2;
        }

        public void draw(Graphics g) {

            if ((isVertical) && (isBroken) && (!isVisited)) {
                g.setColor(Color.WHITE);
                g.fillRect(xCoor, yCoor, size, cellSize);
            } else if ((!isVertical) && (isBroken) && (!isVisited)) {
                g.setColor(Color.WHITE);
                g.fillRect(xCoor, yCoor, cellSize, size);
            } else if ((isVertical) && (isBroken) && (isVisited)) {
                g.setColor(new Color(0, 153, 0));
                g.fillRect(xCoor, yCoor, size, cellSize);
            } else if ((!isVertical) && (isBroken) && (isVisited)) {
                g.setColor(new Color(0, 153, 0));
                g.fillRect(xCoor, yCoor, cellSize, size);
            }
        }
    }

}