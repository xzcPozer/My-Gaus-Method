import java.util.InputMismatchException;
import java.util.Scanner;

public class Test {
    public static void main(String[] args) {
        System.out.println("Введите кол-во неизвестных(кол-во неизвестных равно кол-ву уравнений(строк в матрице))");

        Scanner scanner = new Scanner(System.in);
        int x;

        while (true){
            try {
                x = scanner.nextInt();
                break;
            } catch (InputMismatchException e) {
                System.out.println("Неверный формат данных!(Введите целое число)");
                scanner.nextLine();
            }
        }


        GausMethod ex1 = new GausMethod(x);
        ex1.enterMatrix();

        while (ex1.getIteration() < ex1.getLine()) {
            ex1.printMatrix();

            //проверка на наличие 1
            ex1.checkCurrentColumnWithOne();

            //проверка на ступенчатый вид матрицы и преобразование к нему
            if (ex1.checkSteppedViewColumn())
                ex1.makeSteppedViewMatrix();
            else {
                //если нет 1 в столбце на итерации
                if (!ex1.makeSteppedView()) {
                    //если можно поделить нацело
                    if (ex1.makeCompleteDivision()) {
                        ex1.printMatrix();
                        ex1.checkCurrentColumnWithOne();
                        ex1.makeSteppedView();
                    } else {
                        ex1.makeHardDivision();
                        ex1.printMatrix();
                        ex1.checkCurrentColumnWithOne();
                        ex1.makeSteppedView();
                    }
                }

                //проверяем наличие нулевых строк
                if (ex1.checkFollowingLineOnNull()) {
                    ex1.deleteNullString();
                }

                ex1.makeSteppedViewMatrix();
                ex1.setIteration(ex1.getIteration() + 1);
            }
        }

        ex1.makeIdentityMatrix();

        ex1.printMatrix();
    }
}
