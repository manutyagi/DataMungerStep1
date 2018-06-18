/*
 * Class Name   : DataMunger.java
 * Author       : Manu Tyagi
 * Description  : The following methods are used to manipulate queries using split(),trim(),
 * 				  toLowerCase() etc. java string methods and obtain various **components/parts**
 * 			      of the query like **selected fields, conditional parts, aggregate fields, 
 *                groupBy fields, OrderBy field** 
 */
package com.stackroute.datamunger;

import java.util.ArrayList;

public class DataMunger {

	/* Logic :
	 * Input string has been converted to lowercase using lowerCase() string method and then
	 * it has been split over spaces using split() method.
	 */

	public String[] getSplitStrings(String queryString) {
		String[] strSpace = queryString.toLowerCase().split(" ");		
		return strSpace;
	}
	
	/* Logic :
	 * Initially the input query string has been split on 'from' using split().The obtained
	 * array has two elements.The second element is trimmed for any leading/trailing spaces
	 * and then the obtained string is split on 'spaces' and another array is obtained whose
	 * zeroth element is trimmed and the resulting string gives the filename.
	 */

	public String getFileName(String queryString) {
		String strFrom = queryString.split("from")[1].trim();		
		String strFileName = strFrom.split(" ")[0].trim();				 
		return strFileName;	
	}

	/* Logic :
	 * If the query contains where clause,it is split using split() method at the 'where' 
	 * clause.If not,entire query is returned.
	 */
	
	public String getBaseQuery(String queryString) {
		String strBaseQuery = "";
		if(queryString.contains("where")) {
			strBaseQuery = queryString.toLowerCase().split("where")[0].trim();
		}else if(queryString.contains("group by")||queryString.contains("order by")) {
			strBaseQuery = queryString.toLowerCase().split("group by|order by")[0].trim();
		}
		else {
			strBaseQuery = queryString;
		}
		return strBaseQuery;
	}
	
	/* Logic :
	 * Initially the query is split at 'select',selecting the first element of the 
	 * obtained array and then fields are obtained by splitting the remaining query 
	 * at 'from' and selecting the zeroth element of the obtained array.Now we can
	 * split that string at 'commas' to obtain an array of fields.
	 */
	
	public String[] getFields(String queryString) {
		String strSelect = queryString.toLowerCase().split("select")[1].trim();
		String strFrom = strSelect.split("from")[0].trim();
		String[] strField = strFrom.split(",");
		return strField;
	}
	
	/* Logic :
	 * Here an OR operator is used inside the split method to split the string at 'where',
	 * 'group by' or 'order by' clause ,whatever be present inside the query.Else null is
	 * returned.
	 */
	
	public String getConditionsPartQuery(String queryString) {
		String strConditions = "";
		if(queryString.contains("where")) {
			strConditions = queryString.toLowerCase().trim().split("where|group by|order by")[1].trim();
		}else {
			strConditions = null;
		}
		return strConditions;
	}

	/* Logic :
	 * Here first an OR operator is used inside the split method to split the string at
	 * 'where','group by' or 'order by' clause ,whatever be present inside the query.Then
	 * the remaining string obtained contains AND/OR conditions.So the string is split
	 * at AND/OR and thus condition(s) are easily extracted.Leading and trailing spaces
	 * with 'and' and 'or' (inside split()) have been kept so as to avoid conflicts with
	 * words like Bangalore and Alexander which have 'or' and 'and' as substrings.
	 */

	public String[] getConditions(String queryString) {
		String str = queryString.toLowerCase();
		String[] whereConditions = null;
		if(str.contains("where")) {
			String whereQuery = str.split("where|group by|order by")[1].trim();
			whereConditions = whereQuery.split(" and | or ");
		}else {
			return null;
		}
		return whereConditions;
	}
	
	/* Logic :
	 * Initially the input query is split at 'where' clause and the first element is 
	 * again split at spaces and thus we obtain an array that has all the words after
	 * where clause which also includes and/or/not operators.The obtained array is looped
	 * and we check whether an element matches 'and' ,'or' or 'not' keywords.If match is 
	 * found ,the element is stored in another array whose length will be defined by the
	 * number of and/or/not conditions.Finally the obtained array is returned. 
	 */

	public String[] getLogicalOperators(String queryString) {
		String str = queryString.toLowerCase();
		String[] strAndOr = null;
		String[] strOperator = new String[100];
		if(str.contains("where")) {
			String strwhere = str.split("where")[1].trim();
			strAndOr = strwhere.split(" ");
			int j = 0;
			for(int i = 0;i < strAndOr.length;i++) {
				if(strAndOr[i].equals("and")||strAndOr[i].equals("or")||strAndOr[i].equals("not")) {
					strOperator[j] = strAndOr[i];
					j++;
				}
			}
			String[] returnOperator = new String[j];
			for(int i = 0;i < j;i++) {
				returnOperator[i] = strOperator[i];			
				}
			return returnOperator;
		}
		else {
			return null;
		}	
	}

	/* Logic :
	 * Here first of all it is checked whether the input query has both where and 
	 * order by clause.If true,the string is split at 'where' clause and the first
	 * element of the obtained array is again split at 'order by' clause.The first
	 * element of the obtained array has group by comma-separated fields and thus it
	 * is split at 'commas' and we can obtain the order by fields.Next condition checks
	 * if there is no where clause and just order by clause.In this case,the splitting
	 * is done on 'order by' clause and the first element of the obtained array is split 
	 * at 'commas' to obtain the order by fields.In all other cases null is returned.
	 */

	public String[] getOrderByFields(String queryString) {
		String str = queryString.toLowerCase();
		String[] strOrderByFields = null;
		if(str.contains("where")&&(str.contains("order by"))) {
			String strWhere = str.split("where")[1].trim();
			String strOrderByString = strWhere.split("order by")[1].trim();
			strOrderByFields = strOrderByString.split(",");
			return strOrderByFields;
		}else if(str.contains("order by")){
			String strNotWhere = str.split("order by")[1].trim();
			strOrderByFields = strNotWhere.split(",");
			return strOrderByFields;
		}else {
			return null;
		}
	}

	/* Logic :
	 * Here first of all it is checked whether the input query has both where and 
	 * group by clause.If true,the string is split at 'where' clause and the first
	 * element of the obtained array is again split at 'group by' clause.The first
	 * element of the obtained array has group by comma-separated fields and thus it
	 * is split at 'commas' and we can obtain the group by fields.Next condition checks
	 * if there is no where clause and just group by clause.In this case,the splitting
	 * is done on 'group by' clause and the first element of the obtained array is split 
	 * at 'commas' to obtain the group by fields.In all other cases null is returned.
	 */

	public String[] getGroupByFields(String queryString) {
		String str = queryString.toLowerCase();
		String[] strGroupByFields = null;
		if(str.contains("where")&&(str.contains("group by"))) {
			String whereString = str.split("where")[1].trim();
			String groupByString = whereString.split("group by")[1].trim();
			strGroupByFields = groupByString.split(",");
			return strGroupByFields;
		}else if(str.contains("group by")){
			String notWhereString = str.split("group by")[1].trim();
			strGroupByFields = notWhereString.split(",");
			return strGroupByFields;
		}else {
			return null;
		}
	}

	/* Logic :(Using ArrayList)
	 * Here first of all the input string is split at 'from' and zeroth element 
	 * of the obtained array is split at 'select' and thus we obtain a string 
	 * which has aggregate functions(and maybe some other fields) separated by
	 * commas.Then we split the string at 'commas' and obtain an array containing
	 * aggregate function and other fields.An ArrayList is formed.Inside for loop
	 * (over length of above obtained array),presence of '(' is matched and if any 
	 * array element has '(' ,it is added to the list.Size of list is obtained using 
	 * size() method and if it is zero null is returned otherwise another array is 
	 * formed whose size is equal to the list's size and each of the list's element
	 * is added to the array using get() method and finally the array is returned.
	 */	
	
	public String[] getAggregateFunctions(String queryString) {
		String strFrom = queryString.toLowerCase().split("from")[0].trim();
		String strSelect = strFrom.split("select")[1].trim();
		String[] strFieldsAndAggrfunc = strSelect.split(",");
		ArrayList<String> myAggrFuncList = new  ArrayList<String>();
		for(int i = 0;i < strFieldsAndAggrfunc.length;i++) {
			if(strFieldsAndAggrfunc[i].contains("("))
				myAggrFuncList.add(strFieldsAndAggrfunc[i].trim());
		}
		int listSize = myAggrFuncList.size();
		if(listSize == 0) {
			return null;
		}else {
		String[] strAggrFunc = new String[listSize];
		for(int j = 0;j < listSize;j++) {
			strAggrFunc[j] = myAggrFuncList.get(j);
		}
		return strAggrFunc;
		}
	}
	
}