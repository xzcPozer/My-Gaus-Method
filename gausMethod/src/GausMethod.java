import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class GausMethod {
    private double[][] matrix;
    private int iteration = 0;
    private int[] columnWithOne;
    private int line;
    private int column;
    private List<Integer> indexNullString = new ArrayList();
    private int countDeletedNullString;

    public GausMethod(int n) {
        try {
            if (n >= 16 || n <= 2) {
                throw new IllegalArgumentException();
            }

            this.line = n;
            this.column = n + 1;
            this.matrix = new double[this.line][this.column];
        } catch (IllegalArgumentException var3) {
            System.out.println("Минимальная размерность матрицы 2x3 / Максимальная размерность матрицы 14x15");
        }

    }

    public void enterMatrix() {
        this.columnWithOne = new int[this.line];
        Scanner scanner = new Scanner(System.in);

        while(true) {
            try {
                for(int i = 0; i < this.line; ++i) {
                    for(int j = 0; j < this.column; ++j) {
                        System.out.printf("Введите элемент [%d] [%d]", i + 1, j + 1);
                        double el = scanner.nextDouble();
                        this.matrix[i][j] = el;
                    }
                }

                return;
            } catch (InputMismatchException var6) {
                System.out.println("Неверный формат данных");
                scanner.nextLine();
            }
        }
    }

    public void printMatrix() {
        for(int i = 0; i < this.line; ++i) {
            for(int j = 0; j < this.column; ++j) {
                System.out.printf("%f   ", this.matrix[i][j]);
            }

            System.out.println();
        }

        System.out.println();
    }

    public void makeIdentityMatrix() {
        for(int i = this.line - 1; i > 0; --i) {
            for(int k = 1; k < i + 1; ++k) {
                double temp = -this.matrix[i - k][this.column - 1 - this.countDeletedNullString - (this.line - i)];

                for(int j = 0; j < this.column; ++j) {
                    double[] var10000 = this.matrix[i - k];
                    var10000[j] += this.matrix[i][j] * temp;
                }
            }
        }

    }

    public boolean checkFollowingLineOnNull() {
        int countNull = 0;
        boolean flag = false;

        for(int i = this.iteration + 1; i < this.line; ++i) {
            for(int j = 0; j < this.column - 1; ++j) {
                if (this.matrix[i][j] == 0.0) {
                    ++countNull;
                }
            }

            if (countNull == this.column - 1) {
                this.indexNullString.add(i);
                ++this.countDeletedNullString;
                flag = true;
            }

            countNull = 0;
        }

        return flag;
    }

    public void deleteNullString() {
        this.line -= this.indexNullString.size();
        boolean flag = true;
        int k = 0;
        double[][] newMatrix = new double[this.line][this.column];

        for(int i = 0; i < this.line + this.indexNullString.size(); ++i) {
            int j;
            for(j = 0; j < this.indexNullString.size(); ++j) {
                if (i == (Integer)this.indexNullString.get(j)) {
                    flag = false;
                    break;
                }

                flag = true;
            }

            if (flag) {
                for(j = 0; j < this.column; ++j) {
                    newMatrix[k][j] = this.matrix[i][j];
                }

                ++k;
            }
        }

        this.matrix = newMatrix;
        this.indexNullString.clear();
    }

    public void makeHardDivision() {
        double temp = this.matrix[this.iteration][this.iteration];

        for(int j = 0; j < this.column; ++j) {
            double[] var10000 = this.matrix[this.iteration];
            var10000[j] /= temp;
            if (this.matrix[this.iteration][j] == 0.0) {
                this.matrix[this.iteration][j] = 0.0;
            }
        }

    }

    public boolean makeCompleteDivision() {
        boolean flag = true;

        for(int i = this.iteration; i < this.line; ++i) {
            int j;
            for(j = this.iteration; j < this.column; ++j) {
                if (this.matrix[i][j] % this.matrix[i][this.iteration] != 0.0) {
                    flag = false;
                    break;
                }

                flag = true;
            }

            if (flag) {
                double temp = this.matrix[i][this.iteration];

                for(j = this.iteration; j < this.column; ++j) {
                    double[] var10000 = this.matrix[i];
                    var10000[j] /= temp;
                    if (this.matrix[i][j] == 0.0) {
                        this.matrix[i][j] = 0.0;
                    }
                }

                return true;
            }
        }

        return false;
    }

    public boolean makeSteppedView() {
        if (this.columnWithOne[this.iteration] == 0) {
            return false;
        } else {
            int indexLineWithOne = this.columnWithOne[this.iteration] - 1;

            for(int i = this.iteration; i < this.line; ++i) {
                if (i != indexLineWithOne) {
                    double temp = -this.matrix[i][this.iteration];

                    for(int j = 0; j < this.column; ++j) {
                        double[] var10000 = this.matrix[i];
                        var10000[j] += this.matrix[indexLineWithOne][j] * temp;
                    }
                }
            }

            return true;
        }
    }

    public void checkCurrentColumnWithOne() {
        for(int i = this.iteration; i < this.line; ++i) {
            if (this.matrix[i][this.iteration] == 1.0) {
                this.columnWithOne[this.iteration] = i + 1;
                break;
            }
        }

    }

    public boolean checkSteppedViewColumn() {
        int countNullInColumn = 0;

        for(int i = 0; i < this.line; ++i) {
            if (this.matrix[i][this.iteration] == 0.0) {
                ++countNullInColumn;
            }
        }

        if (countNullInColumn == this.line - this.iteration - 1 && this.columnWithOne[this.iteration] != 0) {
            return true;
        } else {
            return false;
        }
    }

    public void makeSteppedViewMatrix() {
        double[] temp = new double[this.column];
        if (this.columnWithOne[this.iteration] - 1 != this.iteration) {
            int i;
            for(i = 0; i < this.column; ++i) {
                temp[i] = this.matrix[this.iteration][i];
            }

            for(i = 0; i < this.column; ++i) {
                this.matrix[this.iteration][i] = this.matrix[this.columnWithOne[this.iteration] - 1][i];
            }

            for(i = 0; i < this.column; ++i) {
                this.matrix[this.columnWithOne[this.iteration] - 1][i] = temp[i];
            }
        }

    }

    public int getIteration() {
        return this.iteration;
    }

    public void setIteration(int iteration) {
        this.iteration = iteration;
    }

    public int getLine() {
        return this.line;
    }

    public int getColumn() {
        return this.column;
    }
}
