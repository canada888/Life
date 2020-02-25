import java.util.Stack;

public class LifeGrid
{
    private boolean[][][] grid;
    private Stack<boolean[][][]> prev;


    LifeGrid(int w, int h, int d) {
        grid = new boolean[h][w][d];
        prev = new Stack<>();
    }

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();

        for (boolean[][] booleans2D : grid) {
            for (boolean[] booleans : booleans2D) {
                for (int c = 0; c < grid.length; c++) {
                    out.append("[").append(booleans[c] ? "X" : " ").append("]");
                }
                out.append("\n");
            }
        }
        return out.toString();
    }

    int rows(){
        return grid.length;
    }

    int cols(){
        return grid[0].length;
    }

    int depth(){
        return grid[0][0].length;
    }

    void change(int row, int col, int depth) {
        grid[row][col][depth] = !grid[row][col][depth];
    }

    void next() {
        boolean[][][] tempGrid = new boolean[rows()][cols()][depth()];

        for(int row = 0; row < rows(); row++){
            for(int col = 0; col < cols(); col++){
                for(int depth = 0; depth < depth(); depth++) {
                    // 2 cases
                    // count neighbors
                    int neighbors = numNeighbors(row, col, depth);

                    if (grid[row][col][depth]) {
                        // 1 - alive
                        tempGrid[row][col][depth] = (neighbors == 2 || neighbors == 3);
                    } else {
                        // 2 - dead
                        tempGrid[row][col][depth] = (neighbors == 3);
                    }
                }
            }
        }
        prev.push(grid);

        grid = tempGrid;
    }

    void prev() {
        if(!prev.empty()) {
            grid = prev.pop();
        }
    }

    private int numNeighbors(int row, int col, int depth){
        int count = 0;
        for(int i = -1; i <= 1; i++)
            for(int j = -1; j <= 1; j++)
                for(int k = -1; k <= 1; k++)
                    if(i != 0 || j != 0 || k != 0)
                        count += get(row+i,col+j,depth+k)?1:0;

        return count;
    }

    boolean get(int row, int col, int depth){
        return grid[(row + rows()) % rows()][(col + cols()) % cols()][(depth + depth()) % depth()];
    }

    void clearBoard() {
        for(int i = 0; i < rows(); i++) {
            for (int j = 0; j < cols(); j++) {
                for(int k = 0; k < depth(); k++) {
                    if (get(i, j, k))
                        change(i, j, k);
                }
            }
        }
    }
}
