package nc.uap.lfw.jsutil.jstools;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CssTextCompressor implements ITextCompressor {

	public String compress(String input) {
		Pattern pattern = Pattern.compile("(//.*)|(/\\*[\\d\\D]+?\\*/)|([\\r\\n]+)", Pattern.CANON_EQ | Pattern.MULTILINE);
		Matcher matcher = pattern.matcher(new String(input).trim());
		StringBuffer resultBuf = new StringBuffer();
		while (matcher.find())
		{
			matcher.appendReplacement(resultBuf,"");
		}
		matcher.appendTail(resultBuf);
		input = processBlank(resultBuf.toString());
		input = input.replaceAll("}", "}\n");
		return input;
	}
	
	private String processBlank(String oriStr)
	{
		int matchType = 0;
		StringBuffer buf = new StringBuffer();
		int sign = 0;
		for(int i = 0; i < oriStr.length(); i ++)
		{
			char ch = oriStr.charAt(i);
			char prech = '\0';
			//char postch = '\0';
			if(i != 0)
				prech = buf.charAt(buf.length() - 1);
			
			//匹配单引号
			if(ch == '\'')
			{
				if(matchType == 0)
					matchType = 1;
				else if(matchType == 1)
					matchType = 0;
			}
			//匹配双引号
			else if(ch == '"')
			{
				if(matchType == 0)
					matchType = 2;
				else if(matchType == 2)
					matchType = 0;
			}
			//如果位于引号之外
			else if(matchType == 0)
			{
				//如果是tab，替换为一个空白
				if(ch == '\t')
				{
					if(sign == 0)
					{
						sign = 1;
						ch = ' ';
					}
					else
						ch = '\0';
				}
				
				else if(ch == ' ')
				{
					if(sign == 0)
					{
						sign = 1;
					}
					else
						ch = '\0';
				}
				else if(ch == ';')
					sign = 1;
				//如果是=号，且前一个是空格，则删除前面的空格
				else if(ch== ',' ||ch==':'||ch=='&'|| ch == '=' || ch == '<' || ch=='>' || ch=='+' || ch=='-' || ch=='*' || ch=='/' )
				{
					if(prech == ' ' || prech == '\t')
						buf.deleteCharAt(buf.toString().length() - 1);
					sign = 1;
				}
				else if(ch == '}' || ch == '{')
				{
					if(prech == ' ' || prech == '\t')
						buf.deleteCharAt(buf.toString().length() - 1);
					sign = 1;
				}
				else
					sign = 0;
			}
			if(ch != '\0')
				buf.append(ch);
		
		}
		return buf.toString();
	}

}
