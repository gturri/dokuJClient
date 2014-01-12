package dw.cli;

public class Output {
	public String out = "";
	public String err = "";
	public int exitCode = 0;

	public Output(){ }

	public Output(String out){
		this.out = out;
	}

	public Output(String err, int exitCode){
		this.err = err;
		this.exitCode = exitCode;
	}
}
