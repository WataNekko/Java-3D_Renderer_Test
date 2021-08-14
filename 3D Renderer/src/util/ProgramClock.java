package util;

public class ProgramClock {
	private long start;
	private int unit;
	public static final int IN_SECOND = 2;
	public static final int IN_MILLISECOND = 1;
	public static final int IN_NANOSECOND = 0;

	public ProgramClock(int unit) {
		reset(unit);
	}

	public ProgramClock() {
		this(IN_NANOSECOND);
	}

	public void reset() {
		if (this.unit == IN_SECOND)
			this.start = System.currentTimeMillis() / 1000;
		else if (this.unit == IN_MILLISECOND)
			this.start = System.currentTimeMillis();
		else
			this.start = System.nanoTime();
	}

	public void reset(int unit) {
		this.unit = unit;
		reset();
	}

	public int getClockUnit() {
		return this.unit;
	}

	public long currentTime() {
		if (this.unit == IN_SECOND)
			return (System.currentTimeMillis() / 1000) - this.start;
		else if (this.unit == IN_MILLISECOND)
			return System.currentTimeMillis() - this.start;
		else
			return System.nanoTime() - this.start;
	}

	public long currentTimeMillis() {
		if (this.unit == IN_SECOND)
			return -1;
		if (this.unit == IN_MILLISECOND)
			return System.currentTimeMillis() - this.start;
		else
			return System.currentTimeMillis() - (this.start / 1000000);
	}

	public long currentTimeSecond() {
		if (this.unit == IN_SECOND)
			return (System.currentTimeMillis() / 1000) - this.start;
		if (this.unit == IN_MILLISECOND)
			return (System.currentTimeMillis() - this.start) / 1000;
		else
			return (System.nanoTime() - this.start) / 1000000000;
	}

	public String toString() {
		long t = currentTime();
		return t + (this.unit == IN_MILLISECOND ? " millisecond" : (this.unit == IN_SECOND ? " second" : " nanosecond")) + (t > 1 ? "s" : "");
	}
}
