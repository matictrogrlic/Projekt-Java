import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;
import javax.swing.JFrame;

public class StiriVVrsto extends JFrame {

    private static final int VRSTICE = 6;
    private static final int STOLPCI = 7;
    private static final int VELIKOST = 100;

    private int[][] plosca;
    private int trenutniIgralec;
    private boolean konec;
    private String zmagovalec = "";
    private Color zmagabarva = Color.BLACK;

    public StiriVVrsto() {
        plosca = new int[VRSTICE][STOLPCI];
        trenutniIgralec = 1;
        konec = false;

        setTitle("Štiri v vrsto");
        setSize(STOLPCI * VELIKOST + 18, VRSTICE * VELIKOST + 140);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        JPanel panelPlosce = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                narisiPlosco(g);
                legenda(g);
                razglasitev(g);
            }
        };

        panelPlosce.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!konec) {
                    int stolpec = e.getX() / VELIKOST;
                    if (veljavnaPoteza(stolpec)) {
                        poteza(stolpec);
                        zmaga();
                        trenutniIgralec = trenutniIgralec == 1 ? 2 : 1;
                        repaint();
                    }
                }
            }
        });

        getContentPane().add(panelPlosce);
        setVisible(true);
    }

    private void narisiPlosco(Graphics g) {
        for (int vrstica = 0; vrstica < VRSTICE; vrstica++) {
            for (int stolpec = 0; stolpec < STOLPCI; stolpec++) {
                int x = stolpec * VELIKOST;
                int y = (VRSTICE - 1 - vrstica) * VELIKOST;
                Color barva = pridobiBarvo(plosca[vrstica][stolpec]);
                g.setColor(barva);
                g.fillOval(x, y, VELIKOST, VELIKOST);
            }
        }
    }

    private void legenda(Graphics g) {
        Font font = new Font("Arial", Font.BOLD, 16);
        g.setFont(font);
        g.setColor(Color.BLACK);
        g.drawString("Legenda:", 10, VRSTICE * VELIKOST + 40);
        g.setColor(Color.BLACK);
        g.drawString("Igralec 1", 100, VRSTICE * VELIKOST + 40);
        g.setColor(Color.BLUE);
        g.fillOval(190, VRSTICE * VELIKOST + 25, 20, 20);
        g.setColor(Color.BLACK);
        g.drawString("Igralec 2", 100, VRSTICE * VELIKOST + 70);
        g.setColor(Color.GREEN);
        g.fillOval(190, VRSTICE * VELIKOST + 55, 20, 20);
        
    }
    private void razglasitev(Graphics g) {
        Font font = new Font("Arial", Font.BOLD, 16);
        g.setFont(font);
        if (konec) {
            g.setColor(Color.WHITE);
            g.fillRect(getWidth() / 2 - 112, getHeight() / 2 - 125, 248, 36);
	        g.setColor(zmagabarva);
	        g.setFont(new Font("Arial", Font.BOLD, 24));
	        g.drawString(zmagovalec, getWidth() / 2 - 100, getHeight() / 2 - 100);
	    }
    }

    private Color pridobiBarvo(int igralec) {
        switch (igralec) {
            case 1:
                return Color.BLUE;
            case 2:
                return Color.GREEN;
            default:
                return Color.WHITE;
        }
    }

    private boolean veljavnaPoteza(int stolpec) {
        return stolpec >= 0 && stolpec < STOLPCI && plosca[VRSTICE - 1][stolpec] == 0;
    }

    private void poteza(int stolpec) {
        for (int vrstica = 0; vrstica < VRSTICE; vrstica++) {
            if (plosca[vrstica][stolpec] == 0) {
                plosca[vrstica][stolpec] = trenutniIgralec;
                break;
            }
        }
    }

    private void zmaga() {
        // vodoravno
        for (int vrstica = 0; vrstica < VRSTICE; vrstica++) {
            for (int stolpec = 0; stolpec < 4; stolpec++) { // ker vemo, da rabimo met vsaj 4 v vrsto
                int igralec = plosca[vrstica][stolpec];
                if (igralec != 0 && igralec == plosca[vrstica][stolpec + 1] &&
                        igralec == plosca[vrstica][stolpec + 2] && igralec == plosca[vrstica][stolpec + 3]) {
                    konec = true;
                    zmagovalec = "Igralec " + igralec + " je zmagal!";
                    zmagabarva = pridobiBarvo(igralec);
                    repaint();
                    return;
                }
            }
        }

        // navpično
        for (int vrstica = 0; vrstica < 3; vrstica++) {
            for (int stolpec = 0; stolpec < STOLPCI; stolpec++) {
                int igralec = plosca[vrstica][stolpec];
                if (igralec != 0 && igralec == plosca[vrstica + 1][stolpec] &&
                        igralec == plosca[vrstica + 2][stolpec] && igralec == plosca[vrstica + 3][stolpec]) {
                    konec = true;
                    zmagovalec = "Igralec " + igralec + " je zmagal!";
                    zmagabarva = pridobiBarvo(igralec);
                    repaint();
                    return;
                }
            }
        }

        // diagonala (zgoraj levo --> spodaj desno)
        for (int vrstica = 0; vrstica < 3; vrstica++) {
            for (int stolpec = 0; stolpec < 4; stolpec++) {
                int igralec = plosca[vrstica][stolpec];
                if (igralec != 0 && igralec == plosca[vrstica + 1][stolpec + 1] &&
                        igralec == plosca[vrstica + 2][stolpec + 2] && igralec == plosca[vrstica + 3][stolpec + 3]) {
                    konec = true;
                    zmagovalec = "Igralec " + igralec + " je zmagal!";
                    zmagabarva = pridobiBarvo(igralec);
                    repaint();
                    return;
                }
            }
        }

        // diagonala (zgoraj desno --> spodaj levo)
        for (int vrstica = 0; vrstica < 3; vrstica++) {
            for (int stolpec = 3; stolpec < STOLPCI; stolpec++) {
                int igralec = plosca[vrstica][stolpec];
                if (igralec != 0 && igralec == plosca[vrstica + 1][stolpec - 1] &&
                        igralec == plosca[vrstica + 2][stolpec - 2] && igralec == plosca[vrstica + 3][stolpec - 3]) {
                    konec = true;
                    zmagovalec = "Igralec " + igralec + " je zmagal!";
                    zmagabarva = pridobiBarvo(igralec);
                    repaint();
                    return;
                }
            }
        }

        // neodločeno
        boolean polna = true;
        for (int stolpec = 0; stolpec < STOLPCI; stolpec++) {
            if (plosca[VRSTICE - 1][stolpec] == 0) {
                polna = false;
                break;
            }
        }

        if (polna) {
            konec = true;
            zmagovalec = "Igra je neodločena!";
            repaint();
        }
    }

    public static void main(String[] args) {
        new StiriVVrsto();
    }
}