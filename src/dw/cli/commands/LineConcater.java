package dw.cli.commands;

class LineConcater {
	boolean firstLinePrinted = false;
	String concatenation = "";

	public void addLine(String line){
		if ( firstLinePrinted ){
			concatenation += "\n";
		} else {
			firstLinePrinted = true;
		}

		concatenation += line;
	}

	@Override
	public String toString(){
		return concatenation;
	}
}
