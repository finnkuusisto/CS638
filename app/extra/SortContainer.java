package extra;

public class SortContainer<T> implements Comparable<SortContainer<T>> {

	public double sortVal;
	public T obj;
	
	public SortContainer(double sortVal, T obj) {
		this.sortVal = sortVal;
		this.obj = obj;
	}
	
	@Override
	public int compareTo(SortContainer<T> o) {
		if (this.sortVal < o.sortVal) {
			return -1;
		}
		else if (this.sortVal > o.sortVal) {
			return 1;
		}
		return 0;
	}
	
}
