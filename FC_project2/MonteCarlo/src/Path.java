public interface Path {
	public double getT0();          // start to calculation
    public double getT1();          // expiration ( Unit is one year)
    public double getDelta();       // segments of length
	public double[] getPrices();    // path of stock price
}
