import java.util.InputMismatchException;
import java.util.Scanner;

public class Test {
    public Test() {
    }

    public static void main(String[] args) {
        System.out.println("Введите кол-во неизвестных(кол-во неизвестных равно кол-ву уравнений(строк в матрице))");
        Scanner scanner = new Scanner(System.in);

        int x;
        while(true) {
            try {
                x = scanner.nextInt();
                break;
            } catch (InputMismatchException var4) {
                System.out.println("Неверный формат данных!(Введите целое число)");
                scanner.nextLine();
            }
        }

        GausMethod ex1 = new GausMethod(x);
        ex1.enterMatrix();

        while(ex1.getIteration() < ex1.getLine()) {
            ex1.printMatrix();
            ex1.checkCurrentColumnWithOne();
            if (ex1.checkSteppedViewColumn()) {
                ex1.makeSteppedViewMatrix();
            } else {
                if (!ex1.makeSteppedView()) {
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
