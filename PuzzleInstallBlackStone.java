import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.util.Timer;

public class PuzzleInstallBlackStone extends JPanel {
	static final int EMPTY = 0;
    static final int BLACKSTONE = 1;
	static final int YMAX = 8, XMAX = 8;
	ArrayList<Figure> figs = new ArrayList<Figure>();
    int blackStoneNumber = 0;
	int[][] board = new int[YMAX][XMAX];
	Text t1 = new Text(20, 20, "黒石をおいてください", new Font("Serif", Font.BOLD, 22));

	public PuzzleInstallBlackStone() {
		figs.add(t1);
		for (int i = 0; i < YMAX * XMAX; ++i) {
			int r = i / YMAX, c = i % YMAX;
			figs.add(new Rect(Color.GREEN, 80 + r * 20, 40 + c * 20, 18, 18));
		}
		setOpaque(false);
        Timer timer = new Timer(false);
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                int min = 0;
                int max = 7;
                int x = (int) (Math.random()*(max - min)) + min;
                int y = (int) (Math.random()*(max - min)) + min;
				if (board[y][x] != EMPTY) {
					t1.setText("空いていません");
					repaint();
					return;
				}
				int a = ck(x, y, 1, 1), b = ck(x, y, 1, -1), c = ck(x, y, 1, 0), d = ck(x, y, 0, 1);
				if (a > 1 || b > 1 || c > 1 || d > 1) {
					t1.setText("縦横斜めに既に黒石が存在するので、置けません。");
				} else {
                    t1.setText("成功！！");
                    figs.add(new BlackStone(80 + x * 20, 40 + y * 20, 8));
                    board[y][x] = BLACKSTONE;
                    blackStoneNumber++;
				}
                if (blackStoneNumber == 8) {
                    t1.setText("終了！！");
                    timer.cancel();
                }
				repaint();
            }
        };
        timer.schedule(task, 2000, 1000);
	}

	public Rect pick(int x, int y) {
		Rect r = null;
		for (Figure f : figs) {
			if (f instanceof Rect && ((Rect) f).hit(x, y)) {
				r = (Rect) f;
			}
		}
		return r;
	}

	public void paintComponent(Graphics g) {
		for (Figure f : figs) {
			f.draw(g);
		}
	}

	private int ck(int x, int y, int dx, int dy) {
		int s = BLACKSTONE, count = 1;
		for (int i = 1; i < 8; ++i) {
            if (ck1(x + dx * i, y + dy * i, s)) {
			    ++count;
            }
		}
		for (int i = 1; i < 8; ++i) {
            if (ck1(x - dx * i, y - dy * i, s)) {
			    ++count;
            }
		}
		return count;
	}

	private boolean ck1(int x, int y, int s) {
		return 0 <= x && x < XMAX && 0 <= y && y < YMAX && board[y][x] == s;
	}

	public static void main(String[] args) {
		JFrame app = new JFrame();
		app.add(new PuzzleInstallBlackStone());
		app.setSize(500, 300);
		app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		app.setVisible(true);
	}

	interface Figure {
		public void draw(Graphics g);
	}

	static class Text implements Figure {
		int xpos, ypos;
		String txt;
		Font fn;

		public Text(int x, int y, String t, Font f) {
			xpos = x;
			ypos = y;
			txt = t;
			fn = f;
		}

		public void setText(String t) {
			txt = t;
		}

		public void draw(Graphics g) {
			g.setColor(Color.BLACK);
			g.setFont(fn);
			g.drawString(txt, xpos, ypos);
		}
	}

    static class BlackStone implements Figure {
        int xpos, ypos, size;

        public BlackStone(int x, int y, int s) {
            xpos = x;
            ypos = y;
            size = s;
        }

        public void draw(Graphics g) {
            g.setColor(Color.BLACK);
            g.fillOval(xpos - size, ypos - size, 2 * size, 2 * size);
        }
    }

	static class Rect implements Figure {
		Color col;
		int xpos, ypos, width, height;

		public Rect(Color c, int x, int y, int w, int h) {
			col = c;
			xpos = x;
			ypos = y;
			width = w;
			height = h;
		}

		public boolean hit(int x, int y) {
			return xpos - width / 2 <= x && x <= xpos + width / 2 && ypos - height / 2 <= y && y <= ypos + height / 2;
		}

		public int getX() {
			return xpos;
		}

		public int getY() {
			return ypos;
		}

		public void draw(Graphics g) {
			g.setColor(col);
			g.fillRect(xpos - width / 2, ypos - height / 2, width, height);
		}
	}
}