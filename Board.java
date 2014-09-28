package gooseGame;

import gooseGame.cell.StartCell;

public abstract class Board {

	protected int size;
	protected Cell[] cells;


	/**
	 * Superclass constructor.
	 * 
	 * @param size the number of cells on the board (starting cell excluded)
	 */
	public Board(int size) {
		this.size = size;
		this.cells = new Cell[size + 1];
		this.cells[0] = new StartCell();
	}


	/**
	 * Initiates the board, i. e. creates the needed number of cells for the board. 
	 */
	protected abstract void init();

	/**
	 * Simulates the turn of the player p.
	 * 
	 * @param p
	 *            the player
	 * @param diceThrow
	 *            the result of the dice throw
	 */
	public void playTurn(Player p, int diceThrow) {
		Cell c = p.getCell();
		if (c.canBeLeftNow()) {
			int i = c.getIndex() + diceThrow;
			this.normalize(i);
			Cell a = this.getCell(i);
			int j = a.handleMove(diceThrow);
			this.normalize(j);
			a = this.getCell(i);
			System.out.println(p.getName() + " is in cell " + c.getIndex()
					+ ", " + p.getName() + " throws " + diceThrow
					+ " and reaches cell " + a.getIndex());
			if (a.isBusy()) {
				swap(a.getPlayer(), p);
			} else {
				this.moveTo(p, a);
			}
		} else {
			System.out.println(p.getName() + " is in cell " + c.getIndex()
					+ ", " + p.getName() + " cannot play.");
		}

	}

	/**
	 * If the number <code>i</code> exceeds the size of the board,
	 * <code>normalize (i)</code> simulates the backwards movement once the
	 * last cell is reached.
	 * 
	 * @param i
	 * @return a number on the board
	 */
	protected int normalize(int i) {
		if (i >= this.size) {
			return (size - (i - this.size)); /* A VERIFIER */
		}
		return i;
	}

	/**
	 * 
	 * @param i
	 * @return the cell corresponding to the number <code>i</code>
	 */
	protected Cell getCell(int i) {
		return cells[i];
	}

	/**
	 * <code>swap(p1, p2)</code> swaps the player p1 and the player p2, i.e p1
	 * goes to p2's cell and p2 goes to p1's cell.
	 * 
	 * @param p1
	 * @param p2
	 */
	protected void swap(Player p1, Player p2) {
		if (!(p1.equals(p2))) {
			Cell tmp = p1.getCell();
			p1.setCell(p2.getCell());
			p1.getCell().welcome(p1);
			p2.setCell(tmp);
			tmp.welcome(p2);
		}
	}

	/**
	 * Moves a player to a cell.
	 * 
	 * @param p
	 *            the player
	 * @param cell
	 *            the cell where p goes
	 */
	protected void moveTo(Player p, Cell cell) {
		p.getCell().welcome(null);
		p.setCell(cell);
		cell.welcome(p);
	}

	public Cell getLastCell() {
		return getCell(this.size);
	}

	/**
	 * Tests if the last cell is reached.
	 * 
	 * @return true if the last cell is reached, false otherwise
	 */
	public boolean lastCellReached() {
		return getLastCell().isBusy();
	}

}
