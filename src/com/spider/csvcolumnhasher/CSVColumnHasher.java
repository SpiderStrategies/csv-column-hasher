package com.spider.csvcolumnhasher;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.base.Joiner;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.spec.KeySpec;

import au.com.bytecode.opencsv.CSVReader;

public class CSVColumnHasher {

	public static void main(String[] args) throws Exception {
		String filePath = args[0];
		String columnToHash = args[1].toLowerCase();
		String salt = args[2]; //due to project requirements salt is not unique per-record
		int iterations = Integer.parseInt(args[3]);
		DataSet ds = read(new FileInputStream(filePath));
		hashColumnValues(ds, columnToHash, salt, iterations);
		
		System.out.println(Joiner.on(",").join(ds.columns));
		
		for(Map<String,String> record: ds.data) {
			String comma = "";
			for(String column: ds.columns) {
				System.out.print(comma + "\"" + record.get(column) + "\"");
				comma = ",";
			}
			System.out.println("");
		}
	}
	
	private static void hashColumnValues(DataSet ds, String columnToHash, String salt, int iterations) throws Exception {
		for(Map<String,String> record: ds.data) {
			String columnVal = (String) record.get(columnToHash);
			String hashedColumnVal = getSaltedHash(columnVal, salt.getBytes(), iterations);
			record.put(columnToHash, hashedColumnVal);
		}
	}
	
	public static String getSaltedHash(String str, byte[] salt, int iterations) throws Exception {
		if(str == null || str.equals("")) {
			return "";
		}

		KeySpec spec = new PBEKeySpec(str.toCharArray(), salt, iterations, 160);
		SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
		byte[] bytes = f.generateSecret(spec).getEncoded();
		
		StringBuilder sb = new StringBuilder();
		for(int i=0; i< bytes.length ;i++) {
			sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
		}
		
		return sb.toString();
	}
	
	private static DataSet read(InputStream is) throws IOException {
		CSVReader reader = null;
		try {
			reader = new CSVReader(new InputStreamReader(is), ',');
			DataSet ds = new DataSet();
			List<String[]> allRows = reader.readAll();

			List<String> columnNames = new ArrayList<>();
			for (String columnName : allRows.get(0)) {
				columnNames.add(columnName.trim().toLowerCase());
			}
			ds.columns.addAll(columnNames);

			for (int i = 1; i<allRows.size(); i++) {
				Map<String, String> rowData = new HashMap<>();
				String[] csvRowData = allRows.get(i);
				
				for (int j = 0; j<csvRowData.length; j++) {
					String csvRowColumnValue = csvRowData[j];
					rowData.put(ds.columns.get(j), csvRowColumnValue);
				}
				ds.data.add(rowData);
			}
			return ds;
		} finally {
			reader.close();
			is.close();
		}
	}

}
