package com.rageps.util;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Iterator;

public class DatabaseUtils {
    /***
     * Generate a MySQL insert script based on table name and its columns names.
     *
     * @param tableName: Name of table.
     * @param tableColums: Array of columns names.
     * @return: An insert statement script.
     */
    public static String generateInsertStatement(String tableName, String... tableColums) {
        StringBuilder sb = new StringBuilder("INSERT INTO `" + tableName + "`");
        sb.append("(");
        for (String column : tableColums) {
            sb.append('`').append(column).append("`, ");
        }
        sb.delete(sb.length() - 2, sb.length());
        sb.append(")");
        sb.append("VALUES");
        sb.append("(");
        for (int i = 0; i < tableColums.length; i++) {
            sb.append("?");
            if (i + 1 < tableColums.length)
                sb.append(", ");
        }
        sb.append(");");
        return sb.toString();
    }

    public static String getDataStrucutre(Iterator<?> iterator, boolean updateType) {
        StringBuilder sb = new StringBuilder();
        while (iterator.hasNext()) {
            Object object = iterator.next();
            sb.append('`').append(object.toString()).append(updateType ? "` = ?, " : "`, ");
        }
        sb.delete(sb.length() - 2, sb.length());
        return sb.toString();
    }

    public static String getUpdateStrucutre(String[] keys) {
        StringBuilder sb = new StringBuilder();
        for (String string : keys) {
            if (string != null) {
                sb.append('`').append(string).append("` = ?, ");
            }
        }
        sb.delete(sb.length() - 2, sb.length());
        return sb.toString();
    }

    public static String getSelectStructure(ArrayList<String> keys) {
        StringBuilder sb = new StringBuilder();
        for (String string : keys) {
            if (string != null) {
                sb.append('`').append(string).append("`, ");
            }
        }
        sb.delete(sb.length() - 2, sb.length());
        return sb.toString();
    }

    public static String getSelectStructure(String[] keys) {
        StringBuffer sb = new StringBuffer();
        for (String string : keys) {
            if (string != null) {
                sb.append('`' + string + "`, ");
            }
        }
        sb.delete(sb.length() - 2, sb.length());
        return sb.toString();
    }
    public static JSONArray convertToJSON(ResultSet resultSet)
            throws Exception {
        JSONArray jsonArray = new JSONArray();
        while (resultSet.next()) {
            int total_columns = resultSet.getMetaData().getColumnCount();
            JSONObject obj = new JSONObject();
            for (int i = 0; i < total_columns; i++) {
                obj.put(resultSet.getMetaData().getColumnLabel(i + 1).toLowerCase(), resultSet.getObject(i + 1));
            }
            jsonArray.put(obj);
        }
        return jsonArray;
    }
}
