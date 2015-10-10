package ajeffrey.teaching.dining;

import java.util.*;

/**
 * A token pook to fix the dining philosophers problem.
 * @author Alan Jeffrey and Yuancheng Zhang
 * @version 1.0.1
 */

public interface TokenPool 
{
	public void addNewToken (); // Parameters if/as necessary 
	public Token getToken ();  // Parameters if/as necessary 
	public void putToken (Token t);
	public static final PhilosopherFactory factory = new TokenPhilosopherFactoryImpl ();
}

class TokenPoolImpl implements TokenPool
{
	static int tokenTotalNum = -1;
	private ArrayList<Token> tokenList = new ArrayList<Token>();

	public void addNewToken ()
	{
		tokenTotalNum++;
		if(tokenTotalNum > 0)
		{
			tokenList.add(new Token(tokenTotalNum));
		}
	} 
	public Token getToken () 
	{	if(tokenList.size() == 0) 
		{
			return null;
		}
		else
		{
			return tokenList.remove(0);
		}
	}
	public void putToken (Token t)
	{
		tokenList.add(t);
	}
}

class Token
{
	final private int id;
	public Token (int id)
	{
		this.id = id;
	}
	public String getId()
	{
		return String.valueOf(this.id);
	}
}