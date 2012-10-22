public class GBMRandomPathGenerator implements PathGenerator {
	
	private double rate;            // interest rate
	private double sigma;           // volatility
	private double S0;               // Today
	private int N;                    // Segment Of Length
	private RandomVectorGenerator rvg;
    private int expiration;         // expiration ( unit : 1 year)

	public GBMRandomPathGenerator(double rate, int N,
		double sigma, double S0, RandomVectorGenerator rvg, int expiration){
		this.rate = rate;
		this.S0 = S0;
		this.sigma = sigma;
		this.N = N;
		this.rvg = rvg;
        this.expiration = expiration;
	}

	@Override
	public Path getPath() {
		double[] n = rvg.getVector();
		final double[] prices = new double[N+1];
		prices[0] = S0;

		for ( int i=1; i < N+1; ++i){
			prices[i] = prices[i-1]*Math.exp((rate-sigma*sigma/2)+sigma * n[i-1]);       // Geometric Brownian motion
        }

		return new Path() {
			@Override
            public double getT0(){ return 0; }
            
            @Override
            public double getT1(){ return expiration; }
            
            @Override
            public double getDelta(){ return (expiration-0)/(double)N; }

			@Override
			public double[] getPrices() { return prices;	}
		};
	}
}
