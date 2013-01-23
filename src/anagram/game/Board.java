package anagram.game;
import java.util.ArrayList;
import java.util.Arrays;


public class Board {
	private int xSize;
	private int ySize;
	private int cellXSize;
	private int cellYSize;
	private Cell[] cells;
	private Brick[] bricks;
	private int spacing;
	private Brick selectedBrick;
	private int dx, dy;
	private int steps;
	private Puzzle puzzle;
	private int moverCount = 0;
	

	public Board(int xSize, int ySize, int cellXSize, int cellYSize,
			int spacing, int steps) {
		this.xSize = xSize;
		this.ySize = ySize;
		this.cellXSize = cellXSize;
		this.cellYSize = cellYSize;
		this.spacing = spacing;
		this.steps = steps;
	}
	
	public int getCellXSize() {
		return cellXSize;
	}

	public int getCellYSize() {
		return cellYSize;
	}

	public Brick[] getBricks() {
		return bricks; // TODO: dangerous
	}
	public void start(Puzzle puzzle) {
		this.puzzle = puzzle;
		int n = puzzle.solutionWord.length();
		cells = new Cell[n];
		bricks = new Brick[n];
		int indentx = (xSize - n * cellXSize - (n - 1) * spacing) / 2;
		int indenty = (ySize - 3 * cellYSize - 2 * spacing) / 2;
		for (int i = 0; i < n; i++) {
			int x = indentx + i * (cellXSize + spacing);
			Cell solveCell     = new Cell(x, indenty);
			Cell challengeCell = new Cell(x, indenty + 2 * (cellYSize + spacing));
			Brick brick = new Brick(challengeCell,
					puzzle.challengeWord.charAt(i));
			brick.moveHome();
			cells[i] = solveCell;
			bricks[i] = brick;
		}
	}

	public boolean press(int x, int y) {
		if (selectedBrick != null) return false;
		if (moverCount > 0) return false;
		for (Brick brick : bricks) {
			if (brick.hit(x, y)) {
				selectedBrick = brick;
				dx = x - brick.xPos;
				dy = y - brick.yPos;
				brick.moveOut();
				break;
			}
		}
		return true;
	}
	public boolean drag(int x, int y) {
		if (selectedBrick == null) return false;
		if (moverCount > 0) return false;
		selectedBrick.moveTo(x-dx, y-dy);
		return true;
	}
	public Mover release(int x, int y) {
		if (selectedBrick == null) return null;
		if (moverCount > 0) return null;
		selectedBrick.moveTo(x-dx, y-dy);
		LinearMover mover = new LinearMover();
		if (selectedBrick.yPos > ySize/2) {
			mover.add(selectedBrick, selectedBrick.home);
		} else {
			int nearest = findNearest(selectedBrick);
			if (cells[nearest].curr == null) {
				mover.add(selectedBrick, cells[nearest]);
			} else {
				boolean left = insertLeft(selectedBrick, nearest); 
				if (left) {
					if (nearest>0 && cells[nearest-1].curr == null) {
						mover.add(selectedBrick, cells[nearest-1]);
					} else {
						int count = canMoveRight(nearest);
						if (count > 0) {
							for (int i=nearest ; i<nearest+count ; i++) {
								mover.add(cells[i].curr, cells[i+1]);
							}
							mover.add(selectedBrick, cells[nearest]);
						} else {
							//could not move right
							for (int i=nearest-1 ; ; i--) {
								if ((i<0) || (cells[i].curr == null)) break;
								if (i>0)
									mover.add(cells[i].curr, cells[i-1]);
							}
							if (nearest>0) {
								mover.add(selectedBrick, cells[nearest-1]);
							}
						}
					}
				} else {
					if (nearest<cells.length-1 && cells[nearest+1].curr == null) {
						mover.add(selectedBrick, cells[nearest+1]);
					} else {
						int count = canMoveLeft(nearest);
						if (count > 0) {
							for (int i=nearest ; i>nearest-count ; i--) {
								mover.add(cells[i].curr, cells[i-1]);
							}
							mover.add(selectedBrick, cells[nearest]);
						} else {
							//could not move left
							for (int i=nearest+1 ; ; i++) {
								if ((i>=cells.length) || (cells[i].curr == null)) break;
								if (i<cells.length-1)
									mover.add(cells[i].curr, cells[i+1]);
							}
							if (nearest<cells.length-1) {
								mover.add(selectedBrick, cells[nearest+1]);
							}
						}
					}
				}
			}
		}
		selectedBrick = null;
		return mover;
	}

	private int canMoveLeft(int nearest) {
		for (int i=nearest-1 ; i>=0 ; i--) {
			if (cells[i].curr == null) return nearest - i;
		}
		return 0;
	}

	/** 
	 * can a row of bricks move right? If yes, return number of bricks to move, if no return o
	 * @param nearest
	 * @return
	 */
	private int canMoveRight(int nearest) {
		for (int i=nearest+1 ; i<cells.length ; i++) {
			if (cells[i].curr == null) return i - nearest;
		}
		return 0;
	}

	/** 
	 * if insertion point is to the left of the nearest return true
	 * @param brick
	 * @param nearest
	 * @return
	 */
	private boolean insertLeft(Brick brick, int nearest) {
		if (nearest == 0) return brick.xPos < cells[0].xCenter;
		if (nearest == cells.length-1) return brick.xPos < cells[nearest].xCenter;
		boolean leftNearerThanRight = cells[nearest-1].distance(brick) < cells[nearest+1].distance(brick);
		return leftNearerThanRight;
	}

	/**
	 * returns index of nearest cell

	 * @param brick
	 * @return
	 */
	private int findNearest(Brick brick) {
		int distance = cells[0].distance(brick);
		int ndx = 0;
		for (int i=1 ; i<cells.length ; i++) {
			int d = cells[i].distance(brick);
			if (distance > d) {
				distance = d;
				ndx = i;
			} else {
				break;
			}
		}
		return ndx;
	}

	class Cell {
		private int xPos;
		private int yPos;
		private int xCenter;
		private int yCenter;
		private Brick curr;

		public Cell(int xPos, int yPos) {
			this.xPos = xPos;
			this.yPos = yPos;
			xCenter = xPos + cellXSize / 2;
			yCenter = yPos + cellYSize / 2;
		}

		public int distance(Brick brick) {
			int dx = brick.xPos - xCenter;
			int result = Math.abs(dx);
			return result;
		}

		@Override
		public String toString() {
			return "Cell [xCenter=" + xCenter + ", yCenter=" + yCenter
					+ ", curr=" + ((curr == null) ? "-" : curr.value) + "]";
		}
	}

	@Override
	public String toString() {
		return "Board [cells=" + Arrays.toString(cells) + ", bricks="
				+ Arrays.toString(bricks) + ", selectedBrick=" + selectedBrick
				+ "]";
	}
	public Mover checkSolved() {
		char[] tab = new char[cells.length];
		for (int i=0 ; i<cells.length ; i++) {
			tab[i] = cells[i].curr != null ? cells[i].curr.value : ' ';
		}
		String s = String.valueOf(tab).trim();
		if (s.equals(puzzle.solutionWord)) {
			return new DancingMover(250);
		} else {
			return null;
		}
	}

	public class Brick {
		public char getValue() {
			return value;
		}

		public int getxPos() {
			return xPos;
		}

		public int getyPos() {
			return yPos;
		}

		private Cell home;
		private Cell curr;
		private char value;
		private int xPos;
		private int yPos;

		public Brick(Cell home, char value) {
			this.home = home;
			this.value = value;
			moveIn(home);
		}

		private void moveIn(Cell cell) {
			moveTo(cell.xCenter, cell.yCenter);
			curr = cell;
			cell.curr = this;
		}
		private void moveOut() {
			if (curr == null) return;
			curr.curr = null;
			curr = null;
		}

		public void moveTo(int x, int y) {
			xPos = x;
			yPos = y;
		}

		public void moveHome() {
			moveIn(home);
			
		}

		public boolean hit(int x, int y) {
			if (Math.abs(x-xPos) > cellXSize/2) return false;
			if (Math.abs(y-yPos) > cellYSize/2) return false;
			return true;
		}

		@Override
		public String toString() {
			return "Brick [home=" + home + ", curr=" + curr + ", value="
					+ value + ", xPos=" + xPos + ", yPos=" + yPos + "]";
		}

		public void moveRel(int[] dpos) {
			xPos += dpos[0];
			yPos += dpos[1];
		}
	}
	public interface Mover {
		/** 
		 * does a move, returns true if more moves left
		 * @return
		 */
		boolean move();
	}
	public class LinearMover implements Mover {
		private ArrayList<BrickCell> list = new ArrayList<BrickCell>();
		private int step = 0;
		private int[][][] positions;
		public void add(Brick brick, Cell cell) {
			list.add(new BrickCell(brick, cell));
		}
		@Override
		public boolean move() {
			if (step == 0) firstMove();
			moveBricks();
			step++;
			if (step == steps) lastMove();
			return step < steps;
		}
		private void lastMove() {
			for (BrickCell bc : list) {
				bc.b.moveIn(bc.c);
			}
			moverCount--;
		}

		private void moveBricks() {
			for (int j=0 ; j<list.size() ; j++) {
				BrickCell bc = list.get(j);
				bc.b.moveTo(positions[step][j][0], positions[step][j][1]);
			}
		}

		private void firstMove() {
			moverCount++;
			for (BrickCell bc : list) {
				bc.b.moveOut();
			}
			positions = new int[steps][list.size()][2];
			for (int j=0 ; j<list.size() ; j++) {
				BrickCell bc = list.get(j);
				int dx = bc.c.xCenter - bc.b.xPos;
				int dy = bc.c.yCenter - bc.b.yPos;
				for (int i=0 ; i<steps ; i++) {
					positions[i][j][0] = bc.b.xPos + (i+1)*dx/steps;
					positions[i][j][1] = bc.b.yPos + (i+1)*dy/steps;
				}
			}
		}

		@Override
		public String toString() {
			return "Mover [list=" + list + "]";
		}
	}
	public class DancingMover implements Mover {
		private int moves;
		private int n = 0;
		private int[][] positions;
		public DancingMover(int moves) {
			this.moves = moves;
			
			positions = new int[bricks.length][2];
			for (int i=0 ; i<bricks.length ; i++) {
				positions[i][0] = cells[i].curr.xPos;
				positions[i][1] = cells[i].curr.yPos;
			}
		}
		@Override
		public boolean move() {
			if (n>=moves) {
				moverCount--;
				return false;
			}
			if (n==0) {
				moverCount++;
			}
			int amp = 30 * (moves/2 - Math.abs(n - moves/2)) / moves;
			for (int i=0 ; i<bricks.length ; i++) {
				double angle = Math.PI/6*i + n*Math.PI/12;
				double dx = amp*Math.cos(angle);
				double dy = amp*Math.sin(angle);
				Brick curr = cells[i].curr;
				if (curr == null) {
					throw new RuntimeException("WHAT the HECK?");
				}
				curr.moveTo((int)(positions[i][0] + dx), (int)(positions[i][1] + dy));
			}
			n++;
			return true;
		}

	}
	static class BrickCell {
		private Brick b;
		private Cell c;

		BrickCell(Brick b, Cell c) {
			this.b = b;
			this.c = c;
		}

		@Override
		public String toString() {
			return "BrickCell [b=" + b + ", c=" + c + "]";
		}
		
	}
}
