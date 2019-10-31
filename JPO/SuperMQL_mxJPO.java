import matrix.db.Context;

public class SuperMQL_mxJPO
{
	public SuperMQL_mxJPO(Context context, String[] args) throws Exception
	{
		
	}
	
	public int mxMain(Context context, String[] args) throws Exception
	{
		new com.fervort.supermql.SuperMQLMain().invokeSuperMQL(context, args);
		return 0;
	}
	
}