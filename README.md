public static  List<Map<String, Object> > runQueryGetResult(String query) throws Exception{
		
		List<Map<String, Object> > rowList = new ArrayList<>(); 
		
		st = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY) ; 		
		rs = st.executeQuery(query) ;
		
		ResultSetMetaData rsmd = rs.getMetaData() ; 
		while ( rs.next() ) {
			
			Map<String,Object> colNameValueMap = new HashMap<>() ; 
			
			for (int i = 1; i <= rsmd.getColumnCount() ; i++) {
				
				System.out.println( "ColumnName : " + rsmd.getColumnName(i));
				System.out.println( "value  :     " + rs.getObject(i) );
				colNameValueMap.put(rsmd.getColumnName(i), rs.getObject(i) ) ; 
				
			}
			
			rowList.add(colNameValueMap); 
			
			
		}
		
		return rowList ; 
		
	}
