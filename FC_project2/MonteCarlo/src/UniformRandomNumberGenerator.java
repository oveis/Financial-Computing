import java.util.Random;
public class UniformRandomNumberGenerator implements RandomVectorGenerator{
	
	private int N;
	
	public UniformRandomNumberGenerator(int N){
		this.N = N;
	}

	@Override
	public double[] getVector() {
		double[] vector = new double[N];
        Random rand = new Random();
		for ( int i = 0; i < vector.length; ++i){
            vector[i] = rand.nextGaussian();
			//vector[i] = (Math.random() - 0.5) * 2* Math.sqrt(3);        // To make zero mean
		}
		return vector;
	}
}
