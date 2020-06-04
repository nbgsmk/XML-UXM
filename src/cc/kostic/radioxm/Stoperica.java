package cc.kostic.radioxm;

public class Stoperica {
	private long pocetak;
	private long kraj;
	
	public Stoperica() {
		//
	}
	
	public long run() {
		pocetak = System.currentTimeMillis();
		return pocetak;
	}
	
	public long stop() {
		this.kraj = System.currentTimeMillis();
		return (kraj - pocetak) / 1000;
	}
	
	public long getCurrentLapTime() {
		return (System.currentTimeMillis() - pocetak) / 1000;
	}
	
	public long getTotalElapsedSec() {
		return (kraj - pocetak) / 1000;
	}
	

}
