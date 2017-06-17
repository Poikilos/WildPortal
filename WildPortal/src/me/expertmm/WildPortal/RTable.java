/**
 * @author Jacob Gustafson
 *
 */
package me.expertmm.WildPortal;


import java.util.ArrayList;
import java.util.List;


public class RTable {
	public static List<String> getExplodedCSVLine(String line) {
		return getExplodedDelimited(line,',','"');
	}
	public static String CSVFieldToLiteralField(String sCSVField) {//aka CSVFieldToString
		return DelimitedFieldToLiteralField(sCSVField,',','"',null);
	}
	public static String DelimitedFieldToLiteralField(String sCSVField, char cFieldDelimX, char cTextDelimX, String whatBecomesNewLine_ElseNullForEscapeSequences) {//aka CSVFieldToString
		//This logic is consistent with popular proprietary and open source spreadsheet applications.
		String oneQuote=""+cTextDelimX;
		String twoQuotes=""+cTextDelimX+""+cTextDelimX;
		if (sCSVField.contains(oneQuote)) {
			if (sCSVField.length()>=2&&sCSVField.charAt(0)==cTextDelimX&&sCSVField.charAt(sCSVField.length()-1)==cTextDelimX) {
				sCSVField=FrameworkDummy.Substring(sCSVField,1,sCSVField.length()-2);//debug performance (recreating string)
			}
			sCSVField=sCSVField.replace(twoQuotes, oneQuote);//debug performance (recreating string, type conversion from char to string)
		}
		if (whatBecomesNewLine_ElseNullForEscapeSequences==null) {
			sCSVField=sCSVField.replace("\\n","\n");
			sCSVField=sCSVField.replace("\\r","\r");
		}
		else {
			sCSVField=sCSVField.replace(whatBecomesNewLine_ElseNullForEscapeSequences,System.getProperty("line.separator"));
		}
		//for (int iNow=0; iNow<sarrEscapeLiteral.length(); iNow++) {
		//	sCSVField=RString.Replace(sCSVField,sarrEscapeSymbolic[iNow],sarrEscapeLiteral[iNow]);// (old,new)
		//}
		return sCSVField;//now a raw field
	}
	public static String RowToCSVLine(List<String> sarrLiteralFields, char cFieldDelimX, char cTextDelimX, String newLineBecomesWhat_ElseNullForEscapeSequences) {
		String returnString="";
		if (sarrLiteralFields!=null) returnString=PartialRowToCSVLine(sarrLiteralFields,cFieldDelimX,cTextDelimX,0,sarrLiteralFields.size(),newLineBecomesWhat_ElseNullForEscapeSequences);
		else {
			main.logWriteLine("RowToCSVLine cannot convert null row field array to CSV Line");
		}
		return returnString;
	}
	public static String PartialRowToCSVLine(List<String> sarrLiteralFields, char cFieldDelimX, char cTextDelimX, int ColumnStart, int ColumnCount, String newLineBecomesWhat_ElseNullForEscapeSequences) {
		String sReturn="";
		try {
			if (sarrLiteralFields!=null) {
				if (ColumnStart+ColumnCount>sarrLiteralFields.size()) ColumnCount=sarrLiteralFields.size()-ColumnStart;
				int iCol=ColumnStart;
				for (int iColRel=0; iColRel<ColumnCount; iColRel++) {
					sReturn += (iCol>0?(""+cFieldDelimX):"") + RTable.LiteralFieldToDelimitedField(sarrLiteralFields.get(iCol),cFieldDelimX,cTextDelimX,newLineBecomesWhat_ElseNullForEscapeSequences); 
					iCol++;
				}
			}
			else main.logWriteLine("PartialRowToCSVLine cannot convert null row field array to CSV Line");
		}
		catch (Exception e)
		{
			main.logWriteLine(e.getMessage());//System.err.println(e.getMessage());
		}
		return sReturn;
	}
	
	public static List<String> getExplodedDelimited(String line, char fieldDelimiter, char textDelimiter) {
		List<String> returnList = new ArrayList<String>();
		int index=0;
		boolean IsQuoted=false;
		int start=0;
		int endEx=1;
		//String thisValue="";
		while ( index <= line.length() ) {
			if ( index==line.length()
				|| (line.charAt(index)==fieldDelimiter && !IsQuoted) ) {
				endEx=index;
				returnList.add(RTable.DelimitedFieldToLiteralField(line.substring(start, endEx),fieldDelimiter,textDelimiter,null));//java substring 2nd param is exclusive index
				start=endEx+1;//+1 to avoid delimiter
				endEx=start+1;//+1 since exclusive
				index=endEx;//skip what we already know
			}
			else if (line.charAt(index)==textDelimiter) {
				IsQuoted=!IsQuoted;
			}
			index++;
		}
		return returnList;
	}
	public static String LiteralFieldToCSVField(String sCSVField) {//aka CSVFieldToString
		return LiteralFieldToDelimitedField(sCSVField,',','"',null);
	}
	public static String LiteralFieldToDelimitedField(String sCSVField, char cFieldDelimX, char cTextDelimX, String newLineBecomesWhat_ElseNullForEscapeSequences) {//aka CSVFieldToString
		String oneQuote=""+cTextDelimX;
		String twoQuotes=""+cTextDelimX+""+cTextDelimX;
		String fieldDelimiterString = ""+cFieldDelimX;
		//String textDelimiterString = ""+cTextDelimX;
		try {
			sCSVField=sCSVField.replace(oneQuote, twoQuotes);
			//if (ReplaceNewLineWithTabInsteadOfHTMLBr) {
			//	sCSVField=sCSVField.replace("\r\n", "\t");
			//	sCSVField=sCSVField.replace("\r", "\t");
			//	sCSVField=sCSVField.replace("\n", "\t");
			//}
			/*
			if (ReplaceNewLineWithEscapeSequencesInsteadOfHTMLBr) {
				sCSVField=sCSVField.replace("\r\n", "\t");
				sCSVField=sCSVField.replace("\r", "\t");
				sCSVField=sCSVField.replace("\n", "\t");
			}
			else {
				sCSVField=sCSVField.replace("\r\n", "<br/>");
				sCSVField=sCSVField.replace("\r", "<br/>");
				sCSVField=sCSVField.replace("\n", "<br/>");
			}
			*/
			if (newLineBecomesWhat_ElseNullForEscapeSequences!=null) {
				newLineBecomesWhat_ElseNullForEscapeSequences=newLineBecomesWhat_ElseNullForEscapeSequences.replace("\r","\\r");
				newLineBecomesWhat_ElseNullForEscapeSequences=newLineBecomesWhat_ElseNullForEscapeSequences.replace("\n","\\n");
				sCSVField=sCSVField.replace("\r\n", newLineBecomesWhat_ElseNullForEscapeSequences);
				sCSVField=sCSVField.replace("\r", newLineBecomesWhat_ElseNullForEscapeSequences);
				sCSVField=sCSVField.replace("\n", newLineBecomesWhat_ElseNullForEscapeSequences);
			}
			else {
				sCSVField=sCSVField.replace("\r","\\r");
				sCSVField=sCSVField.replace("\n","\\n");
			}
			if (sCSVField.contains(fieldDelimiterString)) {
				sCSVField=oneQuote+sCSVField+oneQuote;
			}
		}
		catch (Exception e) {
			main.logWriteLine("Could not finish LiteralFieldToDelimitedField:"+e.getMessage());
		}
		return sCSVField;
	}//end LiteralFieldToDelimitedField

}
