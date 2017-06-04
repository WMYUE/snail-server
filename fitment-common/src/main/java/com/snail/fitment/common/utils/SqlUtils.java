package com.snail.fitment.common.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.dialect.postgresql.visitor.PGSchemaStatVisitor;
import com.alibaba.druid.stat.TableStat;
import com.alibaba.druid.stat.TableStat.Column;
import com.alibaba.druid.stat.TableStat.Name;
import com.alibaba.druid.util.JdbcConstants;

public class SqlUtils 
{
	public static List<String> getTableName(String sql) {
		List<String> tableNames = new ArrayList<>();
		String dbType = JdbcConstants.MYSQL;
		List<SQLStatement> stmtList = SQLUtils.parseStatements(sql, dbType);
		for (int i = 0; i < stmtList.size(); i++) {	 
            SQLStatement stmt = stmtList.get(i);
            PGSchemaStatVisitor visitor = new PGSchemaStatVisitor();
            stmt.accept(visitor);
            Map<Name, TableStat> tabmap = visitor.getTables();
            for (Iterator<Name> iterator = tabmap.keySet().iterator(); iterator.hasNext();) {
                Name name = (Name) iterator.next();
                tableNames.add(name.toString());
            }
		}
		return tableNames;
	}
	public static void main(String[] args) {
		String sql="select count(*) from `table1` a where a.id=123";
		List<String> tablenames = getTableName(sql);
		String dbType = JdbcConstants.MYSQL;
		String result = SQLUtils.format(sql, dbType);
		
		 List<SQLStatement> stmtList = SQLUtils.parseStatements(sql, dbType);
		//解析出的独立语句的个数
	        System.out.println("size is:" + stmtList.size());
	        for (int i = 0; i < stmtList.size(); i++) {
	 
	            SQLStatement stmt = stmtList.get(i);
	            
	            PGSchemaStatVisitor visitor = new PGSchemaStatVisitor();
	            stmt.accept(visitor);
	            Map<String, String> aliasmap = visitor.getAliasMap();
	            for (Iterator<String> iterator = aliasmap.keySet().iterator(); iterator.hasNext();) {
	                String key = iterator.next().toString();
	                System.out.println("[ALIAS]" + key + " - " + aliasmap.get(key));
	            }
	            Set<Column> groupby_col = visitor.getGroupByColumns();
	            //
	            for (Iterator<Column> iterator = groupby_col.iterator(); iterator.hasNext();) {
	                Column column = (Column) iterator.next();
	                System.out.println("[GROUP]" + column.toString());
	            }
	            //获取表名称
	            System.out.println("table names:");
	            Map<Name, TableStat> tabmap = visitor.getTables();
	            for (Iterator<Name> iterator = tabmap.keySet().iterator(); iterator.hasNext();) {
	                Name name = (Name) iterator.next();
	                System.out.println(name.toString() + " - " + tabmap.get(name).toString());
	            }
	            //System.out.println("Tables : " + visitor.getCurrentTable());
	            //获取操作方法名称,依赖于表名称
	            System.out.println("Manipulation : " + visitor.getTables());
	            //获取字段名称
	            System.out.println("fields : " + visitor.getColumns());
	        }
	}
}
