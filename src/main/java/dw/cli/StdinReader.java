package dw.cli;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class StdinReader {
	public String readStdin() throws IOException{
		StringBuilder result = new StringBuilder();
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		try {
			String input;
			while((input=br.readLine())!=null){
				result.append(input + "\n");
			}

			if (result.length() > 0){
				result.delete(result.length()-1, result.length());
			}

			return result.toString();
		} finally {
			br.close();
		}
	}

}
