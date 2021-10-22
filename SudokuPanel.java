import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import static java.awt.BorderLayout.*;

public class SudokuPanel extends JFrame {
    JButton buttons[];
    JButton goButton;
    JPanel panel,goPanel,bigPanel;
    Dpanel dpanel;
    boolean start;
    int number;
    Sudoku3 MyS2;

   public SudokuPanel() {
        MyS2 = new Sudoku3();
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        bigPanel = new JPanel();
        bigPanel.setLayout(new BoxLayout(bigPanel, BoxLayout.Y_AXIS));
        add(bigPanel);
        panel = new JPanel();
        panel.setLayout(new GridLayout(9, 9, 5,5));
        buttons = new JButton[81];
        PressActionListener pressListener = new PressActionListener();
        for (int i = 0; i < 81; i++) {
            buttons[i] = new JButton();
            buttons[i].setVisible(true);
            buttons[i].setText("0");
            buttons[i].setActionCommand(String.valueOf(i));
            panel.add(buttons[i]);
            buttons[i].addActionListener(pressListener);

            if (((i/27)*3 + ((i%9)/3)) % 2 == 0) {
                buttons[i].setBackground(Color.LIGHT_GRAY);
            }


        }
        panel.setVisible(true);
        goPanel = new JPanel();
        goPanel.setSize(50, 50);
        goPanel.setVisible(true);
        goButton = new JButton();
        goButton.setText("Start");
        goButton.setActionCommand("start");
        PressStartListener pressStartListener = new PressStartListener();
        goButton.addActionListener(pressStartListener);
        goPanel.add(goButton);
        bigPanel.add(panel, NORTH);
        bigPanel.add(goPanel, SOUTH);

        pack();
        setVisible(true);



        dpanel = new Dpanel();


        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                setVisible(false);
                dispose();
            }
        });
        }


// Нажатие на ячейку судоку
    class PressActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            dpanel.setVisible(true);
            number = Integer.parseInt(e.getActionCommand());
        }
   }

   // Нажатие на старт
    class PressStartListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int p;

            for (p =0; p< 81; p++){
                int x = p / 9 ;
                int y = p % 9;
               MyS2.sudokuPanel[y][x].realNum = Integer.parseInt(buttons[p].getText());
            }

              MyS2.doAll();

            for (p =0; p< 81; p++){
                int x = p / 9 ;
                int y = p % 9;
                buttons[p].setText(String.valueOf(MyS2.sudokuPanel[y][x].realNum));
            }

            if (MyS2.isGoodSudoku()) {
                System.out.println("Решение :   ");
                MyS2.printPanel();

                if (MyS2.getSumm() != 0) {
                    System.out.println(" возможные варианты ");
                    MyS2.printPossible();
                }
            }
            else System.out.println("Условие противоречиво");
        }
    }


    class Dpanel extends JFrame{
        public Dpanel(){
            setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
            setSize(220,220);

            JPanel dpanel = new JPanel();
            JButton[] dbuttons = new JButton[9];
            dpanel.setLayout(new GridLayout(3, 3, 5,5));
            add(dpanel);

            // выбор числа в ячейке
            ActionListener daction = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    buttons[number].setText(e.getActionCommand());
                    setVisible(false);
                }
            };
            for (int i = 0; i < 9; i++) {
                dbuttons[i] = new JButton();
                dbuttons[i].setVisible(true);
                dbuttons[i].setText(String.valueOf(i+1));
                dbuttons[i].setActionCommand(String.valueOf(i+1));
                dbuttons[i].addActionListener(daction);
                dpanel.add(dbuttons[i]);
            }
        }
    }

    public static void main(String[] args) {
    javax.swing.SwingUtilities.invokeLater(new Runnable() {
        public void run() {
            SudokuPanel frame = new SudokuPanel();
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.addWindowListener(new WindowListener() {
                @Override
                public void windowOpened(WindowEvent e) {

                }

                @Override
                public void windowClosing(WindowEvent e) {
                    if (frame.dpanel != null) {
                        frame.dpanel.dispose();
                    }
                }

                @Override
                public void windowClosed(WindowEvent e) {

                }

                @Override
                public void windowIconified(WindowEvent e) {

                }

                @Override
                public void windowDeiconified(WindowEvent e) {

                }

                @Override
                public void windowActivated(WindowEvent e) {

                }

                @Override
                public void windowDeactivated(WindowEvent e) {

                }
            });



            frame.setVisible(true);

            Point p;
            Dimension d;
            p = frame.getLocation();
            d = frame.getSize();
            if (p.x>240){
                p.x-=240;
            }
            else {
                p.x = p.x + d.width +20;
            }
            frame.dpanel.setLocation(p);

        }
    });

    }
}
