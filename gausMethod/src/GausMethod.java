import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GausMethod {
    private double[][] matrix;

    private int iteration = 0;

    //хранит индексы строк + 1, в которых есть 1, на каждой итерации (по 1ому индексу строки на каждой итерации)
    private int[] columnWithOne;

    private int line;

    private int column;

    //хранит индексы нулевых строк для последующего удаления из матрицы
    private List<Integer> indexNullString = new ArrayList<>();

    //хранит количество удаленных строк из матрицы
    private int countDeletedNullString;

    public GausMethod(int n) {
        try {
            if (n < 16 && n > 2) {
                this.line = n;
                this.column = n+1;
                matrix = new double[line][column];
            } else {
                throw new IllegalArgumentException();
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Минимальная размерность матрицы 2x3 / Максимальная размерность матрицы 14x15");
        }
    }

    //ввод матрицы
    public void enterMatrix() {
        //для работы по итерациям
        columnWithOne = new int[line];

        Scanner scanner = new Scanner(System.in);
        double el;

        while (true){
            try {
                for (int i = 0; i < line; i++) {
                    for (int j = 0; j < column; j++) {
                        System.out.printf("Введите элемент [%d] [%d]", i + 1, j + 1);
                        el = scanner.nextDouble();
                        matrix[i][j] = el;
                    }
                }
                break;
            } catch (InputMismatchException e) {
                System.out.println("Неверный формат данных");
                scanner.nextLine();
            }
        }
    }

    //вывод матрицы
    public void printMatrix() {
        for (int i = 0; i < line; i++) {
            for (int j = 0; j < column; j++) {
                System.out.printf("%f   ", matrix[i][j]);
            }
            System.out.println();
        }
        System.out.println();
    }

    //метод, который делает единичную матрицу (если были удалены строки, то остаются числа)
    public void makeIdentityMatrix() {
        double temp;

        for (int i = line - 1; i > 0; i--) {
            //делаем 0, на нужных столбцах (над единицей)
            for (int k = 1; k < i + 1; k++) {

                //число, которое равно 0
                temp = -matrix[i - k][column - 1 - countDeletedNullString - (line - i)];

                for (int j = 0; j < column; j++) {
                    matrix[i - k][j] += (matrix[i][j] * temp);
                }
            }
        }
    }

    //проверяет следующие нулевые строки (после итерации)
    public boolean checkFollowingLineOnNull() {
        int countNull = 0;
        boolean flag = false;

        for (int i = iteration + 1; i < line; i++) {
            for (int j = 0; j < column - 1; j++) {
                if (matrix[i][j] == 0)
                    countNull++;
            }
            if (countNull == column - 1) {
                indexNullString.add(i);
                countDeletedNullString++;
                flag = true;
            }

            countNull = 0;
        }

        return flag;
    }

    //удаление нулевых строк из массива
    public void deleteNullString() {
        line -= indexNullString.size();
        boolean flag = true;
        int k = 0;// для нового массива

        double[][] newMatrix = new double[line][column];

        for (int i = 0; i < line + indexNullString.size(); i++) {

            // проверка наличия нулевых строк
            for (int o = 0; o < indexNullString.size(); o++) {
                if (i == indexNullString.get(o)) {
                    flag = false;
                    break;
                } else
                    flag = true;
            }

            if (flag) {
                for (int j = 0; j < column; j++) {
                    newMatrix[k][j] = matrix[i][j];
                }

                k++;
            }
        }

        //меняем матрицу
        matrix = newMatrix;

        //очищаем список для следующих возможных изменений
        indexNullString.clear();
    }

    // выбирает самую 1ую (по порядку) строку, где 1ый элемент не равен 0 и делит на него все элементы в строке для получения 1 в нужном столбце
    public void makeHardDivision() {
        //переменная для деления на одно и то же число
        double temp = matrix[iteration][iteration];

        for (int j = 0; j < column; j++) {
            matrix[iteration][j] /= temp;

            //глупая проверка, но при делении 0 на отрицательное число получается -0 :)
            if(matrix[iteration][j]==-0)
                matrix[iteration][j]=0;
        }
    }

    //поделить нацело элементы строки на текущей итерации для получения 1 на нужном элементе матрицы(в нужном столбце)
    public boolean makeCompleteDivision() {
        //для проверки деления нацело
        boolean flag = true;
        //для деления
        double temp;

        for (int i = iteration; i < line; i++) {
            for (int j = iteration; j < column; j++) {
                //делится ли число на нужный элемент нацело и есть ли на этой строке 1 из предыдущих итераций
                if (matrix[i][j] % matrix[i][iteration] != 0) {
                    flag = false;
                    break;
                } else
                    flag = true;
            }

            if (flag) {
                temp = matrix[i][iteration];

                for (int j = iteration; j < column; j++) {
                    matrix[i][j] /= temp;

                    //глупая проверка, но при делении 0 на отрицательное число получается -0 :)
                    if(matrix[i][j]==-0)
                        matrix[i][j]=0;
                }
                return true;
            }
        }

        return false;
    }

    //делаем 0 в остальных столбцах, если есть 1 в какой-либо строке на месте текущей итерации
    public boolean makeSteppedView() {
        double temp;
        if (columnWithOne[iteration] != 0) {
            int indexLineWithOne = columnWithOne[iteration] - 1;

            for (int i = iteration; i < line; i++) {

                //пропуск строки где стоит 1
                if (i == indexLineWithOne)
                    continue;

                //число, которое станет 0 в строке
                temp = -matrix[i][iteration];

                for (int j = 0; j < column; j++) {
                    matrix[i][j] += (matrix[indexLineWithOne][j] * temp);
                }
            }
            return true;
        } else
            return false;
    }

    //проверка на наличие 1 в заданном столбце
    public void checkCurrentColumnWithOne() {
        for (int i = iteration; i < line; i++) {
            if (matrix[i][iteration] == 1) {
                // на 1 больше для проверки условия
                columnWithOne[iteration] = i + 1;
                break;
            }
        }
    }

    //проверка на соответствие ступенчитого вида столбца матрицы на определенной итерации
    public boolean checkSteppedViewColumn() {
        int countNullInColumn = 0;

        //проверка на 0 в столбце нужной итерации
        for (int i = 0; i < line; i++) {
            if (matrix[i][iteration] == 0)
                countNullInColumn++;
        }

        //проверка на соответствие кол-ва 0 и наличия 1 в столбце
        if (countNullInColumn == (line - iteration - 1) && columnWithOne[iteration] != 0) {
            return true;
        } else
            return false;
    }

    //приведение к ступенчатому виду матрицы
    public void makeSteppedViewMatrix() {
        double[] temp = new double[column];

        if (columnWithOne[iteration] - 1 != iteration) {

            for (int i = 0; i < column; i++) {
                temp[i] = matrix[iteration][i];
            }

            for (int i = 0; i < column; i++) {
                matrix[iteration][i] = matrix[columnWithOne[iteration] - 1][i];
            }

            for (int i = 0; i < column; i++) {
                matrix[columnWithOne[iteration] - 1][i] = temp[i];
            }
        }
    }

    public int getIteration() {
        return iteration;
    }

    public void setIteration(int iteration) {
        this.iteration = iteration;
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }
}
