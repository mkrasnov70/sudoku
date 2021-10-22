import java.util.ArrayList;
import java.util.Iterator;

public class Sudoku3 {

    public unit[][] sudokuPanel;
    public unit[][] complex;


    public Sudoku3() {
        int x,y;
        System.out.println("Initialize...");
        this.sudokuPanel = new unit[9][9];
        this.complex = new unit [27][9];

        for (int i = 0; 9 > i; i++){
            for (int j = 0; 9 > j; j++){
                this.sudokuPanel[j][i] = new unit();  // бежим по строкам [j] -  Номер столбца
// 0-8 строки - строки панели
// 9-17 строки - столбцы панели
// 18-26 строки - маленькие квадраты

                complex[i][j]=sudokuPanel[j][i];
                complex[j+9][i]=sudokuPanel[j][i];
                x = (i / 3) * 3 + (j /3); // посчитали в какой квадрат попали
                y = (i % 3)*3+(j % 3);    // посчитали номер в квадрате
                complex[x+18][y] = sudokuPanel[j][i];
            }
        }
    }

    public void doAll(){
        int S;

        do {
            S = getSumm();
            First();
        }
        while (S != getSumm() );

        do {
            S = getSumm();
            for (int i = 0; i < 27; i++) {
                killLonely(complex[i]);
                First();
            }

        }while (S != getSumm() );

    }

    public void printPanel(){
        for (int i=0;i<9;i++){
            if ((i % 3) == 0) System.out.println("-----------------------------------");
            for (int j=0; j<9; j++){
                System.out.printf("|%-3d",this.sudokuPanel[j][i].realNum);
            }
            System.out.println();
        }

    }


    public void printComplex(){
        for (int i=0;i<27;i++){
            for (int j=0; j<9; j++){
                System.out.printf("| %-2d |",this.complex[i][j].realNum);
            }
            System.out.println();
        }

    }

    public void printPossible() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                String str = sudokuPanel[j][i].possibleToStr();
                int c = 12 - str.length();
                for (int l = 0; l<c; l++) { str = str + " ";}
                System.out.print(str);  System.out.print(" || ");
            }
            System.out.println();
        }
    }


    public void setImp(int col, int row, int n){
        int x = (row / 3) * 3 + (col /3); // посчитали в какой квадрат попали
        for (int i = 0; i < 9; i++) {
            this.complex[row][i].setImpossibleNum(n);
            this.complex[col+9][i].setImpossibleNum(n);
            this.complex[x+18][i].setImpossibleNum(n);
        }
    }


    public int getSumm()    {
        int sum = 0;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (this.sudokuPanel[j][i].realNum == 0) {
                    for (int k = 1; k<this.sudokuPanel[j][i].possibleNum.size(); k++) {
                        sum = sum + this.sudokuPanel[j][i].possibleNum.get(k);
                    }

                }

            }
        }
        return sum;
    }


    public void First(){ // убираем невозможные варианты
        int qx;

        do {
            qx = getSumm();

            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    if (this.sudokuPanel[j][i].realNum != 0) {
                        setImp(j,i,this.sudokuPanel[j][i].realNum);
                    }
                }
            }

            for (int j = 0; j < 27; j++) {
                Third(complex[j]);
            }

        }
        while (qx != getSumm() );
    }


    public void Third(unit[] u){
        for (int i = 0; i < u.length; i++) {
            if (u[i].possibleNum.size() ==2) {
                for (int j = i+1; j < u.length ; j++){
                    if (u[j].possibleNum.size() ==2) {
                        // тут возможно есть  источник ошибок так как нельзя надеяться на сортировку элементов в списке
                        int a0 = u[i].possibleNum.get(0);
                        int a1 = u[i].possibleNum.get(1);
                        if ((u[j].possibleNum.get(0) == a0) & (u[j].possibleNum.get(1) == a1) ){
                            for (int k = 0; k< u.length; k++){
                                if ((k != i) & (k != j)) {
                                    u[k].setImpossibleNum(a0);
                                    u[k].setImpossibleNum(a1);
                                }
                            }
                        }

                    }
                }
            }
        }
    }
    public boolean isGoodUnit(unit[] u){
        for (int i = 0; i < 8; i++) {
            if (u[i].realNum != 0 ){
                for (int j = i+1; j < 9 ; j++) {
                    if (u[i].realNum == u[j].realNum) return false;
                }
            }
        }
        return true;
    }

    public boolean isGoodSudoku() {
        for (int i = 0; i < 27; i++) {
            if (!isGoodUnit(complex[i])) return false;
        }
        return true;
    }


    public void killLonely(unit[] u) {
        int[] p = new int[10];
        for (int i = 0; i < 9; i++) {
            if (u[i].realNum == 0) {
                Iterator<Integer> unitIterator = u[i].possibleNum.iterator();
                while(unitIterator.hasNext()) {
                    Integer nextItem = unitIterator.next();
                    p[nextItem.intValue()]++;
                }
            }
        }

        for (int l = 1; l < 10; l++) {
            if (p[l] == 1) {  // число l в возможных вариантах встретилось однажды
                //ищем ячейку где упоминалось l и вскрываем ее
                for (int i = 0; i < 9; i++) {// по всем ячейкам
                    if (u[i].realNum == 0) {
                        for (int k = 0; k < u[i].possibleNum.size(); k++) {
                            if (u[i].possibleNum.get(k) == l) {
                                u[i].setRealNum(l);  // это плохо
                            }
                        }
                    }
                }
            }
        }
    }



    public class unit {

        public ArrayList<Integer> possibleNum;
        public int realNum;

        public unit() {
            this.possibleNum = new ArrayList<Integer>();
            for (int i = 1; i<10; i++){
                this.possibleNum.add(i);
            }
            this.realNum = 0;
        }

        public void setImpossibleNum(int n) { // Запись примечания о том,  что значение n невозможно в этой ячейке
            if (this.realNum == 0){

                Iterator<Integer> unitIterator = possibleNum.iterator();
                while(unitIterator.hasNext()) {
                    Integer nextItem = unitIterator.next();
                    if (nextItem == n) unitIterator.remove();
                }

                // проверяем что остался  один элемент, если это так то ячейку вскрываем

                if (possibleNum.size() == 1) {
                    this.setRealNum(possibleNum.get(0));
                }
            }
        }


        public void setRealNum(int n){
            this.realNum = n;
        }


        public int numberOfPossible(){
            return possibleNum.size();
        }

        public String possibleToStr(){
            String str = new String();
            if (this.realNum == 0) {
                for (int i = 0; i < this.numberOfPossible(); i++) {
                    str += this.possibleNum.get(i);
                }
            }
            else {str = "---- "+ this.realNum + "----";}
            return str;
        }

    }

}






