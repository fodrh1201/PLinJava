package Etc;

public class Pos {
	private int row;
	private char col;
	
	public Pos(int row, char col) {
		this.row = row;
		this.col = col;
	}
	
	public Pos ds(int dx, int dy) {
		return new Pos((row+dx), (char)(col+dy));
	}
	
	public int x() {
		return row - 1;
	}
	public int y() {
		return (int)(col - 'a');
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + col;
		result = prime * result + row;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Pos other = (Pos) obj;
		if (col != other.col)
			return false;
		if (row != other.row)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "(" + row + ", " + col + ")";
	}
}
