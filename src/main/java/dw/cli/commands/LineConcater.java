package dw.cli.commands;

class LineConcater {
	private boolean _firstLinePrinted = false;
	private final String _separator;
	private String _concatenation = "";

	public LineConcater(){
		this("\n");
	}

	public LineConcater(String separator){
		_separator = separator;
	}

	public void addLine(String line){
		if ( _firstLinePrinted ){
			_concatenation += _separator;
		} else {
			_firstLinePrinted = true;
		}

		_concatenation += line;
	}

	@Override
	public String toString(){
		return _concatenation;
	}
}
