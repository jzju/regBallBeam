package oliver;
// PI class to be written by you
public class PI {
	// Current PI parameters
	private PIParameters p;
	  private double I; // Integrator state
	  private double v; // Desired control signal
	  private double e; // Current control error
	
	// Constructor
	public PI(String name){
		  PIParameters p = new PIParameters();
		  p.Beta = 1.0;
		  p.H = 0.05;
		  p.integratorOn = false;
		  p.K = 4.0;
		  p.Ti = 0.0;
		  p.Tr = 10.0;
		  new PIGUI(this, p, name);
		  setParameters(p);
		  
		  this.I = 0.0;
		  this.v = 0.0;
		  this.e = 0.0;
	}
	
	// Calculates the control signal v.
	// Called from BeamRegul.
	public synchronized double calculateOutput(double y, double yref){
		  this.e = yref - y;
		  this.v = p.K * (p.Beta * yref - y) + I; // I is 0.0 if integratorOn is false
		  return this.v;	
		
	}
	
	
	// Updates the controller state.
	// Should use tracking-based anti-windup
	// Called from BeamRegul.
	public synchronized void updateState(double u){
		  if (p.integratorOn) {
			  I = I + (p.K * p.H / p.Ti) * e + (p.H / p.Tr) * (u - v);
		  } else {
			  I = 0.0;
		  }
	}
	
	// Returns the sampling interval expressed as a long.
	// Note: Explicit type casting needed
	public synchronized long getHMillis(){
		return (long)(p.H*1000);
	}
	
	// Sets the PIParameters.
	// Called from PIGUI.
	// Must clone newParameters.
	public synchronized void setParameters(PIParameters newParameters){
		this.p = (PIParameters)newParameters.clone();
		if(!p.integratorOn){
			I = 0.0;
		}
	}
	
}