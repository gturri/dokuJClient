package dw.cli;

public class Output {
	public String out = "";
	public String err = "";
	public int exitCode = 0;

	public Output(){ }

	public Output(String out, String err, int exitCode){
		this.out = out;
		this.err = err;
		this.exitCode = exitCode;
	}
}
